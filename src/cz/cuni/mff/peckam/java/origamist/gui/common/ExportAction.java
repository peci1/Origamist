package cz.cuni.mff.peckam.java.origamist.gui.common;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.io.File;
import java.text.MessageFormat;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.KeyStroke;
import javax.swing.SwingWorker;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.log4j.Logger;

import cz.cuni.mff.peckam.java.origamist.model.Origami;
import cz.cuni.mff.peckam.java.origamist.services.ServiceLocator;
import cz.cuni.mff.peckam.java.origamist.services.TooltipFactory;
import cz.cuni.mff.peckam.java.origamist.services.interfaces.ConfigurationManager;
import cz.cuni.mff.peckam.java.origamist.services.interfaces.OrigamiHandler;
import cz.cuni.mff.peckam.java.origamist.utils.ExportFormat;
import cz.cuni.mff.peckam.java.origamist.utils.ExportOptions;

/**
 * Exports the currently displayed origami to the given format.
 * 
 * @author Martin Pecka
 */
public class ExportAction extends AbstractAction
{
    /** */
    private static final long serialVersionUID = -399462365929673938L;

    /** The format to export to. */
    protected ExportFormat    format;

    /** The origami to export. */
    protected Origami         origami;

    /**
     * @param format The format to export to.
     */
    public ExportAction(Origami origami, ExportFormat format)
    {
        this.format = format;
        this.origami = origami;
    }

    @Override
    public void actionPerformed(final ActionEvent e)
    {
        beforeAction(e);

        final ResourceBundle appMessages = ResourceBundle.getBundle("application",
                ServiceLocator.get(ConfigurationManager.class).get().getLocale());

        final Component parent;
        if (e.getSource() instanceof Component)
            parent = (Component) e.getSource();
        else
            parent = null;

        JFileChooser chooser = new JFileChooser();
        File defaultFile = ServiceLocator.get(ConfigurationManager.class).get().getLastExportPath().getParentFile();
        chooser.setCurrentDirectory(defaultFile);
        chooser.setFileFilter(new FileNameExtensionFilter("*." + format.toString().toLowerCase(), format.toString()));
        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        chooser.setDialogType(JFileChooser.SAVE_DIALOG);
        chooser.setApproveButtonText(appMessages.getString("exportDialog.approve"));
        chooser.setApproveButtonMnemonic(KeyStroke.getKeyStroke(appMessages.getString("exportDialog.approve.mnemonic"))
                .getKeyCode());
        chooser.setApproveButtonToolTipText(ServiceLocator.get(TooltipFactory.class).getDecorated(
                appMessages.getString("exportDialog.approve.tooltip.message"),
                appMessages.getString("exportDialog.approve.tooltip.title"), "save.png",
                KeyStroke.getKeyStroke("alt " + appMessages.getString("exportDialog.approve.mnemonic"))));
        if (chooser.showDialog(parent, null) == JFileChooser.APPROVE_OPTION) {
            chooserApproved(e);

            File f = chooser.getSelectedFile();
            if (!chooser.accept(f)) {
                f = new File(f.toString() + "." + format.toString().toLowerCase());
            }
            ServiceLocator.get(ConfigurationManager.class).get().setLastExportPath(f);

            if (f.exists()) {
                if (JOptionPane.showConfirmDialog(parent,
                        MessageFormat.format(appMessages.getString("exportDialog.overwrite"), new Object[] { f }),
                        appMessages.getString("exportDialog.overwrite.title"), JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE, null) != JOptionPane.YES_OPTION) {
                    return;
                }
            }

            final File file = f;

            final ExportOptions options = format.askForOptions(origami);

            final JDialog progressDialog = new JDialog();
            final JProgressBar bar = new JProgressBar(0, format.getNumOfProgressChunks(origami, options));
            bar.setStringPainted(true);
            bar.setString(appMessages.getString("exporting.no.percentage"));
            progressDialog.getContentPane().setLayout(new FlowLayout());
            progressDialog.setUndecorated(true);
            progressDialog.setAlwaysOnTop(true);
            progressDialog.setLocationRelativeTo(null);

            final SwingWorker<Set<File>, Void> worker = new SwingWorker<Set<File>, Void>() {

                private int chunksReceived = 0;

                @Override
                protected Set<File> doInBackground() throws Exception
                {
                    final Runnable progressCallback = new Runnable() {
                        @Override
                        public void run()
                        {
                            publish(new Void[] { null });
                            if (isCancelled())
                                throw new RuntimeException();
                        }
                    };

                    return ServiceLocator.get(OrigamiHandler.class).export(origami, file, format, options,
                            progressCallback);
                }

                @Override
                protected void process(List<Void> chunks)
                {
                    chunksReceived += chunks.size();
                    bar.setValue(chunksReceived);
                    bar.setString(MessageFormat.format(appMessages.getString("exporting.percentage"),
                            bar.getPercentComplete()));
                }

                @Override
                protected void done()
                {
                    progressDialog.setVisible(false);
                    progressDialog.dispose();
                    try {
                        if (!isCancelled()) {
                            Set<File> resultFiles = get();
                            StringBuilder b = new StringBuilder();
                            for (File f : resultFiles)
                                b.append("\n").append(f.toString());
                            JOptionPane.showMessageDialog(parent, MessageFormat.format(
                                    appMessages.getString("exportSuccessful.message"), new Object[] { b.toString(),
                                            resultFiles.size() }), appMessages.getString("exportSuccessful.title"),
                                    JOptionPane.INFORMATION_MESSAGE, null);
                        }
                    } catch (HeadlessException e) {
                        JOptionPane.showMessageDialog(parent, MessageFormat.format(
                                appMessages.getString("failedToExport.message"), new Object[] { file.toString() }),
                                appMessages.getString("failedToExport.title"), JOptionPane.ERROR_MESSAGE, null);
                        Logger.getLogger("application").warn("Unable to export origami.", e);
                    } catch (InterruptedException e) {
                        JOptionPane.showMessageDialog(parent, MessageFormat.format(
                                appMessages.getString("failedToExport.message"), new Object[] { file.toString() }),
                                appMessages.getString("failedToExport.title"), JOptionPane.ERROR_MESSAGE, null);
                        Logger.getLogger("application").warn("Unable to export origami.", e);
                    } catch (ExecutionException e) {
                        JOptionPane.showMessageDialog(parent, MessageFormat.format(
                                appMessages.getString("failedToExport.message"), new Object[] { file.toString() }),
                                appMessages.getString("failedToExport.title"), JOptionPane.ERROR_MESSAGE, null);
                        Logger.getLogger("application").warn("Unable to export origami.", e);
                    }
                    ExportAction.this.done(e);
                }

            };

            progressDialog.getContentPane().add(bar);
            JButton cancel = new JButton();
            cancel.setAction(new AbstractAction() {
                /** */
                private static final long serialVersionUID = 104733262578076493L;

                @Override
                public void actionPerformed(ActionEvent e)
                {
                    worker.cancel(true);
                }
            });
            cancel.setText(appMessages.getString("buttons.cancel"));
            progressDialog.getContentPane().add(cancel);

            progressDialog.pack();

            progressDialog.setVisible(true);
            worker.execute();
        } else {
            if (e.getSource() instanceof JComponent)
                ((JComponent) e.getSource()).setCursor(Cursor.getDefaultCursor());
        }
    }

    /**
     * This method gets called before any action from this action is taken.
     */
    protected void beforeAction(ActionEvent evt)
    {

    }

    /**
     * This method gets called when the user approves the file chooser dialog.
     */
    protected void chooserApproved(ActionEvent evt)
    {

    }

    /**
     * This method gets called when the export action has been done.
     */
    protected void done(ActionEvent evt)
    {

    }

}
/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.gui.editor;

import java.applet.AppletContext;
import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JRootPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.origamist.BackgroundImageSupport;
import javax.swing.origamist.BackgroundImageSupport.BackgroundRepeat;
import javax.swing.origamist.BoundButtonGroup;
import javax.swing.origamist.JDropDownButton;
import javax.swing.origamist.JDropDownButtonReflectingSelectionGroup;
import javax.swing.origamist.JToggleMenuItem;
import javax.swing.origamist.JToolBarWithBgImage;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import cz.cuni.mff.peckam.java.origamist.exceptions.UnsupportedDataFormatException;
import cz.cuni.mff.peckam.java.origamist.gui.CommonGui;
import cz.cuni.mff.peckam.java.origamist.logging.GUIAppender;
import cz.cuni.mff.peckam.java.origamist.model.Origami;
import cz.cuni.mff.peckam.java.origamist.services.ServiceLocator;
import cz.cuni.mff.peckam.java.origamist.services.TooltipFactory;
import cz.cuni.mff.peckam.java.origamist.services.interfaces.ConfigurationManager;
import cz.cuni.mff.peckam.java.origamist.services.interfaces.OrigamiHandler;

/**
 * The editor of the origami model. <br />
 * <br />
 * Properties handled by this class (events are fired when they change - but not at startup):
 * <ul>
 * </ul>
 * 
 * This applet is intended to be started using a bootstrapper that will add support for Java3D without the need to
 * install it on the client computer.
 * 
 * @author Martin Pecka
 */
public class OrigamiEditor extends CommonGui
{
    private static final long     serialVersionUID        = -6853141518719373854L;

    /** The bootstrapper that has started this applet, or <code>null</code>, if it has not been bootstrapped. */
    protected JApplet             bootstrap               = null;

    /** The currently displayed origami. May be <code>null</code>. */
    protected Origami             origami                 = null;

    /** Reflects whether alternative action buttons are shown. */
    protected boolean             alternativeActionsShown = false;

    /** The main application toolbar. */
    protected JToolBarWithBgImage toolbar                 = null;

    /** Toolbar buttons for model operations. */
    protected JToggleButton       operationMountainFold, operationValleyFold, operationMountainFoldUnfold,
            operationValleyFoldUnfold, operationThunderboltFoldMountainFirst, operationThunderboltFoldValleyFirst,
            operationTurnOver, operationRotate, operationPull, operationCrimpFoldInside, operationCrimpFoldOutside,
            operationOpen, operationReverseFoldInside, operationReverseFoldOutside, operationRepeatAction,
            operationMark;

    /** Toolbar buttons for model operations. */
    protected JToggleMenuItem     operationRabbitFold, operationSquashFold;

    /**
     * Instantiate the origami viewer without a bootstrapper.
     */
    public OrigamiEditor()
    {
        this(null);
    }

    /**
     * Instanitate the origami viewer with the given bootstrapper.
     * 
     * @param bootstrap The bootstrapper that starts this applet.
     */
    public OrigamiEditor(JApplet bootstrap)
    {
        super();
        this.bootstrap = bootstrap;

        addGlobalKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e)
            {
                super.keyPressed(e);
                if (e.getKeyCode() == KeyEvent.VK_ALT) {
                    showAlternativeActions(!alternativeActionsShown);
                    e.consume();
                }
            }
        });
    }

    /**
     * Create and setup all the form components.
     */
    @Override
    protected void createComponents()
    {
        toolbar = createToolbar();
    }

    /**
     * Setup the form layout.
     */
    @Override
    protected void buildLayout()
    {
        setLayout(new FormLayout("pref:grow", "pref"));

        CellConstraints cc = new CellConstraints();

        add(toolbar, cc.xy(1, 1));
    }

    /**
     * Show or hide (depending on the parameter) alternative diagram actions - eg. display a valley fold button instead
     * of mountain fold button and so on.
     * 
     * @param show If <code>true</code>, show alternatives, otherwise show the default buttons.
     */
    protected void showAlternativeActions(boolean show)
    {
        alternativeActionsShown = show;

        operationMountainFold.setVisible(!show);
        operationValleyFold.setVisible(show);

        operationMountainFoldUnfold.setVisible(!show);
        operationValleyFoldUnfold.setVisible(show);

        operationThunderboltFoldMountainFirst.setVisible(!show);
        operationThunderboltFoldValleyFirst.setVisible(show);

        operationTurnOver.setVisible(!show);
        operationRotate.setVisible(show);

        operationCrimpFoldInside.setVisible(!show);
        operationCrimpFoldOutside.setVisible(show);

        operationReverseFoldInside.setVisible(!show);
        operationReverseFoldOutside.setVisible(show);
    }

    /**
     * @return The main application toolbar.
     */
    protected JToolBarWithBgImage createToolbar()
    {
        JToolBarWithBgImage toolbar = new JToolBarWithBgImage("editor");
        toolbar.setFloatable(false);
        toolbar.setBackground(new Color(231, 231, 184, 230));
        toolbar.setBackgroundImage(new BackgroundImageSupport(getClass()
                .getResource("/resources/images/tooltip-bg.png"), toolbar, 0, 0, BackgroundRepeat.REPEAT_X));

        toolbar.add(toolbar.createToolbarButton(new NewFileAction(), "menu.new", "new-32.png"));
        JDropDownButton open = toolbar.createToolbarDropdownButton(null, "menu.open", "open-32.png");
        toolbar.add(open);
        open.addComponent(toolbar.createToolbarDropdownItem(new OpenFileAction(), "menu.open.file", "open-file-32.png"));
        open.addComponent(toolbar.createToolbarDropdownItem(new OpenURLAction(), "menu.open.url", "open-url-32.png"));

        toolbar.add(new JToolBar.Separator());

        toolbar.add(toolbar.createToolbarButton(new SettingsAction(), "menu.settings", "settings-32.png"));

        toolbar.add(new JToolBar.Separator());

        BoundButtonGroup operationGroup = new BoundButtonGroup();

        toolbar.add(operationMountainFold = toolbar.createToolbarItem(new JToggleButton(), null,
                "menu.operation.mountain", "folds/mountain-32.png"));
        toolbar.add(operationValleyFold = toolbar.createToolbarItem(new JToggleButton(), null, "menu.operation.valley",
                "folds/valley-32.png"));
        toolbar.add(operationMountainFoldUnfold = toolbar.createToolbarItem(new JToggleButton(), null,
                "menu.operation.mountainFoldUnfold", "folds/mountain-fold-unfold-32.png"));
        toolbar.add(operationValleyFoldUnfold = toolbar.createToolbarItem(new JToggleButton(), null,
                "menu.operation.valleyFoldUnfold", "folds/valley-fold-unfold-32.png"));
        toolbar.add(operationThunderboltFoldMountainFirst = toolbar.createToolbarItem(new JToggleButton(), null,
                "menu.operation.thunderboltMountainFirst", "folds/thunderbolt-mountain-first-32.png"));
        toolbar.add(operationThunderboltFoldValleyFirst = toolbar.createToolbarItem(new JToggleButton(), null,
                "menu.operation.thunderboltValleyFirst", "folds/thunderbolt-valley-first-32.png"));
        toolbar.add(operationTurnOver = toolbar.createToolbarItem(new JToggleButton(), null, "menu.operation.turnOver",
                "folds/turn-over-32.png"));
        toolbar.add(operationRotate = toolbar.createToolbarItem(new JToggleButton(), null, "menu.operation.rotate",
                "folds/rotate-32.png"));
        toolbar.add(operationPull = toolbar.createToolbarItem(new JToggleButton(), null, "menu.operation.pull",
                "folds/pull-32.png"));
        toolbar.add(operationCrimpFoldInside = toolbar.createToolbarItem(new JToggleButton(), null,
                "menu.operation.crimpInside", "folds/crimp-inside-32.png"));
        toolbar.add(operationCrimpFoldOutside = toolbar.createToolbarItem(new JToggleButton(), null,
                "menu.operation.crimpOutside", "folds/crimp-outside-32.png"));
        toolbar.add(operationOpen = toolbar.createToolbarItem(new JToggleButton(), null, "menu.operation.open",
                "folds/open-32.png"));
        toolbar.add(operationReverseFoldInside = toolbar.createToolbarItem(new JToggleButton(), null,
                "menu.operation.reverseInside", "folds/reverse-inside-32.png"));
        toolbar.add(operationReverseFoldOutside = toolbar.createToolbarItem(new JToggleButton(), null,
                "menu.operation.reverseOutside", "folds/reverse-outside-32.png"));
        toolbar.add(operationRepeatAction = toolbar.createToolbarItem(new JToggleButton(), null,
                "menu.operation.repeat", "folds/repeat-32.png"));

        operationRabbitFold = toolbar.createToolbarDropdownItem(new JToggleMenuItem(), null, "menu.operation.rabbit",
                "folds/rabbit-32.png");
        operationSquashFold = toolbar.createToolbarDropdownItem(new JToggleMenuItem(), null, "menu.operation.squash",
                "folds/squash-32.png");

        toolbar.add(operationMark = toolbar.createToolbarItem(new JToggleButton(), null, "menu.operation.mark",
                "folds/mark-32.png"));

        operationGroup.add(operationMountainFold);
        operationGroup.add(operationValleyFold);
        operationGroup.add(operationMountainFoldUnfold);
        operationGroup.add(operationValleyFoldUnfold);
        operationGroup.add(operationThunderboltFoldMountainFirst);
        operationGroup.add(operationThunderboltFoldValleyFirst);
        operationGroup.add(operationTurnOver);
        operationGroup.add(operationRotate);
        operationGroup.add(operationPull);
        operationGroup.add(operationCrimpFoldInside);
        operationGroup.add(operationCrimpFoldOutside);
        operationGroup.add(operationOpen);
        operationGroup.add(operationReverseFoldInside);
        operationGroup.add(operationReverseFoldOutside);
        operationGroup.add(operationRepeatAction);
        operationGroup.add(operationRabbitFold);
        operationGroup.add(operationSquashFold);
        operationGroup.add(operationMark);

        JDropDownButton advancedButton = toolbar.createToolbarDropdownButton(
                new JDropDownButtonReflectingSelectionGroup(new JButton(), operationGroup), null,
                "menu.operation.advanced", "empty-32.png");
        toolbar.add(advancedButton);
        advancedButton.addComponent(operationRabbitFold);
        advancedButton.addComponent(operationSquashFold);

        showAlternativeActions(false);

        return toolbar;
    }

    /**
     * @return the origami
     */
    public Origami getOrigami()
    {
        return origami;
    }

    /**
     * @param origami the origami to set
     */
    public void setOrigami(Origami origami)
    {
        this.origami = origami;
    }

    @Override
    protected void setupLoggers()
    {
        super.setupLoggers();

        Logger l = Logger.getLogger("editor");
        l.setResourceBundle(ResourceBundle.getBundle("editor", ServiceLocator.get(ConfigurationManager.class).get()
                .getLocale()));
        l.setLevel(Level.ALL);
        l.addAppender(new GUIAppender(this));
    }

    @Override
    protected void registerServices()
    {
        super.registerServices();
        ServiceLocator.add(OrigamiEditor.class, this);
    }

    // bootstrapping support

    @Override
    public JRootPane getRootPane()
    {
        if (bootstrap != null)
            return bootstrap.getRootPane();
        return super.getRootPane();
    }

    @Override
    public Container getContentPane()
    {
        if (bootstrap != null)
            return bootstrap.getContentPane();
        return super.getContentPane();
    }

    @Override
    public URL getDocumentBase()
    {
        if (bootstrap != null)
            return bootstrap.getDocumentBase();
        return super.getDocumentBase();
    }

    @Override
    public URL getCodeBase()
    {
        if (bootstrap != null)
            return bootstrap.getCodeBase();
        return super.getCodeBase();
    }

    @Override
    public String getParameter(String name)
    {
        if (bootstrap != null)
            return bootstrap.getParameter(name);
        return super.getParameter(name);
    }

    @Override
    public AppletContext getAppletContext()
    {
        if (bootstrap != null)
            return bootstrap.getAppletContext();
        return super.getAppletContext();
    }

    @Override
    public String getAppletInfo()
    {
        if (bootstrap != null)
            return bootstrap.getAppletInfo();
        return super.getAppletInfo();
    }

    /**
     * Shows the settings dialog.
     * 
     * @author Martin Pecka
     */
    protected class SettingsAction extends AbstractAction
    {
        /** */
        private static final long serialVersionUID = -583073126579360879L;

        @Override
        public void actionPerformed(ActionEvent e)
        {
            new SettingsFrame().setVisible(true);
        }
    }

    /**
     * Creates a new origami and sets it to be edited.
     * 
     * @author Martin Pecka
     */
    protected class NewFileAction extends AbstractAction
    {

        /** */
        private static final long serialVersionUID = -4947778589092614575L;

        @Override
        public void actionPerformed(ActionEvent e)
        {
            // TODO check if the currently edited origami is saved, if not, show a warning
            OrigamiPropertiesFrame propertiesFrame = new OrigamiPropertiesFrame(null);
            propertiesFrame.setVisible(true);
            if (propertiesFrame.getOrigami() != null) {
                setOrigami(propertiesFrame.getOrigami());
            }
        }

    }

    /**
     * Shows the open file from disk dialog.
     * 
     * @author Martin Pecka
     */
    protected class OpenFileAction extends AbstractAction
    {

        /** */
        private static final long serialVersionUID = -4079648565735159553L;

        @Override
        public void actionPerformed(ActionEvent e)
        {
            JFileChooser chooser = new JFileChooser();
            File currentDir = ServiceLocator.get(ConfigurationManager.class).get().getLastOpenPath().getParentFile();
            chooser.setCurrentDirectory(currentDir);
            chooser.setFileFilter(new FileNameExtensionFilter("*.xml", "XML"));
            chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            chooser.setDialogType(JFileChooser.OPEN_DIALOG);
            chooser.setApproveButtonText(appMessages.getString("openDialog.approve"));
            chooser.setApproveButtonMnemonic(KeyStroke.getKeyStroke(
                    appMessages.getString("openDialog.approve.mnemonic")).getKeyCode());
            chooser.setApproveButtonToolTipText(ServiceLocator.get(TooltipFactory.class).getDecorated(
                    appMessages.getString("openDialog.approve.tooltip.message"),
                    appMessages.getString("openDialog.approve.tooltip.title"), "open-file-32.png",
                    KeyStroke.getKeyStroke("alt " + appMessages.getString("openDialog.approve.mnemonic"))));
            if (chooser.showDialog(OrigamiEditor.this, null) == JFileChooser.APPROVE_OPTION) {
                File selected = chooser.getSelectedFile();
                ServiceLocator.get(ConfigurationManager.class).get().setLastOpenPath(selected);
                try {
                    Origami o = ServiceLocator.get(OrigamiHandler.class).loadModel(selected.toURI(), false);
                    setOrigami(o);
                } catch (UnsupportedDataFormatException e1) {
                    JOptionPane.showMessageDialog(rootPane,
                            appMessages.getString("exception.UnsupportedDataFormatException.loadModel"),
                            appMessages.getString("exception.UnsupportedDataFormatException.loadModel.title"),
                            JOptionPane.ERROR_MESSAGE);
                    Logger.getLogger("application").error(e1);
                } catch (IOException e1) {
                    JOptionPane.showMessageDialog(rootPane, appMessages.getString("exception.IOException.loadModel"),
                            appMessages.getString("exception.IOException.loadModel.title"), JOptionPane.ERROR_MESSAGE);
                    Logger.getLogger("application").error(e1);
                }
            }
        }

    }

    /**
     * Shows the open file from URL dialog.
     * 
     * @author Martin Pecka
     */
    protected class OpenURLAction extends AbstractAction
    {

        /** */
        private static final long serialVersionUID = -4948201244271857952L;

        @Override
        public void actionPerformed(ActionEvent e)
        {
            Object selected = null;
            URL selectedUrl = null;

            Object defaultURL = ServiceLocator.get(ConfigurationManager.class).get().getLastOpenURL();
            if (defaultURL == null)
                defaultURL = "http://";

            while ((selected = JOptionPane.showInputDialog(rootPane, appMessages.getString("openUrl.message"),
                    appMessages.getString("openUrl.title"), JOptionPane.QUESTION_MESSAGE, new ImageIcon(
                            OrigamiEditor.this.getClass().getResource("/resources/images/open-url.png")), null,
                    selected == null ? defaultURL : selected)) != null) {
                try {
                    selectedUrl = new URL(selected.toString());
                    if (!selectedUrl.toURI().isAbsolute()) {
                        throw new MalformedURLException("The URL to open must be absolute!");
                    }
                    break;
                } catch (MalformedURLException e1) {
                    JOptionPane.showMessageDialog(rootPane, appMessages.getString("openUrl.invalidUrl"),
                            appMessages.getString("openUrl.invalidUrl.title"), JOptionPane.ERROR_MESSAGE);
                } catch (URISyntaxException e1) {
                    JOptionPane.showMessageDialog(rootPane, appMessages.getString("openUrl.invalidUrl"),
                            appMessages.getString("openUrl.invalidUrl.title"), JOptionPane.ERROR_MESSAGE);
                }
            }

            // the user has cancelled the dialog
            if (selectedUrl == null)
                return;

            ServiceLocator.get(ConfigurationManager.class).get().setLastOpenURL(selectedUrl);

            try {
                Origami o = ServiceLocator.get(OrigamiHandler.class).loadModel(selectedUrl, false);
                setOrigami(o);
            } catch (UnsupportedDataFormatException e1) {
                JOptionPane.showMessageDialog(rootPane,
                        appMessages.getString("exception.UnsupportedDataFormatException.loadModel"),
                        appMessages.getString("exception.UnsupportedDataFormatException.loadModel.title"),
                        JOptionPane.ERROR_MESSAGE);
                Logger.getLogger("application").error(e1);
            } catch (IOException e1) {
                JOptionPane.showMessageDialog(rootPane, appMessages.getString("exception.IOException.loadModel"),
                        appMessages.getString("exception.IOException.loadModel.title"), JOptionPane.ERROR_MESSAGE);
                Logger.getLogger("application").error(e1);
            }
        }

    }
}
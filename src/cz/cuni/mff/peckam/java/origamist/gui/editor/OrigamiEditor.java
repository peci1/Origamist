/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.gui.editor;

import java.applet.AppletContext;
import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.MissingResourceException;
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
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.origamist.BackgroundImageSupport;
import javax.swing.origamist.BackgroundImageSupport.BackgroundRepeat;
import javax.swing.origamist.BoundButtonGroup;
import javax.swing.origamist.JDropDownButton;
import javax.swing.origamist.JDropDownButtonReflectingSelectionGroup;
import javax.swing.origamist.JStatusBar;
import javax.swing.origamist.JToggleMenuItem;
import javax.swing.origamist.JToolBarWithBgImage;
import javax.swing.origamist.MessageBar;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import cz.cuni.mff.peckam.java.origamist.exceptions.UnsupportedDataFormatException;
import cz.cuni.mff.peckam.java.origamist.gui.common.CommonGui;
import cz.cuni.mff.peckam.java.origamist.logging.GUIAppender;
import cz.cuni.mff.peckam.java.origamist.model.Origami;
import cz.cuni.mff.peckam.java.origamist.model.jaxb.Operations;
import cz.cuni.mff.peckam.java.origamist.services.ServiceLocator;
import cz.cuni.mff.peckam.java.origamist.services.TooltipFactory;
import cz.cuni.mff.peckam.java.origamist.services.interfaces.ConfigurationManager;
import cz.cuni.mff.peckam.java.origamist.services.interfaces.OrigamiHandler;
import cz.cuni.mff.peckam.java.origamist.utils.ExportFormat;

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

    /** The currently selected operation. */
    protected Operations          currentOperation        = null;

    /** Reflects whether alternative action buttons are shown. */
    protected boolean             alternativeActionsShown = false;

    /** The main application toolbar. */
    protected JToolBarWithBgImage toolbar                 = null;

    /** The dropdown button for saving the model. */
    protected JDropDownButton     saveButton              = null;

    /** The button for displaying model properties. */
    protected JButton             propertiesButton        = null;

    /** Toolbar buttons for model operations. */
    protected JToggleButton       operationMountainFold, operationValleyFold, operationMountainFoldUnfold,
            operationValleyFoldUnfold, operationThunderboltFoldMountainFirst, operationThunderboltFoldValleyFirst,
            operationTurnOver, operationRotate, operationPull, operationCrimpFoldInside, operationCrimpFoldOutside,
            operationOpen, operationReverseFoldInside, operationReverseFoldOutside, operationRepeatAction,
            operationMark;

    /** Toolbar buttons for model operations. */
    protected JToggleMenuItem     operationRabbitFold, operationSquashFold;

    /** The status bar. */
    protected JStatusBar          statusBar               = null;

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

        UIManager.getDefaults().addResourceBundle("editor");

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

    @Override
    public void init()
    {
        super.init();

        setOrigami(null);
    }

    /**
     * Create and setup all the form components.
     */
    @Override
    protected void createComponents()
    {
        toolbar = createToolbar();

        statusBar = new JStatusBar();
        statusBar.showMessage(" ");
    }

    /**
     * Setup the form layout.
     */
    @Override
    protected void buildLayout()
    {
        setLayout(new FormLayout("pref:grow", "pref,$lgap,pref:grow,$lgap,bottom:pref"));

        CellConstraints cc = new CellConstraints();

        add(toolbar, cc.xy(1, 1));

        add(statusBar, cc.xy(1, 5));
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
        // TODO add the possibility to load a model from a listing

        toolbar.add(new JToolBar.Separator());

        JDropDownButton dropDown = toolbar.createToolbarDropdownButton(null, "menu.save", "save-32.png");
        toolbar.add(dropDown);

        dropDown.addComponent(toolbar.createToolbarDropdownSeparator("menu.separator.editable"));
        dropDown.addComponent(toolbar.createToolbarDropdownItem(new ExportAction(ExportFormat.XML), "menu.save.asXML",
                "xml-32.png"));
        dropDown.addComponent(toolbar.createToolbarDropdownItem(new ExportAction(ExportFormat.SVG), "menu.save.asSVG",
                "svg-32.png"));

        dropDown.addComponent(toolbar.createToolbarDropdownSeparator("menu.separator.non-editable"));
        dropDown.addComponent(toolbar.createToolbarDropdownItem(new ExportAction(ExportFormat.PDF), "menu.save.asPDF",
                "pdf-32.png"));
        dropDown.addComponent(toolbar.createToolbarDropdownItem(new ExportAction(ExportFormat.PNG), "menu.save.asPNG",
                "png-32.png"));

        saveButton = dropDown;

        toolbar.add(new JToolBar.Separator());

        toolbar.add(propertiesButton = toolbar.createToolbarButton(new PropertiesAction(), "menu.properties",
                "properties-32.png"));

        toolbar.add(new JToolBar.Separator());

        toolbar.add(toolbar.createToolbarButton(new SettingsAction(), "menu.settings", "settings-32.png"));

        toolbar.add(new JToolBar.Separator());

        BoundButtonGroup operationGroup = new BoundButtonGroup();

        toolbar.add(operationMountainFold = toolbar.createToolbarItem(new JToggleButton(), null,
                "menu.operation.mountain", "folds/mountain-32.png"));
        operationMountainFold.addActionListener(new OperationActionListener(Operations.MOUNTAIN_FOLD, "editor",
                "menu.operation.mountain"));

        toolbar.add(operationValleyFold = toolbar.createToolbarItem(new JToggleButton(), null, "menu.operation.valley",
                "folds/valley-32.png"));
        operationValleyFold.addActionListener(new OperationActionListener(Operations.VALLEY_FOLD, "editor",
                "menu.operation.valley"));

        toolbar.add(operationMountainFoldUnfold = toolbar.createToolbarItem(new JToggleButton(), null,
                "menu.operation.mountainFoldUnfold", "folds/mountain-fold-unfold-32.png"));
        operationMountainFoldUnfold.addActionListener(new OperationActionListener(
                Operations.MOUNTAIN_VALLEY_FOLD_UNFOLD, "editor", "menu.operation.mountainFoldUnfold"));

        toolbar.add(operationValleyFoldUnfold = toolbar.createToolbarItem(new JToggleButton(), null,
                "menu.operation.valleyFoldUnfold", "folds/valley-fold-unfold-32.png"));
        operationValleyFoldUnfold.addActionListener(new OperationActionListener(Operations.VALLEY_MOUNTAIN_FOLD_UNFOLD,
                "editor", "menu.operation.valleyFoldUnfold"));

        toolbar.add(operationThunderboltFoldMountainFirst = toolbar.createToolbarItem(new JToggleButton(), null,
                "menu.operation.thunderboltMountainFirst", "folds/thunderbolt-mountain-first-32.png"));
        operationThunderboltFoldMountainFirst.addActionListener(new OperationActionListener(
                Operations.THUNDERBOLT_FOLD, "editor", "menu.operation.thunderboltMountainFirst"));

        // TODO make a difference between this and the following operation

        toolbar.add(operationThunderboltFoldValleyFirst = toolbar.createToolbarItem(new JToggleButton(), null,
                "menu.operation.thunderboltValleyFirst", "folds/thunderbolt-valley-first-32.png"));
        operationThunderboltFoldValleyFirst.addActionListener(new OperationActionListener(Operations.THUNDERBOLT_FOLD,
                "editor", "menu.operation.thunderboltValleyFirst"));

        toolbar.add(operationTurnOver = toolbar.createToolbarItem(new JToggleButton(), null, "menu.operation.turnOver",
                "folds/turn-over-32.png"));
        operationTurnOver.addActionListener(new OperationActionListener(Operations.TURN_OVER, "editor",
                "menu.operation.turnOver"));

        toolbar.add(operationRotate = toolbar.createToolbarItem(new JToggleButton(), null, "menu.operation.rotate",
                "folds/rotate-32.png"));
        operationRotate.addActionListener(new OperationActionListener(Operations.ROTATE, "editor",
                "menu.operation.rotate"));

        toolbar.add(operationPull = toolbar.createToolbarItem(new JToggleButton(), null, "menu.operation.pull",
                "folds/pull-32.png"));
        operationPull.addActionListener(new OperationActionListener(Operations.PULL, "editor", "menu.operation.pull"));

        toolbar.add(operationCrimpFoldInside = toolbar.createToolbarItem(new JToggleButton(), null,
                "menu.operation.crimpInside", "folds/crimp-inside-32.png"));
        operationCrimpFoldInside.addActionListener(new OperationActionListener(Operations.INSIDE_CRIMP_FOLD, "editor",
                "menu.operation.crimpInside"));

        toolbar.add(operationCrimpFoldOutside = toolbar.createToolbarItem(new JToggleButton(), null,
                "menu.operation.crimpOutside", "folds/crimp-outside-32.png"));
        operationCrimpFoldOutside.addActionListener(new OperationActionListener(Operations.OUTSIDE_CRIMP_FOLD,
                "editor", "menu.operation.crimpOutside"));

        toolbar.add(operationOpen = toolbar.createToolbarItem(new JToggleButton(), null, "menu.operation.open",
                "folds/open-32.png"));
        operationOpen.addActionListener(new OperationActionListener(Operations.OPEN, "editor", "menu.operation.open"));

        toolbar.add(operationReverseFoldInside = toolbar.createToolbarItem(new JToggleButton(), null,
                "menu.operation.reverseInside", "folds/reverse-inside-32.png"));
        operationReverseFoldInside.addActionListener(new OperationActionListener(Operations.INSIDE_REVERSE_FOLD,
                "editor", "menu.operation.reverseInside"));

        toolbar.add(operationReverseFoldOutside = toolbar.createToolbarItem(new JToggleButton(), null,
                "menu.operation.reverseOutside", "folds/reverse-outside-32.png"));
        operationReverseFoldOutside.addActionListener(new OperationActionListener(Operations.OUTSIDE_REVERSE_FOLD,
                "editor", "menu.operation.reverseOutside"));

        toolbar.add(operationRepeatAction = toolbar.createToolbarItem(new JToggleButton(), null,
                "menu.operation.repeat", "folds/repeat-32.png"));
        operationRepeatAction.addActionListener(new OperationActionListener(Operations.REPEAT_ACTION, "editor",
                "menu.operation.repeat"));

        operationRabbitFold = toolbar.createToolbarDropdownItem(new JToggleMenuItem(), null, "menu.operation.rabbit",
                "folds/rabbit-32.png");
        operationRabbitFold.addActionListener(new OperationActionListener(Operations.RABBIT_FOLD, "editor",
                "menu.operation.rabbit"));

        operationSquashFold = toolbar.createToolbarDropdownItem(new JToggleMenuItem(), null, "menu.operation.squash",
                "folds/squash-32.png");
        operationSquashFold.addActionListener(new OperationActionListener(Operations.SQUASH_FOLD, "editor",
                "menu.operation.squash"));

        toolbar.add(operationMark = toolbar.createToolbarItem(new JToggleButton(), null, "menu.operation.mark",
                "folds/mark-32.png"));
        operationMark.addActionListener(new OperationActionListener(null, "editor", "menu.operation.mark"));
        // TODO add Mark operation

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
        saveButton.setEnabled(origami != null);
        propertiesButton.setEnabled(origami != null);
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

    @Override
    protected void registerServicesAfterComponentsAreCreated()
    {
        super.registerServicesAfterComponentsAreCreated();
        ServiceLocator.add(MessageBar.class, statusBar);
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

    /**
     * Exports the currently displayed origami to the given format.
     * 
     * @author Martin Pecka
     */
    class ExportAction extends AbstractAction
    {

        /** */
        private static final long serialVersionUID = -399462365929673938L;

        /** The format to export to. */
        protected ExportFormat    format;

        /**
         * @param format The format to export to.
         */
        public ExportAction(ExportFormat format)
        {
            this.format = format;
        }

        @Override
        public void actionPerformed(ActionEvent e)
        {
            JFileChooser chooser = new JFileChooser();
            File defaultFile = ServiceLocator.get(ConfigurationManager.class).get().getLastExportPath().getParentFile();
            chooser.setCurrentDirectory(defaultFile);
            chooser.setFileFilter(new FileNameExtensionFilter("*." + format.toString().toLowerCase(), format.toString()));
            chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            chooser.setDialogType(JFileChooser.SAVE_DIALOG);
            chooser.setApproveButtonText(appMessages.getString("exportDialog.approve"));
            chooser.setApproveButtonMnemonic(KeyStroke.getKeyStroke(
                    appMessages.getString("exportDialog.approve.mnemonic")).getKeyCode());
            chooser.setApproveButtonToolTipText(ServiceLocator.get(TooltipFactory.class).getDecorated(
                    appMessages.getString("exportDialog.approve.tooltip.message"),
                    appMessages.getString("exportDialog.approve.tooltip.title"), "save.png",
                    KeyStroke.getKeyStroke("alt " + appMessages.getString("exportDialog.approve.mnemonic"))));
            if (chooser.showDialog(OrigamiEditor.this, null) == JFileChooser.APPROVE_OPTION) {
                File f = chooser.getSelectedFile();
                if (!chooser.accept(f)) {
                    f = new File(f.toString() + "." + format.toString().toLowerCase());
                }
                ServiceLocator.get(ConfigurationManager.class).get().setLastExportPath(f);

                if (f.exists()) {
                    OrigamiEditor.this.format.applyPattern(appMessages.getString("exportDialog.overwrite"));
                    if (JOptionPane.showConfirmDialog(OrigamiEditor.this,
                            OrigamiEditor.this.format.format(new Object[] { f }),
                            appMessages.getString("exportDialog.overwrite.title"), JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE, null) != JOptionPane.YES_OPTION) {
                        return;
                    }
                }

                try {
                    ServiceLocator.get(OrigamiHandler.class).export(origami, f, format);
                    OrigamiEditor.this.format.applyPattern(appMessages.getString("exportSuccessful.message"));
                    JOptionPane.showMessageDialog(getRootPane(),
                            OrigamiEditor.this.format.format(new Object[] { f.toString() }),
                            appMessages.getString("exportSuccessful.title"), JOptionPane.INFORMATION_MESSAGE, null);
                } catch (IOException e1) {
                    OrigamiEditor.this.format.applyPattern(appMessages.getString("failedToExport.message"));
                    JOptionPane.showMessageDialog(getRootPane(),
                            OrigamiEditor.this.format.format(new Object[] { f.toString() }),
                            appMessages.getString("failedToExport.title"), JOptionPane.ERROR_MESSAGE, null);
                    Logger.getLogger("application").warn("Unable to export origami.", e1);
                }
            }
        }

    }

    /**
     * Display the origami properties dialog.
     * 
     * @author Martin Pecka
     */
    protected class PropertiesAction extends AbstractAction
    {
        /** */
        private static final long serialVersionUID = -3808672661069071582L;

        @Override
        public void actionPerformed(ActionEvent e)
        {
            OrigamiPropertiesFrame propertiesFrame = new OrigamiPropertiesFrame(origami);
            propertiesFrame.setVisible(true);
            Origami result = propertiesFrame.getOrigami();
            if (origami == null) {
                setOrigami(result);
            }
        }

    }

    /**
     * This listener selects the current operation and displays a tooltip in the statusbar.
     * 
     * @author Martin Pecka
     */
    protected class OperationActionListener implements ActionListener
    {
        /** The operation that belongs to the button. */
        protected Operations operation;
        /** The name of the resource bundle where we get the strings from. */
        protected String     bundleName;
        /** The name of the base key for string getting from resource bundle. */
        protected String     key;

        /**
         * @param operation The operation that belongs to the button.
         * @param bundleName The name of the resource bundle where we get the strings from.
         * @param key The name of the base key for string getting from resource bundle.
         */
        public OperationActionListener(Operations mountainFold, String bundleName, String key)
        {
            this.operation = mountainFold;
            this.bundleName = bundleName;
            this.key = key;
        }

        @Override
        public void actionPerformed(ActionEvent e)
        {
            currentOperation = operation;

            MessageBar statusBar = ServiceLocator.get(MessageBar.class);
            if (statusBar != null) {
                ResourceBundle b = ResourceBundle.getBundle(bundleName, ServiceLocator.get(ConfigurationManager.class)
                        .get().getLocale());
                String statusText = "";
                try {
                    statusText += b.getString(key);
                } catch (MissingResourceException ex) {}
                try {
                    statusText += ": " + b.getString(key + ".description");
                } catch (MissingResourceException ex) {}

                statusBar.showMessage(statusText);
            }
        }

    }
}
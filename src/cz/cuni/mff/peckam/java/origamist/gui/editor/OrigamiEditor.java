/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.gui.editor;

import java.applet.AppletContext;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;

import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.FocusManager;
import javax.swing.ImageIcon;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.origamist.BackgroundImageSupport;
import javax.swing.origamist.BackgroundImageSupport.BackgroundRepeat;
import javax.swing.origamist.BoundButtonGroup;
import javax.swing.origamist.JDropDownButton;
import javax.swing.origamist.JLocalizedLabel;
import javax.swing.origamist.JStatusBar;
import javax.swing.origamist.JToggleMenuItem;
import javax.swing.origamist.JToolBarWithBgImage;
import javax.swing.origamist.MessageBar;
import javax.swing.origamist.OrigamistToolBar;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeModel;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import cz.cuni.mff.peckam.java.origamist.common.LangString;
import cz.cuni.mff.peckam.java.origamist.configuration.Configuration;
import cz.cuni.mff.peckam.java.origamist.exceptions.InvalidOperationException;
import cz.cuni.mff.peckam.java.origamist.exceptions.PaperStructureException;
import cz.cuni.mff.peckam.java.origamist.exceptions.UnsupportedDataFormatException;
import cz.cuni.mff.peckam.java.origamist.gui.common.CommonGui;
import cz.cuni.mff.peckam.java.origamist.gui.common.JEditableSlider;
import cz.cuni.mff.peckam.java.origamist.gui.common.JLangStringListTextField;
import cz.cuni.mff.peckam.java.origamist.gui.common.JZoomSlider;
import cz.cuni.mff.peckam.java.origamist.model.ObjectFactory;
import cz.cuni.mff.peckam.java.origamist.model.Operation;
import cz.cuni.mff.peckam.java.origamist.model.OperationContainer;
import cz.cuni.mff.peckam.java.origamist.model.OperationsHelper;
import cz.cuni.mff.peckam.java.origamist.model.Origami;
import cz.cuni.mff.peckam.java.origamist.model.RepeatOperation;
import cz.cuni.mff.peckam.java.origamist.model.Step;
import cz.cuni.mff.peckam.java.origamist.model.jaxb.Model;
import cz.cuni.mff.peckam.java.origamist.model.jaxb.Operations;
import cz.cuni.mff.peckam.java.origamist.model.jaxb.Steps;
import cz.cuni.mff.peckam.java.origamist.modelstate.arguments.EditorDataReceiver;
import cz.cuni.mff.peckam.java.origamist.modelstate.arguments.OperationArgument;
import cz.cuni.mff.peckam.java.origamist.modelstate.arguments.OperationsTreeDataReceiver;
import cz.cuni.mff.peckam.java.origamist.modelstate.arguments.UserInputDataReceiver;
import cz.cuni.mff.peckam.java.origamist.services.ServiceLocator;
import cz.cuni.mff.peckam.java.origamist.services.StepThumbnailGenerator;
import cz.cuni.mff.peckam.java.origamist.services.TooltipFactory;
import cz.cuni.mff.peckam.java.origamist.services.interfaces.ConfigurationManager;
import cz.cuni.mff.peckam.java.origamist.services.interfaces.OrigamiHandler;
import cz.cuni.mff.peckam.java.origamist.utils.ExportFormat;
import cz.cuni.mff.peckam.java.origamist.utils.ObservableList.ChangeTypes;
import cz.cuni.mff.peckam.java.origamist.utils.ObservablePropertyEvent;
import cz.cuni.mff.peckam.java.origamist.utils.ObservablePropertyListener;
import cz.cuni.mff.peckam.java.origamist.utils.ParametrizedCallable;
import cz.cuni.mff.peckam.java.origamist.utils.ParametrizedLocalizedString;

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
    private static final long                       serialVersionUID         = -6853141518719373854L;

    /** The resource bundle with editor strings. */
    protected ResourceBundle                        editorMessages;

    /** The bootstrapper that has started this applet, or <code>null</code>, if it has not been bootstrapped. */
    protected JApplet                               bootstrap                = null;

    /** The currently displayed origami. May be <code>null</code>. */
    protected Origami                               origami                  = null;

    /** The currently displayed step. */
    protected Step                                  step                     = null;

    /** The currently selected operation. */
    protected Operation                             currentOperation         = null;

    /** The currently processed argument of operation. */
    protected OperationArgument                     currentOperationArgument = null;

    /** Reflects whether alternative action buttons are shown. */
    protected boolean                               alternativeActionsShown  = false;

    /** The main application toolbar. */
    protected JToolBarWithBgImage                   toolbar                  = null;

    /** The dropdown button for saving the model. */
    protected JDropDownButton                       saveButton               = null;

    /** The button for displaying model properties. */
    protected JButton                               propertiesButton         = null;

    /** The button group holding all operation buttons. */
    protected BoundButtonGroup                      operationGroup           = null;

    /** Toolbar buttons for model operations. */
    protected JToggleButton                         operationMountainFold, operationValleyFold,
            operationMountainFoldUnfold, operationValleyFoldUnfold, operationThunderboltFoldMountainFirst,
            operationThunderboltFoldValleyFirst, operationTurnOver, operationRotate, operationPull,
            operationCrimpFoldInside, operationCrimpFoldOutside, operationOpen, operationReverseFoldInside,
            operationReverseFoldOutside, operationRepeatAction, operationMark, operationImage;

    /** Toolbar buttons for model operations. */
    protected JToggleMenuItem                       operationRabbitFold, operationSquashFold;

    /** The table of action alternatives. The key is the primary action and the value is its alternative. */
    protected Hashtable<JComponent, JComponent>     alternativeActions       = new Hashtable<JComponent, JComponent>();

    /** The panel with step tools. */
    protected JPanel                                leftPanel;

    /** The component used to render and edit the step. */
    protected StepEditor                            stepEditor;

    /** The status bar. */
    protected JStatusBar                            statusBar                = null;

    /** The slider for zoom. */
    protected JEditableSlider                       zoomSlider;

    /** Toolbar button. */
    protected JButton                               addStep, nextStep, prevStep, removeStep, undoRemoveStep,
            cancelLastOperation, undoCancelOperation;

    /** List of steps that can be undone. */
    protected List<Step>                            stepsToUndo              = new LinkedList<Step>();

    /** List of operations that can be undone. */
    protected List<Operation>                       operationsToUndo         = new LinkedList<Operation>();

    /** The steps the removed operations belong to. */
    protected Map<Operation, Step>                  undoneOperationsSteps    = new HashMap<Operation, Step>();

    /** The string displaying the current position in the list of steps. */
    protected ParametrizedLocalizedString           stepXofY;

    /** Observer for the number of steps. */
    protected ObservablePropertyListener<Step>      stepsObserver;

    /** Observer for the number of steps. */
    protected ObservablePropertyListener<Operation> operationsObserver;

    /** The list of operations defined for the current step. */
    protected OperationsTree                        operationsTree;

    /** The description of the step. */
    protected JLangStringListTextField<JTextField>  description;

    /** Span of the step. */
    protected JSpinner                              colSpan, rowSpan;

    /** The combobox for selecting the current step. */
    protected JComboBox                             steps;

    /** Key for step editor's message bar. */
    protected static final String                   OPERATION_HINT_KEY       = "operation.hint";

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
                if (e.getKeyCode() == KeyEvent.VK_ALT) {
                    showAlternativeActions(!alternativeActionsShown);
                    e.consume();
                } else if (e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_SPACE) {
                    if (currentOperation != null) {
                        proceedToNextOperationArgument();
                        e.consume();
                    }
                } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    if (currentOperation != null) {
                        setCurrentOperation(null);
                        e.consume();
                    }
                }
            }
        });
    }

    @Override
    public void init()
    {
        stepsObserver = new ObservablePropertyListener<Step>() {
            @Override
            public void changePerformed(ObservablePropertyEvent<? extends Step> evt)
            {
                stepXofY.setParameter(1, origami != null ? origami.getModel().getSteps().getStep().size() : 0);
                updateOperationsModel();
                getContentPane().repaint();
            }
        };

        operationsObserver = new ObservablePropertyListener<Operation>() {
            @Override
            public void changePerformed(final ObservablePropertyEvent<? extends Operation> evt)
            {
                if (step == null || origami == null)
                    return;

                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run()
                    {
                        addStep.setEnabled(step.getOperations().size() > 0);
                        cancelLastOperation.setEnabled(step.getOperations().size() > 0);

                        if (evt.getEvent().getChangeType() != ChangeTypes.ADD) {
                            operationsTree.removeOperation(evt.getEvent().getItem());
                        }
                        if (evt.getEvent().getChangeType() != ChangeTypes.REMOVE) {
                            operationsTree.addTopLevelOperation(evt.getEvent().getItem());
                            operationsTree.scrollToOperation(evt.getEvent().getItem());
                        }
                        operationsTree.repaint();
                    }
                });
            }

        };

        super.init();

        ServiceLocator.get(ConfigurationManager.class).get()
                .addAndRunResourceBundleListener(new Configuration.ResourceBundleLocaleListener("editor") {
                    @Override
                    public void bundleChanged()
                    {
                        editorMessages = bundle;
                    }
                });

        processParams();
    }

    /**
     * Process the application parameters.
     */
    protected void processParams()
    {
        setOrigami(null);
        if (getParameter("file") != null) {
            try {
                Origami o = ServiceLocator.get(OrigamiHandler.class).loadModel(
                        new URL(getCodeBase(), getParameter("file")).toURI(), false);
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
            } catch (URISyntaxException e1) {
                JOptionPane.showMessageDialog(rootPane, appMessages.getString("exception.IOException.loadModel"),
                        appMessages.getString("exception.IOException.loadModel.title"), JOptionPane.ERROR_MESSAGE);
                Logger.getLogger("application").error(e1);
            }
        }
    }

    /**
     * Create and setup all the form components.
     */
    @Override
    protected void createComponents()
    {
        toolbar = createToolbar();

        leftPanel = createLeftPanel();

        stepEditor = new StepEditor();
        stepEditor.addPropertyChangeListener("zoom", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt)
            {
                zoomSlider.setValue((int) (double) ((Double) evt.getNewValue()));
            }
        });

        description = new JLangStringListTextField<JTextField>(new LinkedList<LangString>(), new JTextField()) {
            /** */
            private static final long serialVersionUID = 3780617567174115127L;

            @Override
            protected LinkedHashSet<Locale> getSuggestedLocales()
            {
                LinkedHashSet<Locale> result = super.getSuggestedLocales();
                // TODO may be slow for large and well-localized models; if the tests confirm this, create a Locale
                // cache somewhere in Origami or so
                if (origami != null) {
                    for (Step s : origami.getModel().getSteps().getStep()) {
                        for (LangString ls : s.getDescription()) {
                            result.add(ls.getLang());
                        }
                    }
                }
                return result;
            }
        };

        statusBar = new JStatusBar();
        statusBar.showMessage(" ");

        getTopmostComponent().setMinimumSize(new Dimension(750, 500));
    }

    /**
     * Setup the form layout.
     */
    @Override
    protected void buildLayout()
    {
        setLayout(new FormLayout("left:pref,$ugap,pref:grow", "pref,fill:pref:grow,pref,bottom:pref"));
        CellConstraints cc = new CellConstraints();

        add(toolbar, cc.xyw(1, 1, 3));

        add(leftPanel, cc.xywh(1, 2, 1, 2));

        add(stepEditor, cc.xy(3, 2));

        JPanel descPanel = new JPanel(new FormLayout("pref,$lcgap,pref:grow", "pref"));
        descPanel.add(new JLocalizedLabel("editor", "description.label"), cc.xy(1, 1));
        descPanel.add(description, cc.xy(3, 1));

        add(descPanel, cc.xy(3, 3));

        add(statusBar, cc.xyw(1, 4, 3));
    }

    /**
     * Show or hide (depending on the parameter) alternative diagram actions - eg. display a valley fold button instead
     * of mountain fold button and so on.
     * 
     * @param show If <code>true</code>, show alternatives, otherwise show the default buttons.
     */
    protected void showAlternativeActions(final boolean show)
    {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run()
            {
                alternativeActionsShown = show;

                Component focusOwner = FocusManager.getCurrentManager().getFocusOwner();

                for (Entry<JComponent, JComponent> e : alternativeActions.entrySet()) {
                    e.getKey().setVisible(!show);
                    e.getValue().setVisible(show);

                    if (e.getKey().equals(focusOwner))
                        e.getValue().requestFocusInWindow();

                    if (e.getValue().equals(focusOwner))
                        e.getKey().requestFocusInWindow();
                }
            }
        });
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
        dropDown.addComponent(toolbar.createToolbarDropdownSeparator("menu.separator.animation"));
        dropDown.addComponent(toolbar.createToolbarDropdownItem(new ExportAction(ExportFormat.MOV), "menu.save.asMOV",
                "mov-32.png"));

        saveButton = dropDown;

        toolbar.add(new JToolBar.Separator());

        toolbar.add(propertiesButton = toolbar.createToolbarButton(new PropertiesAction(), "menu.properties",
                "properties-32.png"));

        toolbar.add(new JToolBar.Separator());

        toolbar.add(toolbar.createToolbarButton(new SettingsAction(), "menu.settings", "settings-32.png"));

        toolbar.add(new JToolBar.Separator());

        operationGroup = new BoundButtonGroup();

        toolbar.add(operationMountainFold = toolbar.createToolbarItem(new JToggleButton(), null,
                "menu.operation.mountain", "folds/mountain-32.png"));
        operationMountainFold.addActionListener(new OperationActionListener(Operations.MOUNTAIN_FOLD, "editor",
                "menu.operation.mountain"));

        toolbar.add(operationValleyFold = toolbar.createToolbarItem(new JToggleButton(), null, "menu.operation.valley",
                "folds/valley-32.png"));
        operationValleyFold.addActionListener(new OperationActionListener(Operations.VALLEY_FOLD, "editor",
                "menu.operation.valley"));

        alternativeActions.put(operationMountainFold, operationValleyFold);

        toolbar.add(operationMountainFoldUnfold = toolbar.createToolbarItem(new JToggleButton(), null,
                "menu.operation.mountainFoldUnfold", "folds/mountain-fold-unfold-32.png"));
        operationMountainFoldUnfold.addActionListener(new OperationActionListener(
                Operations.MOUNTAIN_VALLEY_FOLD_UNFOLD, "editor", "menu.operation.mountainFoldUnfold"));

        toolbar.add(operationValleyFoldUnfold = toolbar.createToolbarItem(new JToggleButton(), null,
                "menu.operation.valleyFoldUnfold", "folds/valley-fold-unfold-32.png"));
        operationValleyFoldUnfold.addActionListener(new OperationActionListener(Operations.VALLEY_MOUNTAIN_FOLD_UNFOLD,
                "editor", "menu.operation.valleyFoldUnfold"));

        alternativeActions.put(operationMountainFoldUnfold, operationValleyFoldUnfold);

        toolbar.add(operationThunderboltFoldMountainFirst = toolbar.createToolbarItem(new JToggleButton(), null,
                "menu.operation.thunderboltMountainFirst", "folds/thunderbolt-mountain-first-32.png"));
        operationThunderboltFoldMountainFirst.addActionListener(new OperationActionListener(
                Operations.THUNDERBOLT_FOLD, "editor", "menu.operation.thunderboltMountainFirst"));

        // TODO make a difference between this and the following operation

        toolbar.add(operationThunderboltFoldValleyFirst = toolbar.createToolbarItem(new JToggleButton(), null,
                "menu.operation.thunderboltValleyFirst", "folds/thunderbolt-valley-first-32.png"));
        operationThunderboltFoldValleyFirst.addActionListener(new OperationActionListener(Operations.THUNDERBOLT_FOLD,
                "editor", "menu.operation.thunderboltValleyFirst"));

        alternativeActions.put(operationThunderboltFoldMountainFirst, operationThunderboltFoldValleyFirst);

        toolbar.add(operationTurnOver = toolbar.createToolbarItem(new JToggleButton(), null, "menu.operation.turnOver",
                "folds/turn-over-32.png"));
        operationTurnOver.addActionListener(new OperationActionListener(Operations.TURN_OVER, "editor",
                "menu.operation.turnOver"));

        toolbar.add(operationRotate = toolbar.createToolbarItem(new JToggleButton(), null, "menu.operation.rotate",
                "folds/rotate-32.png"));
        operationRotate.addActionListener(new OperationActionListener(Operations.ROTATE, "editor",
                "menu.operation.rotate"));

        alternativeActions.put(operationTurnOver, operationRotate);

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

        alternativeActions.put(operationCrimpFoldInside, operationCrimpFoldOutside);

        // toolbar.add(operationOpen = toolbar.createToolbarItem(new JToggleButton(), null, "menu.operation.open",
        // "folds/open-32.png"));
        // operationOpen.addActionListener(new OperationActionListener(Operations.OPEN, "editor",
        // "menu.operation.open"));

        toolbar.add(operationReverseFoldInside = toolbar.createToolbarItem(new JToggleButton(), null,
                "menu.operation.reverseInside", "folds/reverse-inside-32.png"));
        operationReverseFoldInside.addActionListener(new OperationActionListener(Operations.INSIDE_REVERSE_FOLD,
                "editor", "menu.operation.reverseInside"));

        toolbar.add(operationReverseFoldOutside = toolbar.createToolbarItem(new JToggleButton(), null,
                "menu.operation.reverseOutside", "folds/reverse-outside-32.png"));
        operationReverseFoldOutside.addActionListener(new OperationActionListener(Operations.OUTSIDE_REVERSE_FOLD,
                "editor", "menu.operation.reverseOutside"));

        alternativeActions.put(operationReverseFoldInside, operationReverseFoldOutside);

        toolbar.add(operationRepeatAction = toolbar.createToolbarItem(new JToggleButton(), null,
                "menu.operation.repeat", "folds/repeat-32.png"));
        operationRepeatAction.addActionListener(new OperationActionListener(Operations.REPEAT_ACTION, "editor",
                "menu.operation.repeat"));

        // operationRabbitFold = toolbar.createToolbarDropdownItem(new JToggleMenuItem(), null, "menu.operation.rabbit",
        // "folds/rabbit-32.png");
        // operationRabbitFold.addActionListener(new OperationActionListener(Operations.RABBIT_FOLD, "editor",
        // "menu.operation.rabbit"));
        //
        // operationSquashFold = toolbar.createToolbarDropdownItem(new JToggleMenuItem(), null, "menu.operation.squash",
        // "folds/squash-32.png");
        // operationSquashFold.addActionListener(new OperationActionListener(Operations.SQUASH_FOLD, "editor",
        // "menu.operation.squash"));

        toolbar.add(operationMark = toolbar.createToolbarItem(new JToggleButton(), null, "menu.operation.mark",
                "folds/mark-32.png"));
        operationMark
                .addActionListener(new OperationActionListener(Operations.MARKER, "editor", "menu.operation.mark"));

        toolbar.add(operationImage = toolbar.createToolbarItem(new JToggleButton(), null, "menu.operation.image",
                "folds/image_op-32.png"));
        operationImage
                .addActionListener(new OperationActionListener(Operations.IMAGE, "editor", "menu.operation.image"));

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
        // operationGroup.add(operationOpen);
        operationGroup.add(operationReverseFoldInside);
        operationGroup.add(operationReverseFoldOutside);
        operationGroup.add(operationRepeatAction);
        // operationGroup.add(operationRabbitFold);
        // operationGroup.add(operationSquashFold);
        operationGroup.add(operationMark);
        operationGroup.add(operationImage);

        // JDropDownButton advancedButton = toolbar.createToolbarDropdownButton(
        // new JDropDownButtonReflectingSelectionGroup(new JButton(), operationGroup), null,
        // "menu.operation.advanced", "empty-32.png");
        // toolbar.add(advancedButton);
        // advancedButton.addComponent(operationRabbitFold);
        // advancedButton.addComponent(operationSquashFold);

        showAlternativeActions(false);

        return toolbar;
    }

    /**
     * @return The panel with step tools.
     */
    protected JPanel createLeftPanel()
    {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.GRAY));

        DefaultFormBuilder builder = new DefaultFormBuilder(new FormLayout("fill:min(pref;100dlu)", ""), panel);

        stepXofY = new ParametrizedLocalizedString("editor", "stepXofY", 0, 0);

        steps = new JComboBox();
        steps.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                setStep((Step) steps.getSelectedItem());
            }
        });
        final Font stepsPopupFont = steps.getFont();
        Font stepsFont = stepsPopupFont.deriveFont(Font.BOLD, 16f);
        steps.setFont(stepsFont);
        steps.setRenderer(new DefaultListCellRenderer() {
            /** */
            private static final long serialVersionUID = -3935099215382824545L;

            @Override
            public Component getListCellRendererComponent(javax.swing.JList list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus)
            {
                JLabel result = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected,
                        cellHasFocus);

                if (origami == null)
                    return result;

                if (index != -1) {
                    result.setText(new Integer(origami.getModel().getSteps().getStep().indexOf(value) + 1).toString());
                    result.setFont(stepsPopupFont);
                } else {
                    result.setText(stepXofY.toString());
                }

                return result;
            }
        });

        builder.append(steps);

        OrigamistToolBar toolbar = new OrigamistToolBar("editor");
        toolbar.setLayout(new FormLayout("$ugap,pref,$rgap,pref,pref,$rgap,pref,$rgap,pref,$ugap",
                "$ugap,pref,$lgap,pref,$ugap"));
        toolbar.setFloatable(false);

        CellConstraints cc = new CellConstraints();

        toolbar.add(addStep = toolbar.createToolbarButton(new AddStepAction(), "leftPanel.addStep", "step-add-24.png"),
                cc.xy(4, 2));
        toolbar.add(
                nextStep = toolbar.createToolbarButton(new NextStepAction(), "leftPanel.nextStep", "step-next-24.png"),
                cc.xy(5, 2));
        toolbar.add(
                prevStep = toolbar.createToolbarButton(new PrevStepAction(), "leftPanel.prevStep", "step-prev-24.png"),
                cc.xy(2, 2));
        toolbar.add(
                removeStep = toolbar.createToolbarButton(new RemoveStepAction(), "leftPanel.removeStep",
                        "step-remove-24.png"), cc.xy(7, 2));
        toolbar.add(
                undoRemoveStep = toolbar.createToolbarButton(new RemoveStepUndoAction(), "leftPanel.undoRemoveStep",
                        "step-remove-undo-24.png"), cc.xy(9, 2));
        undoRemoveStep.setEnabled(false);

        toolbar.add(
                cancelLastOperation = toolbar.createToolbarButton(new CancelOperationAction(),
                        "leftPanel.cancelLastOperation", "lastOperation-cancel-24.png"), cc.xy(2, 4));
        toolbar.add(
                undoCancelOperation = toolbar.createToolbarButton(new CancelOperationUndoAction(),
                        "leftPanel.cancelLastOperationUndo", "lastOperation-cancel-undo-24.png"), cc.xy(9, 4));
        undoCancelOperation.setEnabled(false);

        builder.append(toolbar);

        builder.append(new JLocalizedLabel("editor", "leftPanel.zoomSlider.label"));
        builder.appendRelatedComponentsGapRow();
        builder.nextLine(2);

        zoomSlider = new JZoomSlider();
        zoomSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e)
            {
                if (stepEditor != null && !zoomSlider.getSlider().getModel().getValueIsAdjusting()) {
                    stepEditor.setZoom((double) zoomSlider.getValue());
                }
            }
        });

        builder.append(zoomSlider);

        builder.append(new JLocalizedLabel("editor", "leftPanel.operations.label"));
        builder.appendRelatedComponentsGapRow();
        builder.nextLine(2);

        operationsTree = new OperationsTree();
        operationsTree.setCellRenderer(new OperationListCellRenderer());
        operationsTree.setBorder(BorderFactory.createEmptyBorder());
        updateOperationsModel();

        JScrollPane operationsListScroll = new JScrollPane(operationsTree, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        builder.appendRow("fill:min:grow");
        builder.append(operationsListScroll);

        colSpan = new JSpinner(new SpinnerNumberModel(1, 1, null, 1));
        JPanel colSpanPanel = new JPanel(new FormLayout("pref:grow,$lcgap,pref", "pref"));
        final JLabel colSpanLabel;
        colSpanPanel.add(colSpanLabel = new JLocalizedLabel("editor", "leftPanel.colSpan.label"), cc.xy(1, 1));
        colSpanPanel.add(colSpan, cc.xy(3, 1));
        builder.append(colSpanPanel);

        rowSpan = new JSpinner(new SpinnerNumberModel(1, 1, null, 1));
        JPanel rowSpanPanel = new JPanel(new FormLayout("pref:grow,$lcgap,pref", "pref"));
        final JLabel rowSpanLabel;
        rowSpanPanel.add(rowSpanLabel = new JLocalizedLabel("editor", "leftPanel.rowSpan.label"), cc.xy(1, 1));
        rowSpanPanel.add(rowSpan, cc.xy(3, 1));
        builder.append(rowSpanPanel);

        ServiceLocator
                .get(ConfigurationManager.class)
                .get()
                .addAndRunResourceBundleListener(
                        new Configuration.LocaleListener("editor", "leftPanel.colSpan.tooltip") {
                            @Override
                            protected void updateText(String text)
                            {
                                colSpanLabel.setToolTipText(text);
                                colSpan.setToolTipText(colSpanLabel.getToolTipText());
                                rowSpanLabel.setToolTipText(bundle.getString("leftPanel.rowSpan.tooltip"));
                                rowSpan.setToolTipText(rowSpanLabel.getToolTipText());
                            }
                        });

        colSpan.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e)
            {
                if (step != null) {
                    step.setColspan((Integer) colSpan.getValue());
                }
            }
        });

        rowSpan.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e)
            {
                if (step != null) {
                    step.setRowspan((Integer) rowSpan.getValue());
                }
            }
        });

        return panel;
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
    public void setOrigami(final Origami origami)
    {
        if (this.origami != null) {
            this.origami.removeObservablePropertyListener(stepsObserver, Origami.MODEL_PROPERTY, Model.STEPS_PROPERTY,
                    Steps.STEP_PROPERTY);
        }

        Origami old = this.origami;
        this.origami = origami;

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run()
            {
                saveButton.setEnabled(origami != null);
                propertiesButton.setEnabled(origami != null);
            }
        });

        stepEditor.setOrigami(origami);

        stepXofY.setParameter(1, origami != null ? origami.getModel().getSteps().getStep().size() : 0);

        setStep(null);
        if (origami != null) {
            this.origami.addObservablePropertyListener(stepsObserver, Origami.MODEL_PROPERTY, Model.STEPS_PROPERTY,
                    Steps.STEP_PROPERTY);
            if (origami.getModel().getSteps().getStep().size() == 0) {
                // we have just created the origami, so initialize the first step
                new AddStepAction().actionPerformed(new ActionEvent(this, 0, ""));
            }
            setStep(origami.getModel().getSteps().getStep().get(0));
        }

        getContentPane().repaint();

        if (old != null)
            old.unloadModelStates();
    }

    /**
     * @return the step
     */
    public Step getStep()
    {
        return step;
    }

    /**
     * @param step the step to set
     */
    public void setStep(final Step step)
    {
        if (step != null && step.getAttachedTo() == null) {
            return;
        }

        if (this.step != null)
            this.origami.removeObservablePropertyListener(operationsObserver, Origami.MODEL_PROPERTY,
                    Model.STEPS_PROPERTY, Steps.STEP_PROPERTY, Step.OPERATIONS_PROPERTY);

        ParametrizedCallable<Void, Exception> errorHandler = new ParametrizedCallable<Void, Exception>() {
            @Override
            public Void call(final Exception arg)
            {
                if (arg != null) {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run()
                        {
                            if (arg instanceof InvalidOperationException) {
                                JOptionPane.showMessageDialog(OrigamiEditor.this,
                                        ((InvalidOperationException) arg).getUserFriendlyMessage(),
                                        appMessages.getString("invalid.operation.title"), JOptionPane.ERROR_MESSAGE);
                            } else {
                                JOptionPane.showMessageDialog(OrigamiEditor.this, arg.getMessage(),
                                        appMessages.getString("invalid.operation.title"), JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    });
                }
                return null;
            }
        };

        this.step = step;
        stepEditor.setStep(step, null, errorHandler);
        if (stepEditor.getMessageBar() != null)
            stepEditor.getMessageBar().removeMessage(OPERATION_HINT_KEY);

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run()
            {
                int index = 0, numSteps = 0, numOperations = 0;

                if (origami != null) {
                    index = origami.getModel().getSteps().getStep().indexOf(step);
                    index++;
                    numSteps = origami.getModel().getSteps().getStep().size();
                }

                if (step != null) {
                    numOperations = step.getOperations().size();
                    origami.addObservablePropertyListener(operationsObserver, Origami.MODEL_PROPERTY,
                            Model.STEPS_PROPERTY, Steps.STEP_PROPERTY, Step.OPERATIONS_PROPERTY);
                    description.setStrings(step.getDescription());
                    colSpan.setValue(step.getColspan() != null ? step.getColspan() : 1);
                    rowSpan.setValue(step.getRowspan() != null ? step.getRowspan() : 1);

                    steps.setEnabled(true);
                    steps.setModel(new DefaultComboBoxModel(origami.getModel().getSteps().getStep().toArray()));
                } else {
                    description.setStrings(new LinkedList<LangString>());
                    colSpan.setValue(1);
                    rowSpan.setValue(1);
                    steps.setEnabled(false);
                }

                stepXofY.setParameter(0, index);
                updateOperationsModel();

                boolean operationsEnabled = step != null && step.isEditable();
                for (Enumeration<AbstractButton> en = operationGroup.getElements(); en.hasMoreElements();) {
                    en.nextElement().setEnabled(operationsEnabled);
                }

                if (index == numSteps && index != 0) {
                    addStep.setVisible(true);
                    addStep.setEnabled(numOperations > 0);
                    nextStep.setVisible(false);
                    nextStep.setEnabled(false);
                    removeStep.setEnabled(true);
                    cancelLastOperation.setEnabled(numOperations > 0);
                } else {
                    addStep.setVisible(false);
                    nextStep.setVisible(true);
                    nextStep.setEnabled(numSteps > 0);
                    removeStep.setEnabled(false);
                    cancelLastOperation.setEnabled(false);
                }

                prevStep.setEnabled(index > 1);

                stepEditor.setEnabled(step != null);
                zoomSlider.setEnabled(step != null);
                description.setEnabled(step != null);
                colSpan.setEnabled(step != null);
                rowSpan.setEnabled(step != null);

                getContentPane().repaint();
            }
        });
    }

    /**
     * Update the operations list to reflect the operations of the current step.
     */
    protected void updateOperationsModel()
    {
        final TreeModel model = operationsTree.getModelForOperations(step == null ? null : step.getOperations());

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run()
            {
                operationsTree.setModel(model);
            }
        });
    }

    /**
     * Set the current operation. If the active step isn't editable, nothing happens.
     * 
     * @param operation The operation to set.
     */
    public void setCurrentOperation(final Operation operation)
    {
        if (step == null || !step.isEditable())
            return;

        if (currentOperation != null) {
            final Operation operationToRemove = currentOperation;
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run()
                {
                    operationsTree.removeOperation(operationToRemove);
                    operationsTree.repaint();
                }
            });
            setCurrentOperationArgument(null);
        }

        currentOperation = operation;

        if (operation != null) {
            String userTip = currentOperation.getL7dUserTip(null);
            if (userTip != null)
                stepEditor.getMessageBar().showMessage(userTip, OPERATION_HINT_KEY);
            else
                stepEditor.getMessageBar().removeMessage(OPERATION_HINT_KEY);

            if (operation.getArguments().size() > 0)
                setCurrentOperationArgument(operation.getArguments().get(0));

            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run()
                {
                    operationsTree.addTopLevelOperation(operation);
                    operationsTree.scrollToOperation(operation);
                    operationsTree.repaint();
                }
            });
        }

        stepEditor.clearChosenItems();

    }

    /**
     * Set the current operation argument. If current operation is null, nothing happens.
     * 
     * @param argument The argument to set.
     */
    public void setCurrentOperationArgument(OperationArgument argument)
    {
        this.currentOperationArgument = argument;
        stepEditor.setCurrentOperationArgument(argument);

        if (currentOperation != null && currentOperationArgument != null) {
            String userTip = currentOperation.getL7dUserTip(currentOperationArgument);
            if (userTip != null)
                stepEditor.getMessageBar().showMessage(userTip, OPERATION_HINT_KEY);
            else
                stepEditor.getMessageBar().removeMessage(OPERATION_HINT_KEY);
        } else {
            stepEditor.getMessageBar().removeMessage(OPERATION_HINT_KEY);
        }

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run()
            {
                operationsTree.recomputeHeights();
                operationsTree.repaint();
                operationsTree.scrollToOperation(currentOperation);
            }
        });
        operationsTree.repaint();

        new Thread(new Runnable() {
            @Override
            public void run()
            {
                // arguments that just fetch text input from the user can ask for the input right now
                // trying to proceed to next argument will cause the text input to be shown, and when it is completed,
                // next
                // argument is automatically selected
                if (currentOperationArgument != null && currentOperationArgument instanceof UserInputDataReceiver)
                    proceedToNextOperationArgument();
            }
        }).start();
    }

    /**
     * If the current operation argument is complete, proceed to next operation argument (or finish the operation if no
     * more operation arguments are to be finished). If the current argument isn't completed and is required, show a
     * warning in the statusbar.
     */
    protected void proceedToNextOperationArgument()
    {
        if (currentOperationArgument != null) {

            if (currentOperationArgument instanceof EditorDataReceiver) {
                ((EditorDataReceiver) currentOperationArgument).readDataFromObject(stepEditor);
            } else if (currentOperationArgument instanceof OperationsTreeDataReceiver) {
                ((OperationsTreeDataReceiver) currentOperationArgument).readDataFromObject(operationsTree);
            } else if (!currentOperationArgument.isComplete()) {
                if (currentOperationArgument instanceof UserInputDataReceiver) {
                    ((UserInputDataReceiver) currentOperationArgument).askForData();
                }
            }

            if (currentOperationArgument.isComplete() || !currentOperationArgument.isRequired()) {
                setCurrentOperationArgument(currentOperationArgument.getNext());
                if (currentOperationArgument == null)
                    tryCompleteOperation();
            } else {
                stepEditor.getMessageBar().showMessage(
                        "<html><body><span style=\"font-weight: bold; color: red;\">"
                                + editorMessages.getString("operation.argument.please.complete")
                                + "</span></body></html>", 4000, StepEditingCanvasController.INCOMPLMETE_ARGUMENT_KEY);
            }
        } else if (currentOperation != null) {
            // the operation has no arguments
            tryCompleteOperation();
        }
    }

    /**
     * If the required arguments of the current operation are finished, finish the current operation and set current
     * operation to <code>null</code>. If the required arguments aren't finished, nothing happens.
     */
    protected void tryCompleteOperation()
    {
        if (currentOperation.areRequiredAgrumentsComplete()) {
            final Operation operation = currentOperation;
            new Thread(new Runnable() {
                @Override
                public void run()
                {
                    setCurrentOperation(null);

                    operation.fillFromArguments();
                    if (operation instanceof RepeatOperation) {
                        List<Operation> operations = ((RepeatOperation) operation).getOperations();
                        step.getOperations().removeAll(operations);
                    }
                    step.getOperations().add(operation);

                    if (!step.isModelStateValid(true)) {
                        try {
                            step.getModelState(true);

                            undoRemoveStep.setEnabled(false);
                            stepsToUndo.clear();

                            undoCancelOperation.setEnabled(false);
                            operationsToUndo.clear();
                            undoneOperationsSteps.clear();

                        } catch (RuntimeException e) {
                            step.getOperations().remove(operation);

                            if (e instanceof InvalidOperationException) {
                                final InvalidOperationException ioe = (InvalidOperationException) e;
                                SwingUtilities.invokeLater(new Runnable() {
                                    @Override
                                    public void run()
                                    {
                                        JOptionPane.showMessageDialog(OrigamiEditor.this, ioe.getUserFriendlyMessage(),
                                                appMessages.getString("invalid.operation.title"),
                                                JOptionPane.ERROR_MESSAGE);
                                    }
                                });
                            } else if (e instanceof PaperStructureException) {
                                JOptionPane.showMessageDialog(OrigamiEditor.this, e.getMessage(),
                                        appMessages.getString("invalid.operation.title"), JOptionPane.ERROR_MESSAGE);
                            } else {
                                stepEditor.getMessageBar().showMessage(
                                        "<html><span style=\"color:red;font-weight:bold\">"
                                                + editorMessages.getString("OrigamiEditor.invalidOperation")
                                                        .replaceAll("\\<", "&lt;").replaceAll(">", "&gt;")
                                                + "</span></html>", 7000,
                                        StepEditingCanvasController.INCOMPLMETE_ARGUMENT_KEY);
                                Logger.getLogger("application").warn("Invalid operation", e);
                            }
                        }
                        setStep(step);
                    }

                    operationsTree.repaint();
                }
            }).start();
        }
    }

    @Override
    protected void setupLoggers()
    {
        super.setupLoggers();

        Logger l = Logger.getLogger("editor");
        l.setResourceBundle(ResourceBundle.getBundle("editor", ServiceLocator.get(ConfigurationManager.class).get()
                .getLocale()));
        l.setLevel(Level.ALL);
    }

    @Override
    protected void registerServices()
    {
        super.registerServices();
        ServiceLocator.add(OrigamiEditor.class, this);
        ServiceLocator.add(StepThumbnailGenerator.class, new Callable<StepThumbnailGenerator>() {
            @Override
            public StepThumbnailGenerator call() throws Exception
            {
                return new StepThumbnailGenerator();
            }
        });
    }

    @Override
    protected void registerServicesAfterComponentsAreCreated()
    {
        super.registerServicesAfterComponentsAreCreated();
        ServiceLocator.add(MessageBar.class, statusBar);
    }

    @Override
    protected Component getTopmostComponent()
    {
        Component parent = (bootstrap != null ? bootstrap : this);
        while (parent.getParent() != null)
            parent = parent.getParent();
        return parent;
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
        public void actionPerformed(final ActionEvent e)
        {
            statusBar.showL7dMessage("editor", "settingsDialog.opening", null);
            if (e.getSource() instanceof JComponent)
                ((JComponent) e.getSource()).setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            new Thread(new Runnable() {
                @Override
                public void run()
                {
                    final SettingsFrame frame = new SettingsFrame();
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run()
                        {
                            if (e.getSource() instanceof JComponent)
                                ((JComponent) e.getSource()).setCursor(Cursor.getDefaultCursor());
                            frame.setVisible(true);
                        }
                    });
                }
            }).start();
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
        public void actionPerformed(final ActionEvent e)
        {
            statusBar.showL7dMessage("editor", "newFileDialog.opening", null);
            if (e.getSource() instanceof JComponent)
                ((JComponent) e.getSource()).setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            new Thread(new Runnable() {
                @Override
                public void run()
                {
                    // TODO check if the currently edited origami is saved, if not, show a warning
                    final OrigamiPropertiesFrame propertiesFrame = new OrigamiPropertiesFrame(null);
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run()
                        {
                            if (e.getSource() instanceof JComponent)
                                ((JComponent) e.getSource()).setCursor(Cursor.getDefaultCursor());
                            propertiesFrame.setVisible(true);
                            new Thread(new Runnable() {
                                @Override
                                public void run()
                                {
                                    if (propertiesFrame.getOrigami() != null) {
                                        setOrigami(propertiesFrame.getOrigami());
                                    }
                                }
                            }).start();
                        }
                    });
                }
            }).start();
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
            statusBar.showL7dMessage("editor", "openDialog.opening", null);
            if (e.getSource() instanceof JComponent)
                ((JComponent) e.getSource()).setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

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
                if (e.getSource() instanceof JComponent)
                    ((JComponent) e.getSource()).setCursor(Cursor.getDefaultCursor());

                final File selected = chooser.getSelectedFile();
                ServiceLocator.get(ConfigurationManager.class).get().setLastOpenPath(selected);
                new Thread(new Runnable() {
                    @Override
                    public void run()
                    {
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
                            JOptionPane.showMessageDialog(rootPane,
                                    appMessages.getString("exception.IOException.loadModel"),
                                    appMessages.getString("exception.IOException.loadModel.title"),
                                    JOptionPane.ERROR_MESSAGE);
                            Logger.getLogger("application").error(e1);
                        }
                    }
                }).start();
            } else {
                if (e.getSource() instanceof JComponent)
                    ((JComponent) e.getSource()).setCursor(Cursor.getDefaultCursor());
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

            final URL url = selectedUrl;
            new Thread(new Runnable() {
                @Override
                public void run()
                {
                    try {
                        Origami o = ServiceLocator.get(OrigamiHandler.class).loadModel(url, false);
                        setOrigami(o);
                    } catch (UnsupportedDataFormatException e1) {
                        JOptionPane.showMessageDialog(rootPane,
                                appMessages.getString("exception.UnsupportedDataFormatException.loadModel"),
                                appMessages.getString("exception.UnsupportedDataFormatException.loadModel.title"),
                                JOptionPane.ERROR_MESSAGE);
                        Logger.getLogger("application").error(e1);
                    } catch (IOException e1) {
                        JOptionPane.showMessageDialog(rootPane,
                                appMessages.getString("exception.IOException.loadModel"),
                                appMessages.getString("exception.IOException.loadModel.title"),
                                JOptionPane.ERROR_MESSAGE);
                        Logger.getLogger("application").error(e1);
                    }
                }
            }).start();
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
        public void actionPerformed(final ActionEvent e)
        {
            statusBar.showL7dMessage("editor", "propertiesDialog.opening", null);
            if (e.getSource() instanceof JComponent)
                ((JComponent) e.getSource()).setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            new Thread(new Runnable() {
                @Override
                public void run()
                {
                    final OrigamiPropertiesFrame propertiesFrame = new OrigamiPropertiesFrame(origami);
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run()
                        {
                            if (e.getSource() instanceof JComponent)
                                ((JComponent) e.getSource()).setCursor(Cursor.getDefaultCursor());

                            propertiesFrame.setVisible(true);
                            Origami result = propertiesFrame.getOrigami();
                            if (origami == null) {
                                setOrigami(result);
                            }
                            if (step != null && !step.isModelStateValid(true))
                                setStep(step);
                        }
                    });
                }
            }).start();
        }

    }

    /**
     * Exports the currently displayed origami to the desired format.
     * 
     * @author Martin Pecka
     */
    protected class ExportAction extends cz.cuni.mff.peckam.java.origamist.gui.common.ExportAction
    {
        /** */
        private static final long serialVersionUID = -4751828351174341121L;

        public ExportAction(ExportFormat format)
        {
            super(null, format);
        }

        @Override
        protected void beforeAction(ActionEvent evt)
        {
            this.origami = OrigamiEditor.this.origami;
            if (evt.getSource() instanceof Component)
                ((Component) evt.getSource()).setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        }

        @Override
        protected void done(ActionEvent evt)
        {
            if (evt.getSource() instanceof Component)
                ((Component) evt.getSource()).setCursor(Cursor.getDefaultCursor());
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
        public OperationActionListener(Operations operation, String bundleName, String key)
        {
            this.operation = operation;
            this.bundleName = bundleName;
            this.key = key;
        }

        @Override
        public void actionPerformed(ActionEvent e)
        {
            setCurrentOperation(OperationsHelper.getOperation(operation, alternativeActionsShown));

            final MessageBar statusBar = ServiceLocator.get(MessageBar.class);
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

                final String text = statusText;
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run()
                    {
                        statusBar.showMessage(text);
                    }
                });
            }
        }

    }

    /**
     * Action for selecting the next step in the current model.
     * 
     * @author Martin Pecka
     */
    protected class NextStepAction extends AbstractAction
    {
        /** */
        private static final long serialVersionUID = -6606989835699225730L;

        @Override
        public void actionPerformed(ActionEvent e)
        {
            if (step != null && step.getNext() != null) {
                setStep(step.getNext());
            }
        }
    }

    /**
     * Action for selecting the previous step in the current model.
     * 
     * @author Martin Pecka
     */
    protected class PrevStepAction extends AbstractAction
    {
        /** */
        private static final long serialVersionUID = -2065950879596721316L;

        @Override
        public void actionPerformed(ActionEvent e)
        {
            if (step != null && step.getPrevious() != null) {
                setStep(step.getPrevious());
            }
        }
    }

    /**
     * Action for adding a new step in the current model.
     * 
     * @author Martin Pecka
     */
    protected class AddStepAction extends AbstractAction
    {
        /** */
        private static final long serialVersionUID = 7388070768713032084L;

        @Override
        public void actionPerformed(ActionEvent e)
        {
            if (origami == null)
                return;

            List<Step> steps = origami.getModel().getSteps().getStep();
            if (step == null && steps.size() > 0)
                setStep(steps.get(steps.size() - 1));

            Step newStep = (Step) new ObjectFactory().createStep();
            newStep.setPrevious(step);
            newStep.setNext(null);
            if (step != null) {
                step.setNext(newStep);
                newStep.setId(step.getId() + 1);
                newStep.setZoom(step.getZoom());
            }
            steps.add(newStep);

            setStep(newStep);
        }
    }

    /**
     * Action for removing the last step in the current model.
     * 
     * @author Martin Pecka
     */
    protected class RemoveStepAction extends AbstractAction
    {
        /** */
        private static final long serialVersionUID = -1758965284633881226L;

        @Override
        public void actionPerformed(ActionEvent e)
        {
            if (origami == null || step == null)
                return;

            List<Step> steps = origami.getModel().getSteps().getStep();

            Step newStep = null;
            if (steps.size() == 0) {
                return;
            } else if (steps.size() == 1) {
                newStep = (Step) new ObjectFactory().createStep();
                newStep.setId(1);
                stepsToUndo.add(steps.get(0));
                steps.set(0, newStep);
                setStep(newStep);
                undoRemoveStep.setEnabled(true);
            } else {
                stepsToUndo.add(steps.get(steps.size() - 1));
                steps.remove(steps.size() - 1);
                setStep(steps.get(steps.size() - 1));
                undoRemoveStep.setEnabled(true);
            }

            operationsToUndo.clear();
            undoneOperationsSteps.clear();
            undoCancelOperation.setEnabled(false);
        }
    }

    /**
     * Action for undoing removal of the last step in the current model.
     * 
     * @author Martin Pecka
     */
    protected class RemoveStepUndoAction extends AbstractAction
    {

        /** */
        private static final long serialVersionUID = -7332023681928821236L;

        @Override
        public void actionPerformed(ActionEvent e)
        {
            if (origami == null || step == null)
                return;

            List<Step> steps = origami.getModel().getSteps().getStep();

            if (stepsToUndo.size() > 0) {
                Step step = stepsToUndo.get(stepsToUndo.size() - 1);
                stepsToUndo.remove(stepsToUndo.size() - 1);
                if (step.getId() == steps.get(steps.size() - 1).getId()) {
                    // this can happen either when the user removed the very first step, or if he removed the last step,
                    // and added a new one, which, however, must have no operations at this time, becuse performing an
                    // operation clears the undoable steps.
                    steps.remove(steps.size() - 1);
                }
                steps.add(step);
                setStep(step);
            }

            if (stepsToUndo.size() == 0)
                undoRemoveStep.setEnabled(false);
        }
    }

    /**
     * Action for removing the last operation in the current step.
     * 
     * @author Martin Pecka
     */
    protected class CancelOperationAction extends AbstractAction
    {
        /** */
        private static final long serialVersionUID = 2326569539259850378L;

        @Override
        public void actionPerformed(ActionEvent e)
        {
            if (step == null)
                return;

            List<Operation> operations = step.getOperations();

            if (operations.size() == 0) {
                return;
            } else {
                Operation last = operations.get(operations.size() - 1);
                if (last instanceof OperationContainer) {
                    int dialogResult = JOptionPane.showConfirmDialog(OrigamiEditor.this,
                            editorMessages.getString("OrigamiEditor.delete.repeat.operation.or.last.message"),
                            editorMessages.getString("OrigamiEditor.delete.repeat.operation.or.last.title"),
                            JOptionPane.YES_NO_CANCEL_OPTION);
                    if (dialogResult == JOptionPane.YES_OPTION) {
                        operations.remove(operations.size() - 1);
                        operationsToUndo.add(last);
                        undoneOperationsSteps.put(last, step);
                    } else if (dialogResult == JOptionPane.NO_OPTION) {
                        operations.remove(operations.size() - 1);
                        operations.addAll(((OperationContainer) last).getOperations());
                        // TODO handle removal of container? maybe it isn't needed
                    }
                } else {
                    operations.remove(operations.size() - 1);
                    operationsToUndo.add(last);
                    undoneOperationsSteps.put(last, step);
                    undoCancelOperation.setEnabled(true);
                }
            }

            if (!step.isModelStateValid(true)) {
                setStep(step);
                undoCancelOperation.setEnabled(true);
            }
        }
    }

    /**
     * Action for undoing removal of the last operation in the current step.
     * 
     * @author Martin Pecka
     */
    protected class CancelOperationUndoAction extends AbstractAction
    {

        /** */
        private static final long serialVersionUID = 3874891966442246574L;

        @Override
        public void actionPerformed(ActionEvent e)
        {
            if (step == null)
                return;

            if (operationsToUndo.size() > 0) {
                Operation operation = operationsToUndo.get(operationsToUndo.size() - 1);
                operationsToUndo.remove(operationsToUndo.size() - 1);
                Step step = undoneOperationsSteps.get(operation);
                if (step != null) {
                    undoneOperationsSteps.remove(operation);
                    step.getOperations().add(operation);
                    setStep(step);
                }
                if (operationsToUndo.size() == 0)
                    undoCancelOperation.setEnabled(false);
            }
        }
    }

    /**
     * A renderer for operations in a step.
     * 
     * @author Martin Pecka
     */
    protected class OperationListCellRenderer extends DefaultTreeCellRenderer
    {

        /** */
        private static final long serialVersionUID = -8983928724421088263L;

        {
            setLeafIcon(null);
            setOpenIcon(null);
            setClosedIcon(null);
        }

        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded,
                boolean leaf, int row, boolean hasFocus)
        {
            JLabel result = (JLabel) super
                    .getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

            Object userObject = ((DefaultMutableTreeNode) value).getUserObject();
            if (!(userObject instanceof Operation))
                return result;

            result.setFont(result.getFont().deriveFont(Font.BOLD));
            result.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));

            Operation operation = (Operation) userObject;

            if (operation != currentOperation) {
                result.setText(operation.getDefaultDescription());
            } else {
                result.setText(operation.getConstructDescription(currentOperationArgument));
                result.revalidate();
            }

            return result;
        }

    }
}
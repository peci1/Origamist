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
import java.net.URL;
import java.util.ResourceBundle;

import javax.swing.AbstractAction;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JRootPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
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

import cz.cuni.mff.peckam.java.origamist.gui.CommonGui;
import cz.cuni.mff.peckam.java.origamist.logging.GUIAppender;
import cz.cuni.mff.peckam.java.origamist.model.Origami;
import cz.cuni.mff.peckam.java.origamist.services.ServiceLocator;
import cz.cuni.mff.peckam.java.origamist.services.interfaces.ConfigurationManager;

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
                    if (!alternativeActionsShown)
                        showAlternativeActions(true);
                }
            }

            @Override
            public void keyReleased(KeyEvent e)
            {
                super.keyReleased(e);
                if (e.getKeyCode() == KeyEvent.VK_ALT) {
                    if (alternativeActionsShown)
                        showAlternativeActions(false);
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

        toolbar.add(toolbar.createToolbarButton(null, "menu.new", null));
        toolbar.add(toolbar.createToolbarButton(null, "menu.open", null));

        toolbar.add(new JToolBar.Separator());

        toolbar.add(toolbar.createToolbarButton(new SettingsAction(), "menu.settings", "settings.png"));

        BoundButtonGroup operationGroup = new BoundButtonGroup();

        toolbar.add(operationMountainFold = toolbar.createToolbarItem(new JToggleButton(), null,
                "menu.operation.mountain", null));
        toolbar.add(operationValleyFold = toolbar.createToolbarItem(new JToggleButton(), null, "menu.operation.valley",
                null));
        toolbar.add(operationMountainFoldUnfold = toolbar.createToolbarItem(new JToggleButton(), null,
                "menu.operation.mountainFoldUnfold", null));
        toolbar.add(operationValleyFoldUnfold = toolbar.createToolbarItem(new JToggleButton(), null,
                "menu.operation.valleyFoldUnfold", null));
        toolbar.add(operationThunderboltFoldMountainFirst = toolbar.createToolbarItem(new JToggleButton(), null,
                "menu.operation.thunderboltMountainFirst", null));
        toolbar.add(operationThunderboltFoldValleyFirst = toolbar.createToolbarItem(new JToggleButton(), null,
                "menu.operation.thunderboltValleyFirst", null));
        toolbar.add(operationTurnOver = toolbar.createToolbarItem(new JToggleButton(), null, "menu.operation.turnOver",
                null));
        toolbar.add(operationRotate = toolbar.createToolbarItem(new JToggleButton(), null, "menu.operation.rotate",
                null));
        toolbar.add(operationPull = toolbar.createToolbarItem(new JToggleButton(), null, "menu.operation.pull", null));
        toolbar.add(operationCrimpFoldInside = toolbar.createToolbarItem(new JToggleButton(), null,
                "menu.operation.crimpInside", null));
        toolbar.add(operationCrimpFoldOutside = toolbar.createToolbarItem(new JToggleButton(), null,
                "menu.operation.crimpOutside", null));
        toolbar.add(operationOpen = toolbar.createToolbarItem(new JToggleButton(), null, "menu.operation.open", null));
        toolbar.add(operationReverseFoldInside = toolbar.createToolbarItem(new JToggleButton(), null,
                "menu.operation.reverseInside", null));
        toolbar.add(operationReverseFoldOutside = toolbar.createToolbarItem(new JToggleButton(), null,
                "menu.operation.reverseOutside", null));
        toolbar.add(operationRepeatAction = toolbar.createToolbarItem(new JToggleButton(), null,
                "menu.operation.repeat", null));

        operationRabbitFold = toolbar.createToolbarDropdownItem(new JToggleMenuItem(), null, "menu.operation.rabbit",
                null);
        operationSquashFold = toolbar.createToolbarDropdownItem(new JToggleMenuItem(), null, "menu.operation.squash",
                null);

        toolbar.add(operationMark = toolbar.createToolbarItem(new JToggleButton(), null, "menu.operation.mark", null));

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
                "menu.operation.advanced", null);
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
    class SettingsAction extends AbstractAction
    {
        /** */
        private static final long serialVersionUID = -583073126579360879L;

        @Override
        public void actionPerformed(ActionEvent e)
        {
            new SettingsFrame().setVisible(true);
        }
    }
}
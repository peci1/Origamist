/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.gui.viewer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JToolBar;
import javax.swing.border.Border;
import javax.swing.origamist.BackgroundImageSupport;
import javax.swing.origamist.BackgroundImageSupport.BackgroundRepeat;
import javax.swing.origamist.JMultilineLabel;
import javax.swing.origamist.JPanelWithOverlay;
import javax.swing.origamist.JToolBarWithBgImage;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import cz.cuni.mff.peckam.java.origamist.gui.common.StepRenderer;
import cz.cuni.mff.peckam.java.origamist.model.Origami;
import cz.cuni.mff.peckam.java.origamist.model.Step;
import cz.cuni.mff.peckam.java.origamist.services.ServiceLocator;
import cz.cuni.mff.peckam.java.origamist.services.interfaces.ConfigurationManager;

/**
 * This panel renders the given state of the model.
 * 
 * Provides the following properties:
 * <ul>
 * <li><code>fullscreenModeRequested</code> - fired when the user clicks the "fullscreen" button</li>
 * </ul>
 * 
 * @author Martin Pecka
 */
public class StepRendererWithControls extends JPanelWithOverlay
{
    /** */
    private static final long        serialVersionUID = 6989958008007575800L;

    /** The mode to display this renderer in. */
    protected DisplayMode            displayMode;

    // COMPONENTS

    /** The renderer used to render the step. */
    protected StepRenderer           renderer;

    /** The label that shows the descripton of the shown step. */
    protected final JMultilineLabel  descLabel;

    /** The toolbar used for this renderer to control the display of the step. */
    protected JToolBarWithBgImage    toolbar;

    /** Button for zooming in. */
    protected final JButton          zoomIn;

    /** Button for zooming out. */
    protected final JButton          zoomOut;

    /** The button to show this step in Diagram view, if it is displayed in the Page view. */
    protected final JButton          fullscreenBtn;

    /** The border the toolbar buttons have in the time they are created. */
    protected Border                 defaultToolbarButtonBorder;

    /** The listener to update the text. */
    protected PropertyChangeListener diagramLocaleListener;

    /** The property helper. */
    protected PropertyChangeSupport  properties       = new PropertyChangeSupport(this);

    public StepRendererWithControls()
    {
        setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));

        addMouseListener(new MouseListener());
        addMouseWheelListener(new MouseListener());

        renderer = new StepRenderer();
        renderer.addMouseListener(new MouseListener());

        descLabel = new JMultilineLabel("");
        descLabel.setFont(descLabel.getFont().deriveFont(Font.PLAIN));

        Border emptyBorder = BorderFactory.createEmptyBorder();

        toolbar = new JToolBarWithBgImage("viewer");
        toolbar.setFloatable(false);
        toolbar.setOpaque(false); // MAGIC
        toolbar.setBackground(new Color(231, 231, 184, 230));
        toolbar.setBorder(emptyBorder);

        zoomIn = toolbar.createToolbarButton(new ZoomInAction(), "StepRenderer.zoom.in", "zoom-in-24.png");
        zoomOut = toolbar.createToolbarButton(new ZoomOutAction(), "StepRenderer.zoom.out", "zoom-out-24.png");
        fullscreenBtn = toolbar.createToolbarButton(new FullscreenAction(), "StepRenderer.fullscreen",
                "fullscreen-24.png");

        getOverlay().setOpaque(false);
        getContent().setOpaque(false); // MAGIC
        setOpaque(true);
        showOverlay();

        // Rows labelled MAGIC are important and shouldn't be changed in the future. If you for instance decide to
        // replace toolbar.setOpaque(false) with toolbar.setBackground(new Color(255,255,255,0)), ugly visual artifacts
        // appear on the toolbar buttons

        diagramLocaleListener = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt)
            {
                if (getStep() != null) {
                    descLabel.setText(getStep().getDescription(
                            ServiceLocator.get(ConfigurationManager.class).get().getDiagramLocale()));
                } else {
                    descLabel.setText("");
                }
            }
        };
        ServiceLocator.get(ConfigurationManager.class).get()
                .addPropertyChangeListener("diagramLocale", diagramLocaleListener);

        buildLayout();

        setDisplayMode(DisplayMode.PAGE);
    }

    /**
     * Add the components to their containers.
     */
    protected void buildLayout()
    {
        getContent().setLayout(new BorderLayout());
        getContent().add(renderer, BorderLayout.CENTER);
        // just a workaround - the canvas needs to know its size before the size of its container can be determined
        renderer.setSize(new Dimension(20, 20));
        getContent().add(descLabel, BorderLayout.SOUTH);

        toolbar.add(zoomIn);
        toolbar.add(zoomOut);
        toolbar.add(fullscreenBtn);

        // MAGIC: If you set this property before the button is added to the toolbar, the border isn't the same after it
        // is added to it. On the second look, it seems to be evident - toolbar buttons look differently than regular
        // butons.
        defaultToolbarButtonBorder = zoomIn.getBorder();

        getOverlay().setLayout(new FormLayout("right:pref:grow", "pref"));
        getOverlay().add(toolbar, new CellConstraints(1, 1));
    }

    public StepRendererWithControls(Origami o, Step s)
    {
        this();
        setOrigami(o);
        setStep(s);
    }

    /**
     * @return the origami
     */
    public Origami getOrigami()
    {
        return renderer.getOrigami();
    }

    /**
     * @param origami the origami to set
     */
    public void setOrigami(Origami origami)
    {
        renderer.setOrigami(origami);
        if (origami != null)
            setBackground(origami.getPaper().getColor().getBackground());
        else
            setBackground(Color.GRAY);
    }

    /**
     * @return the step
     */
    public Step getStep()
    {
        return renderer.getStep();
    }

    /**
     * @param stepId the step to set
     */
    public void setStep(Step step)
    {
        renderer.setStep(step);

        updateText();
    }

    /**
     * Reload the description of the step.
     */
    protected void updateText()
    {
        diagramLocaleListener.propertyChange(new PropertyChangeEvent(this, "diagramLocale", null, ServiceLocator
                .get(ConfigurationManager.class).get().getDiagramLocale()));
    }

    /**
     * @return the toolbar The toolbar used for this renderer to control the display of the step.
     */
    public JToolBar getToolbar()
    {
        return toolbar;
    }

    /**
     * @return The mode to display this renderer in.
     */
    public DisplayMode getDisplayMode()
    {
        return displayMode;
    }

    /**
     * @param displayMode The mode to display this renderer in.
     */
    public void setDisplayMode(DisplayMode displayMode)
    {
        this.displayMode = displayMode;
        if (DisplayMode.PAGE.equals(displayMode)) {
            toolbar.setOpaque(false);
            toolbar.setBackgroundImage(null);
            toolbar.setVisible(true);
            fullscreenBtn.setVisible(true);
            Border emptyBorder = BorderFactory.createEmptyBorder();
            for (Component c : toolbar.getComponents()) {
                if (c instanceof JComponent) {
                    ((JComponent) c).setBorder(emptyBorder);
                }
            }
        } else {
            toolbar.setVisible(false);
            toolbar.setOpaque(true);
            toolbar.setBackgroundImage(new BackgroundImageSupport(getClass().getResource(
                    "/resources/images/tooltip-bg.png"), toolbar, 0, 0, BackgroundRepeat.REPEAT_X));
            fullscreenBtn.setVisible(false);
            for (Component c : toolbar.getComponents()) {
                if (c instanceof JComponent) {
                    ((JComponent) c).setBorder(defaultToolbarButtonBorder);
                }
            }
        }
    }

    /**
     * @return the zoom
     */
    public double getZoom()
    {
        return renderer.getZoom();
    }

    /**
     * @param zoom the zoom to set
     */
    public void setZoom(double zoom)
    {
        renderer.setZoom(zoom);
    }

    /**
     * Increase zoom by 10%.
     */
    public void incZoom()
    {
        renderer.incZoom();
    }

    /**
     * Decrease zoom by 10%.
     */
    public void decZoom()
    {
        renderer.decZoom();
    }

    /**
     * @param listener
     * @see java.beans.PropertyChangeSupport#addPropertyChangeListener(java.beans.PropertyChangeListener)
     */
    public void addPropertyChangeListener(PropertyChangeListener listener)
    {
        properties.addPropertyChangeListener(listener);
    }

    /**
     * @param listener
     * @see java.beans.PropertyChangeSupport#removePropertyChangeListener(java.beans.PropertyChangeListener)
     */
    public void removePropertyChangeListener(PropertyChangeListener listener)
    {
        properties.removePropertyChangeListener(listener);
    }

    /**
     * @return
     * @see java.beans.PropertyChangeSupport#getPropertyChangeListeners()
     */
    public PropertyChangeListener[] getPropertyChangeListeners()
    {
        return properties.getPropertyChangeListeners();
    }

    /**
     * @param propertyName
     * @param listener
     * @see java.beans.PropertyChangeSupport#addPropertyChangeListener(java.lang.String,
     *      java.beans.PropertyChangeListener)
     */
    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener)
    {
        properties.addPropertyChangeListener(propertyName, listener);
    }

    /**
     * @param propertyName
     * @param listener
     * @see java.beans.PropertyChangeSupport#removePropertyChangeListener(java.lang.String,
     *      java.beans.PropertyChangeListener)
     */
    public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener)
    {
        properties.removePropertyChangeListener(propertyName, listener);
    }

    /**
     * @param propertyName
     * @return
     * @see java.beans.PropertyChangeSupport#getPropertyChangeListeners(java.lang.String)
     */
    public PropertyChangeListener[] getPropertyChangeListeners(String propertyName)
    {
        return properties.getPropertyChangeListeners(propertyName);
    }

    /**
     * Show this step in DIAGRAM mode.
     * 
     * @author Martin Pecka
     */
    class FullscreenAction extends AbstractAction
    {
        /** */
        private static final long serialVersionUID = -2874524093660872372L;

        @Override
        public void actionPerformed(ActionEvent e)
        {
            properties.firePropertyChange("fullscreenModeRequested", false, true);
        }

    }

    /**
     * Zooms the step by 10% in.
     * 
     * @author Martin Pecka
     */
    class ZoomInAction extends AbstractAction
    {

        /** */
        private static final long serialVersionUID = -8216187646844261072L;

        @Override
        public void actionPerformed(ActionEvent e)
        {
            incZoom();
        }

    }

    /**
     * Zooms the step by 10% out.
     * 
     * @author Martin Pecka
     */
    class ZoomOutAction extends AbstractAction
    {

        /** */
        private static final long serialVersionUID = -4073236265184373224L;

        @Override
        public void actionPerformed(ActionEvent e)
        {
            decZoom();
        }

    }

    /**
     * Mouse event handling.
     * 
     * @author Martin Pecka
     */
    class MouseListener extends MouseAdapter
    {
        @Override
        public void mouseClicked(MouseEvent e)
        {
            if (displayMode.equals(DisplayMode.PAGE) && e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() > 1) {
                new FullscreenAction().actionPerformed(new ActionEvent(StepRendererWithControls.this,
                        ActionEvent.ACTION_LAST, "fullscreen"));
            }
        }

        @Override
        public void mouseWheelMoved(MouseWheelEvent e)
        {
            int steps = e.getWheelRotation();
            if (steps > 0) {
                for (int i = 0; i < steps; i++)
                    incZoom();
            } else {
                for (int i = steps; i < 0; i++)
                    decZoom();
            }
        }
    }
}

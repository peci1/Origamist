/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.gui.common;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.media.j3d.Appearance;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.GraphicsConfigTemplate3D;
import javax.media.j3d.PolygonAttributes;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.origamist.BackgroundImageSupport;
import javax.swing.origamist.BackgroundImageSupport.BackgroundRepeat;
import javax.swing.origamist.JMultilineLabel;
import javax.swing.origamist.JPanelWithOverlay;
import javax.swing.origamist.JToolBarWithBgImage;
import javax.vecmath.Color3f;
import javax.vecmath.Vector3d;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.sun.j3d.exp.swing.JCanvas3D;
import com.sun.j3d.utils.universe.SimpleUniverse;

import cz.cuni.mff.peckam.java.origamist.gui.viewer.DisplayMode;
import cz.cuni.mff.peckam.java.origamist.gui.viewer.OrigamiViewer;
import cz.cuni.mff.peckam.java.origamist.model.Origami;
import cz.cuni.mff.peckam.java.origamist.model.Step;
import cz.cuni.mff.peckam.java.origamist.model.UnitDimension;
import cz.cuni.mff.peckam.java.origamist.model.jaxb.ModelColors;
import cz.cuni.mff.peckam.java.origamist.model.jaxb.Unit;
import cz.cuni.mff.peckam.java.origamist.modelstate.ModelState;
import cz.cuni.mff.peckam.java.origamist.services.ServiceLocator;
import cz.cuni.mff.peckam.java.origamist.services.interfaces.ConfigurationManager;

/**
 * This panel renders the given state of the model.
 * 
 * @author Martin Pecka
 */
public class StepRenderer extends JPanelWithOverlay
{
    private static final long        serialVersionUID = 6989958008007575800L;

    /**
     * The origami diagram we are rendering.
     */
    protected Origami                origami          = null;

    /**
     * The step this renderer is rendering.
     */
    protected Step                   step             = null;

    /**
     * The background color.
     */
    protected Color                  backgroundColor  = null;

    /**
     * The label that shows the descripton of the shown step.
     */
    protected final JMultilineLabel  descLabel;

    /**
     * The canvas the model is rendered to.
     */
    protected JCanvas3D              canvas;

    /** The mode to display this renderer in. */
    protected DisplayMode            displayMode;

    /** The zoom of the step. */
    protected double                 zoom             = 100d;

    // COMPONENTS

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

    public StepRenderer()
    {
        setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));

        addMouseListener(new MouseListener());
        addMouseWheelListener(new MouseListener());

        canvas = createCanvas();

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
                descLabel.setText(step.getDescription(ServiceLocator.get(ConfigurationManager.class).get()
                        .getDiagramLocale()));
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
        getContent().add(canvas, BorderLayout.CENTER);
        // just a workaround - the canvas needs to know its size before the size of its container can be determined
        canvas.setSize(new Dimension(20, 20));
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

    /**
     * @return The configured <code>JCanvas3D</code>.
     */
    protected JCanvas3D createCanvas()
    {
        JCanvas3D canvas = new JCanvas3D(new GraphicsConfigTemplate3D());
        canvas.setOpaque(false);
        canvas.setResizeMode(JCanvas3D.RESIZE_DELAYED);
        return canvas;
    }

    public StepRenderer(Origami o, Step s)
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
        return origami;
    }

    /**
     * @param origami the origami to set
     */
    public void setOrigami(Origami origami)
    {
        this.origami = origami;
        setBackground(origami.getPaper().getColor().getBackground());
    }

    /**
     * @return the step
     */
    public Step getStep()
    {
        return step;
    }

    /**
     * @param stepId the step to set
     */
    public void setStep(final Step step)
    {
        Runnable run = new Runnable() {
            @Override
            public void run()
            {
                synchronized (StepRenderer.this) {
                    StepRenderer.this.step = step;
                    updateText();

                    // TODO refactor from now on

                    ModelState state = step.getModelState();

                    getContent().remove(canvas);
                    canvas = createCanvas();
                    getContent().add(canvas, BorderLayout.CENTER);
                    canvas.setSize(new Dimension(20, 20));

                    canvas.setSize(getWidth(), getHeight() - descLabel.getHeight());
                    canvas.setResizeMode(JCanvas3D.RESIZE_IMMEDIATELY);
                    SimpleUniverse universe = new SimpleUniverse(canvas.getOffscreenCanvas3D());

                    Appearance appearance = new Appearance();
                    Appearance appearance2 = new Appearance();

                    PolygonAttributes polyAttribs = new PolygonAttributes();
                    polyAttribs.setCullFace(PolygonAttributes.CULL_BACK);
                    // DEBUG IMPORTANT: The next line allows switching between wireframe and full filling mode
                    polyAttribs.setPolygonMode(PolygonAttributes.POLYGON_LINE);
                    appearance.setPolygonAttributes(polyAttribs);
                    appearance2.setPolygonAttributes(polyAttribs);

                    ModelColors paperColors = origami.getModel().getPaper().getColors();
                    ColoringAttributes colAttrs = new ColoringAttributes(new Color3f(paperColors.getForeground()),
                            ColoringAttributes.NICEST);
                    appearance.setColoringAttributes(colAttrs);
                    colAttrs = new ColoringAttributes(new Color3f(paperColors.getBackground()),
                            ColoringAttributes.NICEST);
                    appearance2.setColoringAttributes(colAttrs);

                    Transform3D transform = new Transform3D();
                    transform.setEuler(new Vector3d(state.getViewingAngle() - Math.PI / 2.0, 0, state.getRotation()));
                    // TODO adjust zoom according to paper size and renderer size - this is placeholder code
                    transform.setScale((step.getZoom() / 100d) * (zoom / 100d));
                    UnitDimension paperSize = origami.getModel().getPaper().getSize().convertTo(Unit.M);
                    transform
                            .setTranslation(new Vector3d(-paperSize.getWidth() / 2.0, -paperSize.getHeight() / 2.0, 0));
                    TransformGroup tGroup = new TransformGroup();
                    tGroup.setTransform(transform);

                    tGroup.addChild(new Shape3D(state.getTrianglesArray(), appearance));
                    tGroup.addChild(new Shape3D(state.getInverseTrianglesArray(), appearance2));
                    // tGroup.addChild(new Shape3D(state.getLineArray()));

                    BranchGroup contents = new BranchGroup();
                    contents.addChild(tGroup);

                    contents.compile(); // may cause unexpected problems - any consequent change of contents
                    // (or even reading of them) will produce an error

                    universe.getViewingPlatform().setNominalViewingTransform();
                    universe.addBranchGraph(contents);
                }
            }
        };

        if (SwingUtilities.isEventDispatchThread()) {
            new Thread(run).start();
        } else {
            run.run();
        }
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
        return zoom;
    }

    /**
     * @param zoom the zoom to set
     */
    public void setZoom(double zoom)
    {
        this.zoom = zoom;
        // reload the step
        setStep(step);
    }

    /**
     * Increase zoom by 10%.
     */
    public void incZoom()
    {
        setZoom(getZoom() + 10d);
    }

    /**
     * Decrease zoom by 10%.
     */
    public void decZoom()
    {
        setZoom(getZoom() - 10d);
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
            DiagramRenderer parent = null;
            Container comp = StepRenderer.this;
            while ((comp = comp.getParent()) != null) {
                if (comp instanceof DiagramRenderer) {
                    parent = (DiagramRenderer) comp;
                    break;
                }
            }

            ServiceLocator.get(OrigamiViewer.class).setDisplayMode(DisplayMode.DIAGRAM);
            if (parent != null) {
                parent.setStep(step);
            }
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
            super.mouseClicked(e);
            if (displayMode.equals(DisplayMode.PAGE) && e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() > 1) {
                new FullscreenAction().actionPerformed(new ActionEvent(StepRenderer.this, ActionEvent.ACTION_LAST,
                        "fullscreen"));
            }
        }

        @Override
        public void mouseWheelMoved(MouseWheelEvent e)
        {
            super.mouseWheelMoved(e);
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

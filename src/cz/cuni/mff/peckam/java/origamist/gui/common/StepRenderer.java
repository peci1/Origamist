/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.gui.common;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.InputMethodListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import javax.media.j3d.Appearance;
import javax.media.j3d.Behavior;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.Font3D;
import javax.media.j3d.FontExtrusion;
import javax.media.j3d.GraphicsConfigTemplate3D;
import javax.media.j3d.LineAttributes;
import javax.media.j3d.Material;
import javax.media.j3d.OrientedShape3D;
import javax.media.j3d.PolygonAttributes;
import javax.media.j3d.RenderingAttributes;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Text3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3d;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.sun.j3d.exp.swing.JCanvas3D;
import com.sun.j3d.utils.behaviors.mouse.MouseRotate;
import com.sun.j3d.utils.universe.SimpleUniverse;

import cz.cuni.mff.peckam.java.origamist.exceptions.InvalidOperationException;
import cz.cuni.mff.peckam.java.origamist.model.Origami;
import cz.cuni.mff.peckam.java.origamist.model.Step;
import cz.cuni.mff.peckam.java.origamist.model.UnitDimension;
import cz.cuni.mff.peckam.java.origamist.model.jaxb.ModelColors;
import cz.cuni.mff.peckam.java.origamist.model.jaxb.Unit;
import cz.cuni.mff.peckam.java.origamist.modelstate.MarkerRenderData;
import cz.cuni.mff.peckam.java.origamist.modelstate.ModelState;

/**
 * The panel for rendering a step.
 * 
 * Provides the following properties:
 * <ul>
 * <li>zoom</li>
 * </ul>
 * 
 * @author Martin Pecka
 */
public class StepRenderer extends JPanel
{
    /** */
    private static final long       serialVersionUID = 9198803673578003101L;

    /**
     * The origami diagram we are rendering.
     */
    protected Origami               origami          = null;

    /**
     * The step this renderer is rendering.
     */
    protected Step                  step             = null;

    /**
     * The canvas the model is rendered to.
     */
    protected JCanvas3D             canvas;

    /** The offscreen canvas used for drawing. */
    protected Canvas3D              offscreenCanvas;

    /** The universe we use. */
    protected SimpleUniverse        universe;

    /** The main transform used to display the step. */
    protected Transform3D           transform        = new Transform3D();

    /** The transform group containing the whole step. */
    protected TransformGroup        tGroup;

    /** The branch graph to be added to the scene. */
    protected BranchGroup           branchGraph      = null;

    /** The zoom of the step. */
    protected double                zoom             = 100d;

    /** The helper for properties. */
    protected PropertyChangeSupport listeners        = new PropertyChangeSupport(this);

    /** The font to use for drawing markers. */
    protected Font                  markerFont       = new Font("Arial", Font.BOLD, 12);

    /** The font color to use for drawing markers. */
    protected Color                 markerFontColor  = Color.BLACK;

    /**
     * 
     */
    public StepRenderer()
    {
        setLayout(new BorderLayout());

        // Subclassing JCanvas3D is needed to call the protected method.
        // The call is needed because it is a workaround for the offscreen canvas not being notified of AWT events
        // with no registered listeners.
        canvas = new JCanvas3D(new GraphicsConfigTemplate3D()) {
            /** */
            private static final long serialVersionUID = 1159847610761430144L;
            {
                enableEvents(AWTEvent.MOUSE_EVENT_MASK | AWTEvent.MOUSE_MOTION_EVENT_MASK
                        | AWTEvent.MOUSE_WHEEL_EVENT_MASK);
            }
        };

        canvas.setOpaque(false);
        canvas.setResizeMode(JCanvas3D.RESIZE_DELAYED);
        add(canvas, BorderLayout.CENTER);
        canvas.setSize(new Dimension(20, 20));
        canvas.setResizeMode(JCanvas3D.RESIZE_IMMEDIATELY);
        if (getWidth() > 0 && getHeight() > 0)
            canvas.setSize(getWidth(), getHeight());

        offscreenCanvas = canvas.getOffscreenCanvas3D();
        universe = new SimpleUniverse(offscreenCanvas);

        setOpaque(false);
        addMouseWheelListener(new MouseListener());
    }

    /**
     * @param origami
     * @param step
     */
    public StepRenderer(Origami origami, Step step)
    {
        this();
        setOrigami(origami);
        setStep(step);
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

                    if (step == null)
                        return;

                    if (getWidth() > 0 && getHeight() > 0)
                        canvas.setSize(getWidth(), getHeight());

                    try {
                        setupUniverse();
                    } catch (InvalidOperationException e) {
                        Logger.getLogger("application").l7dlog(Level.ERROR, "StepRenderer.InvalidOperationException",
                                new Object[] { StepRenderer.this.step.getId(), e.getOperation().toString() }, e);
                        // TODO some more clever handling of invalid operations
                    }

                    repaint();
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
     * @return The common attributes of polygons to use for rendering.
     */
    protected PolygonAttributes createPolygonAttributes()
    {
        PolygonAttributes polyAttribs = new PolygonAttributes();
        polyAttribs.setCullFace(PolygonAttributes.CULL_BACK);
        // DEBUG IMPORTANT: The next line allows switching between wireframe and full filling mode
        polyAttribs.setPolygonMode(PolygonAttributes.POLYGON_FILL);
        return polyAttribs;
    }

    /**
     * @return The appearance of triangles that represent the foreground of the paper.
     */
    protected Appearance createNormalTrianglesAppearance()
    {
        Appearance appearance = new Appearance();

        appearance.setPolygonAttributes(createPolygonAttributes());

        ModelColors paperColors = origami.getModel().getPaper().getColors();
        ColoringAttributes colAttrs = new ColoringAttributes(new Color3f(paperColors.getForeground()),
                ColoringAttributes.NICEST);
        appearance.setColoringAttributes(colAttrs);

        return appearance;
    }

    /**
     * @return The appearance of triangles that represent the background of the paper.
     */
    protected Appearance createInverseTrianglesAppearance()
    {
        Appearance appearance = new Appearance();

        PolygonAttributes attrs = createPolygonAttributes();
        attrs.setCullFace(PolygonAttributes.CULL_FRONT);
        appearance.setPolygonAttributes(attrs);

        ModelColors paperColors = origami.getModel().getPaper().getColors();
        ColoringAttributes colAttrs = new ColoringAttributes(new Color3f(paperColors.getBackground()),
                ColoringAttributes.NICEST);
        appearance.setColoringAttributes(colAttrs);

        return appearance;
    }

    /**
     * @return The appearance of triangles that represent the foreground of the paper.
     */
    protected Appearance createFoldLinesAppearance()
    {
        Appearance appearance = new Appearance();

        ColoringAttributes colAttrs = new ColoringAttributes(new Color3f(Color.black), ColoringAttributes.NICEST);
        appearance.setColoringAttributes(colAttrs);

        LineAttributes lineAttrs = new LineAttributes();
        lineAttrs.setLineWidth(2);
        appearance.setLineAttributes(lineAttrs);

        return appearance;
    }

    /**
     * Set this.transform to a new value.
     * 
     * @return The transform used for the step just after initialization.
     * 
     * @throws InvalidOperationException If the model state cannot be gotten due to invalid operations.
     */
    protected Transform3D setupTransform() throws InvalidOperationException
    {
        ModelState state = step.getModelState();

        transform.setEuler(new Vector3d(state.getViewingAngle() - Math.PI / 2.0, 0, state.getRotation()));
        // TODO adjust zoom according to paper size and renderer size - this is placeholder code
        transform.setScale((step.getZoom() / 100d) * (zoom / 100d));
        // transform
        // .setScale(new Vector3d(5 * transform.getScale(), 5 * transform.getScale(), 500 * transform.getScale()));
        UnitDimension paperSize = origami.getModel().getPaper().getSize().convertTo(Unit.M);
        transform.setTranslation(new Vector3d(-paperSize.getWidth() / 2.0, -paperSize.getHeight() / 2.0, 0));

        return transform;
    }

    /**
     * @return The transform groups containing nodes for displaying markers.
     */
    protected TransformGroup getMarkerGroups()
    {
        ModelState state = step.getModelState();
        TransformGroup result = new TransformGroup();

        double oneRelInMeters = origami.getModel().getPaper().getOneRelInMeters();
        Font3D font = new Font3D(markerFont, new FontExtrusion());
        // scale of the 3D font; this should make 12pt font be 1/10 of the side of the paper large
        double scale = 1d / 12d * oneRelInMeters * 1d / 10d;

        Appearance textAp = new Appearance();
        Material m = new Material();
        textAp.setMaterial(m);
        textAp.setColoringAttributes(new ColoringAttributes(new Color3f(markerFontColor), ColoringAttributes.FASTEST));
        if (textAp.getRenderingAttributes() == null)
            textAp.setRenderingAttributes(new RenderingAttributes());
        // draw markers always on the top
        textAp.getRenderingAttributes().setDepthTestFunction(RenderingAttributes.ALWAYS);

        for (MarkerRenderData marker : state.getMarkerRenderData()) {
            TransformGroup group = new TransformGroup();
            group.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
            group.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

            // we use Text3D here bacause Text2D looks very, very blurry (even if it isn't zoomed)
            Text3D textGeom = new Text3D(font, marker.getText());

            OrientedShape3D text = new OrientedShape3D(textGeom, textAp, OrientedShape3D.ROTATE_ABOUT_POINT,
                    new Point3f());
            group.addChild(text);

            Transform3D transform = new Transform3D();
            transform.setScale(scale);
            Vector3d translation = new Vector3d(marker.getPoint3d());
            translation.scale(oneRelInMeters);
            transform.setTranslation(translation);

            group.setTransform(transform);

            // TODO add a behavior for fine-positioning colliding markers

            result.addChild(group);
        }
        return result;
    }

    /**
     * Set this.tGroup to a new value.
     * 
     * @return The transform group that contains all nodes.
     * 
     * @throws InvalidOperationException If the model state cannot be gotten due to invalid operations.
     */
    protected TransformGroup setupTGroup() throws InvalidOperationException
    {
        try {
            setupTransform();
            Appearance appearance = createNormalTrianglesAppearance();
            Appearance appearance2 = createInverseTrianglesAppearance();
            Appearance appearance3 = createFoldLinesAppearance();

            ModelState state = step.getModelState();

            tGroup = new TransformGroup();
            tGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
            tGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
            tGroup.setTransform(transform);

            tGroup.addChild(getMarkerGroups());

            tGroup.addChild(new Shape3D(state.getTrianglesArray(), appearance));
            tGroup.addChild(new Shape3D(state.getTrianglesArray(), appearance2));
            tGroup.addChild(new Shape3D(state.getLineArray(), appearance3));

            return tGroup;
        } catch (InvalidOperationException e) {
            tGroup = new TransformGroup();
            // TODO create an ErrorTransformGroup that would signalize to the user that an operatino is invalid
            throw e;
        }
    }

    /**
     * Return the compiled BranchGroup containing this.tGroup and all defined top-level behaviors. Also save the group
     * to this.branchGraph.
     * 
     * @return The compiled BranchGroup containing this.tGroup and all defined top-level behaviors.
     * 
     * @throws InvalidOperationException If the branch graph cannot be created due to an invalid operation in the model.
     */
    protected BranchGroup createBranchGraph() throws InvalidOperationException
    {
        try {
            setupTGroup();

            branchGraph = new BranchGroup();
            branchGraph.addChild(tGroup);

            // TODO now these three lines enable rotating. Either make a whole concept of controlling the
            // displayed step, or delete them
            Behavior rotate = new MouseRotate(tGroup);
            rotate.setSchedulingBounds(new BoundingSphere(new Point3d(), 1000000d));
            branchGraph.addChild(rotate);

            branchGraph.compile(); // may cause unexpected problems - any consequent change of contents
            // (or even reading of them) will produce an error if you don't set the proper capability

            return branchGraph;
        } catch (InvalidOperationException e) {
            branchGraph = new BranchGroup();
            branchGraph.addChild(tGroup);
            branchGraph.compile(); // may cause unexpected problems - any consequent change of contents
            // (or even reading of them) will produce an error if you don't set the proper capability
            throw e;
        }
    }

    /**
     * Set a new universe corresponding to the current step to this.universe.
     * 
     * @throws InvalidOperationException If the universe cannot be setup due to an invalid operation in the model.
     */
    protected void setupUniverse() throws InvalidOperationException
    {
        universe.getViewer().setViewingPlatform(null);
        universe.getViewer().getView().removeAllCanvas3Ds();

        universe = new SimpleUniverse(offscreenCanvas);
        universe.getViewingPlatform().setNominalViewingTransform();

        try {
            createBranchGraph();
            universe.addBranchGraph(branchGraph);
        } catch (InvalidOperationException e) {
            universe.addBranchGraph(branchGraph);
            throw e;
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
        if (!isEnabled())
            return;

        if (zoom < 25d)
            return;

        double oldZoom = this.zoom;
        this.zoom = zoom;
        listeners.firePropertyChange("zoom", oldZoom, zoom);

        transform.setScale((step.getZoom() / 100d) * (zoom / 100d));
        tGroup.setTransform(transform);
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
     * @return The font to use for drawing markers.
     */
    Font getMarkerFont()
    {
        return markerFont;
    }

    /**
     * @param markerFont The font to use for drawing markers.
     */
    void setMarkerFont(Font markerFont)
    {
        this.markerFont = markerFont;
    }

    /**
     * @return The font color to use for drawing markers.
     */
    Color getMarkerFontColor()
    {
        return markerFontColor;
    }

    /**
     * @param markerFontColor The font color to use for drawing markers.
     */
    void setMarkerFontColor(Color markerFontColor)
    {
        this.markerFontColor = markerFontColor;
    }

    /**
     * @param listener
     * @see java.beans.PropertyChangeSupport#addPropertyChangeListener(java.beans.PropertyChangeListener)
     */
    public void addPropertyChangeListener(PropertyChangeListener listener)
    {
        listeners.addPropertyChangeListener(listener);
    }

    /**
     * @param listener
     * @see java.beans.PropertyChangeSupport#removePropertyChangeListener(java.beans.PropertyChangeListener)
     */
    public void removePropertyChangeListener(PropertyChangeListener listener)
    {
        listeners.removePropertyChangeListener(listener);
    }

    /**
     * @return
     * @see java.beans.PropertyChangeSupport#getPropertyChangeListeners()
     */
    public PropertyChangeListener[] getPropertyChangeListeners()
    {
        return listeners.getPropertyChangeListeners();
    }

    /**
     * @param propertyName
     * @param listener
     * @see java.beans.PropertyChangeSupport#addPropertyChangeListener(java.lang.String,
     *      java.beans.PropertyChangeListener)
     */
    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener)
    {
        listeners.addPropertyChangeListener(propertyName, listener);
    }

    /**
     * @param propertyName
     * @param listener
     * @see java.beans.PropertyChangeSupport#removePropertyChangeListener(java.lang.String,
     *      java.beans.PropertyChangeListener)
     */
    public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener)
    {
        listeners.removePropertyChangeListener(propertyName, listener);
    }

    /**
     * @param propertyName
     * @return
     * @see java.beans.PropertyChangeSupport#getPropertyChangeListeners(java.lang.String)
     */
    public PropertyChangeListener[] getPropertyChangeListeners(String propertyName)
    {
        return listeners.getPropertyChangeListeners(propertyName);
    }

    /**
     * @param l
     * @see java.awt.Component#addKeyListener(java.awt.event.KeyListener)
     */
    public synchronized void addKeyListener(KeyListener l)
    {
        canvas.addKeyListener(l);
    }

    /**
     * @param l
     * @see java.awt.Component#addMouseListener(java.awt.event.MouseListener)
     */
    public synchronized void addMouseListener(java.awt.event.MouseListener l)
    {
        canvas.addMouseListener(l);
    }

    /**
     * @param l
     * @see java.awt.Component#addMouseMotionListener(java.awt.event.MouseMotionListener)
     */
    public synchronized void addMouseMotionListener(MouseMotionListener l)
    {
        canvas.addMouseMotionListener(l);
    }

    /**
     * @param l
     * @see java.awt.Component#addMouseWheelListener(java.awt.event.MouseWheelListener)
     */
    public synchronized void addMouseWheelListener(MouseWheelListener l)
    {
        canvas.addMouseWheelListener(l);
    }

    /**
     * @param l
     * @see java.awt.Component#addInputMethodListener(java.awt.event.InputMethodListener)
     */
    public synchronized void addInputMethodListener(InputMethodListener l)
    {
        canvas.addInputMethodListener(l);
    }

    /**
     * Mouse event handling.
     * 
     * @author Martin Pecka
     */
    protected class MouseListener extends MouseAdapter
    {
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

/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.gui.common;

import static java.lang.Math.abs;

import java.awt.AWTEvent;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.InputMethodListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.media.j3d.Appearance;
import javax.media.j3d.Behavior;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.Font3D;
import javax.media.j3d.FontExtrusion;
import javax.media.j3d.GraphicsConfigTemplate3D;
import javax.media.j3d.ImageComponent2D;
import javax.media.j3d.LineAttributes;
import javax.media.j3d.Material;
import javax.media.j3d.Node;
import javax.media.j3d.OrderedGroup;
import javax.media.j3d.OrientedShape3D;
import javax.media.j3d.PolygonAttributes;
import javax.media.j3d.RenderingAttributes;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Text3D;
import javax.media.j3d.Texture;
import javax.media.j3d.Texture2D;
import javax.media.j3d.TextureAttributes;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.TransparencyAttributes;
import javax.media.j3d.TriangleArray;
import javax.swing.AbstractAction;
import javax.swing.Action;
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
import com.sun.j3d.utils.picking.PickCanvas;
import com.sun.j3d.utils.picking.PickResult;
import com.sun.j3d.utils.picking.PickTool;
import com.sun.j3d.utils.universe.SimpleUniverse;

import cz.cuni.mff.peckam.java.origamist.exceptions.InvalidOperationException;
import cz.cuni.mff.peckam.java.origamist.math.Segment2d;
import cz.cuni.mff.peckam.java.origamist.model.Origami;
import cz.cuni.mff.peckam.java.origamist.model.Step;
import cz.cuni.mff.peckam.java.origamist.model.UnitDimension;
import cz.cuni.mff.peckam.java.origamist.model.jaxb.Unit;
import cz.cuni.mff.peckam.java.origamist.modelstate.Fold;
import cz.cuni.mff.peckam.java.origamist.modelstate.FoldLine;
import cz.cuni.mff.peckam.java.origamist.modelstate.Layer;
import cz.cuni.mff.peckam.java.origamist.modelstate.MarkerRenderData;
import cz.cuni.mff.peckam.java.origamist.modelstate.ModelState;

/**
 * The panel for rendering a step.
 * 
 * Provides the following properties:
 * <ul>
 * <li>pickMode</li>
 * <li>zoom</li>
 * </ul>
 * 
 * @author Martin Pecka
 */
public class StepRenderer extends JPanel
{
    /** */
    private static final long         serialVersionUID       = 9198803673578003101L;

    /**
     * The origami diagram we are rendering.
     */
    protected Origami                 origami                = null;

    /**
     * The step this renderer is rendering.
     */
    protected Step                    step                   = null;

    /**
     * The canvas the model is rendered to.
     */
    protected JCanvas3D               canvas;

    /** The offscreen canvas used for drawing. */
    protected Canvas3D                offscreenCanvas;

    /** The canvas support for picking. Automatically updated when branch graph changes. */
    protected PickCanvas              pickCanvas;

    /** The universe we use. */
    protected SimpleUniverse          universe;

    /** The main transform used to display the step. */
    protected Transform3D             transform              = new Transform3D();

    /** The transform group containing the whole step. */
    protected TransformGroup          tGroup;

    /** The branch graph to be added to the scene. */
    protected BranchGroup             branchGraph            = null;

    /** The zoom of the step. */
    protected double                  zoom                   = 100d;

    /** The helper for properties. */
    protected PropertyChangeSupport   listeners              = new PropertyChangeSupport(this);

    /** The font to use for drawing markers. */
    protected Font                    markerFont             = new Font("Arial", Font.BOLD, 12);

    /** The size of the surface texture. */
    protected final static int        TEXTURE_SIZE           = 512;

    /** Cached textures for top and bottom side of the paper. */
    protected Texture                 topTexture, bottomTexture;

    /** The maximum level of anisotropic filter that is supported by the current HW. */
    protected final float             maxAnisotropyLevel;

    /** The list of layers available by the last performed pick operation. */
    protected List<TransformGroup>    availableLayers        = new LinkedList<TransformGroup>();

    /** The currently highlighted (picked) layer. */
    protected TransformGroup          highlighted            = null;

    /** The currently selected layers. */
    protected HashSet<TransformGroup> selectedLayers         = new HashSet<TransformGroup>();

    /** The type of primitves the user can pick. */
    protected PickMode                pickMode               = PickMode.POINT;

    /** The manager for changing layer appearances. */
    protected LayerAppearanceManager  layerAppearanceManager = new LayerAppearanceManager();

    /** The manager of {@link StepRenderer}'s colors. */
    protected ColorManager            colorManager           = new ColorManager(Color.WHITE, Color.WHITE);

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
        updatePickCanvas();

        setOpaque(false);
        MouseListener listener = new MouseListener();
        addMouseWheelListener(listener);
        addMouseMotionListener(listener);
        addMouseListener(listener);

        addPropertyChangeListener("pickMode", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt)
            {
                if (pickMode == PickMode.POINT) {
                    StepRenderer.this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                } else if (pickMode == PickMode.LINE) {
                    StepRenderer.this.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
                } else if (pickMode == PickMode.LAYER) {
                    StepRenderer.this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                }
            }
        });

        maxAnisotropyLevel = (Float) offscreenCanvas.queryProperties().get("textureAnisotropicFilterDegreeMax");
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
        if (origami != null) {
            setBackground(origami.getPaper().getColor().getBackground());
            colorManager = new ColorManager(origami.getModel().getPaper().getBackgroundColor(), origami.getModel()
                    .getPaper().getForegroundColor());
        } else {
            setBackground(Color.GRAY);
        }
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

                    availableLayers.clear();
                    highlighted = null;
                    selectedLayers.clear();
                    topTexture = null;
                    bottomTexture = null;

                    // branchGraph changes, so we have to recreate the pick canvas
                    updatePickCanvas();

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
        polyAttribs.setCapability(PolygonAttributes.ALLOW_OFFSET_WRITE);
        return polyAttribs;
    }

    /**
     * @return The buffered image used for drawing textures.
     */
    protected BufferedImage createTextureBuffer()
    {
        return new BufferedImage(TEXTURE_SIZE, TEXTURE_SIZE, BufferedImage.TYPE_INT_RGB);
    }

    /**
     * Initialize the texture graphics to be able to draw fold lines on it after this method finishes.
     * 
     * @param buffer The buffer to create the graphics from.
     * @param bgColor The background color of the graphics.
     * 
     * @return The initialized graphics object.
     */
    protected Graphics2D initTextureGraphics(BufferedImage buffer, Color bgColor)
    {
        Graphics2D graphics = buffer.createGraphics();

        graphics.setColor(bgColor);
        graphics.setBackground(bgColor);
        graphics.clearRect(0, 0, buffer.getWidth(), buffer.getHeight());

        graphics.setColor(Color.BLACK);
        graphics.setStroke(new BasicStroke(0.5f));

        return graphics;
    }

    /**
     * Create a texture with image taken from the given buffer. Also set some desired texture attributes.
     * 
     * @param buffer The buffer to take the image from.
     * @return A texture.
     */
    protected Texture createTextureFromBuffer(BufferedImage buffer)
    {
        Texture texture = new Texture2D(Texture2D.BASE_LEVEL, Texture2D.RGB, buffer.getWidth(), buffer.getHeight());

        ImageComponent2D image = new ImageComponent2D(ImageComponent2D.FORMAT_RGB, buffer);

        texture.setMagFilter(Texture.NICEST);
        texture.setMinFilter(Texture.NICEST);
        texture.setAnisotropicFilterMode(Texture.ANISOTROPIC_SINGLE_VALUE);
        texture.setAnisotropicFilterDegree(Math.min(4f, maxAnisotropyLevel));
        texture.setImage(0, image);

        return texture;
    }

    /**
     * @return Return (and generate if it doesn't exist) the texture for the top side of the paper.
     */
    protected Texture getTopTexture()
    {
        if (topTexture == null) {
            BufferedImage buffer = createTextureBuffer();
            Graphics2D graphics = initTextureGraphics(buffer, colorManager.getForeground());

            int w = buffer.getWidth();
            for (Fold f : step.getModelState().getFolds()) {
                for (FoldLine line : f.getLines()) {
                    Segment2d seg = line.getSegment2d();
                    graphics.drawLine((int) (seg.getP1().x * w), (int) (w - seg.getP1().y * w),
                            (int) (seg.getP2().x * w), (int) (w - seg.getP2().y * w));
                }
            }

            topTexture = createTextureFromBuffer(buffer);
        }
        return topTexture;
    }

    /**
     * @return Return (and generate if it doesn't exist) the texture for the bottom side of the paper.
     */
    protected Texture getBottomTexture()
    {
        if (bottomTexture == null) {
            BufferedImage buffer = createTextureBuffer();
            Graphics2D graphics = initTextureGraphics(buffer, colorManager.getBackground());

            int w = buffer.getWidth();
            for (Fold f : step.getModelState().getFolds()) {
                for (FoldLine line : f.getLines()) {
                    Segment2d seg = line.getSegment2d();
                    graphics.drawLine((int) (seg.getP1().x * w), (int) (w - seg.getP1().y * w),
                            (int) (seg.getP2().x * w), (int) (w - seg.getP2().y * w));
                }
            }

            bottomTexture = createTextureFromBuffer(buffer);
        }
        return bottomTexture;
    }

    /**
     * @return The appearance of triangles that is common for both sides of the paper.
     */
    protected Appearance createBaseTrianglesAppearance()
    {
        Appearance appearance = new Appearance();

        appearance.setPolygonAttributes(createPolygonAttributes());

        ColoringAttributes colAttrs = new ColoringAttributes();
        colAttrs.setShadeModel(ColoringAttributes.NICEST);
        colAttrs.setCapability(ColoringAttributes.ALLOW_COLOR_WRITE);
        appearance.setColoringAttributes(colAttrs);

        appearance.setTextureAttributes(new TextureAttributes());
        appearance.getTextureAttributes().setPerspectiveCorrectionMode(TextureAttributes.NICEST);
        appearance.getTextureAttributes().setTextureMode(TextureAttributes.COMBINE);

        appearance.setRenderingAttributes(new RenderingAttributes());

        appearance.setTransparencyAttributes(new TransparencyAttributes());
        appearance.getTransparencyAttributes().setCapability(TransparencyAttributes.ALLOW_VALUE_WRITE);
        appearance.getTransparencyAttributes().setTransparencyMode(TransparencyAttributes.NICEST);

        return appearance;
    }

    /**
     * @return The appearance of triangles that represent the foreground of the paper.
     */
    protected Appearance createNormalTrianglesAppearance()
    {
        Appearance appearance = createBaseTrianglesAppearance();

        appearance.getColoringAttributes().setColor(colorManager.getForeground3f());

        appearance.setTexture(getTopTexture());

        return appearance;
    }

    /**
     * @return The appearance of triangles that represent the background of the paper.
     */
    protected Appearance createInverseTrianglesAppearance()
    {
        Appearance appearance = createBaseTrianglesAppearance();

        appearance.getPolygonAttributes().setCullFace(PolygonAttributes.CULL_FRONT);

        appearance.getColoringAttributes().setColor(colorManager.getBackground3f());

        appearance.setTexture(getBottomTexture());

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

        final LineAttributes lineAttrs = new LineAttributes();
        final float lineWidth = 1f;
        lineAttrs.setLineWidth(lineWidth);
        lineAttrs.setLineAntialiasingEnable(true);
        lineAttrs.setCapability(LineAttributes.ALLOW_WIDTH_WRITE);
        appearance.setLineAttributes(lineAttrs);

        addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt)
            {
                if (evt.getPropertyName().equals("zoom")) {
                    lineAttrs.setLineWidth(lineWidth * (float) (getZoom() / 100f));
                }
            }
        });

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
        final TransformGroup result = new TransformGroup();

        double oneRelInMeters = origami.getModel().getPaper().getOneRelInMeters();
        Font3D font = new Font3D(markerFont, new FontExtrusion());
        // scale of the 3D font; this should make 12pt font be 1/10 of the side of the paper large
        double scale = 1d / 12d * oneRelInMeters * 1d / 10d;

        Appearance textAp = new Appearance();
        Material m = new Material();
        textAp.setMaterial(m);
        textAp.setColoringAttributes(new ColoringAttributes(colorManager.getMarker3f(), ColoringAttributes.FASTEST));
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

            ModelState state = step.getModelState();

            tGroup = new TransformGroup();
            tGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
            tGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
            tGroup.setTransform(transform);

            OrderedGroup og = new OrderedGroup();

            TransformGroup model = new TransformGroup();
            model.setPickable(true);

            TriangleArray[] triangleArrays = state.getTrianglesArrays();
            Shape3D top, bottom;
            Appearance appearance;
            Appearance appearance2;
            int i = 0;
            for (Layer layer : state.getLayers()) {
                TransformGroup group = new TransformGroup();
                group.setBoundsAutoCompute(true);
                group.setUserData(layer);
                group.setPickable(true);
                group.setCapability(Shape3D.ENABLE_PICK_REPORTING);

                appearance = createNormalTrianglesAppearance();
                appearance2 = createInverseTrianglesAppearance();

                top = new Shape3D(triangleArrays[i], appearance);
                bottom = new Shape3D(triangleArrays[i], appearance2);

                group.addChild(top);
                group.addChild(bottom);

                model.addChild(group);

                i++;
            }

            Appearance appearance3 = createFoldLinesAppearance();
            model.addChild(new Shape3D(state.getLineArray(), appearance3));

            og.addChild(model);

            og.addChild(getMarkerGroups());

            tGroup.addChild(og);

            return tGroup;
        } catch (InvalidOperationException e) {
            tGroup = new TransformGroup();
            // TODO create an ErrorTransformGroup that would signalize to the user that an operation is invalid
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
            branchGraph = new BranchGroup();

            setupTGroup();

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
     * A callback to be called when picking is to be performed.
     * 
     * The given coordinates are relative to the canvas.
     * 
     * @param x The x coordinte in the canvas.
     * @param y The y coordinte in the canvas.
     * @param e The mouse event that caused this callback to be called.
     */
    protected void pick(int x, int y, MouseEvent e)
    {
        pickCanvas.setShapeLocation(x, y);

        List<PickResult> results = pickMode.filterPickResults(pickCanvas.pickAllSorted());

        if (results.size() > 0) {
            if (pickMode == PickMode.LAYER) {
                boolean containsHighlighted = false;
                List<TransformGroup> newAvailableLayers = new LinkedList<TransformGroup>();
                if (results.size() == availableLayers.size()) {
                    Iterator<TransformGroup> it = availableLayers.iterator();
                    boolean different = false;
                    for (PickResult r : results) {
                        TransformGroup tg = (TransformGroup) r.getNode(PickResult.TRANSFORM_GROUP);
                        if (!different && it.next() != tg)
                            different = true;
                        if (tg == highlighted)
                            containsHighlighted = true;
                        newAvailableLayers.add(tg);
                    }
                    if (!different)
                        return;
                } else {
                    for (PickResult r : results) {
                        TransformGroup tg = (TransformGroup) r.getNode(PickResult.TRANSFORM_GROUP);
                        if (tg == highlighted)
                            containsHighlighted = true;
                        newAvailableLayers.add(tg);
                    }
                }
                availableLayers = newAvailableLayers;

                if (containsHighlighted)
                    return;

                setHighlightedLayer(availableLayers.get(0));
            }
        } else if (highlighted != null) {
            setHighlightedLayer(null);
            availableLayers.clear();
        }

    }

    /**
     * Call this method to update the pick canvas after offscreenCanvas or branchGraph changes.
     */
    protected void updatePickCanvas()
    {
        pickCanvas = new PickCanvas(offscreenCanvas, branchGraph);
        pickCanvas.setMode(PickTool.GEOMETRY_INTERSECT_INFO);
        pickCanvas.setTolerance(3f);
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
     * @return The type of primitives the user can pick.
     */
    public PickMode getPickMode()
    {
        return pickMode;
    }

    /**
     * @param pickMode The type of primitives the user can pick.
     */
    public void setPickMode(PickMode pickMode)
    {
        PickMode oldPickMode = this.pickMode;
        this.pickMode = pickMode;
        listeners.firePropertyChange("pickMode", oldPickMode, pickMode);
    }

    /**
     * Performs the changes needed to make a layer highlighted.
     * 
     * If another layer has been highlighted, un-highlight it before highlighting the new one.
     * 
     * If the given layer already has been highlighted, nothing happens.
     * 
     * @param layer The layer to highlight. Pass <code>null</code> to clear the highlight.
     */
    protected void setHighlightedLayer(TransformGroup layer)
    {
        if (layer == highlighted)
            return;

        if (highlighted != null) {
            if (!selectedLayers.contains(highlighted))
                layerAppearanceManager.setAppearance(highlighted, LayerState.NORMAL);
            else
                layerAppearanceManager.setAppearance(highlighted, LayerState.SELECTED);
            highlighted = null;
        }

        if (layer != null) {
            highlighted = layer;
            if (!selectedLayers.contains(layer))
                layerAppearanceManager.setAppearance(layer, LayerState.HIGHLIGHTED);
            else
                layerAppearanceManager.setAppearance(layer, LayerState.SELECTED_HIGHLIGHTED);
        }
    }

    /**
     * Performs the changes needed to make a layer selected.
     * 
     * If the layer has already been selected, nothing happens.
     * 
     * @param layer The layer to select.
     */
    protected void selectLayer(TransformGroup layer)
    {
        if (selectedLayers.contains(layer))
            return;

        if (layer != highlighted)
            layerAppearanceManager.setAppearance(layer, LayerState.SELECTED);
        else
            layerAppearanceManager.setAppearance(layer, LayerState.SELECTED_HIGHLIGHTED);

        selectedLayers.add(layer);
    }

    /**
     * Performs the changes needed to make a selected layer not selected.
     * 
     * If the given layer hasn't been selected, nothing happens.
     * 
     * @param layer The layer to deselect.
     */
    protected void deselectLayer(TransformGroup layer)
    {
        if (selectedLayers.remove(layer)) {
            if (layer != highlighted)
                layerAppearanceManager.setAppearance(layer, LayerState.NORMAL);
            else
                layerAppearanceManager.setAppearance(layer, LayerState.HIGHLIGHTED);
        }
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
     * Mouse event and picking handling.
     * 
     * @author Martin Pecka
     */
    protected class MouseListener extends MouseAdapter
    {
        @Override
        public void mouseWheelMoved(MouseWheelEvent e)
        {
            int steps = e.getWheelRotation();
            if (steps == 0)
                return;

            if (e.isControlDown()) {
                if (steps > 0) {
                    Action action = new ZoomInAction();
                    ActionEvent event = new ActionEvent(StepRenderer.this, ActionEvent.ACTION_FIRST, "zoomIn");
                    for (int i = 0; i < steps; i++)
                        action.actionPerformed(event);
                } else {
                    Action action = new ZoomOutAction();
                    ActionEvent event = new ActionEvent(StepRenderer.this, ActionEvent.ACTION_FIRST, "zoomOut");
                    for (int i = steps; i < 0; i++)
                        action.actionPerformed(event);
                }
                e.consume();
            } else if (availableLayers.size() > 1 && highlighted != null) {
                // perform selection among available layers
                if (steps > 0) {
                    Action action = new HighlightNextLayerAction();
                    ActionEvent event = new ActionEvent(StepRenderer.this, ActionEvent.ACTION_FIRST,
                            "highlightNextLayer");
                    action.actionPerformed(event);
                } else if (steps < 0) {
                    Action action = new HighlightPreviousLayerAction();
                    ActionEvent event = new ActionEvent(StepRenderer.this, ActionEvent.ACTION_FIRST,
                            "highlightPreviousLayer");
                    action.actionPerformed(event);
                }
                e.consume();
            }
        }

        @Override
        public void mouseMoved(MouseEvent e)
        {
            pick(e.getX(), e.getY(), e);
        }

        @Override
        public void mouseClicked(MouseEvent e)
        {
            if (pickMode == PickMode.LAYER && e.getButton() == MouseEvent.BUTTON1 && highlighted != null) {
                Action action = new ToggleHighlightedLayerSelectionAction();
                ActionEvent event = new ActionEvent(StepRenderer.this, ActionEvent.ACTION_FIRST,
                        "toggleHigghlightedLayerSelection");
                action.actionPerformed(event);
            }
        }

        @Override
        public void mousePressed(MouseEvent e)
        {
            int mods = e.getModifiersEx();
            int middle = MouseEvent.BUTTON2_DOWN_MASK;
            int both = MouseEvent.BUTTON1_DOWN_MASK | MouseEvent.BUTTON3_DOWN_MASK;
            // either a middle button click or left+right button simultaneous click
            if (((mods & middle) == middle) || ((mods & both) == both)) {
                Action action = new TogglePickModeAction();
                ActionEvent event = new ActionEvent(StepRenderer.this, ActionEvent.ACTION_FIRST, "togglePickMode");
                action.actionPerformed(event);
            }
        }
    }

    protected class ZoomInAction extends AbstractAction
    {
        /** */
        private static final long serialVersionUID = 313512643556762110L;

        @Override
        public void actionPerformed(ActionEvent e)
        {
            incZoom();
        }

    }

    protected class ZoomOutAction extends AbstractAction
    {

        /** */
        private static final long serialVersionUID = -5340289900894828612L;

        @Override
        public void actionPerformed(ActionEvent e)
        {
            decZoom();
        }

    }

    protected class HighlightNextLayerAction extends AbstractAction
    {

        /** */
        private static final long serialVersionUID = -3089302395435461134L;

        @Override
        public void actionPerformed(ActionEvent e)
        {
            int selIndex = availableLayers.indexOf(highlighted);

            if (selIndex > -1) {
                selIndex = (selIndex + 1) % availableLayers.size();
                setHighlightedLayer(availableLayers.get(selIndex));
            }
        }

    }

    protected class HighlightPreviousLayerAction extends AbstractAction
    {
        /** */
        private static final long serialVersionUID = -3089302395435461134L;

        @Override
        public void actionPerformed(ActionEvent e)
        {
            int selIndex = availableLayers.indexOf(highlighted);

            if (selIndex > -1) {
                selIndex = selIndex - 1;
                if (selIndex == -1)
                    selIndex = availableLayers.size() - 1;
                setHighlightedLayer(availableLayers.get(selIndex));
            }
        }

    }

    protected class TogglePickModeAction extends AbstractAction
    {
        /** */
        private static final long serialVersionUID = -5513138483903360858L;

        @Override
        public void actionPerformed(ActionEvent e)
        {
            setPickMode(pickMode.getNext());
        }

    }

    protected class ToggleHighlightedLayerSelectionAction extends AbstractAction
    {
        /** */
        private static final long serialVersionUID = -1297141033881147805L;

        @Override
        public void actionPerformed(ActionEvent e)
        {
            if (highlighted == null)
                return;

            if (selectedLayers.contains(highlighted)) {
                deselectLayer(highlighted);
            } else {
                selectLayer(highlighted);
            }
        }

    }

    /**
     * The type of primitives the user can pick.
     * 
     * @author Martin Pecka
     */
    protected enum PickMode
    {
        POINT
        {
            @Override
            protected PickMode getNext()
            {
                return LINE;
            }

            @Override
            protected List<PickResult> filterPickResults(PickResult[] results)
            {
                return new LinkedList<PickResult>();
            }
        },
        LINE
        {
            @Override
            protected PickMode getNext()
            {
                return LAYER;
            }

            @Override
            protected List<PickResult> filterPickResults(PickResult[] results)
            {
                return new LinkedList<PickResult>();
            }
        },
        LAYER
        {
            @Override
            protected PickMode getNext()
            {
                return POINT;
            }

            @Override
            protected List<PickResult> filterPickResults(PickResult[] results)
            {
                List<PickResult> result = new LinkedList<PickResult>();
                if (results == null)
                    return result;

                Node node;
                for (PickResult r : results) {
                    node = r.getNode(PickResult.TRANSFORM_GROUP);
                    if (node != null && node.getUserData() instanceof Layer) {
                        // we have two shapes for each layer and we just want one of them to appear in the list
                        if (result.size() == 0
                                || result.get(result.size() - 1).getNode(PickResult.TRANSFORM_GROUP) != node)
                            result.add(r);
                    }
                }
                return result;
            }
        };

        /**
         * @return The next pick mode in a cycle.
         */
        protected abstract PickMode getNext();

        /**
         * Return only those pick results this pick mode is interested in.
         * 
         * @param results Some pick results to filter.
         * @return The filtered pick results.
         */
        protected abstract List<PickResult> filterPickResults(PickResult[] results);
    }

    /**
     * A state of a layer.
     * 
     * @author Martin Pecka
     */
    protected enum LayerState
    {
        /** Normal appearance. */
        NORMAL,
        /** Highlighted layer. */
        HIGHLIGHTED,
        /** Selected non-highlighted layer. */
        SELECTED,
        /** Selected highlighted layer. */
        SELECTED_HIGHLIGHTED
    }

    /**
     * A manager for changing layer appearance.
     * 
     * @author Martin Pecka
     */
    protected class LayerAppearanceManager
    {
        /**
         * Apply the given appearance on the given layer.
         * 
         * @param layer The layer to apply the appearance on.
         * @param state The state to derive appearance from.
         */
        protected void setAppearance(TransformGroup layer, LayerState state)
        {
            assert layer.numChildren() == 2;

            Enumeration<?> children = layer.getAllChildren();

            Shape3D shape = (Shape3D) children.nextElement();

            switch (state) {
                case NORMAL:
                    shape.getAppearance().getColoringAttributes().setColor(colorManager.getForeground3f());
                    shape.getAppearance().getPolygonAttributes().setPolygonOffset(0f);
                    shape.getAppearance().getPolygonAttributes().setPolygonOffsetFactor(0f);
                    shape.getAppearance().getTransparencyAttributes().setTransparency(0f);
                    break;
                case HIGHLIGHTED:
                    shape.getAppearance().getColoringAttributes().setColor(colorManager.getHighlightFg3f());
                    shape.getAppearance().getPolygonAttributes().setPolygonOffset(-20000f);
                    shape.getAppearance().getPolygonAttributes().setPolygonOffsetFactor(-20000f);
                    shape.getAppearance().getTransparencyAttributes().setTransparency(0f);
                    break;
                case SELECTED:
                    shape.getAppearance().getColoringAttributes().setColor(colorManager.getSelectedFg3f());
                    shape.getAppearance().getPolygonAttributes().setPolygonOffset(-10000f);
                    shape.getAppearance().getPolygonAttributes().setPolygonOffsetFactor(-10000f);
                    shape.getAppearance().getTransparencyAttributes().setTransparency(0.3f);
                    break;
                case SELECTED_HIGHLIGHTED:
                    shape.getAppearance().getColoringAttributes().setColor(colorManager.getSelectedHighlightFg3f());
                    shape.getAppearance().getPolygonAttributes().setPolygonOffset(-20000f);
                    shape.getAppearance().getPolygonAttributes().setPolygonOffsetFactor(-20000f);
                    shape.getAppearance().getTransparencyAttributes().setTransparency(0f);
                    break;
            }

            shape = (Shape3D) children.nextElement();

            switch (state) {
                case NORMAL:
                    shape.getAppearance().getColoringAttributes().setColor(colorManager.getBackground3f());
                    shape.getAppearance().getPolygonAttributes().setPolygonOffset(0f);
                    shape.getAppearance().getPolygonAttributes().setPolygonOffsetFactor(0f);
                    shape.getAppearance().getTransparencyAttributes().setTransparency(0f);
                    break;
                case HIGHLIGHTED:
                    shape.getAppearance().getColoringAttributes().setColor(colorManager.getHighlightBg3f());
                    shape.getAppearance().getPolygonAttributes().setPolygonOffset(-20000f);
                    shape.getAppearance().getPolygonAttributes().setPolygonOffsetFactor(-20000f);
                    shape.getAppearance().getTransparencyAttributes().setTransparency(0f);
                    break;
                case SELECTED:
                    shape.getAppearance().getColoringAttributes().setColor(colorManager.getSelectedBg3f());
                    shape.getAppearance().getPolygonAttributes().setPolygonOffset(-10000f);
                    shape.getAppearance().getPolygonAttributes().setPolygonOffsetFactor(-10000f);
                    shape.getAppearance().getTransparencyAttributes().setTransparency(0.3f);
                    break;
                case SELECTED_HIGHLIGHTED:
                    shape.getAppearance().getColoringAttributes().setColor(colorManager.getSelectedHighlightBg3f());
                    shape.getAppearance().getPolygonAttributes().setPolygonOffset(-20000f);
                    shape.getAppearance().getPolygonAttributes().setPolygonOffsetFactor(-20000f);
                    shape.getAppearance().getTransparencyAttributes().setTransparency(0f);
                    break;
            }
        }
    }

    /**
     * Manager of all colors used in this {@link StepRenderer}.
     * 
     * @author Martin Pecka
     */
    protected class ColorManager
    {
        /** Color of textual markers' text. */
        protected Color marker = Color.BLACK;
        /** Paper background color. */
        protected Color background;
        /** Paper foreground color. */
        protected Color foreground;
        /** Highlighted layer color for foreground/background. */
        protected Color highlightBg = new Color(150, 150, 255), highlightFg = new Color(150, 150, 255);
        /** Selected layer color for foreground/background. */
        protected Color selectedBg  = new Color(100, 100, 200), selectedFg = new Color(100, 100, 200);
        /** Highlighted selected layer color for foreground/background. */
        protected Color selectedHighlightBg = new Color(125, 125, 225), selectedHighlightFg = new Color(125, 125, 225);

        /**
         * @param background Paper background color.
         * @param foreground Paper foreground color.
         */
        public ColorManager(Color background, Color foreground)
        {
            this.background = background;
            this.foreground = foreground;
            ensureColorsAreContrasting();
        }

        /**
         * Call this method to make sure
         */
        public void ensureColorsAreContrasting()
        {
            // TODO May implement a cleverer algorithm, or just allow to set these colors from diagram.
            // This algorithm doesn't check if the highlight and selectedHighlight colors aren't the same, which would
            // be a problem for program usability.

            highlightBg = getContrastingColor(background, highlightBg);
            highlightFg = getContrastingColor(foreground, highlightFg);

            selectedHighlightBg = getContrastingColor(selectedBg, selectedHighlightBg);
            selectedHighlightFg = getContrastingColor(selectedFg, selectedHighlightFg);
        }

        /**
         * Get the well contrasting color for the specified reference color.
         * 
         * @param refColor The reference color that can be used for contrast computations etc.
         * @param color The color we want to be contrasting with refColor.
         * 
         * @return If color has enough contrast, return it, otherwise return a more contrasting color (if color is
         *         brighter than refColor, than an even brighter color will be returned, and conversely).
         */
        protected Color getContrastingColor(Color refColor, Color color)
        {
            Color result = color;

            // if the color difference is sufficient, we can return
            final int colDiff = abs(result.getRed() - refColor.getRed()) + abs(result.getGreen() - refColor.getGreen())
                    + abs(result.getBlue() - refColor.getBlue());
            // WCAG suggests 500, but we don't need such a large contrast here
            if (colDiff > 180)
                return result;

            // if the contrast with the reference color would be too low, change the resulting color to be more
            // contrasting
            float[] hsbRef = new float[3];
            float[] hsbRes = new float[3];

            Color.RGBtoHSB(refColor.getRed(), refColor.getGreen(), refColor.getBlue(), hsbRef);
            Color.RGBtoHSB(result.getRed(), result.getGreen(), result.getBlue(), hsbRes);

            final float threshold = 0.2f;
            final float diff = hsbRef[2] - hsbRes[2];
            if (diff > -threshold && diff <= 0f) {
                float bright;
                if (hsbRef[2] + threshold <= 1f)
                    bright = hsbRef[2] + threshold;
                else
                    bright = hsbRef[2] - threshold;
                result = Color.getHSBColor(hsbRes[0], hsbRes[1], bright);
            } else if (diff < threshold && diff >= 0f) {
                float bright;
                if (hsbRef[2] > threshold)
                    bright = hsbRef[2] - threshold;
                else
                    bright = hsbRef[2] + threshold;
                result = Color.getHSBColor(hsbRes[0], hsbRes[1], bright);
            }

            return result;
        }

        /**
         * @return Color of textual markers' text.
         */
        public Color getMarker()
        {
            return marker;
        }

        /**
         * @return Color of textual markers' text.
         */
        public Color3f getMarker3f()
        {
            return new Color3f(marker);
        }

        /**
         * @param marker Color of textual markers' text.
         */
        public void setMarker(Color marker)
        {
            this.marker = marker;
        }

        /**
         * @return Paper background color.
         */
        public Color getBackground()
        {
            return background;
        }

        /**
         * @return Paper background color.
         */
        public Color3f getBackground3f()
        {
            return new Color3f(background);
        }

        /**
         * @param background Paper background color.
         */
        public void setBackground(Color background)
        {
            this.background = background;
        }

        /**
         * @return Paper foreground color.
         */
        public Color getForeground()
        {
            return foreground;
        }

        /**
         * @return Paper foreground color.
         */
        public Color3f getForeground3f()
        {
            return new Color3f(foreground);
        }

        /**
         * @param foreground Paper foreground color.
         */
        public void setForeground(Color foreground)
        {
            this.foreground = foreground;
        }

        /**
         * @return Highlighted layer background color.
         */
        public Color getHighlightBg()
        {
            return highlightBg;
        }

        /**
         * @return Highlighted layer background color.
         */
        public Color3f getHighlightBg3f()
        {
            return new Color3f(highlightBg);
        }

        /**
         * @param highlightBg Highlighted layer background color.
         */
        public void setHighlightBg(Color highlightBg)
        {
            this.highlightBg = highlightBg;
        }

        /**
         * @return Highlighted layer foreground color.
         */
        public Color getHighlightFg()
        {
            return highlightFg;
        }

        /**
         * @return Highlighted layer foreground color.
         */
        public Color3f getHighlightFg3f()
        {
            return new Color3f(highlightFg);
        }

        /**
         * @param highlightFg Highlighted layer foreground color.
         */
        public void setHighlightFg(Color highlightFg)
        {
            this.highlightFg = highlightFg;
        }

        /**
         * @return Selected layer background color.
         */
        public Color getSelectedBg()
        {
            return selectedBg;
        }

        /**
         * @return Selected layer background color.
         */
        public Color3f getSelectedBg3f()
        {
            return new Color3f(selectedBg);
        }

        /**
         * @param selectedBg Selected layer background color.
         */
        public void setSelectedBg(Color selectedBg)
        {
            this.selectedBg = selectedBg;
        }

        /**
         * @return Selected layer foreground color.
         */
        public Color getSelectedFg()
        {
            return selectedFg;
        }

        /**
         * @return Selected layer foreground color.
         */
        public Color3f getSelectedFg3f()
        {
            return new Color3f(selectedFg);
        }

        /**
         * @param selectedFg Selected layer foreground color.
         */
        public void setSelectedFg(Color selectedFg)
        {
            this.selectedFg = selectedFg;
        }

        /**
         * @return Highlighted selected layer background color.
         */
        public Color getSelectedHighlightBg()
        {
            return selectedHighlightBg;
        }

        /**
         * @return Highlighted selected layer background color.
         */
        public Color3f getSelectedHighlightBg3f()
        {
            return new Color3f(selectedHighlightBg);
        }

        /**
         * @param selectedHighlightBg Highlighted selected layer background color.
         */
        public void setSelectedHighlightBg(Color selectedHighlightBg)
        {
            this.selectedHighlightBg = selectedHighlightBg;
        }

        /**
         * @return Highlighted selected layer foreground color.
         */
        public Color getSelectedHighlightFg()
        {
            return selectedHighlightFg;
        }

        /**
         * @return Highlighted selected layer foreground color.
         */
        public Color3f getSelectedHighlightFg3f()
        {
            return new Color3f(selectedHighlightFg);
        }

        /**
         * @param selectedHighlightFg Highlighted selected layer foreground color.
         */
        public void setSelectedHighlightFg(Color selectedHighlightFg)
        {
            this.selectedHighlightFg = selectedHighlightFg;
        }

        /**
         * Set both background/foreground colors for highlighted layer.
         * 
         * @param highlight Highlighted layer both colors.
         */
        public void setHighlight(Color highlight)
        {
            this.highlightFg = highlight;
            this.highlightBg = highlight;
        }

        /**
         * Set both background/foreground colors for selected layer.
         * 
         * @param selected Selected layer both colors.
         */
        public void setSelected(Color selected)
        {
            this.selectedFg = selected;
            this.selectedBg = selected;
        }

        /**
         * Set both background/foreground colors for highlighted selected layer.
         * 
         * @param selectedHighlight Highlighted selected layer both colors.
         */
        public void setSelectedHighlight(Color selectedHighlight)
        {
            this.selectedHighlightFg = selectedHighlight;
            this.selectedHighlightBg = selectedHighlight;
        }
    }
}
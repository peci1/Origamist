/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.gui.common;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseWheelEvent;

import javax.media.j3d.Appearance;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.GraphicsConfigTemplate3D;
import javax.media.j3d.PolygonAttributes;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.vecmath.Color3f;
import javax.vecmath.Vector3d;

import com.sun.j3d.exp.swing.JCanvas3D;
import com.sun.j3d.utils.universe.SimpleUniverse;

import cz.cuni.mff.peckam.java.origamist.model.Origami;
import cz.cuni.mff.peckam.java.origamist.model.Step;
import cz.cuni.mff.peckam.java.origamist.model.UnitDimension;
import cz.cuni.mff.peckam.java.origamist.model.jaxb.ModelColors;
import cz.cuni.mff.peckam.java.origamist.model.jaxb.Unit;
import cz.cuni.mff.peckam.java.origamist.modelstate.ModelState;

/**
 * The panel for rendering a step.
 * 
 * @author Martin Pecka
 */
public class StepRenderer extends JPanel
{
    /** */
    private static final long serialVersionUID = 9198803673578003101L;

    /**
     * The origami diagram we are rendering.
     */
    protected Origami         origami          = null;

    /**
     * The step this renderer is rendering.
     */
    protected Step            step             = null;

    /**
     * The canvas the model is rendered to.
     */
    protected JCanvas3D       canvas;

    /** The zoom of the step. */
    protected double          zoom             = 100d;

    /**
     * 
     */
    public StepRenderer()
    {
        setLayout(new BorderLayout());
        canvas = createCanvas();
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

                    // TODO refactor from now on

                    ModelState state = step.getModelState();

                    remove(canvas);
                    canvas = null;

                    canvas = createCanvas();
                    add(canvas, BorderLayout.CENTER);
                    canvas.setSize(new Dimension(20, 20));
                    canvas.setResizeMode(JCanvas3D.RESIZE_IMMEDIATELY);
                    canvas.setSize(getWidth(), getHeight());

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
        setStep(step); // TODO refactor
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
     * @return The configured <code>JCanvas3D</code>.
     */
    protected JCanvas3D createCanvas()
    {
        JCanvas3D canvas = new JCanvas3D(new GraphicsConfigTemplate3D());
        canvas.setOpaque(false);
        canvas.setResizeMode(JCanvas3D.RESIZE_DELAYED);
        return canvas;
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

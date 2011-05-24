/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.gui.common;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

import javax.media.j3d.Alpha;
import javax.media.j3d.Appearance;
import javax.media.j3d.Behavior;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.GeometryArray;
import javax.media.j3d.Group;
import javax.media.j3d.ImageComponent2D;
import javax.media.j3d.LineArray;
import javax.media.j3d.Morph;
import javax.media.j3d.OrderedGroup;
import javax.media.j3d.Screen3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.TriangleArray;
import javax.media.j3d.WakeupCondition;
import javax.media.j3d.WakeupOnElapsedFrames;
import javax.vecmath.Point3d;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.sun.j3d.utils.universe.SimpleUniverse;

import cz.cuni.mff.peckam.java.origamist.exceptions.InvalidOperationException;
import cz.cuni.mff.peckam.java.origamist.exceptions.PaperStructureException;
import cz.cuni.mff.peckam.java.origamist.model.Origami;
import cz.cuni.mff.peckam.java.origamist.model.Step;
import cz.cuni.mff.peckam.java.origamist.modelstate.ModelSegment;
import cz.cuni.mff.peckam.java.origamist.modelstate.ModelState;
import cz.cuni.mff.peckam.java.origamist.utils.ParametrizedCallable;

/**
 * A canvas controller that implements model folding animations.
 * 
 * @author Martin Pecka
 */
@SuppressWarnings("deprecation")
public class StepMorphingCanvasController extends StepViewingCanvasController
{

    /** The morph nodes on the current step. */
    protected final Set<Morph> morphs                   = new HashSet<Morph>();

    /** The number of animation frames per a step. */
    protected final int        framesPerStep;

    /** The number of frames elapsed since the last step change. */
    protected int              currentStepElapsedFrames = 0;

    /** Transforms of this and the next step. */
    protected Transform3D      fromTrans                = new Transform3D(), toTrans = new Transform3D();

    {
        // screen dimensions need to be set before rendering
        SimpleUniverse u = new SimpleUniverse(new Canvas3D(SimpleUniverse.getPreferredConfiguration()));
        Screen3D screen = u.getViewer().getCanvas3D().getScreen3D();
        Screen3D screen2 = canvas.getScreen3D();
        screen2.setSize(screen.getSize());
        screen2.setPhysicalScreenWidth(screen.getPhysicalScreenWidth());
        screen2.setPhysicalScreenHeight(screen.getPhysicalScreenHeight());

        if (canvas.getOffScreenBuffer() == null) {
            ImageComponent2D image = new ImageComponent2D(ImageComponent2D.FORMAT_RGBA, 800, 600);
            image.setCapability(ImageComponent2D.ALLOW_IMAGE_READ);
            canvas.setOffScreenBuffer(image);
        }

        canvas.setDoubleBufferEnable(false);
        canvas.setVisible(true);

        canvas.renderOffScreenBuffer();

        setStep(step);
    }

    /**
     * @param canvas
     * @param origami
     * @param step
     * @param framesPerStep The number of animation frames per a step.
     */
    public StepMorphingCanvasController(Canvas3D canvas, Origami origami, Step step, int framesPerStep)
    {
        super(canvas, origami, step);
        this.framesPerStep = framesPerStep;
    }

    /**
     * @param canvas
     * @param framesPerStep The number of animation frames per a step.
     */
    public StepMorphingCanvasController(Canvas3D canvas, int framesPerStep)
    {
        super(canvas);
        this.framesPerStep = framesPerStep;
    }

    @Override
    protected TransformGroup setupTGroup() throws InvalidOperationException
    {
        try {
            // we will need to edit the model state, so create a safe copy of it
            ModelState state = step.getModelState(true).clone();
            TriangleArray[] trianglesWithDelayed = state.getTrianglesArrays();
            LineArray[] lineArraysWithDelayed = state.getLineArrays();

            state.revertDelayedOperations();
            TriangleArray[] trianglesWithoutDelayed = state.getTrianglesArrays();
            LineArray[] lineArraysWithoutDelayed = state.getLineArraysAfterDelayedRevertion();

            assert trianglesWithDelayed.length == trianglesWithoutDelayed.length;
            assert lineArraysWithDelayed.length == lineArraysWithoutDelayed.length;

            tGroup = new TransformGroup();
            tGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
            tGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);

            if (morphs == null)
                return tGroup;

            OrderedGroup og = new OrderedGroup();

            model = new TransformGroup();

            morphs.clear();

            Morph top, bottom;
            Appearance appearance;
            Appearance appearance2;
            for (int i = 0; i < trianglesWithDelayed.length; i++) {
                appearance = createNormalTrianglesAppearance();
                appearance2 = createInverseTrianglesAppearance();

                top = new Morph(new GeometryArray[] { trianglesWithoutDelayed[i], trianglesWithDelayed[i] }, appearance);
                bottom = new Morph(new GeometryArray[] { trianglesWithoutDelayed[i], trianglesWithDelayed[i] },
                        appearance2);

                top.setCapability(Morph.ALLOW_WEIGHTS_WRITE);
                bottom.setCapability(Morph.ALLOW_WEIGHTS_WRITE);

                morphs.add(top);
                morphs.add(bottom);

                model.addChild(top);
                model.addChild(bottom);
            }

            Group lines = new TransformGroup();

            Appearance appearance3;
            Morph morph;
            for (int i = 0; i < lineArraysWithDelayed.length; i++) {
                ModelSegment segment = (ModelSegment) lineArraysWithDelayed[i].getUserData();
                appearance3 = createBasicLinesAppearance();
                getLineAppearanceManager().alterBasicAppearance(appearance3, segment.getDirection(),
                        step.getId() - segment.getOriginatingStepId());

                morph = new Morph(new GeometryArray[] { lineArraysWithoutDelayed[i], lineArraysWithDelayed[i] },
                        appearance3);
                morph.setCapability(Morph.ALLOW_WEIGHTS_WRITE);

                morphs.add(morph);

                lines.addChild(morph);
            }
            model.addChild(lines);

            og.addChild(model);

            setupTransform();
            tGroup.setTransform(transform);

            og.addChild(getMarkerGroups());

            tGroup.addChild(og);

            return tGroup;
        } catch (InvalidOperationException e) {
            tGroup = new TransformGroup();
            tGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
            // TODO create an ErrorTransformGroup that would signalize to the user that an operation is invalid
            throw e;
        }
    }

    @Override
    protected void createAndAddBranchGraphChildren() throws InvalidOperationException
    {
        setupTGroup();

        branchGraph.addChild(tGroup);

        if (!canvas.isOffScreen()) { // onscreen canvases will be controlled by this behavior
            Behavior morph = new Behavior() {
                private Alpha           alpha;
                private WakeupCondition trigger = new WakeupOnElapsedFrames(0);

                public void initialize()
                {
                    alpha = new Alpha(-1, (framesPerStep / 30) * 1000); // play at 30 fps
                    this.setSchedulingBounds(new BoundingSphere(new Point3d(), 1000));
                    this.wakeupOn(trigger);
                }

                public void processStimulus(@SuppressWarnings("rawtypes") Enumeration criteria)
                {
                    // don't need to decode event since there is only one trigger

                    updateMorphs(alpha.value());

                    this.wakeupOn(trigger); // set next wakeup condition
                }
            };
            branchGraph.addChild(morph);
        }
    }

    // @Override
    // protected Transform3D computeInitialViewTransform(Origami origami)
    // {
    // return new Transform3D();
    // }

    @Override
    public void setStep(Step step, Runnable afterSetCallback,
            ParametrizedCallable<?, ? super Exception> exceptionCallback)
    {
        final Step oldStep = this.step;
        this.step = step;

        currentStepElapsedFrames = 0;

        if (step != null && step.getAttachedTo() == null) {
            return;
        }

        Exception exception = null;

        synchronized (this) {

            try {
                if (step != null)
                    setupUniverse();
            } catch (InvalidOperationException e) {
                Logger.getLogger("application").l7dlog(Level.ERROR, "StepRenderer.InvalidOperationException",
                        new Object[] { this.step.getId(), e.getOperation().toString() }, e);
                exception = e;
            } catch (PaperStructureException e) {
                Logger.getLogger("application").error(e.getMessage(), e);
                exception = e;
            } finally {
                topTexture = null;
                bottomTexture = null;
            }
        }

        if (oldStep != null && step != null && step.getNext() != null) {
            fromTrans = new Transform3D(baseTransform);
            this.step = step.getNext();
            setupTransform();
            toTrans = new Transform3D(baseTransform);
            this.step = step;
            setupTransform();
        }

        support.firePropertyChange(STEP_PROPERTY, null, step);

        setZoom(50); // TODO just a workaround, will need to find out why the view is badly centered without this

        if (exception == null) {
            afterSetStep();
            if (afterSetCallback != null)
                afterSetCallback.run();
        } else if (exceptionCallback != null) {
            exceptionCallback.call(exception);
        }
    }

    /**
     * Update morph nodes depending on the given alpha value.
     * 
     * @param alpha The alpha value in 0.0 - 1.0.
     */
    protected void updateMorphs(float alpha)
    {
        final double[] weights = new double[2];
        // don't need to decode event since there is only one trigger
        Arrays.fill(weights, 0);

        weights[0] = 1 - alpha;
        weights[1] = alpha;

        for (Morph morph : morphs)
            morph.setWeights(weights);
    }

    /**
     * @return The next animation frame. If this would be the last frame of the step's animation, the next step is
     *         automatically set. If this is the last step, return <code>null</code>
     */
    public BufferedImage getNextFrame()
    {
        if (step == null)
            return null;

        float alphaValue = ((float) currentStepElapsedFrames / framesPerStep); // get alpha
        updateMorphs(alphaValue);

        adjustSize();

        // Transform3D tr = new Transform3D(fromTrans);
        // tr.invert();
        // tr.mul(toTrans, tr);
        // tr.mul(alphaValue); //TODO will need another kind of interpolation
        //
        // transform.mul(fromTrans, tr);
        // tGroup.setTransform(transform);

        canvas.waitForOffScreenRendering();
        canvas.renderOffScreenBuffer();
        canvas.waitForOffScreenRendering();

        BufferedImage image = new BufferedImage(canvas.getWidth(), canvas.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        g.drawImage(canvas.getOffScreenBuffer().getImage(), 0, 0, origami.getPaper().getBackgroundColor(), null);
        g.dispose();

        currentStepElapsedFrames++;
        if (currentStepElapsedFrames > framesPerStep) {
            currentStepElapsedFrames = 0;
            if (step.getNext() != null)
                setStep(step.getNext());
        }

        return image;
    }
}

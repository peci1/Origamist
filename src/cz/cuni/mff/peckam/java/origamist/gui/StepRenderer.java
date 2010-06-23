/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.Locale;

import javax.media.j3d.Appearance;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.GraphicsConfigTemplate3D;
import javax.media.j3d.PolygonAttributes;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.vecmath.Color3f;
import javax.vecmath.Vector3d;

import com.sun.j3d.exp.swing.JCanvas3D;
import com.sun.j3d.utils.universe.SimpleUniverse;

import cz.cuni.mff.peckam.java.origamist.model.Origami;
import cz.cuni.mff.peckam.java.origamist.model.Step;
import cz.cuni.mff.peckam.java.origamist.modelstate.ModelState;
import cz.cuni.mff.peckam.java.origamist.services.ConfigurationManager;
import cz.cuni.mff.peckam.java.origamist.services.ServiceLocator;

/**
 * This panel renders the given state of the model.
 * 
 * @author Martin Pecka
 */
public class StepRenderer extends JPanel
{
    private static final long serialVersionUID = 6989958008007575800L;

    /**
     * The origami diagram we are rendering.
     */
    protected Origami         origami          = null;

    /**
     * The step this renderer is rendering.
     */
    protected Step            step             = null;

    /**
     * The background color.
     */
    protected Color           backgroundColor  = null;

    /**
     * The label that shows the descripton of the shown step.
     */
    protected JTextArea       descLabel        = new JTextArea();

    /**
     * The canvas the model is rendered to.
     */
    protected JCanvas3D       canvas;

    public StepRenderer()
    {
        this.setLayout(new BorderLayout());

        canvas = new JCanvas3D(new GraphicsConfigTemplate3D());
        canvas.setOpaque(false);
        canvas.setResizeMode(JCanvas3D.RESIZE_DELAYED);
        this.add(canvas, BorderLayout.CENTER);
        // just a workaround - the canvas needs to know its size before the size of its container can be determined
        canvas.setSize(new Dimension(20, 20));

        this.add(descLabel, BorderLayout.SOUTH);
        descLabel.setEditable(false);
        descLabel.setOpaque(false);
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
        this.backgroundColor = origami.getPaper().getColor().getBackground();
    }

    @Override
    public void setPreferredSize(Dimension preferredSize)
    {
        super.setPreferredSize(preferredSize);
        this.descLabel.setMaximumSize(preferredSize);
    }

    @Override
    public void setSize(Dimension size)
    {
        super.setSize(size);
        this.descLabel.setMaximumSize(size);
    }

    @Override
    public void setSize(int width, int height)
    {
        super.setSize(width, height);
        this.descLabel.setMaximumSize(new Dimension(width, height));
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
    public void setStep(Step step)
    {
        Locale l = ServiceLocator.get(ConfigurationManager.class).get().getDiagramLocale();

        this.step = step;
        this.descLabel.setText(step.getDescription(l));

        ModelState state = step.getModelState();

        canvas.setSize(this.getWidth(), this.getHeight() - this.descLabel.getHeight());
        canvas.setResizeMode(JCanvas3D.RESIZE_IMMEDIATELY);
        SimpleUniverse universe = new SimpleUniverse(canvas.getOffscreenCanvas3D());

        Appearance appearance = new Appearance();

        PolygonAttributes polyAttribs = new PolygonAttributes();
        polyAttribs.setCullFace(PolygonAttributes.CULL_NONE);
        polyAttribs.setPolygonMode(PolygonAttributes.POLYGON_FILL);
        appearance.setPolygonAttributes(polyAttribs);

        Color3f paperFrontColor = new Color3f(origami.getModel().getPaper().getColors().getForeground());
        ColoringAttributes colAttrs = new ColoringAttributes(paperFrontColor, ColoringAttributes.NICEST);
        appearance.setColoringAttributes(colAttrs);

        Transform3D transform = new Transform3D();
        transform.setEuler(new Vector3d(state.getViewingAngle() - Math.PI / 2.0, 0, state.getRotation()));
        // TODO adjust zoom according to paper size and renderer size - this is placeholder code
        transform.setScale(0.3 * step.getZoom() / 100.0);
        TransformGroup tGroup = new TransformGroup();
        tGroup.setTransform(transform);

        tGroup.addChild(new Shape3D(state.getTriangleArray(), appearance));
        // TODO need to generate a copy of the triangleArray with reversed faces to be able to give them different color

        BranchGroup contents = new BranchGroup();
        contents.addChild(tGroup);

        universe.getViewingPlatform().setNominalViewingTransform();
        universe.addBranchGraph(contents);
    }

}

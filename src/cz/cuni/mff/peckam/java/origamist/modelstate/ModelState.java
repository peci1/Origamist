/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.modelstate;

import javax.media.j3d.GeometryArray;
import javax.media.j3d.LineArray;
import javax.media.j3d.TriangleArray;
import javax.vecmath.Color4b;
import javax.vecmath.Point3d;
import javax.vecmath.Point4d;

import cz.cuni.mff.peckam.java.origamist.model.Origami;
import cz.cuni.mff.peckam.java.origamist.model.Step;
import cz.cuni.mff.peckam.java.origamist.model.UnitDimension;
import cz.cuni.mff.peckam.java.origamist.model.UnitHelper;
import cz.cuni.mff.peckam.java.origamist.model.jaxb.Unit;
import cz.cuni.mff.peckam.java.origamist.modelstate.Fold.FoldLine;
import cz.cuni.mff.peckam.java.origamist.utils.ChangeNotification;
import cz.cuni.mff.peckam.java.origamist.utils.ChangeNotificationListener;
import cz.cuni.mff.peckam.java.origamist.utils.ChangeNotifyingList;
import cz.cuni.mff.peckam.java.origamist.utils.ChangeNotifyingList.ChangeTypes;

/**
 * The internal state of the model after some steps.
 * 
 * @author Martin Pecka
 */
public class ModelState implements Cloneable
{
    /**
     * Folds on this paper.
     */
    protected ChangeNotifyingList<Fold>     folds                 = new ChangeNotifyingList<Fold>();

    /**
     * Cache for array of the lines representing folds.
     */
    protected LineArray                     foldLineArray         = null;

    /**
     * If true, the value of foldLineArray doesn't have to be consistent and a call to updateLineArray is needed.
     */
    protected boolean                       foldLineArrayDirty    = true;

    /**
     * The triangles this model state consists of.
     */
    protected ChangeNotifyingList<Triangle> triangles             = new ChangeNotifyingList<Triangle>();

    /**
     * The triangles the model state consists of. This representation can be directly used by Java3D.
     */
    protected TriangleArray                 trianglesArray        = null;

    /**
     * The triangles the model state consists of. This representation can be directly used by Java3D. The triangles face
     * the other side than the corresponding ones in trianglesArray.
     */
    protected TriangleArray                 inverseTrianglesArray = null;

    /**
     * If true, the value of trianglesArray doesn't have to be consistent and a call to updateVerticesArray is needed.
     */
    protected boolean                       trianglesArrayDirty   = true;

    /**
     * Rotation of the model (around the axis from eyes to display) in radians.
     */
    protected double                        rotationAngle         = 0;

    /**
     * The angle the model is viewed from (angle between eyes and the unfolded paper surface) in radians.
     * 
     * PI/2 means top view, -PI/2 means bottom view
     */
    protected double                        viewingAngle          = Math.PI / 2.0;

    /**
     * The step this state belongs to.
     */
    protected Step                          step;

    /**
     * The origami model which is this the state of.
     */
    protected Origami                       origami;

    /**
     * The number of steps a foldline remains visible.
     */
    protected int                           stepBlendingTreshold  = 5;

    public ModelState()
    {
        folds.addChangeListener(new ChangeNotificationListener<Fold>() {
            @Override
            public void changePerformed(ChangeNotification<Fold> change)
            {
                ModelState.this.foldLineArrayDirty = true;

                if (change.getChangeType() == ChangeTypes.ADD) {
                    change.getItem().lines.addChangeListener(new ChangeNotificationListener<FoldLine>() {
                        @Override
                        public void changePerformed(ChangeNotification<FoldLine> change)
                        {
                            ModelState.this.foldLineArrayDirty = true;
                        }
                    });
                }
            }
        });

        triangles.addChangeListener(new ChangeNotificationListener<Triangle>() {
            @Override
            public void changePerformed(ChangeNotification<Triangle> change)
            {
                ModelState.this.trianglesArrayDirty = true;
            }
        });
    }

    /**
     * Set the step this model state belongs to.
     * 
     * @param step The step to set.
     */
    public void setStep(Step step)
    {
        this.step = step;
    }

    /**
     * Set the origami model this step will work with.
     * 
     * @param origami The origami model.
     */
    public void setOrigami(Origami origami)
    {
        this.origami = origami;
    }

    /**
     * Update the contents of the foldLineArray so that it corresponds to the actual contents of the folds variable.
     */
    protected synchronized void updateLineArray()
    {
        int linesCount = 0;
        for (Fold fold : folds) {
            linesCount += fold.lines.size();
        }

        UnitDimension paperSize = (UnitDimension) origami.getModel().getPaper().getSize();
        double ratio = UnitHelper.convertTo(Unit.REL, Unit.M, 1, paperSize.getUnit(), paperSize.getMax());

        foldLineArray = new LineArray(2 * linesCount, GeometryArray.COORDINATES | GeometryArray.COLOR_4);
        int i = 0;
        for (Fold fold : folds) {
            for (FoldLine line : fold.lines) {
                // TODO the lines need to be converted to their 3D coordinates
                foldLineArray
                        .setCoordinate(2 * i, new Point3d(line.line.getX1() * ratio, line.line.getY1() * ratio, 0));
                foldLineArray.setCoordinate(2 * i + 1, new Point3d(line.line.getX2() * ratio,
                        line.line.getY2() * ratio, 0));
                byte alpha = (byte) 255;
                if (line.direction != null) {
                    // TODO invent some more sophisticated way to determine the fold "age"
                    int diff = step.getId() - fold.originatingStepId;
                    if (diff <= stepBlendingTreshold) {
                        alpha = (byte) (255 - (diff / stepBlendingTreshold) * 255);
                    } else {
                        alpha = 0;
                    }
                }
                foldLineArray.setColor(2 * i, new Color4b((byte) 0, (byte) 0, (byte) 0, alpha));
                foldLineArray.setColor(2 * i + 1, new Color4b((byte) 0, (byte) 0, (byte) 0, alpha));
                i++;
            }
        }

        foldLineArrayDirty = false;
    }

    /**
     * Retrurn the line array corresponding to the list of folds.
     * 
     * @return The line array corresponding to the list of folds.
     */
    public synchronized LineArray getLineArray()
    {
        if (foldLineArrayDirty)
            updateLineArray();

        return foldLineArray;
    }

    protected synchronized void updateTrianglesArray()
    {
        trianglesArray = new TriangleArray(triangles.size() * 3, TriangleArray.COORDINATES);
        inverseTrianglesArray = new TriangleArray(triangles.size() * 3, TriangleArray.COORDINATES);

        UnitDimension paperSize = (UnitDimension) origami.getModel().getPaper().getSize();
        double ratio = 1.0 / UnitHelper.convertTo(Unit.REL, Unit.M, 1, paperSize.getUnit(), paperSize.getMax());

        int i = 0;
        Point3d p1, p2, p3;
        for (Triangle triangle : triangles) {
            p1 = (Point3d) triangle.getP1().clone();
            p1.project(new Point4d(p1.x, p1.y, p1.z, ratio));

            p2 = (Point3d) triangle.getP2().clone();
            p2.project(new Point4d(p2.x, p2.y, p2.z, ratio));

            p3 = (Point3d) triangle.getP3().clone();
            p3.project(new Point4d(p3.x, p3.y, p3.z, ratio));

            trianglesArray.setCoordinate(3 * i, p1);
            inverseTrianglesArray.setCoordinate(3 * i, (Point3d) p1.clone());

            trianglesArray.setCoordinate(3 * i + 1, p2);
            inverseTrianglesArray.setCoordinate(3 * i + 1, (Point3d) p3.clone());

            trianglesArray.setCoordinate(3 * i + 2, p3);
            inverseTrianglesArray.setCoordinate(3 * i + 2, (Point3d) p2.clone());

            i++;
        }

        trianglesArrayDirty = false;
    }

    /**
     * Return the triangle array.
     * 
     * @return The triangle array.
     */
    public synchronized TriangleArray getTrianglesArray()
    {
        if (trianglesArrayDirty)
            updateTrianglesArray();

        return trianglesArray;
    }

    /**
     * Return the inverse triangle array. (The triangles facing the other side than the ones returned by
     * getTrianglesArray)
     * 
     * @return The inverse triangle array.
     */
    public synchronized TriangleArray getInverseTrianglesArray()
    {
        if (trianglesArrayDirty)
            updateTrianglesArray();

        return inverseTrianglesArray;
    }

    /**
     * Adds the given angle to the current angle of rotation.
     * 
     * @param rotation The angle to add (in radians).
     */
    public void addRotation(double rotation)
    {
        setRotation(rotationAngle + rotation);
    }

    /**
     * Sets the current angle of rotation to the given value.
     * 
     * @param rotation The angle to set (in radians).
     */
    public void setRotation(double rotation)
    {
        rotationAngle = rotation;

        while (rotationAngle > Math.PI)
            rotationAngle -= Math.PI;
        while (rotationAngle < -Math.PI)
            rotationAngle += Math.PI;
    }

    /**
     * Return the rotation of the paper.
     * 
     * @return The rotation of the paper (in radians).
     */
    public double getRotation()
    {
        return rotationAngle;
    }

    /**
     * Adds the given angle to the current viewing angle of the paper.
     * 
     * The angle will be "cropped" to <-PI/2,PI/2> interval.
     * 
     * @param angle The angle to add (in radians).
     */
    public void addViewingAngle(double angle)
    {
        setViewingAngle(viewingAngle + angle);
    }

    /**
     * Changes the viewing angle from top to bottom and vice versa.
     */
    public void flipViewingAngle()
    {
        setViewingAngle(-viewingAngle);
    }

    /**
     * Sets the current viewing angle to the given value.
     * 
     * The angle will be "cropped" to <-PI/2,PI/2> interval.
     * 
     * @param angle The angle to set (in radians).
     */
    public void setViewingAngle(double angle)
    {
        viewingAngle = angle;

        if (viewingAngle > Math.PI / 2.0)
            viewingAngle = Math.PI / 2.0;
        if (viewingAngle < -Math.PI / 2.0)
            viewingAngle = -Math.PI / 2.0;
    }

    /**
     * Get the current viewing angle.
     * 
     * @return The viewing angle (in radians).
     */
    public double getViewingAngle()
    {
        return viewingAngle;
    }

    @Override
    public Object clone() throws CloneNotSupportedException
    {
        ModelState result = new ModelState();

        result.step = this.step;
        result.origami = this.origami;
        result.rotationAngle = this.rotationAngle;
        result.viewingAngle = this.viewingAngle;
        result.stepBlendingTreshold = this.stepBlendingTreshold;

        for (Fold fold : folds)
            result.folds.add((Fold) fold.clone());

        for (Triangle t : triangles)
            result.triangles.add((Triangle) t.clone());
        result.trianglesArrayDirty = true;

        return result;
    }
}

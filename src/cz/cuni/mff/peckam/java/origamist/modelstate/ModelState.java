/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.modelstate;

import java.util.ArrayList;
import java.util.List;

import javax.media.j3d.TriangleArray;

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
    protected List<Fold>    folds         = new ArrayList<Fold>();

    /**
     * The triangles the model state consists of.
     */
    protected TriangleArray triangleArray = null;

    /**
     * Rotation of the model (around the axis from eyes to display) in radians.
     */
    protected double        rotationAngle = 0;

    /**
     * The angle the model is viewed from (angle between eyes and the unfolded paper surface) in radians.
     * 
     * PI/2 means top view, -PI/2 means bottom view
     */
    protected double        viewingAngle  = Math.PI / 2.0;

    /**
     * Return the triangle array.
     * 
     * @return The triangle array.
     */
    public TriangleArray getTriangleArray()
    {
        return triangleArray;
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
        ModelState result = (ModelState) super.clone();
        result.folds = new ArrayList<Fold>(folds);
        for (int i = 0; i < folds.size(); i++)
            folds.set(i, (Fold) folds.get(i).clone());

        if (triangleArray != null) {
            result.triangleArray = new TriangleArray(triangleArray.getVertexCount(), triangleArray.getVertexFormat());
            result.triangleArray.duplicateNodeComponent(triangleArray, true);
        }

        return result;
    }
}

/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.modelstate.arguments;

import javax.vecmath.Point2d;

import cz.cuni.mff.peckam.java.origamist.gui.editor.PickMode;
import cz.cuni.mff.peckam.java.origamist.model.Point2D;

/**
 * A point argument.
 * 
 * @author Martin Pecka
 */
public class PointArgument extends OperationArgument
{

    /** The point. */
    protected Point2d point = null;

    /**
     * @param required If true, this argument is required.
     */
    public PointArgument(boolean required)
    {
        super(required);
    }

    @Override
    public boolean isComplete()
    {
        return point != null;
    }

    /**
     * @return The point.
     * 
     * @throws IllegalStateException If {@link #isComplete()} is false.
     */
    public Point2D getPoint() throws IllegalStateException
    {
        if (!isComplete())
            throw new IllegalStateException("Cannot query properties of a non-completed argument.");

        return new Point2D(point);
    }

    /**
     * @param point The point to set.
     */
    public void setPoint(Point2d point)
    {
        this.point = point;
    }

    @Override
    public PickMode preferredPickMode()
    {
        return PickMode.POINT;
    }
}

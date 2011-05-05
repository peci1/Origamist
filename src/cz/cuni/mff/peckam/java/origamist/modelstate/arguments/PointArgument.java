/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.modelstate.arguments;

import cz.cuni.mff.peckam.java.origamist.gui.editor.PickMode;
import cz.cuni.mff.peckam.java.origamist.gui.editor.StepEditor;
import cz.cuni.mff.peckam.java.origamist.model.Point2D;
import cz.cuni.mff.peckam.java.origamist.modelstate.ModelPoint;

/**
 * A point argument.
 * 
 * @author Martin Pecka
 */
public class PointArgument extends OperationArgument implements EditorDataReceiver
{

    /** The point. */
    protected ModelPoint point = null;

    /**
     * @param required If true, this argument is required.
     * @param resourceBundleKey The key in "editor" resource bundle describing this operation argument.
     */
    public PointArgument(boolean required, String resourceBundleKey)
    {
        super(required, resourceBundleKey);
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
    public Point2D getPoint2D() throws IllegalStateException
    {
        return new Point2D(getPoint().getOriginal());
    }

    /**
     * @return The point.
     * 
     * @throws IllegalStateException If {@link #isComplete()} is false.
     */
    public ModelPoint getPoint() throws IllegalStateException
    {
        if (!isComplete())
            throw new IllegalStateException("Cannot query properties of a non-completed argument.");

        return point;
    }

    /**
     * @param point The point to set.
     */
    public void setPoint(ModelPoint point)
    {
        this.point = point;
    }

    @Override
    public PickMode preferredPickMode()
    {
        return PickMode.POINT;
    }

    @Override
    public void readDataFromEditor(StepEditor editor)
    {
        if (editor.getChosenPoint() != null)
            this.point = editor.getChosenPoint();
    }
}

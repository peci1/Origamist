/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.modelstate.arguments;

import cz.cuni.mff.peckam.java.origamist.gui.editor.PickMode;
import cz.cuni.mff.peckam.java.origamist.gui.editor.StepEditor;
import cz.cuni.mff.peckam.java.origamist.model.Line2D;
import cz.cuni.mff.peckam.java.origamist.modelstate.ModelSegment;

/**
 * A line argument.
 * 
 * @author Martin Pecka
 */
public class LineArgument extends OperationArgument implements EditorDataReceiver
{

    /** The line. */
    protected ModelSegment line = null;

    /**
     * @param required If true, this argument is required.
     * @param resourceBundleKey The key in "editor" resource bundle describing this operation argument.
     */
    public LineArgument(boolean required, String resourceBundleKey)
    {
        super(required, resourceBundleKey);
    }

    @Override
    public boolean isComplete()
    {
        return line != null;
    }

    /**
     * @return The line.
     * 
     * @throws IllegalStateException If {@link #isComplete()} is false.
     */
    public Line2D getLine2D() throws IllegalStateException
    {
        return new Line2D(getLine().getOriginal());
    }

    /**
     * @return The line.
     * 
     * @throws IllegalStateException If {@link #isComplete()} is false.
     */
    public ModelSegment getLine() throws IllegalStateException
    {
        if (!isComplete())
            throw new IllegalStateException("Cannot query properties of a non-completed argument.");

        return line;
    }

    /**
     * @param line The line to set.
     */
    public void setLine(ModelSegment line)
    {
        this.line = line;
    }

    @Override
    public PickMode preferredPickMode()
    {
        return PickMode.POINT;
    }

    @Override
    public void readDataFromObject(StepEditor editor)
    {
        if (editor.getChosenLine() != null)
            this.line = editor.getChosenLine();
    }
}

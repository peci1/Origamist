/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.modelstate.arguments;

import cz.cuni.mff.peckam.java.origamist.gui.editor.PickMode;
import cz.cuni.mff.peckam.java.origamist.math.Segment2d;
import cz.cuni.mff.peckam.java.origamist.model.Line2D;

/**
 * A line argument.
 * 
 * @author Martin Pecka
 */
public class LineArgument extends OperationArgument
{

    /** The line. */
    protected Segment2d line = null;

    /**
     * @param required If true, this argument is required.
     */
    public LineArgument(boolean required)
    {
        super(required);
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
    public Line2D getLine() throws IllegalStateException
    {
        if (!isComplete())
            throw new IllegalStateException("Cannot query properties of a non-completed argument.");

        return new Line2D(line);
    }

    /**
     * @param line The line to set.
     */
    public void setLine(Segment2d line)
    {
        this.line = line;
    }

    @Override
    public PickMode preferredPickMode()
    {
        return PickMode.POINT;
    }
}

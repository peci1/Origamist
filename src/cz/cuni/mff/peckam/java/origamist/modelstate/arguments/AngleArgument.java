/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.modelstate.arguments;

import cz.cuni.mff.peckam.java.origamist.gui.editor.PickMode;

/**
 * Angle argument.
 * 
 * @author Martin Pecka
 */
public class AngleArgument extends OperationArgument
{
    /** The angle. */
    protected Double angle = null;

    /**
     * @param required If true, this argument is required.
     */
    public AngleArgument(boolean required)
    {
        super(required);
    }

    @Override
    public boolean isComplete()
    {
        return angle != null;
    }

    /**
     * @return The angle.
     * 
     * @throws IllegalStateException If {@link #isComplete()} is false.
     */
    public double getAngle() throws IllegalStateException
    {
        if (!isComplete())
            throw new IllegalStateException("Cannot query properties of a non-completed argument.");

        return angle;
    }

    /**
     * @param angle The angle to set.
     */
    public void setAngle(double angle)
    {
        this.angle = angle;
    }

    @Override
    public PickMode preferredPickMode()
    {
        return null;
    }
}

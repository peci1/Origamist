/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.modelstate.arguments;

import cz.cuni.mff.peckam.java.origamist.gui.editor.PickMode;

/**
 * An argument of an operation on a model.
 * 
 * @author Martin Pecka
 */
public abstract class OperationArgument
{
    /** If true, this argument is required. */
    protected boolean required;

    /**
     * @param required If true, this argument is required.
     */
    public OperationArgument(boolean required)
    {
        this.required = required;
    }

    /**
     * Return true if this argument is completely filled-in.
     */
    public abstract boolean isComplete();

    /**
     * @return The preferred pick mode to be set after this argument is to be filled. Return <code>null</code> if no
     *         change is to be made.
     */
    public abstract PickMode preferredPickMode();

    /**
     * @return True if this argument is required.
     */
    public final boolean isRequired()
    {
        return required;
    }
}

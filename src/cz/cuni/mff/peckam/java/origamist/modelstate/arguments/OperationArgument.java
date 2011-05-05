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
    protected boolean           required;

    /** The key in "editor" resource bundle describing this operation argument. */
    protected String            resourceBundleKey;

    /** The next operation argument. */
    protected OperationArgument next = null;

    /**
     * @param required If true, this argument is required.
     * @param resourceBundleKey The key in "editor" resource bundle describing this operation argument.
     */
    public OperationArgument(boolean required, String resourceBundleKey)
    {
        this.required = required;
        this.resourceBundleKey = resourceBundleKey;
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

    /**
     * @return The key in "editor" resource bundle describing this operation argument.
     */
    public final String getResourceBundleKey()
    {
        return resourceBundleKey;
    }

    /**
     * @return The next operation argument.
     */
    public final OperationArgument getNext()
    {
        return next;
    }

    /**
     * @param next The next operation argument.
     */
    public final void setNext(OperationArgument next)
    {
        this.next = next;
    }
}

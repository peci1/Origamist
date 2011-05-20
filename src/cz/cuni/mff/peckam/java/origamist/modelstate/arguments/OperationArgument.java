/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.modelstate.arguments;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import cz.cuni.mff.peckam.java.origamist.gui.editor.PickMode;
import cz.cuni.mff.peckam.java.origamist.utils.LocalizedString;

/**
 * An argument of an operation on a model.
 * 
 * @author Martin Pecka
 */
public abstract class OperationArgument
{
    /** If true, this argument is required. */
    protected boolean               required;

    /** The key in "editor" resource bundle describing this operation argument. */
    protected String                resourceBundleKey;

    /** The next operation argument. */
    protected OperationArgument     next              = null;

    /** Support for property changes. */
    protected PropertyChangeSupport support           = new PropertyChangeSupport(this);

    /** This property will be changed once from false to true when this argument detects it is complete. */
    public static final String      COMPLETE_PROPERTY = "complete";

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
     * @return The string to be displayed to the user as a tooltip for this argument. <code>null</code> if no tooltip
     *         should be displayed.
     */
    public String getL7dUserTip()
    {
        if (isRequired())
            return new LocalizedString(OperationArgument.class.getName(), "proceed").toString();
        else
            return new LocalizedString(OperationArgument.class.getName(), "optional").toString();
    }

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

    /**
     * @param listener
     * @see java.beans.PropertyChangeSupport#addPropertyChangeListener(java.beans.PropertyChangeListener)
     */
    public void addPropertyChangeListener(PropertyChangeListener listener)
    {
        support.addPropertyChangeListener(listener);
    }

    /**
     * @param listener
     * @see java.beans.PropertyChangeSupport#removePropertyChangeListener(java.beans.PropertyChangeListener)
     */
    public void removePropertyChangeListener(PropertyChangeListener listener)
    {
        support.removePropertyChangeListener(listener);
    }

    /**
     * @param propertyName
     * @param listener
     * @see java.beans.PropertyChangeSupport#addPropertyChangeListener(java.lang.String,
     *      java.beans.PropertyChangeListener)
     */
    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener)
    {
        support.addPropertyChangeListener(propertyName, listener);
    }

    /**
     * @param propertyName
     * @param listener
     * @see java.beans.PropertyChangeSupport#removePropertyChangeListener(java.lang.String,
     *      java.beans.PropertyChangeListener)
     */
    public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener)
    {
        support.removePropertyChangeListener(propertyName, listener);
    }

    /**
     * @param propertyName
     * @return
     * @see java.beans.PropertyChangeSupport#hasListeners(java.lang.String)
     */
    public boolean hasListeners(String propertyName)
    {
        return support.hasListeners(propertyName);
    }
}

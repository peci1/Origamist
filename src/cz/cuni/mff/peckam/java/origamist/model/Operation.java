/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.model;

import javax.swing.ImageIcon;
import javax.xml.bind.annotation.XmlTransient;

import cz.cuni.mff.peckam.java.origamist.exceptions.InvalidOperationException;
import cz.cuni.mff.peckam.java.origamist.model.jaxb.Operations;
import cz.cuni.mff.peckam.java.origamist.modelstate.ModelState;
import cz.cuni.mff.peckam.java.origamist.services.ServiceLocator;
import cz.cuni.mff.peckam.java.origamist.services.interfaces.HashCodeAndEqualsHelper;
import cz.cuni.mff.peckam.java.origamist.utils.HasBoundProperties;

/**
 * An operation on the model.
 * <p>
 * Provides property: icon
 * 
 * @author Martin Pecka
 */
@XmlTransient
public abstract class Operation extends cz.cuni.mff.peckam.java.origamist.model.jaxb.Operation implements
        HasBoundProperties
{

    /** The icon property. */
    public static final String    ICON_PROPERTY = "icon:cz.cuni.mff.peckam.java.origamist.model.Operation";

    /** The type property. */
    public static final String    TYPE_PROPERTY = "type:cz.cuni.mff.peckam.java.origamist.model.Operation";

    /**
     * Icon of this operation.
     */
    protected transient ImageIcon icon          = null;

    /**
     * @return The icon of this operation
     */
    @XmlTransient
    public ImageIcon getIcon()
    {
        return icon;
    }

    /**
     * Set the icon of this operation.
     * 
     * If you change the type of this operation, the image gets updated to the default icon of the new type.
     * 
     * @param icon The icon to set
     */
    public void setIcon(ImageIcon icon)
    {
        ImageIcon oldIcon = this.icon;
        this.icon = icon;
        if ((oldIcon != icon && (oldIcon != null || icon != null)) || (oldIcon != null && !oldIcon.equals(icon)))
            support.firePropertyChange(ICON_PROPERTY, oldIcon, icon);
    }

    @Override
    public void setType(Operations value)
    {
        Operations oldType = this.type;
        super.setType(value);
        if (oldType != value)
            support.firePropertyChange(TYPE_PROPERTY, oldType, value);
        setIcon(OperationsHelper.getIcon(value));
    }

    /**
     * Perform folding from the previous state to a new state by this operation. Calling this method will alter the
     * passed ModelState.
     * 
     * <p>
     * Subclasses of {@link Operation} should overwrite this method. They shouldn't call
     * <code>super.getModelState(previousState)</code>.
     * 
     * @param previousState The state the model has now.
     * @return The passed-in state of the model altered by performing this operation.
     * 
     * @throws InvalidOperationException If the operation cannot be completed.
     */
    public ModelState getModelState(ModelState previousState) throws InvalidOperationException
    {
        throw new UnsupportedOperationException("Class " + getClass() + " is a subclass of " + Operation.class
                + " and therefore must overwrite the getModelState() method.");
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = super.hashCode();
        if (icon == null) {
            result = prime * result;
        } else {
            result = prime * result + ServiceLocator.get(HashCodeAndEqualsHelper.class).hashCode(icon);
        }
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        Operation other = (Operation) obj;
        if (icon == null) {
            if (other.icon != null)
                return false;
        } else if (!ServiceLocator.get(HashCodeAndEqualsHelper.class).equals(icon, other.icon))
            return false;
        return true;
    }
}

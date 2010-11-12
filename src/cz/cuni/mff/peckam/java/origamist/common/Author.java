/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.common;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.net.URI;

import javax.xml.bind.annotation.XmlTransient;

/**
 * Represents the author of the model.
 * 
 * @author Martin Pecka
 */
public class Author extends cz.cuni.mff.peckam.java.origamist.common.jaxb.Author
{

    /** Property change listeners. */
    @XmlTransient
    protected PropertyChangeSupport propertyListeners = new PropertyChangeSupport(this);

    @Override
    public void setName(String value)
    {
        String oldValue = getName();
        super.setName(value);
        if ((oldValue == null && value != null) || (oldValue != null && value == null)
                || (oldValue != null && value != null && !oldValue.equals(value)))
            propertyListeners.firePropertyChange("name", oldValue, value);
    }

    @Override
    public void setHomepage(URI value)
    {
        URI oldValue = getHomepage();
        super.setHomepage(value);
        if ((oldValue == null && value != null) || (oldValue != null && value == null)
                || (oldValue != null && value != null && !oldValue.equals(value)))
            propertyListeners.firePropertyChange("homepage", oldValue, value);
    }

    @Override
    public String toString()
    {
        return "Author [name=" + name + ", homepage=" + homepage + "]";
    }

    /**
     * @param listener
     * @see java.beans.PropertyChangeSupport#addPropertyChangeListener(java.beans.PropertyChangeListener)
     */
    public void addPropertyChangeListener(PropertyChangeListener listener)
    {
        propertyListeners.addPropertyChangeListener(listener);
    }

    /**
     * @param listener
     * @see java.beans.PropertyChangeSupport#removePropertyChangeListener(java.beans.PropertyChangeListener)
     */
    public void removePropertyChangeListener(PropertyChangeListener listener)
    {
        propertyListeners.removePropertyChangeListener(listener);
    }

    /**
     * @return
     * @see java.beans.PropertyChangeSupport#getPropertyChangeListeners()
     */
    public PropertyChangeListener[] getPropertyChangeListeners()
    {
        return propertyListeners.getPropertyChangeListeners();
    }

    /**
     * @param propertyName
     * @param listener
     * @see java.beans.PropertyChangeSupport#addPropertyChangeListener(java.lang.String,
     *      java.beans.PropertyChangeListener)
     */
    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener)
    {
        propertyListeners.addPropertyChangeListener(propertyName, listener);
    }

    /**
     * @param propertyName
     * @param listener
     * @see java.beans.PropertyChangeSupport#removePropertyChangeListener(java.lang.String,
     *      java.beans.PropertyChangeListener)
     */
    public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener)
    {
        propertyListeners.removePropertyChangeListener(propertyName, listener);
    }

    /**
     * @param propertyName
     * @return
     * @see java.beans.PropertyChangeSupport#getPropertyChangeListeners(java.lang.String)
     */
    public PropertyChangeListener[] getPropertyChangeListeners(String propertyName)
    {
        return propertyListeners.getPropertyChangeListeners(propertyName);
    }

}

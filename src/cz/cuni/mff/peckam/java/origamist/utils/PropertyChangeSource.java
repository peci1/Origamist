/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.utils;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

/**
 * The source of <code>PropertyChangeEvent</code>s. Provides basic support for it.
 * 
 * @author Martin Pecka
 */
public class PropertyChangeSource
{

    /** The source of the property changes. */
    protected Object                                          source                  = null;

    /** Listeners to property changes. */
    protected Hashtable<String, List<PropertyChangeListener>> propertyChangeListeners = new Hashtable<String, List<PropertyChangeListener>>();

    /**
     * The source of the changes will be <code>this</code>.
     */
    public PropertyChangeSource()
    {
        this.source = this;
    }

    /**
     * @param source The source of the changes.
     */
    public PropertyChangeSource(Object source)
    {
        this.source = source;
    }

    /**
     * Notify observers on the change of the <code>property</code>.
     * 
     * The event is fired only if newValue and oldValue aren't the same.
     * 
     * @param <T> Type of the property that has been changed.
     * @param property Name of the property that has been changed.
     * @param oldValue The old property value.
     * @param newValue The new property value.
     */
    protected <T> void firePropertyChange(String property, T oldValue, T newValue)
    {
        if (oldValue == null) {
            if (newValue == null)
                return;
        } else if (oldValue.equals(newValue)) {
            return;
        }

        List<PropertyChangeListener> listeners = propertyChangeListeners.get(property);
        if (listeners == null)
            return;
        for (PropertyChangeListener l : listeners)
            l.propertyChange(new PropertyChangeEvent(this.source, property, oldValue, newValue));
    }

    /**
     * Adds a listener of the changes of the given property.
     * 
     * Multiple observers for a single property are allowed and will be notified in the order they were registered.
     * 
     * @param property The property to listen its changes to.
     * @param listener The listener to be bound to the change event.
     */
    public void addPropertyChangeListener(String property, PropertyChangeListener listener)
    {
        List<PropertyChangeListener> listeners = propertyChangeListeners.get(property);
        if (listeners == null) {
            listeners = new LinkedList<PropertyChangeListener>();
            propertyChangeListeners.put(property, listeners);
        }
        listeners.add(listener);
    }

    /**
     * Removes the given listener from the list of observers on the changes of the given property.
     * 
     * @param property The property to remove the listener from.
     * @param listener The listener to remove.
     */
    public void removePropertyChangeListener(String property, PropertyChangeListener listener)
    {
        List<PropertyChangeListener> listeners = propertyChangeListeners.get(property);
        if (listeners == null)
            return;
        listeners.remove(listener);
    }

    /**
     * Removes all observers on the changes of the given property.
     * 
     * @param property The property to remove observers from.
     */
    public void removeAllPropertyChangeListeners(String property)
    {
        List<PropertyChangeListener> listeners = propertyChangeListeners.get(property);
        if (listeners == null)
            return;
        listeners.clear();
    }

}

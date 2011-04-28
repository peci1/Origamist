/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.utils;

import java.beans.PropertyChangeListener;
import java.util.List;

/**
 * Interface that defines a class that has some bound properties and allows to add listeners to them.
 * 
 * @author Martin Pecka
 */
public interface HasBoundProperties
{
    /**
     * Add a PropertyChangeListener to the listener list.
     * The listener is registered for all properties.
     * The same listener object may be added more than once, and will be called as many times as it is added.
     * If <code>listener</code> is null, no exception is thrown and no action is taken.
     * 
     * @param listener The PropertyChangeListener to be added.
     */
    void addPropertyChangeListener(PropertyChangeListener listener);

    /**
     * Add a PropertyChangeListener for a specific property. The listener
     * will be invoked only when a call on firePropertyChange names that specific property.
     * The same listener object may be added more than once. For each property, the listener will be invoked the number
     * of times it was added for that property.
     * If <code>property</code> or <code>listener</code> is null, no exception is thrown and no action is taken.
     * 
     * @param property The name of the property to listen on.
     * @param listener The PropertyChangeListener to be added.
     */
    void addPropertyChangeListener(String property, PropertyChangeListener listener);

    /**
     * Remove a PropertyChangeListener for a specific property.
     * If <code>listener</code> was added more than once to the same event source for the specified property, it will be
     * notified one less time after being removed.
     * If <code>property</code> is null, no exception is thrown and no action is taken.
     * If <code>listener</code> is null, or was never added for the specified property, no exception is thrown and no
     * action is taken.
     * 
     * @param property The name of the property that was listened on.
     * @param listener The PropertyChangeListener to be removed.
     */
    void removePropertyChangeListener(String property, PropertyChangeListener listener);

    /**
     * Remove a PropertyChangeListener from the listener list.
     * This removes a PropertyChangeListener that was registered for all properties.
     * If <code>listener</code> was added more than once to the same event source, it will be notified one less time
     * after being removed.
     * If <code>listener</code> is null, or was never added, no exception is thrown and no action is taken.
     * 
     * @param listener The PropertyChangeListener to be removed.
     */
    void removePropertyChangeListener(PropertyChangeListener listener);

    /**
     * Removes all occurences of the given instance of listener from this support.
     * 
     * @param listener The listener instance to remove.
     * 
     * @return The list of properties this listener was removed from (<code>null</code> signalizes all properties).
     */
    List<String> removeAllListeners(PropertyChangeListener listener);
}

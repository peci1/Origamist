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

    /**
     * Add a PropertyChangeListener for a specific hierarchicak property. The listener will be invoked only when a call
     * on firePropertyChange names that specific property.
     * The same listener object may be added more than once. For each property, the listener will be invoked the number
     * of times it was added for that property.
     * If <code>propertyName</code> or <code>listener</code> is null, no exception is thrown and no action is taken.
     * If <code>propertyName</code> is empty, {@link #addPropertyChangeListener(PropertyChangeListener)} is called.
     * 
     * @param listener The PropertyChangeListener to be added
     * @param propertyName The components of hierarchical name of the property to listen on.
     */
    void addPropertyChangeListener(PropertyChangeListener listener, String... propertyName);

    /**
     * Remove a PropertyChangeListener for a specific hierarchical property.
     * If <code>listener</code> was added more than once to the same event source for the specified property, it will be
     * notified one less time after being removed.
     * If <code>propertyName</code> is null, no exception is thrown and no action is taken.
     * If <code>propertyName</code> is empty, {@link #removePropertyChangeListener(PropertyChangeListener)} is called.
     * If <code>listener</code> is null, or was never added for the specified property, no exception is thrown and no
     * action is taken.
     * 
     * @param listener The PropertyChangeListener to be removed
     * @param propertyName The components of hierarchical name of the property that was listened on.
     */
    void removePropertyChangeListener(PropertyChangeListener listener, String... propertyName);

    /**
     * Add a listener that will receive all events with property names beginning with the specified prefix.
     * <p>
     * Remove this listener by {@link #removePrefixedPropertyChangeListener(PropertyChangeListener)}.
     * 
     * @param listener The listener to be triggered.
     * @param prefix The prefix with which this listener will be triggered. If more strings are provided, they are
     *            composed together to form a hierarchical property name.
     */
    void addPrefixedPropertyChangeListener(PropertyChangeListener listener, String... prefix);

    /**
     * Remove all occurences of a prefixed listener added by
     * {@link #addPrefixedPropertyChangeListener(String, PropertyChangeListener)}.
     * 
     * @param listener The listener to be removed.
     */
    void removePrefixedPropertyChangeListener(PropertyChangeListener listener);
}

/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.utils;

import java.util.List;

/**
 * Interface that defines a class that has some observable properties and allows to add listeners to them.
 * 
 * @author Martin Pecka
 */
public interface HasObservableProperties
{
    /**
     * Add a ObservablePropertyListener to the listener list.
     * The listener is registered for all properties.
     * The same listener object may be added more than once, and will be called as many times as it is added.
     * If <code>listener</code> is null, no exception is thrown and no action is taken.
     * 
     * @param listener The ObservablePropertyListener to be added.
     */
    void addObservablePropertyListener(ObservablePropertyListener<?> listener);

    /**
     * Add a ObservablePropertyListener for a specific property. The listener
     * will be invoked only when a call on firePropertyChange names that specific property.
     * The same listener object may be added more than once. For each property, the listener will be invoked the number
     * of times it was added for that property.
     * If <code>property</code> or <code>listener</code> is null, no exception is thrown and no action is taken.
     * 
     * @param property The name of the property to listen on.
     * @param listener The ObservablePropertyListener to be added.
     */
    void addObservablePropertyListener(String property, ObservablePropertyListener<?> listener);

    /**
     * Remove a ObservablePropertyListener for a specific property.
     * If <code>listener</code> was added more than once to the same event source for the specified property, it will be
     * notified one less time after being removed.
     * If <code>property</code> is null, no exception is thrown and no action is taken.
     * If <code>listener</code> is null, or was never added for the specified property, no exception is thrown and no
     * action is taken.
     * 
     * @param property The name of the property that was listened on.
     * @param listener The ObservablePropertyListener to be removed.
     */
    void removeObservablePropertyListener(String property, ObservablePropertyListener<?> listener);

    /**
     * Remove a ObservablePropertyListener from the listener list.
     * This removes a ObservablePropertyListener that was registered for all properties.
     * If <code>listener</code> was added more than once to the same event source, it will be notified one less time
     * after being removed.
     * If <code>listener</code> is null, or was never added, no exception is thrown and no action is taken.
     * 
     * @param listener The ObservablePropertyListener to be removed.
     */
    void removeObservablePropertyListener(ObservablePropertyListener<?> listener);

    /**
     * Removes all occurences of the given instance of listener from this support.
     * 
     * @param listener The listener instance to remove.
     * 
     * @return The list of properties this listener was removed from (<code>null</code> signalizes all properties).
     */
    List<String> removeAllObservablePropertyListeners(ObservablePropertyListener<?> listener);

    /**
     * Add a ObservablePropertyListener for a specific hierarchical property. The listener will be invoked only when a
     * call on firePropertyChange names that specific property.
     * The same listener object may be added more than once. For each property, the listener will be invoked the number
     * of times it was added for that property.
     * If <code>property</code> or <code>listener</code> is null, no exception is thrown and no action is taken.
     * If <code>property</code> is empty, {@link #addObservablePropertyListener(ObservablePropertyListener)} will be
     * called.
     * 
     * @param listener The ObservablePropertyListener to be added.
     * @param property The components of the hierarchical name of the property to listen on.
     */
    void addObservablePropertyListener(ObservablePropertyListener<?> listener, String... property);

    /**
     * Remove a ObservablePropertyListener for a specific hierarchical property.
     * If <code>listener</code> was added more than once to the same event source for the specified property, it will be
     * notified one less time after being removed.
     * If <code>listener</code> is null, or was never added for the specified property, no exception is thrown and no
     * action is taken.
     * If <code>property</code> is null, no exception is thrown and no action is taken.
     * If <code>property</code> is empty, {@link #removeObservablePropertyListener(ObservablePropertyListener)} will be
     * called.
     * 
     * @param listener The ObservablePropertyListener to be removed.
     * @param property The components of the hierarchical name of the property that was listened on.
     */
    void removeObservablePropertyListener(ObservablePropertyListener<?> listener, String... property);

    /**
     * Add a listener that will receive all events with property names beginning with the specified prefix.
     * <p>
     * Remove this listener by {@link #removePrefixedObservablePropertyListener(ObservablePropertyListener)}.
     * 
     * @param listener The listener to be triggered.
     * @param prefix The prefix with which this listener will be triggered. If it composes of more names, a hierarchical
     *            property name is created out of the given property names.
     */
    <T> void addPrefixedObservablePropertyListener(ObservablePropertyListener<T> listener, String... prefix);

    /**
     * Remove all occurences of a prefixed listener added by
     * {@link #addPrefixedObservablePropertyListener(String, ObservablePropertyListener)}.
     * 
     * @param listener The listener to be removed.
     */
    <T> void removePrefixedObservablePropertyListener(ObservablePropertyListener<T> listener);
}

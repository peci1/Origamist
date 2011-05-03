/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.utils;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeListenerProxy;
import java.beans.PropertyChangeSupport;
import java.util.LinkedList;
import java.util.List;

/**
 * A property change support allowing to remove a given listener instance completely from this support.
 * 
 * @author Martin Pecka
 */
public class CustomPropertyChangeSupport extends PropertyChangeSupport
{
    /** */
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a <code>CustomPropertyChangeSupport</code> object.
     * 
     * @param sourceBean The bean to be given as the source for any events.
     */
    public CustomPropertyChangeSupport(Object sourceBean)
    {
        super(sourceBean);
    }

    /**
     * Removes all occurences of the given instance of listener from this support.
     * 
     * @param listener The listener instance to remove.
     * 
     * @return The list of properties this listener was removed from (<code>null</code> signalizes all properties).
     */
    public List<String> removeAllListeners(PropertyChangeListener listener)
    {
        List<String> result = new LinkedList<String>();
        PropertyChangeListener[] listeners = getPropertyChangeListeners();
        for (int i = 0; i < listeners.length; i++) {
            if (listeners[i] instanceof PropertyChangeListenerProxy) {
                PropertyChangeListenerProxy proxy = (PropertyChangeListenerProxy) listeners[i];
                if (proxy.getListener() == listener) {
                    removePropertyChangeListener(proxy.getPropertyName(), (PropertyChangeListener) proxy.getListener());
                    result.add(proxy.getPropertyName());
                }
            } else {
                if (listeners[i] == listener) {
                    removePropertyChangeListener(listener);
                    result.add(null);
                }
            }
        }
        return result;
    }

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
    public synchronized void addPropertyChangeListener(PropertyChangeListener listener, String... propertyName)
    {
        String name = getHierarchicalName(propertyName);
        addPropertyChangeListener("".equals(name) ? null : name, listener);
    }

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
    public synchronized void removePropertyChangeListener(PropertyChangeListener listener, String... propertyName)
    {
        String name = getHierarchicalName(propertyName);
        removePropertyChangeListener("".equals(name) ? null : name, listener);
    }

    /**
     * Add a listener that will receive all events with property names beginning with the specified prefix.
     * <p>
     * Remove this listener by {@link #removePrefixedPropertyChangeListener(PropertyChangeListener)}.
     * 
     * @param listener The listener to be triggered.
     * @param prefix The prefix with which this listener will be triggered. If more strings are provided, they are
     *            composed together to form a hierarchical property name.
     */
    public void addPrefixedPropertyChangeListener(PropertyChangeListener listener, String... prefix)
    {
        addPropertyChangeListener(new PrefixedPropertyChangeListener(getHierarchicalName(prefix), listener));
    }

    /**
     * Remove all occurences of a prefixed listener added by
     * {@link #addPrefixedPropertyChangeListener(String, PropertyChangeListener)}.
     * 
     * @param listener The listener to be removed.
     */
    public void removePrefixedPropertyChangeListener(PropertyChangeListener listener)
    {
        List<PropertyChangeListener> toRemove = new LinkedList<PropertyChangeListener>();
        for (PropertyChangeListener l : getPropertyChangeListeners()) {
            if (l instanceof PrefixedPropertyChangeListener) {
                if (((PrefixedPropertyChangeListener) l).listener == listener)
                    toRemove.add(l);
            }
        }

        for (PropertyChangeListener l : toRemove) {
            removePropertyChangeListener(l);
        }
    }

    /**
     * Build the hierarchical property name out of the given list of property names.
     * 
     * @param names The list of property names to build the hierarchical name of.
     * @return The hierarchical property name.
     */
    protected String getHierarchicalName(String... names)
    {
        if (names == null || names.length == 0)
            return "";

        StringBuilder sb = new StringBuilder(names[0]);
        for (int i = 1; i < names.length; i++)
            sb.append("@").append(names[i]);

        return sb.toString();
    }

    /**
     * A listener that is triggered on events with specific prefix.
     * 
     * @author Martin Pecka
     */
    protected class PrefixedPropertyChangeListener implements PropertyChangeListener
    {
        /** The prefix with which this listener will be triggered. */
        protected String                 prefix;
        /** The listener to be triggered. */
        protected PropertyChangeListener listener;

        /**
         * @param prefix The prefix with which this listener will be triggered.
         * @param listener The listener to be triggered.
         */
        public PrefixedPropertyChangeListener(String prefix, PropertyChangeListener listener)
        {
            this.prefix = prefix;
            this.listener = listener;
        }

        @Override
        public void propertyChange(PropertyChangeEvent evt)
        {
            if (evt.getPropertyName().startsWith(prefix))
                listener.propertyChange(evt);
        }
    }
}

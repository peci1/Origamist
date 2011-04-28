/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.utils;

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

}

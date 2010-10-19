/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.configuration;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

/**
 * A configuration of the program.
 * 
 * Properties that fire PropertyChangeEvent when they are changed:
 * locale
 * diagramLocale
 * 
 * @author Martin Pecka
 */
public class Configuration
{

    /**
     * General locale of the program.
     */
    protected Locale                                locale                  = Locale.getDefault();

    /**
     * The preferred locale for diagrams. If null, means that it is the same as
     * locale.
     */
    protected Locale                                diagramLocale           = null;

    /** Listeners to property changes. */
    Hashtable<String, List<PropertyChangeListener>> propertyChangeListeners = new Hashtable<String, List<PropertyChangeListener>>();

    /**
     * @return the locale
     */
    public Locale getLocale()
    {
        return locale;
    }

    /**
     * @param locale the locale to set
     */
    public void setLocale(Locale locale)
    {
        Locale oldLocale = this.locale;
        this.locale = locale;
        firePropertyChange("locale", locale, oldLocale);
    }

    /**
     * @return the diagramLocale
     */
    public Locale getDiagramLocale()
    {
        return diagramLocale == null ? locale : diagramLocale;
    }

    /**
     * @param diagramLocale the diagramLocale to set
     */
    public void setDiagramLocale(Locale diagramLocale)
    {
        Locale oldLocale = this.diagramLocale;
        this.diagramLocale = diagramLocale;
        firePropertyChange("diagramLocale", oldLocale, diagramLocale);
    }

    /**
     * Notify listeners on the change of the <code>property</code>.
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
            l.propertyChange(new PropertyChangeEvent(this, property, oldValue, newValue));
    }

    /**
     * Adds a listener of the changes of the given property.
     * 
     * Multiple listeners for a single property are allowed and will be notified in the order they were registered.
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
     * Removes the given listener from the list of listeners on the changes of the given property.
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
     * Removes all listeners on the changes of the given property.
     * 
     * @param property The property to remove listeners from.
     */
    public void removeAllPropertyChangeListeners(String property)
    {
        List<PropertyChangeListener> listeners = propertyChangeListeners.get(property);
        if (listeners == null)
            return;
        listeners.clear();
    }

}

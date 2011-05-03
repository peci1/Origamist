/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.utils;

import java.beans.PropertyChangeEvent;

/**
 * A property change event that is propagated some levels up, so it takes account of the original bean which caused the
 * propagation.
 * 
 * @author Martin Pecka
 */
public class PropagatedPropertyChangeEvent extends PropertyChangeEvent
{

    /** */
    private static final long serialVersionUID = 2223506179270063979L;

    /** The bean that fired originally caused this event's propagation. */
    protected Object          deepSource;

    /**
     * Constructs a new <code>PropagatedPropertyChangeEvent</code> with deepSource equal to source.
     * 
     * @param source The bean that fired the event.
     * @param propertyName The programmatic name of the property that was changed.
     * @param oldValue The old value of the property.
     * @param newValue The new value of the property.
     */
    public PropagatedPropertyChangeEvent(Object source, String propertyName, Object oldValue, Object newValue)
    {
        this(source, source, propertyName, oldValue, newValue);
    }

    /**
     * Constructs a new <code>PropagatedPropertyChangeEvent</code>.
     * 
     * @param source The bean that fired the event.
     * @param deepSource The bean that fired originally caused this event's propagation.
     * @param propertyName The programmatic name of the property that was changed.
     * @param oldValue The old value of the property.
     * @param newValue The new value of the property.
     */
    public PropagatedPropertyChangeEvent(Object source, Object deepSource, String propertyName, Object oldValue,
            Object newValue)
    {
        super(source, propertyName, oldValue, newValue);
        this.deepSource = deepSource;
    }

    /**
     * Constructs a new {@link PropagatedPropertyChangeEvent} from the given event.
     * 
     * If the event isn't a {@link PropagatedPropertyChangeEvent}, treat it's source also as deepSource, otherwise copy
     * the deepSource and set source to the given source.
     * 
     * @param source The bean that fired the event.
     * @param propagatedEvent The event to take values from.
     */
    public PropagatedPropertyChangeEvent(Object source, PropertyChangeEvent propagatedEvent)
    {
        this(source, null, propagatedEvent);
    }

    /**
     * Constructs a new {@link PropagatedPropertyChangeEvent} from the given event.
     * 
     * If the event isn't a {@link PropagatedPropertyChangeEvent}, treat it's source also as deepSource, otherwise copy
     * the deepSource and set source to the given source.
     * 
     * @param source The bean that fired the event.
     * @param propertyPrefix The prefix to add before the property name (can denote propagation hierarchy).
     * @param propagatedEvent The event to take values from.
     */
    public PropagatedPropertyChangeEvent(Object source, String propertyPrefix, PropertyChangeEvent propagatedEvent)
    {
        this(
                source,
                (propagatedEvent instanceof PropagatedPropertyChangeEvent ? ((PropagatedPropertyChangeEvent) propagatedEvent).deepSource
                        : propagatedEvent.getSource()), (propertyPrefix == null ? "" : (propertyPrefix + "@"))
                        + propagatedEvent.getPropertyName(), propagatedEvent.getOldValue(), propagatedEvent
                        .getNewValue());
    }

    /**
     * @return The bean that fired originally caused this event's propagation.
     */
    public Object getDeepSource()
    {
        return deepSource;
    }
}

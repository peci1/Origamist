/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.utils;

/**
 * An observable property change event that is propagated some levels up, so it takes account of the original bean which
 * caused the propagation.
 * 
 * @author Martin Pecka
 */
public class PropagatedObservablePropertyEvent<T> extends ObservablePropertyEvent<T>
{
    /** The bean that fired originally caused this event's propagation. */
    protected Object deepSource;

    /**
     * Constructs a new {@link PropagatedObservablePropertyEvent} from the given event.
     * 
     * If the event isn't a {@link PropagatedObservablePropertyEvent}, treat it's source also as deepSource, otherwise
     * copy
     * the deepSource and set source to the given source.
     * 
     * @param source The bean that fired the event.
     * @param propagatedEvent The event to take values from.
     */
    public PropagatedObservablePropertyEvent(Object source, ObservablePropertyEvent<? extends T> propagatedEvent)
    {
        this(source, null, propagatedEvent);
    }

    /**
     * Constructs a new {@link PropagatedObservablePropertyEvent} from the given event.
     * 
     * If the event isn't a {@link PropagatedObservablePropertyEvent}, treat it's source also as deepSource, otherwise
     * copy
     * the deepSource and set source to the given source.
     * 
     * @param source The bean that fired the event.
     * @param propertyPrefix The prefix to add before the property name (can denote propagation hierarchy).
     * @param propagatedEvent The event to take values from.
     */
    public PropagatedObservablePropertyEvent(Object source, String propertyPrefix,
            ObservablePropertyEvent<? extends T> propagatedEvent)
    {
        super(source, (propertyPrefix == null ? "" : (propertyPrefix + "@")) + propagatedEvent.getPropertyName(),
                propagatedEvent.getEvent());
        if (propagatedEvent instanceof PropagatedObservablePropertyEvent)
            this.deepSource = ((PropagatedObservablePropertyEvent<? extends T>) propagatedEvent).deepSource;
        else
            this.deepSource = source;
    }

    /**
     * @return The bean that fired originally caused this event's propagation.
     */
    public Object getDeepSource()
    {
        return deepSource;
    }
}

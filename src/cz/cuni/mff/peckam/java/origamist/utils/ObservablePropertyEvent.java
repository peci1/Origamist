/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.utils;


/**
 * An event on observable property.
 * 
 * @author Martin Pecka
 */
public class ObservablePropertyEvent<T>
{
    /** The source of the event. */
    protected Object                          source;

    /** The name of the property. */
    protected String                          propertyName;

    /** The change event. */
    protected ChangeNotification<? extends T> event;

    /**
     * @param source The source of this event.
     * @param propertyName The name of the observable property this event is fired for.
     * @param event The event that occured.
     */
    public ObservablePropertyEvent(Object source, String propertyName, ChangeNotification<? extends T> event)
    {
        this.source = source;
        this.propertyName = propertyName;
        this.event = event;
    }

    /**
     * @return The source of this event.
     */
    public Object getSource()
    {
        return source;
    }

    /**
     * @return The name of the observable property this event is fired for.
     */
    public String getPropertyName()
    {
        return propertyName;
    }

    /**
     * @return The event that occured.
     */
    public ChangeNotification<? extends T> getEvent()
    {
        return event;
    }
}

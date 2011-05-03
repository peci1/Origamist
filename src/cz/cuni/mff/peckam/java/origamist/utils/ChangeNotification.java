/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.utils;

import java.util.Collection;

import cz.cuni.mff.peckam.java.origamist.utils.ObservableList.ChangeTypes;

/**
 * Notification about a change (addition/removal of an item) in the list
 * 
 * @param T Type of the elements in the observed list.
 * 
 * @author Martin Pecka
 */
public class ChangeNotification<T>
{
    /** The collection that fired this notification. */
    protected Collection<?> source;

    /** The item that was changed. */
    protected T             item;

    /** The old value (only for change). */
    protected T             oldItem;

    /**
     * Type of the change
     */
    protected ChangeTypes   changeType;

    /**
     * @param source The collection that fired this notification.
     * @param item The item that was changed
     * @param changeType The type of the change
     */
    public ChangeNotification(Collection<?> source, T item, ChangeTypes changeType)
    {
        this(source, item, item, changeType);
    }

    /**
     * @param source The collection that fired this notification.
     * @param item The item that was changed
     * @param oldItem The previous item (if the type is CHANGE)
     * @param changeType The type of the change
     */
    public ChangeNotification(Collection<?> source, T item, T oldItem, ChangeTypes changeType)
    {
        this.source = source;
        this.item = item;
        this.oldItem = oldItem;
        this.changeType = changeType;
    }

    /**
     * @return the source
     */
    public Collection<?> getSource()
    {
        return source;
    }

    /**
     * @return the item
     */
    public T getItem()
    {
        return item;
    }

    /**
     * @return the oldItem
     */
    public T getOldItem()
    {
        return oldItem;
    }

    /**
     * @return the changeType
     */
    public ChangeTypes getChangeType()
    {
        return changeType;
    }
}
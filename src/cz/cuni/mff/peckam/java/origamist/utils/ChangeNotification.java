/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.utils;

import cz.cuni.mff.peckam.java.origamist.utils.ChangeNotifyingList.ChangeTypes;

/**
 * Notification about a change (addition/removal of an item) in the list
 * 
 * @author Martin Pecka
 * @param <T> Type of the item that has been modified
 */
public class ChangeNotification<T>
{
    /**
     * The item that was changed
     */
    protected T           item;

    /**
     * The old value (only for change)
     */
    protected T           oldItem;

    /**
     * Type of the change
     */
    protected ChangeTypes changeType;

    /**
     * @param item The item that was changed
     * @param changeType The type of the change
     */
    public ChangeNotification(T item, ChangeTypes changeType)
    {
        this(item, null, changeType);
    }

    /**
     * @param item The item that was changed
     * @param oldItem The previous item (if the type is CHANGE)
     * @param changeType The type of the change
     */
    public ChangeNotification(T item, T oldItem, ChangeTypes changeType)
    {
        this.item = item;
        this.oldItem = oldItem;
        this.changeType = changeType;
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
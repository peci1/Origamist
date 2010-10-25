/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.utils;

import java.util.Hashtable;

/**
 * This change notification listener projects the changes in the given
 * hashtable.
 * 
 * @param T Type of the elements in the list.
 * @param K Type of the keys of the hashtable.
 * @param V Type of the values of the hashtable.
 * 
 * @author Martin Pecka
 */
public class HashtableChangeNotificationListener<T, K, V> implements ChangeNotificationListener<T>
{

    /**
     * The Hashtable bound to this listener.
     */
    protected Hashtable<K, V>                  table   = null;

    /** This adapter extracts keys and values from the changed item. */
    protected HashtableElementAdapter<T, K, V> adapter = null;

    /**
     * @param table The Hashtable bound to this listener
     * @param adapter This adapter extracts keys and values from the changed item.
     */
    public HashtableChangeNotificationListener(Hashtable<K, V> table, HashtableElementAdapter<T, K, V> adapter)
    {
        this.table = table;
        this.adapter = adapter;
    }

    @Override
    public void changePerformed(ChangeNotification<T> change)
    {
        switch (change.getChangeType()) {
            case ADD:
                table.put(adapter.getKey(change.getItem()), adapter.getValue(change.getItem()));
                break;
            case CHANGE:
                table.remove(adapter.getKey(change.getOldItem()));
                table.put(adapter.getKey(change.getItem()), adapter.getValue(change.getItem()));
                break;
            case REMOVE:
                table.remove(adapter.getKey(change.getOldItem()));
                break;
        }
    }

}

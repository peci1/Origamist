/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.utils;

import java.util.Hashtable;

/**
 * This observer projects the changes into the given hashtable.
 * 
 * @param T Type of the observed elements.
 * @param K Type of the keys of the hashtable.
 * @param V Type of the values of the hashtable.
 * 
 * @author Martin Pecka
 */
public class HashtableObserver<T, K, V> implements Observer<T>
{

    /**
     * The Hashtable bound to this observer.
     */
    protected Hashtable<K, V>                  table   = null;

    /** This adapter extracts keys and values from the changed item. */
    protected HashtableElementAdapter<T, K, V> adapter = null;

    /**
     * @param table The Hashtable bound to this observer.
     * @param adapter This adapter extracts keys and values from the changed item.
     */
    public HashtableObserver(Hashtable<K, V> table, HashtableElementAdapter<T, K, V> adapter)
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

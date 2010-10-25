/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.utils;

/**
 * This adapter extracts keys and values from the changed item.
 * 
 * @param T Type of the elements in the list.
 * @param K Type of the keys of the hashtable.
 * @param V Type of the values of the hashtable.
 * 
 * @author Martin Pecka
 */
public interface HashtableElementAdapter<T, K, V>
{
    /**
     * Returns the key to be used in a hashtable to point to the value.
     * 
     * @param item The item to get the key for.
     * @return The key to be used in a hashtable to point to the value.
     */
    K getKey(T item);

    /**
     * Returns the key to be used in a hashtable to point to the value.
     * 
     * @param item The item to get the key for.
     * @return The key to be used in a hashtable to point to the value.
     */
    V getValue(T item);
}

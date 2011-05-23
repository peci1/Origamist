/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.unused.utils;

import java.util.Map;

/**
 * A map that allows asking if it contains some epsilon-equal keys.
 * 
 * @author Martin Pecka
 */
public interface EpsilonMap<K, V> extends Map<K, V>
{
    /**
     * Return the value associated to a key that is epsilon-equal to the given key.
     * 
     * @param key The key to find value for.
     * @return The value associated to a key that is epsilon-equal to the given key.
     */
    V epsilonGet(K key);

    /**
     * Put the value in the map. If the map contains a key epsilon-equal to the given key, the value is associated to
     * that key, otherwise a new key is inserted into the map.
     * 
     * @param key The key to insert the value for.
     * @param value The value to insert.
     * @return The previous value of the key or <code>null</code> if no epsilon-equal key existed in the map before.
     */
    V epsilonPut(K key, V value);

    /**
     * Remove the value associated to a key that is epsilon-equal to a key in the map.
     * 
     * If two or more keys in the map are epsilon-equal to the given key, one of them is chosen in an unspecified way.
     * 
     * @param key The key to remove the mapping for.
     * @return The value associated to a key that is epsilon-equal to a key in the map.
     */
    V epsilonRemove(K key);

    /**
     * Return true if the map contains a key that is epsilon-equal to the given key.
     * 
     * @param key The key to search for.
     * @return true if the map contains a key that is epsilon-equal to the given key.
     */
    boolean epsilonContainsKey(K key);

    /**
     * Put all values from the given map into this map. Use epsilonPut()-like behavior for inserting the values.
     * 
     * @param map The map to read the mappings from.
     */
    void epsilonPutAll(Map<? extends K, ? extends V> map);

    /**
     * Return true if the given map has epsilon-equal keys to this map's keys and the associated values are equal.
     * 
     * @param map The map to compare.
     * @return true if the given map has epsilon-equal keys to this map's keys and the associated values are equal.
     */
    boolean epsilonEquals(Map<? extends K, ? extends V> map);
}

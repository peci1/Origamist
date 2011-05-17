/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * A hash map that provides forced methods for keys with changed hashcode.
 * 
 * @author Martin Pecka
 */
public class MutableHashMap<K, V> extends HashMap<K, V>
{

    /** */
    private static final long serialVersionUID = -9056138260032600845L;

    /**
     * Return the value for the given key. If the default hash map implementation of get() doesn't find an entry, go
     * through the whole map and try to find a key that is equal to the given one without checking its hashcode.
     * 
     * @param key The key to get value for.
     * @return The value.
     */
    public V forcedGet(Object key)
    {
        V val = super.get(key);
        if (val == null) {
            for (Map.Entry<K, V> e : entrySet()) {
                if (e.getKey() == key || e.getKey().equals(key))
                    return e.getValue();
            }
            return null;
        }
        return val;
    }

    /**
     * Return true if the given key is mapped to a value. If the default hash map implementation of containsKey()
     * doesn't find an entry, go through the whole map and try to find a key that is equal to the given one without
     * checking its hashcode.
     * 
     * @param key The key to find.
     * @return If the map contains a mapping for the given key.
     */
    public boolean forcedContainsKey(Object key)
    {
        boolean val = super.containsKey(key);
        if (!val) {
            for (Map.Entry<K, V> e : entrySet()) {
                if (e.getKey() == key || e.getKey().equals(key))
                    return true;
            }
            return false;
        }
        return val;
    }

    /**
     * Remove the entry for the given key and return the value. If the default hash map implementation of remove()
     * doesn't find an entry, go through the whole map and try to find a key that is equal to the given one without
     * checking its hashcode.
     * 
     * @param key The key to be removed.
     * @return The removed value.
     */
    public V forcedRemove(Object key)
    {
        V val = super.remove(key);
        if (val == null) {
            for (Iterator<Map.Entry<K, V>> it = entrySet().iterator(); it.hasNext();) {
                Map.Entry<K, V> e = it.next();
                if (e.getKey() == key || e.getKey().equals(key)) {
                    it.remove();
                    return e.getValue();
                }
            }
            return null;
        }
        return val;
    }

}

/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.utils;

import cz.cuni.mff.peckam.java.origamist.math.Vector;

/**
 * 
 * 
 * @author Martin Pecka
 */
public class MultiDimIntervalTree<K extends Comparable<? super K>, V> extends
        IntervalTree<K, MultiDimIntervalTree<K, V>.ValueOrTree>
{

    /** */
    private static final long serialVersionUID = 5640892912045611L;

    protected int             dimension;

    protected int             size             = 0;

    /**
     * @param dimension
     */
    public MultiDimIntervalTree(int dimension)
    {
        this.dimension = dimension;
    }

    /**
     * @return the dimension
     */
    public int getDimension()
    {
        return dimension;
    }

    public V put(Vector<Interval<K>> key, V value)
    {
        if (this.dimension == 1) {
            Interval<K> interval = key.get(key.getDimension() - 1);
            IntervalEntry entry = getIntervalEntry(interval);
            if (entry == null) {
                put(interval, new ValueOrTree(value));
                size++;
                return null;
            } else {
                ValueOrTree old = entry.setValue(new ValueOrTree(value));
                return old == null ? null : old.value;
            }
        } else {
            Interval<K> interval = key.get(key.getDimension() - this.dimension);
            IntervalEntry entry = getIntervalEntry(interval);
            if (entry == null) {
                put(interval, new ValueOrTree());
                entry = getIntervalEntry(interval);
            }

            int oldSize = entry.value.tree.size();
            V result = entry.value.tree.put(key, value);
            if (oldSize != entry.value.tree.size())
                size++;
            return result;
        }
    }

    public V remove(Vector<Interval<K>> key)
    {
        if (this.dimension == 1) {
            IntervalEntry entry = getIntervalEntry(key.get(key.getDimension() - 1));
            ValueOrTree old = remove(entry.getKey());
            if (old != null)
                size--;
            return old == null ? null : old.value;
        } else {
            Interval<K> interval = key.get(key.getDimension() - this.dimension);
            IntervalEntry entry = getIntervalEntry(interval);
            if (entry == null)
                return null;

            int oldSize = entry.value.tree.size();
            V returnVal = entry.value.tree.remove(key);
            if (oldSize != entry.value.tree.size())
                size--;
            if (entry.value.tree.size() == 0)
                remove(entry.getKey());
            return returnVal;
        }
    }

    public V get(Vector<Interval<K>> key)
    {
        if (this.dimension == 1) {
            IntervalEntry entry = getIntervalEntry(key.get(key.getDimension() - 1));
            if (entry == null)
                return null;
            return entry.value.value;
        } else {
            IntervalEntry entry = getIntervalEntry(key.get(key.getDimension() - this.dimension));
            if (entry == null)
                return null;
            return entry.value.tree.get(key);
        }
    }

    public boolean contains(Vector<Interval<K>> key)
    {
        if (this.dimension == 1) {
            IntervalEntry entry = getIntervalEntry(key.get(key.getDimension() - 1));
            return entry != null;
        } else {
            IntervalEntry entry = getIntervalEntry(key.get(key.getDimension() - this.dimension));
            if (entry == null)
                return false;
            return entry.value.tree.contains(key);
        }
    }

    @Override
    public int size()
    {
        return this.size;
    }

    class ValueOrTree
    {
        V                          value = null;
        MultiDimIntervalTree<K, V> tree  = null;

        public ValueOrTree()
        {
            tree = new MultiDimIntervalTree<K, V>(dimension - 1);
        }

        public ValueOrTree(V value)
        {
            this.value = value;
        }

        @Override
        public String toString()
        {
            return "ValueOrTree [value=" + value + ", tree=" + tree + "]";
        }
    }

}

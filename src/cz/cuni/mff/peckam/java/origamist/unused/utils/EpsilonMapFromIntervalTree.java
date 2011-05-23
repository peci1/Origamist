/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.unused.utils;

import java.util.AbstractCollection;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import cz.cuni.mff.peckam.java.origamist.math.Vector;

/**
 * An epsilon-map backed by an {@link IntervalTree}.
 * 
 * @author Martin Pecka
 */
public abstract class EpsilonMapFromIntervalTree<K extends Comparable<? super K>, V> implements
        EpsilonMap<Vector<K>, V>
{

    /** */
    private static final long            serialVersionUID = -3579305443938365894L;

    /** The backing map. */
    protected MultiDimIntervalTree<K, V> map;

    public EpsilonMapFromIntervalTree(int dim)
    {
        map = new MultiDimIntervalTree<K, V>(dim);
    }

    /**
     * Return the epsilon-interval for the given key.
     * 
     * @param key The key to return the interval for.
     * @return The epsilon-interval for the given key.
     */
    protected abstract EpsilonInterval getEpsilonInterval(Vector<K> key);

    /**
     * @return The comparator that can compare keys.
     */
    protected abstract Comparator<? super K> getKeyComparator();

    /**
     * Return the value that represents the center of the given epsilon-interval of values.
     * 
     * @param epsilonInterval The interval to get the center of.
     * @return The value that represents the center of the given epsilon-interval of values.
     */
    protected Vector<K> getCenter(EpsilonInterval epsilonInterval)
    {
        return epsilonInterval.getCenter();
    }

    protected abstract K getCenter(Interval<K> epsilonInterval);

    @Override
    public V epsilonGet(Vector<K> key)
    {
        return map.get(getEpsilonInterval(key));
    }

    @Override
    public V epsilonPut(Vector<K> key, V value)
    {
        return map.put(getEpsilonInterval(key), value);
    }

    @Override
    public V epsilonRemove(Vector<K> key)
    {
        return map.remove(getEpsilonInterval(key));
    }

    @Override
    public boolean epsilonContainsKey(Vector<K> key)
    {
        return map.contains(getEpsilonInterval(key));
    }

    @Override
    public void epsilonPutAll(Map<? extends Vector<K>, ? extends V> map)
    {
        for (Entry<? extends Vector<K>, ? extends V> e : map.entrySet()) {
            epsilonPut(e.getKey(), e.getValue());
        }
    }

    @Override
    public boolean epsilonEquals(Map<? extends Vector<K>, ? extends V> map)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public int size()
    {
        return this.map.size();
    }

    @Override
    public boolean isEmpty()
    {
        return this.map.isEmpty();
    }

    /**
     * Not implemented.
     */
    @Override
    public boolean containsKey(Object key)
    {
        throw new UnsupportedOperationException();
    }

    /**
     * Not implemented.
     */
    @Override
    public boolean containsValue(Object value)
    {
        throw new UnsupportedOperationException();
    }

    /**
     * Not implemented.
     */
    @Override
    public V get(Object key)
    {
        throw new UnsupportedOperationException();
    }

    /**
     * Not implemented.
     */
    @Override
    public V put(Vector<K> key, V value)
    {
        throw new UnsupportedOperationException();
    }

    /**
     * Not implemented.
     */
    @Override
    public V remove(Object key)
    {
        throw new UnsupportedOperationException();
    }

    /**
     * Not implemented.
     */
    @Override
    public void putAll(Map<? extends Vector<K>, ? extends V> m)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear()
    {
        this.map.clear();
    }

    @Override
    public Set<Vector<K>> keySet()
    {
        return new AbstractSet<Vector<K>>() {
            @Override
            public Iterator<Vector<K>> iterator()
            {
                return new Iterator<Vector<K>>() {
                    protected Iterator<Map.Entry<Vector<K>, V>> entryIt = EpsilonMapFromIntervalTree.this.entrySet()
                                                                                .iterator();

                    @Override
                    public boolean hasNext()
                    {
                        return entryIt.hasNext();
                    }

                    @Override
                    public Vector<K> next()
                    {
                        return entryIt.next().getKey();
                    }

                    @Override
                    public void remove()
                    {
                        entryIt.remove();
                    }
                };
            }

            @Override
            public int size()
            {
                return EpsilonMapFromIntervalTree.this.size();
            }
        };
    }

    @Override
    public Collection<V> values()
    {
        return new AbstractCollection<V>() {

            @Override
            public Iterator<V> iterator()
            {
                return new Iterator<V>() {
                    protected Iterator<Map.Entry<Vector<K>, V>> entryIt = EpsilonMapFromIntervalTree.this.entrySet()
                                                                                .iterator();

                    @Override
                    public boolean hasNext()
                    {
                        return entryIt.hasNext();
                    }

                    @Override
                    public V next()
                    {
                        return entryIt.next().getValue();
                    }

                    @Override
                    public void remove()
                    {
                        entryIt.remove();
                    }
                };
            }

            @Override
            public int size()
            {
                return EpsilonMapFromIntervalTree.this.size();
            }
        };
    }

    @Override
    public Set<Map.Entry<Vector<K>, V>> entrySet()
    {
        return new AbstractSet<Map.Entry<Vector<K>, V>>() {
            @Override
            public Iterator<Map.Entry<Vector<K>, V>> iterator()
            {
                return new Iterator<Map.Entry<Vector<K>, V>>() {

                    protected List<Iterator<Map.Entry<Interval<K>, MultiDimIntervalTree<K, V>.ValueOrTree>>> iters = null;
                    protected List<K>                                                                        key   = new ArrayList<K>(
                                                                                                                           map.getDimension());
                    protected int                                                                            i     = 0;

                    {
                        for (int i = 0; i < map.getDimension(); i++)
                            key.add(null);
                    }

                    @Override
                    public boolean hasNext()
                    {
                        if (iters == null)
                            return map.size() != 0;
                        for (int i = 0; i < iters.size(); i++) {
                            if (iters.get(i).hasNext())
                                return true;
                        }
                        return false;
                    }

                    @Override
                    public Map.Entry<Vector<K>, V> next()
                    {
                        i++;

                        if (iters == null) {
                            iters = new ArrayList<Iterator<Entry<Interval<K>, MultiDimIntervalTree<K, V>.ValueOrTree>>>(
                                    map.getDimension());
                            iters.add(map.entrySet().iterator());
                            Entry<Interval<K>, MultiDimIntervalTree<K, V>.ValueOrTree> e = null;
                            for (int i = 1; i < map.getDimension(); i++) {
                                e = iters.get(i - 1).next();
                                key.set(i - 1, getCenter(e.getKey()));
                                iters.add(e.getValue().tree.entrySet().iterator());
                            }
                            e = iters.get(map.getDimension() - 1).next();
                            key.set(map.getDimension() - 1, getCenter(e.getKey()));
                            return new ExportEntry(new InnerVector(key), e);
                        }

                        for (int i = iters.size() - 1; i >= 0; i--) {
                            if (iters.get(i).hasNext()) {
                                Entry<Interval<K>, MultiDimIntervalTree<K, V>.ValueOrTree> e = iters.get(i).next();
                                key.set(i, getCenter(e.getKey()));
                                if (i < iters.size() - 1) {
                                    Entry<Interval<K>, MultiDimIntervalTree<K, V>.ValueOrTree> en = null;
                                    iters.set(i + 1, e.getValue().tree.entrySet().iterator());
                                    for (int j = i + 1; j < iters.size(); j++) {
                                        en = iters.get(j).next();
                                        key.set(j, getCenter(en.getKey()));
                                        if (j < iters.size() - 1) {
                                            iters.set(j + 1, en.getValue().tree.entrySet().iterator());
                                        }
                                    }
                                    return new ExportEntry(new InnerVector(key), en);
                                }
                                return new ExportEntry(new InnerVector(key), e);
                            }
                        }
                        throw new NoSuchElementException();
                    }

                    @Override
                    public void remove()
                    {
                        throw new UnsupportedOperationException();
                    }

                    class ExportEntry implements Map.Entry<Vector<K>, V>
                    {
                        Vector<K>                                                  key;
                        Entry<Interval<K>, MultiDimIntervalTree<K, V>.ValueOrTree> origEntry;

                        public ExportEntry(Vector<K> key,
                                Entry<Interval<K>, MultiDimIntervalTree<K, V>.ValueOrTree> origEntry)
                        {
                            this.key = key;
                            this.origEntry = origEntry;
                        }

                        @Override
                        public Vector<K> getKey()
                        {
                            return key;
                        }

                        @Override
                        public V getValue()
                        {
                            return origEntry.getValue().value;
                        }

                        @Override
                        public V setValue(V value)
                        {
                            return origEntry.setValue(map.new ValueOrTree(value)).value;
                        }

                    }

                    class InnerVector implements Vector<K>
                    {
                        /** */
                        private static final long serialVersionUID = 1L;

                        K[]                       values;

                        @SuppressWarnings("unchecked")
                        public InnerVector(List<K> values)
                        {
                            this.values = values.toArray(emptyArray());
                        }

                        private K[] emptyArray(K... vals)
                        {
                            return vals;
                        }

                        @Override
                        public Iterator<K> iterator()
                        {
                            return new Iterator<K>() {
                                protected int i = 0;

                                @Override
                                public boolean hasNext()
                                {
                                    return i + 1 < map.getDimension();
                                }

                                @Override
                                public K next()
                                {
                                    return values[i++];
                                }

                                @Override
                                public void remove()
                                {
                                    throw new UnsupportedOperationException();
                                }
                            };
                        }

                        @Override
                        public int getDimension()
                        {
                            return map.getDimension();
                        }

                        @Override
                        public K get(int index)
                        {
                            return values[index];
                        }

                        @Override
                        public K set(int index, K value)
                        {
                            K oldVal = values[index];
                            values[index] = value;
                            return oldVal;
                        }

                        @Override
                        public String toString()
                        {
                            return "InnerVector [" + Arrays.toString(values) + "]";
                        }

                        @Override
                        public int hashCode()
                        {
                            return Arrays.hashCode(values);
                        }

                        @SuppressWarnings("unchecked")
                        @Override
                        public boolean equals(Object obj)
                        {
                            Vector<K> vec = null;
                            try {
                                vec = (Vector<K>) obj;
                            } catch (ClassCastException e) {
                                return false;
                            }

                            if (vec.getDimension() != this.getDimension())
                                return false;

                            for (int i = 0; i < this.getDimension(); i++) {
                                if ((this.get(i) == null) != (vec.get(i) == null)
                                        || (this.get(i) != null && !this.get(i).equals(vec.get(i))))
                                    return false;
                            }

                            return true;
                        }

                    }
                };

            }

            @Override
            public int size()
            {
                return EpsilonMapFromIntervalTree.this.size();
            }
        };
    }

    protected class EpsilonInterval implements Vector<Interval<K>>
    {

        /** */
        private static final long serialVersionUID = 3337664415256021420L;

        protected Vector<K>       min;
        protected Vector<K>       max;
        protected Vector<K>       center;

        public EpsilonInterval(Vector<K> min, Vector<K> max, Vector<K> center)
        {
            this.min = min;
            this.max = max;
            this.center = center;
        }

        /**
         * @return The center of the interval.
         */
        public Vector<K> getCenter()
        {
            return center;
        }

        @Override
        public Iterator<Interval<K>> iterator()
        {
            return new Iterator<Interval<K>>() {
                protected int i = 0;

                @Override
                public boolean hasNext()
                {
                    return i + 1 < getDimension();
                }

                @Override
                public Interval<K> next()
                {
                    return get(i++);
                }

                @Override
                public void remove()
                {
                    throw new UnsupportedOperationException();
                }
            };
        }

        @Override
        public int getDimension()
        {
            return center.getDimension();
        }

        @Override
        public Interval<K> get(int index)
        {
            return new InnerInterval(index);
        }

        @Override
        public Interval<K> set(int index, Interval<K> value)
        {
            Interval<K> oldVal = get(index);
            min.set(index, value.getMin());
            max.set(index, value.getMax());
            return oldVal;
        }

        protected class InnerInterval implements Interval<K>
        {
            protected int index;

            public InnerInterval(int index)
            {
                this.index = index;
            }

            @Override
            public K getMin()
            {
                return min.get(index);
            }

            @Override
            public K getMax()
            {
                return max.get(index);
            }

            @Override
            public boolean overlapsWith(Interval<K> other)
            {
                return (getMin().compareTo(other.getMin()) >= 0 && getMin().compareTo(other.getMax()) <= 0)
                        || (getMax().compareTo(other.getMin()) >= 0 && getMax().compareTo(other.getMax()) <= 0)
                        || (other.getMin().compareTo(getMin()) >= 0 && other.getMin().compareTo(getMax()) <= 0)
                        || (other.getMax().compareTo(getMin()) >= 0 && other.getMax().compareTo(getMax()) <= 0);
            }

            @Override
            public String toString()
            {
                return "InnerInterval [index=" + index + ", min=" + getMin() + ", max=" + getMax() + "]";
            }

        }

        @Override
        public int hashCode()
        {
            final int prime = 31;
            int result = 1;
            result = prime * result + getOuterType().hashCode();
            result = prime * result + ((center == null) ? 0 : center.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj)
        {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            @SuppressWarnings("unchecked")
            EpsilonInterval other = (EpsilonInterval) obj;
            if (!getOuterType().equals(other.getOuterType()))
                return false;
            if (center == null) {
                if (other.center != null)
                    return false;
            } else if (!center.equals(other.center))
                return false;
            return true;
        }

        private EpsilonMapFromIntervalTree<?, ?> getOuterType()
        {
            return EpsilonMapFromIntervalTree.this;
        }

        @Override
        public String toString()
        {
            return "EpsilonInterval [min=" + min + ", max=" + max + ", center=" + center + "]";
        }
    }
}

/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.utils;

import java.util.Comparator;
import java.util.Map;
import java.util.SortedMap;

/**
 * A red-black tree supporting searching for epsilon-equal values.
 * 
 * @author Martin Pecka
 */
public class EpsilonRedBlackTree<K, V> extends RedBlackTree<K, V> implements EpsilonMap<K, V>
{
    /** */
    private static final long       serialVersionUID = -5978298665466857457L;

    /** The comparator to be used for searching epsilon-equal keys. */
    protected Comparator<? super K> epsilonComparator;

    /**
     * Construct a new EpsilonRedBlackTree with the default comparator. The default comparator needs all values inserted
     * into or deleted from the tree to implement {@link Comparable}&lt;K&gt;. If an element doesn't implement it, an
     * insert or delete method will end up throwing a {@link ClassCastException}. Also a default epsilon-comparator is
     * created using the given epsilon and K must implement {@link Number}.
     * 
     * @param epsilon The epsilon used for defining epsilon-equal keys.
     */
    public EpsilonRedBlackTree(K epsilon)
    {
        super();
        epsilonComparator = getDefaultEpsilonComparator(epsilon);
    }

    /**
     * Construct a new EpsilonRedBlackTree using the given comparator. The comparator must be able to compare every two
     * "valid" keys, and must throw a {@link ClassCastException} if an "invalid" key is to be compared. Also a default
     * epsilon-comparator is created using the given epsilon and K must implement {@link Number}.
     * 
     * @param comparator The comparator to use for comparing this tree's element's keys.
     * @param epsilon The epsilon used for defining epsilon-equal keys.
     */
    public EpsilonRedBlackTree(Comparator<? super K> comparator, K epsilon)
    {
        super(comparator);
        epsilonComparator = getDefaultEpsilonComparator(epsilon);
    }

    /**
     * Create a new EpsilonRedBlackTree with entries from the given map. Use the default comparator to compare the keys.
     * The keys must implement {@link Comparable}&lt;K&gt;. Also a default epsilon-comparator is created using the given
     * epsilon and K must implement {@link Number}.
     * 
     * @param m The map to take entries from.
     * @param epsilon The epsilon used for defining epsilon-equal keys.
     * 
     * @throws ClassCastException If a key from the given map doesn't implement the {@link Comparable}&lt;K&gt;
     *             interface.
     * @throws NullPointerException If <code>m == null</code> or if the map contains a <code>null</code> key and the
     *             comparator doesn't support it.
     */
    public EpsilonRedBlackTree(Map<? extends K, ? extends V> m, K epsilon)
    {
        super(m);
        epsilonComparator = getDefaultEpsilonComparator(epsilon);
    }

    /**
     * Create a new EpsilonRedBlackTree with entries from the given sorted map. The map's comparator is used. This is an
     * effective (linear time) algorithm. Also a default epsilon-comparator is created using the given epsilon and K
     * must implement {@link Number}.
     * 
     * @param m The sorted map to take entries from.
     * @param epsilon The epsilon used for defining epsilon-equal keys.
     */
    public EpsilonRedBlackTree(SortedMap<K, ? extends V> m, K epsilon)
    {
        super(m);
        epsilonComparator = getDefaultEpsilonComparator(epsilon);
    }

    /**
     * Construct a new EpsilonRedBlackTree with the default comparator. The default comparator needs all values inserted
     * into or deleted from the tree to implement {@link Comparable}&lt;K&gt;. If an element doesn't implement it, an
     * insert or delete method will end up throwing a {@link ClassCastException}.
     * 
     * @param epsilonComparator The comparator of epsilon-equal keys.
     */
    public EpsilonRedBlackTree(Comparator<? super K> epsilonComparator)
    {
        super();
        this.epsilonComparator = epsilonComparator;
    }

    /**
     * Construct a new EpsilonRedBlackTree using the given comparator. The comparator must be able to compare every two
     * "valid" keys, and must throw a {@link ClassCastException} if an "invalid" key is to be compared.
     * 
     * @param comparator The comparator to use for comparing this tree's element's keys.
     * @param epsilonComparator The comparator of epsilon-equal keys.
     */
    public EpsilonRedBlackTree(Comparator<? super K> comparator, Comparator<? super K> epsilonComparator)
    {
        super(comparator);
        this.epsilonComparator = epsilonComparator;
    }

    /**
     * Create a new EpsilonRedBlackTree with entries from the given map. Use the default comparator to compare the keys.
     * The keys must implement {@link Comparable}&lt;K&gt;.
     * 
     * @param m The map to take entries from.
     * @param epsilonComparator The comparator of epsilon-equal keys.
     * 
     * @throws ClassCastException If a key from the given map doesn't implement the {@link Comparable}&lt;K&gt;
     *             interface.
     * @throws NullPointerException If <code>m == null</code> or if the map contains a <code>null</code> key and the
     *             comparator doesn't support it.
     */
    public EpsilonRedBlackTree(Map<? extends K, ? extends V> m, Comparator<? super K> epsilonComparator)
    {
        super(m);
        this.epsilonComparator = epsilonComparator;
    }

    /**
     * Create a new EpsilonRedBlackTree with entries from the given sorted map. The map's comparator is used. This is an
     * effective (linear time) algorithm.
     * 
     * @param m The sorted map to take entries from.
     * @param epsilonComparator The comparator of epsilon-equal keys.
     */
    public EpsilonRedBlackTree(SortedMap<K, ? extends V> m, Comparator<? super K> epsilonComparator)
    {
        super(m);
        this.epsilonComparator = epsilonComparator;
    }

    @Override
    public V epsilonGet(K key)
    {
        TreePath path = epsilonGetPath(key);
        if (path.size() > 0 && epsilonEquals(path.getLast().getKey(), key))
            return path.getLast().getValue();
        return null;
    }

    @Override
    public V epsilonPut(K key, V value)
    {
        if (root == null) {
            return super.put(key, value);
        }

        TreePath path = epsilonGetPath(key);
        int cmp = epsilonComparator.compare(key, path.getLast().getKey());
        if (cmp == 0)
            return path.getLast().setValue(value);
        // now we have the new entry's parent as the last item on the path
        RedBlackTree<K, V>.Entry e = ((RedBlackTree<K, V>) this).new Entry(key, value);
        if (cmp < 0)
            path.getLast().left = e;
        else
            path.getLast().right = e;

        path.addLast(e);

        repairTreeAfterInsert(path);

        size++;
        modCount++;

        return null;
    }

    @Override
    public V epsilonRemove(K key)
    {
        TreePath path = epsilonGetPath(key);
        if (!epsilonEquals(path.getLast().getKey(), key))
            return null;

        V oldValue = path.getLast().value;
        deleteLastPathEntry(path);
        return oldValue;
    }

    /**
     * Get the path to an entry with epsilon-equal key.
     * 
     * @param key The key to find path for.
     * @return The path to an entry with epsilon-equal key. If no epsilon-equal key is found, the path to the point
     *         where the key should be inserted is returned.
     */
    protected TreePath epsilonGetPath(K key)
    {
        TreePath path = new TreePath();
        RedBlackTree<K, V>.Entry e = this.root;
        while (e != null) {
            path.addLast(e);
            int cmp = epsilonComparator.compare(key, e.key);
            if (cmp < 0)
                e = e.left;
            else if (cmp > 0)
                e = e.right;
            else
                break;
        }

        return path;
    }

    /**
     * Return true if the given keys are epsilon-equal.
     * 
     * @param k1 The first key to compare.
     * @param k2 The second key to compare.
     * @return true if the given keys are epsilon-equal.
     */
    protected boolean epsilonEquals(K k1, K k2)
    {
        return epsilonComparator.compare(k1, k2) == 0;
    }

    @Override
    public boolean epsilonContainsKey(K key)
    {
        TreePath path = epsilonGetPath(key);
        return path.size() > 0 && epsilonEquals(path.getLast().getKey(), key);
    }

    @Override
    public void epsilonPutAll(Map<? extends K, ? extends V> map)
    {
        if (size() == 0) {
            super.putAll(map);
        } else {
            for (Map.Entry<? extends K, ? extends V> e : map.entrySet()) {
                put(e.getKey(), e.getValue());
            }
        }
    }

    @Override
    public boolean epsilonEquals(Map<? extends K, ? extends V> map)
    {
        if (size() != map.size())
            return false;

        if (size() == 0)
            return true;

        for (Map.Entry<? extends K, ? extends V> e : map.entrySet()) {
            TreePath path = epsilonGetPath(e.getKey());
            if (path.size() == 0)
                return false;
            if (!epsilonEquals(path.getLast().getKey(), e.getKey()))
                return false;
            if (!valEquals(path.getLast().getValue(), e.getValue()))
                return false;
        }

        return true;
    }

    /**
     * A default comparator for epsilon-comparison. K must implement {@link Number}.
     * 
     * @param epsilon The epsilon to use for comparison.
     * @return A default comparator for epsilon-comparison. K must implement {@link Number}.
     */
    protected Comparator<? super K> getDefaultEpsilonComparator(final K epsilon)
    {
        return new Comparator<K>() {
            protected double eps = ((Number) epsilon).doubleValue();

            @Override
            public int compare(K o1, K o2)
            {
                if (o1 == null) {
                    if (o2 == null)
                        return 0;
                    else
                        return -1;
                }

                if (o2 == null)
                    return 1;
                Number o1n = (Number) o1;
                Number o2n = (Number) o2;

                Double diff = o1n.doubleValue() - o2n.doubleValue();

                if (diff.compareTo(eps) > 0)
                    return 1;
                else if (diff.compareTo(-eps) < 0)
                    return -1;
                else
                    return 0;
            }

        };
    }

}

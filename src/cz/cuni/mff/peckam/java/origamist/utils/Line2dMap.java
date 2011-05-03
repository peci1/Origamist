/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.utils;

import static cz.cuni.mff.peckam.java.origamist.math.MathHelper.EPSILON;

import java.util.Map;
import java.util.SortedMap;

import javax.vecmath.Tuple2d;

import cz.cuni.mff.peckam.java.origamist.math.CanonicLine2d;
import cz.cuni.mff.peckam.java.origamist.math.Line2d;

/**
 * A map that has lines as keys, and can be accessed in epsilon-equals way.
 * 
 * @author Martin Pecka
 */
public class Line2dMap<V> extends EpsilonRedBlackTree<CanonicLine2d, V, Double>
{
    /** */
    private static final long serialVersionUID = 1989427767196076752L;

    /**
     * Create a new map with lines as keys.
     */
    public Line2dMap()
    {
        super(getEpsilonComparator(0d), getEpsilonComparator(EPSILON));
    }

    /**
     * Create a new Line3dMap with entries from the given map. Use the default comparator to compare the keys. The keys
     * must implement {@link Comparable}&lt;K&gt;.
     * 
     * @param m The map to take entries from.
     * 
     * @throws ClassCastException If a key from the given map doesn't implement the {@link Comparable}&lt;K&gt;
     *             interface.
     * @throws NullPointerException If <code>m == null</code> or if the map contains a <code>null</code> key and the
     *             comparator doesn't support it.
     */
    public Line2dMap(Map<? extends CanonicLine2d, ? extends V> m)
    {
        super(m, getEpsilonComparator(EPSILON));
    }

    /**
     * Create a new Line3dMap with entries from the given sorted map. The map's comparator is used. This is an effective
     * (linear time) algorithm.
     * 
     * @param m The sorted map to take entries from.
     */
    public Line2dMap(SortedMap<CanonicLine2d, ? extends V> m)
    {
        super(m, getEpsilonComparator(EPSILON));
    }

    /**
     * Return a comparator comparing two epsilon-equal values as equal.
     * 
     * @param epsilon The epsilon to use.
     * @return A comparator comparing two epsilon-equal values as equal.
     */
    protected static EpsilonComparator<? super CanonicLine2d, Double> getEpsilonComparator(final double epsilon)
    {
        return new EpsilonComparator<CanonicLine2d, Double>() {
            @Override
            public int compare(CanonicLine2d o1, CanonicLine2d o2)
            {
                return compare(o1, o2, epsilon);
            }

            @Override
            public int compare(CanonicLine2d o1, CanonicLine2d o2, Double eps)
            {
                int cmp1 = compare(o1.getVector(), o2.getVector(), eps);
                if (cmp1 != 0)
                    return cmp1;
                return compare(o1.getPoint(), o2.getPoint(), eps);
            }

            protected int compare(Tuple2d t1, Tuple2d t2, Double eps)
            {
                int cmp1 = compare(t1.x, t2.x, eps);
                if (cmp1 != 0)
                    return cmp1;
                return compare(t1.y, t2.y, eps);
            }

            protected int compare(Double d1, Double d2, Double eps)
            {
                double diff = d1 - d2;
                if (diff < -eps)
                    return -1;
                else if (diff > eps)
                    return 1;
                else
                    return 0;
            }
        };
    }

    /**
     * If the given line is not canonic, canonicalize it. Otherwise works the same way as
     * {@link EpsilonMap#epsilonGet(Object)} does.
     * 
     * @see EpsilonMap#epsilonGet(Object)
     */
    public V epsilonGet(Line2d key)
    {
        return epsilonGet(new CanonicLine2d(key));
    }

    /**
     * @see EpsilonRedBlackTree#epsilonGet(Object, boolean)
     */
    public V epsilonGet(Line2d key, boolean surelyContains)
    {
        return super.epsilonGet(new CanonicLine2d(key), surelyContains);
    }

    /**
     * If the given line is not canonic, canonicalize it. Otherwise works the same way as
     * {@link EpsilonMap#epsilonPut(Object, Object)} does.
     * 
     * @see EpsilonMap#epsilonPut(Object, Object)
     */
    public V epsilonPut(Line2d key, V value)
    {
        return super.epsilonPut(new CanonicLine2d(key), value);
    }

    /**
     * If the given line is not canonic, canonicalize it. Otherwise works the same way as
     * {@link EpsilonMap#epsilonRemove(Object)} does.
     * 
     * @see EpsilonMap#epsilonRemove(Object)
     */
    public V epsilonRemove(Line2d key)
    {
        return super.epsilonRemove(new CanonicLine2d(key));
    }

    /**
     * @see EpsilonRedBlackTree#epsilonRemove(Object, boolean)
     */
    public V epsilonRemove(Line2d key, boolean surelyContains)
    {
        return super.epsilonRemove(new CanonicLine2d(key), surelyContains);
    }

    /**
     * If the given line is not canonic, canonicalize it. Otherwise works the same way as
     * {@link EpsilonMap#epsilonContainsKey(Object)} does.
     * 
     * @see EpsilonMap#epsilonContainsKey(Object)
     */
    public boolean epsilonContainsKey(Line2d key)
    {
        return super.epsilonContainsKey(new CanonicLine2d(key));
    }

    @Override
    protected RedBlackTree<CanonicLine2d, V>.TreePath epsilonGetPathSequentially(CanonicLine2d key)
    {
        if (root == null)
            return new TreePath();

        TreePath path = epsilonGetPath(firstKey());

        while (path.size() > 0) {
            if (epsilonComparator.compare(path.getLast().key, key, 2 * EPSILON) == 0)
                return path;
            path.moveToSuccesor();
        }

        return path;
    }
}

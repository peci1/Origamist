/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.utils;

import static cz.cuni.mff.peckam.java.origamist.math.MathHelper.EPSILON;

import java.util.Comparator;
import java.util.Map;
import java.util.SortedMap;

import javax.vecmath.Tuple3d;

import cz.cuni.mff.peckam.java.origamist.math.CanonicLine3d;
import cz.cuni.mff.peckam.java.origamist.math.Line3d;

/**
 * A map that has lines as keys, and can be accessed in epsilon-equals way.
 * 
 * @author Martin Pecka
 */
public class Line3dMap<V> extends EpsilonRedBlackTree<CanonicLine3d, V>
{
    /** */
    private static final long serialVersionUID = 1989427767196076752L;

    /**
     * Create a new map with lines as keys.
     */
    public Line3dMap()
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
    public Line3dMap(Map<? extends CanonicLine3d, ? extends V> m)
    {
        super(m, getEpsilonComparator(EPSILON));
    }

    /**
     * Create a new Line3dMap with entries from the given sorted map. The map's comparator is used. This is an effective
     * (linear time) algorithm.
     * 
     * @param m The sorted map to take entries from.
     */
    public Line3dMap(SortedMap<CanonicLine3d, ? extends V> m)
    {
        super(m, getEpsilonComparator(EPSILON));
    }

    /**
     * Return a comparator comparing two epsilon-equal values as equal.
     * 
     * @param epsilon The epsilon to use.
     * @return A comparator comparing two epsilon-equal values as equal.
     */
    protected static Comparator<? super CanonicLine3d> getEpsilonComparator(final double epsilon)
    {
        return new Comparator<CanonicLine3d>() {
            @Override
            public int compare(CanonicLine3d o1, CanonicLine3d o2)
            {
                int cmp1 = compare(o1.getVector(), o2.getVector());
                if (cmp1 != 0)
                    return cmp1;
                return compare(o1.getPoint(), o2.getPoint());
            }

            protected int compare(Tuple3d t1, Tuple3d t2)
            {
                int cmp1 = compare(t1.x, t2.x);
                if (cmp1 != 0)
                    return cmp1;
                int cmp2 = compare(t1.y, t2.y);
                if (cmp2 != 0)
                    return cmp2;
                return compare(t1.z, t2.z);
            }

            protected int compare(Double d1, Double d2)
            {
                double diff = d1 - d2;
                if (diff < -epsilon)
                    return -1;
                else if (diff > epsilon)
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
    public V epsilonGet(Line3d key)
    {
        return epsilonGet(new CanonicLine3d(key));
    }

    /**
     * @see EpsilonRedBlackTree#epsilonGet(Object, boolean)
     */
    public V epsilonGet(Line3d key, boolean surelyContains)
    {
        return super.epsilonGet(new CanonicLine3d(key), surelyContains);
    }

    /**
     * If the given line is not canonic, canonicalize it. Otherwise works the same way as
     * {@link EpsilonMap#epsilonPut(Object, Object)} does.
     * 
     * @see EpsilonMap#epsilonPut(Object, Object)
     */
    public V epsilonPut(Line3d key, V value)
    {
        return super.epsilonPut(new CanonicLine3d(key), value);
    }

    /**
     * If the given line is not canonic, canonicalize it. Otherwise works the same way as
     * {@link EpsilonMap#epsilonRemove(Object)} does.
     * 
     * @see EpsilonMap#epsilonRemove(Object)
     */
    public V epsilonRemove(Line3d key)
    {
        return super.epsilonRemove(new CanonicLine3d(key));
    }

    /**
     * @see EpsilonRedBlackTree#epsilonRemove(Object, boolean)
     */
    public V epsilonRemove(Line3d key, boolean surelyContains)
    {
        return super.epsilonRemove(new CanonicLine3d(key), surelyContains);
    }

    /**
     * If the given line is not canonic, canonicalize it. Otherwise works the same way as
     * {@link EpsilonMap#epsilonContainsKey(Object)} does.
     * 
     * @see EpsilonMap#epsilonContainsKey(Object)
     */
    public boolean epsilonContainsKey(Line3d key)
    {
        return super.epsilonContainsKey(new CanonicLine3d(key));
    }

}

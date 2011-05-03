/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.utils;

import static cz.cuni.mff.peckam.java.origamist.math.MathHelper.EPSILON;

import java.util.Comparator;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import cz.cuni.mff.peckam.java.origamist.math.CanonicLine3d;
import cz.cuni.mff.peckam.java.origamist.math.Line3d;
import cz.cuni.mff.peckam.java.origamist.math.Vector;

/**
 * An epsilon-map for the storage of 3D lines.
 * 
 * @author Martin Pecka
 */
public class Line3dEpsilonMap<V> extends EpsilonMapFromIntervalTree<Double, V>
{
    public Line3dEpsilonMap()
    {
        super(6);
    }

    @Override
    protected EpsilonInterval getEpsilonInterval(Vector<Double> key)
    {
        Line3d line = new CanonicLine3d(new Point3d(key.get(3), key.get(4), key.get(5)), new Vector3d(key.get(0),
                key.get(1), key.get(2)));
        Point3d p = line.getPoint();
        Vector3d v = line.getVector();

        return new EpsilonInterval(new Line3d(new Point3d(p.x - EPSILON, p.y - EPSILON, p.z - EPSILON), new Vector3d(
                v.x - EPSILON, v.y - EPSILON, v.z - EPSILON)), new Line3d(new Point3d(p.x + EPSILON, p.y + EPSILON, p.z
                + EPSILON), new Vector3d(v.x + EPSILON, v.y + EPSILON, v.z + EPSILON)), line);
    }

    private Comparator<? super Double> keyComparator = null;

    @Override
    protected Comparator<? super Double> getKeyComparator()
    {
        if (keyComparator == null) {
            keyComparator = new Comparator<Double>() {

                @Override
                public int compare(Double o1, Double o2)
                {
                    if (o1 == null)
                        return (o2 == null ? 0 : -1);
                    if (o2 == null)
                        return 1;
                    return o1.compareTo(o2);
                }
            };
        }
        return keyComparator;
    }

    @Override
    public V epsilonPut(Vector<Double> key, V value)
    {
        if (!(key instanceof CanonicLine3d))
            return super.epsilonPut(
                    new CanonicLine3d(new Point3d(key.get(3), key.get(4), key.get(5)), new Vector3d(key.get(0), key
                            .get(1), key.get(2))), value);

        return super.epsilonPut(key, value);
    }

    @Override
    protected Double getCenter(Interval<Double> epsilonInterval)
    {
        return epsilonInterval.getMin() + EPSILON;
    }
}

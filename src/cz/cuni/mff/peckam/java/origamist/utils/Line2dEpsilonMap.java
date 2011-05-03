/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.utils;

import static cz.cuni.mff.peckam.java.origamist.math.MathHelper.EPSILON;

import java.util.Comparator;

import javax.vecmath.Point2d;
import javax.vecmath.Vector2d;

import cz.cuni.mff.peckam.java.origamist.math.CanonicLine2d;
import cz.cuni.mff.peckam.java.origamist.math.Line2d;
import cz.cuni.mff.peckam.java.origamist.math.Vector;

/**
 * An epsilon-map for the storage of 2D lines.
 * 
 * @author Martin Pecka
 */
public class Line2dEpsilonMap<V> extends EpsilonMapFromIntervalTree<Double, V>
{
    public Line2dEpsilonMap()
    {
        super(4);
    }

    @Override
    protected EpsilonInterval getEpsilonInterval(Vector<Double> key)
    {
        Line2d line = new CanonicLine2d(new Point2d(key.get(2), key.get(3)), new Vector2d(key.get(0), key.get(1)));
        Point2d p = line.getPoint();
        Vector2d v = line.getVector();

        return new EpsilonInterval(new Line2d(new Point2d(p.x - EPSILON, p.y - EPSILON), new Vector2d(v.x - EPSILON,
                v.y - EPSILON)), new Line2d(new Point2d(p.x + EPSILON, p.y + EPSILON), new Vector2d(v.x + EPSILON, v.y
                + EPSILON)), line);
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
        if (!(key instanceof CanonicLine2d))
            return super
                    .epsilonPut(
                            new CanonicLine2d(new Point2d(key.get(2), key.get(3)), new Vector2d(key.get(0), key.get(1))),
                            value);

        return super.epsilonPut(key, value);
    }

    @Override
    protected Double getCenter(Interval<Double> epsilonInterval)
    {
        return epsilonInterval.getMin() + EPSILON;
    }
}

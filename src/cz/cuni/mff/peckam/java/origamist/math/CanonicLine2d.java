/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.math;

import static cz.cuni.mff.peckam.java.origamist.math.MathHelper.EPSILON;
import static java.lang.Math.abs;

import javax.vecmath.Point2d;
import javax.vecmath.Vector2d;

/**
 * A canonic representation of a line. The point and vector are such, that:
 * 
 * if (v == 0): p is unchanged, else:
 * 
 * p.y == 0 and v.y >= 0
 * if (v.y == 0): p.x == 0 and v.x >= 0 and p.y is unchanged
 * 
 * @author Martin Pecka
 */
public class CanonicLine2d extends Line2d
{
    /**
     * @param line
     */
    public CanonicLine2d(Line2d line)
    {
        this(line.p, line.v);
    }

    /**
     * @param p1
     * @param p2
     */
    public CanonicLine2d(Point2d p1, Point2d p2)
    {
        this(p1, new Vector2d(p2.x - p1.x, p2.y - p1.y));
    }

    /**
     * @param p
     * @param v
     */
    public CanonicLine2d(Point2d p, Vector2d v)
    {
        super(getBasePoint(p, v), getNormalized(v));
    }

    protected static Point2d getBasePoint(Point2d p, Vector2d v)
    {
        Point2d result = new Point2d();
        if (abs(v.y) > EPSILON) {
            result.y = 0d;
            double t = -p.y / v.y;
            result.x = p.x + t * v.x;
        } else {
            if (abs(v.x) > EPSILON) {
                result.x = 0d;
                result.y = p.y/* + t*v.y */; // v.y == 0
            } else {
                result = p;
            }
        }
        return result;
    }

    protected static Vector2d getNormalized(Vector2d v)
    {
        Vector2d result = new Vector2d(v);
        result.normalize();
        if (result.y < -EPSILON) {
            result.negate();
        } else if (result.y < EPSILON) {
            if (result.x < -EPSILON)
                result.negate();
        }
        return result;
    }
}

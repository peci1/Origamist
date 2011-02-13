/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.math;

import static cz.cuni.mff.peckam.java.origamist.math.MathHelper.EPSILON;
import static java.lang.Math.abs;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

/**
 * A canonic representation of a line. The point and vector are such, that:
 * 
 * if (v == 0): p is unchanged, else:
 * 
 * p.z == 0 and v.z >= 0
 * if (v.z == 0): p.y == 0 and v.y >= 0 and p.z is unchanged
 * if (v.y == 0): p.x == 0 and v.x >= 0 and p.z and p.y are unchanged
 * 
 * @author Martin Pecka
 */
public class CanonicLine3d extends Line3d
{

    /**
     * @param line
     */
    public CanonicLine3d(Line3d line)
    {
        this(line.p, line.v);
    }

    /**
     * @param p1
     * @param p2
     */
    public CanonicLine3d(Point3d p1, Point3d p2)
    {
        this(p1, new Vector3d(p2.x - p1.x, p2.y - p1.y, p2.z - p1.z));
    }

    /**
     * @param p
     * @param v
     */
    public CanonicLine3d(Point3d p, Vector3d v)
    {
        super(getBasePoint(p, v), getNormalized(v));
    }

    protected static Point3d getBasePoint(Point3d p, Vector3d v)
    {
        Point3d result = new Point3d();
        if (abs(v.z) > EPSILON) {
            result.z = 0d;
            double t = -p.z / v.z;
            result.x = p.x + t * v.x;
            result.y = p.y + t * v.y;
        } else {
            if (abs(v.y) > EPSILON) {
                result.y = 0d;
                double t = -p.y / v.y;
                result.x = p.x + t * v.x;
                result.z = p.z/* + t*v.z */; // v.z == 0
            } else {
                if (abs(v.x) > EPSILON) {
                    result.x = 0d;
                    result.y = p.y/* + t*v.y */; // v.y == 0
                    result.z = p.z/* + t*v.z */; // v.z == 0
                } else {
                    result = p;
                }
            }
        }
        return result;
    }

    protected static Vector3d getNormalized(Vector3d v)
    {
        Vector3d result = new Vector3d(v);
        result.normalize();
        if (result.z < -EPSILON) {
            result.negate();
        } else if (result.z < EPSILON) {
            if (result.y < -EPSILON) {
                result.negate();
            } else if (result.y < EPSILON) {
                if (result.x < -EPSILON)
                    result.negate();
            }
        }
        return result;
    }
}

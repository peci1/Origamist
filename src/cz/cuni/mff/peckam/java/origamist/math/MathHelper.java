/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.math;

import static java.lang.Math.abs;

import java.util.LinkedList;
import java.util.List;

import javax.vecmath.Matrix3d;
import javax.vecmath.Point3d;
import javax.vecmath.Quat4d;
import javax.vecmath.Vector3d;

/**
 * A helper class for math operations.
 * 
 * @author Martin Pecka
 */
public class MathHelper
{
    public static final double EPSILON = 0.000001;

    /**
     * Checks if v2 is a scalar multiple of v1. If it is, returns the scalar s such that v1 = s*v2.
     * 
     * @param v1 The vector to compare.
     * @param v2 The vector to compare.
     * @return The "quotient" ("v1/v2")
     */
    public static Double vectorQuotient3d(Vector3d v1, Vector3d v2)
    {
        List<Double> v1NonZero = new LinkedList<Double>();
        List<Double> v2NonZero = new LinkedList<Double>();
        v1NonZero.add((abs(v1.x) >= EPSILON) ? v1.x : null);
        v1NonZero.add((abs(v1.y) >= EPSILON) ? v1.y : null);
        v1NonZero.add((abs(v1.z) >= EPSILON) ? v1.z : null);
        v2NonZero.add((abs(v1.x) >= EPSILON) ? v2.x : null);
        v2NonZero.add((abs(v1.y) >= EPSILON) ? v2.y : null);
        v2NonZero.add((abs(v1.z) >= EPSILON) ? v2.z : null);

        if (v1NonZero.get(0) == null && v1NonZero.get(1) == null && v1NonZero.get(2) == null
                && v2NonZero.get(0) == null && v2NonZero.get(1) == null && v2NonZero.get(2) == null)
            return 1.0;

        Double result = null;

        for (int i = 0; i < 3; i++) {
            if ((v1NonZero.get(i) == null && v2NonZero.get(i) != null)
                    || (v1NonZero.get(i) != null && v2NonZero.get(i) == null)) {
                result = null;
                break;
            } else if (v1NonZero.get(i) == null && v2NonZero.get(i) == null) {
                continue;
            } else {
                double q = v1NonZero.get(i) / v2NonZero.get(i);
                if (result == null) {
                    result = q;
                } else {
                    if (abs(result - q) > EPSILON) {
                        result = null;
                        break;
                    } else {
                        continue;
                    }
                }
            }
        }

        return result;
    }

    /**
     * Rotates the given point around the given line by the given angle.
     * 
     * http://answers.google.com/answers/threadview/id/361441.html
     * 
     * @param p The point to rotate.
     * @param axis The line to rotate around.
     * @param angle The angle to rotate.
     * @return The rotated point.
     */
    public static Point3d rotate(Point3d p, Line3d axis, double angle)
    {
        double sinA2 = Math.sin(angle / 2);
        double cosA2 = Math.cos(angle / 2);
        Vector3d axisV = new Vector3d(axis.v);
        axisV.normalize();
        Quat4d q = new Quat4d(cosA2, sinA2 * axisV.x, sinA2 * axisV.y, sinA2 * axisV.z);
        Matrix3d m = new Matrix3d(q.x * q.x + q.y * q.y - q.z * q.z - q.w * q.w, 2 * (q.y * q.z - q.x * q.w), 2 * (q.y
                * q.w + q.x * q.z), 2 * (q.z * q.y + q.x * q.w), q.x * q.x - q.y * q.y + q.z * q.z - q.w * q.w,
                2 * (q.z * q.w - q.x * q.y), 2 * (q.w * q.y - q.x * q.z), 2 * (q.w * q.z + q.x * q.y), q.x * q.x - q.y
                        * q.y - q.z * q.z + q.w * q.w);
        Point3d pt = new Point3d(p);
        pt.sub(axis.p);
        Point3d pr = new Point3d(pt.x * m.m00 + pt.y * m.m01 + pt.z * m.m02,
                pt.x * m.m10 + pt.y * m.m11 + pt.z * m.m12, pt.x * m.m20 + pt.y * m.m21 + pt.z * m.m22);
        Point3d result = new Point3d(pr);
        result.add(axis.p);
        return result;
    }
}

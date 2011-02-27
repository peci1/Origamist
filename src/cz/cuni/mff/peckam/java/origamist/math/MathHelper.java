/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.math;

import static java.lang.Math.abs;

import java.util.List;

import javax.vecmath.Matrix3d;
import javax.vecmath.Point3d;
import javax.vecmath.Quat4d;
import javax.vecmath.Vector2d;
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
     * @return The "quotient" ("v1/v2") or <code>null</code> if the vectors aren't parallel or if v2 is a zero vector
     *         and v1 isn't.
     */
    public static Double vectorQuotient3d(Vector3d v1, Vector3d v2)
    {
        Vector3d zero = new Vector3d();
        boolean v1zero = v1.epsilonEquals(zero, EPSILON);
        boolean v2zero = v2.epsilonEquals(zero, EPSILON);
        if (v1zero || v2zero) {
            if (v1zero && v2zero) {
                return 1d;
            } else if (v1zero) {
                return 0d;
            } else {
                return null;
            }
        }

        Vector3d cross = new Vector3d();
        cross.cross(v1, v2);
        if (!cross.epsilonEquals(zero, EPSILON))
            return null; // the vectors aren't parallel

        if (abs(v2.x) > EPSILON)
            return v1.x / v2.x;

        if (abs(v2.y) > EPSILON)
            return v1.y / v2.y;

        if (abs(v2.z) > EPSILON)
            return v1.z / v2.z;

        assert false : "Vector v2 is not a zero vector, but none of its coordinates is diffrent from zero???";
        return null;
    }

    /**
     * Checks if v2 is a scalar multiple of v1. If it is, returns the scalar s such that v1 = s*v2.
     * 
     * @param v1 The vector to compare.
     * @param v2 The vector to compare.
     * @return The "quotient" ("v1/v2") or <code>null</code> if the vectors aren't parallel or if v2 is a zero vector
     *         and v1 isn't.
     */
    public static Double vectorQuotient2d(Vector2d v1, Vector2d v2)
    {
        Vector2d zero = new Vector2d();
        boolean v1zero = v1.epsilonEquals(zero, EPSILON);
        boolean v2zero = v2.epsilonEquals(zero, EPSILON);

        if (v1zero || v2zero) {
            if (v1zero && v2zero) {
                return 1d;
            } else if (v1zero) {
                return 0d;
            } else {
                return null;
            }
        }

        Vector2d v2perp = new Vector2d(-v2.y, v2.x); // a vector perpendicular to v2
        if (abs(v2perp.dot(v1)) < EPSILON) { // v1 is parallel to v2
            if (abs(v2.x) > EPSILON)
                return v1.x / v2.x;

            if (abs(v2.y) > EPSILON)
                return v1.y / v2.y;

            assert false : "Vector v2 is not a zero vector, but none of its coordinates is nonzero???";
            return null;
        }

        return null;
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

    /**
     * Provided a list of points, removes those points that are epsilon-equal so that no two remaining points are
     * epsilon-equal. The changes are made directly to the provided list.
     * 
     * @param list The list to scan for duplicates.
     * @return The input list after duplicate removal.
     */
    public static List<Point3d> removeEpsilonEqualPoints(List<Point3d> list)
    {
        return removeEpsilonEqualPoints(list, EPSILON);
    }

    /**
     * Provided a list of points, removes those points that are epsilon-equal so that no two remaining points are
     * epsilon-equal. The changes are made directly to the provided list.
     * 
     * @param list The list to scan for duplicates.
     * @param epsilon The epsilon to use in epsilonEquals().
     * @return The input list after duplicate removal.
     */
    public static List<Point3d> removeEpsilonEqualPoints(List<Point3d> list, double epsilon)
    {
        int i = 0;
        while (i < list.size() - 1) {
            int j = i + 1;
            Point3d iPoint = list.get(i);
            while (j < list.size()) {
                if (iPoint.epsilonEquals(list.get(j), epsilon)) {
                    list.remove(j);
                } else {
                    j++;
                }
            }
            i++;
        }
        return list;
    }
}

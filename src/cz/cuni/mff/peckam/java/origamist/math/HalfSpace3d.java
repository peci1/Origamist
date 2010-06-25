/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.math;

import static cz.cuni.mff.peckam.java.origamist.math.MathHelper.EPSILON;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

/**
 * Represents a half-space in 3D.
 * 
 * @author Martin Pecka
 */
public class HalfSpace3d
{
    /**
     * A coeficient in the general equation of the defining plane: ax + by + cz + d = 0. The halfspace is definaed as
     * the set of points for which ax+by+cz+d >= 0 is true.
     */
    protected double a;

    /**
     * A coeficient in the general equation of the defining plane: ax + by + cz + d = 0. The halfspace is definaed as
     * the set of points for which ax+by+cz+d >= 0 is true.
     */
    protected double b;

    /**
     * A coeficient in the general equation of the defining plane: ax + by + cz + d = 0. The halfspace is definaed as
     * the set of points for which ax+by+cz+d >= 0 is true.
     */
    protected double c;

    /**
     * A coeficient in the general equation of the defining plane: ax + by + cz + d = 0. The halfspace is definaed as
     * the set of points for which ax+by+cz+d >= 0 is true.
     */
    protected double d;

    /**
     * Create a halfspace defined by the plane containing p1,p2,p3. The halfspace contains a general point r.
     * 
     * @param p1 A point in the border plane.
     * @param p2 A point in the border plane.
     * @param p3 A point in the border plane.
     * @param r A general point in the halfspace.
     */
    public HalfSpace3d(Point3d p1, Point3d p2, Point3d p3, Point3d r)
    {
        Plane3d plane = new Plane3d(p1, p2, p3);
        a = plane.a;
        b = plane.b;
        c = plane.c;
        d = plane.d;

        if (!contains(r)) {
            a = -a;
            b = -b;
            c = -c;
            d = -d;
        }
    }

    public HalfSpace3d(double p1x, double p1y, double p1z, double p2x, double p2y, double p2z, double p3x, double p3y,
            double p3z, double rx, double ry, double rz)
    {
        this(new Point3d(p1x, p1y, p1z), new Point3d(p2x, p2y, p2z), new Point3d(p3x, p3y, p3z),
                new Point3d(rx, ry, rz));
    }

    /**
     * Returns true if this halfspace contains the given point.
     * 
     * @param point The point to check.
     * @return Whether this halfspace contains the given point.
     */
    public boolean contains(Point3d point)
    {
        return a * point.x + b * point.y + c * point.z + d >= -EPSILON;
    }

    /**
     * Create a halfspace perpendicular to the plane containing the given triangle. The halfspace is further defined by
     * the first two points of the triangle (which lie in the border plane); the third point (p3) of the triangle is the
     * general point somewhere in the halfspace.
     * 
     * @param p1 A point in the border plane of the new halfspace.
     * @param p2 A point in the border plane of the new halfspace.
     * @param r A general point of the new halfspace - defines the triangle together with p1, p2.
     * @return The halfspace as defined above.
     */
    public static HalfSpace3d createPerpendicularToTriangle(Point3d p1, Point3d p2, Point3d r)
    {
        Vector3d v1 = new Vector3d(p2.x - p1.x, p2.y - p1.y, p2.z - p1.z);
        Vector3d v2 = new Vector3d(r.x - p1.x, r.y - p1.y, r.z - p1.z);
        Vector3d c = new Vector3d();
        c.cross(v1, v2);
        c.add(p1);
        Point3d p = new Point3d();
        p.add(c);

        return new HalfSpace3d(p1, p2, p, r);
    }

    /**
     * Create a halfspace perpendicular to the plane containing the given triangle. The halfspace is further defined by
     * the first two points of the triangle (which lie in the border plane); the third point (r) of the triangle is the
     * general point somewhere in the halfspace.
     * 
     * @param p1x x-coordinate of a point in the border plane of the new halfspace.
     * @param p1y y-coordinate of a point in the border plane of the new halfspace.
     * @param p1z z-coordinate of a point in the border plane of the new halfspace.
     * @param p2x x-coordinate of a point in the border plane of the new halfspace.
     * @param p2y y-coordinate of a point in the border plane of the new halfspace.
     * @param p2z z-coordinate of a point in the border plane of the new halfspace.
     * @param rx x-coordinate of a general point of the new halfspace - defines the triangle together with p1, p2.
     * @param ry y-coordinate of a general point of the new halfspace - defines the triangle together with p1, p2.
     * @param rz z-coordinate of a general point of the new halfspace - defines the triangle together with p1, p2.
     * @return The halfspace as defined above.
     */
    public static HalfSpace3d createPerpendicularToTriangle(double p1x, double p1y, double p1z, double p2x, double p2y,
            double p2z, double rx, double ry, double rz)
    {
        return createPerpendicularToTriangle(new Point3d(p1x, p1y, p1z), new Point3d(p2x, p2y, p2z), new Point3d(rx,
                ry, rz));
    }

    @Override
    public String toString()
    {
        return "HalfSpace3d [" + a + "x + " + b + "y + " + c + "z + " + d + " >= 0]";
    }
}

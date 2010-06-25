/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.math;

import static cz.cuni.mff.peckam.java.origamist.math.MathHelper.EPSILON;

import javax.vecmath.Point3d;

/**
 * A plane in 3D.
 * 
 * @author Martin Pecka
 */
public class Plane3d
{
    /**
     * A coeficient in the general equation of a plane: ax + by + cz + d = 0.
     */
    protected double a;

    /**
     * A coeficient in the general equation of a plane: ax + by + cz + d = 0.
     */
    protected double b;

    /**
     * A coeficient in the general equation of a plane: ax + by + cz + d = 0.
     */
    protected double c;

    /**
     * A coeficient in the general equation of a plane: ax + by + cz + d = 0.
     */
    protected double d;

    /**
     * Constructs a plane containing the given three points.
     * 
     * @param p1 A point in the plane.
     * @param p2 A point in the plane.
     * @param p3 A point in the plane.
     */
    public Plane3d(Point3d p1, Point3d p2, Point3d p3)
    {
        a = p1.y * (p2.z - p3.z) + p2.y * (p3.z - p1.z) + p3.y * (p1.z - p2.z);
        b = p1.z * (p2.x - p3.x) + p2.z * (p3.x - p1.x) + p3.z * (p1.x - p2.x);
        c = p1.x * (p2.y - p3.y) + p2.x * (p3.y - p1.y) + p3.x * (p1.y - p2.y);
        d = -p1.x * (p2.y * p3.z - p3.y * p2.z) - p2.x * (p3.y * p1.z - p1.y * p3.z) - p3.x
                * (p1.y * p2.z - p2.y * p1.z);
    }

    /**
     * Constructs a plane containing the given three points.
     * 
     * @param p1x x-coordinate of a point in the plane.
     * @param p1y y-coordinate of a point in the plane.
     * @param p1z z-coordinate of a point in the plane.
     * @param p2x x-coordinate of a point in the plane.
     * @param p2y y-coordinate of a point in the plane.
     * @param p2z z-coordinate of a point in the plane.
     * @param p3x x-coordinate of a point in the plane.
     * @param p3y y-coordinate of a point in the plane.
     * @param p3z z-coordinate of a point in the plane.
     */
    public Plane3d(double p1x, double p1y, double p1z, double p2x, double p2y, double p2z, double p3x, double p3y,
            double p3z)
    {
        this(new Point3d(p1x, p1y, p1z), new Point3d(p2x, p2y, p2z), new Point3d(p3x, p3y, p3z));
    }

    public boolean contains(Point3d point)
    {
        double res = a * point.x + b * point.y + c * point.z + d;
        return -EPSILON <= res && res <= EPSILON;
    }

    @Override
    public String toString()
    {
        return "Plane3d [" + a + "x + " + b + "y + " + c + "z + " + d + " = 0]";
    }
}

/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.math;

import static cz.cuni.mff.peckam.java.origamist.math.MathHelper.EPSILON;

import javax.vecmath.Point2d;

/**
 * Represents a half-plane in 2D.
 * 
 * @author Martin Pecka
 */
public class HalfPlane2d
{
    /**
     * This is a coefficient in the general equation of the defining line (ax + by + c = 0). The halfplane is defined as
     * the set of points [x,y] for which is ax + by + c >= 0 true.
     */
    protected double a = 0;

    /**
     * This is a coefficient in the general equation of the defining line (ax + by + c = 0). The halfplane is defined as
     * the set of points [x,y] for which is ax + by + c >= 0 true.
     */
    protected double b = 0;

    /**
     * This is a coefficient in the general equation of the defining line (ax + by + c = 0). The halfplane is defined as
     * the set of points [x,y] for which is ax + by + c >= 0 true.
     */
    protected double c = 0;

    /**
     * Constructs the halfplane in the following manner - p1,p2 define a line and r is a general point in the halfplane.
     * 
     * @param p1 First point of the defining line.
     * @param p2 Second point of the defining line.
     * @param r A general point in the halfplane.
     */
    public HalfPlane2d(Point2d p1, Point2d p2, Point2d r)
    {
        a = p1.y - p2.y;
        b = p2.x - p1.x;
        c = p1.x * p2.y - p1.y * p2.x;

        if (!contains(r)) {
            a = -a;
            b = -b;
            c = -c;
        }
    }

    /**
     * Constructs the halfplane in the following manner - p1,p2 define a line and r is a general point in the halfplane.
     * 
     * @param p1x x-coordinate of the first point of the defining line.
     * @param p1y y-coordinate of the first point of the defining line.
     * @param p2x x-coordinate of the second point of the defining line.
     * @param p2y y-coordinate of the second point of the defining line.
     * @param rx x-coordinate of a general point in the halfplane.
     * @param ry y-coordinate of a general point in the halfplane.
     */
    public HalfPlane2d(double p1x, double p1y, double p2x, double p2y, double rx, double ry)
    {
        this(new Point2d(p1x, p1y), new Point2d(p2x, p2y), new Point2d(rx, ry));
    }

    /**
     * Returns true if this halfplane contains the given point.
     * 
     * @param p The point to check.
     * @return Whether the given point lies in this halfplane.
     */
    public boolean contains(Point2d p)
    {
        return p.x * a + p.y * b + c >= 0 - EPSILON;
    }

    @Override
    public String toString()
    {
        return "HalfPlane2d [" + a + "x + " + b + "y + " + c + " >= 0]";
    }

}

/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.math;

import static cz.cuni.mff.peckam.java.origamist.math.MathHelper.EPSILON;
import static java.lang.Math.abs;

import javax.vecmath.Point2d;

/**
 * Represents a half-plane in 2D.
 * 
 * @author Martin Pecka
 */
public class HalfPlane2d implements Cloneable
{
    /**
     * This is a coefficient in the general equation of the defining line (ax + by + c = 0). The halfplane is defined as
     * the set of points [x,y] for which is ax + by + c >= 0 true.
     */
    protected double  a = 0;

    /**
     * This is a coefficient in the general equation of the defining line (ax + by + c = 0). The halfplane is defined as
     * the set of points [x,y] for which is ax + by + c >= 0 true.
     */
    protected double  b = 0;

    /**
     * This is a coefficient in the general equation of the defining line (ax + by + c = 0). The halfplane is defined as
     * the set of points [x,y] for which is ax + by + c >= 0 true.
     */
    protected double  c = 0;

    /** The border line. */
    protected Line2d  line;

    /** A general point inside the halfplane. */
    protected Point2d r;

    /**
     * Constructs the halfplane in the following manner - p1,p2 define a line and r is a general point in the halfplane.
     * 
     * @param p1 First point of the defining line.
     * @param p2 Second point of the defining line.
     * @param r A general point in the halfplane.
     * 
     * @throws IllegalArgumentException If r lies on the line defined by p1 and p2.
     */
    public HalfPlane2d(Point2d p1, Point2d p2, Point2d r) throws IllegalArgumentException
    {
        a = p1.y - p2.y;
        b = p2.x - p1.x;
        c = p1.x * p2.y - p1.y * p2.x;

        line = new Line2d(p1, p2);
        this.r = r;

        if (line.contains(r)) {
            throw new IllegalArgumentException(
                    "Trying to define a halfplane but the general point lies on the border line.");
        }

        if (!contains(r)) {
            a = -a;
            b = -b;
            c = -c;
            line = new Line2d(p2, p1);
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
     * 
     * @throws IllegalArgumentException If r lies on the line defined by p1 and p2.
     */
    public HalfPlane2d(double p1x, double p1y, double p2x, double p2y, double rx, double ry)
            throws IllegalArgumentException
    {
        this(new Point2d(p1x, p1y), new Point2d(p2x, p2y), new Point2d(rx, ry));
    }

    /**
     * @return The border line on this halfplane.
     */
    public Line2d getLine()
    {
        return line;
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
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits(a);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(b);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(c);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        HalfPlane2d other = (HalfPlane2d) obj;
        if (Double.doubleToLongBits(a) != Double.doubleToLongBits(other.a))
            return false;
        if (Double.doubleToLongBits(b) != Double.doubleToLongBits(other.b))
            return false;
        if (Double.doubleToLongBits(c) != Double.doubleToLongBits(other.c))
            return false;
        return true;
    }

    /**
     * Return <code>true</code> if the given halfplane is almost equal to this one.
     * 
     * @param other The halfplane to compare.
     * @return <code>true</code> if the given halfplane is almost equal to this one.
     */
    public boolean epsilonEquals(HalfPlane2d other)
    {
        if (other == null)
            return false;
        return abs(a - other.a) < EPSILON && abs(b - other.b) < EPSILON && abs(c - other.c) < EPSILON;
    }

    @Override
    public String toString()
    {
        return "HalfPlane2d [" + a + "x + " + b + "y + " + c + " >= 0]";
    }

    @Override
    protected HalfPlane2d clone() throws CloneNotSupportedException
    {
        return new HalfPlane2d(null, null, null);
    }

}

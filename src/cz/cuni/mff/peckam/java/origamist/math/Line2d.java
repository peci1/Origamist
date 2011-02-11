/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.math;

import static cz.cuni.mff.peckam.java.origamist.math.MathHelper.EPSILON;
import static java.lang.Math.abs;

import javax.vecmath.Point2d;
import javax.vecmath.Vector2d;

/**
 * A line in 2D space.
 * 
 * @author Martin Pecka
 */
public class Line2d implements Cloneable
{

    /**
     * This is a coefficient in the general equation of the defining line (ax + by + c = 0).
     */
    protected double   a = 0;

    /**
     * This is a coefficient in the general equation of the defining line (ax + by + c = 0).
     */
    protected double   b = 0;

    /**
     * This is a coefficient in the general equation of the defining line (ax + by + c = 0).
     */
    protected double   c = 0;

    /** A point on the line. */
    protected Point2d  p;

    /** The direction vector of the line. */
    protected Vector2d v;

    /**
     * Constructs the line going through the given points.
     * 
     * A line with zero direction vector is allowed here, then it stands for a single point.
     * 
     * @param p1 First point of the line.
     * @param p2 Second point of the line.
     */
    public Line2d(Point2d p1, Point2d p2)
    {
        a = p1.y - p2.y;
        b = p2.x - p1.x;
        c = p1.x * p2.y - p1.y * p2.x;

        p = p1;
        v = new Vector2d(p2);
        v.sub(p1);
    }

    /**
     * Constructs the line going through the given point with the given direction.
     * 
     * @param p A point on the line.
     * @param v The direction vector of the line.
     */
    public Line2d(Point2d p, Vector2d v)
    {
        this(p, new Point2d(p.x + v.x, p.y + v.y));
    }

    /**
     * Returns true if this line contains the given point.
     * 
     * @param p The point to check.
     * @return Whether the given point lies on this line.
     */
    public boolean contains(Point2d p)
    {
        return abs(p.x * a + p.y * b + c) <= EPSILON;
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
        return "Line2d [" + a + "x + " + b + "y + " + c + " >= 0]";
    }

    @Override
    protected Object clone() throws CloneNotSupportedException
    {
        return new Line2d(p, v);
    }

}

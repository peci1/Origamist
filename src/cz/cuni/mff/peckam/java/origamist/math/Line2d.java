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
     * Constructs a clone of the given line.
     * 
     * @param line
     */
    public Line2d(Line2d line)
    {
        this(new Point2d(line.p), new Vector2d(line.v));
    }

    /**
     * Return the intersection with the line.
     * 
     * @param line The line to find intersection with.
     * @return The intersection point; <code>null</code> if no intersection point was found; <code>(NaN, NaN)</code> if
     *         the lines are equal.
     * 
     * @see http://sputsoft.com/blog/2010/03/line-line-intersection.html
     */
    public Point2d getIntersection(Line2d line)
    {
        double a = line.v.x * v.y - line.v.y * v.x;
        double b = line.v.x * (line.p.y - p.y) - line.v.y * (line.p.x - p.x);

        if (abs(a) < EPSILON) {
            if (abs(b) < EPSILON) {
                return new Point2d(Double.NaN, Double.NaN);
            } else {
                return null;
            }
        }

        return new Point2d(p.x + b / a * v.x, p.y + b / a * v.y);
    }

    /**
     * Returns the parameter <code>t</code> such that <code>this.p + t*this.v == point</code>. If the point doesn't lie
     * on this line, return <code>null</code>.
     * 
     * @param point The point to find parameter for.
     * @return The parameter <code>t</code> such that <code>this.p + t*this.v == point</code>. If the point doesn't lie
     *         on this line, return <code>null</code>.
     */
    public Double getParameterForPoint(Point2d point)
    {
        Vector2d v = new Vector2d();
        v.sub(this.p, point);

        return MathHelper.vectorQuotient2d(v, this.v);
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
        Line2d other = (Line2d) obj;
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
    public boolean epsilonEquals(Line2d other)
    {
        if (other == null)
            return false;
        if (!getClass().equals(other.getClass()))
            return false;
        Point2d intersection = getIntersection(other);
        return Double.isNaN(intersection.x);
    }

    @Override
    public String toString()
    {
        return "Line2d [" + a + "x + " + b + "y + " + c + " >= 0]";
    }

    @Override
    protected Line2d clone() throws CloneNotSupportedException
    {
        return new Line2d(this);
    }

}

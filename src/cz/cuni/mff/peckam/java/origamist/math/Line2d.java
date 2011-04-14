/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.math;

import static cz.cuni.mff.peckam.java.origamist.math.MathHelper.EPSILON;
import static java.lang.Math.abs;

import java.util.Iterator;

import javax.vecmath.Point2d;
import javax.vecmath.Vector2d;

/**
 * A line in 2D space.
 * 
 * @author Martin Pecka
 */
public class Line2d implements Cloneable, Vector<Double>
{

    /** */
    private static final long serialVersionUID = -3849433499881284033L;

    /**
     * This is a coefficient in the general equation of the defining line (ax + by + c = 0).
     */
    protected double          a                = 0;

    /**
     * This is a coefficient in the general equation of the defining line (ax + by + c = 0).
     */
    protected double          b                = 0;

    /**
     * This is a coefficient in the general equation of the defining line (ax + by + c = 0).
     */
    protected double          c                = 0;

    /** A point on the line. */
    protected Point2d         p;

    /** The direction vector of the line. */
    protected Vector2d        v;

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
     * @return A point lying on the line.
     */
    public Point2d getPoint()
    {
        return p;
    }

    /**
     * @return The direction vector of the line.
     */
    public Vector2d getVector()
    {
        return v;
    }

    /**
     * Return the intersection with the line.
     * 
     * @param line The line to find intersection with.
     * @return The intersection point (as a line with zero direction vector); <code>null</code> if no intersection point
     *         was found; <code>this</code> if the lines are epsilon-equal.
     * 
     * @see http://sputsoft.com/blog/2010/03/line-line-intersection.html
     */
    public Line2d getIntersection(Line2d line)
    {
        double a = line.v.x * v.y - line.v.y * v.x;
        double b = line.v.x * (line.p.y - p.y) - line.v.y * (line.p.x - p.x);

        if (abs(a) < EPSILON) {
            if (abs(b) < EPSILON) {
                return this;
            } else {
                return null;
            }
        }

        return new Line2d(new Point2d(p.x + b / a * v.x, p.y + b / a * v.y), new Vector2d());
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
        v.sub(point, this.p);

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

    /**
     * Return the point on this line that is the nearest to the given point.
     * 
     * @param p The point to find the nearest one for.
     * @return The point on this line that is the nearest to the given point (the passed-in instance is returned if it
     *         lies on this line).
     */
    public Point2d getNearestPoint(Point2d p)
    {
        if (contains(p))
            return p;

        if (getVector().epsilonEquals(new Vector2d(), EPSILON))
            return getPoint();

        Vector2d perp = new Vector2d(getVector().y, -getVector().x);
        Line2d perpLine = new Line2d(p, perp);

        Line2d intPoint = perpLine.getIntersection(this);
        // intPoint can be neither null nor can have non-zero dir. vector (the lines aren't parallel)
        return intPoint.getPoint();
    }

    /**
     * Mirror the given point around this line (axis symmetry).
     * 
     * @param p The point to mirror.
     * @return The mirrored point (the passed-in instance is returned if it lies on this line).
     */
    public Point2d mirror(Point2d p)
    {
        Point2d nearest = getNearestPoint(p);
        if (nearest == p) // p lies on this line
            return p;

        // direction vector p->nearest
        Vector2d vect = new Vector2d(nearest);
        vect.sub(p);

        Point2d mirrored = new Point2d(nearest);
        mirrored.add(vect);
        return mirrored;
    }

    /**
     * Mirror the given line around this line (axis symmetry).
     * 
     * @param l The line to mirror.
     * @return The mirrored line (the passed-in instance is returned if it lies on this line).
     */
    public Line2d mirror(Line2d l)
    {
        if (l.epsilonEquals(this))
            return l;

        Point2d p1 = l.getPoint();
        Point2d p2 = new Point2d(p1);
        p2.add(l.getVector());
        return new Line2d(mirror(p1), mirror(p2));
    }

    /**
     * Mirror the given segment around this line (axis symmetry).
     * 
     * @param l The segment to mirror.
     * @return The mirrored segment (the passed-in instance is returned if it lies on this line).
     */
    public Segment2d mirror(Segment2d l)
    {
        Segment2d intPoint = l.getIntersection(this);
        if (intPoint != null && !intPoint.getVector().epsilonEquals(new Vector2d(), EPSILON))
            return l;

        return new Segment2d(mirror(l.getP1()), mirror(l.getP2()));
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
     * Return <code>true</code> if the given line is almost equal to this one.
     * 
     * @param other The halfplane to compare.
     * @return <code>true</code> if the given line is almost equal to this one.
     */
    public boolean epsilonEquals(Line2d other)
    {
        if (other == null)
            return false;
        if (!getClass().equals(other.getClass()))
            return false;
        return getIntersection(other) == this;
    }

    @Override
    public String toString()
    {
        return "Line2d [" + a + "x + " + b + "y + " + c + " >= 0]";
    }

    @Override
    protected Line2d clone()
    {
        return new Line2d(this);
    }

    @Override
    public Iterator<Double> iterator()
    {
        return new Iterator<Double>() {
            private int i = 0;

            @Override
            public boolean hasNext()
            {
                return i + 1 < getDimension();
            }

            @Override
            public Double next()
            {
                return get(i++);
            }

            @Override
            public void remove()
            {
                throw new UnsupportedOperationException("Cannot remove from a vector's iterator.");
            }
        };
    }

    @Override
    public int getDimension()
    {
        return 4;
    }

    @Override
    public Double get(int index)
    {
        switch (index) {
            case 0:
                return v.x;
            case 1:
                return v.y;
            case 2:
                return p.x;
            case 3:
                return p.y;
            default:
                throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public Double set(int index, Double value)
    {
        Double oldVal;
        switch (index) {
            case 0:
                oldVal = v.x;
                v.x = value;
                break;
            case 1:
                oldVal = v.y;
                v.y = value;
                break;
            case 2:
                oldVal = p.x;
                p.x = value;
                break;
            case 3:
                oldVal = p.y;
                p.y = value;
                break;
            default:
                throw new IndexOutOfBoundsException();
        }
        return oldVal;
    }

}

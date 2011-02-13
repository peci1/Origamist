/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.math;

import static cz.cuni.mff.peckam.java.origamist.math.MathHelper.EPSILON;

import javax.vecmath.Point2d;

/**
 * A line segment in 2D.
 * 
 * @author Martin Pecka
 */
public class Segment2d extends Line2d
{
    protected Point2d p2;

    public Segment2d(Point2d p1, Point2d p2)
    {
        super(p1, p2);
        this.p2 = p2;
    }

    public Point2d getP1()
    {
        return this.p;
    }

    public Point2d getP2()
    {
        return this.p2;
    }

    /**
     * @return The both endpoints of the segment.
     */
    public Point2d[] getPoints()
    {
        return new Point2d[] { p, p2 };
    }

    @Override
    public Point2d getIntersection(Line2d line)
    {
        Point2d intersection = super.getIntersection(line);
        if (intersection == null || Double.isNaN(intersection.x) || Double.isNaN(intersection.y))
            return intersection;

        if (contains(intersection)) {
            return intersection;
        }
        return null;
    }

    /**
     * Return the intersection with another segment.
     * 
     * TODO should be able to differentiate if two parallel vectors have a common part or not
     * 
     * @param segment The other segment to find intersection with.
     * @return the intersection with another segment. Will be <code>null</code> if no intersection exists and will
     *         return (NaN, NaN, NaN) if the segments lie on the same line.
     */
    public Point2d getIntersection(Segment2d segment)
    {
        Point2d intersection = getIntersection((Line2d) segment);
        if (intersection == null || Double.isNaN(intersection.x) || Double.isNaN(intersection.y))
            return intersection;

        if (segment.contains(intersection)) {
            return intersection;
        }
        return null;
    }

    /**
     * @return The length of the segment.
     */
    public double getLength()
    {
        return v.length();
    }

    @Override
    public boolean contains(Point2d point)
    {
        Double quotient = getParameterForPoint(point);
        // if the quotient is between 0 and 1, the point lies inside the segment
        return (quotient != null && quotient >= -EPSILON && quotient <= 1.0 + EPSILON);
    }

    @Override
    public boolean epsilonEquals(Line2d other)
    {
        return epsilonEquals(other, false);
    }

    /**
     * @param other The line to compare.
     * @param allowInverseDirection If true, even two segments with inverted direction will be considered equal.
     * @return see {@link Line2d#epsilonEquals(Line3d)}.
     */
    public boolean epsilonEquals(Line2d other, boolean allowInverseDirection)
    {
        if (other == null)
            return false;
        if (!getClass().equals(other.getClass()))
            return false;

        Point2d op = other.p;
        Point2d op2 = ((Segment2d) other).p2;

        boolean p_op = p.epsilonEquals(op, EPSILON);
        boolean p_op2 = p.epsilonEquals(op2, EPSILON);
        boolean p2_op = p2.epsilonEquals(op, EPSILON);
        boolean p2_op2 = p2.epsilonEquals(op2, EPSILON);

        if (allowInverseDirection) {
            return (p_op && p2_op2) || (p_op2 && p2_op);
        } else {
            return p_op && p2_op2;
        }
    }

    /**
     * Return true if the segments have a common part (at least a point) and are parallel.
     * 
     * @param segment The other segment to check.
     * @return true if the segments have a common part (at least a point) and are parallel.
     */
    public boolean overlaps(Segment2d segment)
    {
        Point2d intersection = getIntersection(segment);
        if (intersection == null || !Double.isNaN(intersection.x)) // NaN means segments on the same line
            return false;
        return contains(segment.p) || contains(segment.p2);
    }

    @Override
    public Object clone() throws CloneNotSupportedException
    {
        return new Segment2d(new Point2d(p), new Point2d(p2));
    }

    @Override
    public String toString()
    {
        Point2d pt = new Point2d(v);
        pt.add(p);
        return "Segment2d [" + p + " + t*" + v + "] [" + p + ", " + pt + "]";
    }
}

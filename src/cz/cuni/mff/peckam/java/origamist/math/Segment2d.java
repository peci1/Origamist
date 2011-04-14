/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.math;

import static cz.cuni.mff.peckam.java.origamist.math.MathHelper.EPSILON;

import javax.vecmath.Point2d;
import javax.vecmath.Vector2d;

/**
 * A line segment in 2D.
 * 
 * @author Martin Pecka
 */
public class Segment2d extends Line2d
{
    /** */
    private static final long serialVersionUID = -118307867048129986L;

    protected Point2d         p2;

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

    /**
     * @return The intersection point (as a segment with zero direction vector); <code>null</code> if no intersection
     *         point was found; <code>this</code> if this lies on the given line and the line is not a segment; or a
     *         (new instance of) segment of intersection of this segment and the given segment.
     */
    @Override
    public Segment2d getIntersection(Line2d line)
    {
        if (line instanceof Segment2d) {
            return getIntersection((Segment2d) line);
        }

        // line is a pure line, not a segment
        Line2d intersection = super.getIntersection(line);
        if (intersection == null || intersection == this)
            // no intersection with this as a pure line, or this lies on line
            return (Segment2d) intersection;

        if (contains(intersection.p)) {
            return new Segment2d(intersection.p, intersection.p);
        }
        return null;
    }

    /**
     * Return the intersection with another segment.
     * 
     * @param segment The other segment to find intersection with.
     * @return The intersection point (as a segment with zero direction vector); <code>null</code> if no intersection
     *         point was found; or a (new instance of) segment of intersection of this segment and the given segment.
     */
    public Segment2d getIntersection(Segment2d segment)
    {
        // creating a new instance of line is necessary to avoid infinite call loop
        Segment2d intersection = getIntersection(new Line2d(segment.p, segment.v));
        if (intersection == null) {
            return null;
        } else if (intersection.v.epsilonEquals(new Vector2d(), EPSILON)) { // single point intersection
            if (!segment.contains(intersection.p))
                return null;
            return new Segment2d(intersection.p, intersection.p);
        } else { // the segments lie on the same line
            Segment2d big = this;
            Segment2d small = segment;
            if (!this.contains(segment.p) && !this.contains(segment.p2)) {
                big = segment;
                small = this;
            }
            // if one segment is whole inside the other, big is the "containing" segment... otherwise it doesn't matter

            if (big.contains(small.p)) {
                if (big.contains(small.p2)) {
                    return small.clone();
                } else { // small has to contain either big.p or big.p2
                    if (small.contains(big.p)) {
                        return new Segment2d(big.p, small.p);
                    } else {
                        return new Segment2d(big.p2, small.p);
                    }
                }
            } else if (big.contains(small.p2)) { // small has to contain either big.p or big.p2
                if (small.contains(big.p)) {
                    return new Segment2d(big.p, small.p2);
                } else {
                    return new Segment2d(big.p2, small.p2);
                }
            } else {
                // big is the potentionally "containing" segment, and if it contains no points of small, the segments
                // have no intersection
                return null;
            }
        }
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

    /**
     * Return the point on this segment that is the nearest to the given point.
     * 
     * @param p The point to find the nearest one for.
     * @return The point on this segment that is the nearest to the given point.
     */
    @Override
    public Point2d getNearestPoint(Point2d p)
    {
        Point2d point = super.getNearestPoint(p);

        if (contains(point)) {
            return point;
        } else {
            double p1dist = p.distance(point);
            double p2dist = p2.distance(point);
            if (p1dist < p2dist)
                return p;
            else
                return p2;
        }
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
        return contains(segment.p) || contains(segment.p2) || segment.contains(p) || segment.contains(p2);
    }

    @Override
    public Segment2d clone()
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

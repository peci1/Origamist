/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.math;

import static cz.cuni.mff.peckam.java.origamist.math.MathHelper.EPSILON;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

/**
 * A line segment in 3D.
 * 
 * @author Martin Pecka
 */
public class Segment3d extends Line3d
{
    /** */
    private static final long serialVersionUID = -7131577422249930817L;

    protected Point3d         p2;

    public Segment3d(Point3d p1, Point3d p2)
    {
        super(p1, p2);
        this.p2 = p2;
    }

    public Segment3d(Segment3d seg)
    {
        this(seg.getP1(), seg.getP2());
    }

    public Point3d getP1()
    {
        return this.p;
    }

    public Point3d getP2()
    {
        return this.p2;
    }

    /**
     * @return The both endpoints of the segment.
     */
    public Point3d[] getPoints()
    {
        return new Point3d[] { p, p2 };
    }

    /**
     * @return The intersection point (as a segment with zero direction vector); <code>null</code> if no intersection
     *         point was found; <code>this</code> if this lies on the given line and the line is not a segment; or a
     *         (new instance of) segment of intersection of this segment and the given segment.
     */
    @Override
    public Segment3d getIntersection(Line3d line)
    {
        if (line instanceof Segment3d) {
            return getIntersection((Segment3d) line);
        }

        // line is a pure line, not a segment
        Line3d intersection = super.getIntersection(line);
        if (intersection == null || intersection == this)
            // no intersection with this as a pure line, or this lies on line
            return (Segment3d) intersection;

        if (contains(intersection.p)) {
            return new Segment3d(intersection.p, intersection.p);
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
    public Segment3d getIntersection(Segment3d segment)
    {
        // creating a new instance of line is necessary to avoid infinite call loop
        Segment3d intersection = getIntersection(new Line3d(segment.p, segment.v));
        if (intersection == null) {
            return null;
        } else if (intersection.v.epsilonEquals(new Vector3d(), EPSILON)) { // single point intersection
            if (!segment.contains(intersection.p))
                return null;
            return new Segment3d(intersection.p, intersection.p);
        } else { // the segments lie on the same line
            Segment3d big = this;
            Segment3d small = segment;
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
                        return new Segment3d(big.p, small.p);
                    } else {
                        return new Segment3d(big.p2, small.p);
                    }
                }
            } else if (big.contains(small.p2)) { // small has to contain either big.p or big.p2
                if (small.contains(big.p)) {
                    return new Segment3d(big.p, small.p2);
                } else {
                    return new Segment3d(big.p2, small.p2);
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
    public boolean contains(Point3d point)
    {
        Double quotient = getParameterForPoint(point);
        // if the quotient is between 0 and 1, the point lies inside the segment
        return (quotient != null && quotient >= -EPSILON && quotient <= 1.0 + EPSILON);
    }

    @Override
    public boolean epsilonEquals(Line3d other)
    {
        return epsilonEquals(other, false);
    }

    /**
     * @param other The line to compare.
     * @param allowInverseDirection If true, even two segments with inverted direction will be considered equal.
     * @return see {@link Line3d#epsilonEquals(Line3d)}.
     */
    public boolean epsilonEquals(Line3d other, boolean allowInverseDirection)
    {
        if (!getClass().equals(other.getClass()))
            return false;

        Point3d op = other.p;
        Point3d op2 = ((Segment3d) other).p2;

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
     * Return true if these segments have a common part (at least a point).
     * 
     * @param segment The other segment to check.
     * @return true if these segments have a common part (at least a point).
     */
    public boolean overlaps(Segment3d segment)
    {
        return contains(segment.p) || contains(segment.p2) || segment.contains(p) || segment.contains(p2);
    }

    @Override
    protected Segment3d clone()
    {
        return new Segment3d(new Point3d(p), new Point3d(p2));
    }

    @Override
    public String toString()
    {
        Point3d pt = new Point3d(v);
        pt.add(p);
        return "Segment3d [" + p + " + t*" + v + "] [" + p + ", " + pt + "]";
    }

}

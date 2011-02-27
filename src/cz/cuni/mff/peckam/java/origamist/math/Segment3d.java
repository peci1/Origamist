/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.math;

import static cz.cuni.mff.peckam.java.origamist.math.MathHelper.EPSILON;

import javax.vecmath.Point3d;

/**
 * A line segment in 3D.
 * 
 * @author Martin Pecka
 */
public class Segment3d extends Line3d
{
    protected Point3d p2;

    public Segment3d(Point3d p1, Point3d p2)
    {
        super(p1, p2);
        this.p2 = p2;
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

    @Override
    public Point3d getIntersection(Line3d line)
    {
        Point3d intersection = super.getIntersection(line);
        if (intersection == null || Double.isNaN(intersection.x) || Double.isNaN(intersection.y)
                || Double.isNaN(intersection.z))
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
    public Point3d getIntersection(Segment3d segment)
    {
        Point3d intersection = getIntersection((Line3d) segment);
        if (intersection == null || Double.isNaN(intersection.x) || Double.isNaN(intersection.y)
                || Double.isNaN(intersection.z))
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
    protected Segment3d clone() throws CloneNotSupportedException
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

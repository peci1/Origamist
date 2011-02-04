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
     * Return the intersection with another segment. Will be <code>null</code> if no intersection exists and will return
     * (NaN, NaN, NaN) if the segments are perpendicular.
     * 
     * TODO should be able to differentiate if two perpendicular vectors have a common part or not
     * 
     * @param segment The other segment to find intersection with.
     * @return the intersection with another segment. Will be <code>null</code> if no intersection exists and will
     *         return (NaN, NaN, NaN) if the segments are perpendicular.
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
        // a vector from point p to the given point, we will compare this vector to the direction vector of the segment
        Vector3d vec = new Vector3d(point);
        vec.sub(p);
        Double quotient = MathHelper.vectorQuotient3d(vec, v);
        // if the quotient is between 0 and 1, the point lies inside the segment
        return (quotient != null && quotient >= -EPSILON && quotient <= 1.0 + EPSILON);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException
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

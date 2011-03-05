/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.math;

import static cz.cuni.mff.peckam.java.origamist.math.MathHelper.EPSILON;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

/**
 * A line in 3D.
 * 
 * @author Martin Pecka
 */
public class Line3d implements Cloneable
{
    /**
     * A point this line goes through.
     */
    protected Point3d  p;

    /**
     * The direction vector of the line.
     */
    protected Vector3d v;

    /**
     * Create a standalone copy (clone) of the given line.
     * 
     * @param line The line to copy.
     */
    public Line3d(Line3d line)
    {
        this.p = new Point3d(line.p);
        this.v = new Vector3d(line.v);
    }

    /**
     * Create the line going through the given point and with given direction.
     * 
     * A line with zero direction vector is allowed here, then it stands for a single point.
     * 
     * @param p A point the line goes through.
     * @param v The direction vector of the line.
     */
    public Line3d(Point3d p, Vector3d v)
    {
        this.p = p;
        this.v = v;
    }

    /**
     * Create a line going through the given points.
     * 
     * @param p1 A point on the line.
     * @param p2 A point on the line.
     */
    public Line3d(Point3d p1, Point3d p2)
    {
        this(p1, new Vector3d(p2.x - p1.x, p2.y - p1.y, p2.z - p1.z));
    }

    /**
     * Get the intersection point of this line with the given line.
     * 
     * @param line The line to find intersection with.
     * @return The intersection point (as a line with zero direction vector); <code>null</code> if no intersection point
     *         was found; <code>this</code> if the lines are epsilon-equal.
     * 
     * @see http://mathforum.org/library/drmath/view/62814.html
     */
    public Line3d getIntersection(Line3d line)
    {
        Vector3d v1xv2 = new Vector3d();
        v1xv2.cross(v, line.v);

        if (v1xv2.epsilonEquals(new Vector3d(), EPSILON)) {
            // the lines are parallel
            if (line.contains(p)) {
                // and equal
                return this;
            } else {
                // and different
                return null;
            }
        }

        Point3d p2_p1 = new Point3d();
        p2_p1.sub(line.p, p);
        Vector3d p2_p1xv2 = new Vector3d();
        p2_p1xv2.cross(new Vector3d(p2_p1), line.v);

        Double q = MathHelper.vectorQuotient3d(p2_p1xv2, v1xv2);
        if (q == null)
            // the lines are skew
            return null;

        Point3d result = new Point3d(p);
        Vector3d vt = new Vector3d(v);
        vt.scale(q);
        result.add(vt);
        return new Line3d(result, new Vector3d());
    }

    /**
     * Returns the parameter <code>t</code> such that <code>this.p + t*this.v == point</code>. If the point doesn't lie
     * on this line, return <code>null</code>.
     * 
     * @param point The point to find parameter for.
     * @return The parameter <code>t</code> such that <code>this.p + t*this.v == point</code>. If the point doesn't lie
     *         on this line, return <code>null</code>.
     */
    public Double getParameterForPoint(Point3d point)
    {
        Vector3d v = new Vector3d();
        v.sub(point, this.p);

        return MathHelper.vectorQuotient3d(v, this.v);
    }

    /**
     * Return true if this line is parallel to the other line.
     * 
     * @param line The other line.
     * @return True if this line is parallel to the other line.
     */
    public boolean isParallelTo(Line3d line)
    {
        Vector3d v1xv2 = new Vector3d();
        v1xv2.cross(v, line.v);

        // the cross product is zero iff the vectors are parallel or one of them is a zero vector
        return v1xv2.epsilonEquals(new Vector3d(), EPSILON);
    }

    public boolean contains(Point3d point)
    {
        Point3d pt = new Point3d();
        pt.sub(point, p);
        Vector3d vt = new Vector3d(pt);
        vt.cross(vt, v);
        // if vt and v are colinear (parallel), the point is contained in this line
        return vt.epsilonEquals(new Vector3d(), EPSILON);
    }

    /**
     * @return A point on the line.
     */
    public Point3d getPoint()
    {
        return p;
    }

    /**
     * @return The direction vector of the line.
     */
    public Vector3d getVector()
    {
        return v;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((p == null) ? 0 : p.hashCode());
        result = prime * result + ((v == null) ? 0 : v.hashCode());
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
        Line3d other = (Line3d) obj;
        if (p == null) {
            if (other.p != null)
                return false;
        } else if (!p.equals(other.p))
            return false;
        if (v == null) {
            if (other.v != null)
                return false;
        } else if (!v.equals(other.v))
            return false;
        return true;
    }

    /**
     * Return <code>true</code> if the given line is almost equal to this one.
     * 
     * @param other The line to compare.
     * @return <code>true</code> if the given line is almost equal to this one.
     */
    public boolean epsilonEquals(Line3d other)
    {
        if (!getClass().equals(other.getClass()))
            return false;
        // getIntersection() returns this if the lines are epsilon-equal
        return getIntersection(other) == this;
    }

    @Override
    protected Line3d clone() throws CloneNotSupportedException
    {
        return new Line3d(this);
    }

    @Override
    public String toString()
    {
        return "Line3d [" + p + " + t*" + v + "]";
    }
}

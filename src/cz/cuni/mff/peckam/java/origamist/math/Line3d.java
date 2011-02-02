/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.math;

import static cz.cuni.mff.peckam.java.origamist.math.MathHelper.EPSILON;
import static java.lang.Math.abs;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

/**
 * A line in 3D.
 * 
 * @author Martin Pecka
 */
public class Line3d
{
    /**
     * A point this line goes through.
     */
    protected Point3d  p;

    /**
     * The direction vector of the line.
     */
    protected Vector3d v;

    public Line3d(Point3d p, Vector3d v)
    {
        this.p = p;
        this.v = v;
    }

    public Line3d(Point3d p1, Point3d p2)
    {
        this(p1, new Vector3d(p2.x - p1.x, p2.y - p1.y, p2.z - p1.z));
    }

    /**
     * Get the intersection point of this line with the given line.
     * 
     * @param line The line to find intersection with.
     * @return The intersection point; null if no intersection point was found; (NaN, NaN, NaN) if the lines are equal.
     */
    public Point3d getIntersection(Line3d line)
    {
        Vector3d v1xv2 = new Vector3d();
        v1xv2.cross(v, line.v);

        Point3d p2_p1 = new Point3d();
        p2_p1.sub(line.p, p);
        Vector3d p2_p1xv2 = new Vector3d();
        p2_p1xv2.cross(new Vector3d(p2_p1), line.v);

        if (abs(v1xv2.x) < EPSILON && abs(v1xv2.y) < EPSILON && abs(v1xv2.z) < EPSILON && line.contains(p))
            return new Point3d(Double.NaN, Double.NaN, Double.NaN);

        double qx, qy, qz;
        qx = p2_p1xv2.x / v1xv2.x;
        qy = p2_p1xv2.y / v1xv2.y;
        qz = p2_p1xv2.z / v1xv2.z;
        if (Double.isNaN(qx) && !Double.isNaN(qy)) {
            qx = qy;
        } else if (Double.isNaN(qx)) {
            qx = qz;
        }
        if (Double.isNaN(qy) && !Double.isNaN(qz)) {
            qy = qz;
        } else if (Double.isNaN(qy)) {
            qy = qx;
        }
        if (Double.isNaN(qz) && !Double.isNaN(qx)) {
            qz = qx;
        } else if (Double.isNaN(qz)) {
            qz = qy;
        }

        if (!(abs(qx - qy) <= EPSILON && abs(qy - qz) <= EPSILON && abs(qz - qx) <= EPSILON))
            return null;

        double q = qx;

        Point3d result = new Point3d(p);
        Vector3d vt = new Vector3d(v);
        vt.scale(q);
        result.add(vt);
        return result;

    }

    public boolean contains(Point3d point)
    {
        Point3d pt = new Point3d();
        pt.sub(point, p);
        Vector3d vt = new Vector3d(pt);
        vt.cross(vt, v);
        // if vt and v are colinear (parallel), the point is contained in this line
        return (abs(vt.x) < EPSILON && abs(vt.y) < EPSILON && abs(vt.z) < EPSILON);
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
    public String toString()
    {
        return "Line3d [" + p + " + t*" + v + "]";
    }
}

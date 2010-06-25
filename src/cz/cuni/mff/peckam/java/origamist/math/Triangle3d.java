/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.math;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

/**
 * A representation of a 3D triangle.
 * 
 * @author Martin Pecka
 */
public class Triangle3d implements Cloneable
{
    protected Point3d     p1;
    protected Point3d     p2;
    protected Point3d     p3;

    protected HalfSpace3d hs1;
    protected HalfSpace3d hs2;
    protected HalfSpace3d hs3;
    protected Plane3d     plane;

    protected Segment3d   s1;
    protected Segment3d   s2;
    protected Segment3d   s3;

    public Triangle3d(Point3d p1, Point3d p2, Point3d p3)
    {
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;

        plane = new Plane3d(p1, p2, p3);
        hs1 = HalfSpace3d.createPerpendicularToTriangle(p1, p2, p3);
        hs2 = HalfSpace3d.createPerpendicularToTriangle(p2, p3, p1);
        hs3 = HalfSpace3d.createPerpendicularToTriangle(p3, p1, p2);

        s1 = new Segment3d(p1, p2);
        s2 = new Segment3d(p2, p3);
        s3 = new Segment3d(p1, p3);
    }

    public Triangle3d(double p1x, double p1y, double p1z, double p2x, double p2y, double p2z, double p3x, double p3y,
            double p3z)
    {
        this(new Point3d(p1x, p1y, p1z), new Point3d(p2x, p2y, p2z), new Point3d(p3x, p3y, p3z));
    }

    /**
     * Change the points of this triangle. Only non-null points will be changed.
     * 
     * @param p1
     * @param p2
     * @param p3
     */
    public void setPoints(Point3d p1, Point3d p2, Point3d p3)
    {
        if (p1 != null)
            this.p1 = p1;
        if (p2 != null)
            this.p2 = p2;
        if (p3 != null)
            this.p3 = p3;
    }

    /**
     * Return the coordinates of the first point.
     * 
     * @return The coordinates of the first point.
     */
    public Point3d getP1()
    {
        return p1;
    }

    /**
     * Return the coordinates of the second point.
     * 
     * @return The coordinates of the second point.
     */
    public Point3d getP2()
    {
        return p2;
    }

    /**
     * Return the coordinates of the third point.
     * 
     * @return The coordinates of the third point.
     */
    public Point3d getP3()
    {
        return p3;
    }

    /**
     * @return The first side of the triangle.
     */
    public Segment3d getS1()
    {
        return s1;
    }

    /**
     * @return The second side of the triangle.
     */
    public Segment3d getS2()
    {
        return s2;
    }

    /**
     * @return The third side of the triangle.
     */
    public Segment3d getS3()
    {
        return s3;
    }

    /**
     * Returns true if this triangle contains the given point.
     * 
     * @param point The point to check.
     * @return Whether this triangle contains the given point.
     */
    public boolean cotains(Point3d point)
    {
        return plane.contains(point) && hs1.contains(point) && hs2.contains(point) && hs3.contains(point);
    }

    /**
     * Return the point corresponding to the given barycentric coordinates in this triangle.
     * 
     * @param b The barycentric coordinates to convert.
     * @return The point corresponding to the given barycentric coordinates in this triangle.
     */
    public Point3d interpolatePointFromBarycentric(Vector3d b)
    {
        Point3d result = new Point3d();
        result.x = b.x * p1.x + b.y * p2.x + b.z * p3.x;
        result.y = b.x * p1.y + b.y * p2.y + b.z * p3.y;
        result.z = b.x * p1.z + b.y * p2.z + b.z * p3.z;
        return result;
    }

    public List<Point3d> getIntersection(Line3d line)
    {
        List<Point3d> result = new ArrayList<Point3d>(3);
        if (line instanceof Segment3d) {
            result.add(s1.getIntersection((Segment3d) line));
            result.add(s2.getIntersection((Segment3d) line));
            result.add(s3.getIntersection((Segment3d) line));
        } else {
            result.add(s1.getIntersection(line));
            result.add(s2.getIntersection(line));
            result.add(s3.getIntersection(line));
        }

        if (result.get(0) == null && result.get(1) == null && result.get(2) == null)
            result.clear();

        return result;
    }

    public Vector3d getNormal()
    {
        Vector3d normal = new Vector3d();
        Vector3d foo1 = new Vector3d(p3);
        Vector3d foo2 = new Vector3d(p2);
        foo1.sub(p1);
        foo2.sub(p1);
        normal.cross(foo1, foo2);
        normal.normalize();
        return normal;
    }

    @Override
    public Object clone()
    {
        return new Triangle3d(p1.x, p1.y, p1.z, p2.x, p2.y, p2.z, p3.x, p3.y, p3.z);
    }

    @Override
    public String toString()
    {
        return "Triangle3d [" + p1 + ", " + p2 + ", " + p3 + "]";
    }

}
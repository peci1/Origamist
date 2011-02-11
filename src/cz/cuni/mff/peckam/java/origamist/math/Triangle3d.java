/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.math;

import static cz.cuni.mff.peckam.java.origamist.math.MathHelper.EPSILON;

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

    /**
     * Create a triangle with the given vertices.
     * 
     * @param p1 A vertex.
     * @param p2 A vertex.
     * @param p3 A vertex.
     * 
     * @throws IllegalArgumentException If the given points lie in one line.
     */
    public Triangle3d(Point3d p1, Point3d p2, Point3d p3) throws IllegalArgumentException
    {
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;

        if (new Line3d(p1, p2).contains(p3)) {
            throw new IllegalArgumentException("Trying to create a triangle from colinear points.");
        }

        recomputeDerivedItems();
    }

    /**
     * Create a triangle with the given vertices.
     * 
     * @param p1x A vertex's x coordinate.
     * @param p1y A vertex's y coordinate.
     * @param p1z A vertex's z coordinate.
     * @param p2x A vertex's x coordinate.
     * @param p2y A vertex's y coordinate.
     * @param p2z A vertex's z coordinate.
     * @param p3x A vertex's x coordinate.
     * @param p3y A vertex's y coordinate.
     * @param p3z A vertex's z coordinate.
     * 
     * @throws IllegalArgumentException If the given points lie in one line.
     */
    public Triangle3d(double p1x, double p1y, double p1z, double p2x, double p2y, double p2z, double p3x, double p3y,
            double p3z) throws IllegalArgumentException
    {
        this(new Point3d(p1x, p1y, p1z), new Point3d(p2x, p2y, p2z), new Point3d(p3x, p3y, p3z));
    }

    /**
     * Compute new values of all the helper fields such as plane, s1, s2, s3 and so...
     */
    protected void recomputeDerivedItems()
    {
        plane = new Plane3d(p1, p2, p3);
        hs1 = HalfSpace3d.createPerpendicularToTriangle(p1, p2, p3);
        hs2 = HalfSpace3d.createPerpendicularToTriangle(p2, p3, p1);
        hs3 = HalfSpace3d.createPerpendicularToTriangle(p3, p1, p2);

        s1 = new Segment3d(p1, p2);
        s2 = new Segment3d(p2, p3);
        s3 = new Segment3d(p1, p3);
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

        recomputeDerivedItems();
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
     * @return An array of vertices of the triangle. Further modifications to this array will have no effect on the
     *         triangle.
     */
    public Point3d[] getVertices()
    {
        return new Point3d[] { p1, p2, p3 };
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
     * @return An array of all edges of the triangle. Further modifications to this array will have no effect on the
     *         triangle.
     */
    public Segment3d[] getEdges()
    {
        return new Segment3d[] { s1, s2, s3 };
    }

    /**
     * @return The plane the triangle lies in.
     */
    public Plane3d getPlane()
    {
        return plane;
    }

    /**
     * Returns true if this triangle contains the given point.
     * 
     * @param point The point to check.
     * @return Whether this triangle contains the given point.
     */
    public boolean contains(Point3d point)
    {
        return plane.contains(point) && hs1.contains(point) && hs2.contains(point) && hs3.contains(point);
    }

    /**
     * Return <code>true</code> if the given point lies in one of the sides of this triangle.
     * 
     * @param point The point to check.
     * @return <code>true</code> if the given point lies in one of the sides of this triangle.
     */
    public boolean sidesContain(Point3d point)
    {
        return s1.contains(point) || s2.contains(point) || s3.contains(point);
    }

    /**
     * Return <code>true</code> if the given point is a vertex of this triangle.
     * 
     * @param point The point to check.
     * @return <code>true</code> if the given point is a vertex of this triangle.
     */
    public boolean isVertex(Point3d point)
    {
        return p1.epsilonEquals(point, EPSILON) || p2.epsilonEquals(point, EPSILON) || p3.epsilonEquals(point, EPSILON);
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

    /**
     * Return the intersection points of this triangle and the given line.
     * 
     * @param line The line to get intersections with.
     * @return A segment that defines the intersection of the given line (or segment) and this triangle (the segment can
     *         be zero-length), or <code>null</code>, if no intersection exists. A segment start or end inside the
     *         triangle is also taken as an intersection.
     */
    public Segment3d getIntersection(Line3d line)
    {
        Point3d intersection = null;

        if (Math.abs(line.v.dot(getNormal())) < MathHelper.EPSILON) {
            // the line is parallel to the triangle's plane

            List<Point3d> intersections = new ArrayList<Point3d>(3);

            intersection = (line instanceof Segment3d) ? s1.getIntersection((Segment3d) line) : s1
                    .getIntersection(line);
            // if an intersection exists and it isn't a whole side intersection, add it to the list of intersections
            // (whole side intersections will be detected by intersections with the other sides)
            if (intersection != null && !Double.isNaN(intersection.x))
                intersections.add(intersection);

            intersection = (line instanceof Segment3d) ? s2.getIntersection((Segment3d) line) : s2
                    .getIntersection(line);
            if (intersection != null && !Double.isNaN(intersection.x))
                intersections.add(intersection);

            intersection = (line instanceof Segment3d) ? s3.getIntersection((Segment3d) line) : s3
                    .getIntersection(line);
            if (intersection != null && !Double.isNaN(intersection.x))
                intersections.add(intersection);

            if (line instanceof Segment3d) {
                // a segment can start or end inside the triangle
                for (Point3d p : ((Segment3d) line).getPoints()) {
                    if (this.contains(p)) {
                        intersections.add(p);
                    }
                }
            }

            MathHelper.removeEpsilonEqualPoints(intersections);

            if (intersections.size() == 2) {
                return new Segment3d(intersections.get(0), intersections.get(1));
            } else if (intersections.size() == 1) {
                return new Segment3d(intersections.get(0), intersections.get(0));
            } else if (intersections.size() == 0) {
                return null;
            } else {
                throw new IllegalStateException("Illegal count of intersections of a line and triangle: "
                        + intersections.size());
            }
        } else {
            // the line isn't parallel to the triangle's plane
            intersection = plane.getIntersection(line);
            // line.contains(...) is being called because the line can be also a Segment3d
            if (intersection != null && this.contains(intersection) && line.contains(intersection)) {
                return new Segment3d(intersection, intersection);
            } else {
                return null;
            }
        }
    }

    /**
     * @return The normalized normal vector to the triangle's plane.
     */
    public Vector3d getNormal()
    {
        Vector3d normal = new Vector3d();
        normal.normalize(plane.getNormal());
        return normal;
    }

    @Override
    public Object clone()
    {
        return new Triangle3d(p1.x, p1.y, p1.z, p2.x, p2.y, p2.z, p3.x, p3.y, p3.z);
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((p1 == null) ? 0 : p1.hashCode());
        result = prime * result + ((p2 == null) ? 0 : p2.hashCode());
        result = prime * result + ((p3 == null) ? 0 : p3.hashCode());
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
        Triangle3d other = (Triangle3d) obj;
        if (p1 == null) {
            if (other.p1 != null)
                return false;
        } else if (!p1.equals(other.p1))
            return false;
        if (p2 == null) {
            if (other.p2 != null)
                return false;
        } else if (!p2.equals(other.p2))
            return false;
        if (p3 == null) {
            if (other.p3 != null)
                return false;
        } else if (!p3.equals(other.p3))
            return false;
        return true;
    }

    /**
     * Return <code>true</code> if the given triangle is almost equal to this one.
     * 
     * @param other The triangle to compare.
     * @return <code>true</code> if the given triangle is almost equal to this one.
     */
    public boolean epsilonEquals(Triangle3d other)
    {
        if (other == null)
            return false;
        if (p1 == null) {
            if (other.p1 != null)
                return false;
        } else if (!p1.epsilonEquals(other.p1, EPSILON))
            return false;
        if (p2 == null) {
            if (other.p2 != null)
                return false;
        } else if (!p2.epsilonEquals(other.p2, EPSILON))
            return false;
        if (p3 == null) {
            if (other.p3 != null)
                return false;
        } else if (!p3.epsilonEquals(other.p3, EPSILON))
            return false;
        return true;
    }

    @Override
    public String toString()
    {
        return "Triangle3d [" + p1 + ", " + p2 + ", " + p3 + "]";
    }

}

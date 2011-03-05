/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.math;

import static cz.cuni.mff.peckam.java.origamist.math.MathHelper.EPSILON;
import static java.lang.Math.abs;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Point3d;
import javax.vecmath.Vector2d;
import javax.vecmath.Vector3d;

/**
 * A rectangle in 3D space.
 * 
 * @author Martin Pecka
 */
public class Rectangle3d implements Cloneable
{
    /** A vertex of the rectangle. */
    protected Point3d p1, p2, p3, p4;

    /** An edge of the rectangle (s_i is the edge between p_(i+1 mod 4) and p_i). */
    protected Segment3d s1, s2, s3, s4;

    /**
     * A halfspace defined by edge s_i, so that the halfspace's border plane is perpendicular to this rectangle's plane.
     */
    protected HalfSpace3d hs1, hs2, hs3, hs4;

    /** The plane the rectangle lies in. */
    protected Plane3d     plane;

    /**
     * Create a rectangle with the given vertices.
     * 
     * @param p1 A vertex.
     * @param p2 A vertex.
     * @param p3 A vertex.
     * @param p4 A vertex.
     * 
     * @throws IllegalArgumentException If the given points don't define a rectangle.
     */
    public Rectangle3d(Point3d p1, Point3d p2, Point3d p3, Point3d p4) throws IllegalArgumentException
    {
        setVertices(p1, p2, p3, p4);
    }

    /**
     * Create a rectangle with the given vertices.
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
     * @param p4x A vertex's x coordinate.
     * @param p4y A vertex's y coordinate.
     * @param p4z A vertex's z coordinate.
     * 
     * @throws IllegalArgumentException If the given points don't define a rectangle.
     */
    public Rectangle3d(double p1x, double p1y, double p1z, double p2x, double p2y, double p2z, double p3x, double p3y,
            double p3z, double p4x, double p4y, double p4z) throws IllegalArgumentException
    {
        this(new Point3d(p1x, p1y, p1z), new Point3d(p2x, p2y, p2z), new Point3d(p3x, p3y, p3z), new Point3d(p4x, p4y,
                p4z));
    }

    /**
     * Compute new values of all the helper fields such as plane, s1, s2, s3, s4 and so...
     * 
     * @throws IllegalArgumentException If the rectangle's points don't define a rectangle.
     */
    protected void recomputeDerivedItems() throws IllegalArgumentException
    {
        plane = new Plane3d(p1, p2, p3);
        if (!plane.contains(p4)) {
            throw new IllegalArgumentException("Trying to construct an invalid rectangle.");
        }

        s1 = new Segment3d(p1, p2);
        s2 = new Segment3d(p2, p3);
        s3 = new Segment3d(p3, p4);
        s4 = new Segment3d(p4, p1);

        if (abs(s1.getVector().dot(s2.getVector())) > EPSILON || abs(s2.getVector().dot(s3.getVector())) > EPSILON
                || abs(s3.getVector().dot(s4.getVector())) > EPSILON
                || abs(s4.getVector().dot(s1.getVector())) > EPSILON) {
            // the edges aren't perpendicular
            throw new IllegalArgumentException("Trying to construct an invalid rectangle.");
        }

        // we don't need to check if opposite sides are equal length - the 90° condition above is sufficient

        hs1 = HalfSpace3d.createPerpendicularToTriangle(p1, p2, p3);
        hs2 = HalfSpace3d.createPerpendicularToTriangle(p2, p3, p4);
        hs3 = HalfSpace3d.createPerpendicularToTriangle(p3, p4, p1);
        hs4 = HalfSpace3d.createPerpendicularToTriangle(p4, p1, p2);
    }

    /**
     * Tell whether this rectangle contains the given point.
     * 
     * @param point The point to check.
     * @return <code>true</code> if this rectangle contains the given point.
     */
    public boolean contains(Point3d point)
    {
        return plane.contains(point) && hs1.contains(point) && hs2.contains(point) && hs3.contains(point)
                && hs4.contains(point);
    }

    /**
     * Return the intersection of the given line and this rectangle.
     * 
     * @param line The line to find intersection with.
     * @return The intersection of the given line and this rectangle. Returns <code>null</code> if no intersection
     *         exists and returns a segment with zero direction vector if the intersection is only one point.
     */
    public Segment3d getIntersection(Line3d line)
    {
        List<Point3d> intList = new ArrayList<Point3d>(2);
        for (Segment3d s : getEdges()) {
            Segment3d intersection = s.getIntersection(line);
            if (intersection != null && intersection.v.epsilonEquals(new Vector3d(), EPSILON))
                intList.add(intersection.p);
            else if (intersection != null) // a whole side intersection, we can return it
                return intersection;
        }

        if (intList.size() == 0)
            return null;

        MathHelper.removeEpsilonEqualPoints(intList);

        Point3d p2 = (intList.size() == 1 ? intList.get(0) : intList.get(1));

        return new Segment3d(intList.get(0), p2);
    }

    /**
     * @return The width and height of the rectangle.
     */
    public Vector2d getDimension()
    {
        return new Vector2d(s1.getLength(), s2.getLength());
    }

    /**
     * @return the p1
     */
    public Point3d getP1()
    {
        return p1;
    }

    /**
     * @return the p2
     */
    public Point3d getP2()
    {
        return p2;
    }

    /**
     * @return the p3
     */
    public Point3d getP3()
    {
        return p3;
    }

    /**
     * @return the p4
     */
    public Point3d getP4()
    {
        return p4;
    }

    /**
     * @return An array of all vertices of this rectangle.
     */
    public Point3d[] getVertices()
    {
        return new Point3d[] { p1, p2, p3, p4 };
    }

    /**
     * Set new vertices of the rectangle.
     * 
     * @param p1 A vertex.
     * @param p2 A vertex.
     * @param p3 A vertex.
     * @param p4 A vertex.
     * 
     * @throws IllegalArgumentException If the given points don't define a rectangle.
     */
    public void setVertices(Point3d p1, Point3d p2, Point3d p3, Point3d p4) throws IllegalArgumentException
    {
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
        this.p4 = p4;

        recomputeDerivedItems();
    }

    /**
     * @return the s1
     */
    public Segment3d getS1()
    {
        return s1;
    }

    /**
     * @return the s2
     */
    public Segment3d getS2()
    {
        return s2;
    }

    /**
     * @return the s3
     */
    public Segment3d getS3()
    {
        return s3;
    }

    /**
     * @return the s4
     */
    public Segment3d getS4()
    {
        return s4;
    }

    /**
     * @return An array of all edges of the rectangle.
     */
    public Segment3d[] getEdges()
    {
        return new Segment3d[] { s1, s2, s3, s4 };
    }

    /**
     * @return the hs1
     */
    public HalfSpace3d getHs1()
    {
        return hs1;
    }

    /**
     * @return the hs2
     */
    public HalfSpace3d getHs2()
    {
        return hs2;
    }

    /**
     * @return the hs3
     */
    public HalfSpace3d getHs3()
    {
        return hs3;
    }

    /**
     * @return the hs4
     */
    public HalfSpace3d getHs4()
    {
        return hs4;
    }

    /**
     * @return the plane
     */
    public Plane3d getPlane()
    {
        return plane;
    }

    @Override
    protected Rectangle3d clone() throws CloneNotSupportedException
    {
        return new Rectangle3d(new Point3d(p1), new Point3d(p2), new Point3d(p3), new Point3d(p4));
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((p1 == null) ? 0 : p1.hashCode());
        result = prime * result + ((p2 == null) ? 0 : p2.hashCode());
        result = prime * result + ((p3 == null) ? 0 : p3.hashCode());
        result = prime * result + ((p4 == null) ? 0 : p4.hashCode());
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
        Rectangle3d other = (Rectangle3d) obj;
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
        if (p4 == null) {
            if (other.p4 != null)
                return false;
        } else if (!p4.equals(other.p4))
            return false;
        return true;
    }

    /**
     * Return <code>true</code> if the given rectangle is almost equal to this one.
     * 
     * @param other The rectangle to compare.
     * @return <code>true</code> if the given rectangle is almost equal to this one.
     */
    public boolean epsilonEquals(Rectangle3d other)
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
        if (p4 == null) {
            if (other.p4 != null)
                return false;
        } else if (!p4.epsilonEquals(other.p4, EPSILON))
            return false;
        return true;
    }

    @Override
    public String toString()
    {
        return "Rectangle3d [p1=" + p1 + ", p2=" + p2 + ", p3=" + p3 + ", p4=" + p4 + "]";
    }
}

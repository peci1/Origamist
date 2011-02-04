/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.math;

import static cz.cuni.mff.peckam.java.origamist.math.MathHelper.EPSILON;

import javax.vecmath.Point2d;
import javax.vecmath.Vector3d;

/**
 * A representation of a 2D triangle.
 * 
 * @author Martin Pecka
 */
public class Triangle2d implements Cloneable
{
    protected Point2d     p1;
    protected Point2d     p2;
    protected Point2d     p3;

    protected HalfPlane2d hp1;
    protected HalfPlane2d hp2;
    protected HalfPlane2d hp3;

    // precomputed values for obtaining barycentric coordinates of points
    protected double      b1;
    protected double      b2;
    protected double      b3;
    protected double      b4;

    /**
     * Create a triangle from the given points.
     * 
     * @param p1 A vertex.
     * @param p2 A vertex.
     * @param p3 A vertex.
     * @throws IllegalArgumentException If the points are colinear.
     */
    public Triangle2d(Point2d p1, Point2d p2, Point2d p3) throws IllegalArgumentException
    {
        setPoints(p1, p2, p3);
    }

    /**
     * Create a triangle from the given points.
     * 
     * @param p1x A vertex's x coordinte.
     * @param p1y A vertex's y coordinte.
     * @param p2x A vertex's x coordinte.
     * @param p2y A vertex's y coordinte.
     * @param p3x A vertex's x coordinte.
     * @param p3y A vertex's y coordinte.
     * @throws IllegalArgumentException If the points are colinear.
     */
    public Triangle2d(double p1x, double p1y, double p2x, double p2y, double p3x, double p3y)
            throws IllegalArgumentException
    {
        this(new Point2d(p1x, p1y), new Point2d(p2x, p2y), new Point2d(p3x, p3y));
    }

    /**
     * Change the points of this triangle. Only non-null points will be changed.
     * 
     * @param p1 A vertex or <code>null</code> if that vertex should be left untouched.
     * @param p2 A vertex or <code>null</code> if that vertex should be left untouched.
     * @param p3 A vertex or <code>null</code> if that vertex should be left untouched.
     * 
     * @throws IllegalArgumentException If the points are colinear.
     */
    public void setPoints(Point2d p1, Point2d p2, Point2d p3) throws IllegalArgumentException
    {
        if (p1 != null)
            this.p1 = p1;
        if (p2 != null)
            this.p2 = p2;
        if (p3 != null)
            this.p3 = p3;

        if (new Line2d(this.p1, this.p2).contains(this.p3)) {
            throw new IllegalArgumentException("Trying to define a triangle by three colinear points.");
        }

        hp1 = new HalfPlane2d(p1, p2, p3);
        hp2 = new HalfPlane2d(p2, p3, p1);
        hp3 = new HalfPlane2d(p3, p1, p2);

        // see http://en.wikipedia.org/wiki/Barycentric_coordinates_%28mathematics%29
        double oneOverDetT = 1.0 / ((this.p1.x - this.p3.x) * (this.p2.y - this.p3.y) - (this.p2.x - this.p3.x)
                * (this.p1.y - this.p3.y));
        b1 = (this.p2.y - this.p3.y) * oneOverDetT;
        b2 = (this.p3.x - this.p2.x) * oneOverDetT;
        b3 = (this.p3.y - this.p1.y) * oneOverDetT;
        b4 = (this.p1.x - this.p3.x) * oneOverDetT;
    }

    /**
     * Return the coordinates of the first point.
     * 
     * @return The coordinates of the first point.
     */
    public Point2d getP1()
    {
        return p1;
    }

    /**
     * Return the coordinates of the second point.
     * 
     * @return The coordinates of the second point.
     */
    public Point2d getP2()
    {
        return p2;
    }

    /**
     * Return the coordinates of the third point.
     * 
     * @return The coordinates of the third point.
     */
    public Point2d getP3()
    {
        return p3;
    }

    /**
     * True if this triangle contains the given point (even on its border).
     * 
     * @param point The point to check.
     * @return Whether the given point lies inside this triangle or not.
     */
    public boolean contains(Point2d point)
    {
        return hp1.contains(point) && hp2.contains(point) && hp3.contains(point);
    }

    /**
     * Returns the barycentric coordinates of the given point in this triangle.
     * 
     * @param p The point to get barycentric coordinates for.
     * @return The barycentric coordinates of the given point.
     */
    public Vector3d getBarycentricCoords(Point2d p)
    {
        Vector3d result = new Vector3d();
        result.x = b1 * (p.x - p3.x) + b2 * (p.y - p3.y);
        result.y = b3 * (p.x - p3.x) + b4 * (p.y - p3.y);
        result.z = 1 - result.x - result.y;
        return result;
    }

    @Override
    public Object clone()
    {
        return new Triangle2d(p1.x, p1.y, p2.x, p2.y, p3.x, p3.y);
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
        Triangle2d other = (Triangle2d) obj;
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
    public boolean epsilonEquals(Triangle2d other)
    {
        if (other == null)
            return false;
        return p1.epsilonEquals(other.p1, EPSILON) && p2.epsilonEquals(other.p2, EPSILON)
                && p3.epsilonEquals(other.p3, EPSILON);
    }

    @Override
    public String toString()
    {
        return "Triangle2d [" + p1 + ", " + p2 + ", " + p3 + "]";
    }

}

/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.math;

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

    public Triangle2d(Point2d p1, Point2d p2, Point2d p3)
    {
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;

        hp1 = new HalfPlane2d(p1, p2, p3);
        hp2 = new HalfPlane2d(p2, p3, p1);
        hp3 = new HalfPlane2d(p3, p1, p2);

        // see http://en.wikipedia.org/wiki/Barycentric_coordinates_%28mathematics%29
        double oneOverDetT = 1.0 / ((p1.x - p3.x) * (p2.y - p3.y) - (p2.x - p3.x) * (p1.y - p3.y));
        b1 = (p2.y - p3.y) * oneOverDetT;
        b2 = (p3.x - p2.x) * oneOverDetT;
        b3 = (p3.y - p1.y) * oneOverDetT;
        b4 = (p1.x - p3.x) * oneOverDetT;
    }

    public Triangle2d(double p1x, double p1y, double p2x, double p2y, double p3x, double p3y)
    {
        this(new Point2d(p1x, p1y), new Point2d(p2x, p2y), new Point2d(p3x, p3y));
    }

    /**
     * Change the points of this triangle. Only non-null points will be changed.
     * 
     * @param p1
     * @param p2
     * @param p3
     */
    public void setPoints(Point2d p1, Point2d p2, Point2d p3)
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
    public String toString()
    {
        return "Triangle2d [" + p1 + ", " + p2 + ", " + p3 + "]";
    }

}

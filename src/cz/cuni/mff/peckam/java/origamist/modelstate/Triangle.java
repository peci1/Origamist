/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.modelstate;

import javax.vecmath.Point3d;

/**
 * A representation of a 3D triangle. Units for the points should be the relative units.
 * 
 * @author Martin Pecka
 */
public class Triangle implements Cloneable
{
    protected Point3d p1;
    protected Point3d p2;
    protected Point3d p3;

    public Triangle(Point3d p1, Point3d p2, Point3d p3)
    {
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
    }

    public Triangle(double p1x, double p1y, double p1z, double p2x, double p2y, double p2z, double p3x, double p3y,
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

    @Override
    protected Object clone()
    {
        return new Triangle(p1.x, p1.y, p1.z, p2.x, p2.y, p2.z, p3.x, p3.y, p3.z);
    }

}

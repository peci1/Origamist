/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.model;

import javax.vecmath.Point2d;

/**
 * A point in 2D space.
 * 
 * @author Martin Pecka
 */
public class Point2D extends cz.cuni.mff.peckam.java.origamist.model.jaxb.Point2D implements Cloneable
{
    public Point2D()
    {

    }

    /**
     * @param point The point to represent.
     */
    public Point2D(Point2d point)
    {
        this.x = point.x;
        this.y = point.y;
    }

    /**
     * @param x The x coordinate.
     * @param y The y coordinate.
     */
    public Point2D(double x, double y)
    {
        this.x = x;
        this.y = y;
    }

    /**
     * @return A copy of this point as {@link Point2d}.
     */
    public Point2d toPoint2d()
    {
        return new Point2d(x, y);
    }

    @Override
    public String toString()
    {
        return "[" + x + ", " + y + "]";
    }

    @Override
    public Point2D clone()
    {
        return new Point2D(x, y);
    }
}

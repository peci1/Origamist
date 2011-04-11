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
public class Point2D extends cz.cuni.mff.peckam.java.origamist.model.jaxb.Point2D
{
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
}

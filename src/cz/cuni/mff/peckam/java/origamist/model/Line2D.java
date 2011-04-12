/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.model;

import cz.cuni.mff.peckam.java.origamist.math.Line2d;
import cz.cuni.mff.peckam.java.origamist.math.Segment2d;

/**
 * A line or segment in 2D space.
 * 
 * @author Martin Pecka
 */
public class Line2D extends cz.cuni.mff.peckam.java.origamist.model.jaxb.Line2D
{
    /**
     * @return A copy of this line as a {@link Line2d}.
     */
    public Line2d toLine2d()
    {
        return new Line2d(start.toPoint2d(), end.toPoint2d());
    }

    /**
     * @return A copy of this line as a {@link Segment2d}.
     */
    public Segment2d toSegment2d()
    {
        return new Segment2d(start.toPoint2d(), end.toPoint2d());
    }

    @Override
    public String toString()
    {
        return "Line2D [start=" + start + ", end=" + end + "]";
    }
}

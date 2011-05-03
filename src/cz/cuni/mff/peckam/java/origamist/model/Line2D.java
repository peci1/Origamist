/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.model;

import javax.vecmath.Point2d;

import cz.cuni.mff.peckam.java.origamist.math.Line2d;
import cz.cuni.mff.peckam.java.origamist.math.Segment2d;

/**
 * A line or segment in 2D space.
 * 
 * @author Martin Pecka
 */
public class Line2D extends cz.cuni.mff.peckam.java.origamist.model.jaxb.Line2D implements Cloneable
{
    public Line2D()
    {

    }

    /**
     * @param line The line to represent.
     */
    public Line2D(Line2d line)
    {
        this.start = new Point2D(line.getPoint());
        this.end = new Point2D(line.getPointForParameter(1));
    }

    /**
     * @param segment The segment to represent.
     */
    public Line2D(Segment2d segment)
    {
        this.start = new Point2D(segment.getP1());
        this.end = new Point2D(segment.getP2());
    }

    /**
     * @param start Start point.
     * @param end End point.
     */
    public Line2D(Point2d start, Point2d end)
    {
        this.start = new Point2D(start);
        this.end = new Point2D(end);
    }

    /**
     * @param start Start point.
     * @param end End point.
     */
    public Line2D(Point2D start, Point2D end)
    {
        this.start = start;
        this.end = end;
    }

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

    @Override
    public Line2D clone()
    {
        return new Line2D(start, end);
    }
}

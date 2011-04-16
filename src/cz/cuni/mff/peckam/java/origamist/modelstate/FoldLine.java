/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.modelstate;

import cz.cuni.mff.peckam.java.origamist.math.Segment2d;
import cz.cuni.mff.peckam.java.origamist.math.Segment3d;

/**
 * A line segment that is a part of a fold.
 * 
 * @author Martin Pecka
 */
public class FoldLine implements Cloneable
{
    /**
     * The line this class holds.
     */
    protected ModelTriangleEdge line      = null;

    /**
     * The direction of the line this class holds.
     */
    protected Direction         direction = null;

    /** The fold this FoldLine is part of. */
    protected Fold              fold      = null;

    /**
     * @return The line this class holds.
     */
    public ModelTriangleEdge getLine()
    {
        return line;
    }

    /**
     * @param line The line this class holds.
     */
    public void setLine(ModelTriangleEdge line)
    {
        this.line = line;
    }

    /**
     * @return The direction of the line this class holds.
     */
    public Direction getDirection()
    {
        return direction;
    }

    /**
     * @param direction The direction of the line this class holds.
     */
    public void setDirection(Direction direction)
    {
        this.direction = direction;
    }

    /**
     * @return The fold this FoldLine is part of.
     */
    public Fold getFold()
    {
        return fold;
    }

    /**
     * @param fold The fold this FoldLine is part of.
     */
    public void setFold(Fold fold)
    {
        this.fold = fold;
    }

    /**
     * @return The 3D representation of this fold line.
     */
    public Segment3d getSegment3d()
    {
        return line.getSegment3d();
    }

    /**
     * @return The 2D representation of this fold line.
     */
    public Segment2d getSegment2d()
    {
        return line.getSegment2d();
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((direction == null) ? 0 : direction.hashCode());
        result = prime * result + ((line == null) ? 0 : line.hashCode());
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
        FoldLine other = (FoldLine) obj;
        if (direction != other.direction)
            return false;
        if (line == null) {
            if (other.line != null)
                return false;
        } else if (!line.equals(other.line))
            return false;
        return true;
    }

    @Override
    public String toString()
    {
        return "FoldLine [line=" + line + ", direction=" + direction + "]";
    }

    @Override
    protected FoldLine clone()
    {
        try {
            FoldLine result = (FoldLine) super.clone();

            result.line = this.line.clone();

            // the fold field remains just copied intentionally

            return result;
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }
}
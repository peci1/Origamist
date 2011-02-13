/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.modelstate;

import cz.cuni.mff.peckam.java.origamist.math.Segment2d;
import cz.cuni.mff.peckam.java.origamist.utils.ObservableList;

/**
 * Represents a fold on the paper.
 * 
 * @author Martin Pecka
 */
public class Fold implements Cloneable
{
    /**
     * The lines this fold consists of.
     */
    protected ObservableList<FoldLine> lines             = new ObservableList<FoldLine>();

    /**
     * Id of the step this fold originated in.
     */
    protected Integer                  originatingStepId = null;

    /**
     * @return the lines this fold consists of
     */
    public ObservableList<FoldLine> getLines()
    {
        return lines;
    }

    /**
     * @return the originatingStepId
     */
    public Integer getOriginatingStepId()
    {
        return originatingStepId;
    }

    @Override
    public Object clone() throws CloneNotSupportedException
    {
        Fold result = (Fold) super.clone();

        result.lines = new ObservableList<FoldLine>();
        for (FoldLine l : lines) {
            FoldLine line = new FoldLine();
            line.line = (Segment2d) l.line.clone();
            line.direction = l.direction;
            result.lines.add(line);
        }

        return result;
    }

    /**
     * Represents a straight fold line.
     * 
     * @author Martin Pecka
     */
    static class FoldLine
    {
        /**
         * The line this class holds
         */
        public Segment2d line      = null;

        /**
         * The direction of the line this class holds
         */
        public Direction direction = null;
    }

}

/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.modelstate;

import java.awt.geom.Line2D;

import cz.cuni.mff.peckam.java.origamist.utils.ChangeNotifyingList;

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
    protected ChangeNotifyingList<FoldLine> lines             = new ChangeNotifyingList<FoldLine>();

    /**
     * Id of the step this fold originated in.
     */
    protected Integer                       originatingStepId = null;

    /**
     * @return the lines this fold consists of
     */
    public ChangeNotifyingList<FoldLine> getLines()
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

        result.lines = new ChangeNotifyingList<FoldLine>();
        for (FoldLine l : lines) {
            FoldLine line = new FoldLine();
            line.line = (Line2D) l.line.clone();
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
    protected class FoldLine
    {
        /**
         * The line this class holds
         */
        public Line2D    line      = null;

        /**
         * The direction of the line this class holds
         */
        public Direction direction = null;
    }

}

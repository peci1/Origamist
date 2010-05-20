/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.modelstate;

import java.awt.geom.Line2D;
import java.util.LinkedList;
import java.util.List;

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
    protected List<FoldLine> lines             = new LinkedList<FoldLine>();

    /**
     * Id of the step this fold originated in.
     */
    protected String         originatingStepId = null;

    @Override
    public Object clone() throws CloneNotSupportedException
    {
        Fold result = (Fold) super.clone();

        result.lines = new LinkedList<FoldLine>();
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

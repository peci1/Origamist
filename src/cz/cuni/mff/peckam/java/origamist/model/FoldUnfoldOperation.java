/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.model;

import javax.vecmath.Point2d;

import cz.cuni.mff.peckam.java.origamist.modelstate.Direction;
import cz.cuni.mff.peckam.java.origamist.modelstate.ModelState;

/**
 * Valley or mountain fold and unfold.
 * 
 * @author Martin Pecka
 */
public class FoldUnfoldOperation extends cz.cuni.mff.peckam.java.origamist.model.jaxb.FoldUnfoldOperation
{
    @Override
    public ModelState getModelState(ModelState previousState)
    {
        switch (this.type) {
            case MOUNTAIN_VALLEY_FOLD_UNFOLD:
                previousState.makeFold(Direction.MOUNTAIN, new Point2d(getLine().getStart().getX(), getLine()
                        .getStart().getY()), new Point2d(getLine().getEnd().getX(), getLine().getEnd().getY()), layer,
                        0);
                break;
            case VALLEY_MOUNTAIN_FOLD_UNFOLD:
                previousState.makeFold(Direction.VALLEY, new Point2d(getLine().getStart().getX(), getLine().getStart()
                        .getY()), new Point2d(getLine().getEnd().getX(), getLine().getEnd().getY()), layer, 0);
                break;
        }
        return previousState;
    }

    @Override
    public String toString()
    {
        return "FoldUnfoldOperation [" + type + ", layer=" + layer + ", line=" + line + "]";
    }
}

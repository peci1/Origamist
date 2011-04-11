/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.model;

import javax.vecmath.Point2d;

import cz.cuni.mff.peckam.java.origamist.modelstate.Direction;
import cz.cuni.mff.peckam.java.origamist.modelstate.ModelState;

/**
 * Valley or mountain fold.
 * 
 * @author Martin Pecka
 */
public class FoldOperation extends cz.cuni.mff.peckam.java.origamist.model.jaxb.FoldOperation
{

    @Override
    public ModelState getModelState(ModelState previousState)
    {
        switch (this.type) {
            case VALLEY_FOLD:
                previousState.makeFold(Direction.VALLEY, new Point2d(getLine().getStart().getX(), getLine().getStart()
                        .getY()), new Point2d(getLine().getEnd().getX(), getLine().getEnd().getY()), layer, angle);
                break;
            case MOUNTAIN_FOLD:
                previousState.makeFold(Direction.MOUNTAIN, new Point2d(getLine().getStart().getX(), getLine()
                        .getStart().getY()), new Point2d(getLine().getEnd().getX(), getLine().getEnd().getY()), layer,
                        angle);
                break;
        }
        return previousState;
    }

    @Override
    public String toString()
    {
        return "FoldOperation [" + type + ", angle=" + angle + ", layer=" + layer + ", line=" + line + "]";
    }
}

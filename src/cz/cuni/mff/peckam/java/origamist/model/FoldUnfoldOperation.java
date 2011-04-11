/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.model;

import cz.cuni.mff.peckam.java.origamist.model.jaxb.Operations;
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
        Direction dir = Direction.MOUNTAIN;
        if (this.type == Operations.VALLEY_MOUNTAIN_FOLD_UNFOLD)
            dir = Direction.VALLEY;

        previousState.makeFold(dir, getLine().getStart().toPoint2d(), getLine().getEnd().toPoint2d(), layer, 0);

        return previousState;
    }

    @Override
    public String toString()
    {
        return "FoldUnfoldOperation [" + type + ", layer=" + layer + ", line=" + line + "]";
    }
}

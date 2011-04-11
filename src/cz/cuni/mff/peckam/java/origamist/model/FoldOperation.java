/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.model;

import cz.cuni.mff.peckam.java.origamist.model.jaxb.Operations;
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
        Direction dir = Direction.MOUNTAIN;
        if (this.type == Operations.VALLEY_FOLD)
            dir = Direction.VALLEY;

        previousState.makeFold(dir, getLine().getStart().toPoint2d(), getLine().getEnd().toPoint2d(), layer, angle);

        return previousState;
    }

    @Override
    public String toString()
    {
        return "FoldOperation [" + type + ", angle=" + angle + ", layer=" + layer + ", line=" + line + "]";
    }
}

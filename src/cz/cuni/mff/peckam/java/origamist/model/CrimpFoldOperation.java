/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.model;

import cz.cuni.mff.peckam.java.origamist.exceptions.InvalidOperationException;
import cz.cuni.mff.peckam.java.origamist.model.jaxb.Operations;
import cz.cuni.mff.peckam.java.origamist.modelstate.Direction;
import cz.cuni.mff.peckam.java.origamist.modelstate.ModelState;

/**
 * Inside or outside crimp fold operation.
 * 
 * This fold basically consists of two reverse folds - one outside and one inside.
 * 
 * @author Martin Pecka
 */
public class CrimpFoldOperation extends cz.cuni.mff.peckam.java.origamist.model.jaxb.CrimpFoldOperation
{

    @Override
    public ModelState getModelState(ModelState previousState) throws InvalidOperationException
    {
        Direction dir = Direction.MOUNTAIN;
        if (this.type == Operations.OUTSIDE_CRIMP_FOLD)
            dir = dir.getOpposite();

        previousState.makeReverseFold(dir, getLine().toSegment2d(), getOppositeLine() != null ? getOppositeLine()
                .toSegment2d() : null, getRefLine().toSegment2d(), layer, oppositeLayer);

        previousState.makeReverseFold(dir.getOpposite(), getSecondLine().toSegment2d(),
                getSecondOppositeLine() != null ? getSecondOppositeLine().toSegment2d() : null, getRefLine()
                        .toSegment2d(), secondLayer, secondOppositeLayer);

        return previousState;
    }

    @Override
    public String toString()
    {
        return "CrimpFoldOperation [type=" + type + ", line=" + line + ", refLine=" + refLine + ", layer=" + layer
                + ", oppositeLine=" + oppositeLine + ", oppositeLayer=" + oppositeLayer + ", secondLine=" + secondLine
                + ", secondLayer=" + secondLayer + ", secondOppositeLine=" + secondOppositeLine
                + ", secondOppositeLayer=" + secondOppositeLayer + "]";
    }
}

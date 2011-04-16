/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.model;

import cz.cuni.mff.peckam.java.origamist.model.jaxb.Operations;
import cz.cuni.mff.peckam.java.origamist.modelstate.Direction;
import cz.cuni.mff.peckam.java.origamist.modelstate.ModelState;

/**
 * Inside or outside reverse fold operation.
 * 
 * @author Martin Pecka
 */
public class ReverseFoldOperation extends cz.cuni.mff.peckam.java.origamist.model.jaxb.ReverseFoldOperation
{
    @Override
    public ModelState getModelState(ModelState previousState)
    {
        Direction dir = Direction.MOUNTAIN;
        if (this.type == Operations.OUTSIDE_REVERSE_FOLD)
            dir = dir.getOpposite();

        previousState.makeReverseFold(dir, getLine().toSegment2d(), getOppositeLine() != null ? getOppositeLine()
                .toSegment2d() : null, getRefLine().toSegment2d(), layer, oppositeLayer);

        return previousState;
    }

    @Override
    public String toString()
    {
        return "ReverseFoldOperation [type=" + type + ", layer=" + layer + ", line=" + line + ", refLine=" + refLine
                + "]";
    }
}

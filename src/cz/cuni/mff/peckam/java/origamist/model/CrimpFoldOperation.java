/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.model;

import java.util.ArrayList;
import java.util.List;

import cz.cuni.mff.peckam.java.origamist.exceptions.InvalidOperationException;
import cz.cuni.mff.peckam.java.origamist.model.jaxb.Operations;
import cz.cuni.mff.peckam.java.origamist.modelstate.Direction;
import cz.cuni.mff.peckam.java.origamist.modelstate.ModelState;
import cz.cuni.mff.peckam.java.origamist.modelstate.arguments.ExistingLineArgument;
import cz.cuni.mff.peckam.java.origamist.modelstate.arguments.LayersArgument;
import cz.cuni.mff.peckam.java.origamist.modelstate.arguments.LineArgument;
import cz.cuni.mff.peckam.java.origamist.modelstate.arguments.OperationArgument;

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
    public boolean areAgrumentsComplete()
    {
        return super.areAgrumentsComplete()
                && (arguments.get(3).isComplete() == arguments.get(4).isComplete() && arguments.get(7).isComplete() == arguments
                        .get(8).isComplete());
    }

    @Override
    protected List<OperationArgument> initArguments()
    {
        List<OperationArgument> result = new ArrayList<OperationArgument>(4);

        LineArgument line;
        result.add(line = new LineArgument(true, "operation.argument.select.line"));
        result.add(new ExistingLineArgument(true, "operation.argument.select.existing.line"));
        result.add(new LayersArgument(line, true, "operation.argument.select.layers"));
        result.add(line = new LineArgument(false, "operation.argument.select.opposite.line"));
        result.add(new LayersArgument(line, false, "operation.argument.select.opposite.layers"));
        result.add(line = new LineArgument(true, "operation.argument.select.second.line"));
        result.add(new LayersArgument(line, true, "operation.argument.select.second.layers"));
        result.add(line = new LineArgument(false, "operation.argument.select.second.opposite.line"));
        result.add(new LayersArgument(line, false, "operation.argument.select.second.opposite.layers"));

        return result;
    }

    @Override
    public void fillFromArguments() throws IllegalStateException
    {
        super.fillFromArguments();
        this.line = ((LineArgument) arguments.get(0)).getLine2D();
        this.refLine = ((ExistingLineArgument) arguments.get(1)).getLine2D();
        this.layer = ((LayersArgument) arguments.get(2)).getLayers();
        if (arguments.get(3).isComplete() && arguments.get(4).isComplete()) {
            this.oppositeLine = ((LineArgument) arguments.get(3)).getLine2D();
            this.oppositeLayer = ((LayersArgument) arguments.get(4)).getLayers();
        }
        this.secondLine = ((LineArgument) arguments.get(5)).getLine2D();
        this.secondLayer = ((LayersArgument) arguments.get(6)).getLayers();
        if (arguments.get(7).isComplete() && arguments.get(8).isComplete()) {
            this.secondOppositeLine = ((LineArgument) arguments.get(7)).getLine2D();
            this.secondOppositeLayer = ((LayersArgument) arguments.get(8)).getLayers();
        }
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

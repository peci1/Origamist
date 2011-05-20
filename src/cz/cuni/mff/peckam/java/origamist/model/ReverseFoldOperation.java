/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.model;

import java.util.ArrayList;
import java.util.List;

import cz.cuni.mff.peckam.java.origamist.model.jaxb.Operations;
import cz.cuni.mff.peckam.java.origamist.modelstate.Direction;
import cz.cuni.mff.peckam.java.origamist.modelstate.LayerFilter;
import cz.cuni.mff.peckam.java.origamist.modelstate.ModelState;
import cz.cuni.mff.peckam.java.origamist.modelstate.arguments.ExistingLineArgument;
import cz.cuni.mff.peckam.java.origamist.modelstate.arguments.LayersArgument;
import cz.cuni.mff.peckam.java.origamist.modelstate.arguments.LineArgument;
import cz.cuni.mff.peckam.java.origamist.modelstate.arguments.OperationArgument;

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
                .toSegment2d() : null, getRefLine().toSegment2d(), new LayerFilter(layer),
                oppositeLayer.size() > 0 ? new LayerFilter(oppositeLayer) : null);

        return previousState;
    }

    @Override
    public boolean areAgrumentsComplete()
    {
        return super.areAgrumentsComplete() && (arguments.get(3).isComplete() == arguments.get(4).isComplete());
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
    }

    @Override
    public String getL7dUserTip(OperationArgument argument)
    {
        String bundleKey = null;
        if (argument == getArguments().get(0)) {
            bundleKey = "reverse.line.user.tip";
        } else if (argument == getArguments().get(1)) {
            bundleKey = "reverse.refLine.user.tip";
        } else if (argument == getArguments().get(2)) {
            bundleKey = "reverse.layers.user.tip";
        } else if (argument == getArguments().get(3)) {
            bundleKey = "reverse.opposite.line.user.tip";
        } else if (argument == getArguments().get(4)) {
            bundleKey = "reverse.opposite.layers.user.tip";
        }

        if (bundleKey != null)
            return messages.getString(bundleKey);
        else
            return null;
    }

    @Override
    public String toString()
    {
        return "ReverseFoldOperation [type=" + type + ", layer=" + layer + ", line=" + line + ", refLine=" + refLine
                + "]";
    }
}

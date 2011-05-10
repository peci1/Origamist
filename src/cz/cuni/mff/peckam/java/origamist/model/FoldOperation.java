/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.model;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Point2d;

import cz.cuni.mff.peckam.java.origamist.model.jaxb.Operations;
import cz.cuni.mff.peckam.java.origamist.modelstate.Direction;
import cz.cuni.mff.peckam.java.origamist.modelstate.ModelState;
import cz.cuni.mff.peckam.java.origamist.modelstate.arguments.AngleArgument;
import cz.cuni.mff.peckam.java.origamist.modelstate.arguments.LayersArgument;
import cz.cuni.mff.peckam.java.origamist.modelstate.arguments.LineArgument;
import cz.cuni.mff.peckam.java.origamist.modelstate.arguments.OperationArgument;
import cz.cuni.mff.peckam.java.origamist.modelstate.arguments.PointArgument;

/**
 * Valley or mountain fold.
 * 
 * @author Martin Pecka
 */
public class FoldOperation extends cz.cuni.mff.peckam.java.origamist.model.jaxb.FoldOperation
{

    @Override
    public ModelState getModelState(ModelState previousState, boolean withDelayed)
    {
        Direction dir = Direction.MOUNTAIN;
        if (this.type == Operations.VALLEY_FOLD)
            dir = dir.getOpposite();

        Point2d refPoint = (getRefPoint() != null ? getRefPoint().toPoint2d() : null);
        previousState.makeFold(dir, getLine().getStart().toPoint2d(), getLine().getEnd().toPoint2d(), refPoint, layer,
                angle, withDelayed);

        return previousState;
    }

    @Override
    protected List<OperationArgument> initArguments()
    {
        List<OperationArgument> result = new ArrayList<OperationArgument>(4);

        LineArgument line;
        result.add(line = new LineArgument(true, "operation.argument.select.line"));
        result.add(new LayersArgument(line, true, "operation.argument.select.layers"));
        result.add(new AngleArgument(true, "operation.argument.angle"));
        result.add(new PointArgument(false, "operation.argument.select.reference.point"));

        return result;
    }

    @Override
    public void fillFromArguments() throws IllegalStateException
    {
        super.fillFromArguments();
        this.line = ((LineArgument) arguments.get(0)).getLine2D();
        this.layer = ((LayersArgument) arguments.get(1)).getLayers();
        this.angle = ((AngleArgument) arguments.get(2)).getAngle();
        if (arguments.get(3).isComplete())
            this.refPoint = ((PointArgument) arguments.get(3)).getPoint2D();
    }

    @Override
    public String toString()
    {
        return "FoldOperation [" + type + ", angle=" + angle + ", layer=" + layer + ", line=" + line + "]";
    }
}

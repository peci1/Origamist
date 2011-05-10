/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.model;

import static cz.cuni.mff.peckam.java.origamist.math.MathHelper.EPSILON;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Point2d;
import javax.vecmath.Vector2d;

import cz.cuni.mff.peckam.java.origamist.exceptions.InvalidOperationException;
import cz.cuni.mff.peckam.java.origamist.math.Segment2d;
import cz.cuni.mff.peckam.java.origamist.modelstate.Direction;
import cz.cuni.mff.peckam.java.origamist.modelstate.ModelState;
import cz.cuni.mff.peckam.java.origamist.modelstate.arguments.AngleArgument;
import cz.cuni.mff.peckam.java.origamist.modelstate.arguments.LayersArgument;
import cz.cuni.mff.peckam.java.origamist.modelstate.arguments.LineArgument;
import cz.cuni.mff.peckam.java.origamist.modelstate.arguments.OperationArgument;
import cz.cuni.mff.peckam.java.origamist.modelstate.arguments.PointArgument;

/**
 * A thunderbolt fold.
 * 
 * This fold consists of two basic folds - one mountain and one valley.
 * 
 * @author Martin Pecka
 */
public class ThunderboltFoldOperation extends cz.cuni.mff.peckam.java.origamist.model.jaxb.ThunderboltFoldOperation
{

    @Override
    public ModelState getModelState(ModelState previousState, boolean withDelayed) throws InvalidOperationException
    {
        Direction dir = Direction.MOUNTAIN;

        if (secondAngle == null)
            secondAngle = angle;

        Segment2d line = getLine().toSegment2d();
        Segment2d secondLine = getSecondLine().toSegment2d();

        Segment2d intPoint = line.getIntersection(secondLine);
        // check if line and secondLine have no common point "inside" common point
        if (intPoint != null) {
            Point2d point = intPoint.getPoint();
            if (!intPoint.getVector().epsilonEquals(new Vector2d(), EPSILON) || !line.isBorderPoint(point)
                    || !secondLine.isBorderPoint(point))
                throw new InvalidOperationException(
                        "<line> and <secondLine> must have either no intersection point or exactly one (and then it"
                                + " has to be a border point of both of them), but their intersection is: "
                                + intPoint.toStringAsIntersection(), this);
        }

        Point2d refPoint = (getRefPoint() != null ? getRefPoint().toPoint2d() : null);
        previousState.makeFold(dir, line.getP1(), line.getP2(), refPoint, layer, angle, withDelayed);

        dir = dir.getOpposite();
        previousState.makeFold(dir, secondLine.getP1(), secondLine.getP2(), refPoint, secondLayer, secondAngle,
                withDelayed);

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
        result.add(line = new LineArgument(true, "operation.argument.select.second.line"));
        result.add(new LayersArgument(line, true, "operation.argument.select.second.layers"));
        result.add(new AngleArgument(false, "operation.argument.second.angle"));

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
        this.secondLine = ((LineArgument) arguments.get(4)).getLine2D();
        this.secondLayer = ((LayersArgument) arguments.get(5)).getLayers();
        if (arguments.get(6).isComplete())
            this.secondAngle = ((AngleArgument) arguments.get(6)).getAngle();
    }

    @Override
    public String toString()
    {
        return "ThunderboltFoldOperation [angle=" + angle + ", line=" + line + ", refPoint=" + refPoint + ", layer="
                + layer + ", secondAngle=" + secondAngle + ", secondLine=" + secondLine + ", secondLayer="
                + secondLayer + "]";
    }
}
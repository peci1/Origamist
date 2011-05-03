/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.model;

import static cz.cuni.mff.peckam.java.origamist.math.MathHelper.EPSILON;

import javax.vecmath.Point2d;
import javax.vecmath.Vector2d;

import cz.cuni.mff.peckam.java.origamist.exceptions.InvalidOperationException;
import cz.cuni.mff.peckam.java.origamist.math.Segment2d;
import cz.cuni.mff.peckam.java.origamist.modelstate.Direction;
import cz.cuni.mff.peckam.java.origamist.modelstate.ModelState;

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
    public ModelState getModelState(ModelState previousState) throws InvalidOperationException
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
        previousState.makeFold(dir, line.getP1(), line.getP2(), refPoint, layer, angle);

        dir = dir.getOpposite();
        previousState.makeFold(dir, secondLine.getP1(), secondLine.getP2(), refPoint, secondLayer, secondAngle);

        return previousState;
    }

    @Override
    public String toString()
    {
        return "ThunderboltFoldOperation [angle=" + angle + ", line=" + line + ", refPoint=" + refPoint + ", layer="
                + layer + ", secondAngle=" + secondAngle + ", secondLine=" + secondLine + ", secondLayer="
                + secondLayer + "]";
    }
}
/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.model;

import static cz.cuni.mff.peckam.java.origamist.math.MathHelper.EPSILON;

import java.util.LinkedHashMap;
import java.util.List;

import javax.vecmath.Vector2d;

import cz.cuni.mff.peckam.java.origamist.exceptions.InvalidOperationException;
import cz.cuni.mff.peckam.java.origamist.math.Line2d;
import cz.cuni.mff.peckam.java.origamist.math.Segment2d;
import cz.cuni.mff.peckam.java.origamist.math.Segment3d;
import cz.cuni.mff.peckam.java.origamist.model.jaxb.Operations;
import cz.cuni.mff.peckam.java.origamist.modelstate.Direction;
import cz.cuni.mff.peckam.java.origamist.modelstate.Layer;
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

        Segment2d line = getLine().toSegment2d();
        Segment2d refLine = getRefLine().toSegment2d();

        // check if line and refLine have exactly one common point
        Line2d intPoint = line.getIntersection(refLine);
        if (intPoint == null || !intPoint.getVector().epsilonEquals(new Vector2d(), EPSILON)) {
            throw new InvalidOperationException(
                    "<line> and <refLine> must intersect in exactly one point, but their intersection is: " + intPoint,
                    this);
        }

        LinkedHashMap<Layer, Segment3d> lineAffectedLayers = new LinkedHashMap<Layer, Segment3d>(layer.size());
        // create the first fold
        previousState.makeFold(dir, getLine().getStart().toPoint2d(), getLine().getEnd().toPoint2d(), layer, 0,
                lineAffectedLayers);

        Segment2d oppositeLine;
        List<Integer> oppositeLayer;

        if (this.oppositeLine == null) {
            // neither oppositeLine nor oppositeLayer were specified, so we must compute them
            oppositeLine = refLine.mirror(line);
            // check if the oppositeLine lies in the paper
            if (!previousState.getOrigami().getModel().getPaper().containsRelative(oppositeLine.getP1())
                    || !previousState.getOrigami().getModel().getPaper().containsRelative(oppositeLine.getP2())) {
                throw new InvalidOperationException(
                        "The opposite side of <line> couldn't be found (the found one doesn't lie on the paper)", this);
            }

            oppositeLayer = previousState
                    .getOppositeLineAffectedLayers(line, oppositeLine, lineAffectedLayers.keySet());

        } else {
            oppositeLine = this.oppositeLine.toSegment2d();
            oppositeLayer = this.oppositeLayer;

            Segment2d intersection = oppositeLine.getIntersection(line);
            if (intersection == null) {
                throw new InvalidOperationException("<line> and <oppositeLine> don't intersect.");
            } else {
                Segment2d intersection2 = refLine.getIntersection(intersection);
                if (intersection2 == null) {
                    throw new InvalidOperationException("<line> and <oppositeLine> don't intersect on <refLine>.");
                } else if (!intersection2.getVector().epsilonEquals(new Vector2d(), EPSILON)) {
                    throw new InvalidOperationException("<line> and <oppositeLine> can't be parallel to <refLine>.");
                }
            }
        }

        LinkedHashMap<Layer, Segment3d> oppositeAffectedLayers = new LinkedHashMap<Layer, Segment3d>(
                oppositeLayer.size());
        // create the opposite fold
        previousState.makeFold(dir.getOpposite(), oppositeLine.getP1(), oppositeLine.getP2(), oppositeLayer, 0,
                oppositeAffectedLayers);

        // bend the paper
        previousState.bendReverseFold(dir, line, oppositeLine, refLine, lineAffectedLayers, oppositeAffectedLayers);

        return previousState;
    }

    @Override
    public String toString()
    {
        return "ReverseFoldOperation [type=" + type + ", layer=" + layer + ", line=" + line + ", refLine=" + refLine
                + "]";
    }
}

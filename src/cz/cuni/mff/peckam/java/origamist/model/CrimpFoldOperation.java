/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.model;

import static cz.cuni.mff.peckam.java.origamist.math.MathHelper.EPSILON;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.vecmath.Point2d;
import javax.vecmath.Vector3d;

import cz.cuni.mff.peckam.java.origamist.exceptions.InvalidOperationException;
import cz.cuni.mff.peckam.java.origamist.math.Line2d;
import cz.cuni.mff.peckam.java.origamist.math.Segment2d;
import cz.cuni.mff.peckam.java.origamist.math.Segment3d;
import cz.cuni.mff.peckam.java.origamist.model.jaxb.Operations;
import cz.cuni.mff.peckam.java.origamist.modelstate.Direction;
import cz.cuni.mff.peckam.java.origamist.modelstate.Layer;
import cz.cuni.mff.peckam.java.origamist.modelstate.LayerFilter;
import cz.cuni.mff.peckam.java.origamist.modelstate.ModelPoint;
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
public class CrimpFoldOperation extends cz.cuni.mff.peckam.java.origamist.model.jaxb.CrimpFoldOperation implements
        HasSymmetricOperation
{

    /** P1 is the center of rotation segment, P2 is the furthest rotated point in the last getModelState() call. */
    protected Segment3d markerPosition = null;

    @Override
    public ModelState getModelState(ModelState previousState) throws InvalidOperationException
    {
        Direction dir = Direction.MOUNTAIN;
        if (this.type == Operations.OUTSIDE_CRIMP_FOLD)
            dir = dir.getOpposite();

        Segment2d newRefLine = new Segment2d(new Point2d(), new Point2d());

        previousState.setFurthestRotationSegment(getLine().toSegment2d());

        List<Map<Layer, Layer>> layersToBend = previousState.makeReverseFold(dir, getLine().toSegment2d(),
                getOppositeLine() != null ? getOppositeLine().toSegment2d() : null, getRefLine().toSegment2d(),
                new LayerFilter(layer), oppositeLayer.size() > 0 ? new LayerFilter(oppositeLayer) : null, newRefLine);

        if (layersToBend.size() == 0 || layersToBend.get(0).size() == 0)
            throw new InvalidOperationException("crimp.fold.no.second.layers.found");

        {// if we rotated the layers so that they face the screen by their opposite sides, we don't want to change the
         // fold direction because the change will be made simply by the rotation of the layers
            Entry<Layer, Layer> anyLayer = layersToBend.get(0).entrySet().iterator().next();
            Vector3d normalBeforeRotation = anyLayer.getValue().getNormal();
            Vector3d normalAfterRotation = anyLayer.getKey().getNormal();
            double angleToScreenBefore = previousState.getScreenNormal().angle(normalBeforeRotation);
            double angleToScreenAfter = previousState.getScreenNormal().angle(normalAfterRotation);
            if ((angleToScreenBefore >= Math.PI / 2d + EPSILON) == (angleToScreenAfter >= Math.PI / 2d + EPSILON))
                dir = dir.getOpposite();
        }

        previousState.makeReverseFold(dir, getSecondLine().toSegment2d(),
                getSecondOppositeLine() != null ? getSecondOppositeLine().toSegment2d() : null, newRefLine,
                new LayerFilter(layersToBend.get(0).keySet()), new LayerFilter(layersToBend.get(1).keySet()));

        ModelPoint furthest = previousState.getFurthestRotatedPointAroundSegment();
        if (furthest != null)
            markerPosition = new Segment3d(furthest, previousState.getFurthestRotationSegment().getNearestPoint(
                    furthest));
        previousState.clearFurthestRotationSegment();

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
        result.add(line = new LineArgument(true, "operation.argument.select.second.line"));
        result.add(line = new LineArgument(false, "operation.argument.select.second.opposite.line"));

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
        if (arguments.get(6).isComplete()) {
            this.secondOppositeLine = ((LineArgument) arguments.get(6)).getLine2D();
        }
    }

    @Override
    public Segment3d getMarkerPosition()
    {
        return markerPosition;
    }

    @Override
    public String getL7dUserTip(OperationArgument argument)
    {
        String bundleKey = null;
        if (argument == getArguments().get(0)) {
            bundleKey = "crimp.line.user.tip";
        } else if (argument == getArguments().get(1)) {
            bundleKey = "reverse.refLine.user.tip";
        } else if (argument == getArguments().get(2)) {
            bundleKey = "crimp.layers.user.tip";
        } else if (argument == getArguments().get(3)) {
            bundleKey = "crimp.opposite.line.user.tip";
        } else if (argument == getArguments().get(4)) {
            bundleKey = "crimp.opposite.layers.user.tip";
        } else if (argument == getArguments().get(5)) {
            bundleKey = "crimp.second.line.user.tip";
        } else if (argument == getArguments().get(6)) {
            bundleKey = "crimp.second.opposite.line.user.tip";
        }

        if (bundleKey != null)
            return messages.getString(bundleKey);
        else
            return null;
    }

    @Override
    public String toString()
    {
        return "CrimpFoldOperation [type=" + type + ", line=" + line + ", refLine=" + refLine + ", layer=" + layer
                + ", oppositeLine=" + oppositeLine + ", oppositeLayer=" + oppositeLayer + ", secondLine=" + secondLine
                + ", secondOppositeLine=" + secondOppositeLine + "]";
    }

    @Override
    public Operation getSymmetricOperation(Line2d symmetryAxis)
    {
        CrimpFoldOperation result = new CrimpFoldOperation();

        result.type = this.type;
        result.line = new Line2D(symmetryAxis.mirror(this.line.toLine2d()));
        result.layer = new LinkedList<Integer>(this.layer);
        // TODO the mirrored refline doesn't work; need to take a shortened refline from the main vertex of this fold
        result.refLine = new Line2D(symmetryAxis.mirror(this.refLine.toLine2d()));
        if (oppositeLine != null)
            result.oppositeLine = new Line2D(symmetryAxis.mirror(this.oppositeLine.toLine2d()));
        if (oppositeLayer != null)
            result.oppositeLayer = new LinkedList<Integer>(this.oppositeLayer);
        result.secondLine = new Line2D(symmetryAxis.mirror(this.secondLine.toLine2d()));
        if (secondOppositeLine != null)
            result.secondOppositeLine = new Line2D(symmetryAxis.mirror(this.secondOppositeLine.toLine2d()));
        if (secondOppositeLayer != null)
            result.secondOppositeLayer = new LinkedList<Integer>(this.secondOppositeLayer);

        return result;
    }
}
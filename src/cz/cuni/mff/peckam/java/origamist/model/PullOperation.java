/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cz.cuni.mff.peckam.java.origamist.math.Segment3d;
import cz.cuni.mff.peckam.java.origamist.model.jaxb.Operations;
import cz.cuni.mff.peckam.java.origamist.modelstate.Layer;
import cz.cuni.mff.peckam.java.origamist.modelstate.LayerFilter;
import cz.cuni.mff.peckam.java.origamist.modelstate.ModelPoint;
import cz.cuni.mff.peckam.java.origamist.modelstate.ModelState;
import cz.cuni.mff.peckam.java.origamist.modelstate.arguments.ExistingLinesArgument;
import cz.cuni.mff.peckam.java.origamist.modelstate.arguments.LayersArgument;
import cz.cuni.mff.peckam.java.origamist.modelstate.arguments.LineArgument;
import cz.cuni.mff.peckam.java.origamist.modelstate.arguments.OperationArgument;
import cz.cuni.mff.peckam.java.origamist.modelstate.arguments.PointArgument;

/**
 * Pull some existing folds.
 * 
 * @author Martin Pecka
 */
public class PullOperation extends cz.cuni.mff.peckam.java.origamist.model.jaxb.PullOperation
{
    /** P1 is the center of rotation segment, P2 is the furthest rotated point in the last getModelState() call. */
    protected Segment3d markerPosition = null;

    @Override
    protected void init()
    {
        super.init();
        type = Operations.PULL; // it doesn't help to specify the value as fixed in XSD
    }

    @Override
    public ModelState getModelState(ModelState previousState)
    {
        Map<Layer, Layer> layers = null;

        if (getLine().size() > 0)
            previousState.setFurthestRotationSegment(getLine().get(0).toSegment2d());

        for (Line2D line : this.line) {
            layers = previousState.pullFold(line.getStart().toPoint2d(), line.getEnd().toPoint2d(),
                    refPoint.toPoint2d(), layers != null ? new LayerFilter(layers.keySet()) : new LayerFilter(layer));
        }

        ModelPoint furthest = previousState.getFurthestRotatedPointAroundSegment();
        if (furthest != null)
            markerPosition = new Segment3d(furthest, previousState.getFurthestRotationSegment().getNearestPoint(
                    furthest));
        previousState.clearFurthestRotationSegment();

        return previousState;
    }

    @Override
    protected List<OperationArgument> initArguments()
    {
        List<OperationArgument> result = new ArrayList<OperationArgument>(3);

        LineArgument line;
        result.add(line = new ExistingLinesArgument(true, "operation.argument.select.existing.lines"));
        result.add(new LayersArgument(line, true, "operation.argument.select.layers"));
        result.add(new PointArgument(true, "operation.argument.select.reference.point"));

        return result;
    }

    @Override
    public void fillFromArguments() throws IllegalStateException
    {
        super.fillFromArguments();
        this.line = ((ExistingLinesArgument) arguments.get(0)).getLines();
        this.layer = ((LayersArgument) arguments.get(1)).getLayers();
        this.refPoint = ((PointArgument) arguments.get(2)).getPoint2D();
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
            bundleKey = "pull.lines.user.tip";
        } else if (argument == getArguments().get(1)) {
            bundleKey = "pull.layers.user.tip";
        } else if (argument == getArguments().get(2)) {
            bundleKey = "pull.refPoint.user.tip";
        }

        if (bundleKey != null)
            return messages.getString(bundleKey);
        else
            return null;
    }

    @Override
    public String toString()
    {
        return "PullOperation [line=" + line + ", layer=" + layer + ", refPoint=" + refPoint + "]";
    }
}

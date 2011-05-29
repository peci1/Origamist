/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import cz.cuni.mff.peckam.java.origamist.math.Line2d;
import cz.cuni.mff.peckam.java.origamist.math.Segment3d;
import cz.cuni.mff.peckam.java.origamist.model.jaxb.Operations;
import cz.cuni.mff.peckam.java.origamist.modelstate.Direction;
import cz.cuni.mff.peckam.java.origamist.modelstate.LayerFilter;
import cz.cuni.mff.peckam.java.origamist.modelstate.ModelPoint;
import cz.cuni.mff.peckam.java.origamist.modelstate.ModelSegment;
import cz.cuni.mff.peckam.java.origamist.modelstate.ModelState;
import cz.cuni.mff.peckam.java.origamist.modelstate.arguments.LayersArgument;
import cz.cuni.mff.peckam.java.origamist.modelstate.arguments.LineArgument;
import cz.cuni.mff.peckam.java.origamist.modelstate.arguments.OperationArgument;

/**
 * Valley or mountain fold and unfold.
 * 
 * @author Martin Pecka
 */
public class FoldUnfoldOperation extends cz.cuni.mff.peckam.java.origamist.model.jaxb.FoldUnfoldOperation implements
        HasSymmetricOperation
{
    /** P1 is the center of rotation segment, P2 is the furthest rotated point in the last getModelState() call. */
    protected Segment3d markerPosition = null;

    @Override
    public ModelState getModelState(ModelState previousState)
    {
        Direction dir = Direction.MOUNTAIN;
        if (this.type == Operations.VALLEY_MOUNTAIN_FOLD_UNFOLD)
            dir = dir.getOpposite();

        previousState.setFurthestRotationSegment(getLine().toSegment2d());

        previousState.makeFold(dir, getLine().getStart().toPoint2d(), getLine().getEnd().toPoint2d(), null,
                new LayerFilter(layer), 0);

        // TODO doesn't work because bendPaper() is skipped

        ModelPoint furthest = previousState.getFurthestRotatedPointAroundSegment();
        if (furthest != null)
            markerPosition = new Segment3d(furthest, previousState.getFurthestRotationSegment().getNearestPoint(
                    furthest));
        previousState.clearFurthestRotationSegment();

        // workaround for bendPaper() not bending paper for 0° angle
        ModelSegment seg = new ModelSegment(new Segment3d(previousState.locatePointFromPaperTo3D(getLine().getStart()
                .toPoint2d()), previousState.locatePointFromPaperTo3D(getLine().getEnd().toPoint2d())), getLine()
                .toSegment2d());
        Vector3d normal = previousState.getSegmentNormal(seg);
        Vector3d cross = new Vector3d();
        cross.cross(seg.getVector(), normal);
        cross.normalize();
        cross.scale(0.15);
        Point3d end = seg.getPointForParameter(0.5);
        Point3d start = new Point3d(end);
        start.sub(cross);
        markerPosition = new Segment3d(start, end);

        return previousState;
    }

    @Override
    protected List<OperationArgument> initArguments()
    {
        List<OperationArgument> result = new ArrayList<OperationArgument>(3);

        LineArgument line;
        result.add(line = new LineArgument(true, "operation.argument.select.line"));
        result.add(new LayersArgument(line, true, "operation.argument.select.layers"));

        return result;
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
            bundleKey = "fold.line.user.tip";
        } else if (argument == getArguments().get(1)) {
            bundleKey = "fold.layers.user.tip";
        }

        if (bundleKey != null)
            return messages.getString(bundleKey);
        else
            return null;
    }

    @Override
    public void fillFromArguments() throws IllegalStateException
    {
        super.fillFromArguments();
        this.line = ((LineArgument) arguments.get(0)).getLine2D();
        this.layer = ((LayersArgument) arguments.get(1)).getLayers();
    }

    @Override
    public String toString()
    {
        return "FoldUnfoldOperation [" + type + ", layer=" + layer + ", line=" + line + "]";
    }

    @Override
    public Operation getSymmetricOperation(Line2d symmetryAxis)
    {
        FoldUnfoldOperation result = new FoldUnfoldOperation();

        result.type = this.type;
        result.line = new Line2D(symmetryAxis.mirror(this.line.toLine2d()));
        result.layer = new LinkedList<Integer>(this.layer);

        return result;
    }
}

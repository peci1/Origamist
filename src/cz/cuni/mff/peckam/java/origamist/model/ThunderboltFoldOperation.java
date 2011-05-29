/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.model;

import static cz.cuni.mff.peckam.java.origamist.math.MathHelper.EPSILON;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.vecmath.Point2d;
import javax.vecmath.Vector2d;
import javax.vecmath.Vector3d;

import cz.cuni.mff.peckam.java.origamist.exceptions.InvalidOperationException;
import cz.cuni.mff.peckam.java.origamist.math.AngleUnit;
import cz.cuni.mff.peckam.java.origamist.math.Line2d;
import cz.cuni.mff.peckam.java.origamist.math.Segment2d;
import cz.cuni.mff.peckam.java.origamist.math.Segment3d;
import cz.cuni.mff.peckam.java.origamist.modelstate.Direction;
import cz.cuni.mff.peckam.java.origamist.modelstate.Layer;
import cz.cuni.mff.peckam.java.origamist.modelstate.LayerFilter;
import cz.cuni.mff.peckam.java.origamist.modelstate.ModelPoint;
import cz.cuni.mff.peckam.java.origamist.modelstate.ModelState;
import cz.cuni.mff.peckam.java.origamist.modelstate.arguments.AngleArgument;
import cz.cuni.mff.peckam.java.origamist.modelstate.arguments.LayersArgument;
import cz.cuni.mff.peckam.java.origamist.modelstate.arguments.LineArgument;
import cz.cuni.mff.peckam.java.origamist.modelstate.arguments.OperationArgument;
import cz.cuni.mff.peckam.java.origamist.services.ServiceLocator;
import cz.cuni.mff.peckam.java.origamist.services.interfaces.ConfigurationManager;

/**
 * A thunderbolt fold.
 * 
 * This fold consists of two basic folds - one mountain and one valley.
 * 
 * @author Martin Pecka
 */
public class ThunderboltFoldOperation extends cz.cuni.mff.peckam.java.origamist.model.jaxb.ThunderboltFoldOperation
        implements HasSymmetricOperation
{

    /** P1 is the center of rotation segment, P2 is the furthest rotated point in the last getModelState() call. */
    protected Segment3d markerPosition = null;

    @Override
    public ModelState getModelState(ModelState previousState) throws InvalidOperationException
    {
        Direction dir = Direction.MOUNTAIN;
        if (invert != null && invert == true)
            dir = dir.getOpposite();

        if (secondAngle == null)
            secondAngle = angle;

        Segment2d line = getLine().toSegment2d();
        Segment2d secondLine = getSecondLine().toSegment2d();

        Point2d refPoint = secondLine.getP1();
        if (line.isBorderPoint(refPoint))
            refPoint = secondLine.getP2();

        Segment2d intPoint = line.getIntersection(secondLine);
        // check if line and secondLine have no "inside" common point
        if (intPoint != null) {
            Point2d point = intPoint.getPoint();
            if (!intPoint.getVector().epsilonEquals(new Vector2d(), EPSILON) || !line.isBorderPoint(point)
                    || !secondLine.isBorderPoint(point))
                throw new InvalidOperationException(
                        "<line> and <secondLine> must have either no intersection point or exactly one (and then it"
                                + " has to be a border point of both of them), but their intersection is: "
                                + intPoint.toStringAsIntersection(), this);
        }

        previousState.setFurthestRotationSegment(getLine().toSegment2d());

        Map<Layer, Layer> layersToBend = previousState.makeFold(dir, line.getP1(), line.getP2(), refPoint,
                new LayerFilter(layer), angle);

        {// if we rotated the layers so that they face the screen by their opposite sides, we don't want to change the
         // fold direction because the change will be made simply by the rotation of the layers
            Entry<Layer, Layer> anyLayer = layersToBend.entrySet().iterator().next();
            Vector3d normalBeforeRotation = anyLayer.getValue().getNormal();
            Vector3d normalAfterRotation = anyLayer.getKey().getNormal();
            double angleToScreenBefore = previousState.getScreenNormal().angle(normalBeforeRotation);
            double angleToScreenAfter = previousState.getScreenNormal().angle(normalAfterRotation);
            if ((angleToScreenBefore >= Math.PI / 2d + EPSILON) == (angleToScreenAfter >= Math.PI / 2d + EPSILON))
                dir = dir.getOpposite();
        }

        {// find the second refPoint - try to prolong the line created by some of border points of line and secondLine
            refPoint = null;
            out: for (Point2d p1 : line.getPoints()) {
                for (Point2d p2 : secondLine.getPoints()) {
                    Segment2d seg = new Segment2d(p1, p2);
                    if (!seg.getVector().epsilonEquals(new Vector2d(), EPSILON)) {
                        Point2d newRefPoint = seg.getPointForParameter(1 + 10000d * EPSILON);
                        if (previousState.getOrigami().getModel().getPaper().containsRelative(newRefPoint)) {
                            refPoint = newRefPoint;
                            break out;
                        }
                    }
                }
            }
        }

        previousState.makeFold(dir, secondLine.getP1(), secondLine.getP2(), refPoint,
                new LayerFilter(layersToBend.keySet()), secondAngle);

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
        List<OperationArgument> result = new ArrayList<OperationArgument>(5);

        LineArgument line;
        result.add(line = new LineArgument(true, "operation.argument.select.line"));
        result.add(new LayersArgument(line, true, "operation.argument.select.layers"));
        result.add(new AngleArgument(true, "operation.argument.angle", 0d, Math.PI, AngleUnit.RAD));
        result.add(line = new LineArgument(true, "operation.argument.select.second.line"));
        result.add(new AngleArgument(false, "operation.argument.second.angle", 0d, Math.PI, AngleUnit.RAD));

        return result;
    }

    @Override
    public void fillFromArguments() throws IllegalStateException
    {
        super.fillFromArguments();
        this.line = ((LineArgument) arguments.get(0)).getLine2D();
        this.layer = ((LayersArgument) arguments.get(1)).getLayers();
        this.angle = ((AngleArgument) arguments.get(2)).getAngle();
        this.secondLine = ((LineArgument) arguments.get(3)).getLine2D();
        if (arguments.get(4).isComplete())
            this.secondAngle = ((AngleArgument) arguments.get(4)).getAngle();
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
            bundleKey = "thunder.line.user.tip";
        } else if (argument == getArguments().get(1)) {
            bundleKey = "thunder.layers.user.tip";
        } else if (argument == getArguments().get(2)) {
            bundleKey = "thunder.angle.user.tip";
        } else if (argument == getArguments().get(3)) {
            bundleKey = "thunder.second.line.user.tip";
        } else if (argument == getArguments().get(4)) {
            bundleKey = "thunder.second.angle.user.tip";
        }

        if (bundleKey != null)
            return messages.getString(bundleKey);
        else
            return null;
    }

    @Override
    public String getDefaultDescription()
    {
        String prefix = type.toString();
        StringBuilder text = new StringBuilder("<html><body><dl style=\"margin:0px;\"><dt>").append(getL7dName())
                .append("</dt>");

        AngleUnit prefUnit = ServiceLocator.get(ConfigurationManager.class).get().getPreferredAngleUnit();

        text.append("<dd style=\"margin-left: 10px;\">");
        if (invert == null || !invert)
            text.append(messages.getString(prefix + ".firstMountain"));
        else
            text.append(messages.getString(prefix + ".firstValley"));
        text.append("</dd>");

        text.append("<dd style=\"margin-left: 10px;\">");
        String angleText = prefUnit.formatValue(AngleUnit.RAD.convertTo(angle, prefUnit));
        text.append(MessageFormat.format(messages.getString(prefix + ".angleFormat"), new Object[] { angleText }));
        text.append("</dd>");

        text.append("<dd style=\"margin-left: 10px;\">");
        if (invert == null || !invert)
            text.append(messages.getString(prefix + ".secondValley"));
        else
            text.append(messages.getString(prefix + ".secondMountain"));
        text.append("</dd>");

        text.append("<dd style=\"margin-left: 10px;\">");
        angleText = prefUnit.formatValue(AngleUnit.RAD.convertTo(secondAngle != null ? secondAngle : angle, prefUnit));
        text.append(MessageFormat.format(messages.getString(prefix + ".secondAngleFormat"), new Object[] { angleText }));
        text.append("</dd>");

        text.append("</dl></body></html>");
        return text.toString();
    }

    @Override
    public String toString()
    {
        return "ThunderboltFoldOperation [angle=" + angle + ", line=" + line + ", layer=" + layer + ", secondAngle="
                + secondAngle + ", secondLine=" + secondLine + "]";
    }

    @Override
    public Operation getSymmetricOperation(Line2d symmetryAxis)
    {
        ThunderboltFoldOperation result = new ThunderboltFoldOperation();
        result.type = this.type;

        result.line = new Line2D(symmetryAxis.mirror(this.line.toLine2d()));
        result.layer = new LinkedList<Integer>(this.layer);
        result.angle = this.angle;
        result.secondLine = new Line2D(symmetryAxis.mirror(this.secondLine.toLine2d()));
        result.secondAngle = this.secondAngle;
        result.invert = this.invert;

        return result;
    }
}
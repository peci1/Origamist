/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.modelstate;

import java.awt.geom.Line2D;

import javax.media.j3d.TriangleArray;
import javax.vecmath.Point2d;
import javax.vecmath.Point3d;

import cz.cuni.mff.peckam.java.origamist.model.Origami;
import cz.cuni.mff.peckam.java.origamist.model.UnitDimension;
import cz.cuni.mff.peckam.java.origamist.model.UnitHelper;
import cz.cuni.mff.peckam.java.origamist.model.jaxb.Unit;
import cz.cuni.mff.peckam.java.origamist.modelstate.Fold.FoldLine;

/**
 * This model state will be used by the first step as it's "predecessor's" model state.
 * 
 * @author Martin Pecka
 */
public class DefaultModelState extends ModelState
{

    /**
     * Create a default model state for the given origami diagram.
     * 
     * @param o The origami to create the default state for.
     */
    public DefaultModelState(Origami o)
    {
        UnitDimension paperSize = (UnitDimension) o.getModel().getPaper().getSize();
        double halfWidth = UnitHelper.convertTo(paperSize.getUnit(), Unit.M, paperSize.getWidth()) / 2.0;
        double halfHeight = UnitHelper.convertTo(paperSize.getUnit(), Unit.M, paperSize.getHeight()) / 2.0;

        Point2d ul = new Point2d(-halfWidth, halfHeight);
        Point2d ur = new Point2d(halfWidth, halfHeight);
        Point2d dl = new Point2d(-halfWidth, -halfHeight);
        Point2d dr = new Point2d(halfWidth, -halfHeight);

        triangleArray = new TriangleArray(6, TriangleArray.COORDINATES);
        triangleArray.setCoordinate(0, new Point3d(dl.x, dl.y, 0));
        triangleArray.setCoordinate(1, new Point3d(dr.x, dr.y, 0));
        triangleArray.setCoordinate(2, new Point3d(ul.x, ul.y, 0));
        triangleArray.setCoordinate(3, new Point3d(ur.x, ur.y, 0));
        triangleArray.setCoordinate(4, new Point3d(ul.x, ul.y, 0));
        triangleArray.setCoordinate(5, new Point3d(dr.x, dr.y, 0));

        Fold fold = new Fold();

        FoldLine line = fold.new FoldLine();
        line.direction = null;
        line.line = new Line2D.Double(ul.x, ul.y, ur.x, ur.y);
        fold.lines.add(line);

        line = fold.new FoldLine();
        line.direction = null;
        line.line = new Line2D.Double(ur.x, ur.y, dr.x, dr.y);
        fold.lines.add(line);

        line = fold.new FoldLine();
        line.direction = null;
        line.line = new Line2D.Double(dr.x, dr.y, dl.x, dl.y);
        fold.lines.add(line);

        line = fold.new FoldLine();
        line.direction = null;
        line.line = new Line2D.Double(dl.x, dl.y, ul.x, ul.y);
        fold.lines.add(line);

        fold.originatingStepId = 0;
        folds.add(fold);
    }

}

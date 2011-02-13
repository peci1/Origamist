/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.modelstate;

import javax.vecmath.Point2d;

import cz.cuni.mff.peckam.java.origamist.math.Segment2d;
import cz.cuni.mff.peckam.java.origamist.math.Triangle2d;
import cz.cuni.mff.peckam.java.origamist.model.DoubleDimension;
import cz.cuni.mff.peckam.java.origamist.model.Origami;
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
        this.origami = o;

        DoubleDimension dim = o.getModel().getPaper().getRelativeDimensions();
        double width = dim.getWidth();
        double height = dim.getHeight();

        Point2d ul = new Point2d(0, height);
        Point2d ur = new Point2d(width, height);
        Point2d dl = new Point2d(0, 0);
        Point2d dr = new Point2d(width, 0);

        triangles.add(new ModelTriangle(dl.x, dl.y, 0, dr.x, dr.y, 0, ul.x, ul.y, 0, new Triangle2d(dl.x, dl.y, dr.x,
                dr.y, ul.x, ul.y)));
        triangles.add(new ModelTriangle(ur.x, ur.y, 0, ul.x, ul.y, 0, dr.x, dr.y, 0, new Triangle2d(ur.x, ur.y, ul.x,
                ul.y, dr.x, dr.y)));
        layers.add(new Layer(triangles));

        Fold fold = new Fold();

        FoldLine line = new FoldLine();
        line.direction = null;
        line.line = new Segment2d(ul, ur);
        fold.lines.add(line);

        line = new FoldLine();
        line.direction = null;
        line.line = new Segment2d(ur, dr);
        fold.lines.add(line);

        line = new FoldLine();
        line.direction = null;
        line.line = new Segment2d(dr, dl);
        fold.lines.add(line);

        line = new FoldLine();
        line.direction = null;
        line.line = new Segment2d(dl, ul);
        fold.lines.add(line);

        fold.originatingStepId = 0;
        folds.add(fold);
    }
}

/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.modelstate;

import java.awt.geom.Line2D;

import javax.media.j3d.TriangleArray;
import javax.vecmath.Point3d;

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
        // TODO remove testing stuff
        Fold fold = new Fold();
        FoldLine line = fold.new FoldLine();
        line.direction = Direction.VALLEY;
        line.line = new Line2D.Double(0, 0, 0.5, 0.5);
        fold.lines.add(line);
        fold.originatingStepId = "0";
        folds.add(fold);

        triangleArray = new TriangleArray(6, TriangleArray.COORDINATES);
        triangleArray.setCoordinate(0, new Point3d(-0.5, -0.5, 0));
        triangleArray.setCoordinate(1, new Point3d(0.5, -0.5, 0));
        triangleArray.setCoordinate(2, new Point3d(-0.5, 0.5, 0));
        triangleArray.setCoordinate(3, new Point3d(0.5, 0.5, 0));
        triangleArray.setCoordinate(4, new Point3d(-0.5, 0.5, 0));
        triangleArray.setCoordinate(5, new Point3d(0.5, -0.5, 0));
    }

}

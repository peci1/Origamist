/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.modelstate;

import java.util.LinkedList;
import java.util.concurrent.Callable;

import javax.vecmath.Point2d;

import org.apache.log4j.Logger;

import cz.cuni.mff.peckam.java.origamist.math.Triangle2d;
import cz.cuni.mff.peckam.java.origamist.model.DoubleDimension;
import cz.cuni.mff.peckam.java.origamist.model.Origami;

/**
 * This model state will be used by the first step as it's "predecessor's" model state.
 * 
 * @author Martin Pecka
 */
public class DefaultModelState extends ModelState
{

    /**
     * The origami supplied to the constructor doesn't have to be completely loaded, so we rather initialize this step
     * the latest time we can.
     */
    protected Callable<Void> loadData = new Callable<Void>() {
                                          @Override
                                          public Void call() throws Exception
                                          {
                                              Origami o = origami;

                                              DoubleDimension dim = o.getModel().getPaper().getRelativeDimensions();
                                              double width = dim.getWidth();
                                              double height = dim.getHeight();

                                              Point2d ul = new Point2d(0, height);
                                              Point2d ur = new Point2d(width, height);
                                              Point2d dl = new Point2d(0, 0);
                                              Point2d dr = new Point2d(width, 0);

                                              ModelTriangle mt1 = new ModelTriangle(dl.x, dl.y, 0, dr.x, dr.y, 0, ul.x,
                                                      ul.y, 0, new Triangle2d(dl.x, dl.y, dr.x, dr.y, ul.x, ul.y));
                                              triangles.add(mt1);

                                              ModelTriangle mt2 = new ModelTriangle(ur.x, ur.y, 0, ul.x, ul.y, 0, dr.x,
                                                      dr.y, 0, new Triangle2d(ur.x, ur.y, ul.x, ul.y, dr.x, dr.y));
                                              triangles.add(mt2);

                                              mt1.getRawNeighbors().add(mt2);
                                              mt2.getRawNeighbors().add(mt1);

                                              layers.add(new Layer(triangles));

                                              Fold fold = new Fold(0);

                                              FoldLine line = new FoldLine();
                                              line.direction = null;
                                              line.line = new ModelTriangleEdge(mt2, 0);
                                              fold.lines.add(line);
                                              mt2.setFoldLines(0, new LinkedList<FoldLine>());
                                              mt2.getFoldLines(0).add(line);

                                              line = new FoldLine();
                                              line.direction = null;
                                              line.line = new ModelTriangleEdge(mt2, 2);
                                              fold.lines.add(line);
                                              mt2.setFoldLines(2, new LinkedList<FoldLine>());
                                              mt2.getFoldLines(2).add(line);

                                              line = new FoldLine();
                                              line.direction = null;
                                              line.line = new ModelTriangleEdge(mt1, 0);
                                              fold.lines.add(line);
                                              mt1.setFoldLines(0, new LinkedList<FoldLine>());
                                              mt1.getFoldLines(0).add(line);

                                              line = new FoldLine();
                                              line.direction = null;
                                              line.line = new ModelTriangleEdge(mt1, 2);
                                              fold.lines.add(line);
                                              mt1.setFoldLines(2, new LinkedList<FoldLine>());
                                              mt1.getFoldLines(2).add(line);

                                              fold.originatingStepId = 0;
                                              folds.add(fold);

                                              return null;
                                          }
                                      };

    /**
     * Create a default model state for the given origami diagram.
     * 
     * @param o The origami to create the default state for.
     */
    public DefaultModelState(Origami o)
    {
        this.origami = o;
    }

    @Override
    public ModelState clone()
    {
        if (loadData != null) {
            // load the data from origami
            try {
                loadData.call();
            } catch (Exception e) {
                Logger.getLogger(getClass()).error("Default model state failed to load origami data.", e);
            }
            loadData = null;
        }
        return super.clone();
    }
}

/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.modelstate;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.vecmath.Point2d;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import cz.cuni.mff.peckam.java.origamist.math.IntersectionWithTriangle;
import cz.cuni.mff.peckam.java.origamist.math.Triangle2d;
import cz.cuni.mff.peckam.java.origamist.math.Triangle3d;

/**
 * A 3D triangle holding information about its coordinates in the original sheet of paper. Units for the points should
 * be the relative units.
 * 
 * @author Martin Pecka
 */
public class ModelTriangle extends Triangle3d
{

    /**
     * The original position of this triangle on the sheet of paper.
     */
    protected Triangle2d          originalPosition;

    /** The list of neighbors. */
    protected List<ModelTriangle> neighbors   = new LinkedList<ModelTriangle>();

    /** The read-only view on the neighbors list. */
    protected List<ModelTriangle> neighborsRO = Collections.unmodifiableList(neighbors);

    /**
     * @param p1
     * @param p2
     * @param p3
     * @param o The original position on the sheet of paper.
     */
    public ModelTriangle(Point3d p1, Point3d p2, Point3d p3, Triangle2d o)
    {
        super(p1, p2, p3);
        originalPosition = o;
    }

    /**
     * @param p1
     * @param p2
     * @param p3
     * @param o1 The first vertex of the original position on the sheet of paper.
     * @param o2 The second vertex of the original position on the sheet of paper.
     * @param o3 The third vertex of the original position on the sheet of paper.
     */
    public ModelTriangle(Point3d p1, Point3d p2, Point3d p3, Point2d o1, Point2d o2, Point2d o3)
    {
        this(p1, p2, p3, new Triangle2d(o1, o2, o3));
    }

    /**
     * @param p1
     * @param p2
     * @param p3
     * @param o1x x-coordinate of the first vertex of the original position on the sheet of paper.
     * @param o1y y-coordinate of the first vertex of the original position on the sheet of paper.
     * @param o2x x-coordinate of the second vertex of the original position on the sheet of paper.
     * @param o2y y-coordinate of the second vertex of the original position on the sheet of paper.
     * @param o3x x-coordinate of the third vertex of the original position on the sheet of paper.
     * @param o3y y-coordinate of the third vertex of the original position on the sheet of paper.
     */
    public ModelTriangle(Point3d p1, Point3d p2, Point3d p3, double o1x, double o1y, double o2x, double o2y,
            double o3x, double o3y)
    {
        this(p1, p2, p3, new Triangle2d(o1x, o1y, o2x, o2y, o3x, o3y));
    }

    /**
     * @param p1x
     * @param p1y
     * @param p1z
     * @param p2x
     * @param p2y
     * @param p2z
     * @param p3x
     * @param p3y
     * @param p3z
     * @param o The original position on the sheet of paper.
     */
    public ModelTriangle(double p1x, double p1y, double p1z, double p2x, double p2y, double p2z, double p3x,
            double p3y, double p3z, Triangle2d o)
    {
        this(new Point3d(p1x, p1y, p1z), new Point3d(p2x, p2y, p2z), new Point3d(p3x, p3y, p3z), o);
    }

    /**
     * @param p1x
     * @param p1y
     * @param p1z
     * @param p2x
     * @param p2y
     * @param p2z
     * @param p3x
     * @param p3y
     * @param p3z
     * @param o1 The first vertex of the original position on the sheet of paper.
     * @param o2 The second vertex of the original position on the sheet of paper.
     * @param o3 The third vertex of the original position on the sheet of paper.
     */
    public ModelTriangle(double p1x, double p1y, double p1z, double p2x, double p2y, double p2z, double p3x,
            double p3y, double p3z, Point2d o1, Point2d o2, Point2d o3)
    {
        this(new Point3d(p1x, p1y, p1z), new Point3d(p2x, p2y, p2z), new Point3d(p3x, p3y, p3z), o1, o2, o3);
    }

    /**
     * @param p1x
     * @param p1y
     * @param p1z
     * @param p2x
     * @param p2y
     * @param p2z
     * @param p3x
     * @param p3y
     * @param p3z
     * @param o1x x-coordinate of the first vertex of the original position on the sheet of paper.
     * @param o1y y-coordinate of the first vertex of the original position on the sheet of paper.
     * @param o2x x-coordinate of the second vertex of the original position on the sheet of paper.
     * @param o2y y-coordinate of the second vertex of the original position on the sheet of paper.
     * @param o3x x-coordinate of the third vertex of the original position on the sheet of paper.
     * @param o3y y-coordinate of the third vertex of the original position on the sheet of paper.
     */
    public ModelTriangle(double p1x, double p1y, double p1z, double p2x, double p2y, double p2z, double p3x,
            double p3y, double p3z, double o1x, double o1y, double o2x, double o2y, double o3x, double o3y)
    {
        this(new Point3d(p1x, p1y, p1z), new Point3d(p2x, p2y, p2z), new Point3d(p3x, p3y, p3z), o1x, o1y, o2x, o2y,
                o3x, o3y);
    }

    /**
     * Sets the original position of this triangle on the sheet of paper.
     * 
     * @param o The original 2D position.
     */
    public void setOriginalPosition(Triangle2d o)
    {
        this.originalPosition = o;
    }

    /**
     * Sets the original position of this triangle on the sheet of paper.
     * 
     * @param o1 The first vertex of the original 2D position.
     * @param o2 The second vertex of the original 2D position.
     * @param o3 The third vertex of the original 2D position.
     */
    public void setOriginalPosition(Point2d o1, Point2d o2, Point2d o3)
    {
        this.originalPosition = new Triangle2d(o1, o2, o3);
    }

    /**
     * Sets the original position of this triangle on the sheet of paper.
     * 
     * @param o1x x-coordinate of the first vertex of the original 2D position.
     * @param o1y x-coordinate of the first vertex of the original 2D position.
     * @param o2x y-coordinate of the second vertex of the original 2D position.
     * @param o2y x-coordinate of the second vertex of the original 2D position.
     * @param o3x y-coordinate of the third vertex of the original 2D position.
     * @param o3y x-coordinate of the third vertex of the original 2D position.
     */
    public void setOriginalPosition(double o1x, double o1y, double o2x, double o2y, double o3x, double o3y)
    {
        this.originalPosition = new Triangle2d(o1x, o1y, o2x, o2y, o3x, o3y);
    }

    /**
     * Returns the original position of this triangle on the sheet of paper.
     * 
     * @return The original position of this triangle on the sheet of paper.
     */
    public Triangle2d getOriginalPosition()
    {
        return originalPosition;
    }

    /**
     * @return The list of neighbor triangles. The returned list is read-only.
     */
    public List<ModelTriangle> getNeighbors()
    {
        return neighborsRO;
    }

    @Override
    public <T extends Triangle3d> List<T> subdivideTriangle(IntersectionWithTriangle<T> segment)
            throws IllegalArgumentException
    {
        List<T> result = super.subdivideTriangle(segment);

        // the second check is an alternative to find out if (T instanceof ModelTriangle)
        if (result.size() > 1 && (result.get(0) instanceof ModelTriangle)) {

            // remove this triangle from the neighbors' neighbors lists and add new triangles as neighbors
            for (ModelTriangle n : neighbors) {
                n.neighbors.remove(this);
                for (T t : result) {
                    if (n.hasCommonEdge(t, false)) {
                        ModelTriangle mt = (ModelTriangle) t;
                        n.neighbors.add(mt);
                        mt.neighbors.add(n);
                    }
                }
            }

            // find and add neighbors among the new triangles
            int i = 0;
            for (T t1 : result) {
                if (i + 1 <= result.size() - 1) {
                    for (T t2 : result.subList(i + 1, result.size())) {
                        if (t1.hasCommonEdge(t2, false)) {
                            ModelTriangle mt1 = (ModelTriangle) t1;
                            ModelTriangle mt2 = (ModelTriangle) t2;
                            mt1.neighbors.add(mt2);
                            mt2.neighbors.add(mt1);
                        }
                    }
                }
                i++;
            }

        }

        return result;
    }

    @Override
    protected Triangle3d createSubtriangle(Point3d p1, Point3d p2, Point3d p3)
    {
        // recompute also the original position triangle
        Vector3d bp1 = getBarycentricCoordinates(p1);
        Vector3d bp2 = getBarycentricCoordinates(p2);
        Vector3d bp3 = getBarycentricCoordinates(p3);

        Point2d pp1 = originalPosition.interpolatePointFromBarycentric(bp1);
        Point2d pp2 = originalPosition.interpolatePointFromBarycentric(bp2);
        Point2d pp3 = originalPosition.interpolatePointFromBarycentric(bp3);

        return new ModelTriangle(p1, p2, p3, new Triangle2d(pp1, pp2, pp3));
    }

    @Override
    public ModelTriangle clone()
    {
        Triangle3d t = (Triangle3d) super.clone();
        ModelTriangle result = new ModelTriangle(t.getP1(), t.getP2(), t.getP3(),
                (Triangle2d) this.originalPosition.clone());
        return result;
    }

}

/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.modelstate;

import static cz.cuni.mff.peckam.java.origamist.math.MathHelper.EPSILON;

import java.util.AbstractList;
import java.util.LinkedList;
import java.util.List;

import javax.vecmath.Point2d;
import javax.vecmath.Point3d;
import javax.vecmath.Vector2d;
import javax.vecmath.Vector3d;

import cz.cuni.mff.peckam.java.origamist.math.IntersectionWithTriangle;
import cz.cuni.mff.peckam.java.origamist.math.MathHelper;
import cz.cuni.mff.peckam.java.origamist.math.Segment2d;
import cz.cuni.mff.peckam.java.origamist.math.Segment3d;
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
    protected Triangle2d     originalPosition;

    /** The fold lines for the first edge of the triangle. */
    protected List<FoldLine> s1FoldLines = null;

    /** The fold lines for the second edge of the triangle. */
    protected List<FoldLine> s2FoldLines = null;

    /** The fold lines for the third edge of the triangle. */
    protected List<FoldLine> s3FoldLines = null;

    /** The triangle representing this model triangle before any rotation was done in the step this triangle is part of. */
    protected Triangle3d     beforeRotation;

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
        resetBeforeRotation();
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

    @Override
    public List<ModelTriangle> getNeighbors()
    {
        return new AbstractList<ModelTriangle>() {
            @Override
            public ModelTriangle get(int index)
            {
                return (ModelTriangle) neighborsRO.get(index);
            }

            @Override
            public int size()
            {
                return neighborsRO.size();
            }
        };
    }

    /**
     * @return The raw (modifiable) list of neighboring triangles.
     */
    List<ModelTriangle> getRawNeighbors()
    {
        return new AbstractList<ModelTriangle>() {
            @Override
            public ModelTriangle get(int index)
            {
                return (ModelTriangle) neighbors.get(index);
            }

            @Override
            public int size()
            {
                return neighbors.size();
            }

            @Override
            public ModelTriangle set(int index, ModelTriangle element)
            {
                return (ModelTriangle) neighbors.set(index, element);
            }

            @Override
            public void add(int index, ModelTriangle element)
            {
                neighbors.add(index, element);
            }

            @Override
            public ModelTriangle remove(int index)
            {
                return (ModelTriangle) neighbors.remove(index);
            }

        };

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

        Point3d p1rot = beforeRotation.interpolatePointFromBarycentric(bp1);
        Point3d p2rot = beforeRotation.interpolatePointFromBarycentric(bp2);
        Point3d p3rot = beforeRotation.interpolatePointFromBarycentric(bp3);

        ModelTriangle mt = new ModelTriangle(p1, p2, p3, new Triangle2d(pp1, pp2, pp3));

        mt.beforeRotation = new Triangle3d(p1rot, p2rot, p3rot);

        return mt;
    }

    /**
     * Return the fold lines for the edge with the given index.
     * 
     * @param i The index of the edge. An integer from 0 to 2.
     * @return The fold lines for the edge with the given index.
     */
    protected List<FoldLine> getFoldLines(int i)
    {
        switch (i) {
            case 0:
                return s1FoldLines;
            case 1:
                return s2FoldLines;
            case 2:
                return s3FoldLines;
            default:
                throw new ArrayIndexOutOfBoundsException();
        }
    }

    /**
     * Sets the fold lines for the edge with the given index.
     * 
     * @param i The index of the edge. An integer from 0 to 2.
     * @param lines The new list of fold lines.
     */
    protected void setFoldLines(int i, List<FoldLine> lines)
    {
        switch (i) {
            case 0:
                s1FoldLines = lines;
                break;
            case 1:
                s2FoldLines = lines;
                break;
            case 2:
                s3FoldLines = lines;
                break;
            default:
                throw new ArrayIndexOutOfBoundsException();
        }
    }

    /**
     * Adds the fold lines for the edge with the given index.
     * 
     * @param i The index of the edge. An integer from 0 to 2.
     * @param lines The new fold line.
     */
    protected void addFoldLine(int i, FoldLine line)
    {
        List<FoldLine> lines = getFoldLines(i);
        if (lines == null) {
            setFoldLines(i, new LinkedList<FoldLine>());
            lines = getFoldLines(i);
        }
        lines.add(line);
    }

    /**
     * @return The triangle representing this model triangle before any rotation was done in the step this triangle is
     *         part of.
     */
    public Triangle3d getBeforeRotation()
    {
        return beforeRotation;
    }

    /**
     * Sets the beforeRotation triangle to represent the current triangle's position. Should be called eg. after this
     * triangle is cloned and added to another step.
     */
    public void resetBeforeRotation()
    {
        this.beforeRotation = new Triangle3d((Point3d) p1.clone(), (Point3d) p2.clone(), (Point3d) p3.clone());
    }

    /**
     * {@inheritDoc}
     * <p>
     * Subdivides the fold lines, assigns them to the new triangles and nulls foldline references in segment.triangle.
     */
    @Override
    public <T extends Triangle3d> List<T> subdivideTriangle(IntersectionWithTriangle<T> segment)
            throws IllegalArgumentException
    {
        List<T> result = super.subdivideTriangle(segment);

        // subdivide also foldLines
        if (result.size() > 1 && result.get(0) instanceof ModelTriangle) {
            int i = 0;
            for (Segment3d edge : getEdges()) {
                List<FoldLine> lines = getFoldLines(i);
                if (lines != null && lines.size() > 0) {
                    loop: for (T t : result) {
                        ModelTriangle mt = (ModelTriangle) t;
                        int j = 0;
                        for (Segment3d tEdge : t.getEdges()) {
                            Segment3d intersection = edge.getIntersection(tEdge);
                            if (intersection != null
                                    && !intersection.getVector().epsilonEquals(new Vector3d(), MathHelper.EPSILON)) {

                                ModelTriangleEdge newEdge = new ModelTriangleEdge(mt, j);
                                for (FoldLine line : lines) {
                                    FoldLine newLine = line.clone();
                                    newLine.setLine(newEdge);
                                    newLine.getFold().getLines().add(newLine);
                                    mt.addFoldLine(j, newLine);
                                }

                                continue loop;
                            }
                            j++;
                        }
                    }

                    for (FoldLine line : lines) {
                        line.getFold().getLines().remove(line);
                    }
                    setFoldLines(i, null);
                }
                i++;
            }

        }

        return result;
    }

    /**
     * @return An array of all edges of the triangle. Further modifications to this array will have no effect on the
     *         triangle.
     */
    public ModelTriangleEdge[] getModelTriangleEdges()
    {
        return new ModelTriangleEdge[] { new ModelTriangleEdge(this, 0), new ModelTriangleEdge(this, 1),
                new ModelTriangleEdge(this, 2) };
    }

    /**
     * Returns the common part of edges of this and the given triangle. If they have just a common point, a segment
     * with zero direction vector will be returned. If the triangles overlay by more than an edge, the result is
     * undefined. If the triangles do not have a common segment, <code>null</code> will be returned.
     * <p>
     * The triangles are checked in both 3D and 2D.
     * 
     * @param t The segment to find common edge with.
     * @param strict If true, then the edges must match exactly. If it is false, it is sufficient that the edges
     *            overlap.
     * @return The common part of edges of this and the given triangle.
     */
    public ModelSegment getCommonEdge(ModelTriangle t, boolean strict)
    {
        ModelSegment result = null;
        for (ModelTriangleEdge edge1 : getModelTriangleEdges()) {
            for (ModelTriangleEdge edge2 : t.getModelTriangleEdges()) {
                if (strict) {
                    if (edge1.getSegment2d().epsilonEquals(edge2.getSegment2d(), true)
                            && edge1.getSegment3d().epsilonEquals(edge2.getSegment3d(), true))
                        return edge1.getSegment();
                } else {
                    if (edge1.getSegment2d().overlaps(edge2.getSegment2d())
                            && edge1.getSegment3d().overlaps(edge2.getSegment3d())) {
                        Segment2d intersection2 = edge1.getSegment2d().getIntersection(edge2.getSegment2d());
                        Segment3d intersection3 = edge1.getSegment3d().getIntersection(edge2.getSegment3d());
                        // if the intersection isn't just a point, we can surely return
                        if (intersection2 != null && !intersection2.getVector().epsilonEquals(new Vector2d(), EPSILON)
                                && intersection3 != null
                                && !intersection3.getVector().epsilonEquals(new Vector3d(), EPSILON))
                            return new ModelSegment(intersection3, intersection2);
                        if (intersection2 != null && intersection3 != null)
                            result = new ModelSegment(intersection3, intersection2);
                    }
                }
            }
        }
        return result;
    }

    /**
     * Returns the common edges of this and the given triangle's original 2D triangles. If the triangles do not have a
     * common segment, <code>null</code> will be returned.
     * 
     * @param t The triangle to find common edge with.
     * @param strict If true, then the edges must match exactly. If it is false, it is sufficient that the edges
     *            overlap.
     * @return The common part of edges of this and the given triangle.
     */
    public ModelTriangleEdge[] getCommonEdge2d(ModelTriangle t, boolean strict)
    {
        ModelTriangleEdge[] result = null;
        int i = 0, j = 0;
        out: for (Segment2d edge1 : getOriginalPosition().getEdges()) {
            j = 0;
            for (Segment2d edge2 : t.getOriginalPosition().getEdges()) {
                if (strict) {
                    if (edge1.epsilonEquals(edge2, true)) {
                        result = new ModelTriangleEdge[] { new ModelTriangleEdge(this, i), new ModelTriangleEdge(t, j) };
                        break out;
                    }
                } else {
                    if (edge1.overlaps(edge2)) {
                        Segment2d intersection = edge1.getIntersection(edge2);
                        // if the intersection isn't just a point, we can surely break
                        if (intersection != null && !intersection.getVector().epsilonEquals(new Vector2d(), EPSILON)) {
                            result = new ModelTriangleEdge[] { new ModelTriangleEdge(this, i),
                                    new ModelTriangleEdge(t, j) };
                            break out;
                        } else if (intersection != null) {
                            result = new ModelTriangleEdge[] { new ModelTriangleEdge(this, i),
                                    new ModelTriangleEdge(t, j) };
                            // not breaking here allows us to find really the edges that overlap, not just breaking at
                            // the first two edges that just regularly intersect
                        }
                    }
                }
                j++;
            }
            i++;
        }
        return result;
    }

    /**
     * <p>
     * <b>The lists of fold lines belonging to edges are cloned, but the Folds the FoldLines belong to aren't changed to
     * reference the newly created fold lines (but the newly created fold lines reference the same parent folds as the
     * source ones).</b>
     * </p>
     * 
     * {@inheritDoc}
     */
    @Override
    public ModelTriangle clone()
    {
        ModelTriangle result = new ModelTriangle(getP1(), getP2(), getP3(), this.originalPosition.clone());

        for (int i = 0; i < 3; i++) {
            List<FoldLine> lines = getFoldLines(i);
            if (lines != null) {
                result.setFoldLines(i, new LinkedList<FoldLine>());
                List<FoldLine> newLines = result.getFoldLines(i);
                for (FoldLine line : lines) {
                    FoldLine newLine = line.clone();
                    newLine.setLine(new ModelTriangleEdge(result, line.getLine().getIndex()));
                    newLines.add(newLine);
                }
            }
        }

        result.neighbors.addAll(this.neighbors);
        result.beforeRotation = this.beforeRotation.clone();

        return result;
    }

}

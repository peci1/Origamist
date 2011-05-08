/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.math;

import static cz.cuni.mff.peckam.java.origamist.math.MathHelper.EPSILON;

import javax.vecmath.Point2d;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import cz.cuni.mff.peckam.java.origamist.modelstate.ModelPoint;
import cz.cuni.mff.peckam.java.origamist.modelstate.ModelSegment;
import cz.cuni.mff.peckam.java.origamist.modelstate.ModelTriangle;
import cz.cuni.mff.peckam.java.origamist.modelstate.ModelTriangleEdge;

/**
 * This represents an intersection of a segment with a triangle.
 * 
 * @param T The type of the triangle.
 * 
 * @author Martin Pecka
 */
public class IntersectionWithTriangle<T extends Triangle3d> extends Segment3d
{
    /** */
    private static final long serialVersionUID = 3418089833892732927L;

    /** The triangle the intersection occurs on. */
    public T                  triangle;

    /**
     * @param triangle A triangle.
     * @param segment A segment intersecting the triangle.
     */
    public IntersectionWithTriangle(T triangle, Segment3d segment)
    {
        super(segment.getP1(), segment.getP2());
        this.triangle = triangle;
    }

    /**
     * @return The triangle the intersection occurs on.
     */
    public T getTriangle()
    {
        return triangle;
    }

    /**
     * @return <code>true</code> if the intersection segment coincides with an edge of the triangle (even partly
     *         coincides if the segment is shorter than the side).
     */
    public boolean isWholeSideIntersection()
    {
        if (v.epsilonEquals(new Vector3d(), EPSILON))
            // a single point of intersection
            return false;

        for (Segment3d edge : triangle.getEdges()) {
            if (this.isParallelTo(edge) && edge.contains(p))
                return true;
        }
        return false;
    }

    /**
     * Get the edge containing the point or nearest to the point.
     * 
     * @param i Pass 0 to set point to p1, otherwise set it to p2.
     * @return The edge (if T instanceof ModelTriangle, return ModelSegment with null direction and originatingStepId
     *         0).
     */
    public Segment3d getEdgeForPoint(int i)
    {
        Point3d p;
        if (i == 0) {
            p = this.p;
        } else {
            p = p2;
        }

        if (triangle instanceof ModelTriangle) {
            ModelTriangle mt = (ModelTriangle) triangle;
            if (triangle.s1.contains(p))
                return new ModelTriangleEdge(mt, 0).getSegment();
            else if (triangle.s2.contains(p))
                return new ModelTriangleEdge(mt, 1).getSegment();
            else if (triangle.s3.contains(p))
                return new ModelTriangleEdge(mt, 2).getSegment();
            else {
                double d1 = mt.s1.getNearestPoint(p).distance(p);
                double d2 = mt.s2.getNearestPoint(p).distance(p);
                double d3 = mt.s3.getNearestPoint(p).distance(p);
                if (d1 + EPSILON >= d2 && d1 + EPSILON >= d3)
                    return new ModelTriangleEdge(mt, 0).getSegment();
                if (d2 + EPSILON >= d1 && d2 + EPSILON >= d3)
                    return new ModelTriangleEdge(mt, 1).getSegment();
                if (d3 + EPSILON >= d1 && d3 + EPSILON >= d2)
                    return new ModelTriangleEdge(mt, 2).getSegment();
            }
        } else {
            if (triangle.s1.contains(p))
                return triangle.s1;
            else if (triangle.s2.contains(p))
                return triangle.s2;
            else if (triangle.s3.contains(p))
                return triangle.s3;
            else {
                double d1 = triangle.s1.getNearestPoint(p).distance(p);
                double d2 = triangle.s2.getNearestPoint(p).distance(p);
                double d3 = triangle.s3.getNearestPoint(p).distance(p);
                if (d1 + EPSILON >= d2 && d1 + EPSILON >= d3)
                    return triangle.s1;
                if (d2 + EPSILON >= d1 && d2 + EPSILON >= d3)
                    return triangle.s2;
                if (d3 + EPSILON >= d1 && d3 + EPSILON >= d2)
                    return triangle.s3;
            }
        }

        return null;
    }

    /**
     * Return the point with the given index.
     * 
     * @param i Pass 0 to set point to p1, otherwise set it to p2.
     * @return The point (if T instanceof ModelTriangle, then return a corresponding ModelPoint).
     */
    public Point3d getPoint(int i)
    {
        Point3d p;
        if (i == 0) {
            p = this.p;
        } else {
            p = p2;
        }

        if (triangle instanceof ModelTriangle) {
            ModelSegment edge = (ModelSegment) getEdgeForPoint(i);
            double param = edge.getP1().distance(p) / edge.getP1().distance(edge.getP2());
            Point2d point = edge.getOriginal().getPointForParameter(param);
            return new ModelPoint(p, point);
        } else {
            return p;
        }
    }
}
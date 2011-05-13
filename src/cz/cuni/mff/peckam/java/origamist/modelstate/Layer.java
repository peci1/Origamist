/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.modelstate;

import static cz.cuni.mff.peckam.java.origamist.math.MathHelper.EPSILON;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.vecmath.Point2d;
import javax.vecmath.Point3d;

import cz.cuni.mff.peckam.java.origamist.math.IntersectionWithTriangle;
import cz.cuni.mff.peckam.java.origamist.math.Line3d;
import cz.cuni.mff.peckam.java.origamist.math.Polygon3d;
import cz.cuni.mff.peckam.java.origamist.math.Segment2d;
import cz.cuni.mff.peckam.java.origamist.math.Segment3d;
import cz.cuni.mff.peckam.java.origamist.math.Stripe3d;

/**
 * A layer of the paper - a planar part of the paper that is surrouned by paper boundaries and triangles with a
 * non-parallel normal.
 * 
 * @author Martin Pecka
 */
public class Layer extends Polygon3d<ModelTriangle>
{

    /**
     * Create a new layer consisting of the given triangles.
     * 
     * @param triangles The triangles the layer consists of. The list can be modified by this function.
     * @throws IllegalStateException If the resulting layer either wouldn't be connected or one of the triangles
     *             doesn't lie in the same plane as the first triangle does. In the case this exception is thrown, the
     *             layers's state will remain the same as before calling this function (eg. this will not try to add
     *             all "valid" triangles from the given list, but it either accepts all or none of them).
     */
    public Layer(List<ModelTriangle> triangles) throws IllegalStateException
    {
        super(triangles);
    }

    /**
     * Create a new layer consisting of the given triangles.
     * 
     * @param triangles The triangles the layer consists of. The list can be modified by this function.
     * @throws IllegalStateException If the resulting layer either wouldn't be connected or one of the triangles
     *             doesn't lie in the same plane as the first triangle does. In the case this exception is thrown, the
     *             layers's state will remain the same as before calling this function (eg. this will not try to add
     *             all "valid" triangles from the given list, but it either accepts all or none of them).
     */
    public Layer(ModelTriangle... triangles) throws IllegalStateException
    {
        super(triangles);
    }

    /**
     * Create a new layer consisting of the given triangles.
     * 
     * @param triangles The triangles the layer consists of. The list can be modified by this function.
     * @throws IllegalStateException If the resulting layer either wouldn't be connected or one of the triangles
     *             doesn't lie in the same plane as the first triangle does. In the case this exception is thrown, the
     *             layers's state will remain the same as before calling this function (eg. this will not try to add
     *             all "valid" triangles from the given list, but it either accepts all or none of them).
     */
    public Layer(Set<ModelTriangle> triangles) throws IllegalStateException
    {
        super(triangles);
    }

    /**
     * Create a new layer out of the given polygon.
     * 
     * @param polygon The polygon to create the layer from.
     */
    public Layer(Polygon3d<ModelTriangle> polygon)
    {
        this(new HashSet<ModelTriangle>(polygon.getTriangles()));
    }

    @Override
    protected boolean additionalAddTrianglesCheck(Set<ModelTriangle> triangles)
    {
        // check if every two triangles neighboring in 3D are also neighbors on the paper
        for (ModelTriangle t : triangles) {
            List<ModelTriangle> neighbors = getNeighbors(t);
            for (ModelTriangle n : neighbors) {
                if (!t.originalPosition.hasCommonEdge(n.originalPosition, false))
                    return false;
            }
        }
        return true;
    }

    @Override
    protected boolean additionalRemoveTrianglesCheck(Set<ModelTriangle> triangles)
    {
        // check if every two triangles neighboring in 3D are also neighbors on the paper
        for (ModelTriangle t : this.triangles) {
            List<ModelTriangle> neighbors = getNeighbors(t);
            for (ModelTriangle n : neighbors) {
                if (!t.originalPosition.hasCommonEdge(n.originalPosition, false))
                    return false;
            }
        }
        return true;
    }

    @Override
    public List<? extends ModelSegment> getIntersections(Line3d line)
    {
        // connect all segments that can be connected into one new segment
        List<IntersectionWithTriangle<ModelTriangle>> intersections = getIntersectionsWithTriangles(line);
        return joinNeighboringSegments(intersections);
    }

    @Override
    public List<? extends ModelSegment> joinNeighboringSegments(List<IntersectionWithTriangle<ModelTriangle>> segments)
    {
        LinkedList<ModelSegment> result = new LinkedList<ModelSegment>();

        for (IntersectionWithTriangle<ModelTriangle> s : segments) {
            ModelSegment newSeg = new ModelSegment(s, null, 0);
            if (result.size() == 0 || !result.getLast().getP2().epsilonEquals(s.getP1(), EPSILON)) {
                result.add(newSeg);
            } else {
                ModelSegment last = result.getLast();
                last = new ModelSegment(new Segment3d(last.getP1(), s.getP2()), new Segment2d(last.getOriginal()
                        .getP1(), newSeg.getOriginal().getP2()), null, 0);

                result.removeLast();
                result.add(last);
            }
        }

        return result;
    }

    /**
     * Tell whether this polygon contains the given point both in 3D and 2D.
     * 
     * @param point The point to check.
     * @return <code>true</code> if this polygon contains the given point in both 3D and 2D.
     */
    public boolean contains(ModelPoint point)
    {
        if (!plane.contains(point))
            return false;

        for (ModelTriangle t : triangles) {
            if (t.contains(point) && t.getOriginalPosition().contains(point.getOriginal()))
                return true;
        }
        return false;
    }

    /**
     * Tell whether this polygon's 2D triangles contain the given 2D point.
     * 
     * @param point The point to check.
     * @return <code>true</code> if this polygon contains the given point in 2D.
     */
    public boolean contains(Point2d point)
    {
        for (ModelTriangle t : triangles) {
            if (t.getOriginalPosition().contains(point))
                return true;
        }
        return false;
    }

    /**
     * Return the single segment defining the intersection of the given stripe with this polygon (this segment joins the
     * first and last segment returned by {@link Polygon3d#getIntersections}).
     * 
     * @param stripe The stripe to find the intersection with.
     * @return The intersection of the given stripe and this polygon. <code>null</code> if the stripe is parallel to
     *         this polygon (and if it lies in the same plane as the polygon) or if the stripe doesn't intersect with
     *         it.
     */
    public ModelSegment getIntersectionSegment(Stripe3d stripe)
    {
        @SuppressWarnings("unchecked")
        List<? extends ModelSegment> ints = (List<? extends ModelSegment>) getIntersections(stripe);
        if (ints == null || ints.size() == 0)
            return null;

        Point3d int1 = ints.get(0).getP1();
        Point3d int2 = ints.get(ints.size() - 1).getP2();
        Point2d int1_2 = ints.get(0).getOriginal().getP1();
        Point2d int2_2 = ints.get(ints.size() - 1).getOriginal().getP2();

        return new ModelSegment(new Segment3d(int1, int2), new Segment2d(int1_2, int2_2), null, -1);
    }
}
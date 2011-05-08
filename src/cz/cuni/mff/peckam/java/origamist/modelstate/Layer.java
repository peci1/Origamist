/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.modelstate;

import static cz.cuni.mff.peckam.java.origamist.math.MathHelper.EPSILON;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import cz.cuni.mff.peckam.java.origamist.math.IntersectionWithTriangle;
import cz.cuni.mff.peckam.java.origamist.math.Line3d;
import cz.cuni.mff.peckam.java.origamist.math.Polygon3d;
import cz.cuni.mff.peckam.java.origamist.math.Segment2d;
import cz.cuni.mff.peckam.java.origamist.math.Segment3d;

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
}
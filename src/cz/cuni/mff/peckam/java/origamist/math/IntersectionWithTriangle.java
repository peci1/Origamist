/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.math;

import static cz.cuni.mff.peckam.java.origamist.math.MathHelper.EPSILON;

import javax.vecmath.Vector3d;

/**
 * This represents an intersection of a segment with a triangle.
 * 
 * @param T The type of the triangle.
 * 
 * @author Martin Pecka
 */
public class IntersectionWithTriangle<T extends Triangle3d> extends Segment3d
{
    /** The triangle the intersection occurs on. */
    public T triangle;

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
}
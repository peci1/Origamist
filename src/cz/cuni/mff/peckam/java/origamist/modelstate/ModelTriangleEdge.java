/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.modelstate;

import cz.cuni.mff.peckam.java.origamist.math.Segment2d;
import cz.cuni.mff.peckam.java.origamist.math.Segment3d;

/**
 * A "weak" reference to a model triangle's edge. This class just takes a triangle and the index of the edge and returns
 * always the up-to-date edge, even after rotating the triangle or so.
 * 
 * @author Martin Pecka
 */
public class ModelTriangleEdge implements Cloneable
{
    /** The triangle this is an edge of. */
    protected ModelTriangle triangle;

    /** The index of the edge in the triangle - 0 is edge p1-p2, 1 is edge p2-p3, 2 is edge p1-p3. */
    protected int           index;

    /**
     * Create a "weak" reference to the given triangle's edge with the given index.
     * 
     * @param triangle The triangle this class works with.
     * @param edgeIndex The index of the edge - 0 is edge p1-p2, 1 is edge p2-p3, 2 is edge p3-p1.
     */
    public ModelTriangleEdge(ModelTriangle triangle, int edgeIndex)
    {
        this.triangle = triangle;
        this.index = edgeIndex;
    }

    /**
     * @return The 3D line corresponding to this edge.
     */
    public Segment3d getSegment3d()
    {
        switch (index) {
            case 0:
                return triangle.getS1();
            case 1:
                return triangle.getS2();
            case 2:
                return triangle.getS3();
            default:
                return null;
        }
    }

    /**
     * @return The 2D line corresponding to this edge.
     */
    public Segment2d getSegment2d()
    {
        switch (index) {
            case 0:
                return triangle.getOriginalPosition().getS1();
            case 1:
                return triangle.getOriginalPosition().getS2();
            case 2:
                return triangle.getOriginalPosition().getS3();
            default:
                return null;
        }
    }

    /**
     * @return The model segment corresponding to this edge.
     */
    public ModelSegment getSegment()
    {
        switch (index) {
            case 0:
                return new ModelSegment(triangle.getS1(), triangle.getOriginalPosition().getS1(), null, 0);
            case 1:
                return new ModelSegment(triangle.getS2(), triangle.getOriginalPosition().getS2(), null, 0);
            case 2:
                return new ModelSegment(triangle.getS3(), triangle.getOriginalPosition().getS3(), null, 0);
            default:
                return null;
        }
    }

    /**
     * @return The triangle this is an edge of.
     */
    public ModelTriangle getTriangle()
    {
        return triangle;
    }

    /**
     * @param triangle The triangle this is an edge of.
     */
    public void setTriangle(ModelTriangle triangle)
    {
        this.triangle = triangle;
    }

    /**
     * @return The index of the edge in the triangle - 0 is edge p1-p2, 1 is edge p2-p3, 2 is edge p1-p3.
     */
    public int getIndex()
    {
        return index;
    }

    /**
     * @param index The index of the edge in the triangle - 0 is edge p1-p2, 1 is edge p2-p3, 2 is edge p1-p3.
     */
    public void setIndex(int index)
    {
        this.index = index;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + index;
        result = prime * result + ((triangle == null) ? 0 : triangle.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ModelTriangleEdge other = (ModelTriangleEdge) obj;
        if (index != other.index)
            return false;
        if (triangle == null) {
            if (other.triangle != null)
                return false;
        } else if (!triangle.equals(other.triangle))
            return false;
        return true;
    }

    /**
     * <p>
     * <b>Cloning this class doesn't deep-clone the referenced triangle!</b>
     * </p>
     * 
     * {@inheritDoc}
     */
    @Override
    protected ModelTriangleEdge clone() throws CloneNotSupportedException
    {
        try {
            // the triangle field isn't cloned intentionally
            return (ModelTriangleEdge) super.clone();
        } catch (CloneNotSupportedException e) {
            assert false : "super.clone() not supported";
            return null;
        }
    }

    @Override
    public String toString()
    {
        return "ModelTriangleEdge [getSegment3d()=" + getSegment3d() + ", getSegment2d()=" + getSegment2d() + "]";
    }
}

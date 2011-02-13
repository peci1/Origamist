/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.modelstate;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cz.cuni.mff.peckam.java.origamist.math.Polygon3d;

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
        return super.additionalAddTrianglesCheck(triangles);
        // TODO Auto-generated method stub
    }

    @Override
    protected boolean additionalRemoveTrianglesCheck(Set<ModelTriangle> triangles)
    {
        return super.additionalRemoveTrianglesCheck(triangles);
        // TODO Auto-generated method stub
    }

}

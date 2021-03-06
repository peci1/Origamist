/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.math;

import static cz.cuni.mff.peckam.java.origamist.math.MathHelper.EPSILON;

import java.util.Collection;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

/**
 * Represents a half-space in 3D.
 * 
 * @author Martin Pecka
 */
public class HalfSpace3d implements Cloneable
{
    /**
     * The border plane of this halfspace. The points in the halfspace are defined as the points (x,y,z), for which ax +
     * by + cz + d >= 0 holds, where a,b,c,d are the general form coefficients of the plane.
     */
    protected Plane3d plane;

    /**
     * Create a halfspace with the given plane as the border plane and that contains the part of space where the planes
     * normal points.
     * 
     * @param plane The border plane.
     */
    public HalfSpace3d(Plane3d plane)
    {
        this.plane = plane;
    }

    /**
     * Create a halfspace with its border plane perpendicular to the given normal and containing the given point, and
     * that contains the part of space where the given normal points.
     * 
     * @param normal the normal of the border plane, also defines the half of the space that is contained.
     * @param point A point on the border plane.
     */
    public HalfSpace3d(Vector3d normal, Point3d point)
    {
        this(new Plane3d(normal, point));
    }

    /**
     * Create a halfspace defined by the plane containing p1,p2,p3. The halfspace contains a general point r.
     * 
     * @param p1 A point in the border plane.
     * @param p2 A point in the border plane.
     * @param p3 A point in the border plane.
     * @param r A general point in the halfspace.
     * 
     * @throws IllegalArgumentException If r lies in the plane defined by p1, p2 and p3.
     */
    public HalfSpace3d(Point3d p1, Point3d p2, Point3d p3, Point3d r) throws IllegalArgumentException
    {
        plane = new Plane3d(p1, p2, p3);

        if (plane.contains(r)) {
            throw new IllegalArgumentException("Trying to define a halfspace using a plane and a point lying in it.");
        }

        if (!contains(r)) {
            invert();
        }
    }

    /**
     * Create a halfspace defined by the plane containing p1,p2,p3. The halfspace contains a general point r.
     * 
     * @param p1x X coordinate of a point in the border plane.
     * @param p1y Y coordinate of a point in the border plane.
     * @param p1z Z coordinate of a point in the border plane.
     * @param p2x X coordinate of a point in the border plane.
     * @param p2y Y coordinate of a point in the border plane.
     * @param p2z Z coordinate of a point in the border plane.
     * @param p3x X coordinate of a point in the border plane.
     * @param p3y Y coordinate of a point in the border plane.
     * @param p3z Z coordinate of a point in the border plane.
     * @param rx X coordinate of a general point in the halfspace.
     * @param ry Y coordinate of a general point in the halfspace.
     * @param rz Z coordinate of a general point in the halfspace.
     * 
     * @throws IllegalArgumentException If r lies in the plane defined by p1, p2 and p3.
     */
    public HalfSpace3d(double p1x, double p1y, double p1z, double p2x, double p2y, double p2z, double p3x, double p3y,
            double p3z, double rx, double ry, double rz) throws IllegalArgumentException
    {
        this(new Point3d(p1x, p1y, p1z), new Point3d(p2x, p2y, p2z), new Point3d(p3x, p3y, p3z),
                new Point3d(rx, ry, rz));
    }

    /**
     * Returns true if this halfspace contains the given point.
     * 
     * @param point The point to check.
     * @return Whether this halfspace contains the given point.
     */
    public boolean contains(Point3d point)
    {
        return plane.a * point.x + plane.b * point.y + plane.c * point.z + plane.d >= -EPSILON;
    }

    /**
     * Return true if this halfspace contains the given point, but its distance from the border plane is nonzero.
     * 
     * @param point The point to check.
     * @return Whether this halfspace contains the given point and its border plane does not.
     */
    public boolean containsExclusive(Point3d point)
    {
        return plane.signedDistance(point) > EPSILON;
    }

    /**
     * Return true if this halfspace contains all the given triangles and at least one of their points is contained
     * exclusively.
     * 
     * @param triangles The list of triangles to check.
     * @return Whether the triangles are contained or not.
     */
    public boolean containsTriangles(Collection<? extends Triangle3d> triangles)
    {
        for (Triangle3d t : triangles) {
            for (Point3d p : t.getVertices()) {
                double sDist = plane.signedDistance(p);
                if (sDist > EPSILON)
                    return true;
                else if (sDist < -EPSILON)
                    return false;
            }
        }
        return false;
    }

    /**
     * Invert this halfspace to contain the other half of the space.
     * 
     * @return this
     */
    public HalfSpace3d invert()
    {
        plane.setA(-plane.getA());
        plane.setB(-plane.getB());
        plane.setC(-plane.getC());
        plane.setD(-plane.getD());

        return this;
    }

    /**
     * Create a halfspace perpendicular to the plane containing the given triangle. The halfspace is further defined by
     * the first two points of the triangle (which lie in the border plane); the third point (p3) of the triangle is the
     * general point somewhere in the halfspace.
     * 
     * @param p1 A point in the border plane of the new halfspace.
     * @param p2 A point in the border plane of the new halfspace.
     * @param r A general point of the new halfspace - defines the triangle together with p1, p2.
     * @return The halfspace as defined above.
     */
    public static HalfSpace3d createPerpendicularToTriangle(Point3d p1, Point3d p2, Point3d r)
    {
        Vector3d v1 = new Vector3d(p2.x - p1.x, p2.y - p1.y, p2.z - p1.z);
        Vector3d v2 = new Vector3d(r.x - p1.x, r.y - p1.y, r.z - p1.z);
        Vector3d c = new Vector3d();
        c.cross(v1, v2);
        c.normalize(); // added to improve robustness against rounding errors
        c.add(p1);
        Point3d p = new Point3d(c);

        return new HalfSpace3d(p1, p2, p, r);
    }

    /**
     * Create a halfspace perpendicular to the plane containing the given triangle. The halfspace is further defined by
     * the first two points of the triangle (which lie in the border plane); the third point (r) of the triangle is the
     * general point somewhere in the halfspace.
     * 
     * @param p1x x-coordinate of a point in the border plane of the new halfspace.
     * @param p1y y-coordinate of a point in the border plane of the new halfspace.
     * @param p1z z-coordinate of a point in the border plane of the new halfspace.
     * @param p2x x-coordinate of a point in the border plane of the new halfspace.
     * @param p2y y-coordinate of a point in the border plane of the new halfspace.
     * @param p2z z-coordinate of a point in the border plane of the new halfspace.
     * @param rx x-coordinate of a general point of the new halfspace - defines the triangle together with p1, p2.
     * @param ry y-coordinate of a general point of the new halfspace - defines the triangle together with p1, p2.
     * @param rz z-coordinate of a general point of the new halfspace - defines the triangle together with p1, p2.
     * @return The halfspace as defined above.
     */
    public static HalfSpace3d createPerpendicularToTriangle(double p1x, double p1y, double p1z, double p2x, double p2y,
            double p2z, double rx, double ry, double rz)
    {
        return createPerpendicularToTriangle(new Point3d(p1x, p1y, p1z), new Point3d(p2x, p2y, p2z), new Point3d(rx,
                ry, rz));
    }

    /**
     * @return The border plane of this halfspace.
     */
    public Plane3d getPlane()
    {
        return plane;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((plane == null) ? 0 : plane.hashCode());
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
        HalfSpace3d other = (HalfSpace3d) obj;
        if (plane == null) {
            if (other.plane != null)
                return false;
        } else if (!plane.equals(other.plane))
            return false;
        return true;
    }

    /**
     * Return <code>true</code> if the given halfspance is almost equal to this one.
     * 
     * @param other The halfspace to compare.
     * @return <code>true</code> if the given halfspace is almost equal to this one.
     */
    public boolean epsilonEquals(HalfSpace3d other)
    {
        if (other == null)
            return false;
        return plane.epsilonEquals(other.getPlane());
    }

    @Override
    public String toString()
    {
        return "HalfSpace3d [" + plane.a + "x + " + plane.b + "y + " + plane.c + "z + " + plane.d + " >= 0]";
    }

    @Override
    protected HalfSpace3d clone()
    {
        return new HalfSpace3d(plane.clone());
    }
}

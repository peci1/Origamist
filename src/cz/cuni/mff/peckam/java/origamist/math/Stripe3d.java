/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.math;

import javax.vecmath.Point3d;

/**
 * An infinite 3D stripe defined by two lines and the space between them.
 * 
 * @author Martin Pecka
 */
public class Stripe3d implements Cloneable
{
    /** The halfspace defined by the first line. */
    protected HalfSpace3d hs1;
    /** The halfspace defined by the second line. */
    protected HalfSpace3d hs2;
    /** The plane defined by the two lines. */
    protected Plane3d     plane;

    /** Line defining this stripe. */
    protected Line3d      line1, line2;

    /**
     * @param line1 The first line defining the stripe.
     * @param line2 The second line defining the stripe.
     */
    public Stripe3d(Line3d line1, Line3d line2)
    {
        this.line1 = line1;
        this.line2 = line2;

        Point3d point1 = (Point3d) line1.p.clone();
        point1.add(line1.v);
        hs1 = HalfSpace3d.createPerpendicularToTriangle(line1.p, point1, line2.p);

        Point3d point2 = (Point3d) line2.p.clone();
        point2.add(line2.v);
        hs2 = HalfSpace3d.createPerpendicularToTriangle(line2.p, point2, line1.p);

        plane = new Plane3d(line1.p, point1, line2.p);
    }

    /**
     * Return <code>true</true> if this stripe contains the given point.
     * 
     * @param point The point to locate.
     * @return <code>true</true> if this stripe contains the given point.
     */
    public boolean contains(Point3d point)
    {
        return hs1.contains(point) && hs2.contains(point) && plane.contains(point);
    }

    /**
     * @return the hs1
     */
    public HalfSpace3d getHalfspace1()
    {
        return hs1;
    }

    /**
     * @return the hs2
     */
    public HalfSpace3d getHalfspace2()
    {
        return hs2;
    }

    /**
     * @return the plane
     */
    public Plane3d getPlane()
    {
        return plane;
    }
}

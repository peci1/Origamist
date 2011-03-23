/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.math;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

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
        Vector3d vector1 = new Vector3d(line1.v);
        vector1.normalize();
        point1.add(vector1);
        hs1 = HalfSpace3d.createPerpendicularToTriangle(line1.p, point1, line2.p);

        Point3d point2 = (Point3d) line2.p.clone();
        Vector3d vector2 = new Vector3d(line2.v);
        vector2.normalize();
        point2.add(vector2);
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

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((line1 == null) ? 0 : line1.hashCode());
        result = prime * result + ((line2 == null) ? 0 : line2.hashCode());
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
        Stripe3d other = (Stripe3d) obj;
        if (line1 == null) {
            if (other.line1 != null)
                return false;
        } else if (!line1.equals(other.line1))
            return false;
        if (line2 == null) {
            if (other.line2 != null)
                return false;
        } else if (!line2.equals(other.line2))
            return false;
        return true;
    }

    /**
     * Return <code>true</code> if the given stripe is almost equal to this one.
     * 
     * @param other The stripe to compare.
     * @return <code>true</code> if the given stripe is almost equal to this one.
     */
    public boolean epsilonEquals(Stripe3d other)
    {
        if (other == null)
            return false;

        return (line1.epsilonEquals(other.line1) && line2.epsilonEquals(other.line2))
                || (line1.epsilonEquals(other.line2) && line2.epsilonEquals(other.line1));
    }

    @Override
    protected Stripe3d clone() throws CloneNotSupportedException
    {
        return new Stripe3d(new Line3d(line1), new Line3d(line2));
    }

    @Override
    public String toString()
    {
        return "Stripe3d [line1=" + line1 + ", line2=" + line2 + "]";
    }
}

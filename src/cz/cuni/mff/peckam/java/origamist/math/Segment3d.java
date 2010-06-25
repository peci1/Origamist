/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.math;

import static cz.cuni.mff.peckam.java.origamist.math.MathHelper.EPSILON;
import static java.lang.Math.abs;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

/**
 * A line segment in 3D.
 * 
 * @author Martin Pecka
 */
public class Segment3d extends Line3d
{
    protected Point3d p2;

    public Segment3d(Point3d p1, Point3d p2)
    {
        super(p1, p2);
        this.p2 = p2;
    }

    public Point3d getP1()
    {
        return this.p;
    }

    public Point3d getP2()
    {
        return this.p2;
    }

    @Override
    public Point3d getIntersection(Line3d line)
    {
        Point3d i = super.getIntersection(line);
        if (i == null || Double.isNaN(i.x) || Double.isNaN(i.y) || Double.isNaN(i.z))
            return i;
        Point3d temp = new Point3d(i);
        temp.sub(p);

        double t = temp.x / v.x;
        if (t < -EPSILON || t > 1 + EPSILON)
            return null;
        return i;
    }

    public Point3d getIntersection(Segment3d segment)
    {
        Point3d i = getIntersection((Line3d) segment);
        if (i == null || Double.isNaN(i.x) || Double.isNaN(i.y) || Double.isNaN(i.z))
            return i;

        Point3d i1 = segment.getIntersection((Line3d) this);
        return i1;
    }

    @Override
    public boolean contains(Point3d point)
    {
        Point3d pt = new Point3d();
        pt.sub(point, p);
        Vector3d vt = new Vector3d();
        vt.cross(vt, v);
        Double quotient = MathHelper.vectorQuotient3d(new Vector3d(pt), v);
        // if vt and v are colinear (parallel), the point is contained in this line
        return (abs(vt.x) < EPSILON && abs(vt.y) < EPSILON && abs(vt.z) < EPSILON && quotient != null && quotient >= 0 && quotient <= 1.0);
    }

    @Override
    public String toString()
    {
        Point3d pt = new Point3d(v);
        pt.add(p);
        return "Segment3d [" + p + " + t*" + v + "] [" + p + ", " + pt + "]";
    }

}

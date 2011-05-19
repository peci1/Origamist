/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.math;

import static cz.cuni.mff.peckam.java.origamist.math.MathHelper.EPSILON;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Point2d;
import javax.vecmath.Vector2d;
import javax.vecmath.Vector3d;

import org.apache.log4j.Logger;

/**
 * A representation of a 2D triangle.
 * 
 * @author Martin Pecka
 */
public class Triangle2d implements Cloneable
{
    /** A vertex of the triangle. */
    protected Point2d p1, p2, p3;

    /** A halfplane defined by one edge of the triangle. */
    protected HalfPlane2d hp1, hp2, hp3;

    /** Edge of the triangle. s_i = (p_(i+1 mod 3) - p_i). */
    protected Segment2d   s1, s2, s3;

    /** Precomputed values for obtaining barycentric coordinates of points. */
    protected double      b1, b2, b3, b4;

    /**
     * Create a triangle from the given points.
     * 
     * @param p1 A vertex.
     * @param p2 A vertex.
     * @param p3 A vertex.
     * @throws IllegalArgumentException If the points are colinear.
     */
    public Triangle2d(Point2d p1, Point2d p2, Point2d p3) throws IllegalArgumentException
    {
        setPoints(p1, p2, p3);
    }

    /**
     * Create a triangle from the given points.
     * 
     * @param p1x A vertex's x coordinte.
     * @param p1y A vertex's y coordinte.
     * @param p2x A vertex's x coordinte.
     * @param p2y A vertex's y coordinte.
     * @param p3x A vertex's x coordinte.
     * @param p3y A vertex's y coordinte.
     * @throws IllegalArgumentException If the points are colinear.
     */
    public Triangle2d(double p1x, double p1y, double p2x, double p2y, double p3x, double p3y)
            throws IllegalArgumentException
    {
        this(new Point2d(p1x, p1y), new Point2d(p2x, p2y), new Point2d(p3x, p3y));
    }

    /**
     * Change the points of this triangle. Only non-null points will be changed.
     * 
     * @param p1 A vertex or <code>null</code> if that vertex should be left untouched.
     * @param p2 A vertex or <code>null</code> if that vertex should be left untouched.
     * @param p3 A vertex or <code>null</code> if that vertex should be left untouched.
     * 
     * @throws IllegalArgumentException If the points are colinear.
     */
    public void setPoints(Point2d p1, Point2d p2, Point2d p3) throws IllegalArgumentException
    {
        if (p1 != null)
            this.p1 = p1;
        if (p2 != null)
            this.p2 = p2;
        if (p3 != null)
            this.p3 = p3;

        if (new Line2d(this.p1, this.p2).contains(this.p3)) {
            throw new IllegalArgumentException("Trying to define a triangle by three colinear points.");
        }

        s1 = new Segment2d(p1, p2);
        s2 = new Segment2d(p2, p3);
        s3 = new Segment2d(p3, p1);

        hp1 = new HalfPlane2d(p1, p2, p3);
        hp2 = new HalfPlane2d(p2, p3, p1);
        hp3 = new HalfPlane2d(p3, p1, p2);

        // see http://en.wikipedia.org/wiki/Barycentric_coordinates_%28mathematics%29
        double oneOverDetT = 1.0 / ((this.p1.x - this.p3.x) * (this.p2.y - this.p3.y) - (this.p2.x - this.p3.x)
                * (this.p1.y - this.p3.y));
        b1 = (this.p2.y - this.p3.y) * oneOverDetT;
        b2 = (this.p3.x - this.p2.x) * oneOverDetT;
        b3 = (this.p3.y - this.p1.y) * oneOverDetT;
        b4 = (this.p1.x - this.p3.x) * oneOverDetT;
    }

    /**
     * Return the coordinates of the first point.
     * 
     * @return The coordinates of the first point.
     */
    public Point2d getP1()
    {
        return p1;
    }

    /**
     * Return the coordinates of the second point.
     * 
     * @return The coordinates of the second point.
     */
    public Point2d getP2()
    {
        return p2;
    }

    /**
     * Return the coordinates of the third point.
     * 
     * @return The coordinates of the third point.
     */
    public Point2d getP3()
    {
        return p3;
    }

    /**
     * @return The vertices of the triangle.
     */
    public Point2d[] getVertices()
    {
        return new Point2d[] { p1, p2, p3 };
    }

    /**
     * @return The edge of the triangle.
     */
    public Segment2d getS1()
    {
        return s1;
    }

    /**
     * @return The edge of the triangle.
     */
    public Segment2d getS2()
    {
        return s2;
    }

    /**
     * @return The edge of the triangle.
     */
    public Segment2d getS3()
    {
        return s3;
    }

    /**
     * @return The edges of the triangle.
     */
    public Segment2d[] getEdges()
    {
        return new Segment2d[] { s1, s2, s3 };
    }

    /**
     * True if this triangle contains the given point (even on its border).
     * 
     * @param point The point to check.
     * @return Whether the given point lies inside this triangle or not.
     */
    public boolean contains(Point2d point)
    {
        return hp1.contains(point) && hp2.contains(point) && hp3.contains(point);
    }

    /**
     * Returns true if the given triangle has a common edge with this triangle.
     * 
     * If <code>strict</code> is true, then the edges must match exactly. If it is false, it is sufficient that the
     * edges overlap.
     * 
     * @param t The triangle to try to find common edge with.
     * @param strict If true, then the edges must match exactly. If it is false, it is sufficient that the edges
     *            overlap.
     * @return true if the given triangle has a common edge with this triangle.
     */
    public boolean hasCommonEdge(Triangle2d t, boolean strict)
    {
        for (Segment2d edge1 : getEdges()) {
            for (Segment2d edge2 : t.getEdges()) {
                if (strict) {
                    if (edge1.epsilonEquals(edge2, true))
                        return true;
                } else {
                    if (edge1.overlaps(edge2))
                        return true;
                }
            }
        }
        return false;
    }

    /**
     * Return <code>true</code> if the given point lies in one of the sides of this triangle.
     * 
     * @param point The point to check.
     * @return <code>true</code> if the given point lies in one of the sides of this triangle.
     */
    public boolean sidesContain(Point2d point)
    {
        return s1.contains(point) || s2.contains(point) || s3.contains(point);
    }

    /**
     * Return the intersection points of this triangle and the given line.
     * <p>
     * Note that some heuristic is performed (such as "magnetizing" edges and vertices), so it generally doesn't hold
     * that the returned segment is a subsegment of the argument (it can be probably 10*EPSILON-different).
     * 
     * @param line The line to get intersections with.
     * @return A segment that defines the intersection of the given line (or segment) and this triangle (the segment can
     *         be zero-length), or <code>null</code>, if no intersection exists. A segment start or end inside the
     *         triangle is also taken as an intersection.
     */
    public Segment2d getIntersection(Line2d line)
    {

        Segment2d intersection = null;
        List<Point2d> intersections = new ArrayList<Point2d>(3);

        for (Segment2d s : getEdges()) { // find intersections with edges
            intersection = s.getIntersection(line);
            if (intersection != null && intersection.v.epsilonEquals(new Vector2d(), EPSILON)) {
                // the point->param->point conversion is done to be as close to the edge as possible
                double param = s.getParameterForPoint(intersection.p);
                // make vertices magnetic
                if (param < 1 * EPSILON) {
                    intersections.add(s.getPointForParameter(0));
                } else if (param > 1 - 1 * EPSILON) {
                    intersections.add(s.getPointForParameter(1));
                } else {
                    intersections.add(s.getPointForParameter(param));
                }
            } else if (intersection != null) {
                // the line lies on the same line as the edge and they have nonempty intersection - we can return
                return intersection.getIntersection(s);
            }
        }

        if (line instanceof Segment2d) {
            // a segment can start or end inside the triangle
            for (Point2d p : ((Segment2d) line).getPoints()) {
                if (this.contains(p)/* && !sidesContain(p) */) {
                    intersections.add(p);
                }
            }
        }

        // rounding erros may affect the method a lot, so ensure it is a little more tolerant
        MathHelper.removeEpsilonEqualPoints2d(intersections, 2d * EPSILON);

        // if there is a nearby point contained in an edge of the triangle, then retain only that on-edge point and
        // remove all nearby points
        for (int i = 0; i < intersections.size(); i++) {
            if (!sidesContain(intersections.get(i))) {
                Point2d substitution = null;
                for (int j = 0; j < intersections.size(); j++) {
                    if (j == i)
                        continue;
                    if (sidesContain(intersections.get(j))
                            && intersections.get(i).distance(intersections.get(j)) < 10d * EPSILON) {
                        substitution = intersections.get(j);
                        break;
                    }
                }
                if (substitution != null) {
                    intersections.remove(i--);
                }
            }
        }

        double i = 2d;
        while (intersections.size() > 2 && i < 10d) {
            MathHelper.removeEpsilonEqualPoints2d(intersections, i++ * EPSILON);
        }
        if (i > 2d)
            Logger.getLogger(getClass()).warn(
                    "Used " + (i - 1)
                            + "*EPSILON for joining intersection points. The resulting intersection points are "
                            + intersections);

        if (intersections.size() == 2) {
            return new Segment2d(intersections.get(0), intersections.get(1));
        } else if (intersections.size() == 1) {
            return new Segment2d(intersections.get(0), intersections.get(0));
        } else if (intersections.size() == 0) {
            return null;
        } else {
            throw new IllegalStateException("Illegal count of intersections of a line and triangle: "
                    + intersections.size());
        }
    }

    /**
     * Return the point corresponding to the given barycentric coordinates in this triangle.
     * 
     * @param b The barycentric coordinates to convert.
     * @return The point corresponding to the given barycentric coordinates in this triangle.
     */
    public Point2d interpolatePointFromBarycentric(Vector3d b)
    {
        Point2d result = new Point2d();
        result.x = b.x * p1.x + b.y * p2.x + b.z * p3.x;
        result.y = b.x * p1.y + b.y * p2.y + b.z * p3.y;
        return result;
    }

    /**
     * Returns the barycentric coordinates of the given point in this triangle.
     * 
     * @param p The point to get barycentric coordinates for.
     * @return The barycentric coordinates of the given point.
     */
    public Vector3d getBarycentricCoords(Point2d p)
    {
        Vector3d result = new Vector3d();
        result.x = b1 * (p.x - p3.x) + b2 * (p.y - p3.y);
        result.y = b3 * (p.x - p3.x) + b4 * (p.y - p3.y);
        result.z = 1 - result.x - result.y;
        return result;
    }

    @Override
    public Triangle2d clone()
    {
        return new Triangle2d(p1.x, p1.y, p2.x, p2.y, p3.x, p3.y);
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((p1 == null) ? 0 : p1.hashCode());
        result = prime * result + ((p2 == null) ? 0 : p2.hashCode());
        result = prime * result + ((p3 == null) ? 0 : p3.hashCode());
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
        Triangle2d other = (Triangle2d) obj;
        if (p1 == null) {
            if (other.p1 != null)
                return false;
        } else if (!p1.equals(other.p1))
            return false;
        if (p2 == null) {
            if (other.p2 != null)
                return false;
        } else if (!p2.equals(other.p2))
            return false;
        if (p3 == null) {
            if (other.p3 != null)
                return false;
        } else if (!p3.equals(other.p3))
            return false;
        return true;
    }

    /**
     * Return <code>true</code> if the given triangle is almost equal to this one.
     * 
     * @param other The triangle to compare.
     * @return <code>true</code> if the given triangle is almost equal to this one.
     */
    public boolean epsilonEquals(Triangle2d other)
    {
        if (other == null)
            return false;
        return p1.epsilonEquals(other.p1, EPSILON) && p2.epsilonEquals(other.p2, EPSILON)
                && p3.epsilonEquals(other.p3, EPSILON);
    }

    @Override
    public String toString()
    {
        return "Triangle2d [" + p1 + ", " + p2 + ", " + p3 + "]";
    }

}

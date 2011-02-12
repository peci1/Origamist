/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.math;

import static cz.cuni.mff.peckam.java.origamist.math.MathHelper.EPSILON;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

/**
 * A (possibly non-convex and hole-containing, but connected) polygon in 3D space. All the triangles the polygon
 * consists of must lie in the same plane.
 * 
 * @author Martin Pecka
 * 
 * @param T The type of the triangles this polygon consists of.
 */
public class Polygon3d<T extends Triangle3d>
{
    /** The triangles the polygon consists of. */
    protected HashSet<T>                    triangles = new HashSet<T>();

    /** The plane the polygon lies in. */
    protected Plane3d                       plane     = null;

    /** The list of all neighboring triangles by their common edges. */
    protected Hashtable<Segment3d, List<T>> neighbors = new Hashtable<Segment3d, List<T>>();

    /**
     * Create a new polygon consisting of the given triangles.
     * 
     * @param triangles The triangles the polygon consists of. The list can be modified by this function.
     * @throws IllegalStateException If the resulting polygon either wouldn't be connected or one of the triangles
     *             doesn't lie in the same plane as the first triangle does. In the case this exception is thrown, the
     *             polygon's state will remain the same as before calling this function (eg. this will not try to add
     *             all "valid" triangles from the given list, but it either accepts all or none of them).
     */
    public Polygon3d(T... triangles) throws IllegalStateException
    {
        this(Arrays.asList(triangles));
    }

    /**
     * Create a new polygon consisting of the given triangles.
     * 
     * @param triangles The triangles the polygon consists of. The list can be modified by this function.
     * @throws IllegalStateException If the resulting polygon either wouldn't be connected or one of the triangles
     *             doesn't lie in the same plane as the first triangle does. In the case this exception is thrown, the
     *             polygon's state will remain the same as before calling this function (eg. this will not try to add
     *             all "valid" triangles from the given list, but it either accepts all or none of them).
     */
    public Polygon3d(List<T> triangles) throws IllegalStateException
    {
        this(new HashSet<T>(triangles));
    }

    /**
     * Create a new polygon consisting of the given triangles.
     * 
     * @param triangles The triangles the polygon consists of. The set can be modified by this function.
     * @throws IllegalStateException If the resulting polygon either wouldn't be connected or one of the triangles
     *             doesn't lie in the same plane as the first triangle does. In the case this exception is thrown, the
     *             polygon's state will remain the same as before calling this function (eg. this will not try to add
     *             all "valid" triangles from the given list, but it either accepts all or none of them).
     */
    public Polygon3d(Set<T> triangles) throws IllegalStateException
    {
        if (triangles.size() > 0) {
            addTriangles(triangles);
        }
    }

    /**
     * Add all the given triangles to the polygon.
     * 
     * @param triangles The triangles to add. The list can be modified by this function.
     * @throws IllegalStateException If the resulting polygon either wouldn't be connected or one of the triangles
     *             doesn't lie in the same plane as the layer does. In the case this exception is thrown, the polygon's
     *             state will remain the same as before calling this function (eg. this will not try to add all "valid"
     *             triangles from the given list, but it either accepts all or none of them).
     */
    public void addTriangles(T... triangles) throws IllegalStateException
    {
        addTriangles(Arrays.asList(triangles));
    }

    /**
     * Add all the given triangles to the polygon.
     * 
     * @param triangles The triangles to add. The list can be modified by this function.
     * @throws IllegalStateException If the resulting polygon either wouldn't be connected or one of the triangles
     *             doesn't lie in the same plane as the layer does. In the case this exception is thrown, the polygon's
     *             state will remain the same as before calling this function (eg. this will not try to add all "valid"
     *             triangles from the given list, but it either accepts all or none of them).
     */
    public void addTriangles(List<T> triangles) throws IllegalStateException
    {
        addTriangles(new HashSet<T>(triangles));
    }

    /**
     * Add all the given triangles to the polygon.
     * 
     * @param triangles The triangles to add. The set can be modified by this function.
     * @throws IllegalStateException If the resulting polygon either wouldn't be connected or one of the triangles
     *             doesn't lie in the same plane as the layer does. In the case this exception is thrown, the polygon's
     *             state will remain the same as before calling this function (eg. this will not try to add all "valid"
     *             triangles from the given list, but it either accepts all or none of them).
     */
    public void addTriangles(Set<T> triangles) throws IllegalStateException
    {
        if (triangles == null || triangles.size() == 0)
            return;

        // be sure not to add a triangle for the second time
        triangles.removeAll(this.triangles);
        if (triangles.size() == 0)
            return; // nothing new to add

        if (plane == null)
            plane = triangles.iterator().next().getPlane();

        // check that all the new triangles lie in the polygon's plane
        for (T t : triangles) {
            Vector3d cross = new Vector3d();
            cross.cross(plane.getNormal(), t.getNormal());
            // if the normals are parallel, then the cross product will be zero
            if (!cross.epsilonEquals(new Vector3d(), EPSILON)) {
                throw new IllegalStateException(
                        "Adding a triangle to polygon but the triangle doesn't lie in the polygon's plane.");
            }
        }

        // backup for the case that the resulting polygon is invalid
        HashSet<T> oldTriangles = new HashSet<T>(this.triangles);
        Hashtable<Segment3d, List<T>> oldNeighbors = new Hashtable<Segment3d, List<T>>(this.neighbors.size());
        for (Entry<Segment3d, List<T>> e : this.neighbors.entrySet()) {
            oldNeighbors.put(e.getKey(), e.getValue() != null ? new LinkedList<T>() : null);
            if (e.getValue() != null) {
                oldNeighbors.get(e.getKey()).addAll(e.getValue());
            }
        }

        this.triangles.addAll(triangles);

        T borderTriangle = null; // the triangle from the "old" polygon which neighbors with a new triangle
        if (oldTriangles.size() == 0)
            // if we have had no triangles in the polygon yet, fake the borderTriangle with any new triangle
            borderTriangle = triangles.iterator().next();

        // update the neighbors list
        for (T t : triangles) {
            for (Segment3d s : t.getEdges()) {
                if (neighbors.get(s) == null)
                    neighbors.put(s, new LinkedList<T>());
                neighbors.get(s).add(t);
                // we can set the borderTriangle if the neighbors list had a previous size of 1 and the neigboring
                // triangle is from the old triangle
                if (borderTriangle == null && neighbors.get(s).size() == 2
                        && oldTriangles.contains(neighbors.get(s).get(0)))
                    borderTriangle = neighbors.get(s).get(0);
            }
        }

        if (borderTriangle == null) {
            this.triangles = oldTriangles;
            this.neighbors = oldNeighbors;
            throw new IllegalStateException(
                    "Trying to add triangles to polygon, but none of them neighbors to the polygon.");
        }

        // check if the polygon is connected (doesn't consist of two or more parts)
        // this can be done by recursively traversing the neighbors of any one triangle and checking that we visited all
        // triangles this way (in a connected space there always exists a path between any two points)
        // two triangles are not considered being neighbors if they only touch in a vertex
        HashSet<T> visited = new HashSet<T>(oldTriangles); // we suppose that the old polygon was connected
        Queue<T> toVisit = new LinkedList<T>();
        toVisit.add(borderTriangle);
        T t;
        while ((t = toVisit.poll()) != null) {
            visited.add(t);
            List<T> neighbors = getNeighbors(t);
            for (T n : neighbors) {
                if (!visited.contains(n))
                    toVisit.add(n);
            }
        }

        if (visited.size() != this.triangles.size()) {
            // if we didn't manage to visit all triangles, the resulting polygon wouldn't be connected
            this.triangles = oldTriangles;
            this.neighbors = oldNeighbors;
            throw new IllegalStateException("Trying to construct a non-connected polygon.");
        }

        if (!additionalAddTrianglesCheck(triangles)) {
            this.triangles = oldTriangles;
            this.neighbors = oldNeighbors;
            throw new IllegalStateException(
                    "The triangles newly added to this polygon don't conform to the rules for new triangles.");
        }
    }

    /**
     * Add the given triangle to the polygon.
     * 
     * For adding just one triangle, this is more efficient than {@link Polygon3d#addTriangles(Set)}.
     * 
     * @param triangle The triangle to add.
     * @throws IllegalStateException If the resulting polygon either wouldn't be connected or the triangle doesn't lie
     *             in the same plane as the layer does. In the case this exception is thrown, the polygon's state will
     *             remain the same as before calling this function.
     */
    public void addTriangle(T triangle) throws IllegalStateException
    {
        if (triangle == null)
            return;

        // be sure not to add the triangle for the second time
        if (this.triangles.contains(triangle))
            return; // nothing new to add

        if (plane == null) {
            plane = triangle.getPlane();
        } else {
            // check that the new triangle lies in the polygon's plane
            Vector3d cross = new Vector3d();
            cross.cross(plane.getNormal(), triangle.getNormal());
            // if the normals are parallel, then the cross product will be zero
            if (!cross.epsilonEquals(new Vector3d(), EPSILON)) {
                throw new IllegalStateException(
                        "Adding a triangle to polygon but the triangle doesn't lie in the polygon's plane.");
            }
        }

        // backup for the case that the resulting polygon is invalid
        HashSet<T> oldTriangles = new HashSet<T>(this.triangles);
        Hashtable<Segment3d, List<T>> oldNeighbors = new Hashtable<Segment3d, List<T>>(this.neighbors.size());
        for (Entry<Segment3d, List<T>> e : this.neighbors.entrySet()) {
            oldNeighbors.put(e.getKey(), e.getValue() != null ? new LinkedList<T>() : null);
            if (e.getValue() != null) {
                oldNeighbors.get(e.getKey()).addAll(e.getValue());
            }
        }

        this.triangles.add(triangle);

        boolean neighborsToOldPolygon = false;
        if (oldTriangles.size() == 0)
            // if we have had no triangles in the polygon yet, set this flag to true, since it's useless in this case
            neighborsToOldPolygon = true;

        // update the neighbors list
        for (Segment3d s : triangle.getEdges()) {
            if (neighbors.get(s) == null)
                neighbors.put(s, new LinkedList<T>());
            neighbors.get(s).add(triangle);
            // if the neighbors list contains two items for an edge of the new triangle, then the second item must be a
            // triangle from the old polygon
            if (!neighborsToOldPolygon && neighbors.get(s).size() == 2)
                neighborsToOldPolygon = true;
        }

        if (!neighborsToOldPolygon) {
            this.triangles = oldTriangles;
            this.neighbors = oldNeighbors;
            throw new IllegalStateException(
                    "Trying to add a triangle to polygon, but it doesn't neighbor to the polygon.");
        }

        HashSet<T> set = new HashSet<T>(1);
        set.add(triangle);
        if (!additionalAddTrianglesCheck(set)) {
            this.triangles = oldTriangles;
            this.neighbors = oldNeighbors;
            throw new IllegalStateException(
                    "The triangle newly added to this polygon doesn't conform to the rules for new triangles.");
        }
    }

    /**
     * Remove all the given triangles from the polygon. Triangles not present in the polygon are ignored.
     * 
     * @param triangles The triangles to remove. The set can be modified by this function.
     * @throws IllegalStateException If the resulting polygon wouldn't be connected. In the case this exception is
     *             thrown, the polygon's state will remain the same as before calling this function (eg. this will not
     *             try to remove all "valid" triangles from the given list, but it either accepts all or none of them).
     */
    public void removeTriangles(Set<T> triangles) throws IllegalStateException
    {
        if (triangles == null || triangles.size() == 0)
            return;

        // ignore triangles not present in the polygon
        try {
            triangles.retainAll(this.triangles);
        } catch (UnsupportedOperationException e) {
            Iterator<T> it = triangles.iterator();
            while (it.hasNext()) {
                T t = it.next();
                if (!this.triangles.contains(t))
                    it.remove();
            }
        }
        if (triangles.size() == 0)
            return; // nothing to remove

        if (triangles.size() == this.triangles.size()) {
            // we want to remove all triangles
            this.triangles.clear();
            this.neighbors.clear();
            plane = null;
            return;
        }

        // backup for the case that the resulting polygon is invalid
        HashSet<T> oldTriangles = new HashSet<T>(this.triangles);
        Hashtable<Segment3d, List<T>> oldNeighbors = new Hashtable<Segment3d, List<T>>(this.neighbors.size());
        for (Entry<Segment3d, List<T>> e : this.neighbors.entrySet()) {
            oldNeighbors.put(e.getKey(), e.getValue() != null ? new LinkedList<T>() : null);
            if (e.getValue() != null) {
                oldNeighbors.get(e.getKey()).addAll(e.getValue());
            }
        }

        this.triangles.removeAll(triangles);

        // remove the triangles from the neighbors list
        for (T t : triangles) {
            for (Segment3d s : t.getEdges()) {
                this.neighbors.get(s).remove(t);
                if (this.neighbors.get(s).size() == 0)
                    this.neighbors.put(s, null);
            }
        }

        // check if the polygon is connected (doesn't consist of two or more parts)
        // this can be done by recursively traversing the neighbors of any one triangle and checking that we visited all
        // triangles this way (in a connected space there always exists a path between any two points)
        // two triangles are not considered being neighbors if they only touch in a vertex
        HashSet<T> visited = new HashSet<T>(this.triangles.size());
        Queue<T> toVisit = new LinkedList<T>();
        toVisit.add(this.triangles.iterator().next());
        T t;
        while ((t = toVisit.poll()) != null) {
            visited.add(t);
            List<T> neighbors = getNeighbors(t);
            for (T n : neighbors) {
                if (!visited.contains(n))
                    toVisit.add(n);
            }
        }

        if (visited.size() != this.triangles.size()) {
            // if we didn't manage to visit all triangles, the resulting polygon wouldn't be connected
            this.triangles = oldTriangles;
            this.neighbors = oldNeighbors;
            throw new IllegalStateException("Trying to construct a non-connected polygon.");
        }

        if (!additionalRemoveTrianglesCheck(triangles)) {
            this.triangles = oldTriangles;
            this.neighbors = oldNeighbors;
            throw new IllegalStateException(
                    "The triangles removed from this polygon don't conform to the rules for removed triangles.");
        }
    }

    /**
     * Remove the given triangle from the polygon. If it is not present in the polygon, nothing happens.
     * 
     * This function is no more effective than calling {@link Polygon3d#removeTriangles(Set)} with a one-element set.
     * 
     * @param triangle The triangle to remove.
     * @throws IllegalStateException If the resulting polygon wouldn't be connected. In the case this exception is
     *             thrown, the polygon's state will remain the same as before calling this function.
     */
    public void removeTriangle(T triangle) throws IllegalStateException
    {
        HashSet<T> set = new HashSet<T>(1);
        set.add(triangle);
        removeTriangles(set);
    }

    /**
     * Performs additional checks on the newly added triangles.
     * 
     * This is intended to be used by subclasses to specify more precisely the rules for adding new triangles.
     * 
     * @param triangles The newly triangles.
     * @return <code>true</code> if all the checks were ok.
     */
    protected boolean additionalAddTrianglesCheck(Set<T> triangles)
    {
        return true;
    }

    /**
     * Performs additional checks on the removed triangles.
     * 
     * This is intended to be used by subclasses to specify more precisely the rules for removing triangles.
     * The check is called in the time when this.triangles and this.neighbors don't contain the removed triangles.
     * 
     * @param triangles The removed triangles.
     * @return <code>true</code> if all the checks were ok.
     */
    protected boolean additionalRemoveTrianglesCheck(Set<T> triangles)
    {
        return true;
    }

    /**
     * Return all triangles that have a common edge with the given triangle.
     * 
     * @param triangle The triangle to find neighbors for.
     * @return All triangles that have a common edge with the given triangle.
     */
    public List<T> getNeighbors(T triangle)
    {
        HashSet<T> neighbors = new HashSet<T>(3);
        for (Segment3d s : new Segment3d[] { triangle.s1, triangle.s2, triangle.s3 }) {
            neighbors.addAll(this.neighbors.get(s));
        }
        neighbors.remove(triangle);
        return new LinkedList<T>(neighbors);
    }

    /**
     * @return An unmodifiable set of the triangles this polygon consists of.
     */
    public Set<T> getTriangles()
    {
        return Collections.unmodifiableSet(triangles);
    }

    /**
     * Tell whether this polygon contains the given point.
     * 
     * @param point The point to check.
     * @return <code>true</code> if this polygon contains the given point.
     */
    public boolean contains(Point3d point)
    {
        // TODO maybe ineffective
        for (T t : triangles) {
            if (t.contains(point))
                return true;
        }
        return false;
    }

    /**
     * Return the segments that are the intersection of the given line and this polygon. If a part of the intersection
     * would be a point, then a segment with zero direction vector will appear in the list.
     * 
     * No two returned segments will have a common point. All of them will have their direction vector pointing in the
     * same direction (the same direction where points the line's direction vector).
     * 
     * @param line The line we search intersections with.
     * @return the segments that are the intersection of the given line and this polygon. If a part of the intersection
     *         would be a point, then a segment with zero direction vector will appear in the list.
     */
    public List<Segment3d> getIntersection(final Line3d line)
    {
        // idea: find intersections with triangles, sort them as they go along the line, and connect all segments that
        // can be connected into one new segment

        class SegmentWithParameters
        {
            Segment3d segment;
            double    p1p, p2p;

            public SegmentWithParameters(Segment3d segment)
            {
                this.segment = segment;
                p1p = line.getParameterForPoint(segment.p);
                p2p = line.getParameterForPoint(segment.p2);

                // make sure the segment is oriented in the same direction as the line's direction vector
                if (p1p - p2p > EPSILON) {
                    this.segment = new Segment3d(segment.p2, segment.p);
                    double tmp = p2p;
                    p2p = p1p;
                    p1p = tmp;
                }
            }
        }

        List<SegmentWithParameters> intersections = new LinkedList<SegmentWithParameters>();
        for (Triangle3d t : triangles) {
            Segment3d intersection = t.getIntersection(line);
            if (intersection != null)
                intersections.add(new SegmentWithParameters(intersection));
        }

        if (intersections.size() == 0)
            return new LinkedList<Segment3d>();
        if (intersections.size() == 1)
            return new LinkedList<Segment3d>(Arrays.asList(new Segment3d[] { intersections.get(0).segment }));

        // sort the segments according to the parameters of the border points
        // this means the segments will be ordered "as they go along the line"
        Collections.sort(intersections, new Comparator<SegmentWithParameters>() {
            @Override
            public int compare(SegmentWithParameters o1, SegmentWithParameters o2)
            {
                double o1min = Math.min(o1.p1p, o1.p2p);
                double o2min = Math.min(o2.p1p, o2.p2p);

                double diff = o1min - o2min;
                return (diff < -EPSILON ? -1 : (diff > EPSILON ? 1 : 0));
            }
        });

        // with the segments sorted we can now take one each time and try to connect it with the previous segment to get
        // only the longest segments possible
        LinkedList<Segment3d> result = new LinkedList<Segment3d>();
        for (SegmentWithParameters s : intersections) {
            if (result.size() == 0 || !result.getLast().p2.epsilonEquals(s.segment.p, EPSILON)) {
                result.add(s.segment);
            } else {
                Segment3d last = result.getLast();
                last = new Segment3d(last.p, s.segment.p2);
                result.removeLast();
                result.add(last);
            }
        }

        return result;
    }
}

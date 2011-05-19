/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.modelstate;

import static cz.cuni.mff.peckam.java.origamist.math.MathHelper.EPSILON;
import static java.lang.Math.abs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;

import javax.media.j3d.GeometryArray;
import javax.media.j3d.LineArray;
import javax.media.j3d.TriangleArray;
import javax.vecmath.Point2d;
import javax.vecmath.Point2f;
import javax.vecmath.Point3d;
import javax.vecmath.TexCoord2f;
import javax.vecmath.Vector2d;
import javax.vecmath.Vector3d;

import org.apache.log4j.Logger;

import cz.cuni.mff.peckam.java.origamist.exceptions.InvalidOperationException;
import cz.cuni.mff.peckam.java.origamist.math.HalfSpace3d;
import cz.cuni.mff.peckam.java.origamist.math.IntersectionWithTriangle;
import cz.cuni.mff.peckam.java.origamist.math.Line2d;
import cz.cuni.mff.peckam.java.origamist.math.Line3d;
import cz.cuni.mff.peckam.java.origamist.math.MathHelper;
import cz.cuni.mff.peckam.java.origamist.math.Plane3d;
import cz.cuni.mff.peckam.java.origamist.math.Polygon3d;
import cz.cuni.mff.peckam.java.origamist.math.Segment2d;
import cz.cuni.mff.peckam.java.origamist.math.Segment3d;
import cz.cuni.mff.peckam.java.origamist.math.Stripe3d;
import cz.cuni.mff.peckam.java.origamist.math.Triangle2d;
import cz.cuni.mff.peckam.java.origamist.math.Triangle3d;
import cz.cuni.mff.peckam.java.origamist.model.Origami;
import cz.cuni.mff.peckam.java.origamist.model.Step;
import cz.cuni.mff.peckam.java.origamist.model.UnitDimension;
import cz.cuni.mff.peckam.java.origamist.model.UnitHelper;
import cz.cuni.mff.peckam.java.origamist.model.jaxb.Unit;
import cz.cuni.mff.peckam.java.origamist.utils.ChangeNotification;
import cz.cuni.mff.peckam.java.origamist.utils.ObservableList;
import cz.cuni.mff.peckam.java.origamist.utils.ObservableList.ChangeTypes;
import cz.cuni.mff.peckam.java.origamist.utils.Observer;

/**
 * The internal state of the model after some steps.
 * 
 * @author Martin Pecka
 */
public class ModelState implements Cloneable
{
    /**
     * Folds on this paper.
     */
    protected ObservableList<Fold>                 folds                 = new ObservableList<Fold>();

    /** Cache for arrays of the lines representing folds. */
    protected LineArray[]                          foldLineArrays        = null;

    /**
     * If true, the value of foldLineArray doesn't have to be consistent and a call to updateLineArray is needed.
     */
    protected boolean                              foldLineArraysDirty   = true;

    /** The triangles this model state consists of. */
    protected ObservableList<ModelTriangle>        triangles             = new ObservableList<ModelTriangle>();

    /** The layers of the paper. */
    protected ObservableList<Layer>                layers                = new ObservableList<Layer>();

    /** The list of markers to be displayed. */
    protected ObservableList<Marker>               markers               = new ObservableList<Marker>();

    /** The mapping of triangles to their containing layer. Automatically updated when <code>layers</code> change */
    protected Hashtable<ModelTriangle, Layer>      trianglesToLayers     = new Hashtable<ModelTriangle, Layer>();

    /**
     * A cache for quick finding of a 3D triangle corresponding to the given 2D triangle. Automatically updated when
     * <code>triangles</code> change.
     */
    protected Hashtable<Triangle2d, ModelTriangle> paperToSpaceTriangles = new Hashtable<Triangle2d, ModelTriangle>();

    /** Cache for finding 3D locations of points corresponding to 2D points on the paper. */
    protected Hashtable<Point2d, Point3d>          paperToSpacePoint     = new Hashtable<Point2d, Point3d>();

    /**
     * The triangles the model state consists of. Each array component contains triangles of one layer. Indices in the
     * array correspond to indices in the layer list. This representation can be directly used by Java3D.
     */
    protected TriangleArray[]                      trianglesArrays       = null;

    /**
     * If true, the value of trianglesArrays doesn't have to be consistent and a call to updateVerticesArray is needed.
     */
    protected boolean                              trianglesArraysDirty  = true;

    /** The data from markers needed for rendering. This list should be automatically handled by the markers list. */
    protected List<MarkerRenderData>               markerData            = new LinkedList<MarkerRenderData>();

    /** If true, the 3D positions of markers need to be recomputed before returning them to some caller. */
    protected boolean                              markersDirty          = true;

    /**
     * Rotation of the model (around the axis from eyes to display) in radians.
     */
    protected double                               rotationAngle         = 0;

    /**
     * The angle the model is viewed from (angle between eyes and the unfolded paper surface) in radians.
     * 
     * PI/2 means top view, -PI/2 means bottom view
     */
    protected double                               viewingAngle          = Math.PI / 2.0;

    /** The normal of the screen. Originally it is a vector of (0,0,1) and rotation and viewing angle are applied to it. */
    protected Vector3d                             screenNormal          = null;

    /**
     * The step this state belongs to.
     */
    protected Step                                 step;

    /**
     * The origami model which is this the state of.
     */
    protected Origami                              origami;

    /**
     * The number of steps a foldline remains visible.
     */
    protected int                                  stepBlendingTreshold  = 5;

    public ModelState()
    {
        addObservers();
    }

    /**
     * Add all the needed observers to this state's observable fields.
     */
    protected void addObservers()
    {
        folds.addObserver(new Observer<Fold>() {
            @Override
            public void changePerformed(ChangeNotification<? extends Fold> change)
            {
                ModelState.this.foldLineArraysDirty = true;

                if (change.getChangeType() == ChangeTypes.ADD) {
                    change.getItem().lines.addObserver(new Observer<FoldLine>() {
                        @Override
                        public void changePerformed(ChangeNotification<? extends FoldLine> change)
                        {
                            ModelState.this.foldLineArraysDirty = true;
                        }
                    });
                }
            }
        });

        triangles.addObserver(new Observer<ModelTriangle>() {
            @Override
            public void changePerformed(ChangeNotification<? extends ModelTriangle> change)
            {
                ModelState.this.trianglesArraysDirty = true;
                ModelState.this.foldLineArraysDirty = true;
                ModelState.this.markersDirty = true;
                paperToSpacePoint.clear();
                if (change.getChangeType() != ChangeTypes.ADD) {
                    ModelTriangle t = change.getOldItem();
                    paperToSpaceTriangles.remove(t.originalPosition);
                } else if (change.getChangeType() != ChangeTypes.REMOVE) {
                    ModelTriangle t = change.getItem();
                    paperToSpaceTriangles.put(t.originalPosition, t);
                }
            }
        });

        layers.addObserver(new Observer<Layer>() {
            @Override
            public void changePerformed(ChangeNotification<? extends Layer> change)
            {
                if (change.getChangeType() != ChangeTypes.ADD) {
                    Layer old = change.getOldItem();
                    for (ModelTriangle t : old.getTriangles()) {
                        trianglesToLayers.remove(t);
                    }
                    old.clearTrianglesObservers();
                } else if (change.getChangeType() != ChangeTypes.REMOVE) {
                    final Layer layer = change.getItem();
                    for (ModelTriangle t : layer.getTriangles()) {
                        trianglesToLayers.put(t, layer);
                    }
                    layer.addTrianglesObserver(new Observer<ModelTriangle>() {
                        @Override
                        public void changePerformed(ChangeNotification<? extends ModelTriangle> change)
                        {
                            if (change.getChangeType() != ChangeTypes.ADD) {
                                trianglesToLayers.remove(change.getOldItem());
                            } else if (change.getChangeType() != ChangeTypes.REMOVE) {
                                ModelTriangle triangle = change.getItem();
                                trianglesToLayers.put(triangle, layer);
                            }
                        }
                    });
                }
            }

        });

        markers.addObserver(new Observer<Marker>() {
            @Override
            public void changePerformed(ChangeNotification<? extends Marker> change)
            {
                if (change.getChangeType() != ChangeTypes.ADD)
                    markerData.remove(change.getOldItem().getRenderData());
                if (change.getChangeType() != ChangeTypes.REMOVE)
                    markerData.add(change.getItem().getRenderData());
            }
        });
    }

    /**
     * Set the step this model state belongs to.
     * 
     * @param step The step to set.
     */
    public void setStep(Step step)
    {
        this.step = step;
    }

    /**
     * Set the origami model this step will work with.
     * 
     * @param origami The origami model.
     */
    public void setOrigami(Origami origami)
    {
        this.origami = origami;
    }

    /**
     * @return The step this model state belongs to.
     */
    public Step getStep()
    {
        return step;
    }

    /**
     * @return The origami model this step works with.
     */
    public Origami getOrigami()
    {
        return origami;
    }

    /**
     * Takes a point defined in the 2D paper relative coordinates and returns the position of the point in the 3D model
     * state (also in relative coordinates).
     * 
     * This method uses a cache for the points. The cache is cleared everytime a triangle is added or removed.
     * 
     * @param point The 2D paper point to find the corresponding 3D point for.
     * @return The 3D point. The returned copy is a fresh instance, so you can alter it.
     * 
     * @throws IllegalArgumentException If the given point doesn't lie in the paper.
     */
    public Point3d locatePointFromPaperTo3D(Point2d point) throws IllegalArgumentException
    {
        if (!origami.getModel().getPaper().containsRelative(point))
            throw new IllegalArgumentException("locatePointFromPaperTo3D: Given point doesn't lie in the paper: "
                    + point);

        if (paperToSpacePoint.get(point) == null) {
            ModelTriangle containingTriangle = null;
            // TODO possible performance loss, try to use some kind of Voronoi diagram??? But it seems that this section
            // won't be preformance-bottle-neck
            for (ModelTriangle t : triangles) {
                if (t.getOriginalPosition().contains(point)) {
                    containingTriangle = t;
                    break;
                }
            }

            if (containingTriangle == null) {
                Logger.getLogger(getClass()).warn("locatePointFromPaperTo3D: Couldn't locate point " + point);
                return new Point3d();
            }

            Vector3d barycentric = containingTriangle.getOriginalPosition().getBarycentricCoords(point);

            paperToSpacePoint.put(point, new Point3d(containingTriangle.interpolatePointFromBarycentric(barycentric)));
        }
        return paperToSpacePoint.get(point);
    }

    /**
     * Update the contents of the foldLineArrays so that it corresponds to the actual contents of the folds variable.
     */
    @SuppressWarnings("serial")
    protected synchronized void updateLineArrays()
    {

        // HOW THIS METHOD WORKS
        // WE HAVE: a bunch of triangle edges marked as fold lines; a lot of them is duplicated by various folds going
        // through that triangle edge
        // WE WANT: a set of lines, where no two lines could be connected together to form a new narrow line not
        // intersected by another line; also no lines can be duplicated
        // WE DO:
        // 1) take only the newest line for each triangle edge (the newest is the latest in the edge's fold line list)
        // - this makes sure no lines are duplicated
        // 2) connect all segments into the longest narrow lines available
        // - this makes sure no two lines can be connected to form a new narrow line
        // 3) split the lines at their intersections
        // - this makes sure the lines aren't too long

        List<List<ModelSegment>> lines = new ArrayList<List<ModelSegment>>(step.getId() * 3);
        for (int i = 0; i < step.getId() * 3; i++)
            lines.add(new LinkedList<ModelSegment>());

        // STEP 1
        // put only the newest fold lines into lines (it means, if more folds go through a fold line, add only the one
        // with highest originatingStepId)
        int index;
        for (ModelTriangle t : triangles) {
            for (int i = 0; i < 3; i++) {
                List<FoldLine> foldLines = t.getFoldLines(i);
                if (foldLines != null && foldLines.size() > 0) {
                    FoldLine line = foldLines.get(foldLines.size() - 1);
                    if (line.getDirection() != null)
                        index = line.getDirection().ordinal() * line.getFold().getOriginatingStepId();
                    else
                        index = 2 * line.getFold().getOriginatingStepId();
                    lines.get(index).add(new ModelSegment(line));
                }
            }
        }

        // STEP 2
        // now we have a lot of lines split by the boundaries of the triangles, so join all adjacent lines together
        int numLines = 0;
        for (List<ModelSegment> list : lines) {
            if (list.size() == 0)
                continue;
            numLines += list.size();
            // join lines that are parallel and overlap
            // TODO O(n^2) algorithm, couldn't we do it better? (it already has contained some optimizations, but still
            // it's been O(n^2))
            int i = 0;
            for (Iterator<ModelSegment> it = list.iterator(); it.hasNext();) {
                if (i >= list.size() - 1)
                    break;
                ModelSegment seg = it.next();
                boolean merged = false;
                for (ModelSegment other : list.subList(i + 1, list.size())) {
                    if (other.merge(seg)) {
                        it.remove();
                        numLines--;
                        merged = true;
                        break;
                    }
                }

                if (!merged)
                    i++;
            }
        }

        // now flatten the joined lines into linesWhole and prepare linesIntersected for further use
        ModelSegment[] linesWhole = new ModelSegment[numLines];
        List<List<ModelSegment>> linesIntersected = new ArrayList<List<ModelSegment>>(numLines);
        int i = 0;
        for (List<ModelSegment> list : lines) {
            for (final ModelSegment seg : list) {
                linesWhole[i] = seg;
                linesIntersected.add(new ArrayList<ModelSegment>(5) {
                    {
                        add(seg.clone());
                    }
                });
                i++;
            }
        }

        // STEP 3
        // now we have all the parts of narrow lines joined into one line; but we want to get lines split at their
        // intersections with other lines

        // this algorithm runs in O(n^2) time; it just takes pairs of the narrow lines and looks if they intersect; if
        // they do, it subdivides those lines into linesIntersected list and then looks for intersections for the
        // subdivided parts

        // it would be nice to use iterators and foreach loops here, but we need to add elements to the iterated lists
        for (i = 0; i < linesWhole.length - 1; i++) {
            for (int j = i + 1; j < linesWhole.length; j++) {
                // discard lines not intersecting on the paper
                if (linesWhole[i].original.getIntersection(linesWhole[j].original) == null)
                    continue;
                Segment3d intersection = linesWhole[i].getIntersection(linesWhole[j]);
                // if the whole lines intersect, find the intersecting subsegments and slice them
                if (intersection != null && intersection.getVector().epsilonEquals(new Vector3d(), EPSILON)) {
                    Point3d intPoint = intersection.getPoint();
                    List<ModelSegment> lineIntersected = linesIntersected.get(i);
                    k: for (int k = 0; k < lineIntersected.size(); k++) {
                        ModelSegment line = lineIntersected.get(k);
                        ModelSegment split = line.split(intPoint);
                        if (split != null || line.isBorderPoint(intPoint)) {
                            if (split != null) {
                                lineIntersected.add(split);
                                numLines++;
                            }
                            break k;
                        }
                    }
                    List<ModelSegment> otherIntersected = linesIntersected.get(j);
                    l: for (int l = 0; l < otherIntersected.size(); l++) {
                        ModelSegment other = otherIntersected.get(l);
                        ModelSegment split = other.split(intPoint);
                        if (split != null || other.isBorderPoint(intPoint)) {
                            if (split != null) {
                                otherIntersected.add(split);
                                numLines++;
                            }
                            break l;
                        }
                    }
                }
            }
        }

        UnitDimension paperSize = origami.getModel().getPaper().getSize();
        double ratio = UnitHelper.convertTo(Unit.REL, Unit.M, 1, paperSize.getUnit(), paperSize.getMax());

        foldLineArrays = new LineArray[numLines];
        i = 0;
        for (List<ModelSegment> lineIntersected : linesIntersected) {
            for (ModelSegment line : lineIntersected) {
                foldLineArrays[i] = new LineArray(2, GeometryArray.COORDINATES);

                Point3d startPoint = new Point3d(line.getP1());
                startPoint.scale(ratio);
                foldLineArrays[i].setCoordinate(0, startPoint);

                Point3d endPoint = new Point3d(line.getP2());
                endPoint.scale(ratio);
                foldLineArrays[i].setCoordinate(1, endPoint);

                foldLineArrays[i].setUserData(line);

                i++;
            }
        }

        foldLineArraysDirty = false;
    }

    /**
     * Retrurn the line arrays corresponding to the lines on the paper.
     * 
     * @return The line arrays corresponding to the lines on the paper.
     */
    public synchronized LineArray[] getLineArrays()
    {
        if (foldLineArraysDirty)
            updateLineArrays();

        return foldLineArrays;
    }

    /**
     * Update the contents of the trianglesArrays so that it corresponds to the actual model state.
     */
    protected synchronized void updateTrianglesArrays()
    {
        trianglesArrays = new TriangleArray[layers.size()];

        double oneRelInMeters = origami.getModel().getPaper().getOneRelInMeters();
        int index = 0;
        Point3d p;

        for (Layer layer : layers) {
            trianglesArrays[index] = new TriangleArray(layer.getTriangles().size() * 3, TriangleArray.COORDINATES
                    | TriangleArray.TEXTURE_COORDINATE_2);
            trianglesArrays[index].setUserData(layer);

            int i = 0;
            for (ModelTriangle triangle : layer.getTriangles()) {
                p = (Point3d) triangle.getP1().clone();
                p.scale(oneRelInMeters);
                trianglesArrays[index].setCoordinate(3 * i, p);
                trianglesArrays[index].setTextureCoordinate(0, 3 * i, new TexCoord2f(new Point2f(triangle
                        .getOriginalPosition().getP1())));

                p = (Point3d) triangle.getP2().clone();
                p.scale(oneRelInMeters);
                trianglesArrays[index].setCoordinate(3 * i + 1, p);
                trianglesArrays[index].setTextureCoordinate(0, 3 * i + 1, new TexCoord2f(new Point2f(triangle
                        .getOriginalPosition().getP2())));

                p = (Point3d) triangle.getP3().clone();
                p.scale(oneRelInMeters);
                trianglesArrays[index].setCoordinate(3 * i + 2, p);
                trianglesArrays[index].setTextureCoordinate(0, 3 * i + 2, new TexCoord2f(new Point2f(triangle
                        .getOriginalPosition().getP3())));

                i++;
            }
            index++;
        }

        trianglesArraysDirty = false;
    }

    /**
     * Return the triangle arrays.
     * 
     * @return The triangle arrays.
     */
    public synchronized TriangleArray[] getTrianglesArrays()
    {
        if (trianglesArraysDirty)
            updateTrianglesArrays();

        return trianglesArrays;
    }

    /**
     * @return The list of marker data needed for rendering.
     */
    public synchronized List<MarkerRenderData> getMarkerRenderData()
    {
        if (markersDirty)
            updateMarkers();
        return markerData;
    }

    /**
     * Recompute markers 3D positions.
     */
    protected synchronized void updateMarkers()
    {
        for (Marker m : markers)
            m.setPoint3d(locatePointFromPaperTo3D(m.getPoint2d()));
        markersDirty = false;
    }

    /**
     * Performs a valley/mountain fold.
     * 
     * @param direction The direction of the fold - VALLEY/MOUNTAIN.
     * @param startPoint Starting point of the fold (in 2D paper relative coordinates).
     * @param endPoint Ending point of the fold (in 2D paper relative coordinates).
     * @param layerFilter The filter that filters the layers this fold should be made on.
     * @param angle The angle the paper should be bent by (in radians). The value must be in &lt;0, &pi;&gt; interval.
     * 
     * @return The layers of the paper that were rotated as keys, the old layers they are part of as values. Only
     *         contains entries with values accepted by layerFilter (therefore only the "primarily" rotated layers, not
     *         those rotated because they neighbor to a already rotated layer).
     * 
     * @throws IllegalArgumentException If angle isn't in interval &lt;0, &pi;&gt;.
     */
    public Map<Layer, Layer> makeFold(Direction direction, Point2d startPoint, Point2d endPoint,
            LayerFilter layerFilter, double angle)
    {
        return makeFold(direction, startPoint, endPoint, null, layerFilter, angle, null, null);
    }

    /**
     * Performs a valley/mountain fold.
     * 
     * @param direction The direction of the fold - VALLEY/MOUNTAIN.
     * @param startPoint Starting point of the fold (in 2D paper relative coordinates).
     * @param endPoint Ending point of the fold (in 2D paper relative coordinates).
     * @param layerFilter The filter that filters the layers this fold should be made on.
     * @param angle The angle the paper should be bent by (in radians). The value must be in &lt;0, &pi;&gt; interval.
     * @param neighborTest The test for including neighbors when bending.
     * 
     * @return The layers of the paper that were rotated as keys, the old layers they are part of as values. Only
     *         contains entries with values accepted by layerFilter (therefore only the "primarily" rotated layers, not
     *         those rotated because they neighbor to a already rotated layer).
     * 
     * @throws IllegalArgumentException If angle isn't in interval &lt;0, &pi;&gt;.
     */
    public Map<Layer, Layer> makeFold(Direction direction, Point2d startPoint, Point2d endPoint,
            LayerFilter layerFilter, double angle, NeighborInclusionTest neighborTest)
    {
        return makeFold(direction, startPoint, endPoint, null, layerFilter, angle, null, neighborTest);
    }

    /**
     * Performs a valley/mountain fold.
     * 
     * @param direction The direction of the fold - VALLEY/MOUNTAIN.
     * @param startPoint Starting point of the fold (in 2D paper relative coordinates).
     * @param endPoint Ending point of the fold (in 2D paper relative coordinates).
     * @param refPoint A general point in the part of the paper to be bent. Pass <code>null</code> to autocompute so
     *            that the part with less triangles will be rotated.
     * @param layerFilter The filter that filters the layers this fold should be made on.
     * @param angle The angle the paper should be bent by (in radians). The value must be in &lt;0, &pi;&gt; interval.
     * 
     * @return The layers of the paper that were rotated as keys, the old layers they are part of as values. Only
     *         contains entries with values accepted by layerFilter (therefore only the "primarily" rotated layers, not
     *         those rotated because they neighbor to a already rotated layer).
     * 
     * @throws IllegalArgumentException If angle isn't in interval &lt;0, &pi;&gt;.
     */
    public Map<Layer, Layer> makeFold(Direction direction, Point2d startPoint, Point2d endPoint, Point2d refPoint,
            LayerFilter layerFilter, double angle)
    {
        return makeFold(direction, startPoint, endPoint, refPoint, layerFilter, angle, null, null);
    }

    /**
     * Performs a valley/mountain fold.
     * 
     * @param direction The direction of the fold - VALLEY/MOUNTAIN.
     * @param startPoint Starting point of the fold (in 2D paper relative coordinates).
     * @param endPoint Ending point of the fold (in 2D paper relative coordinates).
     * @param refPoint A general point in the part of the paper to be bent. Pass <code>null</code> to autocompute so
     *            that the part with less triangles will be rotated.
     * @param layerFilter The filter that filters the layers this fold should be made on.
     * @param angle The angle the paper should be bent by (in radians). The value must be in &lt;0, &pi;&gt; interval.
     * @param neighborTest The test for including neighbors when bending.
     * 
     * @return The layers of the paper that were rotated as keys, the old layers they are part of as values. Only
     *         contains entries with values accepted by layerFilter (therefore only the "primarily" rotated layers, not
     *         those rotated because they neighbor to a already rotated layer).
     * 
     * @throws IllegalArgumentException If angle isn't in interval &lt;0, &pi;&gt;.
     */
    public Map<Layer, Layer> makeFold(Direction direction, Point2d startPoint, Point2d endPoint, Point2d refPoint,
            LayerFilter layerFilter, double angle, NeighborInclusionTest neighborTest)
    {
        return makeFold(direction, startPoint, endPoint, refPoint, layerFilter, angle, null, neighborTest);
    }

    /**
     * Performs a valley/mountain fold.
     * 
     * @param direction The direction of the fold - VALLEY/MOUNTAIN.
     * @param startPoint Starting point of the fold (in 2D paper relative coordinates).
     * @param endPoint Ending point of the fold (in 2D paper relative coordinates).
     * @param layerFilter The filter that filters the layers this fold should be made on.
     * @param angle The angle the paper should be bent by (in radians). The value must be in &lt;0, &pi;&gt; interval.
     * @param foundAffectedLayers Output parameter. The map of found layers affected by this fold and corresponding
     *            intersection segments. Pass <code>null</code> if you aren't interested in this information. Note that
     *            the list corresponds to layers before bending, so it is practically useful only if
     *            <code>angle == 0</code>.
     * 
     * @return The layers of the paper that were rotated as keys, the old layers they are part of as values. Only
     *         contains entries with values accepted by layerFilter (therefore only the "primarily" rotated layers, not
     *         those rotated because they neighbor to a already rotated layer).
     * 
     * @throws IllegalArgumentException If angle isn't in interval &lt;0, &pi;&gt;.
     */
    public Map<Layer, Layer> makeFold(Direction direction, Point2d startPoint, Point2d endPoint,
            LayerFilter layerFilter, double angle, Map<Layer, Segment3d> foundAffectedLayers)
    {
        return makeFold(direction, startPoint, endPoint, null, layerFilter, angle, foundAffectedLayers, null);
    }

    /**
     * Performs a valley/mountain fold.
     * 
     * @param direction The direction of the fold - VALLEY/MOUNTAIN.
     * @param startPoint Starting point of the fold (in 2D paper relative coordinates).
     * @param endPoint Ending point of the fold (in 2D paper relative coordinates).
     * @param refPoint A general point in the part of the paper to be bent. Pass <code>null</code> to autocompute so
     *            that the part with less triangles will be rotated.
     * @param layerFilter The filter that filters the layers this fold should be made on.
     * @param angle The angle the paper should be bent by (in radians). The value must be in &lt;0, &pi;&gt; interval.
     * @param foundAffectedLayers Output parameter. The map of found layers affected by this fold and corresponding
     *            intersection segments. Pass <code>null</code> if you aren't interested in this information. Note that
     *            the list corresponds to layers before bending, so it is practically useful only if
     *            <code>angle == 0</code>.
     * 
     * @return The layers of the paper that were rotated as keys, the old layers they are part of as values. Only
     *         contains entries with values accepted by layerFilter (therefore only the "primarily" rotated layers, not
     *         those rotated because they neighbor to a already rotated layer).
     * 
     * @throws IllegalArgumentException If angle isn't in interval &lt;0, &pi;&gt;.
     */
    public Map<Layer, Layer> makeFold(Direction direction, Point2d startPoint, Point2d endPoint, Point2d refPoint,
            LayerFilter layerFilter, double angle, Map<Layer, Segment3d> foundAffectedLayers)
    {
        return makeFold(direction, startPoint, endPoint, refPoint, layerFilter, angle, foundAffectedLayers, null);
    }

    /**
     * Performs a valley/mountain fold.
     * 
     * @param direction The direction of the fold - VALLEY/MOUNTAIN.
     * @param startPoint Starting point of the fold (in 2D paper relative coordinates).
     * @param endPoint Ending point of the fold (in 2D paper relative coordinates).
     * @param layerFilter The filter that filters the layers this fold should be made on.
     * @param angle The angle the paper should be bent by (in radians). The value must be in &lt;0, &pi;&gt; interval.
     * @param foundAffectedLayers Output parameter. The map of found layers affected by this fold and corresponding
     *            intersection segments. Pass <code>null</code> if you aren't interested in this information. Note that
     *            the list corresponds to layers before bending, so it is practically useful only if
     *            <code>angle == 0</code>.
     * @param neighborTest The test for including neighbors when bending.
     * 
     * @return The layers of the paper that were rotated as keys, the old layers they are part of as values. Only
     *         contains entries with values accepted by layerFilter (therefore only the "primarily" rotated layers, not
     *         those rotated because they neighbor to a already rotated layer).
     * 
     * @throws IllegalArgumentException If angle isn't in interval &lt;0, &pi;&gt;.
     */
    public Map<Layer, Layer> makeFold(Direction direction, Point2d startPoint, Point2d endPoint,
            LayerFilter layerFilter, double angle, Map<Layer, Segment3d> foundAffectedLayers,
            NeighborInclusionTest neighborTest)
    {
        return makeFold(direction, startPoint, endPoint, null, layerFilter, angle, foundAffectedLayers, neighborTest);
    }

    /**
     * Performs a valley/mountain fold.
     * 
     * @param direction The direction of the fold - VALLEY/MOUNTAIN.
     * @param startPoint Starting point of the fold (in 2D paper relative coordinates).
     * @param endPoint Ending point of the fold (in 2D paper relative coordinates).
     * @param refPoint A general point in the part of the paper to be bent. Pass <code>null</code> to autocompute so
     *            that the part with less triangles will be rotated.
     * @param layerFilter The filter that filters the layers this fold should be made on.
     * @param angle The angle the paper should be bent by (in radians). The value must be in &lt;0, &pi;&gt; interval.
     * @param foundAffectedLayers Output parameter. The map of found layers affected by this fold and corresponding
     *            intersection segments. Pass <code>null</code> if you aren't interested in this information. Note that
     *            the list corresponds to layers before bending, so it is practically useful only if
     *            <code>angle == 0</code>.
     * @param neighborTest The test for including neighbors when bending.
     * 
     * @return The layers of the paper that were rotated as keys, the old layers they are part of as values. Only
     *         contains entries with values accepted by layerFilter (therefore only the "primarily" rotated layers, not
     *         those rotated because they neighbor to a already rotated layer). Only contains entries with values
     *         accepted by layerFilter (therefore only the "primarily" rotated layers, not those rotated because they
     *         neighbor to a already rotated layer).
     * 
     * @throws IllegalArgumentException If angle isn't in interval &lt;0, &pi;&gt;.
     */
    protected Map<Layer, Layer> makeFold(Direction direction, Point2d startPoint, Point2d endPoint, Point2d refPoint,
            LayerFilter layerFilter, double angle, Map<Layer, Segment3d> foundAffectedLayers,
            NeighborInclusionTest neighborTest)
    {
        if (angle < -EPSILON || angle > Math.PI + EPSILON)
            throw new IllegalArgumentException("Cannot pass angles outside <0, PI> interval to makeFold().");

        Point3d start = locatePointFromPaperTo3D(startPoint);
        Point3d end = locatePointFromPaperTo3D(endPoint);
        Point3d ref = (refPoint != null ? locatePointFromPaperTo3D(refPoint) : null);
        ModelSegment segment = new ModelSegment(new Segment3d(start, end), new Segment2d(startPoint, endPoint),
                direction, step.getId());

        final LinkedHashMap<Layer, ModelSegment> layerInts = getLayers(segment);
        final Map<Layer, Direction> foldDirections = new HashMap<Layer, Direction>(layerInts.size());

        // filter out the layers we aren't interested in
        layerFilter.filter(layerInts);

        // cut triangles along the segment and make the appropriate fold lines
        for (Entry<Layer, ModelSegment> entry : layerInts.entrySet())
            makeFoldInLayer(entry.getKey(), direction, entry.getValue(), foldDirections);

        if (foundAffectedLayers != null)
            foundAffectedLayers.putAll(layerInts);

        // bend the paper
        return bendPaper(segment, ref, layerInts, angle, neighborTest, foldDirections);
    }

    /**
     * Bends the paper. Requires that the fold line goes only along triangle edges, not through the interiors of them.
     * <p>
     * To specify the part of the paper that will be rotated, the segment's direction vector is used. Make cross product
     * of the normal of the layer the segment lies in and the direction vector of the segment. The cross product points
     * to the part of the paper that will be moved.
     * 
     * @param segment The segment to bend around. Note that the direction vector of the segment specifies which part of
     *            the paper will be rotated.
     * @param layerInts A map of affected layers and intersections of the fold stripe with them.
     * @param angle The angle the paper should be bent by (in radians). The value must be in &lt;0, &pi;&gt; interval.
     * @param foldDirections The real directions in fold lines in layers.
     * 
     * @return The layers of the paper that were rotated as keys, the old layers they are part of as values. Only
     *         contains entries with values being keys in layerInts (therefore only the "primarily" rotated layers, not
     *         those rotated because they neighbor to a already rotated layer).
     * 
     * @throws IllegalArgumentException If angle isn't in interval &lt;0, &pi;&gt;.
     */
    protected Map<Layer, Layer> bendPaper(ModelSegment segment, Map<Layer, ModelSegment> layerInts, double angle,
            Map<Layer, Direction> foldDirections)
    {
        return bendPaper(segment, null, layerInts, angle, null, foldDirections);
    }

    /**
     * Bends the paper. Requires that the fold line goes only along triangle edges, not through the interiors of them.
     * <p>
     * To specify the part of the paper that will be rotated, the segment's direction vector is used. Make cross product
     * of the normal of the layer the segment lies in and the direction vector of the segment. The cross product points
     * to the part of the paper that will be moved.
     * 
     * @param segment The segment to bend around. Note that the direction vector of the segment specifies which part of
     *            the paper will be rotated.
     * @param layerInts A map of affected layers and intersections of the fold stripe with them.
     * @param angle The angle the paper should be bent by (in radians). The value must be in &lt;0, &pi;&gt; interval.
     * @param neighborTest A callback for narrowing the set of neighbor triangles that should be bent. Pass
     *            <code>null</code> if you don't need this callback.
     * @param foldDirections The real directions in fold lines in layers.
     * 
     * @return The layers of the paper that were rotated as keys, the old layers they are part of as values. Only
     *         contains entries with values being keys in layerInts (therefore only the "primarily" rotated layers, not
     *         those rotated because they neighbor to a already rotated layer).
     * 
     * @throws IllegalArgumentException If angle isn't in interval &lt;0, &pi;&gt;.
     */
    protected Map<Layer, Layer> bendPaper(ModelSegment segment, Map<Layer, ModelSegment> layerInts, double angle,
            NeighborInclusionTest neighborTest, Map<Layer, Direction> foldDirections)
    {
        return bendPaper(segment, null, layerInts, angle, neighborTest, foldDirections);
    }

    /**
     * Bends the paper. Requires that the fold line goes only along triangle edges, not through the interiors of them.
     * <p>
     * To specify the part of the paper that will be rotated, imagine a plane going through <code>segment</code> and
     * perpendicular to the layer <code>segment</code> lies in. This is a halfspace's border plane. Then
     * <code>refPoint</code> specifies the halfspace containing the parts of the paper that are to be bent.
     * 
     * @param segment The segment to bend around. Note that the direction vector of the segment specifies which part of
     *            the paper will be rotated.
     * @param refPoint A reference point lying in the halfspace containing the parts of the paper that are to be bent.
     *            Pass <code>null</code> to autodetermine the part as the one with less triangles. Please ensure
     *            <code>refPoint</code> doesn't lie in the border plane as described above, this will not be checked.
     * @param layerInts A map of affected layers and intersections of the fold stripe with them.
     * @param angle The angle the paper should be bent by (in radians). The value must be in &lt;0, &pi;&gt; interval.
     * @param foldDirections The real directions in fold lines in layers.
     * 
     * @return The layers of the paper that were rotated as keys, the old layers they are part of as values. Only
     *         contains entries with values being keys in layerInts (therefore only the "primarily" rotated layers, not
     *         those rotated because they neighbor to a already rotated layer).
     * 
     * @throws IllegalArgumentException If angle isn't in interval &lt;0, &pi;&gt;.
     */
    protected Map<Layer, Layer> bendPaper(ModelSegment segment, Point3d refPoint, Map<Layer, ModelSegment> layerInts,
            double angle, Map<Layer, Direction> foldDirections)
    {
        return bendPaper(segment, refPoint, layerInts, angle, null, foldDirections);
    }

    /**
     * Bends the paper. Requires that the fold line goes only along triangle edges, not through the interiors of them.
     * <p>
     * To specify the part of the paper that will be rotated, imagine a plane going through <code>segment</code> and
     * perpendicular to the layer <code>segment</code> lies in. This is a halfspace's border plane. Then
     * <code>refPoint</code> specifies the halfspace containing the parts of the paper that are to be bent.
     * 
     * @param segment The segment to bend around. Note that the direction vector of the segment specifies which part of
     *            the paper will be rotated.
     * @param refPoint A reference point lying in the halfspace containing the parts of the paper that are to be bent.
     *            Pass <code>null</code> to autodetermine the part as the one with less triangles. Please ensure
     *            <code>refPoint</code> doesn't lie in the border plane as described above, this will not be checked.
     * @param layerInts A map of affected layers and intersections of the fold stripe with them.
     * @param angle The angle the paper should be bent by (in radians). The value must be in &lt;0, &pi;&gt; interval.
     * @param neighborTest A callback for narrowing the set of neighbor triangles that should be bent. Pass
     *            <code>null</code> if you don't need this callback.
     * @param foldDirections The real directions in fold lines in layers.
     * 
     * @return The layers of the paper that were rotated as keys, the old layers they are part of as values. Only
     *         contains entries with values being keys in layerInts (therefore only the "primarily" rotated layers, not
     *         those rotated because they neighbor to a already rotated layer).
     * 
     * @throws IllegalArgumentException If angle isn't in interval &lt;0, &pi;&gt;.
     */
    protected Map<Layer, Layer> bendPaper(ModelSegment segment, Point3d refPoint, Map<Layer, ModelSegment> layerInts,
            double angle, NeighborInclusionTest neighborTest, Map<Layer, Direction> foldDirections)
    {
        if (angle < -EPSILON || angle > Math.PI + EPSILON)
            throw new IllegalArgumentException("Cannot pass angles outside <0, PI> interval to bendPaper().");

        if (angle < EPSILON || layerInts.size() == 0)
            return new HashMap<Layer, Layer>(0);

        Layer segLayer = null;
        {// find the layer the given segment lies in
            for (Entry<Layer, ModelSegment> entry : layerInts.entrySet()) {
                Segment3d intersection = entry.getValue().getIntersection(segment);
                if (intersection != null && !intersection.getVector().epsilonEquals(new Vector3d(), EPSILON)) {
                    segLayer = entry.getKey();
                }
            }
            if (segLayer == null) {
                throw new IllegalStateException("Cannot find layer in which the segment to bend over lies.");
            }
        }

        Point3d r = refPoint;
        if (r == null) {
            // find a reference point by taking the cross product of the normal of the layer the segment lies in, and
            // the direction vector of the segment; add this vector to a point on the segment to get a general point in
            // the halfspace that contains the layers to be bent

            Vector3d layerNormalSegmentDirCross = new Vector3d();
            layerNormalSegmentDirCross.cross(segLayer.getNormal(), segment.getVector());
            Vector3d v = new Vector3d(layerNormalSegmentDirCross);
            v.normalize();
            r = new Point3d(v);
            r.add(segment.getPoint());
        }

        double angle1 = angle;
        {// invert the rotation angle if it doesn't correspond to the real fold direction
            Direction foldDir = foldDirections.get(segLayer);

            // the halfspace having the layer's plane as its border and defining the half of the space where the layer's
            // normal points
            HalfSpace3d halfspace = new HalfSpace3d(segLayer.getPlane());

            Point3d rotated = MathHelper.rotate(r, segment, angle);

            boolean isInHalfspace = halfspace.contains(rotated);

            // mountain fold should bend the paper "under" the top plane, whereas valley fold should bend it "over" the
            // top plane
            if (foldDir == Direction.MOUNTAIN && isInHalfspace)
                angle1 = -angle1;
            else if (foldDir == Direction.VALLEY && !isInHalfspace)
                angle1 = -angle1;
        }

        Vector3d segNormal = getSegmentNormal(segment);

        Vector3d halfSpaceNormal = new Vector3d();
        halfSpaceNormal.cross(segNormal, segment.getVector());

        // the halfspace that contains the layers to be bent
        HalfSpace3d halfspace = new HalfSpace3d(halfSpaceNormal, segment.getP1());
        if (!halfspace.contains(r))
            halfspace.invert();

        // further we will need to search in layerInts, but the layers will probably change, so we backup the old
        // removed layers here
        HashMap<Layer, Layer> newLayersToOldOnes = new HashMap<Layer, Layer>();

        // fill this queue with triangles from the new layers that need to be bent
        Queue<ModelTriangle> queue = new LinkedList<ModelTriangle>();
        for (Entry<Layer, ModelSegment> layerInt : layerInts.entrySet()) {
            Layer layer = layerInt.getKey();
            ModelSegment splitSegment = layerInt.getValue();

            List<Polygon3d<ModelTriangle>> part1 = new LinkedList<Polygon3d<ModelTriangle>>();
            List<Polygon3d<ModelTriangle>> part2 = new LinkedList<Polygon3d<ModelTriangle>>();
            // split the layer into at least two new ones
            layer.splitPolygon(splitSegment, part1, part2);

            // we want to have the layers to be bent in part1, so we will maybe need to swap them
            boolean swapParts = false;
            if (part1.size() > 0) {
                Triangle3d part1t = part1.get(0).getTriangles().iterator().next();
                if (!(halfspace.contains(part1t.getP1()) && halfspace.contains(part1t.getP2()) && halfspace
                        .contains(part1t.getP3()))) {
                    swapParts = true;
                }
            } else {
                Triangle3d part2t = part2.get(0).getTriangles().iterator().next();
                if (!(halfspace.contains(part2t.getP1()) && halfspace.contains(part2t.getP2()) && halfspace
                        .contains(part2t.getP3()))) {
                    swapParts = true;
                }
            }

            if (part1.size() == 0 && part2.size() > 0) // can happen if bending already bent folds
                swapParts = true;

            if (swapParts) {
                List<Polygon3d<ModelTriangle>> tmp = part1;
                part1 = part2;
                part2 = tmp;
            }

            // remove the old layer and add the new ones
            this.layers.remove(layer);
            for (Polygon3d<ModelTriangle> l : part1) {
                if (l.getTriangles().size() > 0) {
                    Layer newL = new Layer(l);
                    this.layers.add(newL);
                    newLayersToOldOnes.put(newL, layer);
                }
            }
            for (Polygon3d<ModelTriangle> l : part2) {
                if (l.getTriangles().size() > 0) {
                    Layer newL = new Layer(l);
                    this.layers.add(newL);
                    newLayersToOldOnes.put(newL, layer);
                }
            }

            // add triangles from layers from part1 to the queue
            for (Polygon3d<ModelTriangle> l : part1) {
                queue.addAll(l.getTriangles());
            }
        }

        // to find all triangles that have to be rotated, first add all triangles in "affected" layers that lie in the
        // right halfspace, and then go over neighbors of all found triangles to rotate and add them, if the neighbor
        // doesn't lie on an opposite side of a fold line.

        // another constraint for not adding some triangles to trianglesToRotate can be provided in neighborTest

        Set<ModelTriangle> inQueue = new HashSet<ModelTriangle>(queue);
        Set<ModelTriangle> trianglesToRotate = new HashSet<ModelTriangle>();
        ModelTriangle t;
        while ((t = queue.poll()) != null) {
            if (!trianglesToLayers.containsKey(t))
                throw new IllegalStateException("Cannot find layer for triangle " + t);

            trianglesToRotate.add(t);

            // border is the intersection line in the layer of the processed triangle - if the triangle lies in a layer
            // without intersection line, it can be surely added to the queue
            Segment3d border = layerInts.get(trianglesToLayers.get(t));
            if (border == null) { // this is assumed to be true
                Layer oldLayer = newLayersToOldOnes.get(trianglesToLayers.get(t));
                if (oldLayer != null)
                    border = layerInts.get(oldLayer);
            }

            List<ModelTriangle> neighbors = findNeighbors(t);
            n: for (ModelTriangle n : neighbors) {
                if (inQueue.contains(n))
                    continue;

                if (border != null) {
                    Segment3d intWithNeighbor = t.getCommonEdge(n, false);
                    // if the common edge between t and n is a part of the border line, we need to check if n lies in
                    // the processed halfspace; if not, it is "on the other side" of the border line, so we don't want
                    // to add it to the queue
                    if (intWithNeighbor != null && intWithNeighbor.overlaps(border)) {
                        if (!halfspace.contains(n.getP1()) || !halfspace.contains(n.getP2())
                                || !halfspace.contains(n.getP3())) {
                            continue n;
                        } else if (neighborTest != null && !neighborTest.includeNeighbor(t, n)) {
                            // or neighborTest can also discard some triangles from being added to trianglesToRotate
                            continue n;
                        }
                    } else if (neighborTest != null && !neighborTest.includeNeighbor(t, n)) {
                        // or neighborTest can also discard some triangles from being added to trianglesToRotate
                        continue n;
                    }
                }
                // else - if no border line lies in t's layer, we automatically want to add all of its neighbors

                queue.add(n);
                inQueue.add(n);
            }
        }

        Set<Layer> layersToRotate = new HashSet<Layer>();
        // find a set of layers that contain all the triangles to be rotated
        for (ModelTriangle tr : trianglesToRotate) {
            layersToRotate.add(trianglesToLayers.get(tr));
        }

        // HEURISTIC: If we have guessed refPoint, and we find out that more than a half of all triangles should be
        // rotated, we rotate the rest of the layers (so that the bigger part of paper will always stay unrotated).
        // However, we cannot use this heuristic if a neighbor inclusion test is specified!
        if (refPoint == null && neighborTest == null) {
            if (trianglesToRotate.size() > triangles.size() / 2) {
                Set<Layer> newLayersToRotate = new HashSet<Layer>(layers.size() - layersToRotate.size());
                for (Layer l : layers)
                    if (!layersToRotate.contains(l))
                        newLayersToRotate.add(l);
                layersToRotate = newLayersToRotate;
                angle1 = -angle1;
            }
        }

        Map<Layer, Layer> result = new HashMap<Layer, Layer>(layersToRotate.size());
        for (Layer l : layersToRotate) {
            // remove, rotate, and then add the triangles back to make sure all caches and maps will hold the correct
            // value
            if (l == null)
                continue;
            triangles.removeAll(l.getTriangles());
            l.rotate(segment, angle1);
            triangles.addAll(l.getTriangles());

            Layer oldLayer = newLayersToOldOnes.get(l);
            if (layerInts.keySet().contains(oldLayer))
                result.put(l, oldLayer);
        }

        return result;
    }

    /**
     * Make a reverse fold and bend the paper correspondingly.
     * <p>
     * If <code>line</code> and <code>refLine</code> are perpendicular AND <code>refLine</code> doesn't start or end on
     * <code>line</code>, then the part to be rotated is determined by the start point of <code>refLine</code>.
     * 
     * @param direction Mountain means inside fold, valley means outside fold.
     * @param line The line to bend along.
     * @param oppositeLine The second line to fold along. Pass <code>null</code> to autocompute. If you do so, you must
     *            also pass <code>null</code> to <code>oppositeLayerFilter</code>.
     * @param refLine The line around which the paper will twist (you'd press on this line with your finger when doing
     *            the fold manually).
     * @param layerFilter The filter that filters the layers the fold along <code>line</code> should be made on.
     * @param oppositeLayerFilter The filter that filters the layers the fold along <code>oppositeLine</code> should be
     *            made on. Pass <code>null</code> to autocompute. If you do so, you must also pass <code>null</code> to
     *            <code>oppositeLine</code>.
     * 
     * @return The layers of the paper that were rotated. The first list item containing the first layers, the second
     *         list item containing the opposite layers. Every entry has the same meaning as the return value of
     *         {@link #makeFold(Direction, Point2d, Point2d, LayerFilter, double, boolean)}.
     * 
     * @throws InvalidOperationException If the assumptions for doing this fold aren't satisfied.
     */
    public List<Map<Layer, Layer>> makeReverseFold(Direction direction, Segment2d line, Segment2d oppositeLine,
            Segment2d refLine, LayerFilter layerFilter, LayerFilter oppositeLayerFilter)
            throws InvalidOperationException
    {
        Segment3d line3 = new Segment3d(locatePointFromPaperTo3D(line.getP1()), locatePointFromPaperTo3D(line.getP2()));
        Segment3d refLine3 = new Segment3d(locatePointFromPaperTo3D(refLine.getP1()),
                locatePointFromPaperTo3D(refLine.getP2()));

        // check if line and refLine have exactly one common point in 3D
        Segment3d intPoint = line3.getIntersection(refLine3);
        if (intPoint == null || !intPoint.getVector().epsilonEquals(new Vector3d(), EPSILON)) {
            throw new InvalidOperationException("line.and.ref.line.must.intersect.in.exactly.one.point",
                    intPoint == null ? "null" : intPoint.toStringAsIntersection());
        }

        Segment2d oppositeLine2;
        Segment3d oppositeLine3;
        LayerFilter oppositeLayerFilter2;

        if (oppositeLine == null) {
            // neither oppositeLine nor oppositeAffectedLayer were specified, so we must compute them
            // line and refLine don't have to intersect on the paper, so we can treat them as lines
            oppositeLine2 = new Segment2d(new Line2d(refLine).mirror(new Line2d(line)));
            // check if the oppositeLine lies in the paper
            if (!getOrigami().getModel().getPaper().containsRelative(oppositeLine2.getP1())
                    || !getOrigami().getModel().getPaper().containsRelative(oppositeLine2.getP2())) {
                throw new InvalidOperationException("opposite.line.couldnt.be.found");
            }

            oppositeLine3 = new Segment3d(locatePointFromPaperTo3D(oppositeLine2.getP1()),
                    locatePointFromPaperTo3D(oppositeLine2.getP2()));

            final LinkedHashMap<Layer, ModelSegment> layerInts = getLayers(new ModelSegment(line3, line, null, 0));
            layerFilter.filter(layerInts);

            oppositeLayerFilter2 = getOppositeLineLayerFilter(new ModelSegment(line3, line, null, 0), new ModelSegment(
                    oppositeLine3, oppositeLine2, null, 0), layerInts.keySet());

        } else {
            oppositeLine2 = oppositeLine;
            oppositeLayerFilter2 = oppositeLayerFilter;

            oppositeLine3 = new Segment3d(locatePointFromPaperTo3D(oppositeLine2.getP1()),
                    locatePointFromPaperTo3D(oppositeLine2.getP2()));

            Segment3d intersection = oppositeLine3.getIntersection(line3);
            if (intersection == null) {
                throw new InvalidOperationException("line.and.opposite.line.dont.intersect");
            } else {
                Segment3d intersection2 = refLine3.getIntersection(intersection);
                if (intersection2 == null) {
                    throw new InvalidOperationException("line.and.opposite.line.dont.intersect.on.refline");
                } else if (!intersection2.getVector().epsilonEquals(new Vector3d(), EPSILON)) {
                    throw new InvalidOperationException("line.and.opposite.line.cant.be.parallel.to.refline");
                }
            }
        }

        // ________________lineP...oppositeP_____________.................
        // ................l/|........|\o....................O.\......../.A
        // ...............i/.|........|.\p....................N.\....../.N
        // .ONE.SIDE.....n/..|........|..\p..ANOTHER.SIDE......E.\..../.O
        // .............e/...|........|...\o......................\../.T
        // _____________/____|........|____\s____________.......S..\/.H.
        // ............^...refLine.refLine..^...................IDE...ER.SIDE
        // ............|....................|
        // ..........common..............common

        Point3d lineP = line3.getP1();
        Point3d common = line3.getP2();
        if (!oppositeLine3.contains(common)) {
            lineP = line3.getP2();
            common = line3.getP1();
        }
        Point3d oppositeP = oppositeLine3.getP1();
        if (oppositeP.epsilonEquals(common, EPSILON))
            oppositeP = oppositeLine3.getP2();

        Vector3d lineP_oppositeP = new Vector3d(oppositeP);
        lineP_oppositeP.sub(lineP);
        if (lineP_oppositeP.epsilonEquals(new Vector3d(), EPSILON))
            // line and opposite are equal, so we need another way to determine the dividing plane's normal
            // TODO this may not be sufficient handling of this corner case
            lineP_oppositeP.cross(line3.getVector(), refLine3.getVector());

        Direction dir = direction, oppositeDir = direction;
        {// determine the real from-screen direction of the folds
         // lineLayerNormal and its opposite should point "outside" the triangle defined by common, lineP and oppositeP

            Vector3d lineLayerNormal = getSegmentNormal(new ModelSegment(line3, line));
            Vector3d oppositeLineLayerNormal = getSegmentNormal(new ModelSegment(oppositeLine3, oppositeLine2));

            if (lineLayerNormal == null) {
                throw new InvalidOperationException("reverse.fold.no.line.normal");
            }

            if (oppositeLineLayerNormal == null) {
                throw new InvalidOperationException("reverse.fold.no.opposite.line.normal");
            }

            if (lineLayerNormal.angle(lineP_oppositeP) < Math.PI / 2d - EPSILON)
                lineLayerNormal.negate();
            if (oppositeLineLayerNormal.angle(lineP_oppositeP) > Math.PI / 2d + EPSILON)
                oppositeLineLayerNormal.negate();

            if (lineLayerNormal.angle(getScreenNormal()) > Math.PI / 2d + EPSILON)
                dir = dir.getOpposite();
            if (oppositeLineLayerNormal.angle(getScreenNormal()) > Math.PI / 2d + EPSILON)
                oppositeDir = oppositeDir.getOpposite();
        }

        // refLineEnd is the border point of refLine that will be rotated
        Point3d refLineEnd;
        Point2d refLineEnd2;
        if (refLine3.getP1().epsilonEquals(common, EPSILON) || refLine3.getP2().epsilonEquals(common, EPSILON)) {
            if (refLine3.getP1().epsilonEquals(common, EPSILON)) {
                refLineEnd = refLine3.getP2();
                refLineEnd2 = refLine.getP2();
            } else {
                refLineEnd = refLine3.getP1();
                refLineEnd2 = refLine.getP1();
            }
        } else {
            double angle = new Segment3d(common, lineP).getVector().angle(refLine3.getVector());
            double halfPI = Math.PI / 2d;
            if (abs(angle - halfPI) > EPSILON) {
                if ((direction == Direction.MOUNTAIN && angle > halfPI + EPSILON)
                        || (direction == Direction.VALLEY && angle < halfPI - EPSILON)) {
                    refLineEnd = refLine3.getP1();
                    refLineEnd2 = refLine.getP1();
                } else {
                    refLineEnd = refLine3.getP2();
                    refLineEnd2 = refLine.getP2();
                }
            } else {
                refLineEnd = refLine3.getP1();
                refLineEnd2 = refLine.getP1();
            }
        }

        // dividing plane goes along refLine and halves the space between lineP and oppositeP
        Plane3d dividingPlane = new Plane3d(lineP_oppositeP, refLine3.getP1());

        // this halfspace just serves to be able to distinguish layers that correspond to line from that corresponding
        // to opposite
        final HalfSpace3d halfspace = new HalfSpace3d(dividingPlane);

        // this test determines if both triangles lie in the same halfspace "around" refLine
        NeighborInclusionTest neighborTest = new NeighborInclusionTest() {
            @Override
            public boolean includeNeighbor(ModelTriangle t, ModelTriangle n)
            {
                return (halfspace.contains(t.getP1()) && halfspace.contains(t.getP2()) && halfspace.contains(t.getP3())) == (halfspace
                        .contains(n.getP1()) && halfspace.contains(n.getP2()) && halfspace.contains(n.getP3()));
            }
        };

        // DETERMINING THE ROTATION ANGLE

        // to determine the angle of rotation we find the image of refLineEnd in a "plane symmetry" through the plane
        // defined by lineP, oppositeP and common; once we know this image, we can simply compute the angle between
        // the source and the image around <line> and we've finished (we don't need to compute the angle of rotation
        // around <opposite>, it will be the same)
        Plane3d mirrorPlane;
        try {
            mirrorPlane = new Plane3d(lineP, oppositeP, common);
        } catch (IllegalArgumentException e) {
            // lineP, oppositeP and common don't form a triangle, so both parts of the paper are parallel
            Vector3d mirrorPlaneDirection = new Vector3d();
            mirrorPlaneDirection.cross(line3.getVector(), refLine3.getVector());
            mirrorPlaneDirection.add(common);
            mirrorPlane = new Plane3d(lineP, common, new Point3d(mirrorPlaneDirection));
        }

        Vector3d mirrorPlaneNormal = new Vector3d(mirrorPlane.getNormal());
        mirrorPlaneNormal.normalize();
        Line3d mirrorLine = new Line3d(refLineEnd, mirrorPlaneNormal);
        Point3d mirrorPoint = mirrorPlane.getIntersection(mirrorLine).getPoint();

        Vector3d refLineEnd_mirrorPoint = new Vector3d(mirrorPoint);
        refLineEnd_mirrorPoint.sub(refLineEnd);

        // this is the desired image
        Point3d mirroredRefLineEnd = new Point3d(mirrorPoint);
        mirroredRefLineEnd.add(refLineEnd_mirrorPoint);

        Point3d nearestLinePoint = line3.getNearestPoint(refLineEnd);

        Vector3d v1 = new Vector3d(refLineEnd);
        v1.sub(nearestLinePoint);
        Vector3d v2 = new Vector3d(mirroredRefLineEnd);
        v2.sub(nearestLinePoint);

        // and here we have the angle
        double angle = v1.angle(v2);

        List<Map<Layer, Layer>> result = new LinkedList<Map<Layer, Layer>>();

        result.add(makeFold(dir, line.getP1(), line.getP2(), refLineEnd2, layerFilter, angle, null, neighborTest));

        // create the opposite fold
        result.add(makeFold(oppositeDir, oppositeLine2.getP1(), oppositeLine2.getP2(), refLineEnd2,
                oppositeLayerFilter2, angle, null, neighborTest));

        return result;
    }

    /**
     * A call to this method will revert all triangle rotations made by paper bending in this step. This can be used to
     * retrieve a model state without delayed operations from a model state with delayed operations. Note that you
     * shouldn't base any other computations on this model state after a call to this method because it is globally
     * invalid.
     */
    public void revertDelayedOperations()
    {
        this.triangles.clear();

        for (Layer l : layers) {
            List<ModelTriangle> triangles = new LinkedList<ModelTriangle>(l.getTriangles());
            // we need to construct the hash set because removeTriangles can alter the given argument
            l.removeTriangles(new HashSet<ModelTriangle>(triangles));
            for (ModelTriangle t : triangles) {
                t.setPoints((Point3d) t.beforeRotation.getP1().clone(), (Point3d) t.beforeRotation.getP2().clone(),
                        (Point3d) t.beforeRotation.getP3().clone());
            }
            l.addTriangles(triangles);

            this.triangles.addAll(triangles);
        }
    }

    /**
     * Add a textual marker bound to a point on the paper.
     * 
     * @param point The point the marker should "stick" to.
     * @param text The text to display.
     * @param stepsToHide Number of steps this marker should be displayed during.
     */
    public void addMarker(Point2d point, String text, int stepsToHide)
    {
        Marker marker = new Marker(text, locatePointFromPaperTo3D(point), point, stepsToHide);
        markers.add(marker);
    }

    /**
     * Returns a list of triangles having a common point with the given triangle.
     * 
     * @param t The triangle to find neighbors to.
     * @return The list of neighbors of t.
     */
    protected List<ModelTriangle> findNeighbors(ModelTriangle triangle)
    {
        return triangle.getNeighbors();
    }

    /**
     * Returns a list of triangles having a common point with the given triangle.
     * 
     * @param t The triangle to find neighbors to.
     * @return The list of neighbors of t.
     */
    protected List<Triangle2d> findNeighbors(Triangle2d triangle)
    {
        final List<ModelTriangle> list = paperToSpaceTriangles.get(triangle).getNeighbors();
        List<Triangle2d> result = new LinkedList<Triangle2d>();

        for (ModelTriangle t : list)
            result.add(t.getOriginalPosition());

        return result;
    }

    /**
     * <p>
     * Returns a sorted map of layers defined by the given segment.
     * </p>
     * 
     * <p>
     * <i>A layer is a part of the paper surrounded by either fold lines or paper boundaries.</i>
     * </p>
     * 
     * <p>
     * This function returns the layers that intersect with a stripe defined by the given segment and that has the
     * direction of the average normal of the segment (see {@link #getSegmentNormal(ModelSegment)}).
     * </p>
     * 
     * <p>
     * The list is sorted in the order the layers intersect with the stripe.
     * </p>
     * 
     * <p>
     * The very first layer is the one that has its intersection the furthest in the direction of the normal of the
     * layer the segment lies in.
     * </p>
     * 
     * @param segment The segment we search layers for.
     * @return A list of layers defined by the given line and the intersections with the fold stripe with those layers.
     */
    public LinkedHashMap<Layer, ModelSegment> getLayers(final ModelSegment segment)
    {
        // find the segment's average normal
        final Vector3d segNormal = getSegmentNormal(segment);
        if (segNormal == null)
            return new LinkedHashMap<Layer, ModelSegment>();

        // find another layers: is done by creating a stripe pointing in the found normal's direction and finding
        // intersections of the stripe with triangles
        final Line3d p1line = new Line3d(segment.getP1(), segNormal);
        final Line3d p2line = new Line3d(segment.getP2(), segNormal);

        final Stripe3d stripe = new Stripe3d(p1line, p2line);

        return getLayers(stripe);
    }

    /**
     * <p>
     * Returns a sorted map of layers intersecting the given stripe.
     * </p>
     * 
     * <p>
     * <i>A layer is a part of the paper surrounded by either fold lines or paper boundaries.</i>
     * </p>
     * 
     * <p>
     * The list is sorted in the order the layers intersect with the stripe.
     * </p>
     * 
     * <p>
     * The very first layer is the one that has its intersection the furthest in the direction of the normal of the
     * layer the segment lies in.
     * </p>
     * 
     * @param stripe The stripe we search layers for.
     * @return A list of layers intersecting with the given stripe and their intersections with it.
     */
    protected LinkedHashMap<Layer, ModelSegment> getLayers(final Stripe3d stripe)
    {
        final LinkedHashMap<Layer, ModelSegment> result = new LinkedHashMap<Layer, ModelSegment>();

        for (Layer l : layers) {
            final ModelSegment intSegment = l.getIntersectionSegment(stripe);
            if (intSegment == null || intSegment.getVector().epsilonEquals(new Vector3d(), MathHelper.EPSILON)) {
                continue;
            } else {
                result.put(l, intSegment);
            }
        }

        final List<Layer> keys = new ArrayList<Layer>(result.keySet());
        // sort the layers along the stripe
        // we assume that all the layers intersect with the stripe, so it's no problem to sort the layers by their
        // intersections with one of the stripe's border line
        Collections.sort(keys, new Comparator<Layer>() {
            @Override
            public int compare(Layer o1, Layer o2)
            {
                final Line3d int1 = o1.getPlane().getIntersection(stripe.getLine1());
                final Line3d int2 = o2.getPlane().getIntersection(stripe.getLine1());

                // we can assume int1 and int2 to be regular points, because intersections with layers parallel to the
                // stripe are discarded
                // nevertheless we do the null checks here to avoid NPEs due to rounding errors
                if (int1 == null) {
                    if (int2 == null)
                        return 0;
                    return -1;
                } else if (int2 == null) {
                    return 1;
                }

                double t1 = stripe.getLine1().getParameterForPoint(int1.getPoint());
                double t2 = stripe.getLine1().getParameterForPoint(int2.getPoint());
                return (t1 - t2 > EPSILON) ? 1 : (t1 - t2 < -EPSILON ? -1 : 0);
            }
        });

        final LinkedHashMap<Layer, ModelSegment> sortedResult = new LinkedHashMap<Layer, ModelSegment>(result.size());
        for (Layer l : keys) {
            sortedResult.put(l, result.get(l));
        }

        return sortedResult;
    }

    /**
     * Return the average normal of all layers the given point lies in.
     * <p>
     * If the average normal is a zero vector, return the normal of any layer the point lies in.
     * <p>
     * If the point doesn't lie in any layer, the normal cannot be detected, so <code>null</code> will be returned and a
     * warning will be issued by this class' logger.
     * <p>
     * The average normal is determined as angle weighted normal, described in: G. Thurmer, C. A. Wuthrich,
     * "Computing vertex normals from polygonal facets" Journal of Graphics Tools, 3 1998
     * 
     * @param point The point to find normal for.
     * @return The normal at the point, or <code>null</code> if the point doesn't lie in any of the layers.
     */
    protected Vector3d getNormalAtPoint(ModelPoint point)
    {
        Vector3d anyNormal = null;

        List<Layer> containingLayers = new LinkedList<Layer>();

        // find if any layer contains the given point, remember the containing layers
        for (Layer l : layers) {
            if (l.contains(point)) {
                containingLayers.add(l);
                if (anyNormal == null) {
                    anyNormal = new Vector3d(l.getNormal());
                }
            }
        }

        if (anyNormal == null) {
            Logger.getLogger(getClass()).warn("getNormalAtPoint: cannot find layer for point " + point);
            return null;
        }

        // if there is only 1 layer the point lies in, we save lots of computing by just returning - the weighted normal
        // of a single layer is just its normal
        if (containingLayers.size() == 1) {
            anyNormal.normalize();
            return anyNormal;
        }

        // find all triangles that have the given point as their vertex, compute the normal of the triangle, multiply it
        // with the angle at the point, and add it to the result normal
        final Vector3d result = new Vector3d();
        for (Layer l : containingLayers) {
            for (ModelTriangle t : l.getTriangles()) {
                if (t.isVertex(point)) {
                    Point3d vertex = t.getNearestVertex(point);
                    Segment3d[] edges = t.getVertexEdges(vertex);
                    // it'd be more precise to project the triangle onto some 2D plane and use the 2D angle (not doing
                    // so leads us to not having the sum of 360°), but it just isn't worth it; we don't need such
                    // precise computation
                    double angle = edges[0].getVector().angle(edges[1].getVector());
                    Vector3d normal = new Vector3d(t.getNormal());
                    normal.scale(angle);
                    result.add(normal);
                }
            }
        }

        // if the found normal is non-zero, normalize it and return; otherwise, the normals have cancelled each other,
        // so we must return simply one of the layers' normals
        if (!result.epsilonEquals(new Vector3d(), EPSILON)) {
            result.normalize();
            return result;
        } else {
            return anyNormal;
        }
    }

    /**
     * Return the average normal of the layers this segment lies in.
     * <p>
     * Basically, if the segment isn't a border of the layer it lies in, the layer's normal will be returned.
     * <p>
     * If this segment lies in no layers, <code>null</code> will be returned and a warning will be issued by this class'
     * logger.
     * 
     * @param segment The segment to find normal for.
     * @return The average normal of a point on this segment.
     */
    protected Vector3d getSegmentNormal(ModelSegment segment)
    {
        final double[] trialPoints = new double[] { 0.5d, 0.75d, 0.25d, 0.1d, 0.9d, 0.01d, 0.99d };
        for (double d : trialPoints) {
            Vector3d normal = getNormalAtPoint(segment.getPointForParameter(d));
            if (normal != null)
                return normal;
        }
        return null;
    }

    /**
     * Given all triangles in one layer, divides all triangles in the layer by the given line to smaller triangles.
     * 
     * @param layer The triangles in the layer with the appropriate intersection points.
     * @param direction The direction of the created fold.
     * @param segment The segments defining the fold directly in this layer.
     * @param foldDirections An entry will be added to this map with the given layer as key and the value being the real
     *            fold direction on the layer's top side.
     * 
     * @return The intersections of the given segment with the layer.
     */
    protected List<? extends Segment3d> makeFoldInLayer(Layer layer, Direction direction, ModelSegment segment,
            Map<Layer, Direction> foldDirections)
    {
        Direction realDir = direction;
        // if the down side of the layer faces screen, invert the fold type
        if (layer.getNormal().angle(getScreenNormal()) >= Math.PI / 2d + EPSILON)
            realDir = realDir.getOpposite();

        foldDirections.put(layer, realDir);

        List<IntersectionWithTriangle<ModelTriangle>> intersections = layer.getIntersectionsWithTriangles(segment);

        Fold fold = new Fold();
        fold.originatingStepId = this.step.getId();

        for (IntersectionWithTriangle<ModelTriangle> intersection : intersections) {
            if (intersection == null) {
                // no intersection with the triangle - something's weird (we loop over intersections with triangles)
                throw new IllegalStateException(
                        "Invalid diagram: no intersection found in IntersectionWithTriangle in step " + step.getId());
            }

            // also subdivides the referenced foldlines and removes references to the old ones from
            // intersection.triangle, so no references to it should remain in this.folds
            List<ModelTriangle> newTriangles = layer.subdivideTriangle(intersection);
            if (newTriangles.size() > 1) {
                triangles.remove(intersection.triangle);
                triangles.addAll(newTriangles);
            }

            for (ModelTriangle t : newTriangles) {
                int i = 0;
                for (Segment3d edge : t.getEdges()) {
                    Segment3d edgeInt = edge.getIntersection(intersection);
                    if (edgeInt != null && !edgeInt.getVector().epsilonEquals(new Vector3d(), MathHelper.EPSILON)) {
                        // this method adds all fold lines twice - one for each triangle adjacent to the
                        // intersection segment - but we don't care (maybe we should, it'll be more clear further)
                        FoldLine line = new FoldLine();
                        line.setDirection(realDir);
                        line.setFold(fold);
                        line.setLine(new ModelTriangleEdge(t, i));
                        t.addFoldLine(i, line);
                        fold.getLines().add(line);
                    }
                    i++;
                }
            }
        }

        folds.add(fold);

        return layer.joinNeighboringSegments(intersections);
    }

    /**
     * Return the layer filter that selects the layers corresponding to <code>opposite</code> line.
     * <p>
     * That means, construct a triangle from <code>line</code> and <code>opposite</code> (they are required to have a
     * common point) and return all layers intersecting that triangle that aren't in the affected layers of
     * <code>line</code>.
     * 
     * TODO a lot of getLayers() usage can be cached
     * 
     * @param line The original fold line.
     * @param opposite The <code>line</code>'s opposite line (it's image in 2D axis symmetry around an axis that has a
     *            common point with <code>line</code>).
     * @param lineAffectedLayers The set of affected layers for <code>line</code>.
     * @return The corresponding layer filter for <code>opposite</code>. <code>null</code> if for the given
     *         <code>line</code> and <code>opposite</code> no solution is available (eg. one of them has zero direction
     *         vector or they together form a line).
     */
    protected LayerFilter getOppositeLineLayerFilter(ModelSegment line, ModelSegment opposite,
            Set<Layer> lineAffectedLayers)
    {
        Point3d lineP = line.getP1();
        Point3d common = line.getP2();
        if (!opposite.contains(common)) {
            lineP = line.getP2();
            common = line.getP1();
        }
        Point3d oppositeP = opposite.getP1();
        if (oppositeP.epsilonEquals(common, EPSILON))
            oppositeP = opposite.getP2();

        // try to construct a triangle
        //
        // lineP--------------oppositeP
        // ...l.\............/.o
        // ....i.\........../.p
        // .....n.\......../.p
        // ......e.\....../.o
        // .........\..../.s
        // ..........\../..i
        // ...........\/...t
        // ........common..e

        // not null if the given points form a triangle in 3D
        Triangle3d triangle = null;
        try {
            triangle = new Triangle3d(lineP, common, oppositeP);
        } catch (IllegalArgumentException e) {}

        // if the triangle could be constructed, we use this algorithm, otherwise we have another one
        if (triangle != null) {

            // construct a stripe that has one border line as the line lineP3-oppositeP3 and the second one parallel to
            // that one and going through common3
            Vector3d stripeDir = new Vector3d(oppositeP);
            stripeDir.sub(lineP);
            stripeDir.normalize();

            Line3d line1 = new Line3d(lineP, stripeDir);
            Line3d line2 = new Line3d(common, stripeDir);

            Stripe3d stripe = new Stripe3d(line1, line2);

            // get intersection of the constructed stripe with all layers and remove those that either are in affected
            // layers of line, or don't intersect with the constructed triangle
            //
            // this way we get the layers that intersect with the interior of the constructed triangle and aren't "used"
            // by line
            LinkedHashMap<Layer, ModelSegment> layerInts = getLayers(stripe);
            for (Iterator<Entry<Layer, ModelSegment>> it = layerInts.entrySet().iterator(); it.hasNext();) {
                Entry<Layer, ModelSegment> entry = it.next();
                if (lineAffectedLayers.contains(entry.getKey())) {
                    it.remove();
                } else {
                    Segment3d intersection = triangle.getIntersection(entry.getValue());
                    if (intersection == null || intersection.getVector().epsilonEquals(new Vector3d(), EPSILON))
                        it.remove();
                }
            }

            Set<Layer> oppositeLayers = layerInts.keySet();
            // intersections with layers as defined by opposite
            Set<Layer> oppositeLineLayers = getLayers(opposite).keySet();
            oppositeLineLayers.retainAll(oppositeLayers);
            return new LayerFilter(oppositeLineLayers);
        } else {
            // if the triangle couldn't be constructed, there are several possibilities
            Segment3d intersection = line.getIntersection(opposite);
            if (intersection == null || intersection.getVector().epsilonEquals(new Vector3d(), EPSILON)) {
                // line and opposite either don't intersect or one of them has zero direction vector
                return null;
            }

            // line and opposite do overlap, which means the "triangle" is effectively a line, so we are interested only
            // in layers that go through that line (using affectedLayers it is possible to find some layers that go
            // through that line and aren't "used" by line)
            LinkedHashMap<Layer, ModelSegment> oppositeLayerInts = getLayers(opposite);
            oppositeLayerInts.keySet().removeAll(lineAffectedLayers);
            return new LayerFilter(oppositeLayerInts.keySet());
        }
    }

    /**
     * Adds the given angle to the current angle of rotation.
     * 
     * @param rotation The angle to add (in radians).
     */
    public void addRotation(double rotation)
    {
        setRotation(rotationAngle + rotation);
    }

    /**
     * Sets the current angle of rotation to the given value.
     * 
     * @param rotation The angle to set (in radians).
     */
    public void setRotation(double rotation)
    {
        rotationAngle = rotation;

        while (rotationAngle > Math.PI)
            rotationAngle -= 2 * Math.PI;
        while (rotationAngle < -Math.PI)
            rotationAngle += 2 * Math.PI;

        screenNormal = null;
    }

    /**
     * Return the rotation of the paper.
     * 
     * @return The rotation of the paper (in radians).
     */
    public double getRotation()
    {
        return rotationAngle;
    }

    /**
     * Adds the given angle to the current viewing angle of the paper.
     * 
     * The angle will be "cropped" to <-PI/2,PI/2> interval.
     * 
     * @param angle The angle to add (in radians).
     */
    public void addViewingAngle(double angle)
    {
        setViewingAngle(viewingAngle + angle);
    }

    /**
     * Changes the viewing angle from top to bottom and vice versa.
     */
    public void flipViewingAngle()
    {
        setViewingAngle(-viewingAngle);
    }

    /**
     * Sets the current viewing angle to the given value.
     * 
     * The angle will be "cropped" to <-PI/2,PI/2> interval.
     * 
     * @param angle The angle to set (in radians).
     */
    public void setViewingAngle(double angle)
    {
        viewingAngle = angle;

        if (viewingAngle > Math.PI / 2.0)
            viewingAngle = Math.PI / 2.0;
        if (viewingAngle < -Math.PI / 2.0)
            viewingAngle = -Math.PI / 2.0;

        screenNormal = null;
    }

    /**
     * Get the current viewing angle.
     * 
     * @return The viewing angle (in radians).
     */
    public double getViewingAngle()
    {
        return viewingAngle;
    }

    /**
     * @return The folds on the paper.
     */
    public ObservableList<Fold> getFolds()
    {
        return folds;
    }

    /**
     * @return The list of layers.
     */
    public ObservableList<Layer> getLayers()
    {
        return layers;
    }

    /**
     * This function has to be called after {@link #clone()} if you intend to base the next step on this model state.
     */
    public void proceedToNextStep()
    {
        for (Iterator<Marker> it = markers.iterator(); it.hasNext();) {
            Marker marker = it.next();
            if (marker.getStepsToHide() > 0)
                marker.setStepsToHide(marker.getStepsToHide() - 1);
            else
                it.remove();
        }

        for (ModelTriangle t : triangles)
            t.resetBeforeRotation();
    }

    @Override
    public ModelState clone()
    {
        ModelState result = null;
        try {
            result = (ModelState) super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }

        result.foldLineArrays = null;
        result.foldLineArraysDirty = true;
        result.trianglesArrays = null;
        result.trianglesArraysDirty = true;

        result.layers = new ObservableList<Layer>(layers.size());
        result.folds = new ObservableList<Fold>(folds.size());
        result.markers = new ObservableList<Marker>(markers.size());
        result.markerData = new LinkedList<MarkerRenderData>();
        result.paperToSpaceTriangles = new Hashtable<Triangle2d, ModelTriangle>(paperToSpaceTriangles.size());
        result.triangles = new ObservableList<ModelTriangle>(triangles.size());
        result.trianglesToLayers = new Hashtable<ModelTriangle, Layer>(trianglesToLayers.size());
        result.paperToSpacePoint = new Hashtable<Point2d, Point3d>(paperToSpacePoint.size());

        result.addObservers();

        Hashtable<ModelTriangle, ModelTriangle> newTriangles = new Hashtable<ModelTriangle, ModelTriangle>(
                triangles.size());
        for (ModelTriangle t : triangles) {
            ModelTriangle newT = t.clone();
            // the fold lines will be filled further again with their new instances
            newT.s1FoldLines = null;
            newT.s2FoldLines = null;
            newT.s3FoldLines = null;
            newTriangles.put(t, newT);
            result.triangles.add(newT);
        }

        for (ModelTriangle t : result.triangles) {
            List<ModelTriangle> oldNeighbors = new LinkedList<ModelTriangle>(t.getRawNeighbors());
            t.getRawNeighbors().clear();
            for (ModelTriangle n : oldNeighbors) {
                ModelTriangle newN = newTriangles.get(n);
                if (newN == null)
                    throw new IllegalStateException("clone: Cannot find new triangle for old triangle "
                            + n.getOriginalPosition());
                t.getRawNeighbors().add(newN);
            }
        }

        for (Fold fold : folds) {
            Fold newFold = fold.clone();
            newFold.lines.getObservers().clear();
            newFold.addObservers();

            for (FoldLine l : newFold.lines) {
                ModelTriangle newTriangle = newTriangles.get(l.getLine().getTriangle());
                if (newTriangle == null)
                    throw new IllegalStateException("clone: Cannot find new triangle for old triangle "
                            + l.getLine().getTriangle().getOriginalPosition());

                l.getLine().setTriangle(newTriangle);
                List<FoldLine> foldLines = newTriangle.getFoldLines(l.getLine().getIndex());
                if (foldLines == null) {
                    newTriangle.setFoldLines(l.getLine().getIndex(), new LinkedList<FoldLine>());
                    foldLines = newTriangle.getFoldLines(l.getLine().getIndex());
                }
                foldLines.add(l);
            }

            result.folds.add(newFold);
        }

        for (Layer l : layers) {
            List<ModelTriangle> triangles = new LinkedList<ModelTriangle>();
            for (ModelTriangle t : l.getTriangles()) {
                ModelTriangle newT = newTriangles.get(t);
                if (newT == null)
                    throw new IllegalStateException("clone: Cannot find new triangle for old triangle "
                            + t.getOriginalPosition());
                triangles.add(newT);
            }
            result.layers.add(new Layer(triangles));
        }

        for (Marker m : markers) {
            result.markers.add(m.clone());
        }

        return result;
    }

    /**
     * Return true if the line composed of the given points goes only through connected parallel layers.
     * 
     * @param p1 Start point.
     * @param p2 End point.
     * @return true if the line composed of the given points goes only through connected parallel layers.
     */
    public boolean canChooseLine(ModelPoint p1, ModelPoint p2)
    {
        ModelSegment seg = new ModelSegment(p1, p2, null, 0);
        List<ModelSegment> ints = new LinkedList<ModelSegment>();

        // get intersections with all layers
        for (Layer l : layers) {
            if (abs(l.getNormal().dot(seg.getVector())) <= EPSILON && l.getPlane().contains(seg.getP1())) {
                ints.addAll(l.getIntersections(seg));
            }
        }

        // this condition should never be satisfied, but false is the correct answer anyways
        if (ints.size() == 0)
            return false;

        // the directions as lines
        Line3d dir3d = new Line3d(seg);
        Line2d dir2d = new Line2d(seg.getOriginal());

        Vector3d zero = new Vector3d();
        Vector2d zero2d = new Vector2d();

        // discard all intersections not having the right direction
        for (Iterator<ModelSegment> it = ints.iterator(); it.hasNext();) {
            ModelSegment s = it.next();
            if ((!s.getVector().epsilonEquals(zero, EPSILON) && !dir3d.isParallelTo(s)) || !dir3d.contains(s.getP1())) {
                it.remove();
                continue;
            }
            if ((!s.getOriginal().getVector().epsilonEquals(zero2d, EPSILON) && !dir2d.isParallelTo(s.getOriginal()))
                    || !dir2d.contains(s.getOriginal().getP1())) {
                it.remove();
                continue;
            }
        }

        if (ints.size() == 0)
            return false;

        // now we want to find out if all the found intersections can be merged to a single line

        // so we sort them by 2D coordinates and then try to merge them in the sorted order
        // if the lines can be merged, this sort always sorts them in the way that the segments being one next to the
        // other in real, are succeeding in this list; on the other side, if the segments can't be merged into one line,
        // the order has no meaning
        Collections.sort(ints, new Comparator<ModelSegment>() {
            @Override
            public int compare(ModelSegment o1, ModelSegment o2)
            {
                Point2d o1p1 = o1.getOriginal().getP1();
                Point2d o2p1 = o2.getOriginal().getP1();

                if (o1p1.x < o2p1.x + EPSILON)
                    return -1;

                if (o1p1.x + EPSILON > o2p1.x)
                    return 1;

                if (o1p1.y < o2p1.y + EPSILON)
                    return -1;

                if (o1p1.y + EPSILON > o2p1.y)
                    return 1;

                return 0;
            }
        });

        // try to merge the sorted results into one line
        while (ints.size() > 1 && ints.get(0).merge(ints.get(1)))
            ints.remove(1);

        // a line can only by selected if it can be merged into one piece
        return ints.size() == 1 && ints.get(0).epsilonEquals(seg, true);
    }

    /**
     * @return The normal of the screen in the local coordinate system.
     */
    public Vector3d getScreenNormal()
    {
        if (screenNormal == null) {
            if (Math.abs(getViewingAngle() - Math.PI / 2d) < EPSILON) {
                screenNormal = new Vector3d(0, 0, 1);
            } else { // viewingAngle = -PI/2
                screenNormal = new Vector3d(0, 0, -1);
            }
            // TODO when arbitrary viewing angle is allowed, this will need to be changed
        }
        return screenNormal;
    }

    /**
     * A callback that tests whether a triangle's neighbor should be included in some list or so.
     * 
     * @author Martin Pecka
     */
    protected interface NeighborInclusionTest
    {
        /**
         * Return true if <code>neighbor</code> should be included.
         * 
         * @param t A triangle.
         * @param neighbor Neighbor of <code>t</code>.
         * 
         * @return true if <code>neighbor</code> should be included.
         */
        boolean includeNeighbor(ModelTriangle t, ModelTriangle neighbor);
    }
}

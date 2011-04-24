/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.modelstate;

import static cz.cuni.mff.peckam.java.origamist.math.MathHelper.EPSILON;
import static java.lang.Math.abs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
            public void changePerformed(ChangeNotification<Fold> change)
            {
                ModelState.this.foldLineArraysDirty = true;

                if (change.getChangeType() == ChangeTypes.ADD) {
                    change.getItem().lines.addObserver(new Observer<FoldLine>() {
                        @Override
                        public void changePerformed(ChangeNotification<FoldLine> change)
                        {
                            ModelState.this.foldLineArraysDirty = true;
                        }
                    });
                }
            }
        });

        triangles.addObserver(new Observer<ModelTriangle>() {
            @Override
            public void changePerformed(ChangeNotification<ModelTriangle> change)
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
            public void changePerformed(ChangeNotification<Layer> change)
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
                        public void changePerformed(ChangeNotification<ModelTriangle> change)
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
            public void changePerformed(ChangeNotification<Marker> change)
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
     * @param affectedLayers The layers the fold will be performed on.
     * @param angle The angle the paper should be bent by (in radians). Value in (0, PI) means that the down right part
     *            of the paper (with respect to the line) will be moved; value in (-PI,0) means that the other part of
     *            paper will be moved.
     */
    public void makeFold(Direction direction, Point2d startPoint, Point2d endPoint, List<Integer> affectedLayers,
            double angle)
    {
        makeFold(direction, startPoint, endPoint, null, affectedLayers, angle, null);
    }

    /**
     * Performs a valley/mountain fold.
     * 
     * @param direction The direction of the fold - VALLEY/MOUNTAIN.
     * @param startPoint Starting point of the fold (in 2D paper relative coordinates).
     * @param endPoint Ending point of the fold (in 2D paper relative coordinates).
     * @param refPoint A general point in the part of the paper to be bent. Pass <code>null</code> to autocompute from
     *            fold direction.
     * @param affectedLayers The layers the fold will be performed on.
     * @param angle The angle the paper should be bent by (in radians). Value in (0, PI) means that the down right part
     *            of the paper (with respect to the line) will be moved; value in (-PI,0) means that the other part of
     *            paper will be moved.
     */
    public void makeFold(Direction direction, Point2d startPoint, Point2d endPoint, Point2d refPoint,
            List<Integer> affectedLayers, double angle)
    {
        makeFold(direction, startPoint, endPoint, refPoint, affectedLayers, angle, null);
    }

    /**
     * Performs a valley/mountain fold.
     * 
     * @param direction The direction of the fold - VALLEY/MOUNTAIN.
     * @param startPoint Starting point of the fold (in 2D paper relative coordinates).
     * @param endPoint Ending point of the fold (in 2D paper relative coordinates).
     * @param affectedLayers The layers the fold will be performed on.
     * @param angle The angle the paper should be bent by (in radians). Value in (0, PI) means that the down right part
     *            of the paper (with respect to the line) will be moved; value in (-PI,0) means that the other part of
     *            paper will be moved.
     * @param foundAffectedLayers Output parameter. The map of found layers affected by this fold and corresponding
     *            intersection segments. Pass <code>null</code> if you aren't interested in this information. Note that
     *            the list corresponds to layers before bending, so it is practically useful only if
     *            <code>angle == 0</code>.
     */
    public void makeFold(Direction direction, Point2d startPoint, Point2d endPoint, List<Integer> affectedLayers,
            double angle, Map<Layer, Segment3d> foundAffectedLayers)
    {
        makeFold(direction, startPoint, endPoint, null, affectedLayers, angle, foundAffectedLayers);
    }

    /**
     * Performs a valley/mountain fold.
     * 
     * @param direction The direction of the fold - VALLEY/MOUNTAIN.
     * @param startPoint Starting point of the fold (in 2D paper relative coordinates).
     * @param endPoint Ending point of the fold (in 2D paper relative coordinates).
     * @param refPoint A general point in the part of the paper to be bent. Pass <code>null</code> to autocompute from
     *            fold direction.
     * @param affectedLayers The layers the fold will be performed on.
     * @param angle The angle the paper should be bent by (in radians). Value in (0, PI) means that the down right part
     *            of the paper (with respect to the line) will be moved; value in (-PI,0) means that the other part of
     *            paper will be moved.
     * @param foundAffectedLayers Output parameter. The map of found layers affected by this fold and corresponding
     *            intersection segments. Pass <code>null</code> if you aren't interested in this information. Note that
     *            the list corresponds to layers before bending, so it is practically useful only if
     *            <code>angle == 0</code>.
     */
    public void makeFold(Direction direction, Point2d startPoint, Point2d endPoint, Point2d refPoint,
            List<Integer> affectedLayers, double angle, Map<Layer, Segment3d> foundAffectedLayers)
    {
        Point3d start = locatePointFromPaperTo3D(startPoint);
        Point3d end = locatePointFromPaperTo3D(endPoint);
        Point3d ref = (refPoint != null ? locatePointFromPaperTo3D(refPoint) : null);
        Segment3d segment = new Segment3d(start, end);

        LinkedHashMap<Layer, Segment3d> layerInts = getLayers(segment);

        int i = 1;
        Iterator<Entry<Layer, Segment3d>> it = layerInts.entrySet().iterator();
        while (it.hasNext()) {
            Entry<Layer, Segment3d> entry = it.next();
            if (!affectedLayers.contains(i++)) {
                it.remove();
            } else {
                // TODO handle direction in some appropriate way
                makeFoldInLayer(entry.getKey(), direction, entry.getValue());
            }
        }

        if (foundAffectedLayers != null)
            foundAffectedLayers.putAll(layerInts);

        bendPaper(direction, segment, ref, layerInts, angle);
    }

    /**
     * Bends the paper. Requires that the fold line goes only along triangle edges, not through the interiors of them.
     * <p>
     * To specify the part of the paper that will be rotated, the segment's direction vector is used. Make cross product
     * of the normal of the layer the segment lies in and the direction vector of the segment. The cross product points
     * to the part of the paper that will be moved.
     * 
     * @param direction The direction of the fold - VALLEY/MOUNTAIN.
     * @param segment The segment to bend around. Note that the direction vector of the segment specifies which part of
     *            the paper will be rotated.
     * @param layerInts A map of affected layers and intersections of the fold stripe with them.
     * @param angle The angle the paper should be bent by (in radians). Value in (0, PI) means that the down right part
     *            of the paper (with respect to the line) will be moved; value in (-PI,0) means that the other part of
     *            paper will be moved.
     */
    protected void bendPaper(Direction direction, Segment3d segment, Map<Layer, Segment3d> layerInts, double angle)
    {
        bendPaper(direction, segment, null, layerInts, angle, null);
    }

    /**
     * Bends the paper. Requires that the fold line goes only along triangle edges, not through the interiors of them.
     * <p>
     * To specify the part of the paper that will be rotated, the segment's direction vector is used. Make cross product
     * of the normal of the layer the segment lies in and the direction vector of the segment. The cross product points
     * to the part of the paper that will be moved.
     * 
     * @param direction The direction of the fold - VALLEY/MOUNTAIN.
     * @param segment The segment to bend around. Note that the direction vector of the segment specifies which part of
     *            the paper will be rotated.
     * @param layerInts A map of affected layers and intersections of the fold stripe with them.
     * @param angle The angle the paper should be bent by (in radians). Value in (0, PI) means that the down right part
     *            of the paper (with respect to the line) will be moved; value in (-PI,0) means that the other part of
     *            paper will be moved.
     * @param neighborTest A callback for narrowing the set of neighbor triangles that should be bent. Pass
     *            <code>null</code> if you don't need this callback.
     */
    protected void bendPaper(Direction direction, Segment3d segment, Map<Layer, Segment3d> layerInts, double angle,
            NeighborInclusionTest neighborTest)
    {
        bendPaper(direction, segment, null, layerInts, angle, neighborTest);
    }

    /**
     * Bends the paper. Requires that the fold line goes only along triangle edges, not through the interiors of them.
     * <p>
     * To specify the part of the paper that will be rotated, imagine a plane going through <code>segment</code> and
     * perpendicular to the layer <code>segment</code> lies in. This is a halfspace's border plane. Then
     * <code>refPoint</code> specifies the halfspace containing the parts of the paper that are to be bent.
     * 
     * @param direction The direction of the fold - VALLEY/MOUNTAIN.
     * @param segment The segment to bend around. Note that the direction vector of the segment specifies which part of
     *            the paper will be rotated.
     * @param refPoint A reference point lying in the halfspace containing the parts of the paper that are to be bent.
     *            Pass <code>null</code> to autodetermine the part from <code>segment</code>'s direction vector as
     *            described in {@link ModelState#bendPaper(Direction, Segment3d, Map, double)}. Please ensure
     *            <code>refPoint</code> doesn't lie in the border plane as described above, this will not be checked.
     * @param layerInts A map of affected layers and intersections of the fold stripe with them.
     * @param angle The angle the paper should be bent by (in radians). Value in (0, PI) means that the down right part
     *            of the paper (with respect to the line) will be moved; value in (-PI,0) means that the other part of
     *            paper will be moved.
     */
    protected void bendPaper(Direction direction, Segment3d segment, Point3d refPoint, Map<Layer, Segment3d> layerInts,
            double angle)
    {
        bendPaper(direction, segment, refPoint, layerInts, angle, null);
    }

    /**
     * Bends the paper. Requires that the fold line goes only along triangle edges, not through the interiors of them.
     * <p>
     * To specify the part of the paper that will be rotated, imagine a plane going through <code>segment</code> and
     * perpendicular to the layer <code>segment</code> lies in. This is a halfspace's border plane. Then
     * <code>refPoint</code> specifies the halfspace containing the parts of the paper that are to be bent.
     * 
     * @param direction The direction of the fold - VALLEY/MOUNTAIN.
     * @param segment The segment to bend around. Note that the direction vector of the segment specifies which part of
     *            the paper will be rotated.
     * @param refPoint A reference point lying in the halfspace containing the parts of the paper that are to be bent.
     *            Pass <code>null</code> to autodetermine the part from <code>segment</code>'s direction vector as
     *            described in {@link ModelState#bendPaper(Direction, Segment3d, Map, double, NeighborInclusionTest)}.
     *            Please ensure <code>refPoint</code> doesn't lie in the border plane as described above, this will not
     *            be checked.
     * @param layerInts A map of affected layers and intersections of the fold stripe with them.
     * @param angle The angle the paper should be bent by (in radians). Value in (0, PI) means that the down right part
     *            of the paper (with respect to the line) will be moved; value in (-PI,0) means that the other part of
     *            paper will be moved.
     * @param neighborTest A callback for narrowing the set of neighbor triangles that should be bent. Pass
     *            <code>null</code> if you don't need this callback.
     */
    protected void bendPaper(Direction direction, Segment3d segment, Point3d refPoint, Map<Layer, Segment3d> layerInts,
            double angle, NeighborInclusionTest neighborTest)
    {
        double angle1 = angle;
        if (abs(angle1) < EPSILON)
            return;

        if (direction == Direction.MOUNTAIN)
            angle1 = -angle1;

        Point3d r;
        if (refPoint == null) {
            // find a reference point by taking the cross product of the normal of the layer the segment lies in, and
            // the direction vector of the segment; add this vector to a point on the segment to get a general point in
            // the halfspace that contains the layer to be bent
            Point3d segCenter = new Point3d(segment.getP1());
            segCenter.add(segment.getP2());
            segCenter.scale(0.5d);

            Layer segLayer = getLayerForPoint(segCenter);
            Vector3d layerNormalSegmentDirCross = new Vector3d();
            layerNormalSegmentDirCross.cross(segLayer.getNormal(), segment.getVector());
            Vector3d v = new Vector3d(layerNormalSegmentDirCross);
            v.normalize();
            r = new Point3d(v);
            r.add(segCenter);
        } else {
            r = refPoint;
        }

        // the halfspace that contains the layer to be bent
        HalfSpace3d halfspace = HalfSpace3d.createPerpendicularToTriangle(segment.getP1(), segment.getP2(), r);

        // further we will need to search in layerInts, but the layers will probably change, so we backup the old
        // removed layers here
        Hashtable<Layer, Layer> newLayersToOldOnes = new Hashtable<Layer, Layer>();

        // fill this queue with triangles from the new layers that need to be bent
        Queue<ModelTriangle> queue = new LinkedList<ModelTriangle>();
        for (Entry<Layer, Segment3d> layerInt : layerInts.entrySet()) {
            Layer layer = layerInt.getKey();
            Segment3d splitSegment = layerInt.getValue();

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

            if (swapParts) {
                List<Polygon3d<ModelTriangle>> tmp = part1;
                part1 = part2;
                part2 = tmp;
            }

            // remove the old layer and add the new ones
            this.layers.remove(layer);
            for (Polygon3d<ModelTriangle> l : part1) {
                Layer newL = new Layer(l);
                this.layers.add(newL);
                newLayersToOldOnes.put(newL, layer);
            }
            for (Polygon3d<ModelTriangle> l : part2) {
                Layer newL = new Layer(l);
                this.layers.add(newL);
                newLayersToOldOnes.put(newL, layer);
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

        for (Layer l : layersToRotate) {
            // remove, rotate, and then add the triangles back to make sure all caches and maps will hold the correct
            // value
            triangles.removeAll(l.getTriangles());
            l.rotate(segment, angle1);
            triangles.addAll(l.getTriangles());
        }
    }

    /**
     * Make a reverse fold and bend the paper correspondingly.
     * 
     * @param direction The direction of the fold along <code>line</code>.
     * @param line The line to bend along.
     * @param oppositeLine The second line to fold along. Pass <code>null</code> to autocompute. If you do so, you must
     *            also pass <code>null</code> to <code>oppositeAffectedLayer</code>.
     * @param refLine The line around which the paper will twist (you'd press on this line with your finger when doing
     *            the fold manually).
     * @param affectedLayers Indices of the layers to fold along <code>line</code>.
     * @param oppositeAffectedLayers Indices of the layers to fold along <code>oppositeLine</code>. Pass
     *            <code>null</code> to autocompute. If you do so, you must also pass <code>null</code> to
     *            <code>oppositeLine</code>.
     * 
     * @throws InvalidOperationException If the assumptions for doing this fold aren't satisfied.
     */
    public void makeReverseFold(Direction direction, Segment2d line, Segment2d oppositeLine, Segment2d refLine,
            List<Integer> affectedLayers, List<Integer> oppositeAffectedLayers) throws InvalidOperationException
    {
        Segment3d line3 = new Segment3d(locatePointFromPaperTo3D(line.getP1()), locatePointFromPaperTo3D(line.getP2()));
        Segment3d refLine3 = new Segment3d(locatePointFromPaperTo3D(refLine.getP1()),
                locatePointFromPaperTo3D(refLine.getP2()));

        // check if line and refLine have exactly one common point in 3D
        Segment3d intPoint = line3.getIntersection(refLine3);
        if (intPoint == null || !intPoint.getVector().epsilonEquals(new Vector3d(), EPSILON)) {
            throw new InvalidOperationException(
                    "line and refLine must intersect in exactly one point, but their intersection is: "
                            + (intPoint == null ? "empty" : intPoint.toStringAsIntersection()));
        }

        LinkedHashMap<Layer, Segment3d> lineAffectedLayers = new LinkedHashMap<Layer, Segment3d>(affectedLayers.size());
        // create the first fold
        makeFold(direction, line.getP1(), line.getP2(), affectedLayers, 0, lineAffectedLayers);

        Segment2d oppositeLine2;
        Segment3d oppositeLine3;
        List<Integer> oppositeLayers;

        if (oppositeLine == null) {
            // neither oppositeLine nor oppositeAffectedLayer were specified, so we must compute them
            // line and refLine don't have to intersect on the paper, so we can treat them as lines
            oppositeLine2 = new Segment2d(new Line2d(refLine).mirror(new Line2d(line)));
            // check if the oppositeLine lies in the paper
            if (!getOrigami().getModel().getPaper().containsRelative(oppositeLine2.getP1())
                    || !getOrigami().getModel().getPaper().containsRelative(oppositeLine2.getP2())) {
                throw new InvalidOperationException(
                        "The opposite side of line couldn't be found (the found one doesn't lie on the paper). "
                                + "Please specify it implicitly in the XML source file.");
            }

            oppositeLine3 = new Segment3d(locatePointFromPaperTo3D(oppositeLine2.getP1()),
                    locatePointFromPaperTo3D(oppositeLine2.getP2()));

            oppositeLayers = getOppositeLineAffectedLayers(line3, oppositeLine3, lineAffectedLayers.keySet());

        } else {
            oppositeLine2 = oppositeLine;
            oppositeLayers = oppositeAffectedLayers;

            oppositeLine3 = new Segment3d(locatePointFromPaperTo3D(oppositeLine2.getP1()),
                    locatePointFromPaperTo3D(oppositeLine2.getP2()));

            Segment3d intersection = oppositeLine3.getIntersection(line3);
            if (intersection == null) {
                throw new InvalidOperationException("line and oppositeLine don't intersect.");
            } else {
                Segment3d intersection2 = refLine3.getIntersection(intersection);
                if (intersection2 == null) {
                    throw new InvalidOperationException("line and oppositeLine don't intersect on refLine.");
                } else if (!intersection2.getVector().epsilonEquals(new Vector3d(), EPSILON)) {
                    throw new InvalidOperationException("line and oppositeLine can't be parallel to refLine.");
                }
            }
        }

        LinkedHashMap<Layer, Segment3d> oppositeAffectedLayers2 = new LinkedHashMap<Layer, Segment3d>(
                oppositeLayers.size());
        // create the opposite fold
        makeFold(direction.getOpposite(), oppositeLine2.getP1(), oppositeLine2.getP2(), oppositeLayers, 0,
                oppositeAffectedLayers2);

        // bend the paper
        bendReverseFold(direction, line3, oppositeLine3, refLine3, lineAffectedLayers, oppositeAffectedLayers2);
    }

    /**
     * Bend a reverse fold. Requires that all the fold lines have been created yet.
     * 
     * <code>line</code>, <code>opposite</code> and <code>refLine</code> must all meet just in a single point.
     * 
     * @param direction The direction of the fold.
     * @param line The first line to fold along.
     * @param opposite The second line to fold along.
     * @param refLine The line around which the paper will twist (you'd press on this line with your finger when doing
     *            the fold manually).
     * @param lineAffectedLayers The layers to be bent over <code>line</code>, mapped to intersections with the stripe
     *            going through <code>line</code>.
     * @param oppositeAffectedLayers The layers to be bent over <code>opposite</code>, mapped to intersections with the
     *            stripe going through <code>opposite</code>.
     */
    public void bendReverseFold(Direction direction, Segment3d line, Segment3d opposite, Segment3d refLine,
            Map<Layer, Segment3d> lineAffectedLayers, Map<Layer, Segment3d> oppositeAffectedLayers)
    {
        // ________________lineP...oppositeP_____________.................
        // ................l/|........|\o....................O.\......../.A
        // ...............i/.|........|.\p....................N.\....../.N
        // .ONE.SIDE.....n/..|........|..\p..ANOTHER.SIDE......E.\..../.O
        // .............e/...|........|...\o......................\../.T
        // _____________/____|........|____\s____________.......S..\/.H.
        // ............^...refLine.refLine..^...................IDE...ER.SIDE
        // ............|....................|
        // ..........common..............common

        Point3d lineP = line.getP1();
        Point3d common = line.getP2();
        if (!opposite.contains(common)) {
            lineP = line.getP2();
            common = line.getP1();
        }
        Point3d oppositeP = opposite.getP1();
        if (oppositeP.epsilonEquals(common, EPSILON))
            oppositeP = opposite.getP2();

        // refLineEnd is the border point of refLine that doesn't meet with line and opposite
        Point3d refLineEnd = refLine.getP1();
        if (refLine.getP1().epsilonEquals(common, EPSILON))
            refLineEnd = refLine.getP2();

        Vector3d lineP_oppositeP = new Vector3d(oppositeP);
        lineP_oppositeP.sub(lineP);
        if (lineP_oppositeP.epsilonEquals(new Vector3d(), EPSILON))
            // line and opposite are equal, so we need another way to determine the dividing plane's normal
            // TODO this may not be sufficient handling of this corner case
            lineP_oppositeP.cross(line.getVector(), refLine.getVector());

        // dividing plane goes along refLine and halves the space between lineP and oppositeP
        Plane3d dividingPlane = new Plane3d(lineP_oppositeP, refLine.getP1());

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
            mirrorPlaneDirection.cross(line.getVector(), refLine.getVector());
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

        Point3d nearestLinePoint = line.getNearestPoint(refLineEnd);

        Vector3d v1 = new Vector3d(refLineEnd);
        v1.sub(nearestLinePoint);
        Vector3d v2 = new Vector3d(mirroredRefLineEnd);
        v2.sub(nearestLinePoint);

        // and here we are the angle
        double angle = v1.angle(v2);

        // angle is in [0,PI], but it may be needed to be in [PI,2PI] to do the right rotation; so we try to rotate the
        // refLineEnd, and if it doesn't correspond to mirroredRefLineEnd, we know we need the complementary angle
        if (!MathHelper.rotate(refLineEnd, line, angle).epsilonEquals(mirroredRefLineEnd, EPSILON))
            angle = -angle;

        // bendPaper() also performs this change, but we already have the correct angle, so we call it once againg to
        // cancel the change
        if (direction == Direction.MOUNTAIN)
            angle = -angle;

        // and now just bend the two parts of paper
        bendPaper(direction, line, refLineEnd, lineAffectedLayers, angle, neighborTest);

        bendPaper(direction.getOpposite(), opposite, refLineEnd, oppositeAffectedLayers, angle, neighborTest);
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
     * This function returns the layers that intersect with a stripe defined by the two given points and that is
     * perpendicular to the layer the line lies in.
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
    protected LinkedHashMap<Layer, Segment3d> getLayers(final Segment3d segment)
    {
        // finds the top layer
        final Point3d center = new Point3d(segment.getP1());
        center.add(segment.getP2());
        center.scale(0.5d);

        final Layer firstLayer = getLayerForPoint(center);
        if (firstLayer == null)
            return new LinkedHashMap<Layer, Segment3d>();

        // find another layers: is done by creating a stripe perpendicular to the first layer and finding intersections
        // of the stripe with triangles

        final Vector3d stripeDirection = firstLayer.getNormal();
        final Line3d p1line = new Line3d(segment.getP1(), stripeDirection);
        final Line3d p2line = new Line3d(segment.getP2(), stripeDirection);

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
    protected LinkedHashMap<Layer, Segment3d> getLayers(final Stripe3d stripe)
    {
        final LinkedHashMap<Layer, Segment3d> result = new LinkedHashMap<Layer, Segment3d>();

        for (Layer l : layers) {
            final Segment3d intSegment = l.getIntersectionSegment(stripe);
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

        final LinkedHashMap<Layer, Segment3d> sortedResult = new LinkedHashMap<Layer, Segment3d>(result.size());
        for (Layer l : keys) {
            sortedResult.put(l, result.get(l));
        }

        return sortedResult;
    }

    /**
     * Return the layer that contains the given point.
     * 
     * @param point The point to find layer for.
     * @return The layer that contains the given point.
     */
    protected Layer getLayerForPoint(Point3d point)
    {
        for (Layer l : layers) {
            if (l.contains(point))
                return l;
        }
        Logger.getLogger(getClass()).warn("getLayerForPoint: cannot find layer for point " + point);
        return null;
    }

    /**
     * Given all triangles in one layer, divides all triangles in the layer by the given line to smaller triangles.
     * 
     * @param layer The triangles in the layer with the appropriate intersection points.
     * @param direction The direction of the created fold.
     * @param segment The segments defining the fold directly in this layer.
     * 
     * @return The intersections of the given segment with the layer.
     */
    protected List<Segment3d> makeFoldInLayer(Layer layer, Direction direction, Segment3d segment)
    {
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
                        line.setDirection(direction);
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
     * Return indices of layers to be used in {@link this#makeFold(Direction, Point2d, Point2d, List, double)} for
     * <code>opposite</code>. That means, construct a triangle from <code>line</code> and <code>opposite</code> (they
     * are required to have a common point) and return all layers intersecting that triangle that aren't in the affected
     * layers of <code>line</code>.
     * 
     * TODO a lot of getLayers() usage can be cached
     * 
     * @param line The original fold line.
     * @param opposite The <code>line</code>'s opposite line (it's image in 2D axis symmetry around an axis that has a
     *            common point with <code>line</code>).
     * @param lineAffectedLayers The set of affected layers for <code>line</code>.
     * @return The corresponding list of indices of affected layers for <code>opposite</code>. <code>null</code> if for
     *         the given <code>line</code> and <code>opposite</code> no solution is available (eg. one of them has zero
     *         direction vector or they together form a line).
     */
    protected List<Integer> getOppositeLineAffectedLayers(Segment3d line, Segment3d opposite,
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
            LinkedHashMap<Layer, Segment3d> layerInts = getLayers(stripe);
            for (Iterator<Entry<Layer, Segment3d>> it = layerInts.entrySet().iterator(); it.hasNext();) {
                Entry<Layer, Segment3d> entry = it.next();
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

            // now just walk through oppositeLineLayers and return the indices that are also in oppositeLayers
            List<Integer> result = new LinkedList<Integer>();
            int i = 1;
            for (Layer l : oppositeLineLayers) {
                if (oppositeLayers.contains(l))
                    result.add(i);
                i++;
            }
            return result;
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
            LinkedHashMap<Layer, Segment3d> oppositeLayerInts = getLayers(opposite);
            List<Integer> result = new LinkedList<Integer>();
            int i = 1;
            for (Entry<Layer, Segment3d> entry : oppositeLayerInts.entrySet()) {
                intersection = entry.getValue().getIntersection(opposite);
                if (intersection != null && !intersection.getVector().epsilonEquals(new Vector3d(), EPSILON)
                        && !lineAffectedLayers.contains(entry.getKey()))
                    result.add(i);
                i++;
            }
            return result;
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

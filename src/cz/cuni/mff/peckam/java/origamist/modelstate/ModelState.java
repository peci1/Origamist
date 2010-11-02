/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.modelstate;

import static cz.cuni.mff.peckam.java.origamist.math.MathHelper.EPSILON;
import static java.lang.Math.abs;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javax.media.j3d.GeometryArray;
import javax.media.j3d.LineArray;
import javax.media.j3d.TriangleArray;
import javax.vecmath.Color4b;
import javax.vecmath.Point2d;
import javax.vecmath.Point3d;
import javax.vecmath.Point4d;
import javax.vecmath.Vector3d;

import cz.cuni.mff.peckam.java.origamist.math.HalfSpace3d;
import cz.cuni.mff.peckam.java.origamist.math.Line3d;
import cz.cuni.mff.peckam.java.origamist.math.MathHelper;
import cz.cuni.mff.peckam.java.origamist.math.Segment3d;
import cz.cuni.mff.peckam.java.origamist.math.Triangle2d;
import cz.cuni.mff.peckam.java.origamist.math.Triangle3d;
import cz.cuni.mff.peckam.java.origamist.model.DoubleDimension;
import cz.cuni.mff.peckam.java.origamist.model.Origami;
import cz.cuni.mff.peckam.java.origamist.model.Step;
import cz.cuni.mff.peckam.java.origamist.model.UnitDimension;
import cz.cuni.mff.peckam.java.origamist.model.UnitHelper;
import cz.cuni.mff.peckam.java.origamist.model.jaxb.Unit;
import cz.cuni.mff.peckam.java.origamist.modelstate.Fold.FoldLine;
import cz.cuni.mff.peckam.java.origamist.utils.ChangeNotification;
import cz.cuni.mff.peckam.java.origamist.utils.Observer;
import cz.cuni.mff.peckam.java.origamist.utils.ObservableList;
import cz.cuni.mff.peckam.java.origamist.utils.ObservableList.ChangeTypes;

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
    protected ObservableList<Fold>          folds                 = new ObservableList<Fold>();

    /**
     * Cache for array of the lines representing folds.
     */
    protected LineArray                          foldLineArray         = null;

    /**
     * If true, the value of foldLineArray doesn't have to be consistent and a call to updateLineArray is needed.
     */
    protected boolean                            foldLineArrayDirty    = true;

    /**
     * The triangles this model state consists of.
     */
    protected ObservableList<ModelTriangle> triangles             = new ObservableList<ModelTriangle>();

    /**
     * The triangles the model state consists of. This representation can be directly used by Java3D.
     */
    protected TriangleArray                      trianglesArray        = null;

    /**
     * The triangles the model state consists of. This representation can be directly used by Java3D. The triangles face
     * the other side than the corresponding ones in trianglesArray.
     */
    protected TriangleArray                      inverseTrianglesArray = null;

    /**
     * If true, the value of trianglesArray doesn't have to be consistent and a call to updateVerticesArray is needed.
     */
    protected boolean                            trianglesArrayDirty   = true;

    /**
     * Rotation of the model (around the axis from eyes to display) in radians.
     */
    protected double                             rotationAngle         = 0;

    /**
     * The angle the model is viewed from (angle between eyes and the unfolded paper surface) in radians.
     * 
     * PI/2 means top view, -PI/2 means bottom view
     */
    protected double                             viewingAngle          = Math.PI / 2.0;

    /**
     * The step this state belongs to.
     */
    protected Step                               step;

    /**
     * The origami model which is this the state of.
     */
    protected Origami                            origami;

    /**
     * The number of steps a foldline remains visible.
     */
    protected int                                stepBlendingTreshold  = 5;

    public ModelState()
    {
        folds.addObserver(new Observer<Fold>() {
            @Override
            public void changePerformed(ChangeNotification<Fold> change)
            {
                ModelState.this.foldLineArrayDirty = true;

                if (change.getChangeType() == ChangeTypes.ADD) {
                    change.getItem().lines.addObserver(new Observer<FoldLine>() {
                        @Override
                        public void changePerformed(ChangeNotification<FoldLine> change)
                        {
                            ModelState.this.foldLineArrayDirty = true;
                        }
                    });
                }
            }
        });

        triangles.addObserver(new Observer<ModelTriangle>() {
            @Override
            public void changePerformed(ChangeNotification<ModelTriangle> change)
            {
                ModelState.this.trianglesArrayDirty = true;
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
     * Takes a point defined in the 2D paper relative coordinates and returns the position of the point in the 3D model
     * state (also in relative coordinates).
     * 
     * @param point
     * @return
     */
    protected Point3d locatePointFromPaperTo3D(Point2d point)
    {
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
            System.err.println("locatePointFromPaperTo3D: Couldn't locate point " + point);
            return new Point3d();
        }

        Vector3d barycentric = containingTriangle.getOriginalPosition().getBarycentricCoords(point);

        return containingTriangle.interpolatePointFromBarycentric(barycentric);
    }

    /**
     * Update the contents of the foldLineArray so that it corresponds to the actual contents of the folds variable.
     */
    protected synchronized void updateLineArray()
    {
        int linesCount = 0;
        for (Fold fold : folds) {
            linesCount += fold.lines.size();
        }

        UnitDimension paperSize = (UnitDimension) origami.getModel().getPaper().getSize();
        double ratio = UnitHelper.convertTo(Unit.REL, Unit.M, 1, paperSize.getUnit(), paperSize.getMax());

        foldLineArray = new LineArray(2 * linesCount, GeometryArray.COORDINATES | GeometryArray.COLOR_4);
        int i = 0;
        for (Fold fold : folds) {
            for (FoldLine line : fold.lines) {
                Point2d startPoint = new Point2d(line.line.getX1(), line.line.getY1());
                Point3d startPoint2 = locatePointFromPaperTo3D(startPoint);
                startPoint2.scale(ratio);
                foldLineArray.setCoordinate(2 * i, startPoint2);

                Point2d endPoint = new Point2d(line.line.getX2(), line.line.getY2());
                Point3d endPoint2 = locatePointFromPaperTo3D(endPoint);
                endPoint2.scale(ratio);
                foldLineArray.setCoordinate(2 * i + 1, endPoint2);

                // TODO implement some more line thickness and style possibilities
                byte alpha = (byte) 255;
                if (line.direction != null) {
                    // TODO invent some more sophisticated way to determine the fold "age"
                    int diff = step.getId() - fold.originatingStepId;
                    if (diff <= stepBlendingTreshold) {
                        alpha = (byte) (255 - (diff / stepBlendingTreshold) * 255);
                    } else {
                        alpha = 0;
                    }
                }
                foldLineArray.setColor(2 * i, new Color4b((byte) 0, (byte) 0, (byte) 0, alpha));
                foldLineArray.setColor(2 * i + 1, new Color4b((byte) 0, (byte) 0, (byte) 0, alpha));
                i++;
            }
        }

        foldLineArrayDirty = false;
    }

    /**
     * Retrurn the line array corresponding to the list of folds.
     * 
     * @return The line array corresponding to the list of folds.
     */
    public synchronized LineArray getLineArray()
    {
        if (foldLineArrayDirty)
            updateLineArray();

        return foldLineArray;
    }

    /**
     * Update the contents of the trianglesArray so that it corresponds to the actual contents of the triangles
     * variable.
     */
    protected synchronized void updateTrianglesArray()
    {
        trianglesArray = new TriangleArray(triangles.size() * 3, TriangleArray.COORDINATES);
        inverseTrianglesArray = new TriangleArray(triangles.size() * 3, TriangleArray.COORDINATES);

        UnitDimension paperSize = (UnitDimension) origami.getModel().getPaper().getSize();
        double ratio = 1.0 / UnitHelper.convertTo(Unit.REL, Unit.M, 1, paperSize.getUnit(), paperSize.getMax());

        int i = 0;
        Point3d p1, p2, p3;
        for (Triangle3d triangle : triangles) {
            p1 = (Point3d) triangle.getP1().clone();
            p1.project(new Point4d(p1.x, p1.y, p1.z, ratio));

            p2 = (Point3d) triangle.getP2().clone();
            p2.project(new Point4d(p2.x, p2.y, p2.z, ratio));

            p3 = (Point3d) triangle.getP3().clone();
            p3.project(new Point4d(p3.x, p3.y, p3.z, ratio));

            trianglesArray.setCoordinate(3 * i, p1);
            inverseTrianglesArray.setCoordinate(3 * i, (Point3d) p1.clone());

            trianglesArray.setCoordinate(3 * i + 1, p2);
            inverseTrianglesArray.setCoordinate(3 * i + 1, (Point3d) p3.clone());

            trianglesArray.setCoordinate(3 * i + 2, p3);
            inverseTrianglesArray.setCoordinate(3 * i + 2, (Point3d) p2.clone());

            i++;
        }

        trianglesArrayDirty = false;
    }

    /**
     * Return the triangle array.
     * 
     * @return The triangle array.
     */
    public synchronized TriangleArray getTrianglesArray()
    {
        if (trianglesArrayDirty)
            updateTrianglesArray();

        return trianglesArray;
    }

    /**
     * Return the inverse triangle array. (The triangles facing the other side than the ones returned by
     * getTrianglesArray)
     * 
     * @return The inverse triangle array.
     */
    public synchronized TriangleArray getInverseTrianglesArray()
    {
        if (trianglesArrayDirty)
            updateTrianglesArray();

        return inverseTrianglesArray;
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
        // TODO implement some way of defining which part of the paper will stay on its place and which will move
        List<List<IntersectionsWithTriangle>> layers = getLayers(startPoint, endPoint);
        int i = 1;
        for (List<IntersectionsWithTriangle> layer : layers) {
            if (!affectedLayers.contains(i++))
                continue;
            // TODO handle direction in some appropriate way
            makeFoldInLayer(layer, direction, startPoint, endPoint);
        }

        bendPaper(direction, startPoint, endPoint, affectedLayers, angle);
    }

    protected void bendPaper(Direction direction, Point2d startPoint, Point2d endPoint, List<Integer> affectedLayers,
            double angle)
    {
        Point3d start3d = locatePointFromPaperTo3D(startPoint);
        Point3d end3d = locatePointFromPaperTo3D(endPoint);
        Segment3d segment = new Segment3d(start3d, end3d);

        double angle1 = angle;
        if (abs(angle1) < EPSILON)
            return;

        if (direction == Direction.MOUNTAIN)
            angle1 = -angle1;

        DoubleDimension paperSize = origami.getModel().getPaper().getRelativeDimensions();

        Point3d r = new Point3d();
        if (angle1 > 0) {
            r = locatePointFromPaperTo3D(new Point2d(paperSize.getWidth(), 0));
            if (((Line3d) segment).contains(r)) {
                r = locatePointFromPaperTo3D(new Point2d(0, 0));
            }
        } else {
            r = locatePointFromPaperTo3D(new Point2d(0, paperSize.getHeight()));
            if (((Line3d) segment).contains(r)) {
                r = locatePointFromPaperTo3D(new Point2d(paperSize.getWidth(), paperSize.getHeight()));
            }
        }

        HalfSpace3d halfspace = HalfSpace3d.createPerpendicularToTriangle(segment.getP1(), segment.getP2(), r);
        List<List<IntersectionsWithTriangle>> layers = getLayers(startPoint, endPoint);
        List<ModelTriangle> trianglesToRotate = new LinkedList<ModelTriangle>();
        int i = 1;
        Queue<ModelTriangle> queue = new LinkedList<ModelTriangle>();
        for (List<IntersectionsWithTriangle> layer : layers) {
            if (!affectedLayers.contains(i++))
                continue;
            for (IntersectionsWithTriangle intWT : layer) {
                ModelTriangle t = intWT.getTriangle();
                if (!halfspace.contains(t.getP1()) || !halfspace.contains(t.getP2()) || !halfspace.contains(t.getP3()))
                    continue;
                queue.add(t);
            }
        }

        ModelTriangle t;
        while ((t = queue.poll()) != null) {
            if (!trianglesToRotate.contains(t))
                trianglesToRotate.add(t);
            Vector3d tNormal = t.getNormal();
            List<ModelTriangle> neighbours = findNeighbours(t);
            for (ModelTriangle n : neighbours) {
                if (trianglesToRotate.contains(n))
                    continue;

                if (!halfspace.contains(n.getP1()) || !halfspace.contains(n.getP2()) || !halfspace.contains(n.getP3()))
                    continue;
                double a = tNormal.angle(n.getNormal());
                if (a < EPSILON) {
                    queue.add(n);
                }
            }
        }

        for (ModelTriangle tr : trianglesToRotate) {
            tr.setPoints(MathHelper.rotate(tr.getP1(), segment, angle1), MathHelper.rotate(tr.getP2(), segment, angle1),
                    MathHelper.rotate(tr.getP3(), segment, angle1));
        }
        trianglesArrayDirty = true;
    }

    /**
     * Returns a list of triangles having a common point with the given triangle.
     * 
     * TODO probably will need some clever algorithm for speeding up.
     * 
     * @param t The triangle to find neighbours to.
     * @return The list of neighbours of t.
     */
    protected List<ModelTriangle> findNeighbours(ModelTriangle triangle)
    {
        List<ModelTriangle> result = new LinkedList<ModelTriangle>();
        List<Segment3d> triangleSides = new LinkedList<Segment3d>();
        triangleSides.add(triangle.getS1());
        triangleSides.add(triangle.getS2());
        triangleSides.add(triangle.getS3());

        for (ModelTriangle t : triangles) {
            List<Segment3d> tSides = new LinkedList<Segment3d>();
            tSides.add(t.getS1());
            tSides.add(t.getS2());
            tSides.add(t.getS3());

            out: for (Segment3d triangleSide : triangleSides) {
                for (Segment3d tSide : tSides) {
                    Point3d i = triangleSide.getIntersection(tSide);
                    if (i != null && Double.isNaN(i.x) && Double.isNaN(i.y) && Double.isNaN(i.z)) {
                        result.add(t);
                        break out;
                    }
                }
            }
        }

        return result;
    }

    /**
     * Returns a list of layers defined by the given line. TODO define and explain what a layer is.
     * 
     * @param startPoint Starting point of the line.
     * @param endPoint Ending point of the line.
     * @return A list of layers defined by the given line.
     */
    protected List<List<IntersectionsWithTriangle>> getLayers(Point2d startPoint, Point2d endPoint)
    {
        List<List<IntersectionsWithTriangle>> result = new ArrayList<List<IntersectionsWithTriangle>>();

        // finds the top layer
        Point3d p1 = locatePointFromPaperTo3D(startPoint);
        Point3d p2 = locatePointFromPaperTo3D(endPoint);
        result.add(getIntersectionsWithTriangles(new Segment3d(p1, p2)));

        // TODO find another layers
        // can be done by creating a stripe perpendicular to the first layer and finding intersections of the stripe
        // with triangles
        return result;
    }

    protected List<IntersectionsWithTriangle> getIntersectionsWithTriangles(Segment3d segment)
    {
        LinkedList<IntersectionsWithTriangle> result = new LinkedList<IntersectionsWithTriangle>();
        for (ModelTriangle t : triangles) {
            List<Point3d> intersections = t.getIntersection(segment);
            IntersectionsWithTriangle ints = new IntersectionsWithTriangle(t);
            ints.addAll(intersections);
            if (!intersections.isEmpty())
                result.add(ints);
        }
        return result;
    }

    /**
     * Given all triangles in one layer, divides all triangles in the layer by the given line to smaller triangles.
     * 
     * @param layer The triangles in the layer with the appropriate intersection points.
     * @param direction The direction of the created fold.
     * @param startPoint Starting point of the line.
     * @param endPoint Ending point of the line.
     */
    protected void makeFoldInLayer(List<IntersectionsWithTriangle> layer, Direction direction, Point2d startPoint,
            Point2d endPoint)
    {
        for (IntersectionsWithTriangle ints : layer) {
            if (ints.isWholeSideIntersection())
                continue; // TODO maybe change the type of the fold or something

            // find the two points of intersection - p1, p2
            List<Point3d> intPoints = new LinkedList<Point3d>();
            intPoints.addAll(ints);
            Point3d toDelete = ints.get(0);
            for (Point3d p : intPoints) {
                if (p == null)
                    toDelete = p;
            }

            ModelTriangle t = ints.getTriangle();

            // not a case where one of the intersection points is a vertex (but two distinct intersection points exist)
            if (toDelete == null) {

                intPoints.remove(toDelete);

                /*
                 * _________________________|_p1______________________________________________________________________
                 * __________________v*-----*-------------------*tv1__________________________________________________
                 * ___________________\_____|___________________/_____________________________________________________
                 * ____________________\____|__________________/______________________________________________________
                 * _____________________\___|_________________/_______________________________________________________
                 * ______________________\__|________________/________________________________________________________
                 * _______________________\_|_______________/_________________________________________________________
                 * ________________________\|______________/__________________________________________________________
                 * _________________________*_p2__________/___________________________________________________________
                 * _________________________|\___________/____________________________________________________________
                 * _________________________|_\_________/_____________________________________________________________
                 * _________________________|__\_______/______________________________________________________________
                 * _____________________________\_____/_______________________________________________________________
                 * ______________________________\___/________________________________________________________________
                 * _______________________________\_/_________________________________________________________________
                 * ________________________________*tv2_______________________________________________________________
                 * Please view this ASCII graphics without line-breaking (or break lines at minimum 120 characters)
                 */

                Point3d p1 = intPoints.get(0);
                Point3d p2 = intPoints.get(1);

                if (p1 == null || p2 == null)
                    throw new IllegalStateException(
                            "Invalid diagram: a fold beginning or ending in the interior of a triangle in step "
                                    + step.getId());
                // The line intersects the triangle only in a single vertex
                if (p1.epsilonEquals(p2, EPSILON))
                    continue;

                // find the sides of 3D triangle which contain the intersection points p1, p2 - save them into "sides"
                List<Segment3d> sides = new LinkedList<Segment3d>();
                sides.add(new Segment3d(t.getP1(), t.getP2()));
                sides.add(new Segment3d(t.getP2(), t.getP3()));
                sides.add(new Segment3d(t.getP3(), t.getP1()));
                Segment3d segToDelete = null;
                for (Segment3d side : sides) {
                    if (!side.contains(p1) && !side.contains(p2))
                        segToDelete = side;
                }
                sides.remove(segToDelete);

                // set v to the vertex of 3D triangle which lies alone in the halfplane defined by the triangle's plane
                // and line p1p2
                Point3d v = sides.get(0).getIntersection(sides.get(1));

                // tv1 is a vertex of 3D triangle such that p1 lies on the segment tv1v
                // tv2 is a vertex of 3D triangle such that p2 lies on the segment tv2v
                List<Point3d> triangleVertices = new LinkedList<Point3d>();
                triangleVertices.add(t.getP1());
                triangleVertices.add(t.getP2());
                triangleVertices.add(t.getP3());
                Point3d triangleVertToDelete = null;
                for (Point3d vert : triangleVertices) {
                    if (vert.epsilonEquals(v, EPSILON))
                        triangleVertToDelete = vert;
                }
                triangleVertices.remove(triangleVertToDelete);
                Point3d tv1 = triangleVertices.get(0);
                Point3d tv2 = triangleVertices.get(1);
                if (!new Line3d(v, tv1).contains(p1)) {
                    Point3d tmp = tv2;
                    tv2 = tv1;
                    tv1 = tmp;
                }

                // find the vertices of 2D triangle corresponding to the found 3D triangle vertices v, tv1, tv2; prefix
                // them with 'p'
                List<Point2d> paperVertices = new LinkedList<Point2d>();
                paperVertices.add(t.getOriginalPosition().getP1());
                paperVertices.add(t.getOriginalPosition().getP2());
                paperVertices.add(t.getOriginalPosition().getP3());
                Point2d pv = paperVertices.get(0);
                for (Point2d vert : paperVertices) {
                    if (locatePointFromPaperTo3D(vert).epsilonEquals(v, EPSILON))
                        pv = vert;
                }
                paperVertices.remove(pv);

                Point2d ptv1;
                Point2d ptv2;

                if (locatePointFromPaperTo3D(paperVertices.get(0)).epsilonEquals(tv1, EPSILON)) {
                    ptv1 = paperVertices.get(0);
                    ptv2 = paperVertices.get(1);
                } else {
                    ptv1 = paperVertices.get(1);
                    ptv2 = paperVertices.get(0);
                }

                // find points pp1, pp2 in the 2D triangle which correspond to p1, p2
                double v_tv1 = v.distance(tv1);
                double v_tv2 = v.distance(tv2);
                double v_p1 = v.distance(p1);
                double v_p2 = v.distance(p2);

                Point2d pp1 = new Point2d(pv.x + (v_p1 / v_tv1) * (tv1.x - pv.x), pv.y + (v_p1 / v_tv1)
                        * (tv1.y - pv.y));
                Point2d pp2 = new Point2d(pv.x + (v_p2 / v_tv2) * (tv2.x - pv.x), pv.y + (v_p2 / v_tv2)
                        * (tv2.y - pv.y));

                // construct the three newly defined triangles
                Vector3d tNormal = t.getNormal();

                Vector3d normal = new Triangle3d(p1, p2, v).getNormal();
                if (tNormal.angle(normal) < EPSILON)
                    triangles.add(new ModelTriangle(p1, p2, v, new Triangle2d(pp1, pp2, pv)));
                else
                    triangles.add(new ModelTriangle(p1, v, p2, new Triangle2d(pp1, pv, pp2)));

                normal = new Triangle3d(p1, p2, tv1).getNormal();
                if (tNormal.angle(normal) < EPSILON)
                    triangles.add(new ModelTriangle(p1, p2, tv1, new Triangle2d(pp1, pp2, ptv1)));
                else
                    triangles.add(new ModelTriangle(p1, tv1, p2, new Triangle2d(pp1, ptv1, pp2)));

                normal = new Triangle3d(p2, tv1, tv2).getNormal();
                if (tNormal.angle(normal) < EPSILON)
                    triangles.add(new ModelTriangle(p2, tv1, tv2, new Triangle2d(pp2, ptv1, ptv2)));
                else
                    triangles.add(new ModelTriangle(p2, tv2, tv1, new Triangle2d(pp2, ptv2, ptv1)));
            } else { // one of the intersection points is a vertex; the other inters. point is distinct from that one

                /*
                 * ________________________________|__________________________________________________________________
                 * ________________tv1*------------*p-----------*tv2__________________________________________________
                 * ___________________\____________|____________/_____________________________________________________
                 * ____________________\___________|___________/______________________________________________________
                 * _____________________\__________|__________/_______________________________________________________
                 * ______________________\_________|_________/________________________________________________________
                 * _______________________\________|________/_________________________________________________________
                 * ________________________\_______|_______/__________________________________________________________
                 * _________________________\______|______/___________________________________________________________
                 * __________________________\_____|_____/____________________________________________________________
                 * ___________________________\____|____/_____________________________________________________________
                 * ____________________________\___|___/______________________________________________________________
                 * _____________________________\__|__/_______________________________________________________________
                 * ______________________________\_|_/________________________________________________________________
                 * _______________________________\|/_________________________________________________________________
                 * ________________________________*v_________________________________________________________________
                 * Please view this ASCII graphics without line-breaking (or break lines at minimum 120 characters)
                 */

                // v is the intersection point which is also a vertex; p is the other intersection point
                Point3d deleted = intPoints.remove(2);
                if (intPoints.get(0).epsilonEquals(intPoints.get(1), EPSILON))
                    intPoints.set(0, deleted);
                else if (intPoints.get(1).epsilonEquals(deleted, EPSILON)) {
                    intPoints.set(1, intPoints.get(0));
                    intPoints.set(0, deleted);
                }

                Point3d v = intPoints.get(0);
                Point3d p = intPoints.get(1);

                // tv1, tv2 are the other vertices (v is the third one) of the 3D triangle
                List<Point3d> triangleVertices = new LinkedList<Point3d>();
                triangleVertices.add(t.getP1());
                triangleVertices.add(t.getP2());
                triangleVertices.add(t.getP3());
                Point3d triangleVertToDelete = null;
                for (Point3d vert : triangleVertices) {
                    if (vert.epsilonEquals(v, EPSILON))
                        triangleVertToDelete = vert;
                }
                triangleVertices.remove(triangleVertToDelete);
                Point3d tv1 = triangleVertices.get(0);
                Point3d tv2 = triangleVertices.get(1);

                // find the vertices of 2D triangle corresponding to the found 3D triangle vertices v, tv1, tv2; prefix
                // them with 'p'
                List<Point2d> paperVertices = new LinkedList<Point2d>();
                paperVertices.add(t.getOriginalPosition().getP1());
                paperVertices.add(t.getOriginalPosition().getP2());
                paperVertices.add(t.getOriginalPosition().getP3());
                Point2d pv = paperVertices.get(0);
                for (Point2d vert : paperVertices) {
                    if (locatePointFromPaperTo3D(vert).epsilonEquals(v, EPSILON))
                        pv = vert;
                }
                paperVertices.remove(pv);

                Point2d ptv1;
                Point2d ptv2;

                if (locatePointFromPaperTo3D(paperVertices.get(0)).epsilonEquals(tv1, EPSILON)) {
                    ptv1 = paperVertices.get(0);
                    ptv2 = paperVertices.get(1);
                } else {
                    ptv1 = paperVertices.get(1);
                    ptv2 = paperVertices.get(0);
                }

                // locate position corresponding to the 3D point p
                double tv1_p = tv1.distance(p);
                double tv1_tv2 = tv1.distance(tv2);

                Point2d pp = new Point2d(ptv1.x + (tv1_p / tv1_tv2) * (ptv2.x - ptv1.x), ptv1.y + (tv1_p / tv1_tv2)
                        * (ptv2.y - ptv1.y));

                Vector3d tNormal = t.getNormal();
                Vector3d normal = new Triangle3d(v, p, tv1).getNormal();
                // add two new triangles
                if (tNormal.angle(normal) < EPSILON)
                    triangles.add(new ModelTriangle(v, p, tv1, new Triangle2d(pv, pp, ptv1)));
                else
                    triangles.add(new ModelTriangle(v, tv1, p, new Triangle2d(pv, ptv1, pp)));

                normal = new Triangle3d(v, p, tv2).getNormal();
                if (tNormal.angle(normal) < EPSILON)
                    triangles.add(new ModelTriangle(v, p, tv2, new Triangle2d(pv, pp, ptv2)));
                else
                    triangles.add(new ModelTriangle(v, tv2, p, new Triangle2d(pv, ptv2, pp)));
            }

            // remove the old triangle
            triangles.remove(t);
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
            rotationAngle -= Math.PI;
        while (rotationAngle < -Math.PI)
            rotationAngle += Math.PI;
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

    @Override
    public Object clone() throws CloneNotSupportedException
    {
        ModelState result = new ModelState();

        result.step = this.step;
        result.origami = this.origami;
        result.rotationAngle = this.rotationAngle;
        result.viewingAngle = this.viewingAngle;
        result.stepBlendingTreshold = this.stepBlendingTreshold;

        for (Fold fold : folds)
            result.folds.add((Fold) fold.clone());

        for (Triangle3d t : triangles)
            result.triangles.add((ModelTriangle) t.clone());
        result.trianglesArrayDirty = true;

        return result;
    }

    /**
     * This is the list of intersections of a line/segment with a triangle.
     * 
     * @author Martin Pecka
     */
    protected class IntersectionsWithTriangle extends LinkedList<Point3d>
    {
        private static final long serialVersionUID = -2223450103552164376L;
        public ModelTriangle      triangle;

        public IntersectionsWithTriangle(ModelTriangle triangle)
        {
            super();
            this.triangle = triangle;
        }

        public ModelTriangle getTriangle()
        {
            return triangle;
        }

        public boolean isWholeSideIntersection()
        {
            return get(0) != null
                    && get(1) != null
                    && get(2) != null
                    && ((Double.isNaN(get(0).x) && Double.isNaN(get(0).y) && Double.isNaN(get(0).z))
                            || (Double.isNaN(get(1).x) && Double.isNaN(get(1).y) && Double.isNaN(get(1).z)) || (Double
                            .isNaN(get(2).x)
                            && Double.isNaN(get(2).y) && Double.isNaN(get(2).z)));
        }
    }
}

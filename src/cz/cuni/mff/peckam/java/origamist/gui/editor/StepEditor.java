/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.gui.editor;

import static java.lang.Math.abs;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;

import javax.media.j3d.Appearance;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.Group;
import javax.media.j3d.LineArray;
import javax.media.j3d.OrderedGroup;
import javax.media.j3d.PointArray;
import javax.media.j3d.PointAttributes;
import javax.media.j3d.PolygonAttributes;
import javax.media.j3d.RenderingAttributes;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.TransparencyAttributes;
import javax.media.j3d.TriangleArray;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.vecmath.Color3f;
import javax.vecmath.Point2d;
import javax.vecmath.Point3d;

import com.sun.j3d.utils.picking.PickCanvas;
import com.sun.j3d.utils.picking.PickResult;
import com.sun.j3d.utils.picking.PickTool;
import com.sun.j3d.utils.universe.ViewInfo;

import cz.cuni.mff.peckam.java.origamist.exceptions.InvalidOperationException;
import cz.cuni.mff.peckam.java.origamist.gui.common.StepRenderer;
import cz.cuni.mff.peckam.java.origamist.math.Segment3d;
import cz.cuni.mff.peckam.java.origamist.model.ModelPaper;
import cz.cuni.mff.peckam.java.origamist.model.Origami;
import cz.cuni.mff.peckam.java.origamist.model.Step;
import cz.cuni.mff.peckam.java.origamist.model.jaxb.Model;
import cz.cuni.mff.peckam.java.origamist.model.jaxb.ModelColors;
import cz.cuni.mff.peckam.java.origamist.modelstate.Direction;
import cz.cuni.mff.peckam.java.origamist.modelstate.Layer;
import cz.cuni.mff.peckam.java.origamist.modelstate.ModelPoint;
import cz.cuni.mff.peckam.java.origamist.modelstate.ModelSegment;
import cz.cuni.mff.peckam.java.origamist.modelstate.ModelState;

/**
 * A renderer of a step that supports mouse interaction with the step.
 * 
 * @author Martin Pecka
 */
public class StepEditor extends StepRenderer
{

    /** */
    private static final long        serialVersionUID       = -785240462702127380L;

    /** The canvas support for picking. Automatically updated when branch graph changes. */
    protected PickCanvas             pickCanvas;

    /** The transform for transforming vworld coordinates to image plate coordinates. */
    protected Transform3D            vWorldToImagePlate     = new Transform3D();

    /** The group containing all layers. */
    protected Group                  layers                 = null;

    /** The group containing all lines. */
    protected Group                  lines                  = null;

    /** The group that is always drawn after the model is drawn. */
    protected Group                  overModel              = null;

    /** The group that holds all displayed points. */
    protected Group                  pointGroup             = null;

    /** The group that holds the highlighted point to draw it over all other points. */
    protected Group                  highlightedPointGroup  = null;

    /** The factory that creates groups for given model points. */
    protected PointFactory           pointFactory           = new PointFactory();

    /** The list of (points/lines/layers - depends on pickMode) available by the last performed pick operation. */
    protected List<Group>            availableItems         = new LinkedList<Group>();

    /** The currently highlighted (picked) (point/line/layer - depends on pickMode). */
    protected Group                  highlighted            = null;

    /** The currently selected points, lines and layers. */
    protected HashSet<Group>         selected               = new HashSet<Group>();

    /** The set of currently selected layers. */
    protected Set<Layer>             selectedLayers         = new HashSet<Layer>();

    /** The set of currently selected lines. */
    protected Set<ModelSegment>      selectedLines          = new HashSet<ModelSegment>();

    /** The set of currently selected points. */
    protected Set<ModelPoint>        selectedPoints         = new HashSet<ModelPoint>();

    /** The type of primitves the user can pick. */
    protected PickMode               pickMode               = PickMode.POINT;

    /** The manager for changing layer appearances. */
    protected LayerAppearanceManager layerAppearanceManager = new LayerAppearanceManager();

    /** The manager for changing point appearances. */
    protected PointAppearanceManager pointAppearanceManager = new PointAppearanceManager();

    {
        updatePickCanvas();
        updateTransforms();

        addPropertyChangeListener("pickMode", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt)
            {
                clearHighlighted();
                clearAvailableItems();

                switch (pickMode) {
                    case POINT:
                        StepEditor.this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                        break;
                    case LINE:
                        StepEditor.this.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
                        break;
                    case LAYER:
                        StepEditor.this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                        break;
                }
            }
        });

        MouseListener listener = new MouseListener();
        addMouseWheelListener(listener);
        addMouseMotionListener(listener);
        addMouseListener(listener);
    }

    /**
     * 
     */
    public StepEditor()
    {
        super();
    }

    /**
     * @param origami
     * @param step
     */
    public StepEditor(Origami origami, Step step)
    {
        super(origami, step);
    }

    @Override
    public void setOrigami(Origami origami)
    {
        PropertyChangeListener bgListener = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt)
            {
                colorManager.setBackground((Color) evt.getNewValue());
            }
        };

        PropertyChangeListener fgListener = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt)
            {
                colorManager.setForeground((Color) evt.getNewValue());
            }
        };

        if (this.origami != null) {
            this.origami.removePropertyChangeListener(bgListener, Origami.MODEL_PROPERTY, Model.PAPER_PROPERTY,
                    ModelPaper.COLORS_PROPERTY, ModelColors.BACKGROUND_PROPERTY);
            this.origami.removePropertyChangeListener(fgListener, Origami.MODEL_PROPERTY, Model.PAPER_PROPERTY,
                    ModelPaper.COLORS_PROPERTY, ModelColors.FOREGROUND_PROPERTY);
        }

        super.setOrigami(origami);

        if (origami != null) {
            origami.addPropertyChangeListener(bgListener, Origami.MODEL_PROPERTY, Model.PAPER_PROPERTY,
                    ModelPaper.COLORS_PROPERTY, ModelColors.BACKGROUND_PROPERTY);
            origami.addPropertyChangeListener(fgListener, Origami.MODEL_PROPERTY, Model.PAPER_PROPERTY,
                    ModelPaper.COLORS_PROPERTY, ModelColors.FOREGROUND_PROPERTY);
        }
    }

    @Override
    public void setStep(Step step)
    {
        final Callable<Void> callback = new Callable<Void>() {
            @Override
            public Void call() throws Exception
            {
                setStep(StepEditor.this.step); // rebuild the view
                return null;
            }
        };

        if (this.step != step) {
            if (step != null)
                step.getModelStateInvalidationCallbacks().add(callback);
            if (this.step != null)
                this.step.getModelStateInvalidationCallbacks().remove(callback);
        }

        super.setStep(step);

        availableItems.clear();
        highlighted = null;
        selected.clear();
        selectedLayers.clear();
        selectedLines.clear();
        selectedPoints.clear();
    }

    @Override
    protected void afterSetStep()
    {
        super.afterSetStep();
        // branchGraph changes, so we have to recreate the pick canvas
        updatePickCanvas();
    }

    @Override
    protected PolygonAttributes createPolygonAttributes()
    {
        PolygonAttributes polyAttribs = super.createPolygonAttributes();
        polyAttribs.setCapability(PolygonAttributes.ALLOW_OFFSET_WRITE);
        return polyAttribs;
    }

    @Override
    protected Appearance createBaseTrianglesAppearance()
    {
        Appearance appearance = super.createBaseTrianglesAppearance();

        appearance.getColoringAttributes().setCapability(ColoringAttributes.ALLOW_COLOR_WRITE);
        appearance.getTransparencyAttributes().setCapability(TransparencyAttributes.ALLOW_VALUE_WRITE);

        return appearance;
    }

    @Override
    protected Appearance createBasicLinesAppearance()
    {
        Appearance appearance = super.createBasicLinesAppearance();

        appearance.getColoringAttributes().setCapability(ColoringAttributes.ALLOW_COLOR_WRITE);
        appearance.getTransparencyAttributes().setCapability(TransparencyAttributes.ALLOW_VALUE_WRITE);
        appearance.getRenderingAttributes().setCapability(RenderingAttributes.ALLOW_VISIBLE_WRITE);
        appearance.getRenderingAttributes().setCapability(RenderingAttributes.ALLOW_DEPTH_TEST_FUNCTION_WRITE);

        return appearance;
    }

    @Override
    protected TransformGroup setupTGroup() throws InvalidOperationException
    {
        try {
            setupTransform();

            ModelState state = step.getModelState();

            tGroup = new TransformGroup();
            tGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
            tGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
            tGroup.setTransform(transform);

            OrderedGroup og = new OrderedGroup();
            og.setPickable(true);

            TransformGroup model = new TransformGroup();
            model.setPickable(true);

            layers = new TransformGroup();
            layers.setPickable(true);

            TriangleArray[] triangleArrays = state.getTrianglesArrays();
            Shape3D top, bottom;
            Appearance appearance;
            Appearance appearance2;
            for (TriangleArray triangleArray : triangleArrays) {
                TransformGroup group = new TransformGroup();
                group.setBoundsAutoCompute(true);
                group.setUserData(triangleArray.getUserData()); // contains the layer
                group.setPickable(true);
                group.setCapability(Shape3D.ENABLE_PICK_REPORTING);

                appearance = createNormalTrianglesAppearance();
                appearance2 = createInverseTrianglesAppearance();

                top = new Shape3D(triangleArray, appearance);
                bottom = new Shape3D(triangleArray, appearance2);

                group.addChild(top);
                group.addChild(bottom);

                layers.addChild(group);
            }

            model.addChild(layers);

            LineArray[] lineArrays = state.getLineArrays();

            lines = new TransformGroup();
            lines.setPickable(true);
            lines.setCapability(TransformGroup.ALLOW_CHILDREN_EXTEND);
            lines.setCapability(TransformGroup.ALLOW_CHILDREN_READ);
            lines.setCapability(TransformGroup.ALLOW_CHILDREN_WRITE);

            Appearance appearance3;
            Shape3D shape;
            for (LineArray lineArray : lineArrays) {
                BranchGroup group = new BranchGroup();
                group.setBoundsAutoCompute(true);
                group.setUserData(lineArray.getUserData()); // contains the layer
                group.setPickable(true);
                group.setCapability(Shape3D.ENABLE_PICK_REPORTING);
                group.setCapability(BranchGroup.ALLOW_DETACH);
                group.setCapability(BranchGroup.ALLOW_PARENT_READ);

                ModelSegment segment = (ModelSegment) lineArray.getUserData();
                appearance3 = createBasicLinesAppearance();
                getLineAppearanceManager().alterBasicAppearance(appearance3, segment.getDirection(),
                        step.getId() - segment.getOriginatingStepId());

                shape = new Shape3D(lineArray, appearance3);

                group.addChild(shape);
                group.compile();

                lines.addChild(group);
            }
            model.addChild(lines);

            og.addChild(model);

            overModel = new TransformGroup();
            overModel.setCapability(TransformGroup.ALLOW_CHILDREN_EXTEND);
            overModel.setCapability(TransformGroup.ALLOW_CHILDREN_READ);
            overModel.setCapability(TransformGroup.ALLOW_CHILDREN_WRITE);
            overModel.setPickable(true);
            og.addChild(overModel);

            pointGroup = new BranchGroup();
            pointGroup.setCapability(TransformGroup.ALLOW_CHILDREN_EXTEND);
            pointGroup.setCapability(TransformGroup.ALLOW_CHILDREN_READ);
            pointGroup.setCapability(TransformGroup.ALLOW_CHILDREN_WRITE);
            pointGroup.setPickable(true);
            og.addChild(pointGroup);

            highlightedPointGroup = new BranchGroup();
            highlightedPointGroup.setCapability(TransformGroup.ALLOW_CHILDREN_EXTEND);
            highlightedPointGroup.setCapability(TransformGroup.ALLOW_CHILDREN_READ);
            highlightedPointGroup.setCapability(TransformGroup.ALLOW_CHILDREN_WRITE);
            highlightedPointGroup.setPickable(true);
            og.addChild(highlightedPointGroup);

            og.addChild(getMarkerGroups());

            tGroup.addChild(og);

            return tGroup;
        } catch (InvalidOperationException e) {
            tGroup = new TransformGroup();
            // TODO create an ErrorTransformGroup that would signalize to the user that an operation is invalid
            throw e;
        }
    }

    /**
     * A callback to be called when picking is to be performed.
     * 
     * The given coordinates are relative to the canvas.
     * 
     * @param x The x coordinte in the canvas.
     * @param y The y coordinte in the canvas.
     * @param e The mouse event that caused this callback to be called.
     */
    protected void pick(int x, int y, MouseEvent e)
    {
        if (branchGraph == null || !branchGraph.isLive())
            return;

        pickCanvas.setShapeLocation(x, y);

        List<PickResult> results;
        try {
            results = pickMode.filterPickResults(pickCanvas.pickAllSorted());
        } catch (NullPointerException ex) {
            // picking points sometimes causes this exception to be thrown, but if we ignore this pick call, nothing
            // serious happens
            return;
        }

        if (results.size() > 0) {
            if (pickMode == PickMode.LAYER) {
                boolean containsHighlighted = false;
                List<Group> newAvailableItems = new LinkedList<Group>();
                if (results.size() == availableItems.size()) {
                    Iterator<Group> it = availableItems.iterator();
                    boolean different = false;
                    for (PickResult r : results) {
                        TransformGroup tg = (TransformGroup) r.getNode(PickResult.TRANSFORM_GROUP);
                        if (!different && it.next() != tg)
                            different = true;
                        if (tg == highlighted)
                            containsHighlighted = true;
                        newAvailableItems.add(tg);
                    }
                    if (!different)
                        return;
                } else {
                    for (PickResult r : results) {
                        TransformGroup tg = (TransformGroup) r.getNode(PickResult.TRANSFORM_GROUP);
                        if (tg == highlighted)
                            containsHighlighted = true;
                        newAvailableItems.add(tg);
                    }
                }
                availableItems = newAvailableItems;

                if (containsHighlighted)
                    return;

                setHighlightedLayer(availableItems.get(0));
            } else if (pickMode == PickMode.LINE) {
                boolean containsHighlighted = false;
                List<Group> newAvailableItems = new LinkedList<Group>();
                if (results.size() == availableItems.size()) {
                    Iterator<Group> it = availableItems.iterator();
                    boolean different = false;
                    for (PickResult r : results) {
                        BranchGroup tg = (BranchGroup) r.getNode(PickResult.BRANCH_GROUP);
                        if (!different && it.next() != tg)
                            different = true;

                        // if some layers are selected, provide only those lines that lie in the selected layers
                        boolean isInSelectedLayers = selectedLayers.size() == 0;
                        ModelSegment seg = (ModelSegment) tg.getUserData();
                        for (Layer l : selectedLayers) {
                            if (l.liesInThisLayer(seg)) {
                                isInSelectedLayers = true;
                                break;
                            }
                        }
                        if (!isInSelectedLayers)
                            continue;

                        if (tg == highlighted)
                            containsHighlighted = true;

                        newAvailableItems.add(tg);
                    }
                    if (!different)
                        return;
                } else {
                    for (PickResult r : results) {
                        BranchGroup tg = (BranchGroup) r.getNode(PickResult.BRANCH_GROUP);

                        // if some layers are selected, provide only those lines that lie in the selected layers
                        boolean isInSelectedLayers = selectedLayers.size() == 0;
                        ModelSegment seg = (ModelSegment) tg.getUserData();
                        for (Layer l : selectedLayers) {
                            if (l.liesInThisLayer(seg)) {
                                isInSelectedLayers = true;
                                break;
                            }
                        }
                        if (!isInSelectedLayers)
                            continue;

                        if (tg == highlighted)
                            containsHighlighted = true;

                        newAvailableItems.add(tg);
                    }
                }
                availableItems = newAvailableItems;

                if (containsHighlighted)
                    return;

                if (availableItems.size() > 0)
                    setHighlightedLine(availableItems.get(0));
            } else if (pickMode == PickMode.POINT) {
                // update the projection transform
                updateTransforms();

                HashSet<ModelPoint> points = new HashSet<ModelPoint>(results.size());
                LinkedHashSet<Group> pointGroups = new LinkedHashSet<Group>();
                LinkedList<Group> pointsToAttach = new LinkedList<Group>();
                final double tolerance = 6d;

                Point2d evtPos = new Point2d(x, y);

                // if there are some selected points, add the close ones to the new available layers
                for (Group g : selected) {
                    if (g.getUserData() instanceof ModelPoint) {
                        if (evtPos.distance(getPointCanvasPosition(g)) < tolerance && liesInSelectedLinesOrLayers(g)) {
                            points.add((ModelPoint) g.getUserData());
                            pointGroups.add(g);
                        }
                    }
                }

                List<Group> snapPoints = new LinkedList<Group>();
                main: for (PickResult r : results) {
                    BranchGroup group = (BranchGroup) r.getNode(PickResult.BRANCH_GROUP);

                    // if we picked an existing point, just add it
                    if (group.getUserData() instanceof ModelPoint) {
                        if (pointGroups.contains(group) || !liesInSelectedLinesOrLayers(group))
                            continue main;
                        pointGroups.add(group);
                        points.add((ModelPoint) group.getUserData());
                        continue main;
                    }

                    // we have picked a fold line
                    ModelSegment userSegment = (ModelSegment) group.getUserData();

                    // if any of the already available points lies on the fold line, skip this line
                    for (Group g : pointGroups) {
                        if (userSegment.getOriginal().contains(((ModelPoint) g.getUserData()).getOriginal()))
                            continue main;
                    }

                    Point3d[] edges = r.getIntersection(0).getPrimitiveCoordinates();
                    assert edges.length == 2;
                    Segment3d vworldSegment = new Segment3d(edges[0], edges[1]);

                    Point3d vworldIntersection = r.getIntersection(0).getPointCoordinates();

                    double param = vworldSegment.getParameterForPoint(vworldIntersection);
                    Point3d point = userSegment.getPointForParameter(param);
                    Point2d point2 = userSegment.getOriginal().getPointForParameter(param);

                    ModelPoint modelPoint = new ModelPoint(point, point2);

                    if (points.contains(modelPoint))
                        continue;

                    for (ModelPoint p : points) {
                        if (p.epsilonEquals(modelPoint))
                            continue main;
                    }

                    boolean isSnapPoint = false;
                    // create the snap points (edges and center of the line) and try to use them
                    Point3d center = vworldSegment.getPointForParameter(0.5d);
                    Point3d[] snaps = new Point3d[] { new Point3d(vworldSegment.getP1()),
                            new Point3d(vworldSegment.getP2()), center };
                    for (Point3d p : snaps) {
                        if (evtPos.distance(getLocalPointCanvasPosition(p, r.getLocalToVworld())) < tolerance) {
                            vworldIntersection = p;
                            param = vworldSegment.getParameterForPoint(p);
                            point = userSegment.getPointForParameter(param);
                            point2 = userSegment.getOriginal().getPointForParameter(param);
                            modelPoint = new ModelPoint(point, point2);
                            isSnapPoint = true;
                            break;
                        }
                    }

                    // the point changed, so we must re-check if it doesn't collide with an already available point
                    if (isSnapPoint) {
                        if (points.contains(modelPoint))
                            continue main;

                        for (ModelPoint p : points) {
                            if (p.epsilonEquals(modelPoint))
                                continue main;
                        }
                    }

                    Group g = pointFactory.createPoint(modelPoint, vworldIntersection);
                    // make snap points look like squares
                    if (isSnapPoint)
                        ((Shape3D) g.getChild(0)).getAppearance().getPointAttributes()
                                .setPointAntialiasingEnable(false);

                    if (!liesInSelectedLinesOrLayers(g))
                        continue main;

                    points.add(modelPoint);
                    pointGroups.add(g);
                    pointsToAttach.add(g);
                    if (isSnapPoint)
                        snapPoints.add(g);
                }
                // we don't rather attach the new points while we iterate over pick results, it could cause mess
                for (Group g : pointsToAttach) {
                    pointGroup.addChild(g);
                }

                // change priorities of points
                LinkedList<Group> newAvailableItems = new LinkedList<Group>(pointGroups);
                newAvailableItems.removeAll(pointsToAttach);
                newAvailableItems.addAll(0, pointsToAttach);
                newAvailableItems.removeAll(snapPoints);
                newAvailableItems.addAll(0, snapPoints);

                availableItems.removeAll(newAvailableItems);
                for (Group g : availableItems) {
                    if (!selected.contains(g)) {
                        ((BranchGroup) g).detach();
                    }
                }

                availableItems = newAvailableItems;

                if (availableItems.size() > 0)
                    setHighlightedPoint(availableItems.get(0));
            }
        } else if (highlighted != null) {
            clearHighlighted();
            clearAvailableItems();
        }

    }

    /**
     * Call this method to update the pick canvas after offscreenCanvas or branchGraph changes.
     */
    protected void updatePickCanvas()
    {
        pickCanvas = new PickCanvas(offscreenCanvas, branchGraph);
        pickCanvas.setMode(PickTool.GEOMETRY_INTERSECT_INFO);
        pickCanvas.setTolerance(3f);
    }

    /**
     * Return true if the given point group lies in a selected line or layer.
     * 
     * @param point The point group to check.
     * @return true if the given point group lies in a selected line or layer.
     */
    protected boolean liesInSelectedLinesOrLayers(Group point)
    {
        if (selectedLayers.size() + selectedLines.size() == 0)
            return true;

        ModelPoint p = (ModelPoint) point.getUserData();
        Point2d pOrig = p.getOriginal();

        for (ModelSegment line : selectedLines) {
            if (line.getOriginal().contains(pOrig))
                return true;
        }

        for (Layer layer : selectedLayers) {
            if (layer.contains(p))
                return true;
        }

        return false;
    }

    /**
     * @return The type of primitives the user can pick.
     */
    public PickMode getPickMode()
    {
        return pickMode;
    }

    /**
     * @param pickMode The type of primitives the user can pick.
     */
    public void setPickMode(PickMode pickMode)
    {
        PickMode oldPickMode = this.pickMode;
        this.pickMode = pickMode;
        listeners.firePropertyChange("pickMode", oldPickMode, pickMode);
    }

    /**
     * Performs the changes needed to make a highlighted item unhighlighted.
     */
    protected void clearHighlighted()
    {
        if (highlighted == null)
            return;

        if (highlighted.getUserData() instanceof Layer) {
            setHighlightedLayer(null);
        } else if (highlighted.getUserData() instanceof ModelSegment) {
            setHighlightedLine(null);
        } else {
            setHighlightedPoint(null);
        }
    }

    /**
     * Performs the changes needed to make all selected items unselected.
     */
    protected void clearSelection()
    {
        if (selected.size() == 0)
            return;

        // temp is needed because direct usage of selected would lead to ConcurrentModificaationException
        List<Group> temp = new LinkedList<Group>(selected);
        for (Group g : temp) {
            if (g.getUserData() instanceof Layer)
                deselectLayer(g);
            else if (g.getUserData() instanceof ModelSegment)
                deselectLine(g);
            else
                deselectPoint(g);
        }
    }

    /**
     * Performs the changes needed to remove all available items.
     */
    protected void clearAvailableItems()
    {
        if (availableItems.size() == 0)
            return;

        for (Group g : availableItems) {
            if (g.getUserData() instanceof ModelPoint && g != highlighted && !selected.contains(g)) {
                ((BranchGroup) g).detach();
            }
        }

        availableItems.clear();
    }

    /**
     * Performs the changes needed to make a layer highlighted.
     * 
     * If another layer has been highlighted, un-highlight it before highlighting the new one.
     * 
     * If the given layer already has been highlighted, nothing happens.
     * 
     * @param layer The layer to highlight. Pass <code>null</code> to clear the highlight.
     */
    protected void setHighlightedLayer(Group layer)
    {
        if (layer == highlighted)
            return;

        if (highlighted != null) {
            if (!selected.contains(highlighted))
                layerAppearanceManager.setAppearance(highlighted, SelectionState.NORMAL);
            else
                layerAppearanceManager.setAppearance(highlighted, SelectionState.SELECTED);
            highlighted = null;
        }

        if (layer != null) {
            highlighted = layer;
            if (!selected.contains(layer))
                layerAppearanceManager.setAppearance(layer, SelectionState.HIGHLIGHTED);
            else
                layerAppearanceManager.setAppearance(layer, SelectionState.SELECTED_HIGHLIGHTED);
        }
    }

    /**
     * Performs the changes needed to make a line highlighted.
     * 
     * If another line has been highlighted, un-highlight it before highlighting the new one.
     * 
     * If the given line already has been highlighted, nothing happens.
     * 
     * @param line The line to highlight. Pass <code>null</code> to clear the highlight.
     */
    protected void setHighlightedLine(Group line)
    {
        if (line == highlighted)
            return;

        if (highlighted != null) {
            if (!selected.contains(highlighted))
                getLineAppearanceManager().setAppearance(highlighted, SelectionState.NORMAL);
            else
                getLineAppearanceManager().setAppearance(highlighted, SelectionState.SELECTED);
            highlighted = null;
        }

        if (line != null) {
            highlighted = line;
            if (!selected.contains(line))
                getLineAppearanceManager().setAppearance(line, SelectionState.HIGHLIGHTED);
            else
                getLineAppearanceManager().setAppearance(line, SelectionState.SELECTED_HIGHLIGHTED);
        }
    }

    /**
     * Performs the changes needed to make a point highlighted.
     * 
     * If another point has been highlighted, un-highlight it before highlighting the new one.
     * 
     * If the given point already has been highlighted, nothing happens.
     * 
     * @param point The point to highlight. Pass <code>null</code> to clear the highlight.
     */
    protected void setHighlightedPoint(Group point)
    {
        if (point == highlighted)
            return;

        if (highlighted != null) {
            if (!selected.contains(highlighted)) {
                pointAppearanceManager.setAppearance(highlighted, SelectionState.NORMAL);
                ((BranchGroup) highlighted).detach();
                if (availableItems.contains(highlighted)) {
                    pointGroup.addChild(highlighted);
                }
            } else {
                pointAppearanceManager.setAppearance(highlighted, SelectionState.SELECTED);
                ((BranchGroup) highlighted).detach();
                pointGroup.addChild(highlighted);
            }
            highlighted = null;
        }

        if (point != null) {
            highlighted = point;
            if (!selected.contains(point))
                pointAppearanceManager.setAppearance(point, SelectionState.HIGHLIGHTED);
            else
                pointAppearanceManager.setAppearance(point, SelectionState.SELECTED_HIGHLIGHTED);
            ((BranchGroup) highlighted).detach();
            highlightedPointGroup.addChild(highlighted);
        }
    }

    /**
     * Performs the changes needed to make a layer selected.
     * 
     * If the layer has already been selected, nothing happens.
     * 
     * @param layer The layer to select.
     */
    protected void selectLayer(Group layer)
    {
        if (selected.contains(layer))
            return;

        if (layer != highlighted)
            layerAppearanceManager.setAppearance(layer, SelectionState.SELECTED);
        else
            layerAppearanceManager.setAppearance(layer, SelectionState.SELECTED_HIGHLIGHTED);

        selected.add(layer);
        selectedLayers.add((Layer) layer.getUserData());
    }

    /**
     * Performs the changes needed to make a selected layer not selected.
     * 
     * If the given layer hasn't been selected, nothing happens.
     * 
     * @param layer The layer to deselect.
     */
    protected void deselectLayer(Group layer)
    {
        if (selected.remove(layer)) {
            if (layer != highlighted)
                layerAppearanceManager.setAppearance(layer, SelectionState.NORMAL);
            else
                layerAppearanceManager.setAppearance(layer, SelectionState.HIGHLIGHTED);
            selectedLayers.remove(layer.getUserData());
        }
    }

    /**
     * Performs the changes needed to make a line selected.
     * 
     * If the line has already been selected, nothing happens.
     * 
     * @param line The line to select.
     */
    protected void selectLine(Group line)
    {
        if (selected.contains(line))
            return;

        if (line != highlighted)
            getLineAppearanceManager().setAppearance(line, SelectionState.SELECTED);
        else
            getLineAppearanceManager().setAppearance(line, SelectionState.SELECTED_HIGHLIGHTED);

        selected.add(line);
        selectedLines.add((ModelSegment) line.getUserData());
    }

    /**
     * Performs the changes needed to make a selected line not selected.
     * 
     * If the given line hasn't been selected, nothing happens.
     * 
     * @param line The line to deselect.
     */
    protected void deselectLine(Group line)
    {
        if (selected.remove(line)) {
            if (line != highlighted)
                getLineAppearanceManager().setAppearance(line, SelectionState.NORMAL);
            else
                getLineAppearanceManager().setAppearance(line, SelectionState.HIGHLIGHTED);
            selectedLines.remove(line.getUserData());
        }
    }

    /**
     * Performs the changes needed to make a point selected.
     * 
     * If the point has already been selected, nothing happens.
     * 
     * @param point The point to select.
     */
    protected void selectPoint(Group point)
    {
        if (selected.contains(point))
            return;

        if (point != highlighted)
            pointAppearanceManager.setAppearance(point, SelectionState.SELECTED);
        else
            pointAppearanceManager.setAppearance(point, SelectionState.SELECTED_HIGHLIGHTED);

        selected.add(point);
        selectedPoints.add((ModelPoint) point.getUserData());
    }

    /**
     * Performs the changes needed to make a selected point not selected.
     * 
     * If the given point hasn't been selected, nothing happens.
     * 
     * @param point The point to deselect.
     */
    protected void deselectPoint(Group point)
    {
        if (selected.remove(point)) {
            if (point != highlighted) {
                pointAppearanceManager.setAppearance(point, SelectionState.NORMAL);
                if (!availableItems.contains(point)) {
                    ((BranchGroup) point).detach();
                }
            } else
                pointAppearanceManager.setAppearance(point, SelectionState.HIGHLIGHTED);
            selectedPoints.remove(point.getUserData());
        }
    }

    /**
     * Update the transformation matrices used to calculate on-canvas position of a 3D point.
     */
    protected void updateTransforms()
    {
        ViewInfo viewInfo = new ViewInfo(offscreenCanvas.getView());

        vWorldToImagePlate = new Transform3D();
        viewInfo.getImagePlateToVworld(offscreenCanvas, vWorldToImagePlate, null);
        vWorldToImagePlate.invert();
    }

    /**
     * Get the on-canvas position of the given vworld point.
     * 
     * This method doesn't alter the passed-in point.
     * 
     * @param point A point in vworld coordinates.
     * @return The pixel position of the point on the canvas.
     */
    protected Point2d getVworldPointCanvasPosition(Point3d point)
    {
        return getVworldPointCanvasPosition(point, false);
    }

    /**
     * Get the on-canvas position of the given vworld point.
     * 
     * @param point A point in vworld coordinates.
     * @param canAlterArgument If true, the the given point can be altered by this function, otherwise a copy of it is
     *            created.
     * @return The pixel position of the point on the canvas.
     */
    protected Point2d getVworldPointCanvasPosition(Point3d point, boolean canAlterArgument)
    {
        Point3d trans;
        if (!canAlterArgument)
            trans = new Point3d(point);
        else
            trans = point;

        vWorldToImagePlate.transform(trans);

        Point2d res = new Point2d();
        offscreenCanvas.getPixelLocationFromImagePlate(trans, res);

        return res;
    }

    /**
     * Get the on-canvas position of the given local point.
     * 
     * This method doesn't alter the passed-in point.
     * 
     * @param point A point in local coordinates.
     * @param localToVworld The transform for transforming local coordinates to vworld.
     * @return The pixel position of the point on the canvas.
     */
    protected Point2d getLocalPointCanvasPosition(Point3d point, Transform3D localToVworld)
    {
        return getLocalPointCanvasPosition(point, localToVworld, false);
    }

    /**
     * Get the on-canvas position of the given local point.
     * 
     * @param point A point in local coordinates.
     * @param localToVworld The transform for transforming local coordinates to vworld.
     * @param canAlterArgument If true, the the given point can be altered by this function, otherwise a copy of it is
     *            created.
     * @return The pixel position of the point on the canvas.
     */
    protected Point2d getLocalPointCanvasPosition(Point3d point, Transform3D localToVworld, boolean canAlterArgument)
    {
        Point3d trans;
        if (!canAlterArgument)
            trans = new Point3d(point);
        else
            trans = point;

        localToVworld.transform(trans);
        return getVworldPointCanvasPosition(trans, true);
    }

    /**
     * Get the on-canvas position of the given point.
     * 
     * The given group must contain a child node with index 0 having its Geometry of type PointArray.
     * 
     * @param point A point group.
     * @return The pixel position of the point on the canvas.
     */
    protected Point2d getPointCanvasPosition(Group point)
    {
        Point3d gPoint = new Point3d();
        ((PointArray) ((Shape3D) point.getChild(0)).getGeometry()).getCoordinate(0, gPoint);

        Transform3D localToVworld = new Transform3D();
        point.getLocalToVworld(localToVworld);

        return getLocalPointCanvasPosition(gPoint, localToVworld, true);
    }

    @Override
    protected ColorManager createColorManager(Color background, Color foreground)
    {
        return (ColorManager) (colorManager = new ColorManager(background == null ? Color.WHITE : background,
                foreground == null ? Color.white : foreground));
    }

    @Override
    protected ColorManager getColorManager()
    {
        return (ColorManager) super.getColorManager();
    }

    @Override
    protected LineAppearanceManager createLineAppearanceManager()
    {
        return (LineAppearanceManager) (lineAppearanceManager = new LineAppearanceManager());
    }

    @Override
    protected LineAppearanceManager getLineAppearanceManager()
    {
        return (LineAppearanceManager) super.getLineAppearanceManager();
    }

    protected class HighlightNextItemAction extends AbstractAction
    {

        /** */
        private static final long serialVersionUID = -3089302395435461134L;

        @Override
        public void actionPerformed(ActionEvent e)
        {
            int selIndex = availableItems.indexOf(highlighted);

            if (selIndex > -1) {
                selIndex = (selIndex + 1) % availableItems.size();
                switch (pickMode) {
                    case POINT:
                        setHighlightedPoint(availableItems.get(selIndex));
                        break;
                    case LINE:
                        setHighlightedLine(availableItems.get(selIndex));
                        break;
                    case LAYER:
                        setHighlightedLayer(availableItems.get(selIndex));
                        break;
                }
            }
        }

    }

    protected class HighlightPreviousItemAction extends AbstractAction
    {
        /** */
        private static final long serialVersionUID = -3089302395435461134L;

        @Override
        public void actionPerformed(ActionEvent e)
        {
            int selIndex = availableItems.indexOf(highlighted);

            if (selIndex > -1) {
                selIndex = selIndex - 1;
                if (selIndex == -1)
                    selIndex = availableItems.size() - 1;
                switch (pickMode) {
                    case POINT:
                        setHighlightedPoint(availableItems.get(selIndex));
                        break;
                    case LINE:
                        setHighlightedLine(availableItems.get(selIndex));
                        break;
                    case LAYER:
                        setHighlightedLayer(availableItems.get(selIndex));
                        break;
                }
            }
        }

    }

    protected class TogglePickModeAction extends AbstractAction
    {
        /** */
        private static final long serialVersionUID = -5513138483903360858L;

        @Override
        public void actionPerformed(ActionEvent e)
        {
            setPickMode(pickMode.getNext());
        }

    }

    protected class ToggleHighlightedItemSelectionAction extends AbstractAction
    {
        /** */
        private static final long serialVersionUID = -1297141033881147805L;

        @Override
        public void actionPerformed(ActionEvent e)
        {
            if (highlighted == null)
                return;

            switch (pickMode) {
                case POINT:
                    if (selected.contains(highlighted)) {
                        deselectPoint(highlighted);
                    } else {
                        selectPoint(highlighted);
                    }
                    break;
                case LINE:
                    if (selected.contains(highlighted)) {
                        deselectLine(highlighted);
                    } else {
                        selectLine(highlighted);
                    }
                    break;
                case LAYER:
                    if (selected.contains(highlighted)) {
                        deselectLayer(highlighted);
                    } else {
                        selectLayer(highlighted);
                    }
                    break;
            }
        }

    }

    protected class ClearSelectionAction extends AbstractAction
    {
        /** */
        private static final long serialVersionUID = 7889712823950928127L;

        @Override
        public void actionPerformed(ActionEvent e)
        {
            clearSelection();
        }

    }

    /**
     * Mouse event and picking handling.
     * 
     * @author Martin Pecka
     */
    protected class MouseListener extends MouseAdapter
    {
        @Override
        public void mouseWheelMoved(MouseWheelEvent e)
        {
            e.consume();
            int steps = e.getWheelRotation();
            if (steps == 0)
                return;

            if (!(e.isControlDown() || (e.getModifiersEx() & MouseEvent.BUTTON3_DOWN_MASK) > 0)
                    && availableItems.size() > 1 && highlighted != null) {
                // perform selection among available items
                if (steps > 0) {
                    Action action = new HighlightNextItemAction();
                    ActionEvent event = new ActionEvent(StepEditor.this, ActionEvent.ACTION_FIRST, "highlightNextItem");
                    action.actionPerformed(event);
                } else if (steps < 0) {
                    Action action = new HighlightPreviousItemAction();
                    ActionEvent event = new ActionEvent(StepEditor.this, ActionEvent.ACTION_FIRST,
                            "highlightPreviousItem");
                    action.actionPerformed(event);
                }
            }
        }

        @Override
        public void mouseMoved(MouseEvent e)
        {
            pick(e.getX(), e.getY(), e);
            removeUnnecessaryListeners();
        }

        @Override
        public void mouseClicked(MouseEvent e)
        {
            if (e.getButton() == MouseEvent.BUTTON1 && highlighted != null) {
                Action action = new ToggleHighlightedItemSelectionAction();
                ActionEvent event = new ActionEvent(StepEditor.this, ActionEvent.ACTION_FIRST,
                        "toggleHighlightedItemSelection");
                action.actionPerformed(event);
            } else if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() >= 2) {
                Action action = new ClearSelectionAction();
                ActionEvent event = new ActionEvent(StepEditor.this, ActionEvent.ACTION_FIRST, "clearSelection");
                action.actionPerformed(event);
            }
        }

        @Override
        public void mousePressed(MouseEvent e)
        {
            int mods = e.getModifiersEx();
            int middle = MouseEvent.BUTTON2_DOWN_MASK;
            int both = MouseEvent.BUTTON1_DOWN_MASK | MouseEvent.BUTTON3_DOWN_MASK;
            // either a middle button click or left+right button simultaneous click
            if (((mods & middle) == middle) || ((mods & both) == both)) {
                Action action = new TogglePickModeAction();
                ActionEvent event = new ActionEvent(StepEditor.this, ActionEvent.ACTION_FIRST, "togglePickMode");
                action.actionPerformed(event);
                pick(e.getX(), e.getY(), e);
                removeUnnecessaryListeners();
            }
        }
    }

    /**
     * A state of a selected item.
     * 
     * @author Martin Pecka
     */
    protected enum SelectionState
    {
        /** Normal appearance. */
        NORMAL,
        /** Highlighted item. */
        HIGHLIGHTED,
        /** Selected non-highlighted item. */
        SELECTED,
        /** Selected highlighted item. */
        SELECTED_HIGHLIGHTED
    }

    /**
     * A manager for changing layer appearance.
     * 
     * @author Martin Pecka
     */
    protected class LayerAppearanceManager
    {
        /**
         * Apply the given appearance on the given layer.
         * 
         * @param layer The layer to apply the appearance on.
         * @param state The state to derive appearance from.
         */
        protected void setAppearance(Group layer, SelectionState state)
        {
            assert layer.numChildren() == 2;

            Enumeration<?> children = layer.getAllChildren();

            Shape3D shape = (Shape3D) children.nextElement();
            Appearance app = shape.getAppearance();

            switch (state) {
                case NORMAL:
                    app.getColoringAttributes().setColor(getColorManager().getForeground3f());
                    app.getPolygonAttributes().setPolygonOffset(0f);
                    app.getPolygonAttributes().setPolygonOffsetFactor(0f);
                    app.getTransparencyAttributes().setTransparency(0f);
                    break;
                case HIGHLIGHTED:
                    app.getColoringAttributes().setColor(getColorManager().getHighlightFg3f());
                    app.getPolygonAttributes().setPolygonOffset(-20000f);
                    app.getPolygonAttributes().setPolygonOffsetFactor(-20000f);
                    app.getTransparencyAttributes().setTransparency(0f);
                    break;
                case SELECTED:
                    app.getColoringAttributes().setColor(getColorManager().getSelectedFg3f());
                    app.getPolygonAttributes().setPolygonOffset(-10000f);
                    app.getPolygonAttributes().setPolygonOffsetFactor(-10000f);
                    app.getTransparencyAttributes().setTransparency(0.3f);
                    break;
                case SELECTED_HIGHLIGHTED:
                    app.getColoringAttributes().setColor(getColorManager().getSelectedHighlightFg3f());
                    app.getPolygonAttributes().setPolygonOffset(-20000f);
                    app.getPolygonAttributes().setPolygonOffsetFactor(-20000f);
                    app.getTransparencyAttributes().setTransparency(0f);
                    break;
            }

            shape = (Shape3D) children.nextElement();
            app = shape.getAppearance();

            switch (state) {
                case NORMAL:
                    app.getColoringAttributes().setColor(getColorManager().getBackground3f());
                    app.getPolygonAttributes().setPolygonOffset(0f);
                    app.getPolygonAttributes().setPolygonOffsetFactor(0f);
                    app.getTransparencyAttributes().setTransparency(0f);
                    break;
                case HIGHLIGHTED:
                    app.getColoringAttributes().setColor(getColorManager().getHighlightBg3f());
                    app.getPolygonAttributes().setPolygonOffset(-20000f);
                    app.getPolygonAttributes().setPolygonOffsetFactor(-20000f);
                    app.getTransparencyAttributes().setTransparency(0f);
                    break;
                case SELECTED:
                    app.getColoringAttributes().setColor(getColorManager().getSelectedBg3f());
                    app.getPolygonAttributes().setPolygonOffset(-10000f);
                    app.getPolygonAttributes().setPolygonOffsetFactor(-10000f);
                    app.getTransparencyAttributes().setTransparency(0.3f);
                    break;
                case SELECTED_HIGHLIGHTED:
                    app.getColoringAttributes().setColor(getColorManager().getSelectedHighlightBg3f());
                    app.getPolygonAttributes().setPolygonOffset(-20000f);
                    app.getPolygonAttributes().setPolygonOffsetFactor(-20000f);
                    app.getTransparencyAttributes().setTransparency(0f);
                    break;
            }
        }
    }

    /**
     * A manager for changing point appearance.
     * 
     * @author Martin Pecka
     */
    protected class PointAppearanceManager
    {
        /**
         * Apply the given appearance on the given point.
         * 
         * @param point The point to apply the appearance on.
         * @param state The state to derive appearance from.
         */
        protected void setAppearance(Group point, SelectionState state)
        {
            assert point.numChildren() == 1;

            Enumeration<?> children = point.getAllChildren();

            Shape3D shape = (Shape3D) children.nextElement();
            Appearance app = shape.getAppearance();

            switch (state) {
                case NORMAL:
                    app.getRenderingAttributes().setVisible(false);
                    break;
                case HIGHLIGHTED:
                    app.getColoringAttributes().setColor(getColorManager().getHighlightedPoint3f());
                    app.getTransparencyAttributes().setTransparency(0f);
                    app.getRenderingAttributes().setVisible(true);
                    break;
                case SELECTED:
                    app.getColoringAttributes().setColor(getColorManager().getSelectedPoint3f());
                    app.getTransparencyAttributes().setTransparency(0.3f);
                    app.getRenderingAttributes().setVisible(true);
                    break;
                case SELECTED_HIGHLIGHTED:
                    app.getColoringAttributes().setColor(getColorManager().getSelectedHighlightedPoint3f());
                    app.getTransparencyAttributes().setTransparency(0f);
                    app.getRenderingAttributes().setVisible(true);
                    break;
            }
        }
    }

    /**
     * A factory for creating point groups from model points.
     * 
     * @author Martin Pecka
     */
    protected class PointFactory
    {
        /**
         * Create the group from the given point.
         * 
         * @param point The source point.
         * @param position The position of the point in local coordinates.
         * @return The group.
         */
        public Group createPoint(ModelPoint point, Point3d position)
        {
            final BranchGroup group = new BranchGroup();
            group.setUserData(point);
            group.setCapability(Shape3D.ENABLE_PICK_REPORTING);
            group.setCapability(BranchGroup.ALLOW_DETACH);
            group.setCapability(BranchGroup.ALLOW_PARENT_READ);
            group.setPickable(true);

            final float pointSize = 10f;

            final Appearance app = new Appearance();

            app.setPointAttributes(new PointAttributes());
            app.getPointAttributes().setPointAntialiasingEnable(true);
            app.getPointAttributes().setPointSize(pointSize * (float) getZoom() / 100f);
            app.getPointAttributes().setCapability(PointAttributes.ALLOW_SIZE_WRITE);
            app.getPointAttributes().setCapability(PointAttributes.ALLOW_ANTIALIASING_WRITE);

            final PropertyChangeListener listener = new PropertyChangeListener() {
                @Override
                public void propertyChange(PropertyChangeEvent evt)
                {
                    app.getPointAttributes().setPointSize(pointSize * (float) getZoom() / 100f);
                }
            };
            addPropertyChangeListener("zoom", listener);

            removeListenersCallbacks.add(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception
                {
                    if (group.getParent() == null) {
                        listeners.removePropertyChangeListener("zoom", listener);
                        return true;
                    }
                    return false;
                }
            });

            app.setColoringAttributes(new ColoringAttributes());
            app.getColoringAttributes().setCapability(ColoringAttributes.ALLOW_COLOR_WRITE);
            app.setTransparencyAttributes(new TransparencyAttributes());
            app.getTransparencyAttributes().setCapability(TransparencyAttributes.ALLOW_VALUE_WRITE);
            app.setRenderingAttributes(new RenderingAttributes());
            app.getRenderingAttributes().setCapability(RenderingAttributes.ALLOW_VISIBLE_WRITE);
            app.getRenderingAttributes().setDepthTestFunction(RenderingAttributes.ALWAYS);
            app.getRenderingAttributes().setVisible(false);

            PointArray array = new PointArray(1, PointArray.COORDINATES);
            array.setCoordinate(0, position);

            group.addChild(new Shape3D(array, app));
            group.compile();

            return group;
        }
    }

    /**
     * A manager for changing line appearance.
     * 
     * @author Martin Pecka
     */
    protected class LineAppearanceManager extends StepRenderer.LineAppearanceManager
    {
        /**
         * Apply the given appearance on the given line.
         * 
         * @param line The line to apply the appearance on.
         * @param state The state to derive appearance from.
         */
        protected void setAppearance(Group line, SelectionState state)
        {
            assert line.numChildren() == 1;

            Enumeration<?> children = line.getAllChildren();

            Shape3D shape = (Shape3D) children.nextElement();
            ModelSegment seg = (ModelSegment) line.getUserData();
            Appearance app = shape.getAppearance();
            Direction dir = seg.getDirection();
            int age = step.getId() - seg.getOriginatingStepId();

            switch (state) {
                case NORMAL:
                    app.getColoringAttributes().setColor(getColorManager().getLine3f());
                    app.getTransparencyAttributes().setTransparency(0f);
                    app.getRenderingAttributes().setVisible(seg.getOriginatingStepId() != step.getId());
                    app.getRenderingAttributes().setDepthTestFunction(RenderingAttributes.LESS_OR_EQUAL);
                    app.getLineAttributes().setLineWidth(getLineWidth(dir, age) * (float) getZoom() / 100f);

                    if (line.getParent() == overModel) {
                        overModel.removeChild(line);
                        ((BranchGroup) line).detach();
                        lines.addChild(line);
                    }
                    break;
                case HIGHLIGHTED:
                    app.getColoringAttributes().setColor(getColorManager().getHighlightedLine3f());
                    app.getTransparencyAttributes().setTransparency(0f);
                    app.getRenderingAttributes().setVisible(true);
                    app.getRenderingAttributes().setDepthTestFunction(RenderingAttributes.ALWAYS);
                    app.getLineAttributes().setLineWidth(2.5f * getLineWidth(dir, age) * (float) getZoom() / 100f);

                    if (line.getParent() == lines) {
                        lines.removeChild(line);
                        ((BranchGroup) line).detach();
                        overModel.addChild(line);
                    }
                    break;
                case SELECTED:
                    app.getColoringAttributes().setColor(getColorManager().getSelectedLine3f());
                    app.getTransparencyAttributes().setTransparency(0.3f);
                    app.getRenderingAttributes().setVisible(true);
                    app.getRenderingAttributes().setDepthTestFunction(RenderingAttributes.ALWAYS);
                    app.getLineAttributes().setLineWidth(2f * getLineWidth(dir, age) * (float) getZoom() / 100f);

                    if (line.getParent() == lines) {
                        lines.removeChild(line);
                        ((BranchGroup) line).detach();
                        overModel.addChild(line);
                    }
                    break;
                case SELECTED_HIGHLIGHTED:
                    app.getColoringAttributes().setColor(getColorManager().getSelectedHighlightedLine3f());
                    app.getTransparencyAttributes().setTransparency(0f);
                    app.getRenderingAttributes().setVisible(true);
                    app.getRenderingAttributes().setDepthTestFunction(RenderingAttributes.ALWAYS);
                    app.getLineAttributes().setLineWidth(2f * getLineWidth(dir, age) * (float) getZoom() / 100f);

                    if (line.getParent() == lines) {
                        lines.removeChild(line);
                        ((BranchGroup) line).detach();
                        overModel.addChild(line);
                    }
                    break;
            }
        }
    }

    /**
     * Manager of all colors used in this {@link StepRenderer}.
     * 
     * @author Martin Pecka
     */
    protected class ColorManager extends StepRenderer.ColorManager
    {
        /** Highlighted layer color for foreground/background. */
        protected Color highlightBg              = new Color(150, 150, 255), highlightFg = new Color(150, 150, 255);
        /** Selected layer color for foreground/background. */
        protected Color selectedBg               = new Color(100, 100, 200), selectedFg = new Color(100, 100, 200);
        /** Highlighted selected layer color for foreground/background. */
        protected Color selectedHighlightBg      = new Color(125, 125, 225), selectedHighlightFg = new Color(125, 125,
                                                         225);
        /** Color of a highlighted fold line. */
        protected Color highlightedLine          = new Color(75, 75, 150);
        /** Color of a selected fold line. */
        protected Color selectedLine             = new Color(25, 25, 100);
        /** Color of a selected highlighted fold line. */
        protected Color selectedHighlightedLine  = new Color(50, 50, 125);
        /** Color of a highlighted point. */
        protected Color highlightedPoint         = new Color(0, 0, 75);
        /** Color of a selected point. */
        protected Color selectedPoint            = new Color(50, 125, 50);
        /** Color of a selected highlighted point. */
        protected Color selectedHighlightedPoint = new Color(100, 200, 100);

        /**
         * @param background Paper background color.
         * @param foreground Paper foreground color.
         */
        public ColorManager(Color background, Color foreground)
        {
            super(background, foreground);
            ensureColorsAreContrasting();
        }

        /**
         * Call this method to make sure
         */
        public void ensureColorsAreContrasting()
        {
            // TODO May implement a cleverer algorithm, or just allow to set these colors from diagram.
            // This algorithm doesn't check if the highlight and selectedHighlight colors aren't the same, which would
            // be a problem for program usability.

            highlightBg = getContrastingColor(background, highlightBg);
            highlightFg = getContrastingColor(foreground, highlightFg);

            selectedHighlightBg = getContrastingColor(selectedBg, selectedHighlightBg);
            selectedHighlightFg = getContrastingColor(selectedFg, selectedHighlightFg);
        }

        /**
         * Get the well contrasting color for the specified reference color.
         * 
         * @param refColor The reference color that can be used for contrast computations etc.
         * @param color The color we want to be contrasting with refColor.
         * 
         * @return If color has enough contrast, return it, otherwise return a more contrasting color (if color is
         *         brighter than refColor, than an even brighter color will be returned, and conversely).
         */
        protected Color getContrastingColor(Color refColor, Color color)
        {
            Color result = color;

            // if the color difference is sufficient, we can return
            final int colDiff = abs(result.getRed() - refColor.getRed()) + abs(result.getGreen() - refColor.getGreen())
                    + abs(result.getBlue() - refColor.getBlue());
            // WCAG suggests 500, but we don't need such a large contrast here
            if (colDiff > 180)
                return result;

            // if the contrast with the reference color would be too low, change the resulting color to be more
            // contrasting
            float[] hsbRef = new float[3];
            float[] hsbRes = new float[3];

            Color.RGBtoHSB(refColor.getRed(), refColor.getGreen(), refColor.getBlue(), hsbRef);
            Color.RGBtoHSB(result.getRed(), result.getGreen(), result.getBlue(), hsbRes);

            final float threshold = 0.2f;
            final float diff = hsbRef[2] - hsbRes[2];
            if (diff > -threshold && diff <= 0f) {
                float bright;
                if (hsbRef[2] + threshold <= 1f)
                    bright = hsbRef[2] + threshold;
                else
                    bright = hsbRef[2] - threshold;
                result = Color.getHSBColor(hsbRes[0], hsbRes[1], bright);
            } else if (diff < threshold && diff >= 0f) {
                float bright;
                if (hsbRef[2] > threshold)
                    bright = hsbRef[2] - threshold;
                else
                    bright = hsbRef[2] + threshold;
                result = Color.getHSBColor(hsbRes[0], hsbRes[1], bright);
            }

            return result;
        }

        /**
         * @return Highlighted layer background color.
         */
        public Color getHighlightBg()
        {
            return highlightBg;
        }

        /**
         * @return Highlighted layer background color.
         */
        public Color3f getHighlightBg3f()
        {
            return new Color3f(highlightBg);
        }

        /**
         * @param highlightBg Highlighted layer background color.
         */
        public void setHighlightBg(Color highlightBg)
        {
            this.highlightBg = highlightBg;
        }

        /**
         * @return Highlighted layer foreground color.
         */
        public Color getHighlightFg()
        {
            return highlightFg;
        }

        /**
         * @return Highlighted layer foreground color.
         */
        public Color3f getHighlightFg3f()
        {
            return new Color3f(highlightFg);
        }

        /**
         * @param highlightFg Highlighted layer foreground color.
         */
        public void setHighlightFg(Color highlightFg)
        {
            this.highlightFg = highlightFg;
        }

        /**
         * @return Selected layer background color.
         */
        public Color getSelectedBg()
        {
            return selectedBg;
        }

        /**
         * @return Selected layer background color.
         */
        public Color3f getSelectedBg3f()
        {
            return new Color3f(selectedBg);
        }

        /**
         * @param selectedBg Selected layer background color.
         */
        public void setSelectedBg(Color selectedBg)
        {
            this.selectedBg = selectedBg;
        }

        /**
         * @return Selected layer foreground color.
         */
        public Color getSelectedFg()
        {
            return selectedFg;
        }

        /**
         * @return Selected layer foreground color.
         */
        public Color3f getSelectedFg3f()
        {
            return new Color3f(selectedFg);
        }

        /**
         * @param selectedFg Selected layer foreground color.
         */
        public void setSelectedFg(Color selectedFg)
        {
            this.selectedFg = selectedFg;
        }

        /**
         * @return Highlighted selected layer background color.
         */
        public Color getSelectedHighlightBg()
        {
            return selectedHighlightBg;
        }

        /**
         * @return Highlighted selected layer background color.
         */
        public Color3f getSelectedHighlightBg3f()
        {
            return new Color3f(selectedHighlightBg);
        }

        /**
         * @param selectedHighlightBg Highlighted selected layer background color.
         */
        public void setSelectedHighlightBg(Color selectedHighlightBg)
        {
            this.selectedHighlightBg = selectedHighlightBg;
        }

        /**
         * @return Highlighted selected layer foreground color.
         */
        public Color getSelectedHighlightFg()
        {
            return selectedHighlightFg;
        }

        /**
         * @return Highlighted selected layer foreground color.
         */
        public Color3f getSelectedHighlightFg3f()
        {
            return new Color3f(selectedHighlightFg);
        }

        /**
         * @param selectedHighlightFg Highlighted selected layer foreground color.
         */
        public void setSelectedHighlightFg(Color selectedHighlightFg)
        {
            this.selectedHighlightFg = selectedHighlightFg;
        }

        /**
         * Set both background/foreground colors for highlighted layer.
         * 
         * @param highlight Highlighted layer both colors.
         */
        public void setHighlight(Color highlight)
        {
            this.highlightFg = highlight;
            this.highlightBg = highlight;
        }

        /**
         * Set both background/foreground colors for selected layer.
         * 
         * @param selected Selected layer both colors.
         */
        public void setSelected(Color selected)
        {
            this.selectedFg = selected;
            this.selectedBg = selected;
        }

        /**
         * Set both background/foreground colors for highlighted selected layer.
         * 
         * @param selectedHighlight Highlighted selected layer both colors.
         */
        public void setSelectedHighlight(Color selectedHighlight)
        {
            this.selectedHighlightFg = selectedHighlight;
            this.selectedHighlightBg = selectedHighlight;
        }

        /**
         * @return Color of a highlighted fold line.
         */
        public Color getHighlightedLine()
        {
            return highlightedLine;
        }

        /**
         * @return Color of a highlighted fold line.
         */
        public Color3f getHighlightedLine3f()
        {
            return new Color3f(highlightedLine);
        }

        /**
         * @param highlightedLine Color of a highlighted fold line.
         */
        public void setHighlightedLine(Color highlightedLine)
        {
            this.highlightedLine = highlightedLine;
        }

        /**
         * @return Color of a selected fold line.
         */
        public Color getSelectedLine()
        {
            return selectedLine;
        }

        /**
         * @return Color of a selected fold line.
         */
        public Color3f getSelectedLine3f()
        {
            return new Color3f(selectedLine);
        }

        /**
         * @param selectedLine Color of a selected fold line.
         */
        public void setSelectedLine(Color selectedLine)
        {
            this.selectedLine = selectedLine;
        }

        /**
         * @return Color of a selected highlighted fold line.
         */
        public Color getSelectedHighlightedLine()
        {
            return selectedHighlightedLine;
        }

        /**
         * @return Color of a selected highlighted fold line.
         */
        public Color3f getSelectedHighlightedLine3f()
        {
            return new Color3f(selectedHighlightedLine);
        }

        /**
         * @param selectedHighlightedLine Color of a selected highlighted fold line.
         */
        public void setSelectedHighlightedLine(Color selectedHighlightedLine)
        {
            this.selectedHighlightedLine = selectedHighlightedLine;
        }

        /**
         * @return Color of a highlighted point.
         */
        public Color getHighlightedPoint()
        {
            return highlightedPoint;
        }

        /**
         * @return Color of a highlighted point.
         */
        public Color3f getHighlightedPoint3f()
        {
            return new Color3f(highlightedPoint);
        }

        /**
         * @param highlightedPoint Color of a highlighted point.
         */
        public void setHighlightedPoint(Color highlightedPoint)
        {
            this.highlightedPoint = highlightedPoint;
        }

        /**
         * @return Color of a selected point.
         */
        public Color getSelectedPoint()
        {
            return selectedPoint;
        }

        /**
         * @return Color of a selected point.
         */
        public Color3f getSelectedPoint3f()
        {
            return new Color3f(selectedPoint);
        }

        /**
         * @param selectedPoint Color of a selected point.
         */
        public void setSelectedPoint(Color selectedPoint)
        {
            this.selectedPoint = selectedPoint;
        }

        /**
         * @return Color of a selected highlighted point.
         */
        public Color getSelectedHighlightedPoint()
        {
            return selectedHighlightedPoint;
        }

        /**
         * @return Color of a selected highlighted point.
         */
        public Color3f getSelectedHighlightedPoint3f()
        {
            return new Color3f(selectedHighlightedPoint);
        }

        /**
         * @param selectedHighlightedPoint Color of a selected highlighted point.
         */
        public void setSelectedHighlightedPoint(Color selectedHighlightedPoint)
        {
            this.selectedHighlightedPoint = selectedHighlightedPoint;
        }
    }
}
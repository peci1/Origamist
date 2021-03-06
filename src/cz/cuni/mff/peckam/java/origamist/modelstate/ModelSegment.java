/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.modelstate;

import javax.vecmath.Point2d;
import javax.vecmath.Point3d;
import javax.vecmath.Vector2d;
import javax.vecmath.Vector3d;

import cz.cuni.mff.peckam.java.origamist.math.IntersectionWithTriangle;
import cz.cuni.mff.peckam.java.origamist.math.Segment2d;
import cz.cuni.mff.peckam.java.origamist.math.Segment3d;

/**
 * A segment in the model containing additional model information.
 * 
 * @author Martin Pecka
 */
public class ModelSegment extends Segment3d
{
    /** */
    private static final long serialVersionUID = 5783162571585033772L;

    /** The image of this segment on the paper in 2D. */
    protected Segment2d       original;

    /**
     * The direction of the segment. MOUNTAIN/VALLEY is related to the front side of the paper, <code>null</code> means
     * that this is an older segment that doesn't need to signalize if it is MOUNTAIN or VALLEY.
     */
    protected Direction       direction;

    /** Id of the step "this segment was last touched". */
    protected int             originatingStepId;

    /**
     * @param segment The segment in 3D.
     * @param original The image of this segment on the paper in 2D.
     * @param direction The direction of the segment. MOUNTAIN/VALLEY is related to the front side of the paper,
     *            <code>null</code> means that this is an older segment that doesn't need to signalize if it is MOUNTAIN
     *            or VALLEY.
     * @param originatingStepId Id of the step "this segment was last touched".
     */
    public ModelSegment(Segment3d segment, Segment2d original, Direction direction, int originatingStepId)
    {
        super(segment.getP1(), segment.getP2());
        this.original = new Segment2d(original);
        this.direction = direction;
        this.originatingStepId = originatingStepId;
    }

    /**
     * Create a model segment with <code>null</code> direction and originatingStepId 0.
     * 
     * @param segment The segment in 3D.
     * @param original The image of this segment on the paper in 2D.
     */
    public ModelSegment(Segment3d segment, Segment2d original)
    {
        this(segment, original, null, 0);
    }

    /**
     * @param line The foldLine to fetch data from.
     */
    public ModelSegment(FoldLine line)
    {
        this(line.getSegment3d(), line.getSegment2d(), line.getDirection(), line.getFold().getOriginatingStepId());
    }

    /**
     * Create a model segment with <code>null</code> direction and originatingStepId 0.
     * 
     * @param p1 The start point.
     * @param p2 The end point.
     */
    public ModelSegment(ModelPoint p1, ModelPoint p2)
    {
        this(p1, p2, null, 0);
    }

    /**
     * @param p1 The start point.
     * @param p2 The end point.
     * @param direction The direction of the segment. MOUNTAIN/VALLEY is related to the front side of the paper,
     *            <code>null</code> means that this is an older segment that doesn't need to signalize if it is MOUNTAIN
     *            or VALLEY.
     * @param originatingStepId Id of the step "this segment was last touched".
     */
    public ModelSegment(ModelPoint p1, ModelPoint p2, Direction direction, int originatingStepId)
    {
        this(new Segment3d(p1, p2), new Segment2d(p1.getOriginal(), p2.getOriginal()), direction, originatingStepId);
    }

    /**
     * @param inter Intersection with model triangle to create this segment from.
     * @param direction The direction of the segment. MOUNTAIN/VALLEY is related to the front side of the paper,
     *            <code>null</code> means that this is an older segment that doesn't need to signalize if it is MOUNTAIN
     *            or VALLEY.
     * @param originatingStepId Id of the step "this segment was last touched".
     */
    public ModelSegment(IntersectionWithTriangle<ModelTriangle> inter, Direction direction, int originatingStepId)
    {
        this(inter, new Segment2d(((ModelPoint) inter.getPoint(0)).getOriginal(),
                ((ModelPoint) inter.getPoint(1)).getOriginal()), direction, originatingStepId);
    }

    @Override
    public boolean isSinglePoint()
    {
        return super.isSinglePoint() && original.isSinglePoint();
    }

    @Override
    protected boolean isNonTrivial()
    {
        return !super.isSinglePoint() && !original.isSinglePoint();
    }

    /**
     * @return The image of this segment on the paper in 2D.
     */
    public Segment2d getOriginal()
    {
        return original;
    }

    /**
     * @param original The image of this segment on the paper in 2D.
     */
    public void setOriginal(Segment2d original)
    {
        this.original = original;
    }

    /**
     * @return The direction of the segment. MOUNTAIN/VALLEY is related to the front side of the paper,
     *         <code>null</code> means that this is an older segment that doesn't need to signalize if it is MOUNTAIN
     *         or VALLEY.
     */
    public Direction getDirection()
    {
        return direction;
    }

    /**
     * @param direction The direction of the segment. MOUNTAIN/VALLEY is related to the front side of the paper,
     *            <code>null</code> means that this is an older segment that doesn't need to signalize if it is MOUNTAIN
     *            or VALLEY.
     */
    public void setDirection(Direction direction)
    {
        this.direction = direction;
    }

    /**
     * @return Id of the step "this segment was last touched".
     */
    public int getOriginatingStepId()
    {
        return originatingStepId;
    }

    /**
     * @param originatingStepId Id of the step "this segment was last touched".
     */
    public void setOriginatingStepId(int originatingStepId)
    {
        this.originatingStepId = originatingStepId;
    }

    /**
     * If the segments are parallel and overlap, return true and change this segment to the union of the two segments.
     * Otherwise return false and leave this segment unchanged.
     * 
     * @param other The segment to merge with.
     * @return True if the segments were merged.
     */
    public boolean merge(ModelSegment other)
    {
        if (this.original.contains(other.original.getP1()) && this.original.contains(other.original.getP2()))
            // other is either equal or is a subsegment
            return true;
        if (!this.original.isParallelTo(other.original) || !this.isParallelTo(other))
            return false;
        if (!this.original.overlaps(other.original) || !this.overlaps(other))
            return false;
        if ((this.original.containsAll(other.original.getPoints()) != this.containsAll(other.getPoints()))
                || (other.original.containsAll(this.original.getPoints()) != other.containsAll(this.getPoints())))
            return false;

        Segment3d trial;
        if ((trial = new Segment3d(this.getP1(), other.getP1())) != null && trial.contains(this.getP2())
                && trial.contains(other.getP2())) {
            this.p2 = other.getP1();
            this.original = new Segment2d(this.original.getP1(), other.original.getP1());
        } else if ((trial = new Segment3d(this.getP1(), other.getP2())) != null && trial.contains(this.getP2())
                && trial.contains(other.getP1())) {
            this.p2 = other.getP2();
            this.original = new Segment2d(this.original.getP1(), other.original.getP2());
        } else if ((trial = new Segment3d(this.getP2(), other.getP1())) != null && trial.contains(this.getP1())
                && trial.contains(other.getP2())) {
            this.p = other.getP1();
            this.original = new Segment2d(other.original.getP1(), this.original.getP2());
        } else if ((trial = new Segment3d(this.getP2(), other.getP2())) != null && trial.contains(this.getP1())
                && trial.contains(other.getP1())) {
            this.p = other.getP2();
            this.original = new Segment2d(other.original.getP2(), this.original.getP2());
        } else { // other contains this
            this.p = other.getP1();
            this.p2 = other.getP2();
            this.original = new Segment2d(other.original);
        }
        this.v = new Vector3d(this.p2);
        this.v.sub(this.p);

        return true;
    }

    /**
     * If this segment contains the given point (and it isn't its border point), split it at that point, set this
     * segment to be one part and return the other part.
     * 
     * @param point The split point.
     * @return The second segment, or <code>null</code> if this segment couldn't be split at the given point.
     */
    public ModelSegment split(Point3d point)
    {
        if (!contains(point) || isBorderPoint(point))
            return null;

        double param = getParameterForPoint(point);
        Point2d point2 = new Point2d(this.original.getP1());
        Vector2d vec2 = new Vector2d(this.original.getVector());
        vec2.scale(param);
        point2.add(vec2);

        ModelSegment result = new ModelSegment(new Segment3d(point, this.getP2()), new Segment2d(point2,
                this.original.getP2()), direction, originatingStepId);

        this.p2 = point;
        this.v = new Vector3d(this.p2);
        this.v.sub(this.p);

        this.original = new Segment2d(this.original.getP1(), point2);

        return result;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((direction == null) ? 0 : direction.hashCode());
        result = prime * result + ((original == null) ? 0 : original.hashCode());
        result = prime * result + originatingStepId;
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        ModelSegment other = (ModelSegment) obj;
        if (direction != other.direction)
            return false;
        if (original == null) {
            if (other.original != null)
                return false;
        } else if (!original.equals(other.original))
            return false;
        if (originatingStepId != other.originatingStepId)
            return false;
        return true;
    }

    /**
     * @param other The line to compare.
     * @param allowInverseDirection If true, even two segments with inverted direction will be considered equal.
     * @return If the segments are epsilon-equal in both 3D and 2D.
     */
    public boolean epsilonEquals(ModelSegment other, boolean allowInverseDirection)
    {
        return this == other
                || (super.epsilonEquals(other, allowInverseDirection) && other.getOriginal().epsilonEquals(original,
                        allowInverseDirection));
    }

    @Override
    public ModelPoint getPointForParameter(double param)
    {
        return new ModelPoint(super.getPointForParameter(param), original.getPointForParameter(param));
    }

    @Override
    public ModelSegment clone()
    {
        return new ModelSegment(super.clone(), original.clone(), direction, originatingStepId);
    }

    @Override
    public String toString()
    {
        return "ModelSegment [segment=" + super.toString() + ", original=" + original + ", direction=" + direction
                + ", originatingStepId=" + originatingStepId + "]";
    }

}

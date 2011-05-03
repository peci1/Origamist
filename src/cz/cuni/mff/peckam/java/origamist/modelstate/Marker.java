/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.modelstate;

import javax.vecmath.Point2d;
import javax.vecmath.Point3d;

/**
 * A text marker which refers to a point on paper.
 * 
 * @author Martin Pecka
 */
public class Marker implements Cloneable
{
    /** The data of this marker needed for rendering. */
    protected MarkerRenderData renderData;

    /** The point on paper this marker refers to. */
    protected Point2d          point2d;

    /** Number of steps this marker should be displayed. */
    protected int              stepsToHide;

    /**
     * @param text The text to display.
     * @param point3d The point to refer to.
     * @param point2d The point on paper this marker refers to.
     * @param stepsToHide Number of steps this marker should be displayed.
     */
    public Marker(String text, Point3d point3d, Point2d point2d, int stepsToHide)
    {
        this.renderData = new MarkerRenderData(text, point3d);
        this.point2d = point2d;
        this.stepsToHide = stepsToHide;
    }

    /**
     * @return The point on paper this marker refers to.
     */
    public Point2d getPoint2d()
    {
        return point2d;
    }

    /**
     * @param point2d The point on paper this marker refers to.
     */
    public void setPoint2d(Point2d point2d)
    {
        this.point2d = point2d;
    }

    /**
     * @return Number of steps this marker should be displayed.
     */
    public int getStepsToHide()
    {
        return stepsToHide;
    }

    /**
     * @param stepsToHide Number of steps this marker should be displayed.
     */
    public void setStepsToHide(int stepsToHide)
    {
        this.stepsToHide = stepsToHide;
    }

    /**
     * @return The text to display.
     */
    public String getText()
    {
        return renderData.getText();
    }

    /**
     * @return The point to refer to.
     */
    public Point3d getPoint3d()
    {
        return renderData.getPoint3d();
    }

    /**
     * @param text The text to display.
     */
    void setText(String text)
    {
        renderData.text = text;
    }

    /**
     * @param point3d The point to refer to.
     */
    void setPoint3d(Point3d point3d)
    {
        renderData.point3d = point3d;
    }

    /**
     * @return The data of this marker needed for rendering.
     */
    MarkerRenderData getRenderData()
    {
        return renderData;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((point2d == null) ? 0 : point2d.hashCode());
        result = prime * result + ((renderData == null) ? 0 : renderData.hashCode());
        result = prime * result + stepsToHide;
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
        Marker other = (Marker) obj;
        if (point2d == null) {
            if (other.point2d != null)
                return false;
        } else if (!point2d.equals(other.point2d))
            return false;
        if (renderData == null) {
            if (other.renderData != null)
                return false;
        } else if (!renderData.equals(other.renderData))
            return false;
        if (stepsToHide != other.stepsToHide)
            return false;
        return true;
    }

    @Override
    public String toString()
    {
        return "Marker [text=" + getText() + ", point3d=" + getPoint3d() + ", point2d=" + point2d + ", stepsToHide="
                + stepsToHide + "]";
    }

    @Override
    protected Marker clone()
    {
        try {
            Marker clone = (Marker) super.clone();
            clone.renderData = renderData.clone();
            clone.point2d = (Point2d) point2d.clone();
            return clone;
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }
}
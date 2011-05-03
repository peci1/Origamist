/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.modelstate;

import javax.vecmath.Point3d;

/**
 * A text marker which refers to a point on paper.
 * 
 * This class holds just the information used by Java3D, for a full infoset refer to the corresponding {@link Marker}.
 * 
 * @author Martin Pecka
 */
public class MarkerRenderData implements Cloneable
{
    /** The text to display. */
    protected String  text;
    /** The point to refer to. */
    protected Point3d point3d;

    /**
     * @param text The text to display.
     * @param point3d The point to refer to.
     */
    MarkerRenderData(String text, Point3d point3d)
    {
        this.text = text;
        this.point3d = point3d;
    }

    /**
     * @return The text to display.
     */
    public String getText()
    {
        return text;
    }

    /**
     * @param text The text to display.
     */
    void setText(String text)
    {
        this.text = text;
    }

    /**
     * @return The point to refer to.
     */
    public Point3d getPoint3d()
    {
        return point3d;
    }

    /**
     * @param point3d The point to refer to.
     */
    void setPoint3d(Point3d point3d)
    {
        this.point3d = point3d;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((point3d == null) ? 0 : point3d.hashCode());
        result = prime * result + ((text == null) ? 0 : text.hashCode());
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
        MarkerRenderData other = (MarkerRenderData) obj;
        if (point3d == null) {
            if (other.point3d != null)
                return false;
        } else if (!point3d.equals(other.point3d))
            return false;
        if (text == null) {
            if (other.text != null)
                return false;
        } else if (!text.equals(other.text))
            return false;
        return true;
    }

    @Override
    protected MarkerRenderData clone()
    {
        try {
            MarkerRenderData clone = (MarkerRenderData) super.clone();
            clone.point3d = (Point3d) point3d.clone();
            return clone;
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }
}

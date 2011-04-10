/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.model;

/**
 * A line or segment in 2D space.
 * 
 * @author Martin Pecka
 */
public class Line2D extends cz.cuni.mff.peckam.java.origamist.model.jaxb.Line2D
{
    @Override
    public String toString()
    {
        return "Line2D [start=" + start + ", end=" + end + "]";
    }
}

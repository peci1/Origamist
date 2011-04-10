/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.model;

/**
 * A point in 2D space.
 * 
 * @author Martin Pecka
 */
public class Point2D extends cz.cuni.mff.peckam.java.origamist.model.jaxb.Point2D
{
    @Override
    public String toString()
    {
        return "[" + x + ", " + y + "]";
    }
}

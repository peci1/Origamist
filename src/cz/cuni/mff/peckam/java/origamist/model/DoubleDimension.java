/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.model;

/**
 * A decimal dimension object.
 * 
 * @author Martin Pecka
 */
public class DoubleDimension extends cz.cuni.mff.peckam.java.origamist.model.jaxb.Dimension
{
    public DoubleDimension()
    {
        super();
    }

    public DoubleDimension(double width, double height)
    {
        this.width = width;
        this.height = height;
    }
}

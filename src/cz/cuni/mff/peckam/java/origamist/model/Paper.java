/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.model;

/**
 * A basic class for paper.
 * 
 * @author Martin Pecka
 */
public class Paper extends cz.cuni.mff.peckam.java.origamist.model.jaxb.Paper
{

    /**
     * Return the relative dimensions of the paper (so that one of them will be 1.0 and the second one will be <= 1.0).
     * 
     * @return The relative dimensions of the paper.
     */
    public DoubleDimension getRelativeDimensions()
    {
        if (this.size.getWidth() == this.size.getHeight()) {
            return new DoubleDimension(1.0, size.getHeight() / size.getWidth());
        } else {
            return new DoubleDimension(size.getWidth() / size.getHeight(), 1.0);
        }
    }

}

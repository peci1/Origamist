/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.services.interfaces;

import javax.swing.ImageIcon;

/**
 * This class should aid in creating hashCode() and equals() methods.
 * 
 * @author Martin Pecka
 */
public interface HashCodeAndEqualsHelper
{
    /**
     * Computes hashcode of the ImageIcon based on its pixels.
     * 
     * @param icon The icon to compute the hashCode of.
     * @return The hash code.
     */
    public int hashCode(ImageIcon icon);

    /**
     * Tells if the given image icons are the same based on their pixels.
     * 
     * This method scales the images down before comparison!
     * 
     * @param icon1 The icon to compare.
     * @param icon2 The other icon to compare;
     * @return If the icons represent the same images.
     */
    public boolean equals(ImageIcon icon1, ImageIcon icon2);
}

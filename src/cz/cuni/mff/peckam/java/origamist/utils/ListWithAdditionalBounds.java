/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.utils;

import java.util.List;

/**
 * A list with an additional type bound on its elements, so that it can be viewed as a list of elements of another type.
 * 
 * @author Martin Pecka
 */
public interface ListWithAdditionalBounds<E, I> extends List<E>
{
    /**
     * Return the view of this list with elements converted to the second type parameter.
     * 
     * @return The view of this list with elements converted to the second type parameter.
     */
    List<I> getAltView();
}

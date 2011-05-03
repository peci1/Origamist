/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.math;

import java.io.Serializable;

/**
 * A n-dimensional vector.
 * 
 * @author Martin Pecka
 */
public interface Vector<T> extends Serializable, Iterable<T>
{

    /**
     * @return The dimension of the vector.
     */
    int getDimension();

    /**
     * Return the value at the given dimension.
     * 
     * @param index The index of the value to get (dimensions are indexed from 0).
     * @return The value at the given dimension.
     */
    T get(int index);

    /**
     * Set the value at the given dimension to the given value.
     * 
     * @param index The index of the value to set (dimensions are indexed from 0).
     * @param value The new value.
     * @return The old value at that dimension (<code>null</code> if no previous value was set).
     */
    T set(int index, T value);

}

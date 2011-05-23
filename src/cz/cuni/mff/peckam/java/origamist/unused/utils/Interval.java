/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.unused.utils;

/**
 * An interval.
 * 
 * @param T The type of values this interval spans between.
 * 
 * @author Martin Pecka
 */
public interface Interval<T extends Comparable<? super T>>
{
    /**
     * @return The lower bound of the interval.
     */
    T getMin();

    /**
     * @return The upper bound of the interval.
     */
    T getMax();

    /**
     * Returns <code>true</code> if the intersection of this interval and the other is nonempty.
     * 
     * @param other The interval to test overlapping with.
     * @return <code>true</code> if the intersection of this interval and the other is nonempty.
     */
    boolean overlapsWith(Interval<T> other);
}

/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.utils;

import java.util.Comparator;

/**
 * A comparator that treats epsilon-distant items as equal.
 * 
 * @author Martin Pecka
 */
public interface EpsilonComparator<T, E> extends Comparator<T>
{
    /**
     * Compares its first two arguments for order. Returns a negative integer, zero, or a positive integer as the first
     * argument is less than, equal to, or greater than the second.
     * <p>
     * 
     * In the foregoing description, the notation <tt>sgn(</tt><i>expression</i><tt>)</tt> designates the mathematical
     * <i>signum</i> function, which is defined to return one of <tt>-1</tt>, <tt>0</tt>, or <tt>1</tt> according to
     * whether the value of <i>expression</i> is negative, zero or positive.
     * <p>
     * 
     * The implementor must ensure that <tt>sgn(compare(x, y, e)) == -sgn(compare(y, x, e))</tt> for all <tt>x</tt>,
     * <tt>y</tt> and <tt>e</tt>. (This implies that <tt>compare(x, y, e)</tt> must throw an exception if and only if
     * <tt>compare(y, x, e)</tt> throws an exception.)
     * <p>
     * 
     * The relation defined by this comparator doesn't have to be transitive.
     * 
     * It is generally the case, but <i>not</i> strictly required that
     * <tt>(compare(x, y, e)==0) == (x.epsilonEquals(y, e))</tt>. Generally speaking, any epsilon-comparator that
     * violates this condition should clearly indicate this fact. The recommended language is
     * "Note: this comparator imposes orderings that are inconsistent with epsilonEquals."
     * 
     * @param o1 The first object to be compared.
     * @param o2 The second object to be compared.
     * @param epsilon The maximum "distance" of two equal points.
     * 
     * @return a negative integer, zero, or a positive integer as the
     *         first argument is less than, equal to, or greater than the
     *         second (treating epsilon-distant objects as equal).
     * 
     * @throws ClassCastException if the arguments' types prevent them from
     *             being compared by this comparator.
     */
    int compare(T o1, T o2, E epsilon);
}

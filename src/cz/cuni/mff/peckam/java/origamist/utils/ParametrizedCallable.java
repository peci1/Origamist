/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.utils;

/**
 * A callable task that returns value of type O and requires an argument of type I to run.
 * 
 * @author Martin Pecka
 */
public interface ParametrizedCallable<O, I>
{
    /**
     * Do the work.
     * 
     * @param arg An argument that can be used.
     * @return The result of the computation.
     */
    O call(I arg);
}

/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.model;

import cz.cuni.mff.peckam.java.origamist.math.Line2d;

/**
 * An operation for which a symmetric operation can be determined.
 * 
 * @author Martin Pecka
 */
public interface HasSymmetricOperation
{
    /**
     * Return an operation that is symmetric to this one.
     * 
     * @param symmetryAxis The axis of symmetry.
     * @return The symmetric operation.
     */
    Operation getSymmetricOperation(Line2d symmetryAxis);
}

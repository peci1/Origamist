/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.model;

import java.util.List;

/**
 * An object containing other operations.
 * 
 * @author Martin Pecka
 */
public interface OperationContainer
{
    /**
     * @return The operations this container contains.
     */
    List<Operation> getOperations();

    /**
     * @return True if the enclosed operations should be visible even in the step where they have originated.
     */
    boolean areContentsVisible();
}

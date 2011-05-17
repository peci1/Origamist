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
}

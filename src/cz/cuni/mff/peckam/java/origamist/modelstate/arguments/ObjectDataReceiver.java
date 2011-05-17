/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.modelstate.arguments;

/**
 * An operation argument that reads data from a step editor.
 * 
 * @author Martin Pecka
 */
public interface ObjectDataReceiver<T>
{
    /**
     * Read the argument's data from the given object.
     * 
     * @param editor The object that is the source of information.
     */
    void readDataFromObject(T source);
}

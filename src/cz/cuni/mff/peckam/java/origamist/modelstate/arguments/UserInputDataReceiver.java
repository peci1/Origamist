/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.modelstate.arguments;

/**
 * An operation argument demanding some user input.
 * 
 * @author Martin Pecka
 */
public interface UserInputDataReceiver
{
    /**
     * Fetch the argument's data somehow (eg. by displaying a dialog).
     */
    void askForData();
}

/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.modelstate.arguments;

/**
 * An operation argument demanding textual data.
 * 
 * @author Martin Pecka
 */
public interface TextInputDataReceiver
{
    /**
     * Fetch the argument's data somehow (eg. by displaying a dialog).
     */
    void askForData();
}

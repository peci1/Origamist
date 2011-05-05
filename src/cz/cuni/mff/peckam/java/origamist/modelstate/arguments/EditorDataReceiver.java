/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.modelstate.arguments;

import cz.cuni.mff.peckam.java.origamist.gui.editor.StepEditor;

/**
 * An operation argument that reads data from a step editor.
 * 
 * @author Martin Pecka
 */
public interface EditorDataReceiver
{
    /**
     * Read the argument's data from the given step editor.
     * 
     * @param editor The editor that is the source of information.
     */
    void readDataFromEditor(StepEditor editor);
}

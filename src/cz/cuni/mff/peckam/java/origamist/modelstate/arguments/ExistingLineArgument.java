/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.modelstate.arguments;

import cz.cuni.mff.peckam.java.origamist.gui.editor.PickMode;
import cz.cuni.mff.peckam.java.origamist.gui.editor.StepEditor;

/**
 * A line argument that can only be filled with an existing line.
 * 
 * @author Martin Pecka
 */
public class ExistingLineArgument extends LineArgument
{
    /**
     * @param required If true, this argument is required.
     * @param resourceBundleKey The key in "editor" resource bundle describing this operation argument.
     */
    public ExistingLineArgument(boolean required, String resourceBundleKey)
    {
        super(required, resourceBundleKey);
    }

    @Override
    public PickMode preferredPickMode()
    {
        return PickMode.LINE;
    }

    @Override
    public void readDataFromObject(StepEditor editor)
    {
        if (editor.getChosenExistingLine() != null)
            this.line = editor.getChosenExistingLine();
    }
}

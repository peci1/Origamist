/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.modelstate.arguments;

import cz.cuni.mff.peckam.java.origamist.gui.editor.PickMode;
import cz.cuni.mff.peckam.java.origamist.gui.editor.StepEditor;
import cz.cuni.mff.peckam.java.origamist.utils.LocalizedString;

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
        if (line != null)
            support.firePropertyChange(COMPLETE_PROPERTY, false, true);
    }

    @Override
    protected String getUserTipPart()
    {
        return new LocalizedString(OperationArgument.class.getName(), "existing.line.user.tip").toString();
    }

}

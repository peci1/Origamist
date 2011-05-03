/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.modelstate.arguments;

import cz.cuni.mff.peckam.java.origamist.gui.editor.PickMode;

/**
 * A line argument that can only be filled with an existing line.
 * 
 * @author Martin Pecka
 */
public class ExistingLineArgument extends LineArgument
{
    /**
     * @param required If true, this argument is required.
     */
    public ExistingLineArgument(boolean required)
    {
        super(required);
    }

    @Override
    public PickMode preferredPickMode()
    {
        return PickMode.LINE;
    }
}

/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.modelstate.arguments;

import cz.cuni.mff.peckam.java.origamist.gui.editor.PickMode;

/**
 * A text argument.
 * 
 * @author Martin Pecka
 */
public class TextArgument extends OperationArgument
{

    /** The text. */
    protected String text = null;

    /**
     * @param required If true, this argument is required.
     */
    public TextArgument(boolean required)
    {
        super(required);
    }

    @Override
    public boolean isComplete()
    {
        return text != null;
    }

    /**
     * @return The text.
     * 
     * @throws IllegalStateException If {@link #isComplete()} is false.
     */
    public String getText() throws IllegalStateException
    {
        if (!isComplete())
            throw new IllegalStateException("Cannot query properties of a non-completed argument.");

        return text;
    }

    /**
     * @param text The text to set.
     */
    public void setText(String text)
    {
        this.text = text;
    }

    @Override
    public PickMode preferredPickMode()
    {
        return null;
    }
}

/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.modelstate.arguments;

import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import cz.cuni.mff.peckam.java.origamist.gui.editor.PickMode;
import cz.cuni.mff.peckam.java.origamist.services.ServiceLocator;
import cz.cuni.mff.peckam.java.origamist.services.interfaces.ConfigurationManager;

/**
 * A text argument.
 * 
 * @author Martin Pecka
 */
public class TextArgument extends OperationArgument implements TextInputDataReceiver
{

    /** The text. */
    protected String text = null;

    /**
     * @param required If true, this argument is required.
     * @param resourceBundleKey The key in "editor" resource bundle describing this operation argument.
     */
    public TextArgument(boolean required, String resourceBundleKey)
    {
        super(required, resourceBundleKey);
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

    @Override
    public void askForData()
    {
        ResourceBundle messages = ResourceBundle.getBundle("editor", ServiceLocator.get(ConfigurationManager.class)
                .get().getLocale());

        this.text = JOptionPane.showInputDialog(null, messages.getString("operation.argument.text.message"),
                messages.getString("operation.argument.text.title"), JOptionPane.QUESTION_MESSAGE);
    }
}

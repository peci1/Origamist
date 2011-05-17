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
 * Integer argument.
 * 
 * @author Martin Pecka
 */
public class IntegerArgument extends OperationArgument implements UserInputDataReceiver
{
    /** The integer. */
    protected Integer integer = null;

    /**
     * @param required If true, this argument is required.
     * @param resourceBundleKey The key in "editor" resource bundle describing this operation argument.
     */
    public IntegerArgument(boolean required, String resourceBundleKey)
    {
        super(required, resourceBundleKey);
    }

    @Override
    public boolean isComplete()
    {
        return integer != null;
    }

    /**
     * @return The integer.
     * 
     * @throws IllegalStateException If {@link #isComplete()} is false.
     */
    public int getInteger() throws IllegalStateException
    {
        if (!isComplete())
            throw new IllegalStateException("Cannot query properties of a non-completed argument.");

        return integer;
    }

    /**
     * @param integer The integer to set.
     */
    public void setAngle(int integer)
    {
        this.integer = integer;
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
        String input = "";
        while (input != null) {
            input = JOptionPane.showInputDialog(null, messages.getString("operation.argument.integer.message"),
                    messages.getString("operation.argument.integer.title"), JOptionPane.QUESTION_MESSAGE);
            if (input != null) {
                try {
                    integer = Integer.parseInt(input);
                    return;
                } catch (NumberFormatException e) {
                    JOptionPane
                            .showMessageDialog(null,
                                    messages.getString("operation.argument.integer.badnumber.message"),
                                    messages.getString("operation.argument.integer.badnumber.title"),
                                    JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
}

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
 * Angle argument.
 * 
 * @author Martin Pecka
 */
public class AngleArgument extends OperationArgument implements TextInputDataReceiver
{
    /** The angle. */
    protected Double angle = null;

    /**
     * @param required If true, this argument is required.
     * @param resourceBundleKey The key in "editor" resource bundle describing this operation argument.
     */
    public AngleArgument(boolean required, String resourceBundleKey)
    {
        super(required, resourceBundleKey);
    }

    @Override
    public boolean isComplete()
    {
        return angle != null;
    }

    /**
     * @return The angle.
     * 
     * @throws IllegalStateException If {@link #isComplete()} is false.
     */
    public double getAngle() throws IllegalStateException
    {
        if (!isComplete())
            throw new IllegalStateException("Cannot query properties of a non-completed argument.");

        return angle;
    }

    /**
     * @param angle The angle to set.
     */
    public void setAngle(double angle)
    {
        this.angle = angle;
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
            // TODO make a better dialog to display
            input = JOptionPane.showInputDialog(null, messages.getString("operation.argument.angle.message"),
                    messages.getString("operation.argument.angle.title"), JOptionPane.QUESTION_MESSAGE);
            if (input != null) {
                try {
                    angle = Double.parseDouble(input);
                    return;
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null,
                            messages.getString("operation.argument.angle.badnumber.message"),
                            messages.getString("operation.argument.angle.badnumber.title"), JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
}

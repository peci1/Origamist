/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.modelstate.arguments;

import java.util.ResourceBundle;

import javax.swing.origamist.AngleSelectionDialog;

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

        AngleSelectionDialog dialog = new AngleSelectionDialog(messages.getString("operation.argument.angle.message"),
                messages.getString("operation.argument.angle.title"));

        angle = dialog.getAngle();
    }
}

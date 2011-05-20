/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.modelstate.arguments;

import java.util.ResourceBundle;

import javax.swing.origamist.AngleSelectionDialog;

import cz.cuni.mff.peckam.java.origamist.gui.editor.PickMode;
import cz.cuni.mff.peckam.java.origamist.math.AngleUnit;
import cz.cuni.mff.peckam.java.origamist.services.ServiceLocator;
import cz.cuni.mff.peckam.java.origamist.services.interfaces.ConfigurationManager;
import cz.cuni.mff.peckam.java.origamist.utils.LocalizedString;

/**
 * Angle argument.
 * 
 * @author Martin Pecka
 */
public class AngleArgument extends OperationArgument implements UserInputDataReceiver
{
    /** The angle. */
    protected Double    angle = null;

    /** The lower bound on the angle. */
    protected Double    lowerBound;
    /** The upper bound on the angle. */
    protected Double    upperBound;
    /** The unit of the bounds. */
    protected AngleUnit boundsUnit;

    /**
     * @param required If true, this argument is required.
     * @param resourceBundleKey The key in "editor" resource bundle describing this operation argument.
     */
    public AngleArgument(boolean required, String resourceBundleKey)
    {
        this(required, resourceBundleKey, null, null, AngleUnit.RAD);
    }

    /**
     * @param required If true, this argument is required.
     * @param resourceBundleKey The key in "editor" resource bundle describing this operation argument.
     * @param lowerBound The lower bound on the angle.
     * @param upperBound The upper bound on the angle.
     * @param unit The unit of the bounds.
     */
    public AngleArgument(boolean required, String resourceBundleKey, Double lowerBound, Double upperBound,
            AngleUnit unit)
    {
        super(required, resourceBundleKey);
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        this.boundsUnit = unit;
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
    public String getL7dUserTip()
    {
        if (isRequired())
            return new LocalizedString(OperationArgument.class.getName(), "proceed").toString();
        else
            return new LocalizedString(OperationArgument.class.getName(), "optional.dialog").toString();
    }

    @Override
    public void askForData()
    {
        ResourceBundle messages = ResourceBundle.getBundle("editor", ServiceLocator.get(ConfigurationManager.class)
                .get().getLocale());

        AngleSelectionDialog dialog = new AngleSelectionDialog(messages.getString("operation.argument.angle.message"),
                messages.getString("operation.argument.angle.title"));

        if (lowerBound != null || upperBound != null)
            dialog.setBounds(lowerBound, upperBound, boundsUnit);

        angle = dialog.getAngle();

        if (angle != null)
            support.firePropertyChange(COMPLETE_PROPERTY, false, true);
    }
}

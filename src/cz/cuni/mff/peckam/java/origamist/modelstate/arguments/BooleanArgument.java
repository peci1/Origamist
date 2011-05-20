/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.modelstate.arguments;

import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import cz.cuni.mff.peckam.java.origamist.gui.editor.PickMode;
import cz.cuni.mff.peckam.java.origamist.services.ServiceLocator;
import cz.cuni.mff.peckam.java.origamist.services.interfaces.ConfigurationManager;
import cz.cuni.mff.peckam.java.origamist.utils.LocalizedString;

/**
 * Argument asking for a boolean.
 * 
 * @author Martin Pecka
 */
public class BooleanArgument extends OperationArgument implements UserInputDataReceiver
{

    /** The selected value. */
    protected Boolean value = null;

    /** Resource bundle key. */
    protected String  questionBundleKey, trueBundleKey, falseBundleKey;

    /**
     * @param required If true, this argument is required.
     * @param resourceBundleKey The key in "editor" resource bundle describing this operation argument.
     * @param questionBundleKey The key in "editor" resource bundle holding the question to ask.
     * @param trueBundleKey The key in "editor" resource bundle holding the text of the value representing true.
     * @param falseBundleKey The key in "editor" resource bundle holding the text of the value representing false.
     */
    public BooleanArgument(boolean required, String resourceBundleKey, String questionBundleKey, String trueBundleKey,
            String falseBundleKey)
    {
        super(required, resourceBundleKey);
        this.questionBundleKey = questionBundleKey;
        this.trueBundleKey = trueBundleKey;
        this.falseBundleKey = falseBundleKey;
    }

    @Override
    public boolean isComplete()
    {
        return value != null;
    }

    /**
     * @return The selected value.
     */
    public Boolean getValue()
    {
        if (!isComplete())
            throw new IllegalStateException("Cannot query properties of a non-completed argument.");

        return value;
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

        Object[] values = new Object[] { messages.getString(trueBundleKey), messages.getString(falseBundleKey) };

        Object input = new Object();
        while (input != null) {
            input = JOptionPane.showInputDialog(null, messages.getString(questionBundleKey),
                    messages.getString("operation.argument.boolean.title"), JOptionPane.QUESTION_MESSAGE, null, values,
                    values[0]);
            if (input != null) {
                if (input == values[0])
                    value = true;
                else
                    value = false;
                support.firePropertyChange(COMPLETE_PROPERTY, false, true);
                return;
            } else {
                value = null;
            }
        }
    }

}

/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.model;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import cz.cuni.mff.peckam.java.origamist.model.jaxb.Operations;
import cz.cuni.mff.peckam.java.origamist.modelstate.ModelState;
import cz.cuni.mff.peckam.java.origamist.modelstate.arguments.BooleanArgument;
import cz.cuni.mff.peckam.java.origamist.modelstate.arguments.LastOperationsArgument;
import cz.cuni.mff.peckam.java.origamist.modelstate.arguments.OperationArgument;

/**
 * A repeat operation.
 * 
 * @author Martin Pecka
 */
public class RepeatOperation extends cz.cuni.mff.peckam.java.origamist.model.jaxb.RepeatOperation implements
        OperationContainer
{
    @Override
    protected void init()
    {
        super.init();
        type = Operations.REPEAT_ACTION; // it doesn't help to specify the value as fixed in XSD
    }

    @Override
    public ModelState getModelState(ModelState previousState)
    {
        for (Operation o : operations) {
            o.getModelState(previousState);
        }

        return previousState;
    }

    @Override
    protected List<OperationArgument> initArguments()
    {
        List<OperationArgument> arguments = new ArrayList<OperationArgument>(2);

        arguments.add(new LastOperationsArgument(true, "operation.argument.last.operations"));
        arguments.add(new BooleanArgument(true, "operation.argument.hidden", "operation.argument.hidden.message",
                "operation.argument.hidden.displayed", "operation.argument.hidden.hidden"));

        return arguments;
    }

    @Override
    public void fillFromArguments() throws IllegalStateException
    {
        this.operations.clear();
        this.operations.addAll(((LastOperationsArgument) arguments.get(0)).getOperations());
        this.display = ((BooleanArgument) arguments.get(1)).getValue();
    }

    @Override
    public String getL7dUserTip(OperationArgument argument)
    {
        String bundleKey = null;
        if (argument == getArguments().get(0)) {
            bundleKey = "repeat.steps.user.tip";
        } else if (argument == getArguments().get(1)) {
            bundleKey = "repeat.visible.user.tip";
        }

        if (bundleKey != null)
            return messages.getString(bundleKey);
        else
            return null;
    }

    @Override
    public boolean isCompletelyDelayedToNextStep()
    {
        return !isDisplay();
    }

    @Override
    public String getDefaultDescription()
    {
        String prefix = type.toString();
        StringBuilder text = new StringBuilder("<html><body>").append(getL7dName());
        text.append(" (");
        text.append(MessageFormat.format(messages.getString(prefix + ".countFormat"),
                new Object[] { operations.size() }));
        text.append(", ");
        if (display != null && display) {
            text.append(messages.getString(prefix + ".displayed"));
        } else {
            text.append(messages.getString(prefix + ".hidden"));
        }
        text.append(")");

        text.append("</body></html>");
        return text.toString();
    }

    @Override
    public String toString()
    {
        return "RepeatOperation [display=" + display + ", operations=" + operations + "]";
    }
}

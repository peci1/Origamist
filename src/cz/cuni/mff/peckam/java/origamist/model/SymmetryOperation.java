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
import cz.cuni.mff.peckam.java.origamist.modelstate.arguments.LineArgument;
import cz.cuni.mff.peckam.java.origamist.modelstate.arguments.OperationArgument;
import cz.cuni.mff.peckam.java.origamist.modelstate.arguments.OperationsArgument;

/**
 * A repeat operation that is symmetric to some given operations.
 * 
 * @author Martin Pecka
 */
public class SymmetryOperation extends cz.cuni.mff.peckam.java.origamist.model.jaxb.SymmetryOperation implements
        OperationContainer
{
    @Override
    protected void init()
    {
        super.init();
        type = Operations.SYMMETRY; // it doesn't help to specify the value as fixed in XSD
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
    public boolean areContentsVisible()
    {
        return isDisplayed();
    }

    @Override
    protected List<OperationArgument> initArguments()
    {
        List<OperationArgument> arguments = new ArrayList<OperationArgument>(2);

        arguments.add(new LineArgument(true, "operation.argument.symmetry.axis"));
        arguments.add(new OperationsArgument(true, "operation.argument.last.operations"));
        arguments.add(new BooleanArgument(true, "operation.argument.hidden", "operation.argument.hidden.message",
                "operation.argument.hidden.displayed", "operation.argument.hidden.hidden"));

        return arguments;
    }

    @Override
    public void fillFromArguments() throws IllegalStateException
    {
        this.line = ((LineArgument) arguments.get(0)).getLine2D();

        this.operations.clear();
        for (Operation o : ((OperationsArgument) arguments.get(1)).getOperations()) {
            if (o instanceof HasSymmetricOperation) {
                operations.add(((HasSymmetricOperation) o).getSymmetricOperation(line.toLine2d()));
            } else {
                operations.add(o);
            }
        }

        if (((BooleanArgument) arguments.get(2)).getValue() != null)
            this.displayed = ((BooleanArgument) arguments.get(2)).getValue();
    }

    @Override
    public String getL7dUserTip(OperationArgument argument)
    {
        String bundleKey = null;
        if (argument == getArguments().get(0)) {
            bundleKey = "symmetry.line.user.tip";
        } else if (argument == getArguments().get(1)) {
            bundleKey = "symmetry.steps.user.tip";
        } else if (argument == getArguments().get(2)) {
            bundleKey = "symmetry.visible.user.tip";
        }

        if (bundleKey != null)
            return messages.getString(bundleKey);
        else
            return null;
    }

    @Override
    public boolean isCompletelyDelayedToNextStep()
    {
        return !isDisplayed();
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
        if (displayed) {
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
        return "SymmetryOperation [displayed=" + displayed + ", line=" + line + ", operations=" + operations + "]";
    }

}

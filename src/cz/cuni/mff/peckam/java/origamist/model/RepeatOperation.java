/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.model;

import java.text.MessageFormat;

import cz.cuni.mff.peckam.java.origamist.model.jaxb.Operations;
import cz.cuni.mff.peckam.java.origamist.modelstate.ModelState;

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

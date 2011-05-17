/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.model;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import cz.cuni.mff.peckam.java.origamist.math.AngleUnit;
import cz.cuni.mff.peckam.java.origamist.model.jaxb.Operations;
import cz.cuni.mff.peckam.java.origamist.modelstate.ModelState;
import cz.cuni.mff.peckam.java.origamist.modelstate.arguments.AngleArgument;
import cz.cuni.mff.peckam.java.origamist.modelstate.arguments.OperationArgument;
import cz.cuni.mff.peckam.java.origamist.services.ServiceLocator;
import cz.cuni.mff.peckam.java.origamist.services.interfaces.ConfigurationManager;

/**
 * Rotate operation.
 * 
 * @author Martin Pecka
 */
public class RotateOperation extends cz.cuni.mff.peckam.java.origamist.model.jaxb.RotateOperation
{

    @Override
    protected void init()
    {
        super.init();
        type = Operations.ROTATE; // it doesn't help to specify the value as fixed in XSD
    }

    @Override
    public ModelState getModelState(ModelState previousState)
    {
        // TODO handle <refPoint>
        previousState.addRotation(-this.angle);
        return previousState;
    }

    @Override
    public boolean isCompletelyDelayedToNextStep()
    {
        return true;
    }

    @Override
    protected List<OperationArgument> initArguments()
    {
        List<OperationArgument> result = new ArrayList<OperationArgument>(1);

        result.add(new AngleArgument(true, "operation.argument.angle"));

        return result;
    }

    @Override
    public void fillFromArguments() throws IllegalStateException
    {
        super.fillFromArguments();
        this.angle = ((AngleArgument) arguments.get(0)).getAngle();
    }

    @Override
    public String getDefaultDescription()
    {
        String prefix = type.toString();
        StringBuilder text = new StringBuilder("<html><body><dl style=\"margin:0px;\"><dt>").append(getL7dName())
                .append("</dt>");

        text.append("<dd style=\"margin-left: 10px;\">");
        AngleUnit prefUnit = ServiceLocator.get(ConfigurationManager.class).get().getPreferredAngleUnit();
        String angleText = prefUnit.formatValue(AngleUnit.RAD.convertTo(angle, prefUnit));
        text.append(MessageFormat.format(messages.getString(prefix + ".angleFormat"), new Object[] { angleText }));
        text.append("</dd>");

        text.append("</dl></body></html>");
        return text.toString();
    }

    @Override
    public String toString()
    {
        return "RotateOperation [angle=" + angle + "]";
    }
}

/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.model;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import cz.cuni.mff.peckam.java.origamist.exceptions.InvalidOperationException;
import cz.cuni.mff.peckam.java.origamist.model.jaxb.Operations;
import cz.cuni.mff.peckam.java.origamist.modelstate.ModelState;
import cz.cuni.mff.peckam.java.origamist.modelstate.arguments.IntegerArgument;
import cz.cuni.mff.peckam.java.origamist.modelstate.arguments.OperationArgument;
import cz.cuni.mff.peckam.java.origamist.modelstate.arguments.PointArgument;
import cz.cuni.mff.peckam.java.origamist.modelstate.arguments.TextArgument;

/**
 * Add a marker that moves with a specified point on the paper.
 * 
 * @author Martin Pecka
 */
public class MarkerOperation extends cz.cuni.mff.peckam.java.origamist.model.jaxb.MarkerOperation
{
    @Override
    protected void init()
    {
        super.init();
        type = Operations.MARKER; // it doesn't help to specify the value as fixed in XSD
    }

    @Override
    public ModelState getModelState(ModelState previousState) throws InvalidOperationException
    {
        previousState.addMarker(refPoint.toPoint2d(), text, stepsToHide);
        return previousState;
    }

    @Override
    protected List<OperationArgument> initArguments()
    {
        List<OperationArgument> result = new ArrayList<OperationArgument>(2);

        result.add(new PointArgument(true, "operation.argument.select.point"));
        result.add(new TextArgument(true, "operation.argument.text"));
        result.add(new IntegerArgument(true, "operation.argument.stepToHide"));

        return result;
    }

    @Override
    public void fillFromArguments() throws IllegalStateException
    {
        super.fillFromArguments();
        this.refPoint = ((PointArgument) arguments.get(0)).getPoint2D();
        this.text = ((TextArgument) arguments.get(1)).getText();
        this.stepsToHide = ((IntegerArgument) arguments.get(2)).getInteger();
    }

    @Override
    public String getDefaultDescription()
    {
        String prefix = type.toString();
        StringBuilder text = new StringBuilder("<html><body><dl style=\"margin:0px;\"><dt>").append(getL7dName())
                .append("</dt>");

        text.append("<dd style=\"margin-left: 10px;\">");
        text.append(MessageFormat.format(messages.getString(prefix + ".textFormat"), new Object[] { this.text }));
        text.append("</dd>");

        text.append("</dl></body></html>");
        return text.toString();
    }
}

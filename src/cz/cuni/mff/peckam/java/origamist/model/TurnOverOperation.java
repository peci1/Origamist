/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.model;

import cz.cuni.mff.peckam.java.origamist.model.jaxb.Operations;
import cz.cuni.mff.peckam.java.origamist.modelstate.ModelState;
import cz.cuni.mff.peckam.java.origamist.modelstate.arguments.OperationArgument;

/**
 * Turn over operation.
 * 
 * @author Martin Pecka
 */
public class TurnOverOperation extends cz.cuni.mff.peckam.java.origamist.model.jaxb.TurnOverOperation
{
    @Override
    protected void init()
    {
        super.init();
        type = Operations.TURN_OVER; // it doesn't help to specify the value as fixed in XSD
    }

    @Override
    public ModelState getModelState(ModelState previousState)
    {
        previousState.flipViewingAngle();
        return previousState;
    }

    @Override
    public boolean isCompletelyDelayedToNextStep()
    {
        return true;
    }

    @Override
    public String getL7dUserTip(OperationArgument argument)
    {
        return messages.getString("turn.over.user.tip");
    }

    @Override
    public String toString()
    {
        return "TurnOverOperation";
    }
}

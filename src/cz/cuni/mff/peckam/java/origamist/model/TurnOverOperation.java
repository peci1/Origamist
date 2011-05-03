/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.model;

import cz.cuni.mff.peckam.java.origamist.modelstate.ModelState;

/**
 * Turn over operation.
 * 
 * @author Martin Pecka
 */
public class TurnOverOperation extends cz.cuni.mff.peckam.java.origamist.model.jaxb.TurnOverOperation
{
    @Override
    public ModelState getModelState(ModelState previousState)
    {
        previousState.flipViewingAngle();
        return previousState;
    }

    @Override
    public String toString()
    {
        return "TurnOverOperation";
    }
}

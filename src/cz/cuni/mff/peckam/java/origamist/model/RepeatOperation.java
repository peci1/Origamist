/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.model;

import cz.cuni.mff.peckam.java.origamist.modelstate.ModelState;

/**
 * A repeat operation.
 * 
 * @author Martin Pecka
 */
public class RepeatOperation extends cz.cuni.mff.peckam.java.origamist.model.jaxb.RepeatOperation
{
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
    public String toString()
    {
        return "RepeatOperation [display=" + display + ", operations=" + operations + "]";
    }
}

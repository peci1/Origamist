/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.model;

import java.util.List;

import cz.cuni.mff.peckam.java.origamist.utils.ObservableList;

/**
 * The steps the model consists of.
 * 
 * @author Martin Pecka
 */
public class Steps extends cz.cuni.mff.peckam.java.origamist.model.jaxb.Steps
{
    @Override
    public List<Step> getStep()
    {
        // this must be done due to odd behavior - even though the implClass of the list is ObservableList, JAXB somehow
        // constructs an ArrayList instead and sets it as this.step.
        List<Step> result = super.getStep();
        if (!(result instanceof ObservableList<?>)) {
            result = new ObservableList<Step>(result);
            step = result;
        }
        return result;
    }
}

/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.model;

import cz.cuni.mff.peckam.java.origamist.exceptions.InvalidOperationException;
import cz.cuni.mff.peckam.java.origamist.modelstate.ModelState;

/**
 * Add a marker that moves with a specified point on the paper.
 * 
 * @author Martin Pecka
 */
public class MarkerOperation extends cz.cuni.mff.peckam.java.origamist.model.jaxb.MarkerOperation
{
    @Override
    public ModelState getModelState(ModelState previousState) throws InvalidOperationException
    {
        previousState.addMarker(refPoint.toPoint2d(), text, stepsToHide);
        return previousState;
    }
}

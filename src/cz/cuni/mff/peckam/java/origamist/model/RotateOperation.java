/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.model;

import cz.cuni.mff.peckam.java.origamist.modelstate.ModelState;

/**
 * Rotate operation.
 * 
 * @author Martin Pecka
 */
public class RotateOperation extends cz.cuni.mff.peckam.java.origamist.model.jaxb.RotateOperation
{
    @Override
    public ModelState getModelState(ModelState previousState)
    {
        // TODO handle <refPoint>
        previousState.addRotation(-this.angle);
        return previousState;
    }

    @Override
    public String toString()
    {
        return "RotateOperation [angle=" + angle + "]";
    }
}

/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.model;

import java.util.ArrayList;
import java.util.List;

import cz.cuni.mff.peckam.java.origamist.modelstate.ModelState;
import cz.cuni.mff.peckam.java.origamist.modelstate.arguments.AngleArgument;
import cz.cuni.mff.peckam.java.origamist.modelstate.arguments.OperationArgument;

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
    public String toString()
    {
        return "RotateOperation [angle=" + angle + "]";
    }
}

/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.model;

import cz.cuni.mff.peckam.java.origamist.modelstate.ModelState;

/**
 * Inside or outside reverse fold operation.
 * 
 * @author Martin Pecka
 */
public class ReverseFoldOperation extends cz.cuni.mff.peckam.java.origamist.model.jaxb.ReverseFoldOperation
{
    @Override
    public ModelState getModelState(ModelState previousState)
    {
        switch (this.type) {
            case INSIDE_REVERSE_FOLD:
                break;
            case OUTSIDE_REVERSE_FOLD:
                break;
        }
        return previousState;
    }

    @Override
    public String toString()
    {
        return "ReverseFoldOperation [type=" + type + ", layer=" + layer + ", line=" + line + ", refLine=" + refLine
                + "]";
    }
}

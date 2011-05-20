/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.exceptions;

import java.util.ResourceBundle;

import cz.cuni.mff.peckam.java.origamist.modelstate.Layer;
import cz.cuni.mff.peckam.java.origamist.services.ServiceLocator;
import cz.cuni.mff.peckam.java.origamist.services.interfaces.ConfigurationManager;

/**
 * An exception thrown if the paper would intersect.
 * 
 * @author Martin Pecka
 */
public class PaperIntersectionException extends PaperStructureException
{
    /** */
    private static final long serialVersionUID = -3445889651234354185L;

    /** The layers that caused this exception. */
    protected Layer           layer1, layer2;

    /**
     * @param layer1 The layers that caused this exception.
     * @param layer2 The layers that caused this exception.
     */
    public PaperIntersectionException(Layer layer1, Layer layer2)
    {
        this.layer1 = layer1;
        this.layer2 = layer2;
    }

    @Override
    public String getMessage()
    {
        return ResourceBundle.getBundle(PaperStructureException.class.getName(),
                ServiceLocator.get(ConfigurationManager.class).get().getLocale()).getString("intersect");
    }

    @Override
    public String toString()
    {
        return "PaperIntersectionException [layer1=" + layer1 + ", layer2=" + layer2 + "], " + super.toString();
    }
}

/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.model;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlRegistry;

/**
 * 
 * 
 * @author Martin Pecka
 */
@XmlRegistry
public class ObjectFactory extends cz.cuni.mff.peckam.java.origamist.model.jaxb.ObjectFactory
{

    /**
     * Create, setup and return the origami model. Set next and previous for all
     * steps.
     */
    @Override
    public JAXBElement<cz.cuni.mff.peckam.java.origamist.model.jaxb.Origami> createOrigami(
            cz.cuni.mff.peckam.java.origamist.model.jaxb.Origami value)
    {
        JAXBElement<cz.cuni.mff.peckam.java.origamist.model.jaxb.Origami> result = super.createOrigami(value);
        if (value.getModel() != null && value.getModel().getSteps() != null && value instanceof Origami) {
            ((Origami) value).initListeners();
        }
        if (value.getModel() != null && value.getModel().getPaper() != null) {
            // set the reference unit to the paper size
            ((Origami) value).getModel().getPaper().setSize(((Origami) value).getModel().getPaper().getSize());
        }
        return result;
    }
}

/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.model;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
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
     * Create an instance of {@link cz.cuni.mff.peckam.java.origamist.model.jaxb.Origami }
     * 
     */
    @Override
    public cz.cuni.mff.peckam.java.origamist.model.jaxb.Origami createOrigami()
    {
        return new Origami();
    }

    /**
     * Create, setup and return the origami model. Set next and previous for all
     * steps.
     */
    @Override
    @XmlElementDecl(namespace = "http://www.mff.cuni.cz/~peckam/java/origamist/diagram/", name = "origami")
    public JAXBElement<cz.cuni.mff.peckam.java.origamist.model.jaxb.Origami> createOrigami(
            cz.cuni.mff.peckam.java.origamist.model.jaxb.Origami value)
    {
        JAXBElement<cz.cuni.mff.peckam.java.origamist.model.jaxb.Origami> result = super.createOrigami(value);
        if (value.getModel() != null && value.getModel().getSteps() != null && value instanceof Origami) {
            ((Origami) value).initSteps();
        }
        return result;
    }
}

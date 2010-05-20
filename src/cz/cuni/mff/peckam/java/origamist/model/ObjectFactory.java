/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.model;

import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;

import cz.cuni.mff.peckam.java.origamist.modelstate.DefaultModelState;
import cz.cuni.mff.peckam.java.origamist.modelstate.ModelState;

/**
 * 
 * 
 * @author Martin Pecka
 */
@XmlRegistry
public class ObjectFactory extends
        cz.cuni.mff.peckam.java.origamist.model.jaxb.ObjectFactory
{

    /**
     * Create an instance of
     * {@link cz.cuni.mff.peckam.java.origamist.model.jaxb.Origami }
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
        JAXBElement<cz.cuni.mff.peckam.java.origamist.model.jaxb.Origami> result = super
                .createOrigami(value);
        List<cz.cuni.mff.peckam.java.origamist.model.jaxb.Step> list = value
                .getModel().getSteps().getStep();

        ModelState defaultModelState = new DefaultModelState((Origami) value);
        if (list.size() == 0)
            return result;
        else if (list.size() == 1) {
            ((Step) list.get(0)).setDefaultModelState(defaultModelState);
            return result;
        } else {
            Iterator<cz.cuni.mff.peckam.java.origamist.model.jaxb.Step> i = list
                    .iterator();
            Step prev = null, curr = null, next = (Step) i.next();
            next.setDefaultModelState(defaultModelState);
            while (i.hasNext()) {
                prev = curr;
                curr = next;
                next = (Step) i.next();
                curr.setPrevious(prev);
                curr.setNext(next);
            }
            prev = curr;
            curr = next;
            next = null;
            curr.setPrevious(prev);
            curr.setNext(next);

            return result;
        }
    }
}

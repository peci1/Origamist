/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.files;

import javax.xml.bind.JAXBElement;

import cz.cuni.mff.peckam.java.origamist.files.jaxb.Listing;

/**
 * Additional functions for the JAXB generated object factory.
 * 
 * @author Martin Pecka
 */
public class ObjectFactory extends cz.cuni.mff.peckam.java.origamist.files.jaxb.ObjectFactory
{
    @Override
    public JAXBElement<Listing> createListing(Listing value)
    {
        JAXBElement<Listing> result = super.createListing(value);
        ((cz.cuni.mff.peckam.java.origamist.files.Listing) result.getValue()).updateChildParents();
        return result;
    }

}

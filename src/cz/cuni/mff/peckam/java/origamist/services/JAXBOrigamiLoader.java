/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.services;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import cz.cuni.mff.peckam.java.origamist.exceptions.UnsupportedDataFormatException;
import cz.cuni.mff.peckam.java.origamist.model.ObjectFactory;
import cz.cuni.mff.peckam.java.origamist.model.Origami;

/**
 * Loads an origami model from XML file using JAXB.
 * 
 * @author Martin Pecka
 */
public class JAXBOrigamiLoader implements OrigamiLoader
{

    @SuppressWarnings("unchecked")
    @Override
    public Origami loadModel(String path) throws FileNotFoundException, UnsupportedDataFormatException
    {
        try {
            JAXBContext context = JAXBContext.newInstance("cz.cuni.mff.peckam.java.origamist.model.jaxb", getClass()
                    .getClassLoader());
            Unmarshaller u = context.createUnmarshaller();
            u.setProperty("com.sun.xml.internal.bind.ObjectFactory",
                    new cz.cuni.mff.peckam.java.origamist.model.ObjectFactory());

            // TODO handle older versions
            Origami o = ((JAXBElement<Origami>) u.unmarshal(new FileInputStream(path))).getValue();

            // the following line is a fix for a (possible) bug in JAXB
            // I think that the createOrigami method should have been called by unmarshal(),
            // but for some odd reason it doesn't get called
            o = (Origami) new ObjectFactory().createOrigami(o).getValue();

            return o;
        } catch (JAXBException e) {
            // TODO handle errors in data files
            throw new UnsupportedDataFormatException(e);
        }
    }
}

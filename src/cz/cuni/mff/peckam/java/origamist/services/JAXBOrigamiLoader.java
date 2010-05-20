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
    public Origami loadModel(String path) throws FileNotFoundException,
            UnsupportedDataFormatException
    {
        try {
            JAXBContext context = JAXBContext.newInstance(
                    "cz.cuni.mff.peckam.java.origamist.model.jaxb", getClass()
                            .getClassLoader());
            Unmarshaller u = context.createUnmarshaller();
            u
                    .setProperty(
                            com.sun.xml.internal.bind.v2.runtime.unmarshaller.UnmarshallerImpl.FACTORY,
                            new cz.cuni.mff.peckam.java.origamist.model.ObjectFactory());
            Origami o = ((JAXBElement<Origami>) u
                    .unmarshal(new FileInputStream(path))).getValue();
            o = (Origami) new ObjectFactory().createOrigami(o).getValue();
            return o;
        } catch (JAXBException e) {
            throw new UnsupportedDataFormatException(e);
        }
    }
}

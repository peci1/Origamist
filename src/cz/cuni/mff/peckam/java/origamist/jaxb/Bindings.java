package cz.cuni.mff.peckam.java.origamist.jaxb;

import javax.xml.bind.JAXBException;

import org.xml.sax.ContentHandler;

/**
 * An interface for objects able to unmarshal XML files.
 * <p>
 * 
 * Implementations of Bindings are not required to be re-entrant (i.e., are not required to support simultaneous
 * execution by multiple threads), but are required to be re-usable.
 * <p>
 * 
 * This package is based on a XML Schema versioning system from http://www.funkypeople.biz/knowledge/JavaXml-v2.zip .
 * 
 * @author Sean Barnett
 * @author Martin Pecka
 * 
 * @type T The type of the unmarshaled object.
 */
public interface Bindings<T>
{
    /**
     * @return The {@link ContentHandler} to be attached to the reader while parsing.
     */
    ContentHandler getContentHandler();

    /**
     * @return The unmarshalled object. Call this after the parser has finished its work.
     * 
     * @throws JAXBException If the unmarshalling failed.
     * @throws IllegalStateException If this method is called before the parser has reached the end of the document.
     * @throws ClassCastException If the unmarshalled object cannot be cast to this class' type parameter.
     */
    T getResult() throws JAXBException, IllegalStateException, ClassCastException;
}

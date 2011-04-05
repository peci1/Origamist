package cz.cuni.mff.peckam.java.origamist.jaxb;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.xml.sax.ContentHandler;

/**
 * Implementation of Bindings interface that uses straightforward JAXB operations underneath. This is targetted where
 * the Java bindings objects are bound directly to the input and output XML documents' schema (i.e., no transform is
 * required to move between XML schema versions).
 * <p>
 * 
 * This package is based on a XML Schema versioning system from http://www.funkypeople.biz/knowledge/JavaXml-v2.zip .
 * 
 * @author Sean Barnett
 * @author Martin Pecka
 * 
 * @param T The type of the unmarhalled object.
 */
public class SimpleBindings<T> implements Bindings<T>
{

    /** The unmarshaller to be used for this binding. */
    protected final Unmarshaller unmarshaller;

    /**
     * Simple constructor.
     * 
     * @param bindingsManager The reference to the bindings manager.
     * @param targetSchema The schema definition for target XML document.
     * 
     * @throws JAXBException If an error was encountered while creating the Unmarshaller object.
     */
    public SimpleBindings(BindingsManager bindingsManager, SchemaInfo targetSchema) throws JAXBException
    {
        unmarshaller = bindingsManager.createUnmarshaller(targetSchema);
    }

    @Override
    public ContentHandler getContentHandler()
    {
        return unmarshaller.getUnmarshallerHandler();
    }

    @SuppressWarnings("unchecked")
    @Override
    public T getResult() throws JAXBException, IllegalStateException, ClassCastException
    {
        Object result = unmarshaller.getUnmarshallerHandler().getResult();

        // the result can be wrapped inside a JAXBElement if the schema doesn't define a root element
        if (result instanceof JAXBElement<?>)
            result = ((JAXBElement<?>) result).getValue();

        return (T) result;
    }
}

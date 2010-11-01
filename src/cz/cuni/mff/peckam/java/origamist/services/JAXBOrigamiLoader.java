/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.services;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.Enumeration;
import java.util.concurrent.Callable;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.UnmarshallerHandler;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.NamespaceSupport;
import org.xml.sax.helpers.XMLFilterImpl;

import cz.cuni.mff.peckam.java.origamist.exceptions.UnsupportedDataFormatException;
import cz.cuni.mff.peckam.java.origamist.model.ObjectFactory;
import cz.cuni.mff.peckam.java.origamist.model.Origami;
import cz.cuni.mff.peckam.java.origamist.model.jaxb.Model;

/**
 * Loads an origami model from XML file using JAXB.
 * 
 * The code is inspired by partial-unmarshalling example in JAXB section of JWSDP.
 * 
 * @author Martin Pecka
 */
public class JAXBOrigamiLoader implements OrigamiLoader
{

    /** The model to return. */
    protected Origami model        = null;

    /** The base path for resolving relative URIs. */
    protected URL     documentBase = null;

    public JAXBOrigamiLoader(URL documentBase)
    {
        this.documentBase = documentBase;
    }

    @Override
    public Origami loadModel(final URI path, boolean onlyMetadata) throws IOException, UnsupportedDataFormatException
    {
        URL url = null;
        if (path.isAbsolute()) {
            url = path.toURL();
        } else {
            url = new URL(documentBase, path.toString());
        }
        return loadModel(url, onlyMetadata);
    }

    @Override
    public Origami loadModel(final URL path, boolean onlyMetadata) throws IOException, UnsupportedDataFormatException
    {
        try {
            XMLReader reader = getXMLReader();

            InputStream is = path.openStream();
            PartialLoadingXMLFilter filter = new PartialLoadingXMLFilter(is, onlyMetadata);

            reader.setContentHandler(filter);

            // parses the model to this.model
            try {
                reader.parse(new InputSource(is));
            } catch (SAXException e) {
                // if e is ParseAbortedException, then no error actually occured, just the reading was aborted
                if (!(e.getCause() instanceof ParseAbortedException))
                    throw e;
            }

            if (onlyMetadata) {
                // if only metadata are loaded, we need to provide a method to lazily load the rest of the diagram
                final int origVersion = model.getVersion();

                model.setLoadModelCallable(new Callable<Model>() {
                    @Override
                    public Model call() throws Exception
                    {
                        try {
                            Unmarshaller u = getUnmarshallerForVersion(origVersion);

                            @SuppressWarnings("unchecked")
                            Origami o = ((JAXBElement<Origami>) u.unmarshal(path.openStream())).getValue();

                            // the following line is a fix for a (possible) bug in JAXB
                            // I think that the createOrigami method should have been called by unmarshal(),
                            // but for some odd reason it doesn't get called
                            o = (Origami) new ObjectFactory().createOrigami(o).getValue();

                            o = o.convertToNewestVersion();

                            return o.getModel();
                        } catch (JAXBException e) {
                            System.err.println(e);
                            return new ObjectFactory().createModel();
                        } catch (IOException e) {
                            System.err.println(e);
                            return new ObjectFactory().createModel();
                        }
                    }
                });
            }

            return model.convertToNewestVersion();
        } catch (SAXException e) {
            System.err.println(e);
        } catch (ParserConfigurationException e) {
            System.err.println(e);
        }
        return null;
    }

    /**
     * @return the documentBase
     */
    public URL getDocumentBase()
    {
        return documentBase;
    }

    /**
     * @param documentBase the documentBase to set
     */
    public void setDocumentBase(URL documentBase)
    {
        this.documentBase = documentBase;
    }

    /**
     * Return the JAXB Unmarshaller for the given schema version.
     * 
     * @param version The version of the schema. Starting from 1.
     * @return The unmarshaller for the given schema version.
     * @throws JAXBException see {@link JAXBContext#newInstance(String, ClassLoader)}
     */
    protected Unmarshaller getUnmarshallerForVersion(int version) throws JAXBException
    {
        // TODO handle different versions
        JAXBContext context = JAXBContext.newInstance("cz.cuni.mff.peckam.java.origamist.model.jaxb", getClass()
                .getClassLoader());
        Unmarshaller u = context.createUnmarshaller();
        u.setProperty("com.sun.xml.internal.bind.ObjectFactory", new ObjectFactory());
        return u;
    }

    /**
     * Returns the XML reader that is able to parse the diagram schema.
     * 
     * @return The XML reader that is able to parse the diagram schema.
     * @throws SAXException For SAX errors.
     * @throws ParserConfigurationException If a parser cannot be created which satisfies the requested configuration.
     */
    protected XMLReader getXMLReader() throws SAXException, ParserConfigurationException
    {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setNamespaceAware(true);

        return factory.newSAXParser().getXMLReader();
    }

    /**
     * This XML filter first reads the version of the used schema, then decides, which JAXB unmarshaller it will use,
     * and, if desired, can stop reading when the model's metadata are read (in particular it will close the URL
     * connection immediately after the metadata are loaded).
     * 
     * @author Martin Pecka
     */
    protected class PartialLoadingXMLFilter extends XMLFilterImpl
    {
        /**
         * The input stream we are reading from. It is needed here in order to be able to prematurely close the
         * connection.
         */
        private InputStream         is;

        /**
         * If true, stop reading after all metadata have been read (eg. when the start of <code>model</code> element is
         * hit.)
         */
        private boolean             onlyMetadata;

        /** Reference to the unmarshaller handler of the unmarshaller that is unmarshalling an object. */
        private UnmarshallerHandler unmarshallerHandler;

        /** Keeps a reference to the locator object so that we can later pass it to a JAXB unmarshaller. */
        private Locator             locator;

        /**
         * Used to keep track of in-scope namespace bindings.
         * 
         * For JAXB unmarshaller to correctly unmarshal documents, it needs to know all the effective namespace
         * declarations.
         */
        private NamespaceSupport    namespaces = new NamespaceSupport();

        /**
         * @param is The input stream we are reading from.
         * @param onlyMetadata If true, close the <code>is</code> when the <code>model</code> tag is encountered.
         */
        protected PartialLoadingXMLFilter(InputStream is, boolean onlyMetadata)
        {
            this.is = is;
            this.onlyMetadata = onlyMetadata;
        }

        @Override
        public void startElement(String namespaceURI, String localName, String qName, Attributes atts)
                throws SAXException
        {
            if (namespaceURI.equals("http://www.mff.cuni.cz/~peckam/java/origamist/diagram/")
                    && localName.equals("origami")) {
                try {
                    int schemaVersion;
                    try {
                        schemaVersion = Integer.parseInt(atts.getValue("version"));
                    } catch (NumberFormatException e) {
                        schemaVersion = 1; // just a guess
                    }

                    Unmarshaller u = getUnmarshallerForVersion(schemaVersion);

                    unmarshallerHandler = u.getUnmarshallerHandler();

                    // set it as the content handler so that it will receive
                    // SAX events from now on.
                    setContentHandler(unmarshallerHandler);

                    // fire SAX events to emulate the start of a new document.
                    unmarshallerHandler.startDocument();
                    unmarshallerHandler.setDocumentLocator(locator);

                    @SuppressWarnings("rawtypes")
                    Enumeration e = namespaces.getPrefixes();
                    while (e.hasMoreElements()) {
                        String prefix = (String) e.nextElement();
                        String uri = namespaces.getURI(prefix);

                        unmarshallerHandler.startPrefixMapping(prefix, uri);
                    }
                    String defaultURI = namespaces.getURI("");
                    if (defaultURI != null)
                        unmarshallerHandler.startPrefixMapping("", defaultURI);
                } catch (JAXBException e1) {
                    throw new SAXException(e1);
                }

                super.startElement(namespaceURI, localName, qName, atts);
            } else if (localName.equals("model") && onlyMetadata) {
                try {
                    // metadate are loaded, so abort reading from the URL, close the connection
                    is.close();
                } catch (IOException e) {}

                super.startElement(namespaceURI, localName, qName, atts);
                endElement(namespaceURI, localName, qName);
                // this will process the rest of the unmarshalling with the <model> tag empty
                endElement("http://www.mff.cuni.cz/~peckam/java/origamist/diagram/", "origami",
                        namespaces.getPrefix("http://www.mff.cuni.cz/~peckam/java/origamist/diagram/") + ":origami");

                // abort parsing
                throw new SAXException(new ParseAbortedException());
            } else {
                super.startElement(namespaceURI, localName, qName, atts);
            }
        }

        @Override
        public void endElement(String namespaceURI, String localName, String qName) throws SAXException
        {
            // forward this event
            super.endElement(namespaceURI, localName, qName);

            // we have read whole XML file, so unmarshall the diagram
            if (namespaceURI.equals("http://www.mff.cuni.cz/~peckam/java/origamist/diagram/")
                    && localName.equals("origami")) {

                // emulate the end of the document.
                @SuppressWarnings("rawtypes")
                Enumeration e = namespaces.getPrefixes();
                while (e.hasMoreElements()) {
                    String prefix = (String) e.nextElement();
                    unmarshallerHandler.endPrefixMapping(prefix);
                }
                String defaultURI = namespaces.getURI("");
                if (defaultURI != null)
                    unmarshallerHandler.endPrefixMapping("");
                unmarshallerHandler.endDocument();

                // stop forwarding events by setting a dummy handler.
                // XMLFilter doesn't accept null, so we have to give it something,
                // hence a DefaultHandler, which does nothing.
                setContentHandler(new DefaultHandler());

                // then retrieve the fully unmarshalled object
                try {
                    @SuppressWarnings("unchecked")
                    Origami o = ((JAXBElement<Origami>) unmarshallerHandler.getResult()).getValue();

                    // the following line is a fix for a (possible) bug in JAXB
                    // I think that the createOrigami method should have been called by unmarshal(),
                    // but for some odd reason it doesn't get called
                    model = (Origami) new ObjectFactory().createOrigami(o).getValue();
                    model = model.convertToNewestVersion();
                } catch (JAXBException je) {
                    throw new SAXException(je);
                }

                unmarshallerHandler = null;
            }
        }

        public void setDocumentLocator(Locator locator)
        {
            super.setDocumentLocator(locator);
            this.locator = locator;
        }

        public void startPrefixMapping(String prefix, String uri) throws SAXException
        {
            namespaces.pushContext();
            namespaces.declarePrefix(prefix, uri);

            super.startPrefixMapping(prefix, uri);
        }

        public void endPrefixMapping(String prefix) throws SAXException
        {
            namespaces.popContext();

            super.endPrefixMapping(prefix);
        }
    }

    /**
     * This exception will tell that the parser didn't end with error, but it was just forced to stop reading of the
     * data.
     * 
     * @author Martin Pecka
     */
    class ParseAbortedException extends IOException
    {
        /** */
        private static final long serialVersionUID = -1560106113502958618L;
    }
}

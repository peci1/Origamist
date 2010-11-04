/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.services;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.MarshalException;
import javax.xml.bind.Marshaller;
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
import cz.cuni.mff.peckam.java.origamist.files.Listing;
import cz.cuni.mff.peckam.java.origamist.files.ObjectFactory;
import cz.cuni.mff.peckam.java.origamist.services.interfaces.ListingHandler;
import cz.cuni.mff.peckam.java.origamist.utils.URIAdapter;

/**
 * Handles loading and exporting a listing.xml file using JAXB.
 * 
 * The code is inspired by partial-unmarshalling example in JAXB section of JWSDP.
 * 
 * @author Martin Pecka
 */
public class JAXBListingHandler implements ListingHandler
{

    /** The listing to return. */
    protected Listing listing = null;

    @Override
    public void export(Listing listing, File file) throws IOException, MarshalException, JAXBException
    {
        if (!file.exists())
            file.createNewFile();
        if (!file.isFile())
            throw new IOException("Cannot save listing.xml in a directory or a non-file object: "
                    + file.getAbsolutePath() + ".");

        JAXBContext context = JAXBContext.newInstance("cz.cuni.mff.peckam.java.origamist.files.jaxb", getClass()
                .getClassLoader());
        Marshaller m = context.createMarshaller();

        // enable indenting and newline generation
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        // make URLs in the listing relative to the location we save the listing to
        m.setAdapter(new URIAdapter());
        m.getAdapter(URIAdapter.class).setRelativeBase(file.getParentFile().toURI());

        // do the Java class->XML conversion
        m.marshal(new ObjectFactory().createListing(listing), file);
    }

    @Override
    public Listing load(final URL path) throws IOException, UnsupportedDataFormatException
    {
        try {
            XMLReader reader = getXMLReader();

            InputStream is = path.openStream();
            PartialLoadingXMLFilter filter = new PartialLoadingXMLFilter();

            reader.setContentHandler(filter);

            // parses the listing to this.listing
            reader.parse(new InputSource(is));
            return listing.convertToNewestVersion();
        } catch (SAXException e) {
            throw new UnsupportedDataFormatException(e);
        } catch (ParserConfigurationException e) {
            throw new UnsupportedDataFormatException(e);
        }
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
        JAXBContext context = JAXBContext.newInstance("cz.cuni.mff.peckam.java.origamist.files.jaxb", getClass()
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

        @Override
        public void startElement(String namespaceURI, String localName, String qName, Attributes atts)
                throws SAXException
        {
            if (namespaceURI.equals("http://www.mff.cuni.cz/~peckam/java/origamist/files/")
                    && localName.equals("listing")) {
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
            }

            super.startElement(namespaceURI, localName, qName, atts);
        }

        @Override
        public void endElement(String namespaceURI, String localName, String qName) throws SAXException
        {
            // forward this event
            super.endElement(namespaceURI, localName, qName);

            // we have read whole XML file, so unmarshall the diagram
            if (namespaceURI.equals("http://www.mff.cuni.cz/~peckam/java/origamist/files/")
                    && localName.equals("listing")) {

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
                    Listing l = ((JAXBElement<Listing>) unmarshallerHandler.getResult()).getValue();

                    // the following line is a fix for a (possible) bug in JAXB
                    // I think that the createOrigami method should have been called by unmarshal(),
                    // but for some odd reason it doesn't get called
                    listing = (Listing) new ObjectFactory().createListing(l).getValue();
                    listing = listing.convertToNewestVersion();
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

}

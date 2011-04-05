/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.jaxb;

import javax.xml.transform.sax.SAXResult;

import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * A {@link SAXOutputtingContentHandler} that just delegates all {@link org.xml.sax.ContentHandler} events to its
 * {@link SAXResult}. If the result is not set or is set to <code>null</code>, no delegation takes place.
 * 
 * This class can be used as the base class for custom content handlers fed into
 * {@link BindingsManager#addTransform(SchemaInfo, SchemaInfo, String, TransformInfo)}.
 * 
 * @author Martin Pecka
 */
public class ResultDelegatingDefaultHandler extends DefaultHandler implements SAXOutputtingContentHandler
{

    /** The result to work with. */
    protected SAXResult result = null;

    @Override
    public void setResult(SAXResult result) throws IllegalArgumentException
    {
        this.result = result;
    }

    @Override
    public void setDocumentLocator(Locator locator)
    {
        if (result != null)
            result.getHandler().setDocumentLocator(locator);
    }

    @Override
    public void startDocument() throws SAXException
    {
        if (result != null)
            result.getHandler().startDocument();
    }

    @Override
    public void endDocument() throws SAXException
    {
        if (result != null)
            result.getHandler().endDocument();
    }

    @Override
    public void startPrefixMapping(String prefix, String uri) throws SAXException
    {
        if (result != null)
            result.getHandler().startPrefixMapping(prefix, uri);
    }

    @Override
    public void endPrefixMapping(String prefix) throws SAXException
    {
        if (result != null)
            result.getHandler().endPrefixMapping(prefix);
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException
    {
        if (result != null)
            result.getHandler().startElement(uri, localName, qName, atts);
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException
    {
        if (result != null)
            result.getHandler().endElement(uri, localName, qName);
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException
    {
        if (result != null)
            result.getHandler().characters(ch, start, length);
    }

    @Override
    public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException
    {
        if (result != null)
            result.getHandler().ignorableWhitespace(ch, start, length);
    }

    @Override
    public void processingInstruction(String target, String data) throws SAXException
    {
        if (result != null)
            result.getHandler().processingInstruction(target, data);
    }

    @Override
    public void skippedEntity(String name) throws SAXException
    {
        if (result != null)
            result.getHandler().skippedEntity(name);
    }
}

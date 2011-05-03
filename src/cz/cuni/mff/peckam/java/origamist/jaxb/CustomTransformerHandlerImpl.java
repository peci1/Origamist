package cz.cuni.mff.peckam.java.origamist.jaxb;

import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.sax.TransformerHandler;

import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

/**
 * An implementation of a wrapper class that just upcasts {@link TransformerHandler} to {@link CustomTransformerHandler}
 * .
 * 
 * @author Martin Pecka
 */
public class CustomTransformerHandlerImpl implements CustomTransformerHandler
{
    /** The handler this class delegates calls to. */
    protected TransformerHandler handler;

    /**
     * @param handler
     */
    public CustomTransformerHandlerImpl(TransformerHandler handler)
    {
        this.handler = handler;
    }

    @Override
    public void setResult(Result result) throws IllegalArgumentException
    {
        handler.setResult(result);
    }

    @Override
    public void setResult(SAXResult result) throws IllegalArgumentException
    {
        handler.setResult(result);
    }

    @Override
    public void startDTD(String name, String publicId, String systemId) throws SAXException
    {
        handler.startDTD(name, publicId, systemId);
    }

    @Override
    public void setSystemId(String systemID)
    {
        handler.setSystemId(systemID);
    }

    @Override
    public String getSystemId()
    {
        return handler.getSystemId();
    }

    @Override
    public void notationDecl(String name, String publicId, String systemId) throws SAXException
    {
        handler.notationDecl(name, publicId, systemId);
    }

    @Override
    public Transformer getTransformer()
    {
        return handler.getTransformer();
    }

    @Override
    public void setDocumentLocator(Locator locator)
    {
        handler.setDocumentLocator(locator);
    }

    @Override
    public void unparsedEntityDecl(String name, String publicId, String systemId, String notationName)
            throws SAXException
    {
        handler.unparsedEntityDecl(name, publicId, systemId, notationName);
    }

    @Override
    public void endDTD() throws SAXException
    {
        handler.endDTD();
    }

    @Override
    public void startDocument() throws SAXException
    {
        handler.startDocument();
    }

    @Override
    public void startEntity(String name) throws SAXException
    {
        handler.startEntity(name);
    }

    @Override
    public void endDocument() throws SAXException
    {
        handler.endDocument();
    }

    @Override
    public void startPrefixMapping(String prefix, String uri) throws SAXException
    {
        handler.startPrefixMapping(prefix, uri);
    }

    @Override
    public void endEntity(String name) throws SAXException
    {
        handler.endEntity(name);
    }

    @Override
    public void startCDATA() throws SAXException
    {
        handler.startCDATA();
    }

    @Override
    public void endPrefixMapping(String prefix) throws SAXException
    {
        handler.endPrefixMapping(prefix);
    }

    @Override
    public void endCDATA() throws SAXException
    {
        handler.endCDATA();
    }

    @Override
    public void comment(char[] ch, int start, int length) throws SAXException
    {
        handler.comment(ch, start, length);
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException
    {
        handler.startElement(uri, localName, qName, atts);
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException
    {
        handler.endElement(uri, localName, qName);
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException
    {
        handler.characters(ch, start, length);
    }

    @Override
    public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException
    {
        handler.ignorableWhitespace(ch, start, length);
    }

    @Override
    public void processingInstruction(String target, String data) throws SAXException
    {
        handler.processingInstruction(target, data);
    }

    @Override
    public void skippedEntity(String name) throws SAXException
    {
        handler.skippedEntity(name);
    }
}
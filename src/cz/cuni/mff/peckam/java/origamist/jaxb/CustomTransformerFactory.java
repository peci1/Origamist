/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.jaxb;

import javax.xml.transform.ErrorListener;
import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.URIResolver;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TemplatesHandler;
import javax.xml.transform.sax.TransformerHandler;

import org.xml.sax.XMLFilter;

/**
 * A {@link SAXTransformerFactory} upgraded to return {@link CustomTransformerHandler} objects instead of
 * {@link TransformerHandler} ones.
 * 
 * @author Martin Pecka
 */
public class CustomTransformerFactory extends SAXTransformerFactory
{

    /** The delegated factory. */
    protected final SAXTransformerFactory factory;

    /**
     * Use the default SAX transformer factory returned by {@link SAXTransformerFactory#newInstance()}.
     */
    public CustomTransformerFactory()
    {
        this(null);
    }

    /**
     * Create the custom factory over the given one. If the given factory is <code>null</code>, the default factory as
     * returned by {@link SAXTransformerFactory#newInstance()} is used.
     * 
     * @param factory The factory to use.
     */
    public CustomTransformerFactory(SAXTransformerFactory factory)
    {
        if (factory != null)
            this.factory = factory;
        else
            this.factory = (SAXTransformerFactory) SAXTransformerFactory.newInstance();
    }

    @Override
    public CustomTransformerHandler newTransformerHandler(Source src) throws TransformerConfigurationException
    {
        return convert(factory.newTransformerHandler(src));
    }

    @Override
    public CustomTransformerHandler newTransformerHandler(Templates templates) throws TransformerConfigurationException
    {
        return convert(factory.newTransformerHandler(templates));
    }

    @Override
    public CustomTransformerHandler newTransformerHandler() throws TransformerConfigurationException
    {
        return convert(factory.newTransformerHandler());
    }

    @Override
    public TemplatesHandler newTemplatesHandler() throws TransformerConfigurationException
    {
        return factory.newTemplatesHandler();
    }

    @Override
    public XMLFilter newXMLFilter(Source src) throws TransformerConfigurationException
    {
        return factory.newXMLFilter(src);
    }

    @Override
    public XMLFilter newXMLFilter(Templates templates) throws TransformerConfigurationException
    {
        return factory.newXMLFilter(templates);
    }

    @Override
    public Transformer newTransformer(Source source) throws TransformerConfigurationException
    {
        return factory.newTransformer(source);
    }

    @Override
    public Transformer newTransformer() throws TransformerConfigurationException
    {
        return factory.newTransformer();
    }

    @Override
    public Templates newTemplates(Source source) throws TransformerConfigurationException
    {
        return factory.newTemplates(source);
    }

    @Override
    public Source getAssociatedStylesheet(Source source, String media, String title, String charset)
            throws TransformerConfigurationException
    {
        return factory.getAssociatedStylesheet(source, media, title, charset);
    }

    @Override
    public void setURIResolver(URIResolver resolver)
    {
        factory.setURIResolver(resolver);
    }

    @Override
    public URIResolver getURIResolver()
    {
        return factory.getURIResolver();
    }

    @Override
    public void setFeature(String name, boolean value) throws TransformerConfigurationException
    {
        factory.setFeature(name, value);
    }

    @Override
    public boolean getFeature(String name)
    {
        return factory.getFeature(name);
    }

    @Override
    public void setAttribute(String name, Object value)
    {
        factory.setAttribute(name, value);
    }

    @Override
    public Object getAttribute(String name)
    {
        return factory.getAttribute(name);
    }

    @Override
    public void setErrorListener(ErrorListener listener)
    {
        factory.setErrorListener(listener);
    }

    @Override
    public ErrorListener getErrorListener()
    {
        return factory.getErrorListener();
    }

    /**
     * Upcast the given {@link TransformerHandler} to {@link CustomTransformerHandler} by just delegating all methods to
     * it.
     * 
     * @param handler The handler to upcast.
     * @return The upcasted handler.
     */
    protected CustomTransformerHandler convert(TransformerHandler handler)
    {
        return new CustomTransformerHandlerImpl(handler);
    }

}

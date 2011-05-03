package cz.cuni.mff.peckam.java.origamist.jaxb;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.TreeMap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Templates;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.stream.StreamSource;

import org.xml.sax.SAXException;

/**
 * A combination of an XML schema catalogue, an XML transformations catalogue
 * and a holder for appropriately configured parser and transform factories.
 * <p>
 * 
 * This package is based on a XML Schema versioning system from http://www.funkypeople.biz/knowledge/JavaXml-v2.zip .
 * 
 * @author Sean Barnett
 * @author Martin Pecka
 */
public class BindingsManager
{

    /** The JAXB context to work with. */
    protected final JAXBContext                       jaxbContext;

    /**
     * The factory for creating {@link javax.xml.transform.sax.TransformerHandler} objects from
     * {@link javax.xml.transform.Transformer}s.
     */
    protected CustomTransformerFactory                transformerFactory;

    /** The factory for creating parsers. */
    protected SAXParserFactory                        parserFactory;

    /** A mapping from namespaces to corresponding schemata. */
    protected Map<String, SchemaInfo>                 schemaByNamespace         = new TreeMap<String, SchemaInfo>();

    /**
     * A mapping from a schema (identified by namespace) to a mapping of schemata it can be transformed to, the latter
     * mapping pointing to the {@link TransformInfo} needed for such transformation.
     */
    protected Map<String, Map<String, TransformInfo>> transformsByFromNamespace = new TreeMap<String, Map<String, TransformInfo>>();

    /**
     * A mapping from a schema (identified by namespace) to a callback that configures the {@link Unmarshaller} created
     * for that schema.
     */
    protected Map<String, UnmarshallerConfigurator>   unmarshallerConfigurators = new TreeMap<String, UnmarshallerConfigurator>();

    /**
     * Simple constructor.
     * 
     * @param jaxbContext The pre-created JAXB context. Caller should use either <code>new ObjectFactory()</code> -
     *            using ObjectFactory from the appropriate Java package, or more usually the
     *            {@link JAXBContext#newInstance(Class...)} method. Latter is more useful because it can accommodate
     *            multiple bound Java packages (as parameter to newInstance()).
     */
    public BindingsManager(JAXBContext jaxbContext)
    {
        this.jaxbContext = jaxbContext;

        transformerFactory = new CustomTransformerFactory();

        parserFactory = SAXParserFactory.newInstance();
        parserFactory.setNamespaceAware(true);
    }

    /**
     * @return The JAXBContext, as passed into constructor.
     */
    public JAXBContext getJaxbContext()
    {
        return jaxbContext;
    }

    /**
     * @return The factory for transformers.
     */
    public CustomTransformerFactory getTransformerFactory()
    {
        return transformerFactory;
    }

    /**
     * Lookup an XML Schema based upon its namespace.
     * 
     * @param namespace The namespace to find schema for. This namespace must have been added to BindingsManager using
     *            {@link addSchema} earlier.
     * 
     * @return The {@link SchemaInfo} for the given namespace, or <code>null</code> if no schema with the given
     *         namespace has been registered.
     */
    public SchemaInfo getSchema(String namespace)
    {
        return schemaByNamespace.get(namespace);
    }

    /**
     * Find a transform definition for moving from one XML schema to another. Such a transform must have been added
     * using {@link addTransform} earlier.
     * 
     * @param xmlNamespace The "target namespace" of the source XML schema.
     * @param javaNamespace The "target namespace" of the destination XML schema,
     * 
     * @return The transform for transition between schemata defined by the two namespaces.
     */
    public TransformInfo getTransform(String xmlNamespace, String javaNamespace)
    {
        // transforms are indexed first by source namespace and then by
        // destination namespace
        Map<String, TransformInfo> templatesByToNamespace = transformsByFromNamespace.get(xmlNamespace);
        if (templatesByToNamespace != null) {
            return templatesByToNamespace.get(javaNamespace);
        }

        return null;
    }

    /**
     * Add a schema definition to the bindings manager.
     * 
     * @param namespace The target namespace, effectively a primary key for the schema.
     * @param location The actual location of the schema as a Java resource (i.e., effectively a file that exists within
     *            the classpath somewhere)
     * @param bound If true indicates that JAXB bindings exist for this schema.
     * 
     * @return The created {@link SchemaInfo}.
     * 
     * @throws IOException If the schema could not be read from <code>location</code>.
     */
    public SchemaInfo addSchema(String namespace, String location, boolean bound) throws IOException
    {
        InputStream instream = getClass().getClassLoader().getResourceAsStream(location);
        if (instream == null)
            throw new IOException("Cannot open schema resource '" + location + "'");

        String schema = readToString(instream);

        SchemaInfo result = new SchemaInfo(namespace, location, schema, bound);
        schemaByNamespace.put(namespace, result);

        return result;
    }

    /**
     * Add a transform definition. The nominated XML transform is pre-compiled at this point.
     * 
     * @param fromSchema The schema the transform goes from.
     * @param toSchema The schema the transform goes to.
     * @param location The resource where the XML transform is located (i.e., effectively a file in the classpath).
     * @param parent The transform that should be applied before that at <code>location</code>.
     * 
     * @return The newly created {@link TransformInfo}.
     * 
     * @throws IOException If the transform cannot be read from <code>location</code>.
     * @throws TransformerConfigurationException If there are errors in the transform definition.
     */
    public TransformInfo addTransform(SchemaInfo fromSchema, SchemaInfo toSchema, String location, TransformInfo parent)
            throws IOException, TransformerConfigurationException
    {
        InputStream instream = getClass().getClassLoader().getResourceAsStream(location);
        if (instream == null)
            throw new IOException("Cannot open transform resource '" + location + "'");

        // pre-compile transform
        Templates templates = transformerFactory.newTemplates(new StreamSource(instream));

        TransformInfo result = new TransformInfo(parent, fromSchema, toSchema, templates);
        doAddTransform(result);

        return result;
    }

    /**
     * Add a transform definition.
     * 
     * @param fromSchema The schema the transform goes from.
     * @param toSchema The schema the transform goes to.
     * @param transform The transform to add.
     * 
     * @return <code>transform</code>
     */
    public TransformInfo addTransform(SchemaInfo fromSchema, SchemaInfo toSchema, TransformInfo transform)
    {
        doAddTransform(transform);

        return transform;
    }

    /**
     * Add a new transform that is a result of combining two previously defined transforms. For example, if transform X
     * goes from schema A to B, and transform Y goes from B to C, then newly created transform Z will go from schema A
     * to schema C.
     * 
     * @param first The first transform.
     * @param second The second transform.
     * 
     * @return The newly created transform.
     * 
     * @throws IllegalArgumentException If the target of the first transform is notthe source of the second transform.
     */
    public TransformInfo addTransform(TransformInfo first, TransformInfo second) throws IllegalArgumentException
    {
        if (!first.getToSchema().equals(second.getFromSchema()))
            throw new IllegalArgumentException("Two transforms are not additive");

        TransformInfo result;
        if (second.getTemplates() != null) {
            result = new TransformInfo(first, first.getFromSchema(), second.getToSchema(), second.getTemplates());
        } else {
            result = new TransformInfo(first, first.getFromSchema(), second.getToSchema(), second.getContentHandler());
        }
        doAddTransform(result);

        return result;
    }

    /**
     * Set the given unmarshaller configurater to be used for {@link Unmarshaller}s created for the given schema.
     * 
     * @param schema
     * @param configurator
     * 
     * @throws IllegalArgumentException If the given schema doesn't have a JAXB binding (
     *             <code>schema.isBound == false</code>).
     */
    public void addUnmarshallerConfigurator(SchemaInfo schema, UnmarshallerConfigurator configurator)
            throws IllegalArgumentException
    {
        if (!schema.isBound())
            throw new IllegalArgumentException(
                    "Cannot set a unmarshaller configurator for a schema which has no JAXB binding.");

        unmarshallerConfigurators.put(schema.getNamespace(), configurator);
    }

    /**
     * Create a SAX parser to process a generic XML file.
     * 
     * @return A SAX parser to process generic XML files.
     * 
     * @throws ParserConfigurationException If a parser cannot be created which satisfies the requested configuration.
     * @throws SAXException For SAX errors.
     */
    public SAXParser createParser() throws ParserConfigurationException, SAXException
    {
        return createParser(null);
    }

    /**
     * Create a SAX parser to process XML according to the given XML schema.
     * 
     * @param schema The schema to create parser for. If <code>null</code>, a generic XML parser is returned.
     * 
     * @return A SAX parser to process XML according to the given XML schema.
     * 
     * @throws ParserConfigurationException If a parser cannot be created which satisfies the requested configuration.
     * @throws SAXException For SAX errors.
     */
    public SAXParser createParser(SchemaInfo schema) throws ParserConfigurationException, SAXException
    {
        // yet we don't have any custom needs to differentiate the parsers according to their schemata
        return parserFactory.newSAXParser();
    }

    /**
     * Return an {@link Unmarshaller} able to unmarshal the given schema.
     * 
     * @param schema The schema to get unmarshaller for.
     * @return An {@link Unmarshaller} able to unmarshal the given schema.
     * 
     * @throws JAXBException If an error was encountered while creating the Unmarshaller object.
     */
    public Unmarshaller createUnmarshaller(SchemaInfo schema) throws JAXBException
    {
        Unmarshaller result = getJaxbContext().createUnmarshaller();

        UnmarshallerConfigurator configurator = unmarshallerConfigurators.get(schema.getNamespace());

        if (configurator != null) {
            configurator.configure(result);
        }

        return result;
    }

    /**
     * Index a transform, first by source XML schema and secondly by target XML schema.
     * 
     * @param transform The transform to index.
     */
    protected void doAddTransform(TransformInfo transform)
    {
        Map<String, TransformInfo> byToNamespace = transformsByFromNamespace.get(transform.getFromSchema()
                .getNamespace());
        if (byToNamespace == null) {
            byToNamespace = new TreeMap<String, TransformInfo>();
            transformsByFromNamespace.put(transform.getFromSchema().getNamespace(), byToNamespace);
        }

        byToNamespace.put(transform.getToSchema().getNamespace(), transform);
    }

    /**
     * Reads the contents of the input stream into a string.
     * 
     * This method doesn't close the stream.
     * 
     * @param instream The stream to read.
     * @return The string with all the contents of the stream loaded into it.
     * 
     * @throws IOException If the reading failed.
     */
    protected final String readToString(InputStream instream) throws IOException
    {
        StringBuffer result = new StringBuffer();
        byte[] buffer = new byte[4096];

        for (int didRead = 0; (didRead = instream.read(buffer)) >= 0;) {
            result.append(new String(buffer, 0, didRead));
        }

        return result.toString();
    }

}

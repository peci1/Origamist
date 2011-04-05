package cz.cuni.mff.peckam.java.origamist.jaxb;

import java.io.Reader;
import java.io.StringReader;

/**
 * Describes one known XML schema.
 * 
 * This package is based on a XML Schema versioning system from http://www.funkypeople.biz/knowledge/JavaXml-v2.zip .
 * 
 * @author Sean Barnett
 * @author Martin Pecka
 */
public class SchemaInfo
{
    /** The namespace the schema belongs to. */
    protected final String  namespace;

    /** The location of the schema as a resource location. */
    protected final String  location;

    /** The schema read into a string. */
    protected final String  schema;

    /** True if a JAXB mapping exists for this schema. */
    protected final boolean bound;

    /**
     * Create a schema info from the given information.
     * 
     * @param namespace The namespace the schema belongs to.
     * @param location The location of the schema as a resource location.
     * @param schema The schema read into a string.
     * @param bound True if a JAXB mapping exists for this schema.
     */
    public SchemaInfo(String namespace, String location, String schema, boolean bound)
    {
        this.namespace = namespace;
        this.location = location;
        this.schema = schema;
        this.bound = bound;
    }

    /**
     * @return The namespace which acts as primary key for this XML schema.
     */
    public String getNamespace()
    {
        return namespace;
    }

    /**
     * @return The definition of this XML Schema - this is cached to improve performance, but of course won't detect
     *         any schema changes.
     */
    public Reader getSchema()
    {
        return new StringReader(schema);
    }

    /**
     * @return The location of the schema as a resource location.
     */
    public String getLocation()
    {
        return location;
    }

    /**
     * @return True if a JAXB mapping exists for this schema.
     */
    public boolean isBound()
    {
        return bound;
    }
}
package cz.cuni.mff.peckam.java.origamist.jaxb;

import javax.xml.transform.Templates;

/**
 * Describes an XML transform - or series of XML transforms - that move us from one XML schema to another. The
 * sequence of transforms is realised by this TransformInfo having a parent (and so on recursively) - to get from
 * the "fromSchema" to the "toSchema" first apply all of the various parents' transforms and then this
 * TransformInfo's transform.
 * 
 * This package is based on a XML Schema versioning system from http://www.funkypeople.biz/knowledge/JavaXml-v2.zip .
 * 
 * @author Sean Barnett
 * @author Martin Pecka
 */
public class TransformInfo
{

    /** The transform to be executed before this one, or <code>null</code> if no such transform exists. */
    protected final TransformInfo      parent;

    /** The schema this transform transforms from. */
    protected final SchemaInfo         fromSchema;

    /** The schema this transform transforms to. */
    protected final SchemaInfo         toSchema;

    /**
     * The templates object used as a pre-compiled cache of the transform. Either contentHandler or templates will be
     * <code>null</code>.
     */
    protected Templates                templates      = null;

    /** A content handler that handles the transformation. Either contentHandler or templates will be <code>null</code>. */
    protected SAXOutputtingContentHandler contentHandler = null;

    /**
     * The number of overall transforms needed to be performed to get from <code>fromSchema</code> to
     * <code>toSchema</code> (including this one).
     */
    protected final int                depth;

    /**
     * Create a transform info defined by the given {@link Templates} transform.
     * 
     * @param parent The transform to be executed before this one, or <code>null</code> if no such transform exists.
     * @param fromSchema The schema this transform transforms from.
     * @param toSchema The schema this transform transforms to.
     * @param templates The templates object used as a pre-compiled cache of the transform.
     */
    public TransformInfo(TransformInfo parent, SchemaInfo fromSchema, SchemaInfo toSchema, Templates templates)
    {
        this.parent = parent;
        this.fromSchema = fromSchema;
        this.toSchema = toSchema;
        this.templates = templates;
        depth = (parent == null) ? 1 : parent.getDepth() + 1;
    }

    /**
     * Create a transform info defined by the given {@link SAXOutputtingContentHandler} content handler.
     * 
     * @param parent The transform to be executed before this one, or <code>null</code> if no such transform exists.
     * @param fromSchema The schema this transform transforms from.
     * @param toSchema The schema this transform transforms to.
     * @param handler A content handler that handles the transformation.
     */
    public TransformInfo(TransformInfo parent, SchemaInfo fromSchema, SchemaInfo toSchema,
            SAXOutputtingContentHandler handler)
    {
        this.parent = parent;
        this.fromSchema = fromSchema;
        this.toSchema = toSchema;
        this.contentHandler = handler;
        depth = (parent == null) ? 1 : parent.getDepth() + 1;
    }

    /**
     * @return The schema this transform transforms from.
     */
    public SchemaInfo getFromSchema()
    {
        return fromSchema;
    }

    /**
     * @return The schema this transform transforms to.
     */
    public SchemaInfo getToSchema()
    {
        return toSchema;
    }

    /**
     * @return The transform to be executed before this one, or <code>null</code> if no such transform exists.
     * 
     * @see TransformInfo
     */
    public TransformInfo getParent()
    {
        return parent;
    }

    /**
     * @return The templates object used as a pre-compiled cache of the transform. Either contentHandler or templates
     *         will be <code>null</code>.
     */
    public Templates getTemplates()
    {
        return templates;
    }

    /**
     * @return A content handler that handles the transformation. Either contentHandler or templates will be
     *         <code>null</code>.
     */
    public SAXOutputtingContentHandler getContentHandler()
    {
        return contentHandler;
    }

    /**
     * @return The number of overall transforms needed to be performed to get from <code>fromSchema</code> to
     *         <code>toSchema</code> (including this one).
     */
    public int getDepth()
    {
        return depth;
    }
}
/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.jaxb;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import cz.cuni.mff.peckam.java.origamist.jaxb.AdditionalTransforms.AdditionalTransform;

/**
 * A map of additional transforms.
 * 
 * @author Martin Pecka
 */
public class AdditionalTransforms extends HashMap<String, List<AdditionalTransform>>
{

    /** */
    private static final long serialVersionUID = -4282035140752973698L;

    /**
     * Add an additional transform that is to be applied in addition to the transforms fetched from
     * {@link BindingsManager}
     * 
     * @param transform The additional transform. The fromSchema and toSchema will be ignored.
     * @param location The location of the transform.
     * @param execIfSchemataEqual Whether to run this transform even if the XML's schema is in the newest version.
     * @param namespaces The list of source namespaces for which this transform has to be applied. A <code>null</code>
     *            namespace (or namespaces omitted at all) means this transform has to be executed for all source
     *            namespaces (beware: the setting of execIfSchemataEqual can disable the transform if the source
     *            namespace is equal to the target one).
     */
    public void add(TransformInfo transform, TransformLocation location, boolean execIfSchemataEqual,
            String... namespaces)
    {
        AdditionalTransform item = new AdditionalTransform(transform, location, execIfSchemataEqual);

        String[] namespaces2 = namespaces;
        if (namespaces2 == null)
            namespaces2 = new String[] { null };

        for (String namespace : namespaces) {
            List<AdditionalTransform> transforms = get(namespace);
            if (transforms == null) {
                put(namespace, new LinkedList<AdditionalTransform>());
                transforms = get(namespace);
            }
            transforms.add(item);
        }
    }

    /**
     * Get the transforms that are to be applied to the schema with the given namespace.
     * 
     * It holds that "start _join_ beforeUnmarshaller _join_ end == the_returned_list".
     * 
     * @param namespace The source namespace to get additional transforms for.
     * @param equalNamespaces If true, suppose that namespace is also the target namespace.
     * @param start List of transforms to be added before the classic ones.
     * @param beforeUnmarshaller List of transforms to be added before the unmarshaller.
     * @param end List of transforms to be added after the unmarshaller.
     * @return The transforms that are to be applied to the schema with the given namespace. If no transform is to be
     *         applied, an empty list will be returned.
     */
    protected List<AdditionalTransform> getTransforms(String namespace, boolean equalNamespaces,
            List<AdditionalTransform> start, List<AdditionalTransform> beforeUnmarshaller, List<AdditionalTransform> end)
    {
        List<AdditionalTransform> nullTransforms = get(null);
        List<AdditionalTransform> namespaceTransforms = get(namespace);

        if (nullTransforms == null)
            return namespaceTransforms == null ? new LinkedList<AdditionalTransform>() : namespaceTransforms;

        if (namespaceTransforms == null)
            return nullTransforms;

        List<AdditionalTransform> result = new LinkedList<AdditionalTransform>(nullTransforms);
        result.addAll(namespaceTransforms);

        for (Iterator<AdditionalTransform> it = result.iterator(); it.hasNext();) {
            AdditionalTransform transform = it.next();
            if (equalNamespaces && !transform.execIfSchemataEqual()) {
                it.remove();
                continue;
            }
            switch (transform.getLocation()) {
                case START:
                    if (start != null)
                        start.add(transform);
                    break;
                case BEFORE_UNMARSHALLER:
                    if (beforeUnmarshaller != null)
                        beforeUnmarshaller.add(transform);
                    break;
                case END:
                    if (end != null)
                        end.add(transform);
                    break;
            }
        }

        return result;
    }

    /**
     * Get the transforms that are to be applied to the schema with the given namespace.
     * 
     * @param namespace The source namespace to get additional transforms for.
     * @return The transforms that are to be applied to the schema with the given namespace. If no transform is to be
     *         applied, an empty list will be returned.
     */
    public List<AdditionalTransform> getTransforms(String namespace)
    {
        return getTransforms(namespace, false, null, null, null);
    }

    /**
     * An additional transform that has to be executed in addition to the transforms fetched from
     * {@link BindingsManager}.
     * 
     * @author Martin Pecka
     */
    class AdditionalTransform
    {
        /** The additional transform. */
        protected TransformInfo     transform;
        /** The location of the transform. */
        protected TransformLocation location;
        /** Whether to run this transform even if the XML's schema is in the newest version. */
        protected boolean           execIfSchemataEqual;

        /**
         * @param transform The additional transform.
         * @param location The location of the transform.
         * @param execIfSchemataEqual Whether to run this transform even if the XML's schema is in the newest version.
         */
        public AdditionalTransform(TransformInfo transform, TransformLocation location, boolean execIfSchemataEqual)
        {
            this.transform = transform;
            this.location = location;
            this.execIfSchemataEqual = execIfSchemataEqual;
        }

        /**
         * @return The additional transform.
         */
        public TransformInfo getTransform()
        {
            return transform;
        }

        /**
         * @return The location of the transform.
         */
        public TransformLocation getLocation()
        {
            return location;
        }

        /**
         * @return Whether to run this transform even if the XML's schema is in the newest version.
         */
        public boolean execIfSchemataEqual()
        {
            return execIfSchemataEqual;
        }
    }

    /**
     * The location where the additional transform should be placed in the execution string.
     * 
     * @author Martin Pecka
     */
    public enum TransformLocation
    {
        /** Perform the transform as the very first. */
        START,

        /** Perform the transform as the very last (after the unmarshaller does its work). */
        END,

        /** Perform the transform as the last, but before the unmarshaller does its work. */
        BEFORE_UNMARSHALLER
    }

}

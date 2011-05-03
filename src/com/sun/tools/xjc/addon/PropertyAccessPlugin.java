/**
 * 
 */
package com.sun.tools.xjc.addon;

import java.io.StringWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttachmentRef;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlInlineBinaryData;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlMimeType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import com.sun.codemodel.JAnnotationUse;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JFieldVar;
import com.sun.codemodel.JFormatter;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JVar;
import com.sun.tools.xjc.Options;
import com.sun.tools.xjc.Plugin;
import com.sun.tools.xjc.model.CPluginCustomization;
import com.sun.tools.xjc.outline.ClassOutline;
import com.sun.tools.xjc.outline.FieldOutline;
import com.sun.tools.xjc.outline.Outline;

/**
 * XJC plugin that forces XJC to generate classes that use PROPERTY access (intead of the default FIELD access). This
 * means that setters are triggered during unmarshalling and getters are triggered during marshalling.
 * <p>
 * Usage in schema:<br>
 * Add <code>&lt;http://www.mff.cuni.cz/~peckam/java/origamist/jaxb/plugins:property-access></code> either in global or
 * class' annotiation's appinfo.
 * 
 * @author Martin Pecka
 */
public class PropertyAccessPlugin extends Plugin
{

    public static final String NAMESPACE = "http://www.mff.cuni.cz/~peckam/java/origamist/jaxb/plugins";

    @Override
    public String getOptionName()
    {
        return "Xproperty-access";
    }

    @Override
    public String getUsage()
    {
        return "  -Xproperty-access: use PROPERTY accessors in generated classes";
    }

    @Override
    public List<String> getCustomizationURIs()
    {
        return Collections.singletonList(NAMESPACE);
    }

    @Override
    public boolean isCustomizationTagName(String nsUri, String localName)
    {
        return nsUri.equals(NAMESPACE) && localName.equals("property-access");
    }

    @Override
    public boolean run(Outline model, Options opt, ErrorHandler errorHandler) throws SAXException
    {
        CPluginCustomization pluginCust = model.getModel().getCustomizations().find(NAMESPACE, "property-access");
        boolean globalSet = false;
        if (pluginCust != null) {
            pluginCust.markAsAcknowledged();
            globalSet = true;
        }

        for (ClassOutline co : model.getClasses()) {
            CPluginCustomization classCust = co.target.getCustomizations().find(NAMESPACE, "property-access");

            boolean classSet = false;
            if (classCust != null) {
                classCust.markAsAcknowledged();
                classSet = true;
            }

            if (!(globalSet || classSet))
                continue;

            JAnnotationUse classAnnot = removeAnnotation(co.implClass, JDefinedClass.class, XmlAccessorType.class);
            if (classAnnot == null)
                continue;

            co.implClass.annotate(XmlAccessorType.class).param("value", XmlAccessType.PROPERTY);

            for (FieldOutline fo : co.getDeclaredFields()) {
                JFieldVar field = co.implClass.fields().get(fo.getPropertyInfo().getName(false));
                if (field == null)
                    continue;

                String getterName = "get" + field.name().substring(0, 1).toUpperCase() + field.name().substring(1);
                String setterName = "set" + field.name().substring(0, 1).toUpperCase() + field.name().substring(1);

                JMethod method = null;
                Collection<JMethod> methods = co.implClass.methods();
                for (Iterator<JMethod> it = methods.iterator(); it.hasNext();) {
                    JMethod meth = it.next();
                    if (meth.name().equals(getterName)) {
                        method = meth;
                        break;
                    }
                }
                // we could test for setter in the previous loop, but we'll prefer to annotate the getter if it exists
                // (anyways, it should exist)
                if (method == null) {
                    for (Iterator<JMethod> it = methods.iterator(); it.hasNext();) {
                        JMethod meth = it.next();
                        if (meth.name().equals(setterName)) {
                            method = meth;
                            break;
                        }
                    }
                }

                if (method == null)
                    continue;

                JAnnotationUse fieldAnnot = removeAnnotation(field, JVar.class, XmlElement.class);
                if (fieldAnnot == null) {
                    fieldAnnot = removeAnnotation(field, JVar.class, XmlAttribute.class);
                    if (fieldAnnot == null) {
                        fieldAnnot = removeAnnotation(field, JVar.class, XmlValue.class);
                        if (fieldAnnot == null)
                            continue;
                    }
                }

                List<JAnnotationUse> fieldAnnots = new LinkedList<JAnnotationUse>();
                fieldAnnots.add(fieldAnnot);

                List<Class<? extends Annotation>> annotsToMoveWith = new LinkedList<Class<? extends Annotation>>() {
                    /** */
                    private static final long serialVersionUID = 2422879147478430001L;
                    {
                        add(XmlElements.class);
                        add(XmlID.class);
                        add(XmlIDREF.class);
                        add(XmlList.class);
                        add(XmlSchemaType.class);
                        add(XmlValue.class);
                        add(XmlAttachmentRef.class);
                        add(XmlMimeType.class);
                        add(XmlInlineBinaryData.class);
                        add(XmlElementWrapper.class);
                        add(XmlJavaTypeAdapter.class);
                    }
                };

                for (Class<? extends Annotation> annotClass : annotsToMoveWith) {
                    JAnnotationUse annot = removeAnnotation(field, JVar.class, annotClass);
                    if (annot != null)
                        fieldAnnots.add(annot);

                }

                List<JAnnotationUse> methodAnnots = getAnnotations(method, JMethod.class);
                if (methodAnnots == null) {
                    // initialize the inner annotations array
                    method.annotate(XmlElement.class);
                    removeAnnotation(method, JMethod.class, XmlElement.class);
                    methodAnnots = getAnnotations(method, JMethod.class);
                }

                if (methodAnnots == null) {
                    errorHandler.warning(new SAXParseException("Couldn't create the list of annotations for method "
                            + method.name(), null));
                    continue;
                }

                methodAnnots.addAll(fieldAnnots);
            }
        }
        return true;

    }

    /**
     * Uses reflection to get the value of the field <code>field</code> of <code>obj</code>.
     * 
     * @param field The name of the field to get.
     * @param obj The object we get a field of.
     * @param clazz The class containing the field (exactly that class, not a subclass or so!).
     */
    protected Object getValueWithReflection(String field, Object obj, Class<?> clazz)
    {
        Field[] fields = clazz.getDeclaredFields();
        for (Field f : fields) {
            if (!field.equals(f.getName()))
                continue;
            f.setAccessible(true);
            try {
                return f.get(obj);
            } catch (IllegalArgumentException e) {
                System.err.println(e);
            } catch (IllegalAccessException e) {
                System.err.println(e);
            }
        }
        return null;
    }

    protected <T> JAnnotationUse removeAnnotation(T object, Class<? super T> annotationsContainingClass,
            Class<? extends Annotation> annotation)
    {
        List<JAnnotationUse> annots = getAnnotations(object, annotationsContainingClass);
        if (annots != null) {
            for (Iterator<JAnnotationUse> it = annots.iterator(); it.hasNext();) {
                JAnnotationUse au = it.next();
                StringWriter w = new StringWriter();
                au.generate(new JFormatter(w));
                if (w.toString().startsWith("@" + annotation.getName())) {
                    it.remove();
                    return au;
                }
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    protected <T> List<JAnnotationUse> getAnnotations(T object, Class<? super T> annotationsContainingClass)
    {
        return (List<JAnnotationUse>) getValueWithReflection("annotations", object, annotationsContainingClass);
    }
}

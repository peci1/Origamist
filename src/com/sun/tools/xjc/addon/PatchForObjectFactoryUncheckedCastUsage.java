/**
 * 
 */
package com.sun.tools.xjc.addon;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;

import com.sun.codemodel.JAnnotationArrayMember;
import com.sun.codemodel.JAnnotationUse;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JMethod;
import com.sun.tools.xjc.Options;
import com.sun.tools.xjc.Plugin;
import com.sun.tools.xjc.outline.Outline;
import com.sun.tools.xjc.outline.PackageOutline;

/**
 * XJC plugin (to be accurate, bugfix). Sometimes the created Object factory uses cast to raw Class class, which
 * issues a warning. This patch adds @SupressWarnings("rawtypes", "unchecked") to the createElement factory.
 * 
 * @author Martin Pecka
 */
public class PatchForObjectFactoryUncheckedCastUsage extends Plugin
{

    @Override
    public String getOptionName()
    {
        return "Xpatch-objFactory-uncheckedCast";
    }

    @Override
    public String getUsage()
    {
        return "  -Xpatch-objFactory-uncheckedCast: add @SupressWarnings(\"rawtypes\", \"unchecked\") to createElement() method in ObjectFactory";
    }

    @Override
    public List<String> getCustomizationURIs()
    {
        return Collections.singletonList("http://www.mff.cuni.cz/~peckam/java/origamist/jaxb/plugins/patch");
    }

    @Override
    public boolean run(Outline model, Options opt, ErrorHandler errorHandler) throws SAXException
    {

        Iterator<? extends PackageOutline> i = model.getAllPackageContexts().iterator();
        while (i.hasNext()) {
            PackageOutline pack = i.next();
            JDefinedClass factory = pack.objectFactory();
            // iterate through all methods of the object factory
            outer: for (JMethod m : factory.methods()) {
                @SuppressWarnings("unchecked")
                List<JAnnotationUse> annots = (List<JAnnotationUse>) getValueWithReflection("annotations", m,
                        JMethod.class);
                if (annots == null)
                    continue;
                boolean foundXmlElementDeclAnnot = false;
                // search for @XmlElementDecl annotation
                for (JAnnotationUse annot : annots) {
                    JClass annotClass = (JClass) getValueWithReflection("clazz", annot, JAnnotationUse.class);
                    if ("XmlElementDecl".equals(annotClass.name())) {
                        foundXmlElementDeclAnnot = true;
                        break;
                    } else {
                        continue outer;
                    }
                }
                if (!foundXmlElementDeclAnnot)
                    continue;

                // if this method has the @XmlElementDecl annotation, we want to add the @SuppressWarnings annotation.
                JAnnotationUse annot = m.annotate(SuppressWarnings.class);
                JAnnotationArrayMember params = annot.paramArray("value");
                params.param("rawtypes");
                params.param("unchecked");
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
}

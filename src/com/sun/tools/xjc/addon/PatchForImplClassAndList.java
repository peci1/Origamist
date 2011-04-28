/**
 * 
 */
package com.sun.tools.xjc.addon;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;

import com.sun.codemodel.JBlock;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JConditional;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JFieldVar;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JStatement;
import com.sun.codemodel.JVar;
import com.sun.tools.xjc.Options;
import com.sun.tools.xjc.Plugin;
import com.sun.tools.xjc.outline.ClassOutline;
import com.sun.tools.xjc.outline.FieldOutline;
import com.sun.tools.xjc.outline.Outline;

/**
 * XJC plugin (to be accurate, bugfix). If you define a type with implClass and use it in the schema in such way, that
 * XJC generates a list of these elements, then XJC originally uses the generated class and not the implClass as the
 * type of elements in the list. This plugin fixes it. Also this tells XJC to use the user-defined implClass in
 * generated getters and setters.
 * 
 * @author Martin Pecka
 */
public class PatchForImplClassAndList extends Plugin
{

    @Override
    public String getOptionName()
    {
        return "Xpatch-implClass-list";
    }

    @Override
    public String getUsage()
    {
        return "  -Xpatch-implClass-list: generate lists using the defined implClass of its items";
    }

    @Override
    public List<String> getCustomizationURIs()
    {
        return Collections.singletonList("http://www.mff.cuni.cz/~peckam/java/origamist/jaxb/plugins/patch");
    }

    @Override
    public boolean run(Outline model, Options opt, ErrorHandler errorHandler) throws SAXException
    {
        // collect classes that have a user-defined implementation class
        Hashtable<String, String> elementsWithImplClassSet = new Hashtable<String, String>();
        for (ClassOutline co : model.getClasses()) {
            if (co.target.getUserSpecifiedImplClass() != null)
                elementsWithImplClassSet.put(co.implClass.fullName(), co.target.getUserSpecifiedImplClass());
        }

        for (ClassOutline co : model.getClasses()) {
            for (FieldOutline fo : co.getDeclaredFields()) {
                JClass clazz = fo.getRawType().boxify();
                String implClassName = elementsWithImplClassSet.get(clazz.fullName()); // name of the user-defined class
                JClass implClass;
                JClass fieldClass;
                boolean isList;
                if (implClassName != null) {
                    // not a field with implClass resulting in a List
                    implClass = model.getCodeModel().ref(implClassName);
                    fieldClass = implClass;
                    isList = false;
                } else if ("java.util.List".equals(fo.getRawType().erasure().fullName())) {
                    // a field with implClass resulting in a list
                    List<JClass> parameters = ((JClass) fo.getRawType()).getTypeParameters();

                    if (parameters == null)
                        continue;
                    // we only expect List<E>
                    if (parameters.size() > 1 || parameters.size() == 0)
                        continue;

                    JClass c = parameters.get(0);
                    implClassName = elementsWithImplClassSet.get(c.fullName()); // name of the user-defined class
                    if (implClassName == null)
                        continue;

                    implClass = model.getCodeModel().ref(implClassName);
                    // a java.util.List with the corect type argument
                    fieldClass = ((JClass) fo.getRawType().erasure()).narrow(implClass);
                    isList = true;
                } else {
                    // this is nor a field with implClass nor a list field with implClass
                    continue;
                }

                JFieldVar field = co.implClass.fields().get(fo.getPropertyInfo().getName(false));

                String getterName = "get" + field.name().substring(0, 1).toUpperCase() + field.name().substring(1);
                String setterName = "set" + field.name().substring(0, 1).toUpperCase() + field.name().substring(1);

                JClass listImplClass = null;
                JClass newListImplClass = null;

                if (isList) {
                    listImplClass = (JClass) getValueWithReflection("coreList", fo, fo.getClass());
                    assert listImplClass != null;
                    newListImplClass = listImplClass.erasure().narrow(implClass);

                    // remove the old invalid field
                    co.implClass.removeField(field);

                    // replace the old field with the correct one
                    JFieldVar newField = co.implClass.field(field.mods().getValue(), fieldClass, field.name(),
                            JExpr._new(newListImplClass));
                    copyFieldValueWithReflection("annotations", field, newField, field.getClass().getSuperclass());
                    copyFieldValueWithReflection("jdoc", field, newField, field.getClass());
                    field = newField;
                }

                // update getter for the list (no setter is created) getter/setter arguments types
                for (JMethod m : co.implClass.methods()) {
                    if (getterName.equals(m.name())) {
                        // change the return type of the method
                        m.type(fieldClass);

                        if (isList) {
                            assert newListImplClass != null;
                            JBlock body = m.body();
                            JBlock newBody = new JBlock(true, true);
                            for (Object o : body.getContents()) {
                                if (o instanceof JConditional) {
                                    JConditional cond = newBody._if(field.eq(JExpr._null()));
                                    JBlock thenBlock = cond._then();
                                    thenBlock.assign(field, JExpr._new(newListImplClass));
                                } else if (o instanceof JStatement) {
                                    newBody.add((JStatement) o);
                                }
                            }
                            setValueWithReflection("body", m, m.getClass(), newBody);
                        }

                        // change the javadoc to reflect the new reality
                        if (m.javadoc().size() >= 3) {
                            m.javadoc().remove(m.javadoc().size() - 1);
                            m.javadoc().remove(m.javadoc().size() - 1);
                            m.javadoc().remove(m.javadoc().size() - 1);

                            if (implClass != fieldClass) {
                                m.javadoc().append("<p>Objects of the following type(s) are allowed in the list: ");
                            } else {
                                m.javadoc().append("Possible object is: ");
                            }
                            m.javadoc().append(implClass);
                        }
                    } else if (setterName.equals(m.name())) {
                        setValueWithReflection("params", m, m.getClass(), new ArrayList<JVar>());
                        m.param(fieldClass, "value");

                        // change the javadoc to reflect the new reality
                        if (m.javadoc().size() >= 3) {
                            m.javadoc().remove(m.javadoc().size() - 1);
                            m.javadoc().remove(m.javadoc().size() - 1);
                            m.javadoc().remove(m.javadoc().size() - 1);

                            m.javadoc().append("Allowed object is: ");
                            m.javadoc().append(fieldClass);
                        }
                    }

                }
            }
        }

        // BEWARE: uncommenting the next lines would be nice (object factories would return elements of implClass type),
        // but JAXB cannot handle that and unmarshalling ens up with a ClassCastException.

        // Set<String> oldClasses = elementsWithImplClassSet.keySet();
        // for (PackageOutline pack : model.getAllPackageContexts()) {
        // JDefinedClass factory = pack.objectFactory();
        // for (JMethod m : factory.methods()) {
        // if (oldClasses.contains(m.type().fullName())) {
        // m.type(model.getCodeModel().ref(elementsWithImplClassSet.get(m.type().fullName())));
        // }
        // }
        // }

        return true;

    }

    /**
     * Uses reflection to copy value of the field <code>field</code> from <code>oldObj</code> to <code>newObj</code>.
     * 
     * @param field The name of the field to copy.
     * @param oldObj The source object.
     * @param newObj The target object.
     * @param clazz The class containing the field (exactly that class, not a subclass or so!).
     */
    protected void copyFieldValueWithReflection(String field, Object oldObj, Object newObj, Class<?> clazz)
    {
        Field[] fields = clazz.getDeclaredFields();
        for (Field f : fields) {
            if (!field.equals(f.getName()))
                continue;
            f.setAccessible(true);
            try {
                f.set(newObj, f.get(oldObj));
            } catch (IllegalArgumentException e) {
                System.err.println(e);
            } catch (IllegalAccessException e) {
                System.err.println(e);
            }
        }
    }

    /**
     * Uses reflection to set the value of the field <code>field</code> of <code>obj</code> to <code>value</code>.
     * 
     * @param field The name of the field to copy.
     * @param obj The object we set a field of.
     * @param clazz The class containing the field (exactly that class, not a subclass or so!).
     * @param value The value to set.
     */
    protected void setValueWithReflection(String field, Object obj, Class<?> clazz, Object value)
    {
        Field[] fields = clazz.getDeclaredFields();
        for (Field f : fields) {
            if (!field.equals(f.getName()))
                continue;
            f.setAccessible(true);
            try {
                f.set(obj, value);
            } catch (IllegalArgumentException e) {
                System.err.println(e);
            } catch (IllegalAccessException e) {
                System.err.println(e);
            }
        }
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

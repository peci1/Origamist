/**
 * 
 */
package com.sun.tools.xjc.addon;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;

import com.sun.codemodel.JBlock;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JFieldVar;
import com.sun.codemodel.JInvocation;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JType;
import com.sun.tools.xjc.BadCommandLineException;
import com.sun.tools.xjc.Options;
import com.sun.tools.xjc.Plugin;
import com.sun.tools.xjc.outline.ClassOutline;
import com.sun.tools.xjc.outline.FieldOutline;
import com.sun.tools.xjc.outline.Outline;

/**
 * This plugin adds the function getListFields(), which returns an array of all list fields a generated class defines.
 * Optionally it can define String properties for list fields and generate a method returning the list of the names of
 * the properties.
 * 
 * @author Martin Pecka
 */
public class EnumerateListFieldsPlugin extends Plugin
{

    /** Whether to generate property Strings. */
    protected boolean generateProperties  = false;
    /** Whether to generate getListProperties() method. */
    protected boolean enumerateProperties = false;

    @Override
    public String getOptionName()
    {
        return "Xenumerate-list-fields";
    }

    @Override
    public String getUsage()
    {
        return "  -Xenumerate-list-fields:\tadd function getListFields(), which returns an array of all list fields a generated class (and its generated superclasses) defines"
                + "  -Xenumerate-list-fields-generate-properties:\tgenerate a String property for each list field"
                + "  -Xenumerate-list-fields-enumerate-properties:\tadd function getListProperties(), which returns an array of property Strings of all list fields a generated class (and its generated superclasses) defines; requires -Xenumerate-list-fields-generate-properties to be specified.";
    }

    @Override
    public List<String> getCustomizationURIs()
    {
        return Collections.singletonList("http://www.mff.cuni.cz/~peckam/java/origamist/jaxb/plugins");
    }

    @Override
    public int parseArgument(Options opt, String[] args, int i) throws BadCommandLineException, IOException
    {
        int superRes = super.parseArgument(opt, args, i);
        if (superRes > 0)
            return superRes;

        String arg = args[i];
        if (arg.equals("-Xenumerate-list-fields-generate-properties")) {
            generateProperties = true;
            return 1;
        } else if (arg.equals("-Xenumerate-list-fields-enumerate-properties")) {
            enumerateProperties = true;
            return 1;
        }
        return 0;
    }

    @Override
    public boolean run(Outline model, Options opt, ErrorHandler errorHandler) throws SAXException
    {
        if (enumerateProperties && !generateProperties) {
            throw new SAXException("Option -Xenumerate-list-fields-enumerate-properties requires option "
                    + "-Xenumerate-list-fields-generate-properties to be specified, but it wasn't.");
        }

        JType returnType = model.getCodeModel().ref(List.class).erasure()
                .narrow(model.getCodeModel().ref(Object.class).wildcard()).array();
        JType propertyReturnType = model.getCodeModel().ref(String.class).array();
        for (ClassOutline co : model.getClasses()) {
            List<FieldOutline> listFields = new LinkedList<FieldOutline>();
            List<FieldOutline> listFieldsDeclaredInThisClass = new LinkedList<FieldOutline>();
            ClassOutline clazz = co;
            while (clazz != null) {
                for (FieldOutline fo : clazz.getDeclaredFields()) {
                    if (fo.getRawType().erasure() instanceof JClass
                            && model.getCodeModel().ref(List.class)
                                    .isAssignableFrom((JClass) fo.getRawType().erasure())) {
                        listFields.add(fo);
                        if (co == clazz)
                            listFieldsDeclaredInThisClass.add(fo);
                    }
                }
                clazz = clazz.getSuperClass();
            }

            if (generateProperties) {
                for (FieldOutline fo : listFieldsDeclaredInThisClass) {
                    String propertyConstantName = getPropertyName(fo);
                    JFieldVar propertyConstant = co.implClass.field(JMod.PUBLIC | JMod.STATIC | JMod.FINAL,
                            String.class, propertyConstantName,
                            JExpr.lit(fo.getPropertyInfo().getName(false) + ":" + co.implClass.fullName()));
                    propertyConstant.javadoc().clear();
                    propertyConstant.javadoc().add("Property " + fo.getPropertyInfo().getName(false));
                }
            }

            if (listFields.size() > 0) {
                JMethod method = co.implClass.method(JMod.PUBLIC, returnType, "getListFields");
                JBlock body = method.body();
                JInvocation array = JExpr._new(returnType);
                for (FieldOutline fo : listFields) {
                    String fieldName = fo.getPropertyInfo().getName(false);
                    // we need to call the list getter to be 100% sure the list field will be properly initialized
                    // if we used the field, calling this method from a super constructor will result in initializing
                    // the list with null, which we don't want
                    String getterName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                    array.arg(JExpr.invoke(getterName));
                }
                body._return(array);
                method.javadoc().addReturn()
                        .add("Return an array of all list fields defined on this class and its superclasses.");

                if (enumerateProperties) {
                    method = co.implClass.method(JMod.PUBLIC, propertyReturnType, "getListProperties");
                    body = method.body();
                    array = JExpr._new(propertyReturnType);
                    for (FieldOutline fo : listFields) {
                        String propertyName = getPropertyName(fo);
                        array.arg(JExpr.ref(propertyName));
                    }
                    body._return(array);
                    method.javadoc()
                            .addReturn()
                            .add("Return an array of property names of all list fields defined on this class and its "
                                    + "superclasses. The order of the property names corresponds with the order of getListFields().");
                }
            }
        }

        return true;
    }

    /**
     * Return the name of the property constant for the given field.
     * 
     * @param fo The field to get the property name of.
     * @return The name of the property constant for the given field.
     */
    protected String getPropertyName(FieldOutline fo)
    {
        return fo.getPropertyInfo().getName(false).replaceAll("([A-Z])", "_$1").toUpperCase() + "_PROPERTY";
    }
}

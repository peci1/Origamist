/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License). You may not use this file except in
 * compliance with the License.
 * 
 * You can obtain a copy of the license at
 * https://glassfish.dev.java.net/public/CDDLv1.0.html or
 * glassfish/bootstrap/legal/CDDLv1.0.txt.
 * See the License for the specific language governing
 * permissions and limitations under the License.
 * 
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at glassfish/bootstrap/legal/CDDLv1.0.txt.
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * you own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 * 
 * Copyright 2006 Sun Microsystems, Inc. All rights reserved.
 */

package com.sun.tools.xjc.addon.property_listener_injector;

import static com.sun.tools.xjc.outline.Aspect.IMPLEMENTATION;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.xml.bind.annotation.W3CDomHandler;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlMixed;
import javax.xml.bind.annotation.XmlNsForm;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.namespace.QName;

import com.sun.codemodel.JAnnotatable;
import com.sun.codemodel.JBlock;
import com.sun.codemodel.JCatchBlock;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JConditional;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JFieldRef;
import com.sun.codemodel.JFieldVar;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JTryBlock;
import com.sun.codemodel.JType;
import com.sun.codemodel.JVar;
import com.sun.tools.xjc.generator.annotation.spec.XmlAnyElementWriter;
import com.sun.tools.xjc.generator.annotation.spec.XmlAttributeWriter;
import com.sun.tools.xjc.generator.annotation.spec.XmlElementRefWriter;
import com.sun.tools.xjc.generator.annotation.spec.XmlElementRefsWriter;
import com.sun.tools.xjc.generator.annotation.spec.XmlElementWriter;
import com.sun.tools.xjc.generator.annotation.spec.XmlElementsWriter;
import com.sun.tools.xjc.generator.bean.ClassOutlineImpl;
import com.sun.tools.xjc.generator.bean.MethodWriter;
import com.sun.tools.xjc.model.CAttributePropertyInfo;
import com.sun.tools.xjc.model.CCustomizations;
import com.sun.tools.xjc.model.CElement;
import com.sun.tools.xjc.model.CElementInfo;
import com.sun.tools.xjc.model.CElementPropertyInfo;
import com.sun.tools.xjc.model.CPluginCustomization;
import com.sun.tools.xjc.model.CPropertyInfo;
import com.sun.tools.xjc.model.CReferencePropertyInfo;
import com.sun.tools.xjc.model.CTypeInfo;
import com.sun.tools.xjc.model.CTypeRef;
import com.sun.tools.xjc.model.CValuePropertyInfo;
import com.sun.tools.xjc.model.nav.NClass;
import com.sun.tools.xjc.outline.Aspect;
import com.sun.tools.xjc.outline.ClassOutline;
import com.sun.tools.xjc.outline.FieldAccessor;
import com.sun.tools.xjc.outline.FieldOutline;
import com.sun.tools.xjc.reader.TypeUtil;
import com.sun.tools.xjc.util.DOMUtils;
import com.sun.xml.bind.api.impl.NameConverter;

/**
 * Realizes a property through one getter and one setter.
 * This renders:
 * 
 * <pre>
 * T' field;
 * T getXXX() { ... }
 * void setXXX(T value) { ... }
 * </pre>
 * 
 * <p>
 * Normally T'=T, but under some tricky circumstances they could be different (like T'=Integer, T=int.)
 * 
 * This realization is only applicable to fields with (1,1) or (0,1) multiplicity.
 * 
 * @author Jerome Dochez
 * @author Martin Pecka
 */
public class SingleField implements FieldOutline
{

    protected final ClassOutlineImpl outline;
    protected final CPropertyInfo    prop;
    protected final JType            implType;
    protected final JType            exposedType;
    protected final JCodeModel       codeModel;

    private enum Kind
    {
        BOUND, VETOABLE
    }

    /**
     * Field declaration of the actual list object that we use
     * to store data.
     */
    private JFieldVar field;

    public SingleField(ClassOutlineImpl context, CPropertyInfo prop)
    {
        this(context, prop, false);
    }

    /**
     * 
     * @param forcePrimitiveAccess
     *            forces the setter/getter to expose the primitive type.
     *            it's a pointless customization, but it's nevertheless in the spec.
     */
    public SingleField(ClassOutlineImpl context, CPropertyInfo prop, boolean forcePrimitiveAccess)
    {

        outline = context;
        this.prop = prop;

        Kind boundType = null;

        implType = getType(IMPLEMENTATION);
        exposedType = getType(Aspect.EXPOSED);
        codeModel = outline.parent().getCodeModel();

        String interfaceName = null;

        CCustomizations customizations = prop.getCustomizations();
        CPluginCustomization pluginCust = customizations.find(Const.NS, "listener");

        if (pluginCust != null) {
            interfaceName = DOMUtils.getElementText(pluginCust.element);
            if (interfaceName.equals("java.beans.PropertyChangeListener")) {
                boundType = Kind.BOUND;
            } else if (interfaceName.equals("java.beans.VetoableChangeListener")) {
                boundType = Kind.VETOABLE;
            } else {
                boundType = null;
            }
            pluginCust.markAsAcknowledged();
        } else {
            customizations = context.target.getCustomizations();
            pluginCust = customizations.find(Const.NS, "listener");

            if (pluginCust != null) {
                interfaceName = DOMUtils.getElementText(pluginCust.element);
                if (interfaceName.equals("java.beans.PropertyChangeListener")) {
                    boundType = Kind.BOUND;
                } else if (interfaceName.equals("java.beans.VetoableChangeListener")) {
                    boundType = Kind.VETOABLE;
                } else {
                    boundType = null;
                }
                pluginCust.markAsAcknowledged();
            } else {
                customizations = context.parent().getModel().getCustomizations();
                pluginCust = customizations.find(Const.NS, "listener");

                if (pluginCust != null) {
                    interfaceName = DOMUtils.getElementText(pluginCust.element);
                    if (interfaceName.equals("java.beans.PropertyChangeListener")) {
                        boundType = Kind.BOUND;
                    } else if (interfaceName.equals("java.beans.VetoableChangeListener")) {
                        boundType = Kind.VETOABLE;
                    } else {
                        boundType = null;
                    }
                    pluginCust.markAsAcknowledged();
                }
            }
        }

        assert !exposedType.isPrimitive() && !implType.isPrimitive();

        // create the field
        field = outline.implClass.field(JMod.PROTECTED, getFieldType(), prop.getName(false));
        annotate(field);

        JFieldVar propertyConstant = null;
        if (boundType != null) {
            // the constant for the property
            String propertyConstantName = prop.getName(false).replaceAll("([A-Z])", "_$1").toUpperCase() + "_PROPERTY";
            propertyConstant = outline.implClass.field(JMod.PUBLIC | JMod.STATIC | JMod.FINAL, String.class,
                    propertyConstantName, JExpr.lit(prop.getName(false)));
            propertyConstant.javadoc().clear();
            propertyConstant.javadoc().add("Property " + prop.getName(false));
        }

        MethodWriter writer = context.createMethodWriter();
        NameConverter nc = context.parent().getModel().getNameConverter();

        // [RESULT]
        // Type getXXX() {
        // #ifdef default value
        // if(value==null)
        // return defaultValue;
        // #endif
        // return value;
        // }
        JExpression defaultValue = null;
        if (prop.defaultValue != null)
            defaultValue = prop.defaultValue.compute(outline.parent());

        // if Type is a wrapper and we have a default value,
        // we can use the primitive type.
        JType getterType;
        if (defaultValue != null || forcePrimitiveAccess)
            getterType = exposedType.unboxify();
        else
            getterType = exposedType;

        JMethod $get = writer.declareMethod(getterType, getGetterMethod());
        String javadoc = prop.javadoc;
        if (javadoc.length() == 0)
            javadoc = Messages.DEFAULT_GETTER_JAVADOC.format(nc.toVariableName(prop.getName(true)));
        writer.javadoc().append(javadoc);

        if (defaultValue == null) {
            $get.body()._return(ref());
        } else {
            JConditional cond = $get.body()._if(ref().eq(JExpr._null()));
            cond._then()._return(defaultValue);
            cond._else()._return(ref());
        }

        List<Object> possibleTypes = listPossibleTypes(prop);
        writer.javadoc().addReturn().append("possible object is\n").append(possibleTypes);

        // [RESULT]
        // void setXXX(Type newVal) {
        // this.value = newVal;
        // }
        JMethod $set = writer.declareMethod(codeModel.VOID, "set" + prop.getName(true));
        JType setterType = exposedType;
        if (forcePrimitiveAccess)
            setterType = setterType.unboxify();
        JVar $value = writer.addParameter(setterType, "value");
        JBlock body = $set.body();

        JVar oldVal = null;
        if (boundType != null) {
            oldVal = body.decl(implType, "old", JExpr.refthis(ref().name()));
        }

        body.assign(JExpr._this().ref(ref()), castToImplType($value));

        if (boundType != null) {
            assert oldVal != null;
            assert propertyConstant != null;

            outline.ref.javadoc().add(outline.ref.javadoc().size(),
                    "<p>Provided property: " + prop.getName(false) + "\n");

            JExpression cond;
            if (implType.unboxify().isPrimitive()) {
                cond = oldVal.ne($value);
            } else if (implType.isArray()) {
                cond = codeModel.ref(Arrays.class).staticInvoke("equals").arg(oldVal).arg($value).not();
            } else {
                cond = oldVal.ne($value).cand(oldVal.eq(JExpr._null()).cor($value.eq(JExpr._null())))
                        .cor(oldVal.ne(JExpr._null()).cand(oldVal.invoke("equals").arg($value).not()));
            }

            JBlock ifBlock = body._if(cond)._then();

            if (boundType == Kind.VETOABLE) {
                JTryBlock tryBlock = ifBlock._try();

                JClass vetoableClass = (JClass) codeModel._ref(java.beans.PropertyVetoException.class);

                tryBlock.body().add(
                        JExpr.ref("support").invoke("fireVetoableChange")
                                .arg(outline.implClass.staticRef(propertyConstant)).arg(oldVal).arg($value));

                // tryBlock.body().directStatement("support.fireVetoableChange(\"" + prop.getName(false)+ "\"," +
                // ref().name() + ", value);");
                JCatchBlock catchBlock = tryBlock._catch(vetoableClass);
                catchBlock.body().directStatement("return;");
            } else {

                ifBlock.add(JExpr.ref("support").invoke("firePropertyChange")
                        .arg(outline.implClass.staticRef(propertyConstant)).arg(oldVal).arg($value));

            }
        }

        javadoc = prop.javadoc;
        if (javadoc.length() == 0)
            javadoc = Messages.DEFAULT_SETTER_JAVADOC.format(nc.toVariableName(prop.getName(true)));
        writer.javadoc().append(javadoc);
        writer.javadoc().addParam($value).append("allowed object is\n").append(possibleTypes);
    }

    protected JFieldVar ref()
    {
        return field;
    }

    public final JType getFieldType()
    {
        return implType;
    }

    public FieldAccessor create(JExpression targetObject)
    {
        return new Accessor(targetObject);
    }

    /**
     * Useful base class for implementing {@link FieldAccessor}.
     */
    protected class Accessor implements FieldAccessor
    {

        /**
         * Evaluates to the target object this accessor should access.
         */
        protected final JExpression $target;

        protected Accessor(JExpression $target)
        {
            this.$target = $target;
            this.$ref = $target.ref(SingleField.this.ref());
        }

        /**
         * Reference to the field bound by the target object.
         */
        protected final JFieldRef $ref;

        public final FieldOutline owner()
        {
            return SingleField.this;
        }

        public final CPropertyInfo getPropertyInfo()
        {
            return prop;
        }

        public void unsetValues(JBlock body)
        {
            body.assign($ref, JExpr._null());
        }

        public JExpression hasSetValue()
        {
            return $ref.ne(JExpr._null());
        }

        public final void toRawValue(JBlock block, JVar $var)
        {
            block.assign($var, $target.invoke(getGetterMethod()));
        }

        public final void fromRawValue(JBlock block, String uniqueName, JExpression $var)
        {
            block.invoke($target, ("set" + prop.getName(true))).arg($var);
        }
    }

    /**
     * Gets the name of the getter method.
     * 
     * <p>
     * This encapsulation is necessary because sometimes we use {@code isXXXX} as the method name.
     */
    protected String getGetterMethod()
    {
        return (getFieldType().boxify().getPrimitiveType() == codeModel.BOOLEAN ? "is" : "get") + prop.getName(true);
    }

    public CPropertyInfo getPropertyInfo()
    {
        return prop;
    }

    public final JType getRawType()
    {
        return exposedType;
    }

    /**
     * Compute the type of a {@link CPropertyInfo}
     * 
     * @param aspect
     */
    protected JType getType(final Aspect aspect)
    {
        if (prop.getAdapter() != null)
            return prop.getAdapter().customType.toType(outline.parent(), aspect);

        final class TypeList extends ArrayList<JType>
        {
            /** */
            private static final long serialVersionUID = 8817002762839577541L;

            void add(CTypeInfo t)
            {
                add(t.getType().toType(outline.parent(), aspect));
                if (t instanceof CElementInfo) {
                    // UGLY. element substitution is implemented in a way that
                    // the derived elements are not assignable to base elements.
                    // so when we compute the signature, we have to take derived types
                    // into account
                    add(((CElementInfo) t).getSubstitutionMembers());
                }
            }

            void add(Collection<? extends CTypeInfo> col)
            {
                for (CTypeInfo typeInfo : col)
                    add(typeInfo);
            }
        }
        TypeList r = new TypeList();
        r.add(prop.ref());

        JType t;
        if (prop.baseType != null)
            t = prop.baseType;
        else
            t = TypeUtil.getCommonBaseType(codeModel, r);

        // if item type is unboxable, convert t=Integer -> t=int
        // the in-memory data structure can't have primitives directly,
        // but this guarantees that items cannot legal hold null,
        // which helps us improve the boundary signature between our
        // data structure and user code
        if (prop.isUnboxable())
            t = t.unboxify();
        return t;
    }

    /**
     * Returns contents to be added to javadoc.
     */
    protected final List<Object> listPossibleTypes(CPropertyInfo prop)
    {
        List<Object> r = new ArrayList<Object>();
        for (CTypeInfo tt : prop.ref()) {
            JType t = tt.getType().toType(outline.parent(), Aspect.EXPOSED);
            if (t.isPrimitive() || t.isArray())
                r.add(t.fullName());
            else {
                r.add(t);
                r.add("\n");
            }
        }

        return r;
    }

    /**
     * Case from {@link #exposedType} to {@link #implType} if necessary.
     */
    protected final JExpression castToImplType(JExpression exp)
    {
        if (implType == exposedType)
            return exp;
        else
            return JExpr.cast(implType, exp);
    }

    public ClassOutline parent()
    {
        return outline;
    }

    /**
     * Annotate the field according to the recipes given as {@link CPropertyInfo}.
     */
    protected void annotate(JAnnotatable field)
    {

        assert (field != null);

        /*
         * TODO: consider moving this logic to somewhere else
         * so that it can be better shared, for how a field gets
         * annotated doesn't really depend on how we generate accessors.
         * 
         * so perhaps we should separate those two.
         */

        // TODO: consider a visitor
        if (prop instanceof CAttributePropertyInfo) {
            annotateAttribute(field);
        } else if (prop instanceof CElementPropertyInfo) {
            annotateElement(field);
        } else if (prop instanceof CValuePropertyInfo) {
            field.annotate(XmlValue.class);
        } else if (prop instanceof CReferencePropertyInfo) {
            annotateReference(field);
        }

        outline.parent().generateAdapterIfNecessary(prop, field);
    }

    private void annotateReference(JAnnotatable field)
    {
        CReferencePropertyInfo rp = (CReferencePropertyInfo) prop;

        // this is just a quick hack to get the basic test working

        Collection<CElement> elements = rp.getElements();

        XmlElementRefWriter refw;
        if (elements.size() == 1) {
            refw = field.annotate2(XmlElementRefWriter.class);
            CElement e = elements.iterator().next();
            refw.name(e.getElementName().getLocalPart()).namespace(e.getElementName().getNamespaceURI())
                    .type(e.getType().toType(outline.parent(), IMPLEMENTATION));
        } else if (elements.size() > 1) {
            XmlElementRefsWriter refsw = field.annotate2(XmlElementRefsWriter.class);
            for (CElement e : elements) {
                refw = refsw.value();
                refw.name(e.getElementName().getLocalPart()).namespace(e.getElementName().getNamespaceURI())
                        .type(e.getType().toType(outline.parent(), IMPLEMENTATION));
            }
        }

        if (rp.isMixed())
            field.annotate(XmlMixed.class);

        NClass dh = rp.getDOMHandler();
        if (dh != null) {
            XmlAnyElementWriter xaew = field.annotate2(XmlAnyElementWriter.class);
            xaew.lax(rp.getWildcard().allowTypedObject);

            final JClass value = dh.toType(outline.parent(), IMPLEMENTATION);
            if (!value.equals(codeModel.ref(W3CDomHandler.class))) {
                xaew.value(value);
            }
        }

    }

    /**
     * Annotate the element property 'field'
     */
    @SuppressWarnings("deprecation")
    private void annotateElement(JAnnotatable field)
    {
        CElementPropertyInfo ep = (CElementPropertyInfo) prop;
        List<CTypeRef> types = ep.getTypes();

        if (ep.isValueList()) {
            field.annotate(XmlList.class);
        }

        assert ep.getXmlName() == null;
        // if( eName!=null ) { // wrapper
        // XmlElementWrapperWriter xcw = field.annotate2(XmlElementWrapperWriter.class);
        // xcw.name(eName.getLocalPart())
        // .namespace(eName.getNamespaceURI());
        // }

        if (types.size() == 1) {
            CTypeRef t = types.get(0);
            writeXmlElementAnnotation(field, t, resolve(t, IMPLEMENTATION), false);
        } else {
            for (CTypeRef t : types) {
                // generate @XmlElements
                writeXmlElementAnnotation(field, t, resolve(t, IMPLEMENTATION), true);
            }
            xesw = null;
        }
    }

    /**
     * Generate the simplest XmlElement annotation possible taking all semantic optimizations
     * into account. This method is essentially equivalent to:
     * 
     * xew.name(ctype.getTagName().getLocalPart())
     * .namespace(ctype.getTagName().getNamespaceURI())
     * .type(jtype)
     * .defaultValue(ctype.getDefaultValue());
     * 
     * @param field
     * @param ctype
     * @param jtype
     * @param checkWrapper true if the method might need to generate XmlElements
     */
    private void writeXmlElementAnnotation(JAnnotatable field, CTypeRef ctype, JType jType, boolean checkWrapper)
    {
        JType jtype = jType;
        // lazily create - we don't know if we need to generate anything yet
        XmlElementWriter xew = null;

        // these values are used to determine how to optimize the generated annotation
        XmlNsForm formDefault = parent()._package().getElementFormDefault();
        String mostUsedURI = parent()._package().getMostUsedNamespaceURI();
        String propName = prop.getName(false);

        // generate the name property?
        String generatedName = ctype.getTagName().getLocalPart();
        if (!generatedName.equals(propName)) {
            if (xew == null)
                xew = getXew(checkWrapper, field);
            xew.name(generatedName);
        }

        // generate the namespace property?
        String generatedNS = ctype.getTagName().getNamespaceURI();
        if (((formDefault == XmlNsForm.QUALIFIED) && !generatedNS.equals(mostUsedURI))
                || ((formDefault == XmlNsForm.UNQUALIFIED) && !generatedNS.equals(""))) {
            if (xew == null)
                xew = getXew(checkWrapper, field);
            xew.namespace(generatedNS);
        }

        // generate the required() property?
        CElementPropertyInfo ep = (CElementPropertyInfo) prop;
        if (ep.isRequired() && exposedType.isReference()) {
            if (xew == null)
                xew = getXew(checkWrapper, field);
            xew.required(true);
        }

        // generate the type property?

        // I'm not too sure if this is the right place to handle this, but
        // if the schema definition is requiring this element, we should point to a primitive type,
        // not wrapper type (to correctly carry forward the required semantics.)
        // if it's a collection, we can't use a primitive, however.
        if (ep.isRequired() && !prop.isCollection())
            jtype = jtype.unboxify();

        // when generating code for 1.4, the runtime can't infer that ArrayList<Foo> derives
        // from Collection<Foo> (because List isn't parameterized), so always expclitly
        // generate @XmlElement(type=...)
        if (!jtype.equals(exposedType) || (parent().parent().getModel().options.runtime14 && prop.isCollection())) {
            if (xew == null)
                xew = getXew(checkWrapper, field);
            xew.type(jtype);
        }

        // generate defaultValue property?
        final String defaultValue = ctype.getDefaultValue();
        if (defaultValue != null) {
            if (xew == null)
                xew = getXew(checkWrapper, field);
            xew.defaultValue(defaultValue);
        }

        // generate the nillable property?
        if (ctype.isNillable()) {
            if (xew == null)
                xew = getXew(checkWrapper, field);
            xew.nillable(true);
        }
    }

    // ugly hack to lazily create
    private XmlElementsWriter xesw = null;

    private XmlElementWriter getXew(boolean checkWrapper, JAnnotatable field)
    {
        XmlElementWriter xew;
        if (checkWrapper) {
            if (xesw == null) {
                xesw = field.annotate2(XmlElementsWriter.class);
            }
            xew = xesw.value();
        } else {
            xew = field.annotate2(XmlElementWriter.class);
        }
        return xew;
    }

    /**
     * Annotate the attribute property 'field'
     */
    private void annotateAttribute(JAnnotatable field)
    {
        CAttributePropertyInfo ap = (CAttributePropertyInfo) prop;
        QName attName = ap.getXmlName();

        // [RESULT]
        // @XmlAttribute(name="foo", required=true, namespace="bar://baz")
        XmlAttributeWriter xaw = field.annotate2(XmlAttributeWriter.class);

        final String generatedName = attName.getLocalPart();
        final String generatedNS = attName.getNamespaceURI();

        // generate name property?
        if (!generatedName.equals(ap.getName(false))) {
            xaw.name(generatedName);
        }

        // generate namespace property?
        if (!generatedNS.equals("")) { // assume attributeFormDefault == unqualified
            xaw.namespace(generatedNS);
        }

        // generate required property?
        if (ap.isRequired()) {
            xaw.required(true);
        }
    }

    /**
     * return the Java type for the given type reference in the model.
     */
    private JType resolve(CTypeRef typeRef, Aspect a)
    {
        return outline.parent().resolve(typeRef, a);
    }
}

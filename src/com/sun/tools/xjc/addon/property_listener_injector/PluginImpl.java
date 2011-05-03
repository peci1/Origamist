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

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.beans.VetoableChangeListener;
import java.beans.VetoableChangeSupport;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

import org.xml.sax.ErrorHandler;

import com.sun.codemodel.JClass;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JFieldVar;
import com.sun.codemodel.JInvocation;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.tools.xjc.BadCommandLineException;
import com.sun.tools.xjc.Options;
import com.sun.tools.xjc.Plugin;
import com.sun.tools.xjc.model.CPluginCustomization;
import com.sun.tools.xjc.outline.ClassOutline;
import com.sun.tools.xjc.outline.Outline;
import com.sun.tools.xjc.util.DOMUtils;

/**
 * This Plugin will generate property change events on each setXXX.
 * <p>
 * See the javadoc of {@link Plugin} for what those methods mean.
 * <p>
 * Usage in schema:<br>
 * Add either <br>
 * <code>&lt;uri:listener>java.beans.VetoableChangeSupport&lt;/uri:listener></code><br>
 * or<br>
 * <code>&lt;uri:listener>java.beans.PropertyChangeSupport&lt;/uri:listener></code><br>
 * to either global or class bindings element <code>&lt;appinfo><code>.
 * 
 * @author Jerome Dochez
 * @author Martin Pecka
 */
public class PluginImpl extends Plugin
{
    protected String   boundInterface          = null;
    protected String   constrainedInterface    = null;

    protected Class<?> boundSupportClass       = PropertyChangeSupport.class;
    protected Class<?> constrainedSupportClass = VetoableChangeSupport.class;

    /** If false, don't generate the PropertyChangeSupport field and its delegate methods. */
    protected boolean  generateSupport         = true;

    public String getOptionName()
    {
        return "Xinject-listener-code";
    }

    @Override
    public List<String> getCustomizationURIs()
    {
        return Collections.singletonList(Const.NS);
    }

    @Override
    public boolean isCustomizationTagName(String nsUri, String localName)
    {
        return nsUri.equals(Const.NS) && localName.equals("listener");
    }

    @Override
    public String getUsage()
    {
        return "  -Xinject-listener-code\t:  inject property change event support to setter methods\n"
                + "  -Xinject-listener-code-dont-generate-support\t: don't generate the PropertyChangeSupport field and its delegate methods"
                + "  -Xinject-listener-code-interface-bound fully.qualified.interface.name\t: tag classes containing bound properties with this interface\n"
                + "  -Xinject-listener-code-interface-constrained fully.qualified.interface.name\t: tag classes containing constrained properties with this interface\n"
                + "  -Xinject-listener-code-supportClass-bound fully.qualified.interface.name\t: name of the class to be used as PropertyChangeSupport implementation (must extend this class)\n"
                + "  -Xinject-listener-code-supportClass-constrained fully.qualified.interface.name\t: name of the class to be used as VetoableChangeSupport implementation (must extend this class)\n"
                + "\n   Note: Even if you provide an implementation class for either bound/constrained property support, you still have to use the base class names in the schema (eg. java.beans.PropertyChangeSupport)";
    }

    @Override
    public void onActivated(Options opt) throws BadCommandLineException
    {
        FieldRendererFactory frf = new FieldRendererFactory(opt.getFieldRendererFactory());
        opt.setFieldRendererFactory(frf, this);
    }

    @Override
    public int parseArgument(Options opt, String[] args, int i) throws BadCommandLineException, IOException
    {
        int numTokens = super.parseArgument(opt, args, i);
        if (numTokens > 0)
            return numTokens;

        String arg = args[i];
        if (arg.startsWith("-Xinject-listener-code-interface-")) {
            if (args.length <= i)
                throw new BadCommandLineException("Option " + arg + " needs an argument - an interface FQname.");
            String _interface = args[i + 1];
            if (arg.endsWith("bound")) {
                boundInterface = _interface;
                return 2;
            } else if (arg.endsWith("constrained")) {
                constrainedInterface = _interface;
                return 2;
            }
        } else if (arg.startsWith("-Xinject-listener-code-supportClass-")) {
            if (args.length <= i)
                throw new BadCommandLineException("Option " + arg + " needs an argument - a class' FQname.");
            String support = args[i + 1];

            Class<?> supportClass;
            try {
                supportClass = Class.forName(support);
            } catch (ClassNotFoundException e) {
                throw new IOException("The specified property support class " + support
                        + " isn't on XJC classpath! Try to re-run the build, maybe you've dynamically "
                        + "created the class file and Ant hasn't noticed it.", e);
            }

            if (arg.endsWith("bound")) {
                if (!PropertyChangeSupport.class.isAssignableFrom(supportClass)) {
                    throw new BadCommandLineException("The specified bound property support class "
                            + supportClass.getName() + " doesn't extend java.beans.PropertyChangeSupport!");
                }
                boundSupportClass = supportClass;
                return 2;
            } else if (arg.endsWith("constrained")) {
                if (!VetoableChangeSupport.class.isAssignableFrom(supportClass)) {
                    throw new BadCommandLineException("The specified constrained property support class "
                            + supportClass.getName() + " doesn't extend java.beans.VetoableChangeSupport!");
                }
                constrainedSupportClass = supportClass;
                return 2;
            }
        } else if (arg.equals("-Xinject-listener-code-dont-generate-support")) {
            generateSupport = false;
            return 1;
        }

        return 0;
    }

    // meat of the processing
    @Override
    public boolean run(Outline model, Options opt, ErrorHandler errorHandler)
    {
        CPluginCustomization listener = model.getModel().getCustomizations().find(Const.NS, "listener");
        String globalInterfaceSetting = null;
        if (listener != null) {
            listener.markAsAcknowledged();
            globalInterfaceSetting = DOMUtils.getElementText(listener.element);
        }

        for (ClassOutline co : model.getClasses()) {
            CPluginCustomization c = co.target.getCustomizations().find(Const.NS, "listener");

            String interfaceName;
            if (c != null) {
                c.markAsAcknowledged();
                interfaceName = DOMUtils.getElementText(c.element);
            } else {
                interfaceName = globalInterfaceSetting;
            }

            if (generateSupport) {
                if (VetoableChangeListener.class.getName().equals(interfaceName)) {
                    addSupport(VetoableChangeListener.class, constrainedSupportClass, co.implClass,
                            constrainedInterface);
                }
                if (PropertyChangeListener.class.getName().equals(interfaceName)) {
                    addSupport(PropertyChangeListener.class, boundSupportClass, co.implClass, boundInterface);

                }
            }

        }

        return true;
    }

    private void addSupport(Class<?> listener, Class<?> support, JDefinedClass target, String _interface)
    {
        JClass clazz = target;
        boolean foundSupportField = false;
        while (clazz != null) {
            if (!(clazz instanceof JDefinedClass)) {
                clazz = clazz._extends();
                continue;
            }

            if (((JDefinedClass) clazz).fields().get("support") != null) {
                foundSupportField = true;
                break;
            }
            clazz = clazz._extends();
        }

        if (foundSupportField)
            return;

        // add the support field.
        // JFieldVar field = target.field(JMod.PRIVATE|JMod.TRANSIENT, support, "support");
        JFieldVar field = target.field(JMod.PROTECTED | JMod.TRANSIENT, support, "support");

        // and initialize it....
        field.init(JExpr.direct("new " + support.getSimpleName() + "(this)"));

        if (_interface != null) {
            target._implements(target.owner().ref(_interface));
        }

        // we need to hadd
        for (Method method : support.getMethods()) {
            if (Modifier.isPublic(method.getModifiers())) {
                if (method.getName().startsWith("add")) {
                    // look if one of the parameters in the listnener class...
                    for (Class<?> param : method.getParameterTypes()) {
                        if (param.getName().equals(listener.getName())) {
                            addMethod(method, target);
                            break;
                        }
                    }
                }
                if (method.getName().startsWith("remove")) {
                    // look if one of the parameters in the listnener class...
                    for (Class<?> param : method.getParameterTypes()) {
                        if (param.getName().equals(listener.getName())) {
                            addMethod(method, target);
                            break;
                        }
                    }
                }
            }
        }
    }

    private void addMethod(Method method, JDefinedClass target)
    {
        JMethod addListener;

        if (method.getGenericReturnType() instanceof ParameterizedType) {
            ParameterizedType pType = (ParameterizedType) method.getGenericReturnType();
            JClass returnType = target.owner().ref(method.getReturnType()).erasure();
            for (Type t : pType.getActualTypeArguments()) {
                if (t instanceof Class<?>)
                    returnType = returnType.narrow(target.owner().ref((Class<?>) t));
            }
            addListener = target.method(JMod.PUBLIC, returnType, method.getName());
        } else {
            addListener = target.method(JMod.PUBLIC, method.getReturnType(), method.getName());
        }

        Type types[] = method.getGenericParameterTypes();
        Class<?>[] params = method.getParameterTypes();
        JClass[] jParams = new JClass[params.length];
        for (int i = 0; i < params.length; i++) {
            jParams[i] = target.owner().ref(params[i]);
            if (types[i] instanceof ParameterizedType) {
                for (Type t : ((ParameterizedType) types[i]).getActualTypeArguments()) {
                    if (t instanceof Class<?>)
                        jParams[i] = jParams[i].narrow(target.owner().ref((Class<?>) t));
                }
            }
        }

        JInvocation inv;
        if (!method.getReturnType().equals(Void.TYPE))
            addListener.body()._return(inv = JExpr._this().ref("support").invoke(method.getName()));
        else
            inv = addListener.body().invoke(JExpr._this().ref("support"), method.getName());

        for (int i = 0; i < params.length; i++) {
            inv.arg(addListener.param(params[i], "param" + i));
        }
    }
}

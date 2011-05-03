/**
 * 
 */
package com.sun.tools.xjc.addon;

import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;

import com.sun.codemodel.JBlock;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.tools.xjc.BadCommandLineException;
import com.sun.tools.xjc.Options;
import com.sun.tools.xjc.Plugin;
import com.sun.tools.xjc.outline.ClassOutline;
import com.sun.tools.xjc.outline.Outline;

/**
 * This plugin adds a call to init() (or another defined method) to all generated class' no-arg constructors.
 * 
 * The default behavior checks if the class of the current constructor's object is either the examined class or the
 * user-specified implClass. This allows the init function to be called only once for each object even if your generated
 * classes inherit one from another.
 * 
 * @author Martin Pecka
 */
public class CallInitAfterConstructPlugin extends Plugin
{

    /** The method to call. */
    protected String  initMethod = "init";

    /** If false, the constructor won't check the class of created object as a condition to call the init method. */
    protected boolean checkClass = true;

    @Override
    public String getOptionName()
    {
        return "Xcall-init-after-construct";
    }

    @Override
    public String getUsage()
    {
        return "  -Xcall-init-after-construct:\tadd a call to init() (or another defined method) to all generated class' no-arg constructors."
                + "  -Xcall-init-after-construct-init-fn:\teither a name of the init a method or a call to the method as string (the default is \"init\")"
                + "  -Xcall-init-after-construct-no-class-check:\tif set, the constructor won't check the class of created object as a condition to call the init method.";
    }

    @Override
    public List<String> getCustomizationURIs()
    {
        return Collections.singletonList("http://www.mff.cuni.cz/~peckam/java/origamist/jaxb/plugins");
    }

    @Override
    public int parseArgument(Options opt, String[] args, int i) throws BadCommandLineException, IOException
    {
        int parentRes = super.parseArgument(opt, args, i);
        if (parentRes > 0)
            return parentRes;
        String option = args[i];
        if (option.equals(option.equals("-Xcall-init-after-construct-init-fn"))) {
            if (args.length < i + 2) {
                throw new BadCommandLineException("-Xcall-init-after-construct-init-fn needs an argument");
            }
            initMethod = args[i + 1];
            return 2;
        } else if (option.equals("-Xcall-init-after-construct-no-class-check")) {
            checkClass = false;
        }
        return 0;
    }

    @Override
    public boolean run(Outline model, Options opt, ErrorHandler errorHandler) throws SAXException
    {
        for (ClassOutline co : model.getClasses()) {
            JMethod constructor = null;
            for (@SuppressWarnings("unchecked")
            Iterator<JMethod> it = co.implClass.constructors(); it.hasNext();) {
                JMethod constr = it.next();
                if (constr.listParams().length == 0) {
                    constructor = constr;
                    break;
                }
            }
            if (constructor == null)
                constructor = co.implClass.constructor(JMod.PUBLIC);

            JBlock body = constructor.body();

            String className;
            if (co.target.getUserSpecifiedImplClass() != null)
                className = co.target.getUserSpecifiedImplClass();
            else
                className = co.implClass.fullName();

            JBlock then;
            if (checkClass)
                then = body._if(JExpr.invoke("getClass").invoke("getName").invoke("equals").arg(className))._then();
            else
                then = body;

            if (!initMethod.contains("(")) {
                then.invoke(initMethod);
            } else {
                then.directStatement(initMethod);
            }
        }

        return true;
    }
}

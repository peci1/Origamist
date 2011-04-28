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

import com.sun.codemodel.JClass;
import com.sun.tools.xjc.generator.bean.ClassOutlineImpl;
import com.sun.tools.xjc.generator.bean.field.FieldRenderer;
import com.sun.tools.xjc.model.CPropertyInfo;
import com.sun.tools.xjc.outline.FieldOutline;

/**
 * 
 * @author Jerome Dochez
 */
public class FieldRendererFactory extends com.sun.tools.xjc.generator.bean.field.FieldRendererFactory
{

    com.sun.tools.xjc.generator.bean.field.FieldRendererFactory frf;

    /** Creates a new instance of FieldRendererFactory */
    public FieldRendererFactory(com.sun.tools.xjc.generator.bean.field.FieldRendererFactory frf)
    {
        this.frf = frf;
    }

    public FieldRenderer getDefault()
    {
        return DEFAULT;
    }

    public FieldRenderer getArray()
    {
        return frf.getArray();
    }

    public FieldRenderer getRequiredUnboxed()
    {
        return frf.getRequiredUnboxed();
    }

    public FieldRenderer getSingle()
    {
        return SINGLE;
    }

    public FieldRenderer getSinglePrimitiveAccess()
    {
        return frf.getSinglePrimitiveAccess();
    }

    public FieldRenderer getList(JClass coreList)
    {
        return frf.getList(coreList);
    }

    public FieldRenderer getConst(FieldRenderer fallback)
    {
        return frf.getConst(fallback);
    }

    private final FieldRenderer SINGLE  = new FieldRenderer() {
                                            public FieldOutline generate(ClassOutlineImpl context, CPropertyInfo prop)
                                            {
                                                return new SingleField(context, prop);
                                            }
                                        };

    private final FieldRenderer DEFAULT = new DefaultFieldRenderer(this);

}

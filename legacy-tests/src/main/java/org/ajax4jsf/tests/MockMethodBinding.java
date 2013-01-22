/**
 * License Agreement.
 *
 *  JBoss RichFaces - Ajax4jsf Component Library
 *
 * Copyright (C) 2007  Exadel, Inc.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License version 2.1 as published by the Free Software Foundation.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA
 */



package org.ajax4jsf.tests;

import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.el.EvaluationException;
import javax.faces.el.MethodBinding;
import javax.faces.el.MethodNotFoundException;

/**
 * @author Nick - mailto:nbelaevski@exadel.com
 * created 09.04.2007
 *
 */
public class MockMethodBinding extends MethodBinding {
    private List invocationArgs = new ArrayList();
    private MethodResult result;
    private Class returnType;

    public MockMethodBinding() {
        this(null, null);
    }

    public MockMethodBinding(Class returnType, MethodResult result) {
        super();
        this.returnType = returnType;
        this.result = result;
    }

    public Class getType(FacesContext context) throws MethodNotFoundException {
        if (context == null) {
            throw new NullPointerException();
        }

        return returnType;
    }

    public Object invoke(FacesContext context, Object[] params) throws EvaluationException, MethodNotFoundException {
        if (context == null) {
            throw new NullPointerException();
        }

        invocationArgs.add(params);

        if (result != null) {
            return result.invoke(context, params, returnType);
        }

        return null;
    }

    public void clear() {
        invocationArgs.clear();
    }

    public Object[][] getInvocationArgs() {
        return (Object[][]) invocationArgs.toArray(new Object[invocationArgs.size()][]);
    }

    public static interface MethodResult {
        public Object invoke(FacesContext facesContext, Object[] args, Class returnType);
    }
}

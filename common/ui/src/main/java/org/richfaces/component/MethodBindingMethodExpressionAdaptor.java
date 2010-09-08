/**
 * License Agreement.
 *
 * Rich Faces - Natural Ajax for Java Server Faces (JSF)
 *
 * Copyright (C) 2007 Exadel, Inc.
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

package org.richfaces.component;

import javax.el.ELException;
import javax.el.MethodExpression;
import javax.faces.component.StateHolder;
import javax.faces.context.FacesContext;
import javax.faces.el.EvaluationException;
import javax.faces.el.MethodBinding;
import javax.faces.el.MethodNotFoundException;

/**
 * Maps {@link MethodExpression} to {@link MethodBinding}
 *
 * @author Maksim Kaszynski
 */
@SuppressWarnings("deprecation")
public class MethodBindingMethodExpressionAdaptor extends MethodBinding implements StateHolder {
    private MethodExpression expression;
    private boolean tranzient;

    /*
     *  (non-Javadoc)
     * @see javax.faces.el.MethodBinding#getType(javax.faces.context.FacesContext)
     */
    public MethodBindingMethodExpressionAdaptor() {

        // TODO Auto-generated constructor stub
    }

    public MethodBindingMethodExpressionAdaptor(MethodExpression expression) {
        super();
        this.expression = expression;
    }

    @Override
    public Class<?> getType(FacesContext context) throws MethodNotFoundException {
        try {
            return expression.getMethodInfo(context.getELContext()).getReturnType();
        } catch (javax.el.MethodNotFoundException e) {
            throw new MethodNotFoundException(e);
        }
    }

    /*
     *  (non-Javadoc)
     * @see javax.faces.el.MethodBinding#invoke(javax.faces.context.FacesContext, java.lang.Object[])
     */
    @Override
    public Object invoke(FacesContext context, Object[] params) throws EvaluationException, MethodNotFoundException {
        try {
            return expression.invoke(context.getELContext(), params);
        } catch (javax.el.MethodNotFoundException e) {
            throw new MethodNotFoundException(e);
        } catch (ELException e) {
            throw new EvaluationException(e);
        }
    }

    public boolean isTransient() {
        return tranzient;
    }

    public void restoreState(FacesContext context, Object state) {
        expression = (MethodExpression) state;
    }

    public Object saveState(FacesContext context) {
        return expression;
    }

    public void setTransient(boolean newTransientValue) {
        tranzient = newTransientValue;
    }

    @Override
    public String getExpressionString() {
        return expression.getExpressionString();
    }
}

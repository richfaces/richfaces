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
import javax.el.PropertyNotFoundException;
import javax.el.ValueExpression;
import javax.faces.component.StateHolder;
import javax.faces.context.FacesContext;

/**
 * @author Maksim Kaszynski
 */
@SuppressWarnings("deprecation")
public class ValueBindingValueExpressionAdaptor extends javax.faces.el.ValueBinding implements StateHolder {
    private ValueExpression expression;
    private boolean tranzient;

    public ValueBindingValueExpressionAdaptor() {

        // TODO Auto-generated constructor stub
    }

    public ValueBindingValueExpressionAdaptor(ValueExpression expression) {
        super();
        this.expression = expression;
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.faces.el.ValueBinding#getType(javax.faces.context.FacesContext)
     */
    @Override
    public Class<?> getType(FacesContext context) throws ELException {
        try {
            return expression.getType(context.getELContext());
        } catch (javax.el.PropertyNotFoundException e) {
            throw new PropertyNotFoundException(e);
        } catch (ELException e) {
            throw new ELException(e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.faces.el.ValueBinding#getValue(javax.faces.context.FacesContext)
     */
    @Override
    public Object getValue(FacesContext context) throws ELException {
        try {
            return expression.getValue(context.getELContext());
        } catch (javax.el.PropertyNotFoundException e) {
            throw new PropertyNotFoundException(e);
        } catch (ELException e) {
            throw new ELException(e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.faces.el.ValueBinding#isReadOnly(javax.faces.context.FacesContext)
     */
    @Override
    public boolean isReadOnly(FacesContext context) throws ELException {
        try {
            return expression.isReadOnly(context.getELContext());
        } catch (javax.el.PropertyNotFoundException e) {
            throw new PropertyNotFoundException(e);
        } catch (ELException e) {
            throw new ELException(e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.faces.el.ValueBinding#setValue(javax.faces.context.FacesContext, java.lang.Object)
     */
    @Override
    public void setValue(FacesContext context, Object value) throws ELException {
        try {
            expression.setValue(context.getELContext(), value);
        } catch (javax.el.PropertyNotFoundException e) {
            throw new PropertyNotFoundException(e);
        } catch (ELException e) {
            throw new ELException(e);
        }
    }

    public boolean isTransient() {
        return tranzient;
    }

    public void restoreState(FacesContext context, Object state) {
        expression = (ValueExpression) state;
    }

    public Object saveState(FacesContext context) {
        return expression;
    }

    public void setTransient(boolean newTransientValue) {
        tranzient = newTransientValue;
    }

    public ValueExpression getExpression() {
        return expression;
    }

    public void setExpression(ValueExpression expression) {
        this.expression = expression;
    }
}

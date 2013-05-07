/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.richfaces.ui.common;

import javax.el.ELException;
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
    public Class<?> getType(FacesContext context) throws javax.faces.el.EvaluationException {
        try {
            return expression.getType(context.getELContext());
        } catch (javax.el.PropertyNotFoundException e) {
            throw new javax.faces.el.PropertyNotFoundException(e);
        } catch (ELException e) {
            throw new javax.faces.el.EvaluationException(e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.faces.el.ValueBinding#getValue(javax.faces.context.FacesContext)
     */
    @Override
    public Object getValue(FacesContext context) throws javax.faces.el.EvaluationException {
        try {
            return expression.getValue(context.getELContext());
        } catch (javax.el.PropertyNotFoundException e) {
            throw new javax.faces.el.PropertyNotFoundException(e);
        } catch (ELException e) {
            throw new javax.faces.el.EvaluationException(e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.faces.el.ValueBinding#isReadOnly(javax.faces.context.FacesContext)
     */
    @Override
    public boolean isReadOnly(FacesContext context) throws javax.faces.el.EvaluationException {
        try {
            return expression.isReadOnly(context.getELContext());
        } catch (javax.el.PropertyNotFoundException e) {
            throw new javax.faces.el.PropertyNotFoundException(e);
        } catch (ELException e) {
            throw new javax.faces.el.EvaluationException(e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.faces.el.ValueBinding#setValue(javax.faces.context.FacesContext, java.lang.Object)
     */
    @Override
    public void setValue(FacesContext context, Object value) throws javax.faces.el.EvaluationException {
        try {
            expression.setValue(context.getELContext(), value);
        } catch (javax.el.PropertyNotFoundException e) {
            throw new javax.faces.el.PropertyNotFoundException(e);
        } catch (ELException e) {
            throw new javax.faces.el.EvaluationException(e);
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

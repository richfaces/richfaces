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

package org.ajax4jsf.event;

import javax.el.ValueExpression;
import javax.faces.component.StateHolder;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import java.io.Serializable;

/**
 * Helper class to keep reference to listener binded as EL-expression.
 *
 * @author shura
 */
public class AjaxListenerHelper implements AjaxListener, StateHolder {
    private boolean isTransient = false;
    private ValueExpression expression;

    /**
     *
     */
    public AjaxListenerHelper() {
        super();

        // TODO Auto-generated constructor stub
    }

    /**
     * @param expression
     */
    public AjaxListenerHelper(ValueExpression expression) {
        super();

        if (null == expression) {
            throw new IllegalArgumentException("Binding expression for AjaxListener helper must be not null");
        }

        this.expression = expression;
    }

    private AjaxListener getHandler(FacesContext context) {
        return (AjaxListener) expression.getValue(context.getELContext());
    }

    /*
     *  (non-Javadoc)
     * @see javax.faces.component.StateHolder#restoreState(javax.faces.context.FacesContext, java.lang.Object)
     */
    public void restoreState(FacesContext context, Object state) {
        State helperState = (State) state;

        expression = (ValueExpression) UIComponentBase.restoreAttachedState(context, helperState.binding);
    }

    /*
     *  (non-Javadoc)
     * @see javax.faces.component.StateHolder#saveState(javax.faces.context.FacesContext)
     */
    public Object saveState(FacesContext context) {
        State helperState = new State();

        helperState.binding = UIComponentBase.saveAttachedState(context, expression);

        return helperState;
    }

    /**
     * @return Returns the transient.
     */
    public boolean isTransient() {
        return isTransient;
    }

    /**
     * @param transient1 The transient to set.
     */
    public void setTransient(boolean transient1) {
        isTransient = transient1;
    }

    public void processAjax(AjaxEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        AjaxListener handler = getHandler(context);

        handler.processAjax(event);
    }

    /**
     * @author shura
     *         TODO Refactoring
     */
    private static final class State implements Serializable {
        private static final long serialVersionUID = 1978277414406556172L;
        private Object binding;

        private State() {
        }
    }
}

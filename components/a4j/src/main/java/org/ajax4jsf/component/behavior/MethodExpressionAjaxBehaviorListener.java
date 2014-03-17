/*
 * JBoss, Home of Professional Open Source
 * Copyright ${year}, Red Hat, Inc. and individual contributors
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
package org.ajax4jsf.component.behavior;

import javax.el.ELContext;
import javax.el.MethodExpression;
import javax.el.MethodNotFoundException;
import javax.faces.component.StateHolder;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.AjaxBehaviorListener;

/**
 * @author Anton Belevich
 *
 */
public class MethodExpressionAjaxBehaviorListener implements AjaxBehaviorListener, StateHolder {
    private static final Class<?>[] ACTION_LISTENER_ZEROARG_SIG = new Class[] {};
    private MethodExpression methodExpressionOneArg = null;
    private MethodExpression methodExpressionZeroArg = null;
    private boolean isTransient;

    public MethodExpressionAjaxBehaviorListener() {
    }

    public MethodExpressionAjaxBehaviorListener(MethodExpression methodExpressionOneArg) {
        this.methodExpressionOneArg = methodExpressionOneArg;

        FacesContext context = FacesContext.getCurrentInstance();
        ELContext elContext = context.getELContext();
        this.methodExpressionZeroArg = context
            .getApplication()
            .getExpressionFactory()
            .createMethodExpression(elContext, methodExpressionOneArg.getExpressionString(), Void.class,
                ACTION_LISTENER_ZEROARG_SIG);
    }

    public MethodExpressionAjaxBehaviorListener(MethodExpression methodExpressionOneArg, MethodExpression methodExpressionZeroArg) {
        this.methodExpressionOneArg = methodExpressionOneArg;
        this.methodExpressionZeroArg = methodExpressionZeroArg;
    }

    public void processAjaxBehavior(AjaxBehaviorEvent event) throws AbortProcessingException {

        FacesContext context = FacesContext.getCurrentInstance();
        ELContext elContext = context.getELContext();

        try {
            methodExpressionOneArg.invoke(elContext, new Object[] { event });
        } catch (MethodNotFoundException mnfe) {
            methodExpressionZeroArg.invoke(elContext, new Object[] {});
        }
    }

    public boolean isTransient() {
        return isTransient;
    }

    public void setTransient(boolean newTransientValue) {
        isTransient = newTransientValue;
    }

    public void restoreState(FacesContext context, Object state) {
        if (context == null) {
            throw new NullPointerException();
        }
        if (state == null) {
            return;
        }
        methodExpressionOneArg = (MethodExpression) ((Object[]) state)[0];
        methodExpressionZeroArg = (MethodExpression) ((Object[]) state)[1];
    }

    public Object saveState(FacesContext context) {
        if (context == null) {
            throw new NullPointerException();
        }
        return new Object[] { methodExpressionOneArg, methodExpressionZeroArg };
    }
}

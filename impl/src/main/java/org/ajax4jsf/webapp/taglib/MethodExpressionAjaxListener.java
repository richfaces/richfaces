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

package org.ajax4jsf.webapp.taglib;

import org.ajax4jsf.event.AjaxEvent;
import org.ajax4jsf.event.AjaxListener;

import javax.el.MethodExpression;
import javax.faces.component.StateHolder;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;

/**
 * @author Nick Belaevski
 * @since 3.2.2
 */
public class MethodExpressionAjaxListener implements AjaxListener, StateHolder {
    private MethodExpression expression;

    public MethodExpressionAjaxListener() {
        super();
    }

    public MethodExpressionAjaxListener(MethodExpression expression) {
        super();
        this.expression = expression;
    }

    public MethodExpression getExpression() {
        return expression;
    }

    /*
     *  (non-Javadoc)
     * @see org.ajax4jsf.event.AjaxListener#processAjax(org.ajax4jsf.event.AjaxEvent)
     */
    public void processAjax(AjaxEvent event) {
        FacesContext facesContext = FacesContext.getCurrentInstance();

        expression.invoke(facesContext.getELContext(), new Object[]{event});
    }

    public void restoreState(FacesContext context, Object state) {
        this.expression = (MethodExpression) UIComponentBase.restoreAttachedState(context, state);
    }

    public Object saveState(FacesContext context) {
        return UIComponentBase.saveAttachedState(context, this.expression);
    }

    public boolean isTransient() {
        return false;
    }

    public void setTransient(boolean newTransientValue) {
        if (newTransientValue) {
            throw new IllegalArgumentException();
        }
    }
}

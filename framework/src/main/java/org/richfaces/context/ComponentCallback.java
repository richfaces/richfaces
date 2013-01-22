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
package org.richfaces.context;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.component.behavior.ClientBehavior;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitResult;
import javax.faces.context.FacesContext;

import org.ajax4jsf.component.AjaxClientBehavior;
import org.richfaces.renderkit.util.CoreAjaxRendererUtils;

/**
 * User: akolonitsky Date: Oct 13, 2009
 */
abstract class ComponentCallback implements VisitCallback {
    protected final FacesContext facesContext;
    private final String behaviorEvent;

    ComponentCallback(FacesContext facesContext, String behaviorEvent) {
        super();

        this.facesContext = facesContext;
        this.behaviorEvent = behaviorEvent;
    }

    private AjaxClientBehavior findBehavior(UIComponent target) {
        if ((behaviorEvent == null) || !(target instanceof ClientBehaviorHolder)) {
            return null;
        }

        ClientBehaviorHolder behaviorHolder = (ClientBehaviorHolder) target;
        List<ClientBehavior> behaviors = behaviorHolder.getClientBehaviors().get(behaviorEvent);

        if (behaviors == null) {
            return null;
        }

        for (ClientBehavior behavior : behaviors) {
            if ((behavior instanceof AjaxClientBehavior) && !((AjaxClientBehavior) behavior).isDisabled()) {

                // TODO need more reliable algorithm
                return (AjaxClientBehavior) behavior;
            }
        }

        return null;
    }

    protected Collection<String> toCollection(Object value) {
        // TODO - unit tests check for "@none" element
        Collection<String> result = CoreAjaxRendererUtils.asIdsSet(value);
        if (result == null) {
            result = new LinkedHashSet<String>(1);
        }

        return result;
    }

    protected abstract void doVisit(UIComponent target, AjaxClientBehavior behavior);

    public final VisitResult visit(VisitContext visitContext, UIComponent target) {
        AjaxClientBehavior ajaxBehavior = null;

        if (behaviorEvent != null) {
            ajaxBehavior = findBehavior(target);
        }

        doVisit(target, ajaxBehavior);

        return VisitResult.COMPLETE;
    }
}

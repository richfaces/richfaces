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

import org.ajax4jsf.component.AjaxClientBehavior;
import org.ajax4jsf.renderkit.AjaxRendererUtils;
import org.ajax4jsf.renderkit.RendererUtils;

import javax.faces.component.UIComponent;
import javax.faces.component.behavior.ClientBehavior;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitResult;
import javax.faces.context.FacesContext;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * User: akolonitsky
 * Date: Oct 13, 2009
 */
abstract class ComponentCallback implements VisitCallback {

    private Collection<String> componentIds = new LinkedHashSet<String>();

    private final String behaviorEvent;
    private final String defaultIdAttribute;

    ComponentCallback(String behaviorEvent, String defaultIdAttribute) {
        super();

        this.behaviorEvent = behaviorEvent;
        this.defaultIdAttribute = defaultIdAttribute;
    }

    protected String getDefaultComponentId() {
        return null;
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

    protected abstract Object getBehaviorAttributeValue(AjaxClientBehavior behavior);

    protected abstract Object getAttributeValue(UIComponent component);

    protected void doVisit(FacesContext context, UIComponent target, AjaxClientBehavior behavior) {
        Object attributeObject;

        if (behavior == null) {
            attributeObject = getAttributeValue(target);
        } else {
            attributeObject = getBehaviorAttributeValue(behavior);
        }

        //TODO - unit tests check for "@none" element
        Collection<String> attributeIds = AjaxRendererUtils.asSet(attributeObject);
        if (attributeIds == null) {
            attributeIds = new LinkedHashSet<String>();
        }

        if (attributeIds.isEmpty() && defaultIdAttribute != null) {
            // asSet() returns copy of original set and we're free to modify it
            attributeIds.add(defaultIdAttribute);
        }

        componentIds.addAll(RendererUtils.getInstance().findComponentsFor(context, target, attributeIds));
    }

    public final VisitResult visit(VisitContext visitContext, UIComponent target) {
        AjaxClientBehavior ajaxBehavior = null;

        if (behaviorEvent != null) {
            ajaxBehavior = findBehavior(target);
        }

        doVisit(visitContext.getFacesContext(), target, ajaxBehavior);

        return VisitResult.COMPLETE;
    }

    public Collection<String> getComponentIds() {
        return componentIds;
    }

}

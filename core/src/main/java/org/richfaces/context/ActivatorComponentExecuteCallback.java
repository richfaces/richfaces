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
package org.richfaces.context;

import java.util.Collection;
import java.util.Collections;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.ajax4jsf.component.AjaxClientBehavior;
import org.richfaces.component.AjaxContainer;
import org.richfaces.renderkit.AjaxConstants;

/**
 * Callback that collects component attributes necessary for partial view rendering
 */
class ActivatorComponentExecuteCallback extends ActivatorComponentRenderCallback {
    private Collection<String> executeIds = null;

    ActivatorComponentExecuteCallback(FacesContext facesContext, String behaviorEvent) {
        super(facesContext, behaviorEvent);
    }

    @Override
    protected void doVisit(UIComponent target, AjaxClientBehavior behavior) {
        super.doVisit(target, behavior);

        Object value;
        if (behavior != null) {
            value = behavior.getExecute();
        } else {
            value = target.getAttributes().get("execute");
        }

        Collection<String> unresolvedExecuteIds = toCollection(value);
        // toCollection() returns copy of original set and we're free to modify it
        if (unresolvedExecuteIds.isEmpty()) {
            unresolvedExecuteIds.add(AjaxContainer.META_CLIENT_ID);
        } else if (!unresolvedExecuteIds.contains(AjaxContainer.META_CLIENT_ID)) {
            unresolvedExecuteIds.add(AjaxConstants.THIS);
        }

        executeIds = ContextUtils.INSTANCE.findComponentsFor(facesContext, target, unresolvedExecuteIds);
    }

    public Collection<String> getExecuteIds() {
        return (executeIds != null) ? executeIds : Collections.<String>emptySet();
    }
}

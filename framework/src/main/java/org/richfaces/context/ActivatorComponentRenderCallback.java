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

import org.richfaces.ui.ajax.ajax.AjaxClientBehavior;

/**
 * Callback that collects component attributes necessary for partial view rendering
 */
class ActivatorComponentRenderCallback extends ActivatorComponentCallbackBase {
    private Collection<String> renderIds = null;
    private boolean limitRender = false;
    private String oncomplete;
    private String onbeforedomupdate;
    private Object data;

    ActivatorComponentRenderCallback(FacesContext facesContext, String behaviorEvent) {
        super(facesContext, behaviorEvent);
    }

    public boolean isLimitRender() {
        return limitRender;
    }

    public String getOnbeforedomupdate() {
        return onbeforedomupdate;
    }

    public String getOncomplete() {
        return oncomplete;
    }

    public Object getData() {
        return data;
    }

    public Collection<String> getRenderIds() {
        return (renderIds != null) ? renderIds : Collections.<String>emptySet();
    }

    @Override
    protected void doVisit(UIComponent target, AjaxClientBehavior behavior) {
        Object renderValue;
        if (behavior != null) {
            renderValue = behavior.getRender();
            limitRender = behavior.isLimitRender();
            onbeforedomupdate = behavior.getOnbeforedomupdate();
            oncomplete = behavior.getOncomplete();
            data = behavior.getData();
        } else {
            renderValue = target.getAttributes().get("render");
            limitRender = isTrue(target.getAttributes().get("limitRender"));
            onbeforedomupdate = (String) target.getAttributes().get("onbeforedomupdate");
            oncomplete = (String) target.getAttributes().get("oncomplete");
            data = target.getAttributes().get("data");
        }

        Collection<String> unresolvedRenderIds = toCollection(renderValue);
        // NB: toCollection() returns copy of original set and we're free to modify it - not used here
        renderIds = ContextUtils.INSTANCE.findComponentsFor(facesContext, target, unresolvedRenderIds);
    }

    private boolean isTrue(Object value) {
        boolean result = false;

        if (value instanceof Boolean) {
            result = ((Boolean) value).booleanValue();
        } else {
            result = Boolean.valueOf(String.valueOf(value));
        }

        return result;
    }
}

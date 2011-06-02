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
import java.util.Collections;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.ajax4jsf.component.AjaxClientBehavior;
import org.richfaces.renderkit.util.CoreRendererUtils;

/**
 * @author akolonitsky
 * @since Oct 13, 2009
 */
class RenderComponentCallback extends ComponentCallback {
    private Collection<String> renderIds = null;
    private boolean limitRender = false;
    private String oncomplete;
    private String onbeforedomupdate;
    private Object data;

    RenderComponentCallback(FacesContext facesContext, String behaviorEvent) {
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
        renderIds = CoreRendererUtils.INSTANCE.findComponentsFor(facesContext, target, unresolvedRenderIds);
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

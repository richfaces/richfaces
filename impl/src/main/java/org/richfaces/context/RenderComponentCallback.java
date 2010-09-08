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

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.ajax4jsf.component.AjaxClientBehavior;
import org.richfaces.renderkit.util.CoreAjaxRendererUtils;

/**
 * @author akolonitsky
 * @since Oct 13, 2009
 */
class RenderComponentCallback extends ComponentCallback {

    private boolean limitRender = false;

    private String oncomplete;

    private String onbeforedomupdate;

    private Object data;

    RenderComponentCallback(String behaviorEvent) {
        super(behaviorEvent, null);
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

    @Override
    protected void doVisit(FacesContext context, UIComponent target, AjaxClientBehavior behavior) {
        super.doVisit(context, target, behavior);

        limitRender = CoreAjaxRendererUtils.isAjaxLimitRender(target);
        onbeforedomupdate = CoreAjaxRendererUtils.getAjaxOnBeforeDomUpdate(target);
        oncomplete = CoreAjaxRendererUtils.getAjaxOncomplete(target);
        data = CoreAjaxRendererUtils.getAjaxData(target);

        if (behavior != null) {
            limitRender = behavior.isLimitRender();
            onbeforedomupdate = behavior.getOnbeforedomupdate();
            oncomplete = behavior.getOncomplete();
            data = behavior.getData();
        }
    }

    @Override
    public Object getAttributeValue(UIComponent component) {
        return component.getAttributes().get("render");
    }

    @Override
    protected Object getBehaviorAttributeValue(AjaxClientBehavior behavior) {
        return behavior.getRender();
    }

}

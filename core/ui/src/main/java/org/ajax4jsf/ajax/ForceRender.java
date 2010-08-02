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



package org.ajax4jsf.ajax;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.ajax4jsf.context.AjaxContext;
import org.ajax4jsf.event.AjaxEvent;
import org.ajax4jsf.event.AjaxListener;
import org.ajax4jsf.event.AjaxRenderEvent;
import org.ajax4jsf.event.AjaxRenderListener;

/**
 * @author shura
 *
 */
public class ForceRender implements AjaxListener, AjaxRenderListener {

    /*
     *  (non-Javadoc)
     * @see org.ajax4jsf.framework.ajax.AjaxListener#processAjax(org.ajax4jsf.framework.ajax.AjaxEvent)
     */
    public void processAjax(AjaxEvent event) {
        UIComponent component = event.getComponent();
        AjaxRenderEvent renderEvent = new AjaxRenderEvent(component);

        renderEvent.setPhaseId(event.getPhaseId());
        component.queueEvent(renderEvent);
    }

    /*
     *  (non-Javadoc)
     * @see org.ajax4jsf.framework.ajax.AjaxRenderListener#processAjaxRender(org.ajax4jsf.framework.ajax.AjaxRenderEvent)
     */
    public void processAjaxRender(AjaxRenderEvent event) {
        FacesContext facesContext = FacesContext.getCurrentInstance();

        AjaxContext.getCurrentInstance(facesContext).renderAjax(facesContext);
    }
}

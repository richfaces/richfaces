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
package org.richfaces.ui.ajax.command;

import org.richfaces.log.Logger;
import org.richfaces.log.RichfacesLogger;
import org.richfaces.renderkit.RendererBase;
import org.richfaces.ui.behavior.HandlersChain;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import java.util.Map;

/**
 * @author asmirnov@exadel.com (latest modification by $Author: alexsmirnov $)
 * @version $Revision: 1.1.2.3 $ $Date: 2007/02/12 17:46:53 $
 *
 */
@ResourceDependencies({ @ResourceDependency(library = "javax.faces", name = "jsf.js"),
        @ResourceDependency(library = "org.richfaces", name = "jquery.js"),
        @ResourceDependency(library = "org.richfaces", name = "richfaces.js"),
        @ResourceDependency(library = "org.richfaces", name = "richfaces-queue.reslib") })
public abstract class AjaxCommandRendererBase extends RendererBase {
    private static final Logger LOG = RichfacesLogger.RENDERKIT.getLogger();

    @Override
    protected void queueComponentEventForBehaviorEvent(FacesContext context, UIComponent component, String eventName) {
        super.queueComponentEventForBehaviorEvent(context, component, eventName);

        if ("action".equals(eventName) || "click".equals(eventName)) {
            new ActionEvent(component).queue();
        }
    }

    @Override
    protected void doDecode(FacesContext facesContext, UIComponent uiComponent) {
        if (isSubmitted(facesContext, uiComponent)) {
            new ActionEvent(uiComponent).queue();
        }
    }

    protected boolean isSubmitted(FacesContext facesContext, UIComponent uiComponent) {
        if (getUtils().isBooleanAttribute(uiComponent, "disabled")) {
            return false;
        }

        String clientId = uiComponent.getClientId(facesContext);
        Map<String, String> paramMap = facesContext.getExternalContext().getRequestParameterMap();
        Object value = paramMap.get(clientId);
        boolean submitted = null != value;

        if (submitted && LOG.isDebugEnabled()) {
            LOG.debug("Decode submit of the Ajax component " + clientId);
        }

        return submitted;
    }

    public String getOnClick(FacesContext context, UIComponent component) {
        StringBuffer onClick = new StringBuffer();

        if (!getUtils().isBooleanAttribute(component, "disabled")) {
            HandlersChain handlersChain = new HandlersChain(context, component);

            handlersChain.addInlineHandlerFromAttribute("onclick");
            handlersChain.addBehaviors("click", "action");
            handlersChain.addAjaxSubmitFunction();

            String handlerScript = handlersChain.toScript();

            if (handlerScript != null) {
                onClick.append(handlerScript);
            }

            if (!"reset".equals(component.getAttributes().get("type"))) {
                onClick.append(";return false;");
            }
        } else {
            onClick.append("return false;");
        }

        return onClick.toString();
    }

    /*
     * public String getStringValue(UIComponent component) { Object value = getValue(component); return convertToString(value);
     * }
     *
     * protected String convertToString(Object obj ) { return ( obj == null ? "" : obj.toString() ); }
     *
     * protected String convertToString(boolean b ) { return String.valueOf(b); }
     *
     * protected String convertToString(int b ) { return b!=Integer.MIN_VALUE?String.valueOf(b):""; }
     *
     * protected String convertToString(long b ) { return b!=Long.MIN_VALUE?String.valueOf(b):""; }
     *
     * public void encodeChildren(FacesContext context, UIComponent component) throws IOException { renderChildren(context,
     * component); }
     *
     * public Object getValue(UIComponent component) { if (component instanceof ValueHolder) { return ((ValueHolder)
     * component).getValue(); } return component.getAttributes().get("value"); }
     *
     *
     * public String getType(UIComponent uiComponent) { String type; if (uiComponent instanceof HtmlCommandButton) { type =
     * ((HtmlCommandButton) uiComponent).getType(); } else { type = (String) uiComponent.getAttributes().get("type"); } if (type
     * == null) { type = "button"; } return type; }
     */
}

/*
 * JBoss, Home of Professional Open Source
 * Copyright , Red Hat, Inc. and individual contributors
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
package org.richfaces.renderkit;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.richfaces.javascript.JSFunction;
import org.richfaces.javascript.JSObject;
import org.richfaces.services.ServiceTracker;
import org.richfaces.component.AbstractNotifyMessage;
import org.richfaces.component.AbstractNotifyMessages;
import org.richfaces.component.util.HtmlUtil;
import org.richfaces.javascript.JavaScriptService;
import org.richfaces.renderkit.util.RendererUtils;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;

/**
 * @author <a href="http://community.jboss.org/people/lfryc">Lukas Fryc</a>
 */
public class NotifyMessageRendererBase extends MessageRendererBase {
    @Override
    protected String getJSClassName() {
        return "RichFaces.rf4.ui.NotifyMessage";
    }

    protected void encodeScript(FacesContext facesContext, UIComponent component, Map<String, Object> options)
            throws IOException {
        JavaScriptService javaScriptService = ServiceTracker.getService(JavaScriptService.class);
        JSFunction messageObject = new JSObject(getJSClassName(), component.getClientId(facesContext));
        Map<String, Object> attributes = component.getAttributes();
        Builder<String, Object> parametersBuilder = ImmutableMap.builder();
        String forId = (String) attributes.get("for");
        RendererUtils rendererUtils = RendererUtils.getInstance();
        if (!Strings.isNullOrEmpty(forId)) {
            UIComponent target = rendererUtils.findComponentFor(component, forId);
            if (null != target) {
                parametersBuilder.put("forComponentId", target.getClientId(facesContext));
            }
        }
        Severity level = getLevel(component);
        if (FacesMessage.SEVERITY_INFO != level) {
            parametersBuilder.put("level", level.getOrdinal());
        }
        if (!rendererUtils.isBooleanAttribute(component, "showSummary")) {
            parametersBuilder.put("showSummary", false);
        }
        if (rendererUtils.isBooleanAttribute(component, "showDetail")) {
            parametersBuilder.put("showDetail", true);
        }
        if (rendererUtils.isBooleanAttribute(component, "tooltip")) {
            parametersBuilder.put("tooltip", true);
        }
        if (isComponentMessages(component) && rendererUtils.isBooleanAttribute(component, "globalOnly")) {
            parametersBuilder.put("globalOnly", true);
        }
        if (isComponentMessages(component)) {
            parametersBuilder.put("isMessages", true);
        }
        messageObject.addParameter(parametersBuilder.build());
        messageObject.addParameter(options);
        // RendererUtils.getInstance().writeScript(facesContext, component, messageObject);
        javaScriptService.addPageReadyScript(facesContext, messageObject);
    }

    protected void encodeNotification(FacesContext facesContext, UIComponent component, Map<String, Object> options)
            throws IOException {
        JavaScriptService javaScriptService = ServiceTracker.getService(JavaScriptService.class);
        for (MessageForRender message : getVisibleMessages(facesContext, component)) {
            JSFunction notifyCall = new JSFunction("RichFaces.rf4.ui.Notify");
            Map<String, Object> optionsCopy = new LinkedHashMap<String, Object>(options);
            addMessageSpecificAttributes(message, facesContext, component, optionsCopy);
            notifyCall.addParameter(optionsCopy);
            javaScriptService.addPageReadyScript(facesContext, notifyCall);
            message.rendered();
        }
    }

    private void addMessageSpecificAttributes(MessageForRender message, FacesContext facesContext, UIComponent component,
            Map<String, Object> options) {
        Boolean showSummary = (Boolean) component.getAttributes().get("showSummary");
        Boolean showDetail = (Boolean) component.getAttributes().get("showDetail");
        String stackId = NotifyRendererUtils.getStackId(facesContext, component);

        boolean escape = true;
        if (component instanceof AbstractNotifyMessage) {
            escape = ((AbstractNotifyMessage) component).isEscape();
        }
        if (component instanceof AbstractNotifyMessages) {
            escape = ((AbstractNotifyMessages) component).isEscape();
        }

        options.put("severity", message.getSeverity().getOrdinal());

        if (showSummary != null && showSummary) {
            options.put("summary", escapeValue(message.getSummary(), escape));
        }

        if (showSummary != null && showDetail) {
            options.put("detail", escapeValue(message.getDetail(), escape));
        }

        if (stackId != null && !stackId.isEmpty()) {
            options.put("stackId", stackId);
        }
    }

    private static String escapeValue(String value, boolean escape) {
        return escape ? HtmlUtil.escapeHtml(value) : value;
    }
}

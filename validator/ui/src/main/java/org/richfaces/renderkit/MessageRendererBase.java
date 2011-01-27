/*
 * $Id$
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc. and individual contributors
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
import java.util.Iterator;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.component.UIComponent;
import javax.faces.component.UIMessage;
import javax.faces.component.UIMessages;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;

import org.ajax4jsf.javascript.JSFunction;
import org.ajax4jsf.javascript.JSObject;
import org.richfaces.application.ServiceTracker;
import org.richfaces.javascript.JavaScriptService;
import org.richfaces.renderkit.util.RendererUtils;

import com.google.common.base.Predicate;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.UnmodifiableIterator;

/**
 * <p class="changed_added_4_0">
 * </p>
 * 
 * @author asmirnov@exadel.com
 * 
 */
public class MessageRendererBase extends Renderer {

    private static final ImmutableMap<Severity, SeverityAttributes> SEVERITY_MAP = ImmutableMap.of(
        FacesMessage.SEVERITY_INFO, attrs("info", "inf", null), FacesMessage.SEVERITY_WARN, attrs("warn", "wrn", null),
        FacesMessage.SEVERITY_ERROR, attrs("error", "err", null), FacesMessage.SEVERITY_FATAL,
        attrs("fatal", "ftl", null));

    protected Iterator<FacesMessage> getMessages(FacesContext context, String forClientId, UIComponent component) {

        Iterator<FacesMessage> msgIter;

        if (forClientId != null) {

            if (forClientId.length() != 0) {

                UIComponent result = RendererUtils.getInstance().findComponentFor(component, forClientId);
                if (result == null) {
                    msgIter = Iterators.emptyIterator();
                } else {
                    msgIter = context.getMessages(result.getClientId(context));
                }

            } else {
                msgIter = context.getMessages(null);
            }

        } else {
            msgIter = context.getMessages();
        }

        return msgIter;
    }

    /**
     * <p class="changed_added_4_0">
     * TODO - make Generator aware of Iterator.
     * </p>
     * 
     * @param context
     * @param component
     * @return
     */
    protected Iterable<FacesMessage> getVisibleMessages(FacesContext context, UIComponent component) {
        String forId = getFor(component);
        Iterator<FacesMessage> messages = getMessages(context, forId, component);
        UnmodifiableIterator<FacesMessage> filteredMessages =
            Iterators.filter(messages, getMessagesLevelFilter(context, component));
        return Lists.newArrayList(filteredMessages);

    }

    private Predicate<FacesMessage> getMessagesLevelFilter(FacesContext context, UIComponent component) {

        final Severity level = getLevel(component);
        final boolean displayAll = component instanceof UIMessages;
        Predicate<FacesMessage> predicate = new Predicate<FacesMessage>() {
            private int count = 0;
            public boolean apply(FacesMessage input) {
                if(input.getSeverity().compareTo(level) >= 0){
                    return displayAll||0 == count++;
                }
                return false;
            }
        };
        return predicate;
    }

    private Severity getLevel(UIComponent component) {
        Object levelName = component.getAttributes().get("level");
        final Severity level =
            (Severity) (FacesMessage.VALUES_MAP.containsKey(levelName) ? FacesMessage.VALUES_MAP.get(levelName)
                : FacesMessage.SEVERITY_INFO);
        return level;
    }

    private String getFor(UIComponent component) {
        if (component instanceof UIMessages) {
            UIMessages messages = (UIMessages) component;
            if(messages.isGlobalOnly()){
                return "";
            } else {
                return messages.getFor();
            }
        } else if (component instanceof UIMessage) {
            UIMessage message = (UIMessage) component;
            return message.getFor();
        } else {
            return (String) component.getAttributes().get("for");
        }
    }

    protected void encodeMessage(FacesContext facesContext, UIComponent component, Object msg) throws IOException {
        // TODO fix generator to properly detect iteration variable type
        FacesMessage message = (FacesMessage) msg;
        String summary = message.getSummary();
        String detail = message.getDetail();
        boolean showSummary = true;
        boolean showDetail = false;
        if (component instanceof UIMessage) {
            UIMessage uiMessage = (UIMessage) component;
            showSummary = uiMessage.isShowSummary();
            showDetail = uiMessage.isShowDetail();
        } else if (component instanceof UIMessages) {
            UIMessages uiMessages = (UIMessages) component;
            showSummary = uiMessages.isShowSummary();
            showDetail = uiMessages.isShowDetail();
        }
        ResponseWriter responseWriter = facesContext.getResponseWriter();
        // tooltip
        boolean wroteTooltip = Boolean.TRUE.equals(component.getAttributes().get("tooltip"));
        if(wroteTooltip && !Strings.isNullOrEmpty(summary)){
            responseWriter.writeAttribute("title", summary,null);
        }
        if (!wroteTooltip && showSummary) {
            writeMessageLabel(responseWriter, summary, "rf-msg-sum");
        }
        if (showDetail) {
            writeMessageLabel(responseWriter, detail, "rf-msg-des");
        }
    }

    private void writeMessageLabel(ResponseWriter responseWriter, String label, String styleClass) throws IOException {
        if (!Strings.isNullOrEmpty(label)) {
            responseWriter.startElement("span", null);
            responseWriter.writeAttribute("class", styleClass, null);
            responseWriter.writeText(label, null);
            responseWriter.endElement("span");
        }
    }

    protected void encodeScript(FacesContext facesContext, UIComponent component) throws IOException {
        JavaScriptService javaScriptService = ServiceTracker.getService(JavaScriptService.class);
        JSFunction messageObject = new JSObject("RichFaces.ui.Message", component.getClientId(facesContext));
        Map<String, Object> attributes = component.getAttributes();
        Builder<String, Object> parametersBuilder = ImmutableMap.builder();
        String forId = (String) attributes.get("for");
        if (!Strings.isNullOrEmpty(forId)) {
            UIComponent target = RendererUtils.getInstance().findComponentFor(component, forId);
            if (null != target) {
                parametersBuilder.put("forComponentId",
                    target.getClientId(facesContext));
            }
        }
        Severity level = getLevel(component);
        if(FacesMessage.SEVERITY_INFO != level){
            parametersBuilder.put("level", level.getOrdinal());
        }
        if(!Boolean.TRUE.equals(attributes.get("showSummary"))){
            parametersBuilder.put("showSummary", false);
        }
        if(Boolean.TRUE.equals(attributes.get("showDetail"))){
            parametersBuilder.put("showDetail", true);
        }
        if(Boolean.TRUE.equals(attributes.get("tooltip"))){
            parametersBuilder.put("tooltip", true);
        }
        messageObject.addParameter(parametersBuilder.build());
        // RendererUtils.getInstance().writeScript(facesContext, component, messageObject);
        javaScriptService.addPageReadyScript(facesContext, messageObject);
    }

    protected String getMsgClass(FacesContext facesContext, UIComponent component, Object msg) throws IOException {
        FacesMessage message = (FacesMessage) msg;
        SeverityAttributes severityAttributes = SEVERITY_MAP.get(message.getSeverity());
        String styleClass =
            buildSeverityAttribute(component, severityAttributes.skinClass, severityAttributes.classAttribute, ' ');
        return styleClass;
    }

    protected String getMsgStyle(FacesContext facesContext, UIComponent component, Object msg) throws IOException {
        FacesMessage message = (FacesMessage) msg;
        SeverityAttributes severityAttributes = SEVERITY_MAP.get(message.getSeverity());
        String style =
            buildSeverityAttribute(component, severityAttributes.skinStyle, severityAttributes.styleAttribute, ';');
        return style;
    }

    private String buildSeverityAttribute(UIComponent component, String skinValue, String attrName, char delimiter) {
        StringBuilder style = new StringBuilder();
        if (!Strings.isNullOrEmpty(skinValue)) {
            style.append(skinValue);
        }
        Object componentStyle = component.getAttributes().get(attrName);
        if (null != componentStyle && !Strings.isNullOrEmpty(componentStyle.toString())) {
            if (!Strings.isNullOrEmpty(skinValue)) {
                style.append(delimiter);
            }
            style.append(componentStyle);
        }
        return style.toString();
    }

    static SeverityAttributes attrs(String attPrefix, String skinSuffix, String skinStyle) {
        SeverityAttributes attrs =
            new SeverityAttributes(attPrefix + "Style", attPrefix + "Class", "rf-msg-" + skinSuffix, skinStyle, null,
                null);
        return attrs;
    }

    private static final class SeverityAttributes {
        private final String styleAttribute;
        private final String classAttribute;
        private final String skinStyle;
        private final String skinClass;
        private final String labelStyleAttribute;
        private final String labelClassAttribute;

        private SeverityAttributes(String styleAttribute2, String classAttribute2, String markerStyleAttribute2,
            String markerClassAttribute2, String labelStyleAttribute2, String labelClassAttribute2) {
            this.styleAttribute = styleAttribute2;
            this.classAttribute = classAttribute2;
            this.skinStyle = markerStyleAttribute2;
            this.skinClass = markerClassAttribute2;
            this.labelStyleAttribute = labelStyleAttribute2;
            this.labelClassAttribute = labelClassAttribute2;
        }
    }
}

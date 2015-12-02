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

import org.ajax4jsf.javascript.JSFunction;
import org.ajax4jsf.javascript.JSObject;
import org.richfaces.application.ServiceTracker;
import org.richfaces.component.AbstractMessage;
import org.richfaces.component.AbstractMessages;
import org.richfaces.javascript.JavaScriptService;
import org.richfaces.renderkit.util.RendererUtils;

import com.google.common.base.Predicate;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.common.collect.ImmutableSet;
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
public class MessageRendererBase extends RendererBase {
    private static final ImmutableMap<Severity, SeverityAttributes> SEVERITY_MAP = ImmutableMap.of(FacesMessage.SEVERITY_INFO,
            attrs("info", "inf"), FacesMessage.SEVERITY_WARN, attrs("warn", "wrn"), FacesMessage.SEVERITY_ERROR,
            attrs("error", "err"), FacesMessage.SEVERITY_FATAL, attrs("fatal", "ftl"));

    protected Iterator<MessageForRender> getMessages(FacesContext context, String forClientId, UIComponent component) {

        Iterator<MessageForRender> msgIter;

        if (forClientId != null) {

            if (forClientId.length() != 0) {

                UIComponent result = RendererUtils.getInstance().findComponentFor(component, forClientId);
                if (result == null) {
                    msgIter = ImmutableSet.<MessageForRender>of().iterator();
                } else {
                    String clientId = result.getClientId(context);
                    msgIter = getMessagesForId(context, clientId);
                }
            } else {
                msgIter = getMessagesForId(context, null);
            }
        } else {
            msgIter = ImmutableSet.<MessageForRender>of().iterator();
            Iterator<String> clientIdsWithMessages = context.getClientIdsWithMessages();
            while (clientIdsWithMessages.hasNext()) {
                String clientId = (String) clientIdsWithMessages.next();
                msgIter = Iterators.concat(msgIter, getMessagesForId(context, clientId));
            }
        }

        return msgIter;
    }

    private Iterator<MessageForRender> getMessagesForId(FacesContext context, String clientId) {
        Iterator<MessageForRender> msgIter;
        msgIter = Iterators.transform(context.getMessages(clientId), new MessageTransformer(null == clientId ? "" : clientId));
        return msgIter;
    }

    /**
     * <p class="changed_added_4_0">
     * TODO - make Generator aware of Iterator.
     * </p>
     *
     * @param context
     * @param component
     */
    protected Iterable<MessageForRender> getVisibleMessages(FacesContext context, UIComponent component) {
        String forId = getFor(component);
        Iterator<MessageForRender> messages = getMessages(context, forId, component);
        UnmodifiableIterator<MessageForRender> filteredMessages = Iterators.filter(messages,
                getMessagesLevelFilter(context, component));
        return Lists.newArrayList(filteredMessages);
    }

    private Predicate<MessageForRender> getMessagesLevelFilter(FacesContext context, UIComponent component) {

        final Severity level = getLevel(component);
        final boolean displayAll = isComponentMessages(component);
        final boolean redisplay = Boolean.TRUE.equals(component.getAttributes().get("redisplay"));
        Predicate<MessageForRender> predicate = new Predicate<MessageForRender>() {
            private int count = 0;

            public boolean apply(MessageForRender input) {
                if (redisplay || !input.isRendered()) {
                    if (input.getSeverity().compareTo(level) >= 0) {
                        return displayAll || 0 == count++;
                    }
                }
                return false;
            }
        };
        return predicate;
    }

    protected Severity getLevel(UIComponent component) {
        Object levelName = component.getAttributes().get("level");
        final Severity level = (Severity) (FacesMessage.VALUES_MAP.containsKey(levelName) ? FacesMessage.VALUES_MAP
                .get(levelName) : FacesMessage.SEVERITY_INFO);
        return level;
    }

    protected boolean isComponentMessage(UIComponent component) {
        return component instanceof UIMessage;
    }

    protected boolean isComponentRichMessage(UIComponent component) {
        return component instanceof AbstractMessage;
    }

    protected boolean isComponentMessages(UIComponent component) {
        return component instanceof UIMessages;
    }

    protected boolean isComponentRichMessages(UIComponent component) {
        return component instanceof AbstractMessages;
    }

    private String getFor(UIComponent component) {
        if (isComponentMessages(component)) {
            UIMessages messages = (UIMessages) component;
            if (messages.isGlobalOnly()) {
                return "";
            } else {
                return messages.getFor();
            }
        } else if (isComponentMessage(component)) {
            UIMessage message = (UIMessage) component;
            return message.getFor();
        } else {
            return (String) component.getAttributes().get("for");
        }
    }

    protected void encodeMessage(FacesContext facesContext, UIComponent component, Object msg) throws IOException {
        // TODO fix generator to properly detect iteration variable type
        MessageForRender message = (MessageForRender) msg;
        String summary = message.getSummary();
        String detail = message.getDetail();
        boolean showSummary = true;
        boolean showDetail = false;
        boolean isMessages = false;
        if (isComponentMessage(component)) {
            UIMessage uiMessage = (UIMessage) component;
            showSummary = uiMessage.isShowSummary();
            showDetail = uiMessage.isShowDetail();
        } else if (isComponentMessages(component)) {
            UIMessages uiMessages = (UIMessages) component;
            showSummary = uiMessages.isShowSummary();
            showDetail = uiMessages.isShowDetail();
            isMessages = true;
        }
        boolean escape = true;
        if (isComponentRichMessage(component)) {
            AbstractMessage richMessage = (AbstractMessage) component;
            escape = richMessage.isEscape();
        }
        if (isComponentRichMessages(component)) {
            AbstractMessages richMessages = (AbstractMessages) component;
            escape = richMessages.isEscape();
        }
        ResponseWriter responseWriter = facesContext.getResponseWriter();
        // Message id
        responseWriter.writeAttribute("id", component.getClientId() + ':' + message.getSourceId(), null);
        // tooltip
        boolean wroteTooltip = RendererUtils.getInstance().isBooleanAttribute(component, "tooltip");
        if (wroteTooltip && !Strings.isNullOrEmpty(summary)) {
            responseWriter.writeAttribute("title", summary, null);
        }
        if (!wroteTooltip && showSummary) {
            writeMessageLabel(responseWriter, summary, isMessages ? "rf-msgs-sum" : "rf-msg-sum", escape);
        }
        if (showDetail) {
            writeMessageLabel(responseWriter, detail, isMessages ? "rf-msgs-det" : "rf-msg-det", escape);
        }
        message.rendered();
    }

    private void writeMessageLabel(ResponseWriter responseWriter, String label, String styleClass, boolean escape) throws IOException {
        if (!Strings.isNullOrEmpty(label)) {
            responseWriter.startElement("span", null);
            responseWriter.writeAttribute("class", styleClass, null);
            if (escape) {
                responseWriter.writeText(label, null);
            } else {
                responseWriter.write(label);
            }
            responseWriter.endElement("span");
        }
    }

    protected String getJSClassName() {
        return "RichFaces.ui.Message";
    }

    protected void encodeScript(FacesContext facesContext, UIComponent component) throws IOException {
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
        // RendererUtils.getInstance().writeScript(facesContext, component, messageObject);
        javaScriptService.addPageReadyScript(facesContext, messageObject);
    }

    protected String getMsgClass(FacesContext facesContext, UIComponent component, Object msg) throws IOException {
        MessageForRender message = (MessageForRender) msg;
        SeverityAttributes severityAttributes = SEVERITY_MAP.get(message.getSeverity());

        boolean isMessages = (isComponentMessages(component));

        String styleClass = buildSeverityAttribute(component, (isMessages ? severityAttributes.messagesSkinClass
                : severityAttributes.messageSkinClass), severityAttributes.classAttribute, ' ');
        return styleClass;
    }

    protected String getMsgStyle(FacesContext facesContext, UIComponent component, Object msg) throws IOException {
        MessageForRender message = (MessageForRender) msg;
        SeverityAttributes severityAttributes = SEVERITY_MAP.get(message.getSeverity());
        String style = buildSeverityAttribute(component, null, severityAttributes.styleAttribute, ';');
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

    static SeverityAttributes attrs(String attPrefix, String skinSuffix) {
        SeverityAttributes attrs = new SeverityAttributes(attPrefix, skinSuffix);
        return attrs;
    }

    private static final class SeverityAttributes {
        private final String styleAttribute;
        private final String classAttribute;
        private final String messageSkinClass;
        private final String messagesSkinClass;

        private SeverityAttributes(String attPrefix, String skinSuffix) {
            this.styleAttribute = attPrefix + "Style";
            this.classAttribute = attPrefix + "Class";
            this.messageSkinClass = "rf-msg-" + skinSuffix;
            this.messagesSkinClass = "rf-msgs-" + skinSuffix;
        }
    }
}

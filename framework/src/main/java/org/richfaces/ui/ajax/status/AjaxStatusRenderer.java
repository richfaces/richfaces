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
package org.richfaces.ui.ajax.status;

import static org.richfaces.renderkit.RenderKitUtils.addToScriptHash;
import static org.richfaces.renderkit.RenderKitUtils.renderAttribute;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.javascript.JSFunction;
import org.richfaces.renderkit.RenderKitUtils.ScriptHashVariableWrapper;
import org.richfaces.renderkit.RendererBase;
import org.richfaces.ui.behavior.HandlersChain;
import org.richfaces.ui.common.HtmlConstants;
import org.richfaces.util.HtmlUtil;

/**
 * @author Nick Belaevski
 */
@ResourceDependencies({ @ResourceDependency(library = "javax.faces", name = "jsf.js"),
        @ResourceDependency(library = "org.richfaces", name = "jquery.js"),
        @ResourceDependency(library = "org.richfaces", name = "richfaces.js"),
        @ResourceDependency(library = "org.richfaces", name = "richfaces-queue.reslib"),
        @ResourceDependency(library = "org.richfaces", name = "common/richfaces-base-component.js"),
        @ResourceDependency(library = "org.richfaces", name = "ajax/status.js") })
@JsfRenderer(type = "org.richfaces.ui.StatusRenderer", family = AbstractAjaxStatus.COMPONENT_FAMILY)
public class AjaxStatusRenderer extends RendererBase {
    private static final String START = "start";
    private static final String STOP = "stop";
    private static final String ERROR = "error";
    private static final String[] EVENT_NAMES = { "start", "stop", "error", "success" };

    private static enum StatusState {
        // NOTE: states encode order is important for script!
        start(START),
        error(ERROR) {
            {
                this.setOptional();
            }
        },
        stop(STOP) {
            {
                this.setInitial();
            }
        };
        private String stateName;
        private String styleAttributeName;
        private String styleClassAttributeName;
        private String textAttributeName;
        private String defaultStyleClass;
        private boolean initial;
        private boolean optional;

        private StatusState(String stateName) {
            this.stateName = stateName;
            this.styleAttributeName = stateName + "Style";
            this.styleClassAttributeName = stateName + "StyleClass";
            this.textAttributeName = stateName + "Text";
            this.defaultStyleClass = "rf-st-" + stateName;
        }

        public String getStyleAttributeName() {
            return styleAttributeName;
        }

        public String getStyleClassAttributeName() {
            return styleClassAttributeName;
        }

        public String getFacetName() {
            return stateName;
        }

        public String getTextAttributeName() {
            return textAttributeName;
        }

        public String getDefaultStyleClass() {
            return defaultStyleClass;
        }

        public boolean isInitial() {
            return initial;
        }

        public boolean isOptional() {
            return optional;
        }

        protected void setOptional() {
            this.optional = true;
        }

        protected void setInitial() {
            this.initial = true;
        }
    }

    protected void encodeState(FacesContext facesContext, AbstractAjaxStatus status, StatusState state) throws IOException {

        Map<String, Object> statusAttributes = status.getAttributes();
        UIComponent stateFacet = status.getFacet(state.getFacetName());
        String stateText = null;

        if (stateFacet == null) {
            stateText = (String) statusAttributes.get(state.getTextAttributeName());
        }

        if (state.isOptional() && stateFacet == null && stateText == null) {
            return;
        }

        ResponseWriter writer = facesContext.getResponseWriter();
        writer.startElement(HtmlConstants.SPAN_ELEM, status);

        String stateStyle = (String) statusAttributes.get(state.getStyleAttributeName());

        renderAttribute(facesContext, HtmlConstants.STYLE_ATTRIBUTE,
            HtmlUtil.concatStyles(stateStyle, state.isInitial() ? null : "display:none"));

        String stateStyleClass = (String) statusAttributes.get(state.getStyleClassAttributeName());

        renderAttribute(facesContext, HtmlConstants.CLASS_ATTRIBUTE,
            HtmlUtil.concatClasses(state.getDefaultStyleClass(), stateStyleClass));

        if (stateFacet != null && stateFacet.isRendered()) {
            stateFacet.encodeAll(facesContext);
        } else {
            if (stateText != null) {
                writer.writeText(stateText, null);
            }
        }

        writer.endElement(HtmlConstants.SPAN_ELEM);
    }

    @Override
    public void doEncodeEnd(ResponseWriter writer, FacesContext context, UIComponent component) throws IOException {
        super.doEncodeEnd(writer, context, component);

        AbstractAjaxStatus ajaxStatus = (AbstractAjaxStatus) component;
        writer.startElement(HtmlConstants.SPAN_ELEM, component);
        String clientId = component.getClientId(context);
        writer.writeAttribute(HtmlConstants.ID_ATTRIBUTE, clientId, "id");

        for (StatusState state : StatusState.values()) {
            encodeState(context, ajaxStatus, state);
        }

        writer.startElement(HtmlConstants.SCRIPT_ELEM, component);
        writer.writeAttribute(HtmlConstants.TYPE_ATTR, HtmlConstants.TEXT_JAVASCRIPT_TYPE, null);

        JSFunction statusConstructor = new JSFunction("new RichFaces.ui.Status", clientId);

        Map<String, Object> options = new HashMap<String, Object>();

        Map<String, Object> attributes = ajaxStatus.getAttributes();
        for (String eventName : EVENT_NAMES) {
            String eventAttribute = "on" + eventName;
            HandlersChain handlersChain = new HandlersChain(context, component, true);
            handlersChain.addInlineHandlerFromAttribute(eventAttribute);
            handlersChain.addBehaviors(eventName);
            addToScriptHash(options, eventAttribute, handlersChain.toScript(), null, ScriptHashVariableWrapper.eventHandler);
        }

        addToScriptHash(options, "statusName", attributes.get("name"));

        if (!options.isEmpty()) {
            statusConstructor.addParameter(options);
        }

        writer.writeText(statusConstructor.toScript(), null);
        writer.endElement(HtmlConstants.SCRIPT_ELEM);

        writer.endElement(HtmlConstants.SPAN_ELEM);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.renderkit.RendererBase#getComponentClass()
     */
    @Override
    protected Class<? extends UIComponent> getComponentClass() {
        return AbstractAjaxStatus.class;
    }
}

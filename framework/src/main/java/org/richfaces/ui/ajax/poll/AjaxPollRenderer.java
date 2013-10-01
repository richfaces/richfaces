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
package org.richfaces.ui.ajax.poll;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.PartialViewContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.ActionEvent;

import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.javascript.JSFunction;
import org.richfaces.javascript.JSFunctionDefinition;
import org.richfaces.javascript.JSReference;
import org.richfaces.renderkit.RenderKitUtils;
import org.richfaces.renderkit.RendererBase;
import org.richfaces.ui.behavior.HandlersChain;
import org.richfaces.ui.common.HtmlConstants;
import org.richfaces.util.RendererUtils;

/**
 * @author shura
 */
@ResourceDependencies({ @ResourceDependency(library = "org.richfaces", name = "ajax.reslib"),
        @ResourceDependency(library = "org.richfaces", name = "base-component.reslib"),
        @ResourceDependency(library = "org.richfaces", name = "ajax/poll.js") })
@JsfRenderer
public class AjaxPollRenderer extends RendererBase {
    public static final String COMPONENT_FAMILY = "org.richfaces.ui.Poll";
    public static final String RENDERER_TYPE = "org.richfaces.ui.PollRenderer";
    private static final String AJAX_POLL_FUNCTION = "new RichFaces.ui.Poll";
    private static final String ENABLED = "enabled";

    private void addComponentToAjaxRender(FacesContext context, UIComponent component) {
        PartialViewContext pvc = context.getPartialViewContext();
        pvc.getRenderIds().add(component.getClientId(context));
    }

    @Override
    protected void queueComponentEventForBehaviorEvent(FacesContext context, UIComponent component, String eventName) {
        super.queueComponentEventForBehaviorEvent(context, component, eventName);

        if (AbstractPoll.TIMER.equals(eventName)) {
            new ActionEvent(component).queue();
            addComponentToAjaxRender(context, component);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.renderkit.RendererBase#doEncodeEnd(javax.faces.context.ResponseWriter,
     * javax.faces.context.FacesContext, javax.faces.component.UIComponent)
     */

    protected void doEncodeEnd(ResponseWriter writer, FacesContext context, UIComponent component) throws IOException {

        RendererUtils utils = getUtils();
        boolean shouldRenderForm = utils.getNestingForm(component) == null;
        String rootElementName = shouldRenderForm ? HtmlConstants.DIV_ELEM : HtmlConstants.SPAN_ELEM;

        AbstractPoll poll = (AbstractPoll) component;
        writer.startElement(rootElementName, component);
        writer.writeAttribute(HtmlConstants.STYLE_ATTRIBUTE, "display:none;", null);
        utils.encodeId(context, component);

        if (shouldRenderForm) {
            String clientId = component.getClientId(context) + RendererUtils.DUMMY_FORM_ID;
            utils.encodeBeginForm(context, component, writer, clientId);
            utils.encodeEndForm(context, writer);
        }

        // polling script.
        writer.startElement(HtmlConstants.SCRIPT_ELEM, component);
        writer.writeAttribute(HtmlConstants.TYPE_ATTR, "text/javascript", null);
        StringBuffer script = new StringBuffer("");

        JSFunction function = new JSFunction(AJAX_POLL_FUNCTION);
        Map<String, Object> options = new HashMap<String, Object>();

        RenderKitUtils.addToScriptHash(options, "interval", poll.getInterval(), "1000");
        // RenderKitUtils.addToScriptHash(options, "pollId", component.getClientId(context));
        HandlersChain handlersChain = new HandlersChain(context, poll);
        handlersChain.addInlineHandlerFromAttribute(AbstractPoll.ON_TIMER);
        handlersChain.addBehaviors(AbstractPoll.TIMER);
        handlersChain.addAjaxSubmitFunction();

        String handler = handlersChain.toScript();

        if (handler != null) {
            JSFunctionDefinition timerHandler = new JSFunctionDefinition(JSReference.EVENT);
            timerHandler.addToBody(handler);
            options.put(AbstractPoll.ON_TIMER, timerHandler);
        }
        if (poll.isEnabled()) {
            options.put(ENABLED, true);
        }
        function.addParameter(component.getClientId(context));
        function.addParameter(options);
        // function.appendScript(script);
        writer.writeText(function.toScript(), null);

        writer.endElement(HtmlConstants.SCRIPT_ELEM);

        writer.endElement(rootElementName);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.renderkit.RendererBase#getComponentClass()
     */

    protected Class<? extends UIComponent> getComponentClass() {
        // only push component is allowed.
        return AbstractPoll.class;
    }

    @Override
    protected void doDecode(FacesContext context, UIComponent component) {
        super.doDecode(context, component);

        AbstractPoll poll = (AbstractPoll) component;
        if (poll.isEnabled()) {
            Map<String, String> requestParameterMap = context.getExternalContext().getRequestParameterMap();
            if (requestParameterMap.get(poll.getClientId(context)) != null) {
                new ActionEvent(poll).queue();
                addComponentToAjaxRender(context, component);
            }
        }
    }
}

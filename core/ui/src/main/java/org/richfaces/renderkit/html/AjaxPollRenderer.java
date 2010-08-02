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

package org.richfaces.renderkit.html;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.ActionEvent;

import org.ajax4jsf.javascript.JSFunction;
import org.ajax4jsf.javascript.JSFunctionDefinition;
import org.ajax4jsf.javascript.JSReference;
import org.ajax4jsf.renderkit.HandlersChain;
import org.ajax4jsf.renderkit.RendererBase;
import org.ajax4jsf.renderkit.RendererUtils.HTML;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.component.AbstractPoll;

/**
 * @author shura
 */
@ResourceDependencies(value = {
        @ResourceDependency(library = "javax.faces", name = "jsf.js"),
        @ResourceDependency(name = "jquery.js"),
        @ResourceDependency(name = "richfaces.js")})
@JsfRenderer
public class AjaxPollRenderer extends RendererBase {

    public static final String COMPONENT_FAMILY = "org.richfaces.Poll";

    public static final String RENDERER_TYPE = "org.richfaces.PollRenderer";
    private static final String AJAX_POLL_FUNCTION = "RichFaces.startPoll";

    @Override
    protected void queueComponentEventForBehaviorEvent(FacesContext context, UIComponent component, String eventName) {
        super.queueComponentEventForBehaviorEvent(context, component, eventName);
        
        if (AbstractPoll.TIMER.equals(eventName)) {
            new ActionEvent(component).queue();
        } 
    }

    /*
      * (non-Javadoc)
      *
      * @see org.ajax4jsf.renderkit.RendererBase#doEncodeEnd(javax.faces.context.ResponseWriter,
      *      javax.faces.context.FacesContext, javax.faces.component.UIComponent)
      */

    protected void doEncodeEnd(ResponseWriter writer, FacesContext context,
                               UIComponent component) throws IOException {
        AbstractPoll poll = (AbstractPoll) component;
        writer.startElement(HTML.SPAN_ELEM, component);
        writer.writeAttribute(HTML.STYLE_ATTRIBUTE, "display:none;", null);
        getUtils().encodeId(context, component);
        getUtils().encodeBeginFormIfNessesary(context, component);
        // polling script.
        writer.startElement(HTML.SCRIPT_ELEM, component);
        writer.writeAttribute(HTML.TYPE_ATTR, "text/javascript", null);
        StringBuffer script = new StringBuffer("\n");
        if (poll.isEnabled()) {
            JSFunction function = new JSFunction(AJAX_POLL_FUNCTION);
            Map<String, Object> options = new HashMap<String, Object>();
            Integer interval = new Integer(poll.getInterval());
            options.put("pollinterval", interval);
            options.put("pollId", component.getClientId(context));
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
            function.addParameter(options);
            function.appendScript(script);
        } else {
            script.append("RichFaces.stopPoll('").append(component.getClientId(context)).append("')");
        }
        script.append(";\n");
        writer.writeText(script.toString(), null);
        writer.endElement(HTML.SCRIPT_ELEM);
        getUtils().encodeEndFormIfNessesary(context, component);
        writer.endElement(HTML.SPAN_ELEM);
    }

    /*
      * (non-Javadoc)
      *
      * @see org.ajax4jsf.renderkit.RendererBase#getComponentClass()
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
            }
        }
    }

}

/*
 * JBoss, Home of Professional Open Source
 * Copyright ${year}, Red Hat, Inc. and individual contributors
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
import java.util.HashMap;
import java.util.Map;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.ajax4jsf.javascript.JSFunction;
import org.ajax4jsf.javascript.JSFunctionDefinition;
import org.ajax4jsf.javascript.JSLiteral;
import org.ajax4jsf.javascript.JSReference;
import org.ajax4jsf.renderkit.AjaxEventOptions;
import org.ajax4jsf.renderkit.AjaxRendererUtils;
import org.ajax4jsf.renderkit.RendererBase;
import org.ajax4jsf.renderkit.RendererUtils.HTML;
import org.richfaces.component.AbstractDataScroller;
import org.richfaces.event.DataScrollerEvent;

@ResourceDependencies( { @ResourceDependency(library = "javax.faces", name = "jsf-uncompressed.js"),
    @ResourceDependency(name = "jquery.js"), @ResourceDependency(name = "richfaces.js"),
    @ResourceDependency(name = "richfaces-event.js"),
    @ResourceDependency(name = "richfaces-base-component.js"), 
    @ResourceDependency(library = "org.richfaces", name = "datascroller.js"),
    @ResourceDependency(library = "org.richfaces", name = "datascroller.ecss")

})
public class DataScrollerBaseRenderer extends RendererBase {

    public void doDecode(FacesContext context, UIComponent component) {
        Map<String, String> paramMap = context.getExternalContext().getRequestParameterMap();

        String clientId = component.getClientId(context);
        String param = (String) paramMap.get(clientId + ":page");

        if (param != null) {

            AbstractDataScroller scroller = (AbstractDataScroller) component;
            int newPage = scroller.getPageForFacet(param);
            int page = scroller.getPage();

            if (newPage != 0 && newPage != page) {
                new DataScrollerEvent(scroller, String.valueOf(page), param, newPage).queue();
            }
        }
    }

    public ControlsState getControlsState(FacesContext context, UIComponent component) {

        int fastStep = (Integer) component.getAttributes().get("fastStep");
        int pageIndex = (Integer) component.getAttributes().get("page");
        int pageCount = (Integer) component.getAttributes().get("pageCount");

        int minPageIdx = 1;
        int maxPageIdx = pageCount;

        if (fastStep <= 1) {
            fastStep = 1;
        }

        boolean useFirst = true;
        boolean useLast = true;

        boolean useBackFast = true;
        boolean useForwFast = true;

        ControlsState controlsState = new ControlsState();

        if (pageIndex <= minPageIdx) {
            useFirst = false;
        }

        if (pageIndex >= maxPageIdx) {
            useLast = false;
        }

        if (pageIndex - fastStep < minPageIdx) {
            useBackFast = false;
        }

        if (pageIndex + fastStep > maxPageIdx) {
            useForwFast = false;
        }

        String boundaryControls = (String) component.getAttributes().get("boundaryControls");
        String fastControls = (String) component.getAttributes().get("fastControls");
        
        boolean isAuto = "auto".equals(boundaryControls); 
        if (isAuto || "show".equals(boundaryControls)) {
            if (isAuto) {
                controlsState.setFirstRendered(useFirst);
                controlsState.setLastRendered(useLast);
            }

            controlsState.setFirstEnabled(useFirst);
            controlsState.setLastEnabled(useLast);
        } else {
            controlsState.setFirstRendered(false);
            controlsState.setLastRendered(false);
        }

        isAuto = "auto".equals(fastControls);
        if (isAuto || "show".equals(fastControls)) {
            if (isAuto) {
                controlsState.setFastForwardRendered(useForwFast);
                controlsState.setFastRewindRendered(useBackFast);
            }

            controlsState.setFastForwardEnabled(useForwFast);
            controlsState.setFastRewindEnabled(useBackFast);
        } else {
            controlsState.setFastForwardRendered(false);
            controlsState.setFastRewindRendered(false);
        }

        UIComponent controlsSeparatorFacet = component.getFacet("controlsSeparator");
        if (controlsSeparatorFacet != null && controlsSeparatorFacet.isRendered()) {
            controlsState.setControlsSeparatorRendered(true);
        }

        return controlsState;
    }

    public Map<String, String> renderPager(ResponseWriter out, FacesContext context, UIComponent component)
        throws IOException {

        int currentPage = (Integer) component.getAttributes().get("page");
        int maxPages = (Integer) component.getAttributes().get("maxPages");
        int pageCount = (Integer) component.getAttributes().get("pageCount");

        Map<String, String> digital = new HashMap<String, String>();

        if (pageCount <= 1) {
            return digital;
        }

        if (maxPages <= 1) {
            maxPages = 1;
        }

        int delta = maxPages / 2;

        int pages;
        int start;

        if (pageCount > maxPages && currentPage > delta) {
            pages = maxPages;
            start = currentPage - pages / 2 - 1;
            if (start + pages > pageCount) {
                start = pageCount - pages;
            }
        } else {
            pages = pageCount < maxPages ? pageCount : maxPages;
            start = 0;
        }

        String clientId = component.getClientId(context);
        
        int size = start + pages;
        for (int i = start ; i < size; i++) {

            boolean isCurrentPage = (i + 1 == currentPage);
            String styleClass;
            String style;

            if (isCurrentPage) {
                styleClass = (String) component.getAttributes().get("selectedStyleClass");
                style = (String) component.getAttributes().get("selectedStyle");
            } else {
                styleClass = (String) component.getAttributes().get("inactiveStyleClass");
                style = (String) component.getAttributes().get("inactiveStyle");
            }

            if (styleClass == null) {
                styleClass = "";
            }


            if (isCurrentPage) {
                out.startElement(HTML.SPAN_ELEM, component);
                out.writeAttribute(HTML.CLASS_ATTRIBUTE, "rf-ds-dtl rf-ds-cur " + styleClass, null);
            } else {
                out.startElement(HTML.A_ELEMENT, component);
                out.writeAttribute(HTML.CLASS_ATTRIBUTE, "rf-ds-dtl " + styleClass, null);
                out.writeAttribute(HTML.HREF_ATTR, "javascript:void(0);", null);
            }


            if (null != style) {
                out.writeAttribute(HTML.STYLE_ATTRIBUTE, style, null);
            }

            String page = Integer.toString(i + 1);
            String id = clientId + "_ds_" + page;

            out.writeAttribute(HTML.ID_ATTRIBUTE, id, null);

            digital.put(id, page);

            out.writeText(page, null);
        
            if(isCurrentPage) {
                out.endElement(HTML.SPAN_ELEM);
            } else {
                out.endElement(HTML.A_ELEMENT);
            }
        }

        return digital;
    }

    public Map<String, Map<String, String>> getControls(FacesContext context, UIComponent component,
        ControlsState controlsState) {

        Map<String, Map<String, String>> controls = new HashMap<String, Map<String, String>>();
        Map<String, String> right = new HashMap<String, String>();
        Map<String, String> left = new HashMap<String, String>();

        String clientId = component.getClientId(context);

        if (controlsState.getFirstRendered() && controlsState.getFirstEnabled()) {
            left.put(clientId + "_ds_f", AbstractDataScroller.FIRST_FACET_NAME);
        }

        if (controlsState.getFastRewindRendered() && controlsState.getFastRewindEnabled()) {
            left.put(clientId + "_ds_fr", AbstractDataScroller.FAST_REWIND_FACET_NAME);
        }

        if (controlsState.getFastForwardRendered() && controlsState.getFastForwardEnabled()) {
            right.put(clientId + "_ds_ff", AbstractDataScroller.FAST_FORWARD_FACET_NAME);
        }

        if (controlsState.getLastRendered() && controlsState.getLastEnabled()) {
            right.put(clientId + "_ds_l", AbstractDataScroller.LAST_FACET_NAME);
        }

        if (!left.isEmpty()) {
            controls.put("left", left);
        }

        if (!right.isEmpty()) {
            controls.put("right", right);
        }
        return controls;
    }

    public void buildScript(ResponseWriter writer, FacesContext context, UIComponent component, Map buttons,
        Map digitals) throws IOException {

        JSFunction function = new JSFunction("new RichFaces.ui.DataScroller");
        function.addParameter(component.getClientId(context));

        Map<String, Object> options = new HashMap<String, Object>();
        options.put("buttons", buttons);
        options.put("digitals", digitals);
        options.put("currentPage", (Integer) component.getAttributes().get("page"));

        function.addParameter(getSubmitFunction(context, component));
        function.addParameter(options);

        writer.write(function.toString());
    }

    public JSFunctionDefinition getSubmitFunction(FacesContext facesContext, UIComponent component) {
        JSFunctionDefinition definition = new JSFunctionDefinition(JSReference.EVENT, new JSReference("element"),
            new JSReference("data"));

        JSFunction function = AjaxRendererUtils.buildAjaxFunction(facesContext, component,
            AjaxRendererUtils.AJAX_FUNCTION_NAME);
        AjaxEventOptions options = AjaxRendererUtils.buildEventOptions(facesContext, component);

        Map<String, Object> parameters = options.getParameters();
        parameters.put(component.getClientId(facesContext) + ":page", new JSLiteral("data.page"));

        function.addParameter(options);

        StringBuffer buffer = new StringBuffer();
        function.appendScript(buffer);
        definition.addToBody(buffer);
        return definition;
    }

    public boolean getRendersChildren() {
        return true;
    }
}

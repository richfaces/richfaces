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

import static org.richfaces.component.DataScrollerControlsMode.auto;
import static org.richfaces.component.DataScrollerControlsMode.show;

import java.io.IOException;
import java.util.Collections;
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
import org.richfaces.component.AbstractDataScroller;
import org.richfaces.component.DataScrollerControlsMode;
import org.richfaces.event.DataScrollEvent;
import org.richfaces.renderkit.util.AjaxRendererUtils;

@ResourceDependencies({ @ResourceDependency(library = "javax.faces", name = "jsf.js"),
        @ResourceDependency(library = "org.richfaces", name = "jquery.js"),
        @ResourceDependency(library = "org.richfaces", name = "richfaces.js"),
        @ResourceDependency(library = "org.richfaces", name = "richfaces-queue.reslib"),
        @ResourceDependency(library = "org.richfaces", name = "richfaces-base-component.js"),
        @ResourceDependency(library = "org.richfaces", name = "richfaces-event.js"),
        @ResourceDependency(library = "org.richfaces", name = "datascroller.js"),
        @ResourceDependency(library = "org.richfaces", name = "datascroller.ecss") })
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
                new DataScrollEvent(scroller, String.valueOf(page), param, newPage).queue();
            }
        }
    }

    protected boolean shouldRender(UIComponent component){
        AbstractDataScroller ds = (AbstractDataScroller) component;
        if ((ds.getPageCount() == 1)&&(!ds.isRenderIfSinglePage())){
            return false;
        }else{
        return true;
        }
    }

    private DataScrollerControlsMode getModeOrDefault(UIComponent component, String attributeName) {
        DataScrollerControlsMode mode = (DataScrollerControlsMode) component.getAttributes().get(attributeName);
        if (mode == null) {
            mode = DataScrollerControlsMode.DEFAULT;
        }
        return mode;
    }

    public ControlsState getControlsState(FacesContext context, UIComponent component) {

        int fastStep = (Integer) component.getAttributes().get("fastStepOrDefault");
        int pageIndex = (Integer) component.getAttributes().get("page");
        int pageCount = (Integer) component.getAttributes().get("pageCount");

        int minPageIdx = 1;
        int maxPageIdx = pageCount;

        boolean useFirst = true;
        boolean useLast = true;

        boolean useBackFast = true;
        boolean useForwFast = true;

        ControlsState controlsState = new ControlsState();

        if (pageIndex <= minPageIdx) {
            useBackFast = false;
            useFirst = false;
        }

        if (pageIndex >= maxPageIdx) {
            useForwFast = false;
            useLast = false;
        }

        DataScrollerControlsMode boundaryControls = getModeOrDefault(component, "boundaryControls");
        DataScrollerControlsMode stepControls = getModeOrDefault(component, "stepControls");
        DataScrollerControlsMode fastControls = getModeOrDefault(component, "fastControls");

        boolean isAuto = auto.equals(boundaryControls);
        if (isAuto || show.equals(boundaryControls)) {
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

        isAuto = auto.equals(stepControls);
        if (isAuto || show.equals(stepControls)) {
            if (isAuto) {
                controlsState.setPreviousRendered(useFirst);
                controlsState.setNextRendered(useLast);
            }

            controlsState.setPreviousEnabled(useFirst);
            controlsState.setNextEnabled(useLast);
        } else {
            controlsState.setPreviousRendered(false);
            controlsState.setNextRendered(false);
        }

        isAuto = auto.equals(fastControls);
        if (isAuto || show.equals(fastControls)) {
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

        UIComponent controlsSeparatorFacet = component.getFacet(AbstractDataScroller.CONTROLS_SEPARATOR_FACET_NAME);
        if (controlsSeparatorFacet != null && controlsSeparatorFacet.isRendered()) {
            controlsState.setControlsSeparatorRendered(true);
        }

        return controlsState;
    }

    public Map<String, String> renderPagerFacet(ResponseWriter out, FacesContext context, UIComponent facet) throws IOException {
        out.startElement(HtmlConstants.SPAN_ELEM, facet);
        out.writeAttribute(HtmlConstants.CLASS_ATTRIBUTE, "rf-ds-pages ", null);
        encodeFacet(context, facet);
        out.endElement(HtmlConstants.SPAN_ELEM);
        return Collections.emptyMap();
    }

    public Map<String, String> renderPager(ResponseWriter out, FacesContext context, UIComponent component) throws IOException {
        UIComponent pagesFacet = component.getFacet(AbstractDataScroller.PAGES_FACET_NAME);
        if (pagesFacet != null && pagesFacet.isRendered()) {
            return renderPagerFacet(out, context, pagesFacet);
        }

        int currentPage = (Integer) component.getAttributes().get("page");
        int maxPages = (Integer) component.getAttributes().get("maxPagesOrDefault");
        int pageCount = (Integer) component.getAttributes().get("pageCount");

        Map<String, String> digital = new HashMap<String, String>();

        if (pageCount <= 1) {
            return digital;
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
        for (int i = start; i < size; i++) {

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
                out.startElement(HtmlConstants.SPAN_ELEM, component);
                out.writeAttribute(HtmlConstants.CLASS_ATTRIBUTE, "rf-ds-nmb-btn rf-ds-act " + styleClass, null);
            } else {
                out.startElement(HtmlConstants.A_ELEMENT, component);
                out.writeAttribute(HtmlConstants.CLASS_ATTRIBUTE, "rf-ds-nmb-btn " + styleClass, null);
                out.writeAttribute(HtmlConstants.HREF_ATTR, "javascript:void(0);", null);
            }

            if (null != style) {
                out.writeAttribute(HtmlConstants.STYLE_ATTRIBUTE, style, null);
            }

            String page = Integer.toString(i + 1);
            String id = clientId + "_ds_" + page;

            out.writeAttribute(HtmlConstants.ID_ATTRIBUTE, id, null);

            digital.put(id, page);

            out.writeText(page, null);

            if (isCurrentPage) {
                out.endElement(HtmlConstants.SPAN_ELEM);
            } else {
                out.endElement(HtmlConstants.A_ELEMENT);
            }
        }

        return digital;
    }

    public Map<String, Map<String, String>> getControls(FacesContext context, UIComponent component, ControlsState controlsState) {

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

        if (controlsState.getPreviousRendered() && controlsState.getPreviousEnabled()) {
            left.put(clientId + "_ds_prev", AbstractDataScroller.PREVIOUS_FACET_NAME);
        }

        if (controlsState.getFastForwardRendered() && controlsState.getFastForwardEnabled()) {
            right.put(clientId + "_ds_ff", AbstractDataScroller.FAST_FORWARD_FACET_NAME);
        }

        if (controlsState.getNextRendered() && controlsState.getNextEnabled()) {
            right.put(clientId + "_ds_next", AbstractDataScroller.NEXT_FACET_NAME);
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

    public void buildScript(ResponseWriter writer, FacesContext context, UIComponent component, Map<?, ?> buttons, Map<?, ?> digitals)
        throws IOException {

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

        AjaxFunction function = AjaxRendererUtils.buildAjaxFunction(facesContext, component);

        Map<String, Object> parameters = function.getOptions().getParameters();
        parameters.put(component.getClientId(facesContext) + ":page", new JSLiteral("data.page"));

        definition.addToBody(function.toScript());
        return definition;
    }

    public void encodeFacet(FacesContext context, UIComponent component) throws IOException {
        component.encodeAll(context);
    }

    public boolean getRendersChildren() {
        return true;
    }
}

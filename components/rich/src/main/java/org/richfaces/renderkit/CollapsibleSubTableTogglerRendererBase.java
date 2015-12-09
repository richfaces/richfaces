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
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.component.AbstractCollapsibleSubTable;
import org.richfaces.component.AbstractCollapsibleSubTableToggler;
import org.richfaces.renderkit.util.RendererUtils;

import com.google.common.base.Strings;

/**
 * @author Anton Belevich
 */
@JsfRenderer(type = "org.richfaces.CollapsibleSubTableTogglerRenderer", family = AbstractCollapsibleSubTableToggler.COMPONENT_FAMILY)
@ResourceDependencies({ @ResourceDependency(library = "javax.faces", name = "jsf.js"),
        @ResourceDependency(library = "org.richfaces", name = "jquery.js"),
        @ResourceDependency(library = "org.richfaces", name = "richfaces.js"),
        @ResourceDependency(library = "org.richfaces", name = "richfaces-queue.reslib"),
        @ResourceDependency(library = "org.richfaces", name = "richfaces-event.js"),
        @ResourceDependency(library = "org.richfaces", name = "collapsible-subtable-toggler.js"),
        @ResourceDependency(library = "org.richfaces", name = "collapsible-subtable.ecss") })
public abstract class CollapsibleSubTableTogglerRendererBase extends RendererBase {
    /**
     *
     */
    private static final RendererUtils RENDERER_UTILS = RendererUtils.getInstance();
    private static final String DISPLAY_NONE = "display: none;";
    private static final String UP_ICON_URL = "org.richfaces/up_icon.gif";
    private static final String DOWN_ICON_URL = "org.richfaces/down_icon.gif";
    private static final String EXPANDED_STATE = "expanded";
    private static final String COLLAPSED_STATE = "collapsed";
    private static final String EXPANDED_CONTROL_CLASS = "rf-csttg-exp";
    private static final String COLLAPSED_CONTROL_CLASS = "rf-csttg-colps";

    @Override
    protected void doDecode(FacesContext context, UIComponent component) {
        // TODO - review
        context.getPartialViewContext().getRenderIds().add(component.getClientId(context));
    }

    protected void encodeControl(FacesContext context, UIComponent component) throws IOException {
        AbstractCollapsibleSubTableToggler toggleControl = (AbstractCollapsibleSubTableToggler) component;
        AbstractCollapsibleSubTable subTable = findComponent(context, toggleControl);

        if (subTable != null) {
            boolean expanded = subTable.isExpanded();

            ResponseWriter writer = context.getResponseWriter();
            encodeControl(context, writer, toggleControl, expanded, true);
            encodeControl(context, writer, toggleControl, !expanded, false);

            JSFunction jsFunction = new JSFunction("new RichFaces.ui.CollapsibleSubTableToggler");
            String toggleId = toggleControl.getClientId(context);
            jsFunction.addParameter(toggleId);
            Map<String, Object> options = encodeOptions(context, toggleControl, subTable);
            jsFunction.addParameter(options);

            writer.startElement(HtmlConstants.SCRIPT_ELEM, subTable);
            writer.writeText(jsFunction.toScript(), null);
            writer.endElement(HtmlConstants.SCRIPT_ELEM);
        }
    }

    private boolean isEmpty(String value) {
        return (value == null || value.trim().length() == 0);
    }

    protected void encodeControl(FacesContext context, ResponseWriter writer, AbstractCollapsibleSubTableToggler control,
        boolean expanded, boolean visible) throws IOException {
        String state = getState(expanded);
        String styleClass = getStyleClass(context, control);
        String style = getStyle(context, control);

        writer.startElement(HtmlConstants.SPAN_ELEM, control);

        if (!visible) {
            writer.writeAttribute(HtmlConstants.STYLE_ATTRIBUTE, DISPLAY_NONE, null);
        }

        writer.writeAttribute(HtmlConstants.ID_ATTRIBUTE, control.getClientId(context) + ":" + state, null);
        writer.writeAttribute(HtmlConstants.CLASS_ATTRIBUTE,
            concatClasses(styleClass, expanded ? EXPANDED_CONTROL_CLASS : COLLAPSED_CONTROL_CLASS), null);
        writer.writeAttribute(HtmlConstants.STYLE_ATTRIBUTE, style, null);

        UIComponent controlFacet = control.getFacet(state);
        if (controlFacet != null && controlFacet.isRendered()) {

            if (!visible) {
                String facetStyle = (String) controlFacet.getAttributes().get(HtmlConstants.STYLE_ATTRIBUTE);
                facetStyle = facetStyle != null ? facetStyle + ";" + DISPLAY_NONE : DISPLAY_NONE;
                controlFacet.getAttributes().put(HtmlConstants.STYLE_ATTRIBUTE, facetStyle);
            }
            controlFacet.encodeAll(context);
        }

        String expandedIcon = control.getExpandedIcon();
        if (isEmpty(expandedIcon)) {
            expandedIcon = context.getApplication().getResourceHandler().createResource(DOWN_ICON_URL).getRequestPath();
        } else {
            expandedIcon = RenderKitUtils.getResourceURL(expandedIcon, context);
        }

        String collapsedIcon = control.getCollapsedIcon();
        if (isEmpty(collapsedIcon)) {
            collapsedIcon = context.getApplication().getResourceHandler().createResource(UP_ICON_URL).getRequestPath();
        } else {
            collapsedIcon = RenderKitUtils.getResourceURL(collapsedIcon, context);
        }

        String image = expanded ? expandedIcon : collapsedIcon;
        if (image != null && image.trim().length() > 0) {
            writer.startElement(HtmlConstants.IMG_ELEMENT, control);
            writer.writeAttribute(HtmlConstants.SRC_ATTRIBUTE, image, null);
            writer.writeAttribute(HtmlConstants.ALT_ATTRIBUTE, "", null);
            writer.endElement(HtmlConstants.IMG_ELEMENT);
        }

        String label = expanded ? control.getExpandedLabel() : control.getCollapsedLabel();
        if (label != null && label.trim().length() > 0) {
            writer.startElement(HtmlConstants.A_ELEMENT, control);
            writer.writeAttribute(HtmlConstants.HREF_ATTR, "javascript:void(0);", null);
            writer.writeAttribute(HtmlConstants.CLASS_ATTRIBUTE, "rf-csttg-lnk", null);
            writer.writeText(label, null);
            writer.endElement(HtmlConstants.A_ELEMENT);
        }

        writer.endElement(HtmlConstants.SPAN_ELEM);
    }

    public HashMap<String, Object> encodeOptions(FacesContext context, AbstractCollapsibleSubTableToggler toggleControl,
        AbstractCollapsibleSubTable subTable) {
        String forId = subTable.getClientId(context);
        String toggleControlId = toggleControl.getClientId(context);

        HashMap<String, Object> options = new HashMap<String, Object>();
        options.put("forId", forId);
        options.put("expandedControl", toggleControlId + ":expanded");
        options.put("collapsedControl", toggleControlId + ":collapsed");

        String eventName = toggleControl.getEvent();

        if (Strings.isNullOrEmpty(eventName)) {
            eventName = AbstractCollapsibleSubTableToggler.DEFAULT_EVENT;
        }

        eventName = eventName.trim().startsWith("on") ? eventName.substring(2) : eventName;
        options.put("eventName", eventName);
        return options;
    }

    public String getStyleClass(FacesContext context, AbstractCollapsibleSubTableToggler control) {
        return null;
    }

    public String getStyle(FacesContext context, AbstractCollapsibleSubTableToggler control) {
        return null;
    }

    protected AbstractCollapsibleSubTable findComponent(FacesContext context, AbstractCollapsibleSubTableToggler toggleControl) {
        String forId = toggleControl.getFor();
        if (forId != null && forId.length() > 0) {

            UIComponent subTable = RENDERER_UTILS.findComponentFor(toggleControl, forId);
            if (subTable instanceof AbstractCollapsibleSubTable) {
                return (AbstractCollapsibleSubTable) subTable;
            }
        }
        return null;
    }

    protected String getState(boolean expanded) {
        return expanded ? EXPANDED_STATE : COLLAPSED_STATE;
    }
}
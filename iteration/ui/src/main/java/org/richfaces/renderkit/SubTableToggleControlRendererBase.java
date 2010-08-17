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
import org.ajax4jsf.renderkit.RendererBase;
import org.ajax4jsf.renderkit.RendererUtils.HTML;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.component.AbstractSubTable;
import org.richfaces.component.AbstractSubTableToggleControl;

/**
 * @author Anton Belevich
 */


@JsfRenderer(type = "org.richfaces.SubTableToggleControlRenderer", family = AbstractSubTableToggleControl.COMPONENT_FAMILY)
@ResourceDependencies(
    {@ResourceDependency(library = "javax.faces", name = "jsf.js"),
    @ResourceDependency(name = "richfaces.js"), 
    @ResourceDependency(name = "richfaces-event.js"),
    @ResourceDependency(library="org.richfaces", name = "subtable-toggler.js")
})
public class SubTableToggleControlRendererBase extends RendererBase {

    private static final String DISPLAY_NONE = "display: none;";

    private static final String UP_ICON_URL = "org.richfaces/up_icon.gif";

    private static final String DOWN_ICON_URL = "org.richfaces/down_icon.gif";
    
    private static final String EXPAND_STATE = "expand";

    private static final String COLLAPSE_STATE = "collapse";

    @Override
    protected void doDecode(FacesContext context, UIComponent component) {
        context.getPartialViewContext().getRenderIds().add(component.getClientId(context));
    }

    protected void encodeControl(FacesContext context, UIComponent component) throws IOException {
        AbstractSubTableToggleControl toggleControl = (AbstractSubTableToggleControl) component;
        AbstractSubTable subTable = findComponent(context, toggleControl);

        if (subTable != null) {
            String switchType = subTable.getExpandMode();
            boolean expanded = subTable.isExpanded();
            
            ResponseWriter writer = context.getResponseWriter();
            encodeControl(context, writer, toggleControl, switchType, expanded, false);
            encodeControl(context, writer, toggleControl, switchType, !expanded, true);

            JSFunction jsFunction = new JSFunction("new RichFaces.ui.SubTableToggler");
            String toggleId = toggleControl.getClientId(context);
            jsFunction.addParameter(toggleId);
            Map<String, Object> options = encodeOptions(context, toggleControl, subTable);
            jsFunction.addParameter(options);

            writer.startElement(HTML.SCRIPT_ELEM, subTable);
            writer.writeText(jsFunction.toScript(), null);
            writer.endElement(HTML.SCRIPT_ELEM);
        }
    }

    protected void encodeControl(FacesContext context, ResponseWriter writer, AbstractSubTableToggleControl control,
                                 String switchType, boolean expanded, boolean visible) throws IOException {
        String state = getState(expanded);
        String styleClass = getStyleClass(context, control);
        String style = getStyle(context, control);

        writer.startElement(HTML.SPAN_ELEM, control);

        if (!visible) {
            writer.writeAttribute(HTML.STYLE_ATTRIBUTE, DISPLAY_NONE, null);
        }

        writer.writeAttribute(HTML.ID_ATTRIBUTE, control.getClientId() + ":" + state, null);
        writer.writeAttribute(HTML.CLASS_ATTRIBUTE, styleClass, null);
        writer.writeAttribute(HTML.STYLE_ATTRIBUTE, style, null);

        boolean encodeDefault = true;

        UIComponent controlFacet = control.getFacet(state);
        if (controlFacet != null && controlFacet.isRendered()) {

            if (!visible) {
                String facetStyle = (String) controlFacet.getAttributes().get(HTML.STYLE_ATTRIBUTE);
                facetStyle = facetStyle != null ? facetStyle + ";" + DISPLAY_NONE : DISPLAY_NONE;
                controlFacet.getAttributes().put(HTML.STYLE_ATTRIBUTE, facetStyle);
            }
            controlFacet.encodeAll(context);
            encodeDefault = false;
        }

        String expandIcon = control.getExpandIcon();
        String collapseIcon = control.getCollapseIcon();

        if ((expandIcon != null && collapseIcon != null)
            && (expandIcon.trim().length() > 0 && collapseIcon.trim().length() > 0)) {

            String image = expanded ? expandIcon : collapseIcon;
            if (image != null && image.trim().length() > 0) {
                writer.startElement(HTML.IMG_ELEMENT, control);
                writer.writeAttribute(HTML.SRC_ATTRIBUTE, image, null);
                writer.writeAttribute(HTML.ALT_ATTRIBUTE, "", null);
                writer.endElement(HTML.IMG_ELEMENT);
            }
            encodeDefault = false;
        }

        String label = expanded ? control.getExpandLabel() : control.getCollapseLabel();
        if (label != null && label.trim().length() > 0) {
            writer.startElement(HTML.A_ELEMENT, control);
            writer.writeAttribute(HTML.HREF_ATTR, "javascript:void(0);", null);
            writer.writeText(label, null);
            writer.endElement(HTML.A_ELEMENT);
            encodeDefault = false;
        }

        if (encodeDefault) {
            expandIcon = context.getApplication().getResourceHandler().createResource(UP_ICON_URL).getRequestPath();
            collapseIcon = context.getApplication().getResourceHandler().createResource(DOWN_ICON_URL).getRequestPath();

            String image = expanded ? expandIcon : collapseIcon;
            if (image != null && image.trim().length() > 0) {
                writer.startElement(HTML.IMG_ELEMENT, control);
                writer.writeAttribute(HTML.SRC_ATTRIBUTE, image, null);
                writer.writeAttribute(HTML.ALT_ATTRIBUTE, "", null);
                writer.endElement(HTML.IMG_ELEMENT);
            }
        }

        writer.endElement(HTML.SPAN_ELEM);
    }

    public HashMap<String, Object> encodeOptions(FacesContext context, AbstractSubTableToggleControl toggleControl, AbstractSubTable subTable) {
        String forId = subTable.getClientId(context);
        String toggleControlId = toggleControl.getClientId(context);

        HashMap<String, Object> options = new HashMap<String, Object>();
        options.put("forId", forId);
        options.put("expandControl", toggleControlId + ":expand");
        options.put("collapseControl", toggleControlId + ":collapse");

        String eventName = toggleControl.getEvent();
        eventName = eventName.trim().startsWith("on") ? eventName.substring(2) : eventName;
        options.put("eventName", eventName);
        return options;
    }

    public String getStyleClass(FacesContext context, AbstractSubTableToggleControl control) {
        return null;
    }

    public String getStyle(FacesContext context, AbstractSubTableToggleControl control) {
        return null;
    }

    protected AbstractSubTable findComponent(FacesContext context, AbstractSubTableToggleControl toggleControl) {
        String forId = toggleControl.getFor();
        if (forId != null && forId.length() > 0) {

            UIComponent subTable = getUtils().findComponentFor(context, toggleControl, forId);
            if (subTable instanceof AbstractSubTable) {
                return (AbstractSubTable) subTable;
            }
        }
        return null;
    }

    protected String getState(boolean expand) {
        return expand ? EXPAND_STATE : COLLAPSE_STATE;
    }

}

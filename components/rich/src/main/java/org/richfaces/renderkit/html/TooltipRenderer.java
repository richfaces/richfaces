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
package org.richfaces.renderkit.html;

import static org.richfaces.renderkit.HtmlConstants.CLASS_ATTRIBUTE;
import static org.richfaces.renderkit.HtmlConstants.ID_ATTRIBUTE;
import static org.richfaces.renderkit.RenderKitUtils.renderPassThroughAttributes;
import static org.richfaces.renderkit.html.TogglePanelRenderer.addEventOption;
import static org.richfaces.renderkit.html.TogglePanelRenderer.getAjaxOptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.PartialResponseWriter;
import javax.faces.context.ResponseWriter;

import org.ajax4jsf.javascript.JSObject;
import org.richfaces.TooltipMode;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.component.AbstractTooltip;
import org.richfaces.component.Positioning;
import org.richfaces.javascript.JavaScriptService;
import org.richfaces.renderkit.HtmlConstants;
import org.richfaces.renderkit.MetaComponentRenderer;
import org.richfaces.renderkit.RenderKitUtils;
import org.richfaces.application.ServiceTracker;
import org.richfaces.renderkit.util.RendererUtils;

/**
 * @author amarkhel
 * @since 2010-10-24
 */
@ResourceDependencies({ @ResourceDependency(library = "javax.faces", name = "jsf.js"),
        @ResourceDependency(library = "org.richfaces", name = "jquery.js"),
        @ResourceDependency(library = "org.richfaces", name = "richfaces.js"),
        @ResourceDependency(library = "org.richfaces", name = "richfaces-queue.reslib"),
        @ResourceDependency(library = "org.richfaces", name = "richfaces-base-component.js"),
        @ResourceDependency(library = "org.richfaces", name = "jquery.position.js"), @ResourceDependency(library = "org.richfaces", name = "richfaces-event.js"),
        @ResourceDependency(library = "org.richfaces", name = "popup.js"),
        @ResourceDependency(library = "org.richfaces", name = "tooltip.js"),
        @ResourceDependency(library = "org.richfaces", name = "tooltip.ecss") })
@JsfRenderer(type = "org.richfaces.TooltipRenderer", family = AbstractTooltip.COMPONENT_FAMILY)
public class TooltipRenderer extends DivPanelRenderer implements MetaComponentRenderer {
    public static final String HIDE = "hide";
    public static final String SHOW = "show";
    public static final String BEFORE_HIDE = "beforehide";
    public static final String BEFORE_SHOW = "beforeshow";
    private static final RendererUtils RENDERER_UTILS = RendererUtils.getInstance();

    @Override
    protected void doDecode(FacesContext context, UIComponent component) {
        AbstractTooltip tooltip = (AbstractTooltip) component;

        Map<String, String> requestMap = context.getExternalContext().getRequestParameterMap();

        String compClientId = component.getClientId(context);
        String clientId = requestMap.get(compClientId);
        if (clientId != null && clientId.equals(compClientId)) {
            context.getPartialViewContext().getRenderIds().add(tooltip.getContentClientId(context));

            // TODO nick - this should be done on encode, not on decode
            addOnCompleteParam(context, tooltip.getClientId(context));
            context.renderResponse();
        }
    }

    @Override
    protected void doEncodeBegin(ResponseWriter writer, FacesContext context, UIComponent component) throws IOException {
        AbstractTooltip tooltip = (AbstractTooltip) component;

        writer.startElement(getMarkupElement(tooltip), component);
        writer.writeAttribute(ID_ATTRIBUTE, component.getClientId(context), "clientId");

        writer.writeAttribute(HtmlConstants.STYLE_ATTRIBUTE, "display: none;", null);
        writer.startElement(getMarkupElement(tooltip), component);
        writer.writeAttribute(ID_ATTRIBUTE, component.getClientId(context) + ":wrp", null);
        writer.writeAttribute(CLASS_ATTRIBUTE, getStyleClass(component), null);

        int zindex = tooltip.getZindex();

        String style = concatStyles("z-index:" + zindex, getStyle(component));
        if (style != null && style.trim().length() > 0) {
            writer.writeAttribute(HtmlConstants.STYLE_ATTRIBUTE, style, null);
        }

        renderPassThroughAttributes(context, component, getPassThroughAttributes());

        writer.startElement(getMarkupElement(tooltip), component);
        writer.writeAttribute(ID_ATTRIBUTE, component.getClientId(context) + ":cntr", null);
        writer.writeAttribute(CLASS_ATTRIBUTE, "rf-tt-cntr", null);

        if (tooltip.getMode() == TooltipMode.ajax) {
            encodeLoading(writer, context, tooltip);
        }

        encodeContentBegin(writer, context, tooltip);
    }

    private void encodeContentBegin(ResponseWriter writer, FacesContext context, AbstractTooltip tooltip) throws IOException {
        writer.startElement(getMarkupElement(tooltip), tooltip);
        writer.writeAttribute(ID_ATTRIBUTE, tooltip.getClientId(context) + ":content", null);
        writer.writeAttribute(CLASS_ATTRIBUTE, "rf-tt-cnt", null);
        Object value = tooltip.getValue();
        if (tooltip.getChildCount() == 0 && value != null) {
            writer.write(value.toString());
        }
    }

    private void encodeLoading(ResponseWriter writer, FacesContext context, AbstractTooltip tooltip) throws IOException {
        writer.startElement(getMarkupElement(tooltip), tooltip);
        writer.writeAttribute(ID_ATTRIBUTE, tooltip.getClientId(context) + ":loading", null);
        writer.writeAttribute(CLASS_ATTRIBUTE, "rf-tt-loading", null);
        UIComponent loading = tooltip.getFacet("loading");
        if (loading != null && loading.isRendered()) {
            loading.encodeAll(context);
        } else {
            writer.writeText("Loading...", null);
        }
        writer.endElement(getMarkupElement(tooltip));
    }

    private String getMarkupElement(AbstractTooltip component) {
        switch (component.getLayout()) {
            case block:
                return HtmlConstants.DIV_ELEM;

            case inline:
                return HtmlConstants.SPAN_ELEM;

            default:
                throw new IllegalStateException("Unknown tooltip layout (=" + component.getLayout() + ")");
        }
    }

    @Override
    protected String getStyleClass(UIComponent component) {
        return concatClasses("rf-tt", attributeAsString(component, "styleClass"));
    }

    @Override
    protected JSObject getScriptObject(FacesContext context, UIComponent component) {
        return new JSObject("RichFaces.ui.Tooltip", component.getClientId(context), getScriptObjectOptions(context, component));
    }

    @Override
    protected Map<String, Object> getScriptObjectOptions(FacesContext context, UIComponent component) {
        AbstractTooltip tooltip = (AbstractTooltip) component;

        Map<String, Object> options = new HashMap<String, Object>();

        RenderKitUtils.addToScriptHash(options, "ajax", getAjaxOptions(context, tooltip), TooltipMode.DEFAULT);

        Positioning jointPoint = tooltip.getJointPoint();
        if (jointPoint == null) {
            jointPoint = org.richfaces.component.Positioning.DEFAULT;
        }
        Positioning direction = tooltip.getDirection();
        if (direction == null) {
            direction = org.richfaces.component.Positioning.DEFAULT;
        }

        RenderKitUtils.addToScriptHash(options, "jointPoint", jointPoint.getValue(), Positioning.DEFAULT.getValue());
        RenderKitUtils.addToScriptHash(options, "direction", direction.getValue(), Positioning.DEFAULT.getValue());
        RenderKitUtils.addToScriptHash(options, "attached", tooltip.isAttached(), true);
        RenderKitUtils.addToScriptHash(options, "offset", getOffset(tooltip));
        RenderKitUtils.addToScriptHash(options, "mode", tooltip.getMode(), TooltipMode.DEFAULT);
        RenderKitUtils.addToScriptHash(options, "hideDelay", tooltip.getHideDelay(), 0);
        RenderKitUtils.addToScriptHash(options, "hideEvent", tooltip.getHideEvent(), "mouseleave");
        RenderKitUtils.addToScriptHash(options, "showDelay", tooltip.getShowDelay(), 0);
        RenderKitUtils.addToScriptHash(options, "showEvent", tooltip.getShowEvent(), "mouseenter");
        RenderKitUtils.addToScriptHash(options, "followMouse", tooltip.isFollowMouse(), true);
        String target = tooltip.getTarget();
        UIComponent targetComponent = RENDERER_UTILS.findComponentFor(component, target);
        if (targetComponent != null) {
            target = targetComponent.getClientId();
        }
        RenderKitUtils.addToScriptHash(options, "target", target);

        addEventOption(context, tooltip, options, HIDE);
        addEventOption(context, tooltip, options, SHOW);
        addEventOption(context, tooltip, options, BEFORE_HIDE);
        addEventOption(context, tooltip, options, BEFORE_SHOW);

        return options;
    }

    public Integer[] getOffset(AbstractTooltip tooltip) {
        int horizontalOffset = tooltip.getHorizontalOffset();
        int verticalOffset = tooltip.getVerticalOffset();
        if (horizontalOffset == Integer.MIN_VALUE) {
            horizontalOffset = 10;
        }
        if (verticalOffset == Integer.MIN_VALUE) {
            verticalOffset = 10;
        }
        return new Integer[] { horizontalOffset, verticalOffset };
    }

    private void encodeContentEnd(ResponseWriter writer, FacesContext context, AbstractTooltip tooltip) throws IOException {
        writer.endElement(getMarkupElement(tooltip));
    }

    @Override
    protected void doEncodeEnd(ResponseWriter writer, FacesContext context, UIComponent component) throws IOException {
        encodeContentEnd(writer, context, (AbstractTooltip) component);

        writer.endElement(getMarkupElement((AbstractTooltip) component));

        writer.endElement(getMarkupElement((AbstractTooltip) component));

        writeJavaScript(writer, context, component);

        writer.endElement(getMarkupElement((AbstractTooltip) component));
    }

    protected void writeJavaScript(ResponseWriter writer, FacesContext context, UIComponent component) throws IOException {
        Object script = getScriptObject(context, component);
        if (script == null) {
            return;
        }

        JavaScriptService javaScriptService = ServiceTracker.getService(JavaScriptService.class);
        javaScriptService.addScript(context, script);
    }

    public void encodeMetaComponent(FacesContext context, UIComponent component, String metaComponentId) throws IOException {
        if (AbstractTooltip.CONTENT_META_COMPONENT_ID.equals(metaComponentId)) {
            AbstractTooltip tooltip = (AbstractTooltip) component;
            PartialResponseWriter writer = context.getPartialViewContext().getPartialResponseWriter();
            writer.startUpdate(tooltip.getClientId(context) + ":" + AbstractTooltip.CONTENT_META_COMPONENT_ID);

            encodeContentBegin(writer, context, tooltip);
            for (UIComponent child : tooltip.getChildren()) {
                child.encodeAll(context);
            }
            encodeContentEnd(writer, context, tooltip);

            writer.endUpdate();
        }
    }

    public void decodeMetaComponent(FacesContext context, UIComponent component, String metaComponentId) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected Class<? extends UIComponent> getComponentClass() {
        return AbstractTooltip.class;
    }

    @Override
    protected void doEncodeChildren(ResponseWriter writer, FacesContext context, UIComponent component) throws IOException {

        AbstractTooltip tooltip = (AbstractTooltip) component;

        if (tooltip.getMode() == TooltipMode.client) {
            if (tooltip.getChildCount() > 0) {
                for (UIComponent kid : tooltip.getChildren()) {
                    kid.encodeAll(context);
                }
            }
        }
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }
}

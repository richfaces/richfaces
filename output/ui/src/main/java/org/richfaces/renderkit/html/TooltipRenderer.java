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

import org.ajax4jsf.context.AjaxContext;
import org.ajax4jsf.javascript.JSObject;
import org.richfaces.TooltipMode;
import org.richfaces.component.AbstractTooltip;
import org.richfaces.component.html.HtmlTooltip;
import org.richfaces.renderkit.HtmlConstants;
import org.richfaces.renderkit.MetaComponentRenderer;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.PartialResponseWriter;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.richfaces.component.util.HtmlUtil.concatClasses;
import static org.richfaces.renderkit.RenderKitUtils.renderPassThroughAttributes;
import static org.richfaces.renderkit.html.TogglePanelRenderer.addEventOption;
import static org.richfaces.renderkit.html.TogglePanelRenderer.getAjaxOptions;

/**
 * @author amarkhel
 * @since 2010-10-24
 */
@ResourceDependencies( { // TODO review
    @ResourceDependency(library = "javax.faces", name = "jsf.js"),
    @ResourceDependency(name = "jquery.js"),
    @ResourceDependency(name = "jquery.position.js"),
    @ResourceDependency(name = "richfaces.js"),
    @ResourceDependency(name = "richfaces-event.js"),
    @ResourceDependency(name = "richfaces-base-component.js"),
    @ResourceDependency(library = "org.richfaces", name = "popup.js"),
    @ResourceDependency(library = "org.richfaces", name = "Tooltip.js"),
    @ResourceDependency(library = "org.richfaces", name = "tooltip.ecss") })
public class TooltipRenderer extends DivPanelRenderer implements MetaComponentRenderer {

    public static final String HIDE = "hide";
    public static final String SHOW = "show";
    public static final String BEFORE_HIDE = "beforehide";
    public static final String BEFORE_SHOW = "beforeshow";

    @Override
    protected void doDecode(FacesContext context, UIComponent component) {
        AbstractTooltip tooltip = (AbstractTooltip) component;

        Map<String, String> requestMap =
              context.getExternalContext().getRequestParameterMap();

        String compClientId = component.getClientId(context);
        String clientId = requestMap.get(compClientId);
        if (clientId != null && clientId.equals(compClientId)) {
            context.getPartialViewContext().getRenderIds().add(tooltip.getContentClientId(context));

            //TODO nick - this should be done on encode, not on decode
            addOnCompleteParam(tooltip.getClientId());
        }
    }

    protected static void addOnCompleteParam(String tooltipId) {
        AjaxContext.getCurrentInstance().appendOncomplete(new StringBuilder()
                .append("RichFaces.$('").append(tooltipId).append("').onCompleteHandler();").toString());
    }

    @Override
    protected void doEncodeBegin(ResponseWriter writer, FacesContext context, UIComponent component) throws IOException {
        AbstractTooltip tooltip = (AbstractTooltip) component;

        writer.startElement(getMarkupElement(tooltip), component);
        writer.writeAttribute("id", component.getClientId(context), "clientId");
        writer.writeAttribute("class", getStyleClass(component), null);
        String style = getStyle(component);
        if (style != null && style.trim().length() > 0) {
            writer.writeAttribute(HtmlConstants.STYLE_ATTRIBUTE, style, null);
        }

        renderPassThroughAttributes(context, component, getPassThroughAttributes());

        writer.startElement(getMarkupElement(tooltip), component);
        writer.writeAttribute("id", component.getClientId(context) + ":cntr", null);
        writer.writeAttribute("class", "rf-tt-cntr", null);

        if (tooltip.getMode() == TooltipMode.ajax) {
            encodeLoading(writer, context, tooltip);
        }

        encodeContentBegin(writer, context, tooltip);
    }

    private void encodeContentBegin(ResponseWriter writer, FacesContext context, AbstractTooltip tooltip) throws IOException {
        writer.startElement(getMarkupElement(tooltip), tooltip);
        writer.writeAttribute("id", tooltip.getClientId(context) + "@content", null);
        writer.writeAttribute("class", "rf-tt-cnt", null);
    }

    private void encodeLoading(ResponseWriter writer, FacesContext context, AbstractTooltip tooltip) throws IOException {
        writer.startElement(getMarkupElement(tooltip), tooltip);
        writer.writeAttribute("id", tooltip.getClientId(context) + ":loading", null);
        writer.writeAttribute("class", "rf-tt-loading", null);
        writer.writeText("Loading...", null);
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
        return new JSObject("RichFaces.ui.Tooltip",
            component.getClientId(), getScriptObjectOptions(context, component));
    }

    @Override
    protected Map<String, Object> getScriptObjectOptions(FacesContext context, UIComponent component) {
        HtmlTooltip tooltip = (HtmlTooltip) component;

        Map<String, Object> options = new HashMap<String, Object>();
        options.put("ajax", getAjaxOptions(context, tooltip));
        options.put("direction", tooltip.getDirection());
        options.put("attached", tooltip.isAttached());
        options.put("offset", getOffset(tooltip));
        options.put("mode", tooltip.getMode());
        options.put("disabled", tooltip.isDisabled());
        options.put("hideDelay", tooltip.getHideDelay());
        options.put("hideEvent", tooltip.getHideEvent());
        options.put("showDelay", tooltip.getShowDelay());
        options.put("showEvent", tooltip.getShowEvent());
        options.put("followMouse", tooltip.isFollowMouse());
        options.put("target", tooltip.getTarget());

        addEventOption(context, tooltip, options, HIDE);
        addEventOption(context, tooltip, options, SHOW);
        addEventOption(context, tooltip, options, BEFORE_HIDE);
        addEventOption(context, tooltip, options, BEFORE_SHOW);

        return options;
    }

    public Map<String, Object> getOffset(HtmlTooltip tooltip) {
        Map<String, Object> options = new HashMap<String, Object>();
        // TODO
        return options;
    }

    private void encodeContentEnd(ResponseWriter writer, FacesContext context, AbstractTooltip tooltip) throws IOException {
        writer.endElement(getMarkupElement(tooltip));
    }

    @Override
    protected void doEncodeEnd(ResponseWriter writer, FacesContext context, UIComponent component) throws IOException {
        encodeContentEnd(writer, context, (AbstractTooltip) component);

        writer.endElement(getMarkupElement((AbstractTooltip) component));

        writeJavaScript(writer, context, component);

        writer.endElement(getMarkupElement((AbstractTooltip) component));
    }

    public void encodeMetaComponent(FacesContext context, UIComponent component, String metaComponentId) throws IOException {
        if (AbstractTooltip.CONTENT_META_COMPONENT_ID.equals(metaComponentId)) {
            AbstractTooltip tooltip = (AbstractTooltip) component;
            PartialResponseWriter writer = context.getPartialViewContext().getPartialResponseWriter();
            writer.startUpdate(((AbstractTooltip) component).getContentClientId(context));
            
            encodeContentBegin(writer, context, tooltip);
            for (UIComponent child : tooltip.getChildren()) {
                child.encodeAll(context);
            }
            encodeContentEnd(writer, context, tooltip);

            writer.endUpdate();
        }
    }

    @Override
    protected Class<? extends UIComponent> getComponentClass() {
        return AbstractTooltip.class;
    }


}


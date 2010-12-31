package org.richfaces.renderkit.html;

import org.richfaces.renderkit.RenderKitUtils;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

import static org.richfaces.component.util.HtmlUtil.concatClasses;
import static org.richfaces.renderkit.HtmlConstants.*;
import static org.richfaces.renderkit.html.DivPanelRenderer.styleElement;

public abstract class TableIconsRendererHelper {

    protected final String text;
    protected final String cssClassPrefix;

    public TableIconsRendererHelper(String text, String cssClassPrefix) {
        this.text = text;
        this.cssClassPrefix = cssClassPrefix;
    }

    public void encodeHeader(ResponseWriter writer, FacesContext context, UIComponent component) throws IOException {
        writer.startElement(TABLE_ELEMENT, null);
        writer.writeAttribute(CLASS_ATTRIBUTE, cssClassPrefix + "-gr", null);
        writer.startElement(TBODY_ELEMENT, null);
        writer.startElement(TR_ELEMENT, null);

        encodeHeaderGroupIconLeft(writer, context, component);

        writer.startElement(TD_ELEM, null);
        writer.writeAttribute(CLASS_ATTRIBUTE, cssClassPrefix + "-lbl", null);

        UIComponent headerFacet = component.getFacet(text);
        if (headerFacet != null && headerFacet.isRendered()) {
            headerFacet.encodeAll(context);
        } else {
            Object label = component.getAttributes().get(text);
            if (label != null && !label.equals("")) {
                writer.writeText(label, null);
            }
        }

        writer.endElement(TD_ELEM);

        encodeHeaderGroupIconRight(writer, context, component);

        writer.endElement(TR_ELEMENT);
        writer.endElement(TBODY_ELEMENT);
        writer.endElement(TABLE_ELEMENT);
    }

    protected abstract void encodeHeaderGroupIconLeft(ResponseWriter writer, FacesContext context, UIComponent component) throws IOException;

    protected abstract void encodeHeaderGroupIconRight(ResponseWriter writer, FacesContext context, UIComponent menuItem) throws IOException;

    protected void encodeTdIcon(ResponseWriter writer, FacesContext context, String cssClass, boolean isExpanded, String attrIconCollapsedValue, String attrIconExpandedValue) throws IOException {
        writer.startElement(TD_ELEM, null);
        writer.writeAttribute(CLASS_ATTRIBUTE, cssClass, null);

        encodeIdIcon(writer, context, isExpanded, attrIconCollapsedValue, cssClassPrefix + "-colps");
        encodeIdIcon(writer, context, !isExpanded, attrIconExpandedValue, cssClassPrefix + "-exp");

        writer.endElement(TD_ELEM);
    }

    protected void encodeIdIcon(ResponseWriter writer, FacesContext context, boolean isExpanded, String attrIconValue, String styleClass) throws IOException {
        if (attrIconValue == null || attrIconValue.trim().length() <= 0) {
            encodeDivIcon(writer, isExpanded, PanelMenuIcons.none, styleClass);
        } else {
            PanelMenuIcons icon = getIcon(attrIconValue);
            if (icon != null) {
                encodeDivIcon(writer, isExpanded, icon, styleClass);
            } else {
                encodeImage(writer, context, attrIconValue);
            }
        }
    }

    protected PanelMenuIcons getIcon(String attrIconCollapsedValue) {
        if (attrIconCollapsedValue == null) {
            return null;
        }

        try {
            return PanelMenuIcons.valueOf(attrIconCollapsedValue);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public static void encodeDivIcon(ResponseWriter writer, boolean isDisplay, PanelMenuIcons icon, String styleClass) throws IOException {
        writer.startElement(DIV_ELEM, null);
        writer.writeAttribute(CLASS_ATTRIBUTE, concatClasses(styleClass, icon.cssClass()), null);
        writer.writeAttribute(STYLE_ATTRIBUTE, styleElement("display", isDisplay ? "none" : "block"), null);
        writer.endElement(DIV_ELEM);
    }

    public static void encodeImage(ResponseWriter writer, FacesContext context, String attrIconValue) throws IOException {
        writer.startElement(IMG_ELEMENT, null);
        writer.writeAttribute(ALT_ATTRIBUTE, "", null);
        writer.writeURIAttribute(SRC_ATTRIBUTE, RenderKitUtils.getResourceURL(attrIconValue, context), null);
        writer.endElement(IMG_ELEMENT);
    }
}

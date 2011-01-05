package org.richfaces.renderkit.html;

import org.richfaces.renderkit.RenderKitUtils;
import org.richfaces.renderkit.util.PanelIcons;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

import static org.richfaces.component.util.HtmlUtil.concatClasses;
import static org.richfaces.renderkit.HtmlConstants.*;

public abstract class TableIconsRendererHelper<T extends UIComponent> {

    protected final String text;
    protected final String cssClassPrefix;
    protected final String cssIconsClassPrefix;

    protected TableIconsRendererHelper(String text, String cssClassPrefix, String cssIconsClassPrefix) {
        this.text = text;
        this.cssClassPrefix = cssClassPrefix;
        this.cssIconsClassPrefix = cssIconsClassPrefix;
    }

    protected TableIconsRendererHelper(String text, String cssClassPrefix) {
        this(text, cssClassPrefix, cssClassPrefix + "-ico");
    }

    public void encodeHeader(ResponseWriter writer, FacesContext context, T component) throws IOException {
        writer.startElement(TABLE_ELEMENT, null);
        writer.writeAttribute(CLASS_ATTRIBUTE, cssClassPrefix + "-gr", null);
        writer.startElement(TBODY_ELEMENT, null);
        writer.startElement(TR_ELEMENT, null);

        encodeHeaderIconLeft(writer, context, component);
        encodeHeaderText(writer, context, component);
        encodeHeaderIconRight(writer, context, component);

        writer.endElement(TR_ELEMENT);
        writer.endElement(TBODY_ELEMENT);
        writer.endElement(TABLE_ELEMENT);
    }

    protected void encodeHeaderText(ResponseWriter writer, FacesContext context, T component) throws IOException {
        writer.startElement(TD_ELEM, null);
        writer.writeAttribute(CLASS_ATTRIBUTE, cssClassPrefix + "-lbl", null);

        encodeHeaderTextValue(writer, context, component);

        writer.endElement(TD_ELEM);
    }

    protected void encodeHeaderTextValue(ResponseWriter writer, FacesContext context, T component) throws IOException {
        writeFacetOrAttr(writer, context, component, text, text);
    }

    public static void writeFacetOrAttr(ResponseWriter writer, FacesContext context, UIComponent component, String attr, String facetName) throws IOException {
        writeFacetOrAttr(writer, context, component, attr, component.getFacet(facetName));
    }

    public static void writeFacetOrAttr(ResponseWriter writer, FacesContext context, UIComponent component, String attr, UIComponent headerFacet) throws IOException {
        if (headerFacet != null && headerFacet.isRendered()) {
            headerFacet.encodeAll(context);
        } else {
            Object label = component.getAttributes().get(attr);
            if (label != null && !label.equals("")) {
                writer.writeText(label, null);
            }
        }
    }

    protected abstract void encodeHeaderIconLeft(ResponseWriter writer, FacesContext context, T component) throws IOException;

    protected abstract void encodeHeaderIconRight(ResponseWriter writer, FacesContext context, T menuItem) throws IOException;

    protected void encodeTdIcon(ResponseWriter writer, FacesContext context, String cssClass, String attrIconCollapsedValue, String attrIconExpandedValue) throws IOException {
        writer.startElement(TD_ELEM, null);
        writer.writeAttribute(CLASS_ATTRIBUTE, cssClass, null);

        encodeIdIcon(writer, context, attrIconCollapsedValue, cssIconsClassPrefix + "-colps");
        encodeIdIcon(writer, context, attrIconExpandedValue, cssIconsClassPrefix + "-exp");

        writer.endElement(TD_ELEM);
    }

    protected void encodeIdIcon(ResponseWriter writer, FacesContext context, String attrIconValue, String styleClass) throws IOException {
        if (attrIconValue == null || attrIconValue.trim().length() <= 0) {
            encodeDivIcon(writer, PanelIcons.none, styleClass);
        } else {
            PanelIcons icon = PanelIcons.getIcon(attrIconValue);
            if (icon != null) {
                encodeDivIcon(writer, icon, styleClass);
            } else {
                encodeImage(writer, context, attrIconValue);
            }
        }
    }

    public static void encodeDivIcon(ResponseWriter writer, PanelIcons icon, String styleClass) throws IOException {
        writer.startElement(DIV_ELEM, null);
        writer.writeAttribute(CLASS_ATTRIBUTE, concatClasses(styleClass, icon.cssClass()), null);
        writer.endElement(DIV_ELEM);
    }

    public static void encodeImage(ResponseWriter writer, FacesContext context, String attrIconValue) throws IOException {
        writer.startElement(IMG_ELEMENT, null);
        writer.writeAttribute(ALT_ATTRIBUTE, "", null);
        writer.writeURIAttribute(SRC_ATTRIBUTE, RenderKitUtils.getResourceURL(attrIconValue, context), null);
        writer.endElement(IMG_ELEMENT);
    }
}

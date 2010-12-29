package org.richfaces.renderkit.html;

import org.richfaces.component.html.HtmlPanelMenuGroup;
import org.richfaces.renderkit.RenderKitUtils;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

import static org.richfaces.component.util.HtmlUtil.concatClasses;
import static org.richfaces.renderkit.HtmlConstants.*;
import static org.richfaces.renderkit.html.DivPanelRenderer.styleElement;

public class TableIconsRendererHelper {

    public void encodeHeaderGroup(ResponseWriter writer, FacesContext context, HtmlPanelMenuGroup menuItem, String classPrefix) throws IOException {
        writer.startElement(TABLE_ELEMENT, null);
        writer.writeAttribute(CLASS_ATTRIBUTE, classPrefix + "-gr", null);
        writer.startElement(TBODY_ELEMENT, null);
        writer.startElement(TR_ELEMENT, null);

        encodeHeaderGroupIconLeft(writer, context, menuItem, classPrefix);

        writer.startElement(TD_ELEM, null);
        writer.writeAttribute(CLASS_ATTRIBUTE, classPrefix + "-lbl", null);

        UIComponent headerFacet = menuItem.getFacet("label");
        if (headerFacet != null && headerFacet.isRendered()) {
            headerFacet.encodeAll(context);
        } else {
            Object label = menuItem.getLabel();
            if (label != null && !label.equals("")) {
                writer.writeText(label, null);
            }
        }

        writer.endElement(TD_ELEM);

        encodeHeaderGroupIconRight(writer, context, menuItem, classPrefix);

        writer.endElement(TR_ELEMENT);
        writer.endElement(TBODY_ELEMENT);
        writer.endElement(TABLE_ELEMENT);
    }

    public void encodeHeaderGroupIconLeft(ResponseWriter writer, FacesContext context, HtmlPanelMenuGroup menuGroup, String classPrefix) throws IOException {
        String iconCollapsed = menuGroup.isDisabled() ? menuGroup.getIconLeftDisabled() : menuGroup.getIconLeftCollapsed();
        String iconExpanded = menuGroup.isDisabled() ? menuGroup.getIconLeftDisabled() : menuGroup.getIconLeftExpanded();

        encodeTdIcon(writer, context, classPrefix + "-ico", menuGroup.isExpanded(), iconCollapsed, iconExpanded);
    }

    public void encodeHeaderGroupIconRight(ResponseWriter writer, FacesContext context, HtmlPanelMenuGroup menuItem, String classPrefix) throws IOException {
        String iconCollapsed = menuItem.isDisabled() ? menuItem.getIconRightDisabled() : menuItem.getIconRightCollapsed();
        String iconExpanded = menuItem.isDisabled() ? menuItem.getIconRightDisabled() : menuItem.getIconRightExpanded();

        //TODO nick - should this be "-ico-exp"? also why expanded icon state is connected with right icon alignment?
        encodeTdIcon(writer, context, classPrefix + "-exp-ico", menuItem.isExpanded(), iconCollapsed, iconExpanded);
    }

    public void encodeTdIcon(ResponseWriter writer, FacesContext context, String cssClass, boolean isExpanded, String attrIconCollapsedValue, String attrIconExpandedValue) throws IOException {
        writer.startElement(TD_ELEM, null);
        writer.writeAttribute(CLASS_ATTRIBUTE, cssClass, null);

        encodeIdIcon(writer, context, isExpanded, attrIconCollapsedValue, "rf-pm-ico-colps");
        encodeIdIcon(writer, context, !isExpanded, attrIconExpandedValue, "rf-pm-ico-exp");

        writer.endElement(TD_ELEM);
    }

    public void encodeIdIcon(ResponseWriter writer, FacesContext context, boolean isExpanded, String attrIconValue, String styleClass) throws IOException {
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

    public PanelMenuIcons getIcon(String attrIconCollapsedValue) {
        if (attrIconCollapsedValue == null) {
            return null;
        }

        try {
            return PanelMenuIcons.valueOf(attrIconCollapsedValue);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public void encodeDivIcon(ResponseWriter writer, boolean isDisplay, PanelMenuIcons icon, String styleClass) throws IOException {
        writer.startElement(DIV_ELEM, null);
        writer.writeAttribute(CLASS_ATTRIBUTE, concatClasses(styleClass, icon.cssClass()), null);
        writer.writeAttribute(STYLE_ATTRIBUTE, styleElement("display", isDisplay ? "none" : "block"), null);
        writer.endElement(DIV_ELEM);
    }

    public void encodeImage(ResponseWriter writer, FacesContext context, String attrIconValue) throws IOException {
        writer.startElement(IMG_ELEMENT, null);
        writer.writeAttribute(ALT_ATTRIBUTE, "", null);
        writer.writeURIAttribute(SRC_ATTRIBUTE, RenderKitUtils.getResourceURL(attrIconValue, context), null);
        writer.endElement(IMG_ELEMENT);
    }


}

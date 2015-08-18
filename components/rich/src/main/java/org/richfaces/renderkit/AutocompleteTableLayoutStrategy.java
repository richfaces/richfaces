package org.richfaces.renderkit;

import org.richfaces.component.util.HtmlUtil;

import java.io.IOException;

import javax.faces.component.UIColumn;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

public class AutocompleteTableLayoutStrategy extends AbstractAutocompleteLayoutStrategy implements AutocompleteEncodeStrategy {
    public void encodeFakeItem(FacesContext facesContext, UIComponent component) throws IOException {
        ResponseWriter responseWriter = facesContext.getResponseWriter();
        responseWriter.startElement(HtmlConstants.TR_ELEMENT, component);
        responseWriter.startElement(HtmlConstants.TD_ELEM, component);
        responseWriter.writeAttribute(HtmlConstants.STYLE_ATTRIBUTE, "display:none", null);
        responseWriter.endElement(HtmlConstants.TD_ELEM);
        responseWriter.endElement(HtmlConstants.TR_ELEMENT);
    }

    public void encodeItemsContainerBegin(FacesContext facesContext, UIComponent component) throws IOException {
        ResponseWriter responseWriter = facesContext.getResponseWriter();
        responseWriter.startElement(HtmlConstants.TABLE_ELEMENT, component);
        responseWriter.writeAttribute(HtmlConstants.ID_ATTRIBUTE, getContainerElementId(facesContext, component), null);
        responseWriter.writeAttribute(HtmlConstants.CLASS_ATTRIBUTE, "rf-au-tbl", null);
        responseWriter.startElement(HtmlConstants.TBODY_ELEMENT, component);
    }

    public void encodeItemsContainerEnd(FacesContext facesContext, UIComponent component) throws IOException {
        ResponseWriter responseWriter = facesContext.getResponseWriter();
        responseWriter.endElement(HtmlConstants.TBODY_ELEMENT);
        responseWriter.endElement(HtmlConstants.TABLE_ELEMENT);
    }

    public void encodeItemBegin(FacesContext facesContext, UIComponent component) throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();
        writer.startElement(HtmlConstants.TR_ELEMENT, component);
        writer.startElement(HtmlConstants.TD_ELEM, component);
    }

    private void encodeItemChildBegin(FacesContext facesContext, UIComponent component) throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();
        writer.startElement(HtmlConstants.TD_ELEM, component);
    }

    private void encodeItemChildEnd(FacesContext facesContext, UIComponent component) throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();
        writer.endElement(HtmlConstants.TD_ELEM);
    }

    public void encodeItemEnd(FacesContext facesContext, UIComponent component) throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();
        writer.endElement(HtmlConstants.TD_ELEM);
        writer.endElement(HtmlConstants.TR_ELEMENT);
    }

    public void encodeItem(FacesContext facesContext, UIComponent component) throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();
        writer.startElement(HtmlConstants.TR_ELEMENT, component);
        writer.writeAttribute(HtmlConstants.CLASS_ATTRIBUTE, "rf-au-itm", null);
        for (UIComponent child : component.getChildren()) {
            if (child instanceof UIColumn) {
                encodeItemChildBegin(facesContext, component);
                String styleClass = "rf-au-fnt rf-au-inp";
                styleClass = HtmlUtil.concatClasses(styleClass, child.getAttributes().get("styleClass"));
                writer.writeAttribute(HtmlConstants.CLASS_ATTRIBUTE, styleClass, null);
                child.encodeAll(facesContext);
                encodeItemChildEnd(facesContext, component);
            }
        }
        writer.endElement(HtmlConstants.TR_ELEMENT);
    }
}

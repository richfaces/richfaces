package org.richfaces.renderkit;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.richfaces.ui.common.HtmlConstants;

public class AutocompleteListLayoutStrategy extends AbstractAutocompleteLayoutStrategy implements AutocompleteEncodeStrategy {
    public void encodeFakeItem(FacesContext facesContext, UIComponent component) throws IOException {
        ResponseWriter responseWriter = facesContext.getResponseWriter();
        responseWriter.startElement(HtmlConstants.LI_ELEMENT, component);
        responseWriter.writeAttribute(HtmlConstants.STYLE_ATTRIBUTE, "display:none", null);
        responseWriter.endElement(HtmlConstants.LI_ELEMENT);
    }

    public void encodeItemsContainerBegin(FacesContext facesContext, UIComponent component) throws IOException {
        ResponseWriter responseWriter = facesContext.getResponseWriter();
        responseWriter.startElement(HtmlConstants.UL_ELEMENT, component);
        responseWriter.writeAttribute(HtmlConstants.ID_ATTRIBUTE, getContainerElementId(facesContext, component), null);
        responseWriter.writeAttribute(HtmlConstants.CLASS_ATTRIBUTE, "rf-au-lst-ul", null);
    }

    public void encodeItemsContainerEnd(FacesContext facesContext, UIComponent component) throws IOException {
        ResponseWriter responseWriter = facesContext.getResponseWriter();
        responseWriter.endElement(HtmlConstants.UL_ELEMENT);
    }

    public void encodeItemBegin(FacesContext facesContext, UIComponent component) throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();
        writer.startElement(HtmlConstants.LI_ELEMENT, component);
    }

    public void encodeItemEnd(FacesContext facesContext, UIComponent component) throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();
        writer.endElement(HtmlConstants.LI_ELEMENT);
    }

    public void encodeItem(FacesContext facesContext, UIComponent component) throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();
        encodeItemBegin(facesContext, component);
        writer.writeAttribute(HtmlConstants.CLASS_ATTRIBUTE, "rf-au-itm rf-au-opt rf-au-fnt rf-au-inp", null);
        for (UIComponent child : component.getChildren()) {
            child.encodeAll(facesContext);
        }
        encodeItemEnd(facesContext, component);
    }
}

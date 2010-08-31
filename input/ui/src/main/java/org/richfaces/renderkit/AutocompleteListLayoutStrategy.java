package org.richfaces.renderkit;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.ajax4jsf.renderkit.RendererUtils.HTML;

public class AutocompleteListLayoutStrategy extends AbstractAutocompleteLayoutStrategy implements
    AutocompleteEncodeStrategy {

    public void encodeFakeItem(FacesContext facesContext, UIComponent component) throws IOException {
        ResponseWriter responseWriter = facesContext.getResponseWriter();
        responseWriter.startElement(HTML.LI_ELEMENT, component);
        responseWriter.writeAttribute(HTML.STYLE_ATTRIBUTE, "display:none", null);
        responseWriter.endElement(HTML.LI_ELEMENT);

    }

    public void encodeItemsContainerBegin(FacesContext facesContext, UIComponent component) throws IOException {
        ResponseWriter responseWriter = facesContext.getResponseWriter();
        responseWriter.startElement(HTML.UL_ELEMENT, component);
        responseWriter.writeAttribute(HTML.ID_ATTRIBUTE, getContainerElementId(facesContext, component), null);
        responseWriter.writeAttribute(HTML.CLASS_ATTRIBUTE, "rf-au-list-ul", null);
    }

    public void encodeItemsContainerEnd(FacesContext facesContext, UIComponent component) throws IOException {
        ResponseWriter responseWriter = facesContext.getResponseWriter();
        responseWriter.endElement(HTML.UL_ELEMENT);
    }
    
    public void encodeItemBegin(FacesContext facesContext, UIComponent component) throws IOException {
    	ResponseWriter writer = facesContext.getResponseWriter();
        writer.startElement(HTML.LI_ELEMENT, component);
    }
    
    public void encodeItemEnd(FacesContext facesContext, UIComponent component) throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();
        writer.endElement(HTML.LI_ELEMENT);
    }

}

package org.richfaces.renderkit;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.ajax4jsf.renderkit.RendererUtils.HTML;

public class AutocompleteTableLayoutStrategy extends AbstractAutocompleteLayoutStrategy implements
    AutocompleteEncodeStrategy {

    public void encodeFakeItem(FacesContext facesContext, UIComponent component) throws IOException {
        ResponseWriter responseWriter = facesContext.getResponseWriter();
        responseWriter.startElement(HTML.TR_ELEMENT, component);
        responseWriter.startElement(HTML.TD_ELEM, component);
        responseWriter.writeAttribute(HTML.STYLE_ATTRIBUTE, "display:none", null);
        responseWriter.endElement(HTML.TD_ELEM);
        responseWriter.endElement(HTML.TR_ELEMENT);

    }

    public void encodeItemsContainerBegin(FacesContext facesContext, UIComponent component) throws IOException {
        ResponseWriter responseWriter = facesContext.getResponseWriter();
        responseWriter.startElement(HTML.TABLE_ELEMENT, component);
        responseWriter.writeAttribute(HTML.ID_ATTRIBUTE, getContainerElementId(facesContext, component), null);
        responseWriter.startElement(HTML.TBODY_ELEMENT, component);
    }

    public void encodeItemsContainerEnd(FacesContext facesContext, UIComponent component) throws IOException {
        ResponseWriter responseWriter = facesContext.getResponseWriter();
        responseWriter.endElement(HTML.TBODY_ELEMENT);
        responseWriter.endElement(HTML.TABLE_ELEMENT);
    }
    
    public void encodeItemBegin(FacesContext facesContext, UIComponent component) throws IOException {
    	ResponseWriter writer = facesContext.getResponseWriter();
        writer.startElement(HTML.TR_ELEMENT, component);
        writer.startElement(HTML.TD_ELEM, component);
    }
    
    public void encodeItemEnd(FacesContext facesContext, UIComponent component) throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();
        writer.endElement(HTML.TD_ELEM);
        writer.endElement(HTML.TR_ELEMENT);
    }

}

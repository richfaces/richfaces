package org.richfaces.renderkit;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;


public class AutocompleteListLayoutStrategy extends AbstractAutocompleteLayoutStrategy implements
    AutocompleteEncodeStrategy {

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
        responseWriter.writeAttribute(HtmlConstants.CLASS_ATTRIBUTE, "rf-au-list-ul", null);
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

}

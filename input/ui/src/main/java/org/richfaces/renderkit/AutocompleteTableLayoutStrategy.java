package org.richfaces.renderkit;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.ajax4jsf.renderkit.RendererUtils.HTML;
import org.richfaces.component.AbstractAutocomplete;

public class AutocompleteTableLayoutStrategy extends AbstractAutocompleteLayoutStrategy implements
    AutocompleteEncodeStrategy {

	public void encodeFakeItem(FacesContext facesContext, UIComponent component) throws IOException {
        ResponseWriter responseWriter = facesContext.getResponseWriter();
        responseWriter.startElement(HTML.TD_ELEM, component);
        responseWriter.writeAttribute(HTML.STYLE_ATTRIBUTE, "display:none", null);
        responseWriter.endElement(HTML.TD_ELEM);

    }

    public void encodeItemsContainerBegin(FacesContext facesContext, UIComponent component) throws IOException {
        ResponseWriter responseWriter = facesContext.getResponseWriter();
        responseWriter.startElement(HTML.TABLE_ELEMENT, component);
        responseWriter.writeAttribute(HTML.ID_ATTRIBUTE, getContainerElementId(facesContext, component), null);
        responseWriter.startElement(HTML.TBODY_ELEMENT, component);
        // responseWriter.writeAttribute(HTML.CLASS_ATTRIBUTE, "cb_list_ul", null);
    }

    public void encodeItemsContainerEnd(FacesContext facesContext, UIComponent component) throws IOException {
        ResponseWriter responseWriter = facesContext.getResponseWriter();
        responseWriter.endElement(HTML.TABLE_ELEMENT);
        responseWriter.endElement(HTML.TBODY_ELEMENT);
    }

    public void encodeItem(FacesContext facesContext, AbstractAutocomplete comboBox, Object item) throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();

        writer.startElement(HTML.TD_ELEM, comboBox);
        writer.writeAttribute(HTML.CLASS_ATTRIBUTE, "rf-au-option rf-au-font rf-au-input", null);

        if (comboBox.getChildCount() > 0) {
            for (UIComponent child : comboBox.getChildren()) {
                child.encodeAll(facesContext);
            }
        } else {
            if (item != null) {
                // TODO nick - use converter
                String value = null;
                if (comboBox.getItemConverter() != null) {
                    value = comboBox.getItemConverter().getAsString(facesContext, comboBox, item);
                }
                if (value != null) {
                    writer.writeText(value, null);
                }
                writer.writeText(item, null);
                // writer.writeText(InputUtils.getConvertedValue(facesContext, comboBox, item), null);
            }
        }

        writer.endElement(HTML.TD_ELEM);

    }

}

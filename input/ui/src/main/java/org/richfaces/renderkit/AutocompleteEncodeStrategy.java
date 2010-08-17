package org.richfaces.renderkit;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.richfaces.component.AbstractAutocomplete;


public interface AutocompleteEncodeStrategy {
    void encodeItemsContainerBegin(FacesContext facesContext, UIComponent component) throws IOException ;

    void encodeItemsContainerEnd(FacesContext facesContext, UIComponent component) throws IOException ;

    void encodeFakeItem(FacesContext facesContext, UIComponent component) throws IOException ;

    void encodeItem(FacesContext facesContext, AbstractAutocomplete comboBox,
        Object nextItem) throws IOException;

    public String getContainerElementId(FacesContext facesContext, UIComponent component);
}

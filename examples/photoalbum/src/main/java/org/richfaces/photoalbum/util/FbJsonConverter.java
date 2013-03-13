package org.richfaces.photoalbum.util;

import javax.enterprise.context.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Named;

import org.jboss.logging.Logger;
import org.richfaces.json.JSONArray;
import org.richfaces.json.JSONException;

@Named
@RequestScoped
@FacesConverter("fbConverter")
public class FbJsonConverter implements Converter {

    Logger logger = Logger.getLogger(FbJsonConverter.class);

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        JSONArray ja = null;
        try {
            ja = new JSONArray(value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ja;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        return ((JSONArray) value).toString();
    }

}
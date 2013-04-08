package org.richfaces.photoalbum.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.enterprise.context.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Named;

@Named
@RequestScoped
@FacesConverter("timestampConverter")
public class TimestampConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        Date d = new Date(Long.parseLong((String) value) * 1000); // convert to milliseconds
        
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        
        return sdf.format(d);
    }

}

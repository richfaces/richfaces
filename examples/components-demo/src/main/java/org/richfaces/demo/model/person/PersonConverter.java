package org.richfaces.demo.model.person;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 * Created by bleathem on 15/08/14.
 */
@FacesConverter("PersonConverter")
public class PersonConverter implements Converter {
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        return Person.peopleMap.get(value);
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        return value == null ? null : String.valueOf(((Person) value).getId());
    }
}

package org.richfaces.demo.autocomplete;

import java.io.Serializable;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.richfaces.demo.CountriesBean;
import org.richfaces.demo.Country;

@FacesConverter(value = "autocompleteBeanConverter")
public class AutocompleteBeanConverter implements Converter, Serializable {
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String value) {
        System.out.println("AutocompleteBeanConverter#getAsObject is called");
        if (value == null || value.equals("")) {
            return null;
        } else {
            CountriesBean countriesBean = (CountriesBean) FacesContext.getCurrentInstance().getExternalContext()
                    .getApplicationMap().get("countriesBean");
            for (Country country : countriesBean.getCountries()) {
                if (country.getName().equals(value)) {
                    return country;
                }
            }

            return null;
        }
    }

    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object value) {
        System.out.println("AutocompleteBeanConverter#getAsString is called");
        if (value != null) {
            Country bean;
            try {
                bean = (Country) value;
                return bean.getName();
            } catch (ClassCastException e) {
                System.out.println("Cannot cast " + value + "to Country");
                return null;
            }
        } else {
            return null;
        }
    }
}
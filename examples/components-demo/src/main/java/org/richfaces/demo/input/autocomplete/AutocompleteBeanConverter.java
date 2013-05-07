/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.richfaces.demo.input.autocomplete;

import java.io.Serializable;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.richfaces.demo.input.CountriesBean;
import org.richfaces.demo.input.Country;

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
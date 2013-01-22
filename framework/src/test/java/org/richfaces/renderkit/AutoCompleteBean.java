/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc. and individual contributors
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
package org.richfaces.renderkit;

import java.io.Serializable;
import java.util.Locale;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

/**
 * @author Nick Belaevski
 *
 */
public class AutoCompleteBean implements Serializable {
    private static final long serialVersionUID = 3072125097847582809L;

    private class CountryNamePredicate implements Predicate<Country> {
        private String countryNamePrefix;

        public CountryNamePredicate(String countryNamePrefix) {
            super();
            this.countryNamePrefix = countryNamePrefix;
        }

        public boolean apply(Country input) {
            if (countryNamePrefix == null || countryNamePrefix.length() == 0) {
                return true;
            }

            return input.getName().toLowerCase(Locale.US).startsWith(countryNamePrefix);
        }
    }

    private String value;

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    private String mode = "client";

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    private String layout = "list";

    public String getLayout() {
        return layout;
    }

    public void setLayout(String layout) {
        this.layout = layout;
    }

    private CountriesBean countriesBean;

    public void setCountriesBean(CountriesBean countriesBean) {
        this.countriesBean = countriesBean;
    }

    public Object autocomplete(FacesContext facesContext, UIComponent component, String value) {
        // for tests when value does not starts with prefix
        /*
         * String str = value; if (str.charAt(0)=='i') { str = str.substring(1); }
         */
        String v = mode.equals("lazyClient") || mode.equals("client") ? "" : value;
        return Collections2.filter(countriesBean.getCountries(), new CountryNamePredicate(v.toLowerCase()));
    }
}

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

package org.richfaces.bootstrap;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;

import org.richfaces.bootstrap.demo.capitals.Capital;



@ManagedBean
@RequestScoped
public class AutocompleteBean {
    private String value;
    private List<String> autocompleteList;
    @ManagedProperty(value = "#{capitalsParser.capitalsList}")
    private List<Capital> capitals;

    public AutocompleteBean() {
        // TODO Auto-generated constructor stub
    }

    @PostConstruct
    public void init() {
        autocompleteList = new ArrayList<String>();
        for (Capital cap : capitals) {
            autocompleteList.add(cap.getState());
        }
    }

    public List<String> autocomplete(String prefix) {
        ArrayList<String> result = new ArrayList<String>();
        if ((prefix == null) || (prefix.length() == 0)) {
            for (int i = 0; i < 10; i++) {
                result.add(capitals.get(i).getState());
            }
        } else {
            Iterator<Capital> iterator = capitals.iterator();
            while (iterator.hasNext()) {
                Capital elem = (iterator.next());
                if ((elem.getState() != null && elem.getState().toLowerCase().indexOf(prefix.toLowerCase()) == 0)
                    || "".equals(prefix)) {
                    result.add(elem.getState());
                }
            }
        }

        return result;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public List<String> getAutocompleteList() {
        return autocompleteList;
    }

    public void setAutocompleteList(List<String> autocompleteList) {
        this.autocompleteList = autocompleteList;
    }

    public List<Capital> getCapitals() {
        return capitals;
    }

    public void setCapitals(List<Capital> capitals) {
        this.capitals = capitals;
    }
}

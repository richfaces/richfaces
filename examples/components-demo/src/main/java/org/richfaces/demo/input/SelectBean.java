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

package org.richfaces.demo.input;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.richfaces.demo.model.person.Person;

@ManagedBean
@ViewScoped
public class SelectBean implements Serializable {

    private List<SelectItem> values;
    private String value;

    private Person person;

    @PostConstruct
    public void init() {
        values = new ArrayList<SelectItem>();
        for (String s : new String[] { "a", "b", "c", "d" }) {
            values.add(new SelectItem(s));
        }
    }

    public List<SelectItem> getValues() {
        return values;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public List<Person> getPeople() {
        return Person.people;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Collection<Person> suggest(FacesContext facesContext, UIComponent component, final String prefix) {
        Collection<Person> persons = Collections2.filter(Person.people, new Predicate<Person>() {
            @Override
            public boolean apply(Person input) {
                if (prefix == null) {
                    return true;
                }
                return input.getName().toLowerCase().startsWith(prefix.toLowerCase());
            }
        });
        return persons;
    }
}

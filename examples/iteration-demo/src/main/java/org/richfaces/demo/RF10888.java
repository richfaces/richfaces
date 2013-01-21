/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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
package org.richfaces.demo;

import java.util.List;
import java.util.regex.Pattern;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.base.Strings;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

/**
 * @author Nick Belaevski
 *
 */
@ManagedBean(name = "rf10888")
@SessionScoped
public class RF10888 {
    private String name;
    private String surname;
    private String email;
    @ManagedProperty(value = "#{persistenceService}")
    private PersistenceService persistenceService;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPersistenceService(PersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    private Predicate<Person> contains(String value, Function<Person, CharSequence> accessor) {
        return Predicates.compose(Predicates.contains(Pattern.compile(Pattern.quote(value), Pattern.CASE_INSENSITIVE)),
                accessor);
    }

    public Object getFilteredData() {
        @SuppressWarnings("unchecked")
        List<Person> resultList = (List<Person>) persistenceService.getEntityManager().createQuery("SELECT p from Person as p")
                .getResultList();

        List<Predicate<Person>> predicates = Lists.newArrayList();

        if (!Strings.isNullOrEmpty(name)) {
            predicates.add(contains(name, new Function<Person, CharSequence>() {
                public CharSequence apply(Person input) {
                    return input.getName();
                }
            }));
        }
        if (!Strings.isNullOrEmpty(surname)) {
            predicates.add(contains(surname, new Function<Person, CharSequence>() {
                public CharSequence apply(Person input) {
                    return input.getSurname();
                }
            }));
        }
        if (!Strings.isNullOrEmpty(email)) {
            predicates.add(contains(email, new Function<Person, CharSequence>() {
                public CharSequence apply(Person input) {
                    return input.getEmail();
                }
            }));
        }

        return Lists.newArrayList(Collections2.filter(resultList, Predicates.and(predicates)));
    }
}

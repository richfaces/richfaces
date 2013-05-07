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

/**
 *
 */
package org.richfaces.el.model;

import java.net.URI;
import java.util.Collection;
import java.util.Map;

/**
 * @author asmirnov
 *
 */
public class Person {
    public static final String FOO_BAR = "http://foo.bar/baz#";
    public static final String FOAF = "http://xmlns.com/foaf/0.1/";
    private String id;
    private Collection<URI> emailAddress;
    private String name;
    private String dummy;
    private Double account;
    private String string;
    private String property;
    private Map<Integer, Object> options;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Collection<URI> getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(Collection<URI> email) {
        this.emailAddress = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getAccount() {
        return account;
    }

    public void setAccount(Double account) {
        this.account = account;
    }

    public String getDummy() {
        return dummy;
    }

    public void setDummy(String dummy) {
        this.dummy = dummy;
    }

    public void setString(String string) {
        this.string = string;
    }

    public String getString() {
        return string;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getProperty() {
        return property;
    }

    public void setOptions(Map<Integer, Object> options) {
        this.options = options;
    }

    public Map<Integer, Object> getOptions() {
        return options;
    }
}

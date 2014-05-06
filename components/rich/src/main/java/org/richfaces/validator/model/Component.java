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

package org.richfaces.validator.model;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

import com.google.common.collect.Lists;

public class Component {
    private String type;
    private String function;
    private List<Resource> resource = Lists.newArrayList();

    /**
     * @return the type
     */
    @XmlElement
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the function
     */
    @XmlElement
    public String getFunction() {
        return function;
    }

    /**
     * @param function the function to set
     */
    public void setFunction(String function) {
        this.function = function;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @return the resource
     */
    public List<Resource> getResource() {
        return this.resource;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param resource the resource to set
     */
    @XmlElement
    public void setResource(List<Resource> resource) {
        this.resource = resource;
    }
}

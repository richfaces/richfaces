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

package org.richfaces.javascript;

import com.google.common.collect.Sets;

import java.util.LinkedHashSet;

/**
 * This class represent information about external JavaScript library as JSF resource
 *
 * @author asmirnov
 *
 */
public class LibraryResource {
    private final String library;
    private final String resourceName;

    /**
     * @param library
     * @param resourceName
     */
    public LibraryResource(String library, String resourceName) {
        this.library = library;
        this.resourceName = resourceName;
    }

    /**
     * @return the library
     */
    public String getLibrary() {
        return library;
    }

    /**
     * @return the resourceName
     */
    public String getResourceName() {
        return resourceName;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((library == null) ? 0 : library.hashCode());
        result = prime * result + ((resourceName == null) ? 0 : resourceName.hashCode());
        return result;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        LibraryResource other = (LibraryResource) obj;
        if (library == null) {
            if (other.library != null) {
                return false;
            }
        } else if (!library.equals(other.library)) {
            return false;
        }
        if (resourceName == null) {
            if (other.resourceName != null) {
                return false;
            }
        } else if (!resourceName.equals(other.resourceName)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return getLibrary() + ':' + getResourceName();
    }

    public static Iterable<LibraryResource> of(Iterable<LibraryScriptString> scripts) {
        LinkedHashSet<LibraryResource> resources = Sets.newLinkedHashSet();
        for (LibraryScriptString scriptString : scripts) {
            resources.add(scriptString.getResource());
        }
        return resources;
    }
}
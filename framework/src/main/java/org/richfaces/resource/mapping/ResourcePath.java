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
package org.richfaces.resource.mapping;

import java.net.URL;

import org.richfaces.resource.ResourceKey;
import org.richfaces.util.URLUtils;

/**
 * Represents a path to the resource
 *
 * @author Lukas Fryc
 */
public class ResourcePath {

    public String resourcePath;

    public ResourcePath(String resourcePath) {
        this.resourcePath = resourcePath;
    }

    public ResourcePath(URL url) {
        this.resourcePath = url.toExternalForm();
    }

    public ResourcePath(ResourceKey key) {
        this.resourcePath = key.getResourceName();

        if (key.getLibraryName() != null && !key.getLibraryName().isEmpty()) {
            this.resourcePath = key.getLibraryName() + "/" + key.getResourceName();
        }
    }

    public String toExternalForm() {
        return resourcePath;
    }

    public boolean isAbsoluteURL() {
        return URLUtils.isValidURL(resourcePath);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((resourcePath == null) ? 0 : resourcePath.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ResourcePath other = (ResourcePath) obj;
        if (resourcePath == null) {
            if (other.resourcePath != null)
                return false;
        } else if (!resourcePath.equals(other.resourcePath))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "ResourcePath [resourcePath=" + resourcePath + "]";
    }
}

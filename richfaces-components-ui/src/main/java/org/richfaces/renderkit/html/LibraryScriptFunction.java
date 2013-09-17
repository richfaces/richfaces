/*
 * $Id$
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
package org.richfaces.renderkit.html;

import java.util.Map;

import org.richfaces.javascript.LibraryFunction;
import org.richfaces.javascript.Message;
import org.richfaces.resource.ResourceKey;
import org.richfaces.resource.ResourceLibrary;

import com.google.common.collect.ImmutableSet;

/**
 * <p class="changed_added_4_0">
 * This class represents call to function in external library.
 * </p>
 *
 * @author asmirnov@exadel.com
 *
 */
public class LibraryScriptFunction implements ResourceLibrary {
    private final ImmutableSet<ResourceKey> resources;
    private final String name;
    private final Message message;
    private final Map<String, ? extends Object> parameters;

    public LibraryScriptFunction(LibraryFunction libraryScript, Message message, Map<String, ? extends Object> parameters) {
        this.message = message;
        this.parameters = parameters;
        this.resources = ImmutableSet.copyOf(libraryScript.getResources());
        this.name = libraryScript.getName();
    }

    public String getName() {
        return name;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.renderkit.html.LibraryScriptString#getResource()
     */
    public Iterable<ResourceKey> getResources() {
        return resources;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @return the message
     */
    public Message getMessage() {
        return this.message;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @return the parameters
     */
    public Map<String, ? extends Object> getParameters() {
        return this.parameters;
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
        result = prime * result + ((this.message == null) ? 0 : this.message.hashCode());
        result = prime * result + ((this.name == null) ? 0 : this.name.hashCode());
        result = prime * result + ((this.parameters == null) ? 0 : this.parameters.hashCode());
        result = prime * result + ((this.resources == null) ? 0 : this.resources.hashCode());
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
        LibraryScriptFunction other = (LibraryScriptFunction) obj;
        if (this.message == null) {
            if (other.message != null) {
                return false;
            }
        } else if (!this.message.equals(other.message)) {
            return false;
        }
        if (this.name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!this.name.equals(other.name)) {
            return false;
        }
        if (this.parameters == null) {
            if (other.parameters != null) {
                return false;
            }
        } else if (!this.parameters.equals(other.parameters)) {
            return false;
        }
        if (this.resources == null) {
            if (other.resources != null) {
                return false;
            }
        } else if (!this.resources.equals(other.resources)) {
            return false;
        }
        return true;
    }
}

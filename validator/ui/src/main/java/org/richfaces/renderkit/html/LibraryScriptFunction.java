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

import org.ajax4jsf.javascript.JSFunction;
import org.ajax4jsf.javascript.ScriptWithDependencies;
import org.richfaces.javascript.LibraryFunction;
import org.richfaces.resource.ResourceKey;

/**
 * <p class="changed_added_4_0">
 * This class represents call to function in external library.
 * </p>
 * 
 * @author asmirnov@exadel.com
 * 
 */
public class LibraryScriptFunction extends JSFunction implements ScriptWithDependencies {

    private final Iterable<ResourceKey> resources;
    private final String name;

    public LibraryScriptFunction(LibraryFunction libraryScript, Object... parameters) {
        super(libraryScript.getName(), parameters);
        this.resources = libraryScript.getResources();
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

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((resources == null) ? 0 : resources.hashCode());
        result = prime * result + getParameters().hashCode();
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
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        if (resources == null) {
            if (other.resources != null) {
                return false;
            }
        } else if (!resources.equals(other.resources)) {
            return false;
        } else if (!getParameters().equals(other.getParameters())) {
            return false;
        }
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */

}

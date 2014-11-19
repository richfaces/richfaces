/*
 * JBoss, Home of Professional Open Source
 * Copyright ${year}, Red Hat, Inc. and individual contributors
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

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

import org.ajax4jsf.javascript.ScriptWithDependencies;
import org.richfaces.resource.ResourceKey;

/**
 * @author abelevich
 *
 */
public abstract class DnDScript implements ScriptWithDependencies {
    private static final Set<ResourceKey> BASE_RESOURCES = new LinkedHashSet<ResourceKey>();

    static {
        BASE_RESOURCES.add(new ResourceKey("jquery.js", "org.richfaces"));
        BASE_RESOURCES.add(new ResourceKey("jquery.position.js", "org.richfaces"));
        BASE_RESOURCES.add(new ResourceKey("richfaces.js", "org.richfaces"));
        BASE_RESOURCES.add(new ResourceKey("richfaces-base-component.js", "org.richfaces"));
        BASE_RESOURCES.add(new ResourceKey("richfaces-event.js", "org.richfaces"));
        BASE_RESOURCES.add(new ResourceKey("core.js", "com.jqueryui"));
        BASE_RESOURCES.add(new ResourceKey("widget.js", "com.jqueryui"));
        BASE_RESOURCES.add(new ResourceKey("mouse.js", "com.jqueryui"));
        BASE_RESOURCES.add(new ResourceKey("draggable.js", "com.jqueryui"));
        BASE_RESOURCES.add(new ResourceKey("droppable.js", "com.jqueryui"));
    }

    private final String name;

    public DnDScript(String name) {
        this.name = name;
    }

    public Set<ResourceKey> getBaseResources() {
        return BASE_RESOURCES;
    }

    public void appendScriptToStringBuilder(StringBuilder stringBuilder) {
        try {
            appendScript(stringBuilder);
        } catch (IOException e) {
            // ignore
        }
    }

    public String toScript() {
        return name;
    }

    public void appendScript(Appendable target) throws IOException {
        target.append(name);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

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

        DnDScript other = (DnDScript) obj;
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }

        return true;
    }
}

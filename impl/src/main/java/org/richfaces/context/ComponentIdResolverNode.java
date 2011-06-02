/*
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
package org.richfaces.context;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

final class ComponentIdResolverNode {
    private Set<String> fullIds;
    private String id;
    private ComponentIdResolverNode parent;
    private Map<String, ComponentIdResolverNode> children = new HashMap<String, ComponentIdResolverNode>(2);

    public ComponentIdResolverNode(ComponentIdResolverNode parent, String id) {
        this.parent = parent;
        this.id = id;
    }

    public ComponentIdResolverNode getChild(String name) {
        return children.get(name);
    }

    public ComponentIdResolverNode getOrCreateChild(String name) {
        ComponentIdResolverNode child = children.get(name);
        if (child == null) {
            child = new ComponentIdResolverNode(this, name);
            children.put(name, child);
        }

        return child;
    }

    public boolean hasChildren() {
        return !children.isEmpty();
    }

    public Map<String, ComponentIdResolverNode> getChildren() {
        return children;
    }

    protected void removeNode(String id) {
        children.remove(id);
    }

    public void remove() {
        if (parent != null) {
            parent.removeNode(id);
            if (!parent.hasChildren()) {
                parent.remove();
            }
        }
    }

    public Set<String> getFullIds() {
        return fullIds;
    }

    public void addFullId(String fullId) {
        if (fullIds == null) {
            fullIds = new HashSet<String>(2);
        }

        fullIds.add(fullId);
    }

    public void resetFullIds() {
        this.fullIds = null;
    }

    public String getId() {
        return id;
    }

    public ComponentIdResolverNode getParent() {
        return parent;
    }

    public void clearChildren() {
        children.clear();
    }
}
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

package org.richfaces.demo.model.tree.adaptors;

import java.util.List;

import com.google.common.collect.Lists;

/**
 * @author Nick Belaevski
 * @since 3.2
 */

public class SimpleRecursiveNode {

    private SimpleRecursiveNode parent;

    private List<SimpleRecursiveNode> children = Lists.newArrayList();

    private String text;

    public SimpleRecursiveNode(SimpleRecursiveNode parent, String text) {
        super();
        this.parent = parent;
        if (parent != null) {
            parent.addChild(this);
        }
        this.text = text;
    }

    public void addChild(SimpleRecursiveNode node) {
        children.add(node);
    }

    public void removeChild(SimpleRecursiveNode node) {
        children.remove(node);
    }

    public void remove() {
        if (parent != null) {
            parent.removeChild(this);
        }
    }

    public SimpleRecursiveNode getParent() {
        return parent;
    }

    public List<SimpleRecursiveNode> getChildren() {
        return children;
    }

    public String getText() {
        return text;
    }
}

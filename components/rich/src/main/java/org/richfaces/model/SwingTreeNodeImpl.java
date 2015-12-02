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
package org.richfaces.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Enumeration;

import javax.swing.tree.TreeNode;

import com.google.common.base.MoreObjects;
import com.google.common.base.MoreObjects.ToStringHelper;
import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;

/**
 * @author Nick Belaevski
 *
 */
public class SwingTreeNodeImpl<T> implements TreeNode, Serializable {
    private static final long serialVersionUID = 8841984268370598781L;
    private TreeNode parent;
    private T data;
    private Collection<TreeNode> children;
    private boolean allowUpdateParents = true;

    public SwingTreeNodeImpl() {
        this(null);
    }

    public SwingTreeNodeImpl(Collection<TreeNode> children) {
        this.children = wrapNull(children);
    }

    void setAllowUpdateParents(boolean allowUpdateParents) {
        this.allowUpdateParents = allowUpdateParents;
    }

    private static Collection<TreeNode> wrapNull(Collection<TreeNode> src) {
        return src != null ? src : Lists.<TreeNode>newArrayList();
    }

    public TreeNode getChildAt(int childIndex) {
        if (childIndex < getChildCount()) {
            return Iterables.get(children, childIndex);
        }

        return null;
    }

    public int getChildCount() {
        return children.size();
    }

    public TreeNode getParent() {
        return parent;
    }

    public void setParent(TreeNode parent) {
        this.parent = parent;
    }

    public int getIndex(TreeNode node) {
        return Iterables.indexOf(children, Predicates.equalTo(node));
    }

    public void addChild(TreeNode node) {
        children.add(node);
        if (allowUpdateParents && node instanceof SwingTreeNodeImpl<?>) {
            SwingTreeNodeImpl<?> treeNodeImpl = (SwingTreeNodeImpl<?>) node;
            treeNodeImpl.setParent(this);
        }
    }

    public void removeChild(TreeNode node) {
        if (children.remove(node)) {
            if (allowUpdateParents && node instanceof SwingTreeNodeImpl<?>) {
                SwingTreeNodeImpl<?> treeNodeImpl = (SwingTreeNodeImpl<?>) node;
                treeNodeImpl.setParent(null);
            }
        }
    }

    public boolean getAllowsChildren() {
        return true;
    }

    public boolean isLeaf() {
        return children.isEmpty();
    }

    public Enumeration<?> children() {
        return Iterators.asEnumeration(children.iterator());
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Collection<TreeNode> getChildrenList() {
        return children;
    }

    @Override
    public String toString() {
        ToStringHelper toStringHelper = MoreObjects.toStringHelper(this);
        toStringHelper.add("data", data);

        return toStringHelper.toString();
    }
}
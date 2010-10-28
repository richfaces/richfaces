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
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.tree.TreeNode;

import com.google.common.collect.Iterators;

/**
 * @author Nick Belaevski
 * 
 */
public class SwingTreeNodeImpl<T> implements TreeNode, Serializable {

    private static final long serialVersionUID = 8841984268370598781L;

    private TreeNode parent;
    
    private T data;
    
    private List<TreeNode> children = new ArrayList<TreeNode>();
    
    public SwingTreeNodeImpl() {
    }
    
    public SwingTreeNodeImpl(List<TreeNode> children) {
        this.children = children;
    }

    public TreeNode getChildAt(int childIndex) {
        return children.get(childIndex);
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
        return children.indexOf(node);
    }

    public void addChild(TreeNode node) {
        ((SwingTreeNodeImpl<?>) node).setParent(this);
        children.add(node);
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
    
    public List<TreeNode> getChildrenList() {
        return children;
    }
}

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
package org.richfaces.demo.iteration;

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
public class LazyTreeNode implements TreeNode, Serializable {
    private static final long serialVersionUID = 7222747310505408841L;
    private TreeNode srcNode;
    private LazyTreeNode parentNode;
    private List<LazyTreeNode> children = null;

    public LazyTreeNode(LazyTreeNode parentNode, TreeNode srcNode) {
        super();
        this.parentNode = parentNode;
        this.srcNode = srcNode;
    }

    public LazyTreeNode(TreeNode srcNode) {
        this(null, srcNode);
    }

    private void initializeChildren() {
        if (children != null) {
            return;
        }

        children = new ArrayList<LazyTreeNode>();

        Enumeration srcChildren = srcNode.children();
        while (srcChildren.hasMoreElements()) {
            TreeNode srcChild = (TreeNode) srcChildren.nextElement();
            children.add(new LazyTreeNode(this, srcChild));
        }
    }

    public TreeNode getChildAt(int childIndex) {
        initializeChildren();

        return children.get(childIndex);
    }

    public int getChildCount() {
        initializeChildren();

        return children.size();
    }

    public TreeNode getParent() {
        return parentNode;
    }

    public int getIndex(TreeNode node) {
        throw new UnsupportedOperationException();
    }

    public boolean getAllowsChildren() {
        return true;
    }

    public boolean isLeaf() {
        return false;
    }

    public Enumeration children() {
        initializeChildren();

        return Iterators.asEnumeration(children.iterator());
    }

    public TreeNode getWrappedNode() {
        return srcNode;
    }
}

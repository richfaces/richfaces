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
package org.richfaces.ui.iteration.tree.model;

import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;

import javax.faces.convert.Converter;
import javax.swing.tree.TreeNode;

import org.richfaces.model.TreeDataModelTuple;
import org.richfaces.ui.iteration.tree.convert.IntegerSequenceRowKeyConverter;

import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;

/**
 * @author Nick Belaevski
 *
 */
public class SwingTreeNodeDataModelImpl extends NodesTreeSequenceKeyModel<TreeNode> {
    private static final Converter DEFAULT_CONVERTER = new IntegerSequenceRowKeyConverter();
    private boolean asksAllowsChildren = false;
    private Object wrappedData;

    private TreeNode createFakeRootNode(Object wrappedData) {
        Collection<TreeNode> nodes;

        if (wrappedData instanceof Collection<?>) {
            nodes = (Collection<TreeNode>) wrappedData;
        } else if (wrappedData instanceof TreeNode) {
            nodes = Lists.newArrayList((TreeNode) wrappedData);
        } else if (wrappedData == null) {
            nodes = null;
        } else {
            throw new IllegalArgumentException(String.valueOf(wrappedData));
        }

        SwingTreeNodeImpl<?> treeNodeImpl = new SwingTreeNodeImpl<Object>(nodes);
        treeNodeImpl.setAllowUpdateParents(false);
        return treeNodeImpl;
    }

    public void setWrappedData(Object data) {
        this.wrappedData = data;

        setRootNode(createFakeRootNode(data));
    }

    public Object getWrappedData() {
        return wrappedData;
    }

    protected TreeNode findChild(TreeNode parent, Integer simpleKey) {
        int childIdx = simpleKey.intValue();

        if (childIdx < parent.getChildCount()) {
            return parent.getChildAt(childIdx);
        }

        return null;
    }

    public Iterator<TreeDataModelTuple> children() {
        Iterator<TreeNode> children = Iterators.forEnumeration((Enumeration<TreeNode>) getData().children());
        return new IterableDataTuplesIterator(getRowKey(), children);
    }

    public boolean isLeaf() {
        if (!asksAllowsChildren) {
            return getData().isLeaf();
        } else {
            return !getData().getAllowsChildren();
        }
    }

    @Override
    protected TreeNode setupChildContext(Object segment) {
        return findChild(getData(), (Integer) segment);
    }

    public Converter getRowKeyConverter() {
        return DEFAULT_CONVERTER;
    }
}
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

import static com.google.common.base.Objects.firstNonNull;

import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;

import javax.swing.tree.TreeNode;

import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
/**
 * @author Nick Belaevski
 * 
 */
public class SwingTreeNodeDataModelImpl extends TreeSequenceKeyModel<Integer, TreeNode> {

    private static final SequenceRowKey<Integer> EMPTY_KEY = new SequenceRowKey<Integer>();
    
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
    
    public Object getParentRowKey(Object rowKey) {
        throw new UnsupportedOperationException();
    }

    public void setWrappedData(Object data) {
        this.wrappedData = data;
        
        setRootNode(createFakeRootNode(data));
    }
    
    public Object getWrappedData() {
        return wrappedData;
    }

    protected TreeNode findChild(TreeNode parent, Integer simpleKey) {
        return parent.getChildAt(simpleKey.intValue());
    }

    public Iterator<TreeDataModelTuple> children() {
        Iterator<TreeNode> children = Iterators.forEnumeration((Enumeration<TreeNode>) getData().children());
        return new SwingTreeNodeTuplesIterator(getRowKey(), children);
    }

    public boolean isLeaf() {
        if (!asksAllowsChildren) {
            return getData().isLeaf();
        } else {
            return !getData().getAllowsChildren();
        }
    }
    
    @Override
    protected void walkNext(Integer segment) {
        TreeNode child = findChild(getData(), segment);
        //TODO what if node is missing?
        //TODO - optimize - remove partial keys creation
        setRowKeyAndData(safeGetRowKey().append(segment), child);
    }

    private SequenceRowKey<Integer> safeGetRowKey() {
        return firstNonNull(getRowKey(), EMPTY_KEY);
    }
}
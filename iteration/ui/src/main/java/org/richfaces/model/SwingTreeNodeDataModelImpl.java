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

import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.NoSuchElementException;

import javax.swing.tree.TreeNode;

import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;

/**
 * @author Nick Belaevski
 * 
 */
public class SwingTreeNodeDataModelImpl implements TreeDataModel<TreeNode> {

    /**
     * @author Nick Belaevski
     * 
     */
    private final class FakeRootNode implements TreeNode {
        public boolean isLeaf() {
            return !wrappedData.isEmpty();
        }

        public TreeNode getParent() {
            return null;
        }

        public int getIndex(TreeNode node) {
            if (wrappedData == null) {
                return -1;
            }
            
            return Iterables.indexOf(wrappedData, Predicates.equalTo(node));
        }

        public int getChildCount() {
            if (wrappedData == null) {
                return 0;
            }
            
            return wrappedData.size();
        }

        public TreeNode getChildAt(int childIndex) {
            if (wrappedData == null) {
                throw new NoSuchElementException(String.valueOf(childIndex));
            }
            
            return Iterables.get(wrappedData, childIndex);
        }

        public boolean getAllowsChildren() {
            return true;
        }

        public Enumeration<?> children() {
            if (wrappedData == null) {
                return Iterators.asEnumeration(Iterators.emptyIterator());
            }
            
            return Iterators.asEnumeration(wrappedData.iterator());
        }
    }

    private static final SequenceRowKey<Integer> EMPTY_SEQUENCE_ROW_KEY = new SequenceRowKey<Integer>();

    private Collection<TreeNode> wrappedData = null;
    
    private TreeNode fakeRootNode = new FakeRootNode();

    private TreeNode selectedNode;
    
    private SequenceRowKey<Integer> selectedRowKey;
    
    private Iterator<TreeNode> findChildren(SequenceRowKey<Integer> compositeKey) {
        TreeNode treeNode = findNode(compositeKey);
        
        if (treeNode == null) {
            return Iterators.emptyIterator();
        }
        
        return Iterators.forEnumeration((Enumeration<TreeNode>) treeNode.children());
    }
    
    private TreeNode findNode(SequenceRowKey<Integer> compositeKey) {
        if (compositeKey == null) {
            return null;
        }
        
        TreeNode result = fakeRootNode;
        
        for (Integer simpleKey : compositeKey.getSimpleKeys()) {
            int idx = simpleKey.intValue();
            
            if (idx < result.getChildCount()) {
                result = result.getChildAt(idx);
            } else {
                result = null;
                break;
            }
        }
        
        return result;
    }
    
    public void setRowKey(Object key) {
        this.selectedRowKey = (SequenceRowKey<Integer>) key;
        this.selectedNode = findNode(selectedRowKey);
    }

    public Object getRowKey() {
        return selectedRowKey;
    }

    private SequenceRowKey<Integer> castKeyAndWrapNull(Object rowKey) {
        if (rowKey == null) {
            return EMPTY_SEQUENCE_ROW_KEY;
        }
        
        return (SequenceRowKey<Integer>) rowKey;
    }
    
    public Iterator<Object> getChildrenRowKeysIterator(Object rowKey) {
        SequenceRowKey<Integer> sequenceKey = castKeyAndWrapNull(rowKey);
        Iterator<TreeNode> itr = findChildren(sequenceKey);
        
        return new SequenceRowKeyIterator<TreeNode>(sequenceKey, itr);
    }

    public TreeNode getData() {
        if (!isDataAvailable()) {
            throw new IllegalArgumentException();
        }
        
        return selectedNode;
    }

    public Object getParentRowKey(Object rowKey) {
        throw new UnsupportedOperationException();
    }

    public boolean isDataAvailable() {
        return selectedNode != null;
    }
    
    public Object getWrappedData() {
        return wrappedData;
    }
    
    public void setWrappedData(Object wrappedData) {
        this.wrappedData = (Collection<TreeNode>) wrappedData;
    }
}

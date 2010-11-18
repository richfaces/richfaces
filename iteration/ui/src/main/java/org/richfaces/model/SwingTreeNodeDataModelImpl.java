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
public class SwingTreeNodeDataModelImpl extends TreeSequenceKeyModel<Integer, TreeNode> {

    /**
     * @author Nick Belaevski
     * 
     */
    private static final class SwingTreeNodeRowKeyIterator extends SequenceRowKeyIterator<Integer, TreeNode> {
        
        private int counter = 0;

        /**
         * @param baseKey
         * @param baseElement
         * @param itr
         */
        private SwingTreeNodeRowKeyIterator(SequenceRowKey<Integer> baseKey, TreeNode baseElement,
            Iterator<TreeNode> itr) {
            super(baseKey, baseElement, itr);
        }

        @Override
        protected Integer nextKey() {
            return counter++;
        }
    }


    /**
     * @author Nick Belaevski
     * 
     */
    private final class FakeRootNode implements TreeNode {
        
        private Collection<TreeNode> wrappedData;
        
        public FakeRootNode(Collection<TreeNode> wrappedData) {
            super();
            this.wrappedData = wrappedData;
        }

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
        
        public Collection<TreeNode> getWrappedData() {
            return wrappedData;
        }
    }

    private Iterator<TreeNode> safeGetChildren(SequenceRowKey<Integer> key, TreeNode treeNode) {
        if (treeNode == null) {
            return Iterators.emptyIterator();
        }
        
        return Iterators.forEnumeration((Enumeration<TreeNode>) treeNode.children());
    }
    

    public Object getParentRowKey(Object rowKey) {
        throw new UnsupportedOperationException();
    }

    public boolean isLeaf() {
        if (!isDataAvailable()) {
            throw new IllegalStateException();
        }
        
        TreeNode treeNode = getData();
        
        return !treeNode.getAllowsChildren() || treeNode.isLeaf();
    }

    public void setWrappedData(Object data) {
        setRootNode(new FakeRootNode((Collection<TreeNode>) data));
    }
    
    public Collection<TreeNode> getWrappedData() {
        FakeRootNode rootNode = (FakeRootNode) getRootNode();
        if (rootNode == null) {
            return null;
        }
        return rootNode.getWrappedData();
    }

    @Override
    protected TreeNode findChild(TreeNode parent, Integer simpleKey) {
        int idx = simpleKey.intValue();
        if (idx >= 0 && idx < parent.getChildCount()) {
            return parent.getChildAt(idx);
        }
        
        return null;
    }


    @Override
    protected SequenceRowKeyIterator<Integer, TreeNode> createChildrenIterator(SequenceRowKey<Integer> baseKey,
        TreeNode value) {
        
        return new SwingTreeNodeRowKeyIterator(baseKey, value, safeGetChildren(baseKey, value));
    }
}

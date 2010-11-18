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

import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

import javax.faces.context.FacesContext;

import org.ajax4jsf.model.DataVisitor;
import org.ajax4jsf.model.Range;
import org.richfaces.component.TreeRange;

/**
 * @author Nick Belaevski
 * 
 */
public abstract class TreeSequenceKeyModel<K, V> implements TreeDataModel<V> {

    private V rootNode;
    
    private V currentData;
    
    private SequenceRowKey<K> currentRowKey;
    
    private LinkedList<SequenceRowKeyIterator<K, V>> keysStack = new LinkedList<SequenceRowKeyIterator<K, V>>();
    
    public SequenceRowKey<K> getRowKey() {
        return currentRowKey;
    }

    public void setRowKey(Object rowKey) {
        this.currentRowKey = (SequenceRowKey<K>) rowKey;
        this.currentData = findData(currentRowKey);
    }

    public boolean isDataAvailable() {
        return currentRowKey == null || currentData != null;
    }

    public abstract boolean isLeaf();

    public V getData() {
        if (!isDataAvailable()) {
            throw new IllegalArgumentException();
        }
        
        return currentData;
    }

    protected boolean isRootNodeKey(SequenceRowKey<K> key) {
        return key == null || key.getLastKeySegment() == null;
    }
    
    protected V findData(SequenceRowKey<K> key) {
        if (key == null) {
            return rootNode;
        }
        
        if (!keysStack.isEmpty()) {
            ListIterator<SequenceRowKeyIterator<K, V>> listIterator = keysStack.listIterator(keysStack.size());
            
            while (listIterator.hasPrevious()) {
                SequenceRowKeyIterator<K, V> previous = listIterator.previous();

                V baseNode = null;
                
                SequenceRowKey<K> baseKey = previous.getBaseKey();
                if (isRootNodeKey(baseKey) && isRootNodeKey(key.getParent())) {
                    baseNode = rootNode;
                } else if (baseKey.equals(key.getParent())) {
                    baseNode = previous.getBaseElement();
                }
                
                if (baseNode == null) {
                    continue;
                }
                
                return findChild(baseNode, key.getLastKeySegment());
            }
        }
        
        V result = rootNode;
        
        for (K simpleKey : key.getSimpleKeys()) {
            result = findChild(result, simpleKey);

            if (result == null) {
                break;
            }
        }
        
        return result;
    }
        
    protected abstract V findChild(V parent, K simpleKey);
    
    protected abstract SequenceRowKeyIterator<K, V> createChildrenIterator(SequenceRowKey<K> baseKey, V value);
    
    public void enterNode(DataVisitor visitor) {
        SequenceRowKey<K> sequenceKey = getRowKey();
        V data = findData(sequenceKey);
        
        keysStack.addLast(createChildrenIterator(sequenceKey, data));

        if (visitor instanceof TreeDataVisitor) {
            ((TreeDataVisitor) visitor).enterNode();
        }
    }

    public void walk(FacesContext context, DataVisitor visitor, Range range, Object argument) {
        if (getRowKey() != null) {
            visitor.process(context, getRowKey(), argument);
        }

        TreeRange treeRange = (TreeRange) range;
        
        if (treeRange.shouldIterateChildren(getRowKey())) {
            enterNode(visitor);
            Iterator<Object> keysIterator = keysStack.getLast();
            while (keysIterator.hasNext()) {
                Object key = (Object) keysIterator.next();
                setRowKey(key);
                walk(context, visitor, range, argument);
            }
            exitNode(visitor);
        }
    }

    public void exitNode(DataVisitor visitor) {
        if (visitor instanceof TreeDataVisitor) {
            ((TreeDataVisitor) visitor).exitNode();
        }

        keysStack.removeLast();
    }

    public abstract Object getParentRowKey(Object rowKey);

    protected V getRootNode() {
        return rootNode;
    }
    
    protected void setRootNode(V rootNode) {
        this.rootNode = rootNode;
    }
    
}

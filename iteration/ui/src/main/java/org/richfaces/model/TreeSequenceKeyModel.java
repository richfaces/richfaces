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

import javax.faces.context.FacesContext;

import org.ajax4jsf.model.DataVisitor;
import org.ajax4jsf.model.ExtendedDataModel;
import org.ajax4jsf.model.Range;


/**
 * @author Nick Belaevski
 * 
 */
public abstract class TreeSequenceKeyModel<K, V> extends ExtendedDataModel<V> implements TreeDataModel<V> {

    private final SequenceRowKey<K> emptyKey = new SequenceRowKey<K>();
    
    private V rootNode;
    
    private V data;
    
    private SequenceRowKey<K> rowKey;
    
    public SequenceRowKey<K> getRowKey() {
        return rowKey;
    }

    protected SequenceRowKey<K> safeGetRowKey() {
        SequenceRowKey<K> key = getRowKey();

        if (key == null) {
            key = emptyKey;
        }
        
        return key;
    }
    
    public void setRowKey(Object rowKey) {
        if (this.rowKey == null || !this.rowKey.equals(rowKey)) {
            walkKey((SequenceRowKey<K>) rowKey);
        }
    }

    protected void resetRowKeyAndData() {
        setRowKeyAndData(null, rootNode);
    }
    
    protected void setRowKeyAndData(SequenceRowKey<K> key, V data) {
        this.rowKey = key;
        this.data = data;
    }
    
    public boolean isDataAvailable() {
        return getRowKey() == null || data != null;
    }

    public V getData() {
        if (!isDataAvailable()) {
            throw new IllegalArgumentException();
        }
        
        return data;
    }
    
    protected void walkKey(SequenceRowKey<K> key) {
        resetRowKeyAndData();

        if (key != null) {
            for (K simpleKey: key.getSimpleKeys()) {
                walkNext(simpleKey);
            }
        }
    }

    protected abstract void walkNext(K segment);
    
    protected V getRootNode() {
        return rootNode;
    }
    
    protected void setRootNode(V rootNode) {
        this.rootNode = rootNode;
    }
    
    //TODO ExtendedDataModel legacy
    @Override
    public void walk(FacesContext context, DataVisitor visitor, Range range, Object argument) {
        throw new UnsupportedOperationException();
    }


    @Override
    public boolean isRowAvailable() {
        return isDataAvailable();
    }


    @Override
    public int getRowCount() {
        throw new UnsupportedOperationException();
    }


    @Override
    public V getRowData() {
        return getData();
    }


    @Override
    public int getRowIndex() {
        throw new UnsupportedOperationException();
    }


    @Override
    public void setRowIndex(int rowIndex) {
        throw new UnsupportedOperationException();
    }

    public TreeDataModelTuple createSnapshot() {
        return new TreeDataModelTuple(getRowKey(), getData());
    }

    public void restoreFromSnapshot(TreeDataModelTuple tuple) {
        setRowKeyAndData((SequenceRowKey<K>) tuple.getRowKey(), (V) tuple.getData());
    }
    
}

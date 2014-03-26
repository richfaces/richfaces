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
public abstract class TreeSequenceKeyModel<V> extends ExtendedDataModel<V> implements TreeDataModel<V> {
    private V data;
    private SequenceRowKey rowKey;

    public SequenceRowKey getRowKey() {
        return rowKey;
    }

    public void setRowKey(Object rowKey) {
        SequenceRowKey sequenceKey = (SequenceRowKey) rowKey;

        if (this.rowKey == null || !this.rowKey.equals(rowKey)) {
            setupKey(sequenceKey);
        }
    }

    protected void setData(V data) {
        this.data = data;
    }

    protected void setRowKeyAndData(SequenceRowKey key, V data) {
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

    protected abstract void setupKey(SequenceRowKey key);

    // TODO ExtendedDataModel legacy
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

    public Object getParentRowKey(Object rowKey) {
        SequenceRowKey key = getRowKey();

        if (key == null) {
            return null;
        }

        return key.getParent();
    }
}
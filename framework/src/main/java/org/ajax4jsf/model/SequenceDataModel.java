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
package org.ajax4jsf.model;

import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;

/**
 * @author shura
 *
 */
public class SequenceDataModel<E> extends ExtendedDataModel<E> {
    private DataModel<E> wrappedModel;

    /**
     * @param wrapped
     */
    public SequenceDataModel(DataModel<E> wrapped) {
        super();
        this.wrappedModel = wrapped;
    }

    public void walk(FacesContext context, DataVisitor visitor, Range range, Object argument) {
        final SequenceRange seqRange = (SequenceRange) range;
        int rows = seqRange.getRows();
        int rowCount = wrappedModel.getRowCount();
        int currentRow = seqRange.getFirstRow();

        if (rows > 0) {
            rows += currentRow;

            if (rowCount >= 0) {
                rows = Math.min(rows, rowCount);
            }
        } else if (rowCount >= 0) {
            rows = rowCount;
        } else {
            rows = -1;
        }

        DataVisitResult visitResult = null;

        while (rows < 0 || currentRow < rows) {
            wrappedModel.setRowIndex(currentRow);

            if (wrappedModel.isRowAvailable()) {
                visitResult = visitor.process(context, new Integer(currentRow), argument);

                if (DataVisitResult.STOP.equals(visitResult)) {
                    break;
                }
            } else {
                break;
            }

            currentRow++;
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.model.ExtendedDataModel#getRowKey()
     */
    public Object getRowKey() {
        int index = wrappedModel.getRowIndex();

        if (index < 0) {
            return null;
        }

        return new Integer(index);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.model.ExtendedDataModel#setRowKey(java.lang.Object)
     */
    public void setRowKey(Object key) {
        if (null == key) {
            wrappedModel.setRowIndex(-1);
        } else {
            Integer index = (Integer) key;

            wrappedModel.setRowIndex(index.intValue());
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.faces.model.DataModel#getRowCount()
     */
    public int getRowCount() {
        return wrappedModel.getRowCount();
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.faces.model.DataModel#getRowData()
     */
    public E getRowData() {
        return wrappedModel.getRowData();
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.faces.model.DataModel#getRowIndex()
     */
    public int getRowIndex() {
        return wrappedModel.getRowIndex();
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.faces.model.DataModel#getWrappedData()
     */
    public Object getWrappedData() {
        return wrappedModel.getWrappedData();
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.faces.model.DataModel#isRowAvailable()
     */
    public boolean isRowAvailable() {
        return wrappedModel.isRowAvailable();
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.faces.model.DataModel#setRowIndex(int)
     */
    public void setRowIndex(int rowIndex) {
        wrappedModel.setRowIndex(rowIndex);
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.faces.model.DataModel#setWrappedData(java.lang.Object)
     */
    public void setWrappedData(Object data) {
        wrappedModel.setWrappedData(data);
    }

    /**
     * @return the wrappedModel
     */
    protected DataModel<E> getWrappedModel() {
        return this.wrappedModel;
    }

    /**
     * @param wrappedModel the wrappedModel to set
     */
    protected void setWrappedModel(DataModel<E> wrappedModel) {
        this.wrappedModel = wrappedModel;
    }
}

/**
 * License Agreement.
 *
 * Rich Faces - Natural Ajax for Java Server Faces (JSF)
 *
 * Copyright (C) 2007 Exadel, Inc.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License version 2.1 as published by the Free Software Foundation.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA
 */



package org.ajax4jsf.tests;

import java.io.IOException;

import javax.faces.context.FacesContext;

import org.ajax4jsf.model.DataVisitor;
import org.ajax4jsf.model.ExtendedDataModel;
import org.ajax4jsf.model.Range;
import org.ajax4jsf.model.SequenceRange;

/**
 * @author shura
 *
 */
public class MockDataModel extends ExtendedDataModel {
    public static final int ROWS = 10;
    private int minRow = 0;
    private int rowIndex = 0;
    private int maxRow = ROWS;
    private Object wrappedData;

    /**
     * @return the maxRow
     */
    public int getMaxRow() {
        return maxRow;
    }

    /**
     * @param maxRow the maxRow to set
     */
    public void setMaxRow(int maxRow) {
        this.maxRow = maxRow;
    }

    /**
     * @return the minRow
     */
    public int getMinRow() {
        return minRow;
    }

    /**
     * @param minRow the minRow to set
     */
    public void setMinRow(int minRow) {
        this.minRow = minRow;
    }

    public void walk(FacesContext context, DataVisitor visitor, Range range, Object argument) throws IOException {
        int first = 0;
        int count = ROWS;

        if (range instanceof MockRange) {
            MockRange mockRange = (MockRange) range;

            count = mockRange.getCount();
        } else if (range instanceof SequenceRange) {
            SequenceRange seqRange = (SequenceRange) range;

            first = seqRange.getFirstRow();

            int rows = seqRange.getRows();

            if (rows > 0) {
                count = rows + first;
            }
        }

        for (int row = first; row < count && row < ROWS; row++) {
            visitor.process(context, new Integer(row), argument);
        }
    }

    /*
     *  (non-Javadoc)
     * @see org.ajax4jsf.ajax.repeat.ExtendedDataModel#getRowKey()
     */
    public Object getRowKey() {

        // TODO Auto-generated method stub
        return rowIndex < 0 ? null : new Integer(rowIndex);
    }

    /*
     *  (non-Javadoc)
     * @see org.ajax4jsf.ajax.repeat.ExtendedDataModel#setRowKey(java.lang.Object)
     */
    public void setRowKey(Object key) {
        if (null == key) {
            rowIndex = -1;
        } else {
            rowIndex = ((Integer) key).intValue();
        }
    }

    /*
     *  (non-Javadoc)
     * @see javax.faces.model.DataModel#getRowCount()
     */
    public int getRowCount() {

        // TODO Auto-generated method stub
        return getMaxRow();
    }

    /*
     *  (non-Javadoc)
     * @see javax.faces.model.DataModel#getRowData()
     */
    public Object getRowData() {

        // TODO Auto-generated method stub
        return isRowAvailable() ? String.valueOf(rowIndex) : null;
    }

    /*
     *  (non-Javadoc)
     * @see javax.faces.model.DataModel#getRowIndex()
     */
    public int getRowIndex() {

        // TODO Auto-generated method stub
        return rowIndex;
    }

    /*
     *  (non-Javadoc)
     * @see javax.faces.model.DataModel#getWrappedData()
     */
    public Object getWrappedData() {

        // TODO Auto-generated method stub
        return wrappedData;
    }

    /*
     *  (non-Javadoc)
     * @see javax.faces.model.DataModel#isRowAvailable()
     */
    public boolean isRowAvailable() {

        // TODO Auto-generated method stub
        return rowIndex >= getMinRow() && rowIndex < getMaxRow();
    }

    /*
     *  (non-Javadoc)
     * @see javax.faces.model.DataModel#setRowIndex(int)
     */
    public void setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
    }

    /*
     *  (non-Javadoc)
     * @see javax.faces.model.DataModel#setWrappedData(java.lang.Object)
     */
    public void setWrappedData(Object data) {
        wrappedData = data;
    }
}

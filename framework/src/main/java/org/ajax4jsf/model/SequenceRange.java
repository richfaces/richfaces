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
package org.ajax4jsf.model;

/**
 * @author shura
 *
 */
public class SequenceRange implements Range {
    private int firstRow = 0;
    private int rows = -1;

    /**
     *
     */
    protected SequenceRange() {
        super();
    }

    /**
     * @param firstRow
     * @param rows
     */
    public SequenceRange(int firstRow, int rows) {
        super();
        this.firstRow = firstRow;
        this.rows = rows;
    }

    /**
     * @return the firstRow
     */
    public int getFirstRow() {
        return firstRow;
    }

    /**
     * @return the rows
     */
    public int getRows() {
        return rows;
    }

    protected void setFirstRow(int firstRow) {
        this.firstRow = firstRow;
    }

    protected void setRows(int rows) {
        this.rows = rows;
    }
}

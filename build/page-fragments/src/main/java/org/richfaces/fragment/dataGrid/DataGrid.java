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
package org.richfaces.fragment.dataGrid;

import java.util.List;

/**
 * Interface representing a data grid.
 *
 * <p>Each grid consists of its record. It should be page
 * fragment, which will be then returned as a properly initialized object.</p>
 *
 * <p>It is solely on the end user how the implementations of the RECORD
 * would look like. For example it can return the text of the particular grid's
 * record </p>
 *
 * <p>Note 1: Grid does not take into account <b>pagination</b>. Thus one has to
 *  switch to a different page manually in order to access all records.
 *  All indexes used by methods are relative to one page.</p>
 *
 * <p>Note 2: that one can use <tt>org.richfaces.fragment.common.NullFragment</tt>
 * as a Null Object pattern for RECORD generic type.</p>
 *
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @param <RECORD> the smallest data unit of this data grid
 */
public interface DataGrid<RECORD> {

    /**
     * Returns all RECORDs in a particular row.
     *
     * @param rowIndex zero based index of the row, from which all records will be returned
     * @return
     */
    List<RECORD> getRecordsInRow(int rowIndex);

    /**
     * Returns all records currently rendered in this grid.
     *
     * @return all records from the current page
     */
    List<RECORD> getAllVisibleRecords();

    /**
     * Return a particular record from actual page.
     *
     * @param n zero based index of the record to be returned
     * @return
     */
    RECORD getRecord(int n);

    /**
     * Returns number of the rows in this data grid.
     *
     * @return
     */
    int getNumberOfRows();

    /**
     * Returns number of the columns in this data grid.
     *
     * @return
     */
    int getNumberOfColumns();

    /**
     * Returns number of records on the current page.
     *
     * @return
     */
    int getNumberOfRecords();
}

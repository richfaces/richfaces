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
package org.richfaces.ui.iteration;

import java.io.Serializable;

/**
 * Provides access to the iteration status of a sequence.
 *
 * @author Nick Belaevski
 */
public final class SequenceIterationStatus implements Serializable {
    private static final long serialVersionUID = 1968212566967489719L;
    private Integer begin;
    private Integer count;
    private Integer end;
    private int index;
    private Integer rowCount;
    private boolean first;
    private boolean last;
    private boolean even;

    /**
     * The SequenceIterationsStatus constructor.
     *
     * @param begin the index of the first row of the current page of the table
     * @param end the index of the last row of the current page of the table
     * @param index the index of the current row
     * @param rowCount the number of rows in the model
     */
    public SequenceIterationStatus(Integer begin, Integer end, int index, Integer rowCount) {
        int iBegin = (begin != null ? begin.intValue() : 0);

        int iRowCountEnd = (rowCount != null ? rowCount.intValue() - 1 : Integer.MAX_VALUE);
        int iEnd = (end != null ? end.intValue() : iRowCountEnd);
        int iLastIdx = Math.min(iEnd, iRowCountEnd);

        this.begin = begin;
        this.end = iEnd;
        this.index = index;
        this.rowCount = rowCount;

        this.first = (index == iBegin);

        this.last = (index >= iLastIdx);

        this.count = (index - iBegin) + 1;
        this.even = ((count % 2) == 0);
    }

    /**
     * @return the index of the first row of the current page of the table
     */
    public Integer getBegin() {
        return begin;
    }

    /**
     * @return the number of rows shown on the current page
     */
    public int getCount() {
        return count;
    }

    /**
     * Currently unused
     * @return null
     */
    public Object getCurrent() {
        return null;
    }

    /**
     * @return the index of the last row of the current page of the table
     */
    public Integer getEnd() {
        return end;
    }

    /**
     * @return the index of the current row
     */
    public int getIndex() {
        return index;
    }

    /**
     * Used for complex components
     * @return 1
     */
    public Integer getStep() {
        return 1;
    }

    /**
     * @return true if the current row is the first row on the page, false otherwise
     */
    public boolean isFirst() {
        return first;
    }

    /**
     * @return true if the current row is the last row on the page, false otherwise
     */
    public boolean isLast() {
        return last;
    }

    /**
     * @return the number of rows in the model
     */
    public Integer getRowCount() {
        return rowCount;
    }

    /**
     * @return true if the current row has an even numbered index, false otherwise
     */
    public boolean isEven() {
        return even;
    }

    /**
     * @return true if the current row has an odd numbered index, false otherwise
     */
    public boolean isOdd() {
        return !isEven();
    }

    /**
     * @return a string representation of the iteration status state
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("begin= " + begin);
        sb.append(", end= " + end);
        sb.append(", index= " + index);
        sb.append(", count= " + count);
        sb.append(", first= " + first);
        sb.append(", last= " + last);
        sb.append(", even= " + even);
        sb.append(", rowCount= " + rowCount);

        return sb.toString();
    }
}

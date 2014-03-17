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
package org.richfaces.component;

import java.io.Serializable;

/**
 * @author Nick Belaevski
 *
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

    public Integer getBegin() {
        return begin;
    }

    public int getCount() {
        return count;
    }

    public Object getCurrent() {
        return null;
    }

    public Integer getEnd() {
        return end;
    }

    public int getIndex() {
        return index;
    }

    public Integer getStep() {
        return 1;
    }

    public boolean isFirst() {
        return first;
    }

    public boolean isLast() {
        return last;
    }

    public Integer getRowCount() {
        return rowCount;
    }

    public boolean isEven() {
        return even;
    }

    public boolean isOdd() {
        return !isEven();
    }

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

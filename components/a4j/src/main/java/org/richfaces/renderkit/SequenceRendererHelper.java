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
package org.richfaces.renderkit;

import org.richfaces.component.UISequence;
import org.richfaces.component.util.HtmlUtil;

/**
 * @author Nick Belaevski
 *
 */
public class SequenceRendererHelper {
    private UISequence sequence;
    private int rowIndex = -1;
    private int columnIndex = 0;
    private String[] rowClasses = null;
    private String[] columnClasses = null;

    public SequenceRendererHelper(UISequence sequence) {
        super();
        this.sequence = sequence;

        this.rowClasses = splitClassesString((String) sequence.getAttributes().get("rowClasses"));
        this.columnClasses = splitClassesString((String) sequence.getAttributes().get("columnClasses"));
    }

    private static String[] splitClassesString(String s) {
        if (s != null) {
            return s.split(",");
        }

        return null;
    }

    private static String getCorrespondingArrayItem(String[] strings, int idx) {
        if (strings != null && strings.length > 0) {
            return strings[idx % strings.length];
        }

        return null;
    }

    public UISequence getSequence() {
        return sequence;
    }

    private void initialize() {
        rowIndex = sequence.getRowIndex();
    }

    public void nextRow() {
        if (rowIndex == -1) {
            initialize();
        }

        rowIndex++;
        columnIndex = 0;
    }

    public void nextColumn() {
        columnIndex++;
    }

    public String getRowClass() {
        String rowClass = (String) sequence.getAttributes().get("rowClass");
        return HtmlUtil.concatClasses(getCorrespondingArrayItem(rowClasses, rowIndex), rowClass);
    }

    public String getColumnClass() {
        return getCorrespondingArrayItem(columnClasses, columnIndex);
    }

    public boolean hasWalkedOverRows() {
        return rowIndex != -1;
    }
}

/*
 * JBoss, Home of Professional Open Source
 * Copyright ${year}, Red Hat, Inc. and individual contributors
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

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.ajax4jsf.model.DataVisitResult;
import org.ajax4jsf.model.DataVisitor;
import org.ajax4jsf.model.SequenceRange;
import org.richfaces.component.AbstractExtendedDataTable;
import org.richfaces.component.UIDataTableBase;

/**
 * @author Konstantin Mishin
 *
 */
public abstract class SelectionRenderer extends SortingFilteringRowsRenderer {
    private class ClientSelection {
        // TODO nick - use enum instead of constant
        public static final String FLAG_RESET = "x";
        public static final String FLAG_ALL = "a";
        public static final String FLAG_AFTER_RANGE = "d";
        public static final String FLAG_BEFORE_RANGE = "u";
        // TODO nick - add special class that will express selection range
        private int[][] ranges;
        private int activeIndex;
        private int shiftIndex;
        private String selectionFlag;
        private int index;

        public ClientSelection(String selectionString) {
            // TODO nick - this code is not readable at all - lacks comments, has lot of arrays operation
            String[] strings = selectionString.split("\\|", -1);
            String[] rangeStrings = strings[0].split(";");
            if (strings[0].length() > 0) {
                ranges = new int[rangeStrings.length][2];
                for (int i = 0; i < rangeStrings.length; i++) {
                    String[] rangeString = rangeStrings[i].split(",");
                    ranges[i][0] = Integer.parseInt(rangeString[0]);
                    ranges[i][1] = Integer.parseInt(rangeString[1]);
                }
            } else {
                ranges = new int[0][0];
            }
            if (strings[1].matches("\\d+")) {
                activeIndex = Integer.parseInt(strings[1]);
            } else {
                activeIndex = -1;
            }
            if (strings[2].matches("\\d+")) {
                shiftIndex = Integer.parseInt(strings[2]);
            } else if (strings[2].length() > 0) {
                shiftIndex = -1;
            } else {
                shiftIndex = -2;
            }
            if (strings[3].length() > 0) {
                selectionFlag = strings[3];
            }
            index = 0;
        }

        public boolean isSelected(int index) {
            int i = 0;
            while (i < ranges.length && index >= ranges[i][0]) {
                if (index >= ranges[i][0] && index <= ranges[i][1]) {
                    return true;
                } else {
                    i++;
                }
            }
            return false;
        }

        public boolean isActiveIndex(int index) {
            return activeIndex == index;
        }

        public boolean isShiftIndex(int index) {
            return shiftIndex == index;
        }

        public boolean isCleanShiftIndex() {
            return shiftIndex == -2;
        }

        public String getSelectionFlag() {
            return selectionFlag;
        }

        public int nextIndex() {
            return index++;
        }
    }

    protected void encodeSelectionInput(ResponseWriter writer, FacesContext context, UIComponent component) throws IOException {
        writer.startElement(HtmlConstants.INPUT_ELEM, component);
        // TODO nick - selection input id should use constants/be a method
        writer.writeAttribute(HtmlConstants.ID_ATTRIBUTE, component.getClientId(context) + ":si", null);
        writer.writeAttribute(HtmlConstants.NAME_ATTRIBUTE, component.getClientId(context) + ":si", null);
        writer.writeAttribute(HtmlConstants.TYPE_ATTR, HtmlConstants.INPUT_TYPE_HIDDEN, null);
        UIDataTableBase table = (UIDataTableBase) component;
        StringBuilder builder = new StringBuilder("|");
        Object key = table.getRowKey();
        table.captureOrigValue(context);
        SequenceRange range = (SequenceRange) table.getComponentState().getRange();
        int first = range.getFirstRow();
        int last = first + range.getRows() - 1;
        Map<String, Object> attributes = component.getAttributes();
        table.setRowKey(attributes.get("activeRowKey"));
        int activeIndex = table.getRowIndex();
        if (activeIndex > 0) {
            if (activeIndex < first) {
                builder.append(ClientSelection.FLAG_BEFORE_RANGE);
            } else if (activeIndex > last) {
                builder.append(ClientSelection.FLAG_AFTER_RANGE);
            }
        }
        builder.append("|");
        table.setRowKey(attributes.get("shiftRowKey"));
        int shiftIndex = table.getRowIndex();
        if (shiftIndex > 0) {
            if (shiftIndex < first) {
                builder.append(ClientSelection.FLAG_BEFORE_RANGE);
            } else if (shiftIndex > last) {
                builder.append(ClientSelection.FLAG_AFTER_RANGE);
            }
        }
        builder.append("|");
        table.setRowKey(context, key);
        table.restoreOrigValue(context);
        writer.writeAttribute(HtmlConstants.VALUE_ATTRIBUTE, builder.toString(), null);
        writer.endElement(HtmlConstants.INPUT_ELEM);
    }

    @Override
    protected void doDecode(FacesContext context, UIComponent component) {
        super.doDecode(context, component);
        Map<String, String> map = context.getExternalContext().getRequestParameterMap();
        String selectionString = map.get(component.getClientId(context) + ":si");
        if (selectionString != null && selectionString.length() > 0) {
            final ClientSelection clientSelection = new ClientSelection(selectionString);
            final Map<String, Object> attributes = component.getAttributes();
            AbstractExtendedDataTable table = (AbstractExtendedDataTable) component;
            Collection<Object> selection = table.getSelection();
            if (selection == null) {
                selection = new HashSet<Object>();
                // TODO nick - model updates should not happen on the 2nd phase
                updateAttribute(context, component, "selection", selection);
            }
            final Collection<Object> rowKeys = selection;
            String selectionFlag = clientSelection.getSelectionFlag();
            if (selectionFlag != null) {
                selection.clear();
                if (!ClientSelection.FLAG_RESET.equals(selectionFlag)) {
                    encodeSelectionOutsideCurrentRange(context, table, selectionFlag);
                }
            }
            if (clientSelection.isCleanShiftIndex()) {
                attributes.remove("shiftRowKey");
            }
            table.walk(context, new DataVisitor() {
                public DataVisitResult process(FacesContext context, Object rowKey, Object argument) {
                    int index = clientSelection.nextIndex();
                    if (clientSelection.isSelected(index)) {
                        rowKeys.add(rowKey);
                    } else {
                        rowKeys.remove(rowKey);
                    }
                    if (clientSelection.isActiveIndex(index)) {
                        attributes.put("activeRowKey", rowKey);
                    }
                    if (clientSelection.isShiftIndex(index)) {
                        attributes.put("shiftRowKey", rowKey);
                    }
                    return DataVisitResult.CONTINUE;
                }
            }, null);
        }
    }

    private void encodeSelectionOutsideCurrentRange(FacesContext context, AbstractExtendedDataTable table, String selectionFlag) { // TODO
                                                                                                                                   // Rename
                                                                                                                                   // method
        Object key = table.getRowKey();
        table.captureOrigValue(context);
        SequenceRange range = (SequenceRange) table.getComponentState().getRange();
        SequenceRange newRange = null;
        Map<String, Object> attributes = table.getAttributes();
        Object rowKey = attributes.get("shiftRowKey");
        if (rowKey == null) {
            rowKey = attributes.get("activeRowKey");
            if (rowKey == null) {
                rowKey = range.getFirstRow();
            }
            attributes.put("shiftRowKey", rowKey);
        }
        table.setRowKey(rowKey);
        int shiftIndex = table.getRowIndex();
        if (ClientSelection.FLAG_ALL.equals(selectionFlag)) {
            newRange = new SequenceRange(0, 0);
        } else if (shiftIndex > 0) {
            if (ClientSelection.FLAG_BEFORE_RANGE.equals(selectionFlag)) {
                newRange = new SequenceRange(shiftIndex, range.getFirstRow() - shiftIndex);
            } else {
                int last = range.getFirstRow() + range.getRows();
                newRange = new SequenceRange(last, shiftIndex - last + 1);
            }
        }
        table.setRowKey(context, key);
        table.restoreOrigValue(context);
        if (newRange != null) {
            final Collection<Object> rowKeys = table.getSelection();
            table.walk(context, new DataVisitor() {
                public DataVisitResult process(FacesContext context, Object rowKey, Object argument) {
                    rowKeys.add(rowKey);
                    return DataVisitResult.CONTINUE;
                }
            }, newRange, null);
        }
    }
}

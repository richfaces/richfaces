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
import java.util.Iterator;
import java.util.Map;

import javax.faces.component.UIColumn;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.ajax4jsf.renderkit.RendererUtils.HTML;
import org.richfaces.component.Row;


/**
 * @author Anton Belevich
 *
 */
public abstract class AbstractTableBaseRenderer extends SortingFilteringRowsRenderer {

    public static final String ROW_CLASS_KEY = "rowClass";
    
    public static final String FIRST_ROW_CLASS_KEY = "firstRowClass";
    
    public static final String CELL_CLASS_KEY = "cellClass";
    
    public static final String CELL_ELEMENT_KEY = "cellElement";
    
    public static final String BREAK_ROW_BEFORE = "breakRowBefore";
    
    public void encodeColumn(FacesContext context, ResponseWriter writer, UIColumn component, RowHolder rowHolder) throws IOException {
        String parentId = rowHolder.getParentClientId();
        
        if (component instanceof org.richfaces.component.AbstractColumn) {
            Map<String, Object> attributes = component.getAttributes();
            if (Boolean.TRUE.equals(attributes.get(BREAK_ROW_BEFORE)) && rowHolder.getProcessCell() != 0) {
                encodeRowEnd(writer);
                rowHolder.nextRow();
                rowHolder.setRowStart(true);
            }
        }

        if (rowHolder.isRowStart()) {
            int currentRow = rowHolder.getCurrentRow();

            if (rowHolder.getCurrentRow() == 0) {
                encodeFirstRowStart(writer, context, parentId, currentRow, component);
            } else {
                encodeRowStart(writer, context, parentId, currentRow, component);
            }
            
            rowHolder.setRowStart(false);
        }

        encodeColumnStart(writer, context, parentId, component);
        renderChildren(context, component);
        encodeColumnEnd(writer, context, parentId);
               
        rowHolder.nextCell();
    }
    
    public void encodeColumnStart(ResponseWriter writer, FacesContext context, String parentId, UIComponent component) throws  IOException {
        String element = getCellElement(context, parentId);
        writer.startElement(element, component);
        getUtils().encodeId(context, component);
        String cellClass = getCellClass(context, parentId);
        encodeStyleClass(writer, context, component, HTML.STYLE_CLASS_ATTR, cellClass);

        if (component instanceof org.richfaces.component.AbstractColumn) {
            Map<String, Object> attributes = component.getAttributes();

            Integer rowspan = (Integer) attributes.get(HTML.ROWSPAN_ATTRIBUTE);
            if (rowspan != null && rowspan != Integer.MIN_VALUE) {
                writer.writeAttribute(HTML.ROWSPAN_ATTRIBUTE, rowspan, null);
            }

            Integer colspan = (Integer) attributes.get(HTML.COLSPAN_ATTRIBUTE);
            if (colspan != null && colspan != Integer.MIN_VALUE) {
                writer.writeAttribute(HTML.COLSPAN_ATTRIBUTE, colspan, null);
            }
        }
    }
    
    public void encodeColumnEnd(ResponseWriter writer, FacesContext context, String parentId) throws  IOException {
        writer.endElement(getCellElement(context, parentId));
    }
    
    public void encodeFirstRowStart(ResponseWriter writer, FacesContext context, String parentId, int currentRow, UIComponent component) throws  IOException {
        writer.startElement(HTML.TR_ELEMENT, component);
        String styleClass = getFirstRowClass(context, parentId);
        encodeStyleClass(writer, context, component, HTML.STYLE_CLASS_ATTR, styleClass);
    }
    
    public void encodeFirstRowEnd(ResponseWriter writer)throws  IOException {
        writer.endElement(HTML.TR_ELEMENT);
    }
    
    public void encodeRowStart(ResponseWriter writer, FacesContext context, String parentId, int currentRow, UIComponent component) throws  IOException {
        writer.startElement(HTML.TR_ELEMENT, component);
        String styleClass = getRowClass(context, parentId);
        encodeStyleClass(writer, context, component, HTML.STYLE_CLASS_ATTR, styleClass);   
    }
    
    public void encodeRowEnd(ResponseWriter writer) throws  IOException {
        writer.endElement(HTML.TR_ELEMENT);
    }
  
    protected String getFirstRowClass(FacesContext context, String id) {
        return get(context, id + FIRST_ROW_CLASS_KEY);
    }
    
    protected String getRowClass(FacesContext context, String id) {
        return get(context, id + ROW_CLASS_KEY);
    }
    
    protected String getCellClass(FacesContext context, String id) {
        return get(context, id + CELL_CLASS_KEY);
    }
    
    protected String getCellElement(FacesContext context, String id) {
        return get(context, id + CELL_ELEMENT_KEY);
    }
    
    protected void saveRowStyles(FacesContext context, String id, String firstRowClass, String rowClass, String cellClass) {
        put(context, id + FIRST_ROW_CLASS_KEY, firstRowClass);
        put(context, id + ROW_CLASS_KEY, rowClass);
        put(context, id + CELL_CLASS_KEY, cellClass);
    }
    
    public void encodeStyleClass(ResponseWriter writer, FacesContext context, UIComponent component,
        String styleClassAttribute, String styleClass) throws IOException {

        boolean isEmpty = isEmptyAttr(component, styleClassAttribute);
        if (isEmpty && !(styleClass != null && styleClass.trim().length() != 0)) {
            return;
        }

        String componentStyleClass = isEmpty ? styleClass : styleClass + " "
            + component.getAttributes().get(styleClassAttribute);
        writer.writeAttribute(HTML.CLASS_ATTRIBUTE, componentStyleClass, null);
    }
    
    protected boolean isEmptyAttr(UIComponent component, String attribute) {
        if (attribute == null) {
            return true;
        }
        
        String value = (String) component.getAttributes().get(attribute);
        return !(value != null && value.trim().length() != 0);
    }

    protected void encodeStyle(ResponseWriter writer, FacesContext context, UIComponent component,
        String predefinedStyles) throws IOException {
        
        StringBuffer toEncode = new StringBuffer();

        if (!isEmptyAttr(component, HTML.STYLE_ATTRIBUTE)) {
            String style = ((String) component.getAttributes().get(HTML.STYLE_ATTRIBUTE)).trim();
            style = style.endsWith(";") ? style : style + ";";
            toEncode.append(style);
        }

        if (!isEmpty(predefinedStyles)) {
            String style = predefinedStyles.endsWith(";") ? predefinedStyles : predefinedStyles + ";";
            toEncode.append(style);
        }

        if (toEncode.length() > 0) {
            writer.writeAttribute(HTML.STYLE_ATTRIBUTE, toEncode.toString(), null);
        }
    }
    
    protected boolean isEmpty(String style) {
        return !((style != null) && (style.trim().length() != 0));
    }

    
    protected int getColumnsCount(Iterator<UIComponent> col) {
        int count = 0;
        int currentLength = 0;
        while (col.hasNext()) {
            UIComponent component = (UIComponent) col.next();
            if (component.isRendered()) {
                if (component instanceof Row) {
                    // Store max calculated value of previsous rows.
                    if (currentLength > count) {
                        count = currentLength;
                    }
                    // Calculate number of columns in row.
                    currentLength = getColumnsCount(((Row) component).columns());
                    // Store max calculated value
                    if (currentLength > count) {
                        count = currentLength;
                    }
                    currentLength = 0;
                } else if (component instanceof org.richfaces.component.AbstractColumn) {
                    // For new row, save length of previsous.
                    Map<String, Object> attributes = component.getAttributes();
                    if (Boolean.TRUE.equals(attributes.get(BREAK_ROW_BEFORE))) {
                        if (currentLength > count) {
                            count = currentLength;
                        }
                        currentLength = 0;
                    }
                    Integer colspan = (Integer) attributes.get(HTML.COLSPAN_ATTRIBUTE);
                    // Append colspan of this column
                    if (null != colspan && colspan.intValue() != Integer.MIN_VALUE) {
                        currentLength += colspan.intValue();
                    } else {
                        currentLength++;
                    }
                } else if (component instanceof UIColumn) {
                    // UIColumn always have colspan == 1.
                    currentLength++;
                }
            }
        }

        if (currentLength > count) {
            count = currentLength;
        }
        return count;
    }
    
    @Override
    public void encodeFakeRow(FacesContext facesContext, RowHolderBase rowHolder) throws IOException {
        UIComponent component = (UIComponent)rowHolder.getRow();
        ResponseWriter writer = facesContext.getResponseWriter();
        writer.startElement(HTML.TR_ELEMENT, component);
        writer.writeAttribute(HTML.STYLE_ATTRIBUTE, "display:none", null);
        writer.startElement(HTML.TD_ELEM, component);
        writer.endElement(HTML.TD_ELEM);
        writer.endElement(HTML.TR_ELEMENT);
    }
}

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
package org.richfaces.component;

import org.richfaces.json.JSONArray;
import org.richfaces.json.JSONCollection;
import org.richfaces.json.JSONException;
import org.richfaces.json.JSONMap;
import org.richfaces.json.JSONObject;
import org.richfaces.json.JSONStringer;
import org.richfaces.json.JSONWriter;

import javax.faces.component.UIComponent;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;

/**
 * @author <a href="http://community.jboss.org/people/bleathem">Brian Leathem</a>
 */
public class ExtendedDataTableState implements Serializable {

    private static final long serialVersionUID = 1L;

    protected ColumnsWidth columnsWidthState;
    protected ColumnsOrder columnsOrderState;
    protected ColumnsFilter columnsFilterState;
    protected ColumnsSort columnsSortState;

    public ExtendedDataTableState(UIDataTableBase extendedDataTable) {
        columnsWidthState = new ColumnsWidth(extendedDataTable);
        columnsOrderState = new ColumnsOrder(extendedDataTable);
        columnsFilterState = new ColumnsFilter(extendedDataTable);
        columnsSortState = new ColumnsSort(extendedDataTable);
    }

    public ExtendedDataTableState(String tableState) {
        JSONMap json = null;
        if ((tableState != null) && (tableState.length() > 0)) {
            try {
                json = new JSONMap(tableState);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (json != null) {
            columnsWidthState = new ColumnsWidth((JSONMap) json.get("columnsWidthState"));
            columnsOrderState = new ColumnsOrder((JSONCollection) json.get("columnsOrderState"));
            columnsFilterState = new ColumnsFilter((JSONMap) json.get("columnsFilterState"));
            columnsSortState = new ColumnsSort((JSONMap) json.get("columnsSortState"));
        }
    }

    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        try {
            if (columnsWidthState != null && !columnsWidthState.isEmpty()) {
                json.put("columnsWidthState", columnsWidthState.toJSON());
            }
            if (columnsOrderState != null && !columnsOrderState.isEmpty()) {
                json.put("columnsOrderState", columnsOrderState.toJSON());
            }
            if (columnsFilterState != null && !columnsFilterState.isEmpty()) {
                json.put("columnsFilterState", columnsFilterState.toJSON());
            }
            if (columnsSortState != null && !columnsSortState.isEmpty()) {
                json.put("columnsSortState", columnsSortState.toJSON());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    /**
     * Converts its state to String representation in JSON format.
     */
    public String toString() {
        return toJSON().toString();
    }

    /*
     * @see ColumnsWidth#getColumnState(UIComponent)
     */
    public String getColumnWidth(UIComponent column) {
        return columnsWidthState.getColumnState(column);
    }

    /*
     * @see ColumnsFilter#getColumnState(UIComponent)
     */
    public String getColumnFilter(UIComponent column) {
        return columnsFilterState.getColumnState(column);
    }

    /*
     * @see ColumnsSort#getColumnState(UIComponent)
     */
    public String getColumnSort(UIComponent column) {
        return columnsSortState.getColumnState(column);
    }

    /*
     * @see ColumnsOrder#getColumnsOrder(UIComponent)
     */
    public String[] getColumnsOrder() {
        return columnsOrderState.getColumnsOrder();
    }

}

abstract class ColumnsState implements Serializable {

    private static final long serialVersionUID = 1L;

    protected JSONObject json;

    /**
     * Return the value to which the column will map to in the resulting JSONMap
     */
    abstract String getValueFromColumn(AbstractColumn column);

    /**
     * Initialize state from an extendedDataTable
     */
    ColumnsState (UIDataTableBase extendedDataTable) {
        try {
            JSONWriter writer = new JSONStringer().object();
            Iterator<UIComponent> iterator = extendedDataTable.columns();
            while (iterator.hasNext()) { // initialize a map of all the columns
                UIComponent component = iterator.next();
                AbstractColumn column = (AbstractColumn) component;
                writer.key(column.getId()).value(getValueFromColumn(column));
            }
            json = new JSONObject(writer.endObject().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Initialize state from JSON
     */
    ColumnsState (JSONMap json) {
        if ((json != null) && (json.size() > 0)) {
            this.json = new JSONObject(json);
        }
    }

    /**
     * Return the value mapped to by the given column, as stored in the json map.
     *
     * @param column
     * @return the width of the column.  Returns null if not defined in the json map
     */
    public String getColumnState(UIComponent column) {
        String state = null;
        if (json != null && !json.isNull(column.getId())) {
            state = (String) json.opt(column.getId());
        }
        return state;
    }

    public boolean isEmpty() {
        return json == null || json.length() == 0;
    }

    /**
     * Get state in JSON format.
     *
     * @return JSON object contains state
     */
    public JSONMap toJSON() {
        return new JSONMap(json);
    }

    /**
     * Converts its state to String representation in JSON format.
     */
    public String toString() {
        if (json == null) {
            return "";
        }
        return json.toString();
    }

}

class ColumnsWidth extends ColumnsState implements Serializable {

    private static final long serialVersionUID = 1L;

    ColumnsWidth(UIDataTableBase extendedDataTable) {
        super(extendedDataTable);
    }

    ColumnsWidth(JSONMap json) {
        super(json);
    }

    String getValueFromColumn(AbstractColumn column) {
        return column.getWidth();
    }
}

class ColumnsFilter extends ColumnsState implements Serializable {

    private static final long serialVersionUID = 1L;

    ColumnsFilter(UIDataTableBase extendedDataTable) {
        super(extendedDataTable);
    }

    ColumnsFilter(JSONMap json) {
        super(json);
    }

    String getValueFromColumn(AbstractColumn column) {
        Object filterValue = column.getFilterValue();
        return filterValue == null ? null : filterValue.toString();
    }
}

class ColumnsSort extends ColumnsState implements Serializable {

    private static final long serialVersionUID = 1L;

    ColumnsSort(UIDataTableBase extendedDataTable) {
        super(extendedDataTable);
    }

    ColumnsSort(JSONMap json) {
        super(json);
    }

    String getValueFromColumn(AbstractColumn column) {
        Object sortOrder = column.getSortOrder();
        return sortOrder == null ? null : sortOrder.toString();
    }
}

class ColumnsOrder implements Serializable {

    private static final long serialVersionUID = 1;

    private JSONArray json;

    /**
     * Initialize state from an extendedDataTable
     */
    ColumnsOrder (UIDataTableBase extendedDataTable) {
        String[] columnsOrder = (String[]) extendedDataTable.getAttributes().get("columnsOrder");
        if (columnsOrder != null) {
            json = new JSONArray(Arrays.asList(columnsOrder));
        }
    }

    /**
     * Initialize state from JSON
     */
    ColumnsOrder (JSONCollection json) {
        if ((json != null) && (json.size() > 0)) {
            this.json = new JSONArray(json);
        }
    }

    /**
     * Return the order of the columns.  The column order is determined according to the precedence:
     * 1) the column order stored in the table state if it is not null
     * 2) the columnsOrder attribute of the table if that is not null
     * 3) the order of the columns in the component tree
     *
     * @return a String array representing the order of the columns by id.
     */
    public String[] getColumnsOrder() {
        if (json == null) {
            return null;
        }
        String[] columnsOrder = new String[json.length()];
        for (int i = 0; i < json.length(); i++) {
            String columnId = (String) json.opt(i);
            columnsOrder[i] = columnId;
        }
        return columnsOrder;
    }

    public boolean isEmpty() {
        return json == null || json.length() == 0;
    }

    /**
     * Get state in JSON format.
     *
     * @return JSON object contains state
     */
    public JSONArray toJSON() {
        return json;
    }

    /**
     * Converts its state to String representation in JSON format.
     */
    public String toString() {
        if (json == null) {
            return "";
        }
        return json.toString();
    }
}

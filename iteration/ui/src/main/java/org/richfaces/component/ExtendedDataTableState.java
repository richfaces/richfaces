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

import javax.faces.component.UIColumn;
import javax.faces.component.UIComponent;
import java.io.Serializable;
import java.util.Arrays;

/**
 * @author <a href="http://community.jboss.org/people/bleathem">Brian Leathem</a>
 */
public class ExtendedDataTableState implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String NONE_COLUMN_ID = "none";

    protected ColumnsWidth columnsWidthState;
    protected ColumnsOrder columnsOrderState;

    public static ExtendedDataTableState getExtendedDataTableState(UIDataTableBase extendedDataTable) {
        ExtendedDataTableState state = new ExtendedDataTableState();
        state.init(extendedDataTable);
        return state;
    }

    /**
     * Initialize with the state of the associated ExtendedDataTable
     */
    protected void init(UIDataTableBase extendedDataTable) {
        String tableState = (String) extendedDataTable.getAttributes().get("tableState");
        JSONMap stateMap = null;
        if ((tableState != null) && (tableState.length() > 0)) {
            try {
                stateMap = new JSONMap(tableState);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        //initialize columns width
        columnsWidthState = ColumnsWidth.createColumnsWidthState(extendedDataTable, (stateMap == null ? null : (JSONMap) stateMap.get("columnsWidthState")));
        columnsOrderState = ColumnsOrder.createColumnsOrderState(extendedDataTable, (stateMap == null ? null : (JSONCollection) stateMap.get("columnsOrderState")));
    }

    /**
     * Converts its state to String representation in JSON format.
     */
    public String toString() {
        return toJSON().toString();
    }

    public JSONObject toJSON() {
        JSONObject result = new JSONObject();
        try {
            result.put("columnsWidthState", columnsWidthState.toJSON());
            result.put("columnsOrderState", columnsOrderState.toJSON());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    /*
     * @see ColumnsWidth#getColumnWidth(UIComponent)
     */
    public String getColumnWidth(UIComponent column) {
        return columnsWidthState.getColumnWidth(column);
    }

    /*
     * @see ColumnsOrder#getColumnsOrder(UIComponent)
     */
    public String[] getColumnsOrder() {
        return columnsOrderState.getColumnsOrder();
    }

}

class ColumnsWidth implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final String DEFAULT_WIDTH = "100px";

    private JSONObject json;

    private ColumnsWidth() {
        super();
    }

    static ColumnsWidth createColumnsWidthState(UIDataTableBase extendedDataTable, JSONMap state) {
        ColumnsWidth columnsWidth = new ColumnsWidth();
        try {
            columnsWidth.init(extendedDataTable, state);
        } catch ( Exception e) {
            columnsWidth = new ColumnsWidth();
            columnsWidth.init(extendedDataTable, null);
        }
        return columnsWidth;
    }

    /**
     * Return the width of the given column.  The return value of the width is determined according to the precedence:
     * 1) the column width stored in the table state if it is not null
     * 2) the width attribute of the column if that is not null
     * 3) a default json of DEFAULT_WIDTH
     *
     * @param column
     * @return the width of the column.  Returns a default json of DEFAULT_WIDTH if no width is set.
     */
    public String getColumnWidth(UIComponent column) {
        String width = null;
        if (json != null) {
            width = (String) json.opt(column.getId());
        }
        if (width == null) {
            width = (String) column.getAttributes().get("width");
        }
        if (width == null || width.length() == 0 || width.indexOf("%") != -1) {
            width = DEFAULT_WIDTH;
        }
        return width;
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

    /**
     * Converts its state from String representation or create default state if it is not set.
     */
    private void init(UIDataTableBase extendedDataTable, JSONMap state) {
        json = null;
        if ((state != null) && (state.size()>0)) {
            json = new JSONObject(state);
        }

        if (json == null) {
            try {
                JSONWriter writer = new JSONStringer().object();
                for (UIComponent child : extendedDataTable.getChildren()) {
                    UIColumn col = (UIColumn) child;
                    writer.key(col.getId()).value(getColumnWidth(col));
                }
                json = new JSONObject(writer.endObject().toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}

class ColumnsOrder implements Serializable {

    private static final long serialVersionUID = 1;

    private JSONArray json;

    private ColumnsOrder() {
        super();
    }

    static ColumnsOrder createColumnsOrderState(UIDataTableBase extendedDataTable, JSONCollection collection) {
        ColumnsOrder columnsOrder = new ColumnsOrder();
        columnsOrder.init(extendedDataTable, collection);
        return columnsOrder;
    }

    /**
     * Converts the state from String representation or create default state if it is not set.
     */
    private void init(UIDataTableBase extendedDataTable, JSONCollection collection) {
        json = null;
        if ((collection != null) && (collection.size() > 0)) {
            //try to restore state from collection
            json = new JSONArray(collection);
        }
        if (json == null) {
            String[] columnsOrder = (String[]) extendedDataTable.getAttributes().get("columnsOrder");
            if (columnsOrder != null) {
                json = new JSONArray(Arrays.asList(columnsOrder));
            }
        }
        if (json == null) {
            json = new JSONArray();
            for (UIComponent child : extendedDataTable.getChildren()) {
                UIColumn col = (UIColumn) child;
                json.put(col.getId());
            }
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
        String[] columnsOrder = new String[json.length()];
        for (int i = 0; i < json.length(); i++) {
            String columnId = (String) json.opt(i);
            columnsOrder[i] = columnId;
        }
        return columnsOrder;
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

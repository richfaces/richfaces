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

import org.richfaces.json.JSONException;
import org.richfaces.json.JSONMap;
import org.richfaces.json.JSONObject;
import org.richfaces.json.JSONStringer;
import org.richfaces.json.JSONWriter;

import javax.faces.component.UIColumn;
import javax.faces.component.UIComponent;
import java.io.Serializable;

/**
 * @author <a href="http://community.jboss.org/people/bleathem">Brian Leathem</a>
 *
 */
public class ExtendedDataTableState implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String NONE_COLUMN_ID = "none";

    protected ColumnsWidthState columnsWidthState;

    public static ExtendedDataTableState getExtendedDataTableState(AbstractExtendedDataTable extendedDataTable){
        ExtendedDataTableState state = new ExtendedDataTableState();
        state.init(extendedDataTable);
        return state;
    }

    /**
     * Initialize with the state of the associated ExtendedDataTable
     */
    protected void init(AbstractExtendedDataTable extendedDataTable){
        String tableState = extendedDataTable.getTableState();
        JSONMap stateMap = null;
        if ((tableState != null) && (tableState.length() > 0)){
            try {
                stateMap = new JSONMap(tableState);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        //initialize columns width
        try{
            columnsWidthState = ColumnsWidthState.createColumnsWidthState(extendedDataTable, (stateMap == null ? null : (JSONMap) stateMap.get("columnsWidthState")));
        }
        catch(Exception e){
            columnsWidthState = ColumnsWidthState.createColumnsWidthState(extendedDataTable, (JSONMap) null);
        }
    }

    /**
     * Converts its state to String representation in JSON format.
     */
    public String toString(){
        return toJSON().toString();
    }

    public JSONObject toJSON(){
        JSONObject result = new JSONObject();
        try {
            result.put("columnsWidthState", columnsWidthState.toJSON());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    /*
     * @see ColumnsWidthState#getColumnWidth(UIComponent)
     */
    public String getColumnWidth(UIComponent column) {
        return columnsWidthState.getColumnWidth(column);
    }
}

class ColumnsWidthState implements Serializable{

    private static final long serialVersionUID = 1L;

    private static final String DEFAULT_WIDTH = "100px";

    private JSONObject json;

    private ColumnsWidthState() {
        super();
    }

    static ColumnsWidthState createColumnsWidthState(AbstractExtendedDataTable extendedDataTable, JSONMap state){
        ColumnsWidthState columnsWidth = new ColumnsWidthState();
        columnsWidth.init(extendedDataTable, state);
        return columnsWidth;
    }

    /**
     * Return the width of the given column.  The return value of the width is determined according to the precedence:
     * 1) the column width stored in the table state if it is not null
     * 2) the width attribute of the column if that is not null
     * 3) a default value of DEFAULT_WIDTH
     *
     * @param column
     * @return the width of the column.  Returns a default value of DEFAULT_WIDTH if no width is set.
     */
    public String getColumnWidth(UIComponent column){
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
     * @return JSON object contains state
     */
    public JSONMap toJSON(){
        return new JSONMap(json);
    }

    /**
     * Converts its state to String representation in JSON format.
     */
    public String toString(){
        if (json == null){
            return "";
        }
        return json.toString();
    }

    /**
     * Converts its state from String representation or create default state if it is not set.
     */
    private void init(AbstractExtendedDataTable extendedDataTable, JSONMap state){
        json = null;
        if ((state != null) && (state.size()>0)){
            json = new JSONObject(state);
        }

        if (json == null){
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
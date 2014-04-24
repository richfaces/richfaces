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
package org.richfaces.model;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.richfaces.json.JSONArray;
import org.richfaces.json.JSONObject;
import org.richfaces.renderkit.ChartRendererBase;


/**
 *
 * @author Lukas Macko
 */
public abstract class ChartDataModel<T, S> {

    private Map<T, S> data;

    protected ChartType type;

    protected ChartStrategy strategy;

    protected List<T> keys;

    private Map<String, Object> attributes;

    public ChartDataModel(ChartType type) {
        data = new HashMap<T, S>();
        keys = new LinkedList<T>();
        this.type = type;
    }

    public void setData(Map<T, S> data) {
        this.data = data;
    }

    public Map<T, S> getData() {
        return data;
    }

    public void put(T key, S value) {
        data.put(key, value);
        keys.add(key);
    }

    public void remove(T key) {
        data.remove(key);
        keys.remove(key);
    }

    public JSONObject defaultExport() throws IOException {
        JSONObject output = new JSONObject();
        JSONArray jsdata;

        // data
        jsdata = new JSONArray();

        for (T key : keys) {
            JSONArray point = new JSONArray();
            S value = (S) data.get(key);
            point.put(key);
            point.put(value);
            jsdata.put(point);
        }

        ChartRendererBase.addAttribute(output, "data", jsdata);
        // label
        ChartRendererBase.addAttribute(output, "label",
                getAttributes().get("label"));
        // color
        ChartRendererBase.addAttribute(output, "color",
                getAttributes().get("color"));

        return output;
    }

    public Object export() throws IOException {
        return strategy.export(this);
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public ChartType getType() {
        return type;
    }

    public abstract Class getKeyType();

    public abstract Class getValueType();

    public enum ChartType {
        line, bar, pie
    }

}

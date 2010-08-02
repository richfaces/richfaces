/**
 * License Agreement.
 *
 *  JBoss RichFaces - Ajax4jsf Component Library
 *
 * Copyright (C) 2007  Exadel, Inc.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License version 2.1 as published by the Free Software Foundation.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA
 */

package org.richfaces.renderkit;

import org.ajax4jsf.javascript.JSFunctionDefinition;
import org.ajax4jsf.javascript.ScriptStringBase;
import org.ajax4jsf.javascript.ScriptUtils;

import javax.faces.component.UIComponent;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author Maksim Kaszynski
 */
public class ScriptOptions extends ScriptStringBase {
    protected Map<String, Object> opts = new HashMap<String, Object>();
    private UIComponent component;

    public ScriptOptions(UIComponent component) {
        this.component = component;
    }

    public void appendScript(StringBuffer functionString) {
        functionString.append(ScriptUtils.toScript(opts));
    }

    public void addOption(String name) {
        if (name != null) {
            Object value = component.getAttributes().get(name);

            addOption(name, value);
        }
    }

    @SuppressWarnings("unchecked")
    public void addOption(String name, Object value) {
        if (value != null) {
            Object object = opts.get(name);

            if ((object instanceof Map) && (value instanceof Map)) {
                ((Map) object).putAll((Map) value);
            } else {
                opts.put(name, value);
            }
        }
    }

    public void addEventHandler(String event, String handler) {
        if ((event != null) && (handler != null)) {
            JSFunctionDefinition functionDefinition = new JSFunctionDefinition("event");

            functionDefinition.addToBody(handler);
            functionDefinition.addToBody(";return true;");
            addEventHandler(event, functionDefinition);
        }
    }

    public void addEventHandler(String event) {
        if (event != null) {
            String handler = (String) component.getAttributes().get(event);

            addEventHandler(event, handler);
        }
    }

    public void addEventHandler(String event, JSFunctionDefinition definition) {
        if ((event != null) && (definition != null)) {
            opts.put(event, definition);
        }
    }

    public Map<String, Object> getMap() {
        return opts;
    }

    public void merge(ScriptOptions anotherOptions) {
        Iterator<Entry<String, Object>> entrySetIterator = anotherOptions.opts.entrySet().iterator();

        while (entrySetIterator.hasNext()) {
            Entry<String, Object> entry = entrySetIterator.next();

            addOption(entry.getKey(), entry.getValue());
        }
    }
}

/**
 * License Agreement.
 *
 * Rich Faces - Natural Ajax for Java Server Faces (JSF)
 *
 * Copyright (C) 2007 Exadel, Inc.
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

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.ajax4jsf.javascript.ScriptStringBase;
import org.ajax4jsf.javascript.ScriptUtils;

/**
 * @author Nick Belaevski
 * @since 4.0
 */
public class AjaxOptions extends ScriptStringBase {
    public static final String PARAMETERS = "parameters";
    public static final String CLIENT_PARAMETERS = "clientParameters";
    private Map<String, Object> options = new HashMap<String, Object>();
    private Object beforesubmitHandler;

    public void appendScript(Appendable target) throws IOException {
        ScriptUtils.appendScript(target, options);
    }

    public boolean isEmpty() {
        return options.isEmpty();
    }

    public Object get(String optionName) {
        return options.get(optionName);
    }

    public void set(String optionName, Object optionValue) {
        options.put(optionName, optionValue);
    }

    public void remove(String optionName) {
        options.remove(optionName);
    }

    public boolean hasParameters() {
        @SuppressWarnings("unchecked")
        Map<String, Object> parameters = (Map<String, Object>) options.get(PARAMETERS);

        return (parameters != null) && !parameters.isEmpty();
    }

    // TODO: optimize rendered data
    public Map<String, Object> getParameters() {
        @SuppressWarnings("unchecked")
        Map<String, Object> parameters = (Map<String, Object>) options.get(PARAMETERS);

        if (parameters == null) {
            parameters = new LinkedHashMap<String, Object>();
            options.put(PARAMETERS, parameters);
        }

        return parameters;
    }

    public Object getParameter(String parameterName) {
        Object result = null;
        @SuppressWarnings("unchecked")
        Map<String, Object> parameters = (Map<String, Object>) options.get(PARAMETERS);

        if (parameters != null) {
            result = parameters.get(parameterName);
        }

        return result;
    }

    public void setParameter(String parameterName, Object parameterValue) {
        getParameters().put(parameterName, parameterValue);
    }

    public void addParameters(Map<String, Object> params) {
        if (params == null || params.isEmpty()) {
            return;
        }

        getParameters().putAll(params);
    }

    public void removeParameter(String parameterName) {
        @SuppressWarnings("unchecked")
        Map<String, Object> parameters = (Map<String, Object>) options.get(PARAMETERS);

        if (parameters != null) {
            parameters.remove(parameterName);
        }
    }

    public Object getClientParameters() {
        return options.get(CLIENT_PARAMETERS);
    }

    public void setClientParameters(Object value) {
        options.put(CLIENT_PARAMETERS, value);
    }

    public Object getAjaxComponent() {
        return getParameter(AjaxConstants.AJAX_COMPONENT_ID_PARAMETER);
    }

    public void setAjaxComponent(Object ajaxComponent) {
        getParameters().put(AjaxConstants.AJAX_COMPONENT_ID_PARAMETER, ajaxComponent);
    }

    public Object getBeforesubmitHandler() {
        return beforesubmitHandler;
    }

    public void setBeforesubmitHandler(Object beforesubmitHandler) {
        this.beforesubmitHandler = beforesubmitHandler;
    }
}

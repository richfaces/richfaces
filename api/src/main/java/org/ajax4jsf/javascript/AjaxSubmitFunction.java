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



package org.ajax4jsf.javascript;

import java.util.HashMap;
import java.util.Map;

import javax.faces.component.UIComponent;

/**
 * @author shura
 *
 */
public class AjaxSubmitFunction extends JSFunction {

    /**
     * Name Javasript function for submit AJAX request
     */
    public static final String AJAX_FUNCTION_NAME = "A4J.AJAX.Submit";

    // private static Log log = LogFactory.getLog(AjaxSubmitFunction.class);
    public static final String AJAX_REGIONS_ATTRIBUTE = "reRender";

    /**
     * Attribute to keep
     */
    public static final String LIMITRENDER_ATTR_NAME = "limitRender";

    /**
     * Attribute for keep JavaScript funtion name for call after complete
     * request.
     */
    public static final String ONCOMPLETE_ATTR_NAME = "oncomplete";

    /**
     * Attribute for keep clientId of status component
     */
    public static final String STATUS_ATTR_NAME = "status";
    private Map<String, Object> options = new HashMap<String, Object>();
    private Map<String, String> requestParameters = new HashMap<String, String>();

    // private static final Class<?> OBJECT_ARRAY_CLASS = (new Object[0]).getClass();
    private UIComponent component;

    /**
     *
     */
    public AjaxSubmitFunction(UIComponent component) {
        this(component, AJAX_FUNCTION_NAME);
    }

    /**
     * @param name
     */
    public AjaxSubmitFunction(UIComponent component, String name) {
        super(name);
        this.component = component;

        // Fill parameters and options values.
    }

    /**
     * @return the options
     */
    public Map<String, Object> getOptions() {
        return this.options;
    }

    /**
     * @return the requestParameters
     */
    public Map<String, String> getRequestParameters() {
        return this.requestParameters;
    }

    public UIComponent getComponent() {
        return component;
    }
}

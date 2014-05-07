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

package org.richfaces.log;

import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;

/**
 * @author Anton Belevich
 *
 */
public enum RichfacesLogger {
    RESOURCE("Resource"),
    RENDERKIT("Renderkit"),
    CONFIG("Config"),
    CONNECTION("Connection"),
    APPLICATION("Application"),
    CACHE("Cache"),
    CONTEXT("Context"),
    COMPONENTS("Components"),
    WEBAPP("Webapp"),
    UTIL("Util"),
    MODEL("Model");
    private static final String LOGGER_NAME_PREFIX = "org.richfaces.log.";
    private String loggerName;

    private RichfacesLogger(String loggerName) {
        this.loggerName = LOGGER_NAME_PREFIX + loggerName;
    }

    public String getLoggerName() {
        return loggerName;
    }

    public Logger getLogger() {
        return LogFactory.getLogger(loggerName);
    }

    /**
     *
     * Return string which contains formated path from view root to component.
     *
     * @param component
     * @return string
     */
    public static String getComponentPath(UIComponent component) {
        StringBuilder builder = new StringBuilder("Component path: ");
        if (component == null) {
            builder.append("null");
        } else {
            getComponentPath(component, builder);
        }
        return builder.toString();
    }

    private static void getComponentPath(UIComponent component, StringBuilder builder) {
        if (component != null) {
            getComponentPath(component.getParent(), builder);
            builder.append("/").append(component.getClass().getName());
            if (component instanceof UIViewRoot) {
                builder.append("[viewId=");
                builder.append(((UIViewRoot) component).getViewId());
            } else {
                builder.append("[id=");
                builder.append(component.getId());
            }
            builder.append("]");
        }
    }
}

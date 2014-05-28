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
package org.richfaces.renderkit.util;

import static org.richfaces.JsfVersion.JSF_2_2;

import java.util.Map;

import javax.faces.component.UINamingContainer;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.context.PartialResponseWriter;
import javax.faces.render.ResponseStateManager;

import org.richfaces.JsfVersion;

/**
 * @author Lukas Fryc
 */
public final class ViewStateIdUtils {

    /**
     * A key for an attribute counting ViewState identifiers as specified in {@link ResponseStateManager#VIEW_STATE_PARAM}.
     */
    private static final String VIEW_STATE_COUNTER_KEY = "org.richfaces.ViewStateCounterKey";
    /**
     * A magic number used as a base where a value stored under {@link #VIEW_STATE_COUNTER_KEY} attribute starts.
     */
    private static final int VIEW_STATE_NUMBER_BASE = 0;

    /**
     * Static class - protect constructor
     */
    private ViewStateIdUtils() {
    }

    /**
     * Returns ViewState ID which is suitable for current JSF implementation.
     */
    public static String getViewStateId(FacesContext context) {

        if (JsfVersion.getCurrent().isCompliantWith(JSF_2_2)) {
            return generateUniqueViewStateId(context);
        }

        return PartialResponseWriter.VIEW_STATE_MARKER;
    }

    /**
     * Returns generated ViewState ID as specified by JSF 2.2 in form defined in {@link ResponseStateManager#VIEW_STATE_PARAM}
     */
    private static String generateUniqueViewStateId(FacesContext context) {
        String result = null;
        Map<Object, Object> contextAttrs = context.getAttributes();
        Integer counter = (Integer) contextAttrs.get(VIEW_STATE_COUNTER_KEY);
        if (null == counter) {
            counter = VIEW_STATE_NUMBER_BASE;
        }
        char sep = UINamingContainer.getSeparatorChar(context);
        UIViewRoot root = context.getViewRoot();
        result = root.getContainerClientId(context) + sep +
                ResponseStateManager.VIEW_STATE_PARAM + sep +
                + counter;
        contextAttrs.put(VIEW_STATE_COUNTER_KEY, ++counter);

        return result;
    }
}

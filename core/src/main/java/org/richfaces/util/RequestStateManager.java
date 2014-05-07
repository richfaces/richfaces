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
package org.richfaces.util;

import java.util.HashMap;
import java.util.Map;

import javax.faces.context.FacesContext;

/**
 * @author Nick Belaevski
 * @since 4.0
 */
public final class RequestStateManager {
    private static final String CONTEXT_ATTRIBUTE_NAME = RequestStateManager.class.getName();

    private RequestStateManager() {
    }

    // TODO remove this stuff
    public static enum BooleanRequestStateVariable {
        LegacyResourceRequest("org.richfaces.LEGACY_RESOURCE_REQUEST"),
        ResourceRequest("org.richfaces.RESOURCE_REQUEST");
        private String attributeName;

        private BooleanRequestStateVariable(String attributeName) {
            this.attributeName = attributeName;
        }

        public Boolean get(FacesContext context) {
            return (Boolean) RequestStateManager.get(context, this.attributeName);
        }

        public void set(FacesContext context, Boolean value) {
            RequestStateManager.set(context, this.attributeName, value);
        }
    }

    @SuppressWarnings("unchecked")
    private static Map<String, Object> getStateMap(FacesContext context, boolean create) {
        Map<Object, Object> attributesMap = context.getAttributes();
        Map<String, Object> result = (Map<String, Object>) attributesMap.get(CONTEXT_ATTRIBUTE_NAME);

        if (create && (result == null)) {
            result = new HashMap<String, Object>();
            attributesMap.put(CONTEXT_ATTRIBUTE_NAME, result);
        }

        return result;
    }

    public static boolean containsKey(FacesContext context, String key) {
        if (context == null) {
            throw new NullPointerException("context");
        }

        if (key == null) {
            throw new NullPointerException("key");
        }

        Map<String, Object> stateMap = getStateMap(context, false);

        if (stateMap != null) {
            return stateMap.containsKey(key);
        } else {
            return false;
        }
    }

    public static Object get(FacesContext context, String key) {
        if (context == null) {
            throw new NullPointerException("context");
        }

        if (key == null) {
            throw new NullPointerException("key");
        }

        Map<String, Object> stateMap = getStateMap(context, false);

        if (stateMap != null) {
            return stateMap.get(key);
        }

        return null;
    }

    public static void set(FacesContext context, String key, Object value) {
        if (context == null) {
            throw new NullPointerException("context");
        }

        if (key == null) {
            throw new NullPointerException("key");
        }

        if (value != null) {
            Map<String, Object> stateMap = getStateMap(context, true);

            stateMap.put(key, value);
        } else {
            Map<String, Object> stateMap = getStateMap(context, false);

            if (stateMap != null) {
                stateMap.remove(key);
            }
        }
    }
}

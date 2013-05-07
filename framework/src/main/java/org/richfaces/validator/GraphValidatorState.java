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

package org.richfaces.validator;

import java.util.IdentityHashMap;
import java.util.Map;

import javax.faces.context.FacesContext;

public final class GraphValidatorState {
    public static final String STATE_ATTRIBUTE = "org.richfaces.GraphValidator:";
    private boolean active = false;
    private final Object cloned;

    public GraphValidatorState(Object cloned) {
        this.cloned = cloned;
    }

    /**
     * @return the active
     */
    public boolean isActive() {
        return active;
    }

    /**
     * @param active the active to set
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * @return the cloned
     */
    public Object getCloned() {
        return cloned;
    }

    public static Object getActiveClone(FacesContext context, Object base) {
        GraphValidatorState state = getState(context, base);
        if (null == state || !state.isActive()) {
            return null;
        }
        return state.getCloned();
    }

    public static GraphValidatorState getState(FacesContext context, Object base) {
        return getStateMap(context).get(base);
    }

    public static void setState(FacesContext context, Object base, GraphValidatorState state) {
        getStateMap(context).put(base, state);
    }

    public static Map<Object, GraphValidatorState> getStateMap(FacesContext context) {
        IdentityHashMap<Object, GraphValidatorState> statesMap = (IdentityHashMap<Object, GraphValidatorState>) context
            .getAttributes().get(STATE_ATTRIBUTE);
        if (null == statesMap) {
            statesMap = new IdentityHashMap<Object, GraphValidatorState>();
            context.getAttributes().put(STATE_ATTRIBUTE, statesMap);
        }
        return statesMap;
    }
}
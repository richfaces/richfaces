/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc. and individual contributors
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
package org.richfaces.component.util;

import java.io.Serializable;

import javax.faces.component.PartialStateHolder;
import javax.faces.component.StateHolder;
import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;

/**
 * @author Nick Belaevski
 *
 */
public final class PartialStateHolderUtil {
    private static final class StateHolderObject implements Serializable {
        private static final long serialVersionUID = 6157742187482213801L;
        private boolean partialState;
        private Object savedState;

        public StateHolderObject(boolean partialState, Object savedState) {
            super();
            this.partialState = partialState;
            this.savedState = savedState;
        }

        public boolean isPartialState() {
            return partialState;
        }

        public Object getSavedState() {
            return savedState;
        }
    }

    private PartialStateHolderUtil() {
        // utility class constructor
    }

    public static Object saveState(FacesContext context, UIComponent component, Object objectToSave) {
        Object savedState = null;
        boolean nullDelta = true;

        boolean converterHasPartialState = false;

        if (component.initialStateMarked()) {
            if (objectToSave != null) {
                if (objectToSave instanceof PartialStateHolder) {
                    // Delta
                    StateHolder holder = (StateHolder) objectToSave;
                    if (!holder.isTransient()) {
                        Object attachedState = holder.saveState(context);
                        if (attachedState != null) {
                            nullDelta = false;
                            savedState = attachedState;
                        }
                        converterHasPartialState = true;
                    } else {
                        savedState = null;
                    }
                } else {
                    // Full
                    savedState = UIComponentBase.saveAttachedState(context, objectToSave);
                    nullDelta = false;
                }
            }

            if (savedState == null && nullDelta) {
                // No values
                return null;
            }
        } else {
            savedState = UIComponentBase.saveAttachedState(context, objectToSave);
        }

        return new StateHolderObject(converterHasPartialState, savedState);
    }

    public static Object restoreState(FacesContext context, Object savedState, Object existingObject) {
        if (savedState != null) {
            StateHolderObject stateHolderObject = (StateHolderObject) savedState;
            if (stateHolderObject.isPartialState()) {
                ((StateHolder) existingObject).restoreState(context, stateHolderObject.getSavedState());
                return existingObject;
            } else {
                return UIComponentBase.restoreAttachedState(context, stateHolderObject.getSavedState());
            }
        } else {
            return null;
        }
    }
}

/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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

import java.io.Serializable;

import javax.faces.component.StateHolder;
import javax.faces.context.FacesContext;

import org.richfaces.model.DataComponentState;
import org.richfaces.model.ExtendedDataModel;

public final class DataAdaptorIterationState implements StateHolder {
    private DataComponentState componentState;
    private Object savedComponentState;
    private boolean componentStateIsStateHolder;
    private ExtendedDataModel<?> dataModel;

    public DataAdaptorIterationState() {
        super();
    }

    public DataAdaptorIterationState(DataComponentState componentState, ExtendedDataModel<?> dataModel) {
        super();
        this.componentState = componentState;
        this.dataModel = dataModel;
    }

    public ExtendedDataModel<?> getDataModel() {
        return dataModel;
    }

    public DataComponentState getComponentState() {
        return componentState;
    }

    /**
     * @param uiDataAdaptor
     */
    public void restoreComponentState(UIDataAdaptor uiDataAdaptor) {
        if (savedComponentState != null && componentStateIsStateHolder) {
            componentState = uiDataAdaptor.createComponentState();
            ((StateHolder) componentState).restoreState(FacesContext.getCurrentInstance(), savedComponentState);
            savedComponentState = null;
        }
    }

    public void setTransient(boolean newTransientValue) {
        throw new UnsupportedOperationException();
    }

    public boolean isTransient() {
        if (componentState instanceof StateHolder) {
            return ((StateHolder) componentState).isTransient();
        }

        if (componentState instanceof Serializable) {
            return false;
        }

        return true;
    }

    public Object saveState(FacesContext context) {
        if (isTransient()) {
            return null;
        }

        boolean localComponentStateIsHolder = false;
        Object localSavedComponentState = null;

        if (componentState instanceof StateHolder) {
            localComponentStateIsHolder = true;

            StateHolder stateHolder = (StateHolder) componentState;
            localSavedComponentState = stateHolder.saveState(context);
        } else if (componentState instanceof Serializable) {
            localSavedComponentState = componentState;
        }

        return new Object[] { localComponentStateIsHolder ? Boolean.TRUE : Boolean.FALSE, localSavedComponentState };
    }

    public void restoreState(FacesContext context, Object stateObject) {
        if (stateObject != null) {
            Object[] state = (Object[]) stateObject;
            componentStateIsStateHolder = Boolean.TRUE.equals(state[0]);
            Object localSavedComponentState = state[1];

            if (componentStateIsStateHolder) {
                savedComponentState = localSavedComponentState;
            } else {
                componentState = (DataComponentState) localSavedComponentState;
            }
        }
    }
}
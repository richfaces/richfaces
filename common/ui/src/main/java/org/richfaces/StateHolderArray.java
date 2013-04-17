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
package org.richfaces;

import java.util.ArrayList;

import javax.faces.component.StateHolder;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;

import com.google.common.collect.Lists;

/**
 * @author Nick Belaevski
 *
 */
public class StateHolderArray implements StateHolder {
    private boolean tranzient = false;
    private ArrayList<Object> backingList = Lists.newArrayListWithCapacity(2);

    public Object saveState(FacesContext context) {
        Object[] savedState = new Object[backingList.size()];

        boolean hasNonNullState = false;

        for (int i = 0; i < savedState.length; i++) {
            Object state = UIComponentBase.saveAttachedState(context, backingList.get(i));
            savedState[i] = state;

            if (state != null) {
                hasNonNullState = true;
            }
        }

        if (hasNonNullState) {
            return savedState;
        } else {
            return null;
        }
    }

    public void restoreState(FacesContext context, Object stateObject) {
        if (stateObject != null) {
            Object[] state = (Object[]) stateObject;

            backingList.ensureCapacity(state.length);

            for (int i = 0; i < state.length; i++) {
                backingList.add(UIComponentBase.restoreAttachedState(context, state[i]));
            }
        }
    }

    public boolean isTransient() {
        return tranzient;
    }

    public void setTransient(boolean newTransientValue) {
        this.tranzient = newTransientValue;
    }

    public boolean add(Object e) {
        return backingList.add(e);
    }

    public Object get(int index) {
        return backingList.get(index);
    }

    public boolean isEmpty() {
        return backingList.isEmpty();
    }
}

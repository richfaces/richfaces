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



package org.ajax4jsf.tests;

import java.io.Serializable;

import javax.faces.context.FacesContext;

import org.ajax4jsf.model.DataComponentState;
import org.ajax4jsf.model.Range;

/**
 * @author shura
 *
 */
public class MockComponentState implements DataComponentState, Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -4278697745017092414L;
    private int _count = 2;

    /*
     *  (non-Javadoc)
     * @see org.ajax4jsf.ajax.repeat.DataComponentState#getRange()
     */
    public Range getRange() {

        // TODO Auto-generated method stub
        return new MockRange(_count);
    }

    /*
     *  (non-Javadoc)
     * @see javax.faces.component.StateHolder#isTransient()
     */
    public boolean isTransient() {

        // TODO Auto-generated method stub
        return false;
    }

    /*
     *  (non-Javadoc)
     * @see javax.faces.component.StateHolder#restoreState(javax.faces.context.FacesContext, java.lang.Object)
     */
    public void restoreState(FacesContext context, Object state) {
        _count = ((Integer) state).intValue();
    }

    /*
     *  (non-Javadoc)
     * @see javax.faces.component.StateHolder#saveState(javax.faces.context.FacesContext)
     */
    public Object saveState(FacesContext context) {

        // TODO Auto-generated method stub
        return new Integer(_count);
    }

    /*
     *  (non-Javadoc)
     * @see javax.faces.component.StateHolder#setTransient(boolean)
     */
    public void setTransient(boolean newTransientValue) {

        // TODO Auto-generated method stub
    }

    /**
     * @return the count
     */
    public int getCount() {
        return _count;
    }

    /**
     * @param count the count to set
     */
    public void setCount(int count) {
        _count = count;
    }
}

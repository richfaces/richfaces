/**
 * License Agreement.
 *
 *  JBoss RichFaces - Ajax4jsf Component Library
 *
 * Copyright (C) 2007  Exadel, Inc.
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

package org.richfaces.event;

import javax.faces.component.UIComponent;
import javax.faces.event.FacesEvent;
import javax.faces.event.FacesListener;

/**
 * @author Nick Belaevski - nbelaevski@exadel.com
 *         created 23.01.2007
 */
public class SwitchablePanelSwitchEvent extends FacesEvent {

    /**
     *
     */
    private static final long serialVersionUID = 8579050007143915239L;
    private UIComponent eventSource;
    private Object value;

    public SwitchablePanelSwitchEvent(UIComponent component, Object value, UIComponent eventSource) {
        super(component);
        this.value = value;
        this.eventSource = eventSource;
    }

    public Object getValue() {
        return value;
    }

    public UIComponent getEventSource() {
        return eventSource;
    }

    public boolean isAppropriateListener(FacesListener listener) {
        return false;
    }

    public void processListener(FacesListener listener) {
        throw new UnsupportedOperationException();
    }
}

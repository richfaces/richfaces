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



package org.ajax4jsf.event;

import javax.faces.component.UIComponent;
import javax.faces.event.FacesEvent;
import javax.faces.event.FacesListener;
import javax.faces.event.PhaseId;

/**
 * @author shura (latest modification by $Author: alexsmirnov $)
 * @version $Revision: 1.1.2.1 $ $Date: 2007/01/09 18:57:33 $
 *
 * Event for send by <code>AjaxContainer</code> in case of Ajax Request.
 * Used for build set of id's to render at current request, perform special render etc.
 */
public class AjaxEvent extends FacesEvent {

    /**
     *
     */
    private static final long serialVersionUID = -5624716710738446159L;

    /**
     * @param component
     */
    public AjaxEvent(UIComponent component) {
        super(component);
        setPhaseId(PhaseId.RENDER_RESPONSE);
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.faces.event.FacesEvent#isAppropriateListener(javax.faces.event.FacesListener)
     */
    @Override
    public boolean isAppropriateListener(FacesListener listener) {
        return listener instanceof AjaxListener;
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.faces.event.FacesEvent#processListener(javax.faces.event.FacesListener)
     */
    @Override
    public void processListener(FacesListener listener) {
        ((AjaxListener) listener).processAjax(this);
    }
}

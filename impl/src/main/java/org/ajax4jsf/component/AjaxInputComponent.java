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

package org.ajax4jsf.component;

import org.ajax4jsf.context.AjaxContext;
import org.ajax4jsf.event.AjaxEvent;

import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.FacesEvent;
import javax.faces.event.PhaseId;

/**
 * Base class for all AJAX-enabled Input components
 *
 * @author asmirnov@exadel.com (latest modification by $Author: alexsmirnov $)
 * @version $Revision: 1.1.2.2 $ $Date: 2007/02/06 16:23:21 $
 */
public abstract class AjaxInputComponent extends UIInput implements AjaxComponent, EditableValueHolder {

    /*
     *  (non-Javadoc)
     * @see javax.faces.component.UIComponentBase#broadcast(javax.faces.event.FacesEvent)
     */
    public void broadcast(FacesEvent event) throws AbortProcessingException {

        // perform default
        super.broadcast(event);

        if (event instanceof AjaxEvent) {

            // complete re-Render fields. AjaxEvent deliver before render response.
            setupReRender();
        }
    }

    /**
     * Template methods for fill set of resions to render in subclasses.
     */
    protected void setupReRender() {
        FacesContext context = getFacesContext();

        AjaxContext.getCurrentInstance(context).addRegionsFromComponent(this);
    }

    /*
     *  (non-Javadoc)
     * @see javax.faces.component.UIComponentBase#queueEvent(javax.faces.event.FacesEvent)
     */
    public void queueEvent(FacesEvent event) {
        if ((event instanceof ActionEvent) && (event.getComponent() == this)) {
            if (isImmediate()) {
                event.setPhaseId(PhaseId.APPLY_REQUEST_VALUES);
            } else if (isBypassUpdates()) {
                event.setPhaseId(PhaseId.PROCESS_VALIDATIONS);
            } else {
                event.setPhaseId(PhaseId.INVOKE_APPLICATION);
            }
        }

        super.queueEvent(event);
    }
}

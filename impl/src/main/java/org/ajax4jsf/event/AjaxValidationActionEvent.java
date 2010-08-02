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
import javax.faces.event.ActionEvent;
import javax.faces.event.PhaseId;

/**
 * Action event - always assigned to {@link javax.faces.event.PhaseId#PROCESS_VALIDATIONS} phase
 * for bypass change phase in components , setPhaseId method do nothing.
 *
 * @author shura (latest modification by $Author: alexsmirnov $)
 * @version $Revision: 1.1.2.1 $ $Date: 2007/01/09 18:57:37 $
 */
public class AjaxValidationActionEvent extends ActionEvent {

    /**
     *
     */
    private static final long serialVersionUID = 941784856915815112L;

    /**
     * @param component
     */
    public AjaxValidationActionEvent(UIComponent component) {
        super(component);

        // TODO Auto-generated constructor stub
    }

    /*
     *  (non-Javadoc)
     * @see javax.faces.event.FacesEvent#getPhaseId()
     */
    public PhaseId getPhaseId() {

        // TODO Auto-generated method stub
        return PhaseId.PROCESS_VALIDATIONS;
    }
}

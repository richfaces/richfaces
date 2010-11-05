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

package org.richfaces.context;

import java.util.Collection;
import java.util.Collections;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.ajax4jsf.component.AjaxClientBehavior;
import org.richfaces.component.AjaxContainer;

/**
 * @author akolonitsky
 * @since Oct 13, 2009
 */
class ExecuteComponentCallback extends RenderComponentCallback {

    private Collection<String> executeIds = null;
    
    ExecuteComponentCallback(FacesContext facesContext, String behaviorEvent) {
        super(facesContext, behaviorEvent);
    }

    @Override
    protected void doVisit(UIComponent target, AjaxClientBehavior behavior) {
        super.doVisit(target, behavior);
        
        Object value;
        if (behavior != null) {
            value = behavior.getExecute();
        } else {
            value = target.getAttributes().get("execute");
        }
        
        executeIds = resolveComponents(value, target, AjaxContainer.META_CLIENT_ID);
    }
    
    public Collection<String> getExecuteIds() {
        return (executeIds != null) ? executeIds : Collections.<String>emptySet();
    }
    
}

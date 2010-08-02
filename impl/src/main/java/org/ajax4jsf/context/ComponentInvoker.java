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

package org.ajax4jsf.context;

import org.ajax4jsf.component.AjaxViewRoot;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 * Interface for call lifecycle methods on component with known clientId
 * For JSF 1.2 implementation must use invokeOnComponent method, for JSF 1.1 use
 * simple recursion on view tree ( not compatible with UIData components )
 *
 * @author shura
 */
public interface ComponentInvoker {

    /**
     * Invoke method on active AjaxContainer component, or on viewRoot, if none.
     *
     * @param viewRoot
     * @param context
     * @param callback
     */
    public abstract void invokeOnRegionOrRoot(AjaxViewRoot viewRoot, FacesContext context, InvokerCallback callback);

    /**
     * Recursive call for all children for search component with same clientId as target region
     *
     * @param viewRoot
     * @param context
     * @param callback
     * @param regionId
     */
    public abstract boolean invokeOnComponent(UIComponent root, FacesContext context, InvokerCallback callback,
                                              String regionId);
}

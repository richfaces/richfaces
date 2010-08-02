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

import org.ajax4jsf.context.InvokerCallback;

import javax.faces.context.FacesContext;

/**
 * @author Anton Belevich
 */
public class InvokerCallbackWrapper extends ContextCallbackWrapper implements InvokerCallback {
    public InvokerCallbackWrapper(InvokerCallback invokerCallback) {
        super(invokerCallback);
    }

    /*
     * @see org.ajax4jsf.context.InvokerCallback#invokeRoot(javax.faces.context.FacesContext)
     */
    public void invokeRoot(FacesContext context) {
        ((InvokerCallback) this.callback).invokeRoot(context);
    }
}

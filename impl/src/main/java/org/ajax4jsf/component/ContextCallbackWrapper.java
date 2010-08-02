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

import javax.faces.component.ContextCallback;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 * @author Anton Belevich
 */
public class ContextCallbackWrapper implements ContextCallback {
    ContextCallback callback;

    public ContextCallbackWrapper(ContextCallback callback) {
        this.callback = callback;
    }

    public void invokeContextCallback(FacesContext context, UIComponent target) {
        if (isParentRendered(target)) {
            callback.invokeContextCallback(context, target);
        }
    }

    public boolean isParentRendered(UIComponent target) {
        UIComponent component = target;

        while (component != null) {
            if (!component.isRendered()) {
                return false;
            }

            component = component.getParent();
        }

        return true;
    }
}

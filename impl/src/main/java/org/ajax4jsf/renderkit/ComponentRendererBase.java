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

package org.ajax4jsf.renderkit;

import javax.faces.component.UIComponent;
import java.util.HashMap;
import java.util.Stack;

/**
 * Components Base Renderer for all chameleon Skin's and components.
 * At most, make all common procedures and realise concrete work in "template" methods.
 *
 * @author asmirnov@exadel.com (latest modification by $Author: alexsmirnov $)
 * @version $Revision: 1.1.2.1 $ $Date: 2007/01/09 18:58:52 $
 */
public abstract class ComponentRendererBase extends RendererBase {

    /**
     * logger for common cases.
     */
    protected static final String COMPONENT_RENDERER_BASE = ComponentRendererBase.class.getName();

    public ComponentVariables getVariables(UIComponent component) {
        HashMap components;
        Stack stackComponentsVariables;
        ComponentVariables variables;

        components = (HashMap) component.getAttributes().get(COMPONENT_RENDERER_BASE);

        if (components == null) {
            components = new HashMap();
            component.getAttributes().put(COMPONENT_RENDERER_BASE, components);
        }

        stackComponentsVariables = (Stack) components.get(this.getClass().getName());

        if (stackComponentsVariables == null) {
            stackComponentsVariables = new Stack();
            components.put(COMPONENT_RENDERER_BASE, stackComponentsVariables);
        }

        if (stackComponentsVariables.empty()) {
            variables = new ComponentVariables();
            stackComponentsVariables.push(variables);
        } else {
            variables = (ComponentVariables) stackComponentsVariables.peek();
        }

        return variables;
    }

    public void removeVariables(UIComponent component) {
        HashMap components;
        Stack stackComponentsVariables;
        ComponentVariables variables;

        components = (HashMap) component.getAttributes().get(COMPONENT_RENDERER_BASE);

        if (components != null) {
            stackComponentsVariables = (Stack) component.getAttributes().get(this.getClass().getName());

            if (stackComponentsVariables != null) {
                if (!stackComponentsVariables.empty()) {
                    stackComponentsVariables.pop();
                }

                if (stackComponentsVariables.empty()) {
                    components.remove(this.getClass().getName());
                }
            }

            if (components.isEmpty()) {
                component.getAttributes().remove(COMPONENT_RENDERER_BASE);
            }
        }
    }
}

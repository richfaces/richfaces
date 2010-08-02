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

/**
 * Resolve component variables
 *
 * @author ayukhovich@exadel.com (latest modification by $Author:
 *         alexeyyukhovich $)
 * @version $Revision: 1.1.2.1 $ $Date: 2007/01/09 18:58:53 $
 */
public final class ComponentsVariableResolver {
    private static final String COMPONENTS_VARIABLE_RESOLVER = ComponentsVariableResolver.class.getName();

    private ComponentsVariableResolver() {
    }

    private static String getAttributeName(RendererBase renderer) {
        return COMPONENTS_VARIABLE_RESOLVER + ":" + ((renderer != null) ? renderer.getClass().getName() : null);
    }

    public static ComponentVariables getVariables(RendererBase renderer, UIComponent component) {
        ComponentVariables variables;
        String attributeName = getAttributeName(renderer);

        variables = (ComponentVariables) component.getAttributes().get(attributeName);

        if (variables == null) {
            variables = new ComponentVariables();
            component.getAttributes().put(attributeName, variables);
        }

        return variables;
    }

    public static void removeVariables(RendererBase renderer, UIComponent component) {
        component.getAttributes().remove(getAttributeName(renderer));
    }
}

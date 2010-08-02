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

import java.io.IOException;

import java.util.Set;

import javax.faces.context.FacesContext;

/**
 * Marker interface for components, have ability to manipulate rendering in case of Ajax responses.
 * Such as ajax-enabled iterator, menus etc
 * @author shura
 *
 */
public interface AjaxChildrenEncoder {

    /**
     * Iterate over all childs of components. If component id contains in list ,
     * or, if list is empty, compotents is submitted form - render it.
     * TODO - Instead of calculate full path for every component, build current Path
     * for componet and send as parameter.
     *
     * @param context -
     *            current context
     * @param component -
     *            curent faces component.
     * @param ids -
     *            list of Id to render.
     * @throws IOException
     */
    public void encodeAjaxChild(FacesContext context, String path, Set<String> ids, Set<String> renderedAreas)
        throws IOException;
}

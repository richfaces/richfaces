/**
 * License Agreement.
 *
 *  JBoss RichFaces - Ajax4jsf Component Library
 *
 * Copyright (C) 2007  Exadel, Inc.
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

package org.richfaces.component.nsutils;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;

/**
 * @author Nick Belaevski - nbelaevski@exadel.com
 *         created 21.12.2006
 */
public final class NSUtils {
    public static final String XMLNS_PREFIX = "rich";
    public static final String XMLNS_URI = "http://richfaces.ajax4jsf.org/rich";
    public static final String XMLNS_VALUE = "xmlns:" + XMLNS_PREFIX;

    private NSUtils() {
    }

    public static void writeNameSpace(FacesContext context, UIComponent component) throws IOException {
        context.getResponseWriter().writeAttribute(NSUtils.XMLNS_VALUE, NSUtils.XMLNS_URI, null);
    }
}

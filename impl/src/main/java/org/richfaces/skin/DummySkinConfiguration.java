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

package org.richfaces.skin;

import javax.faces.context.FacesContext;

/**
 * @author shura
 */
public class DummySkinConfiguration implements SkinConfiguration {
    private Skin skin;

    /**
     * @param skin
     */
    public DummySkinConfiguration(Skin skin) {
        super();
        this.skin = skin;
    }

    /*
     *  (non-Javadoc)
     * @see org.richfaces.skin.SkinConfiguration#containsParameter(java.lang.String)
     */
    public boolean containsParameter(String name) {
        return skin.containsProperty(name);
    }

    /*
     *  (non-Javadoc)
     * @see org.richfaces.skin.SkinConfiguration#getParameter(javax.faces.context.FacesContext, java.lang.String)
     */
    public Object getParameter(FacesContext context, String name) {
        return skin.getParameter(context, name);
    }

    /*
     *  (non-Javadoc)
     * @see org.richfaces.skin.SkinConfiguration#getParameter(javax.faces.context.FacesContext, String, String, Object)
     */
    public Object getParameter(FacesContext context, String name, String skinName, Object defaultValue) {
        Object parameter = skin.getParameter(context, name);

        if (null == parameter) {
            parameter = defaultValue;
        }

        return parameter;
    }
}

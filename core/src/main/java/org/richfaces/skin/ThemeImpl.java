/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

/**
 *
 */
package org.richfaces.skin;

import java.util.Properties;

import javax.el.ValueExpression;
import javax.faces.context.FacesContext;

/**
 * @author asmirnov
 */
public class ThemeImpl implements Theme {
    private final Properties themeProperties;

    /**
     * @param themeProperties
     */
    public ThemeImpl(Properties themeProperties) {
        this.themeProperties = themeProperties;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.skin.Theme#getProperty(java.lang.String)
     */
    public Object getProperty(String name) {
        Object property = themeProperties.get(name);

        if (property instanceof ValueExpression) {
            ValueExpression ve = (ValueExpression) property;

            property = ve.getValue(FacesContext.getCurrentInstance().getELContext());
        }

        return property;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.skin.Theme#getRendererType()
     */
    public String getRendererType() {
        return (String) getProperty("rendererType");
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.skin.Theme#getScript()
     */
    public String getScript() {
        return (String) getProperty("script");
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.skin.Theme#getStyle()
     */
    public String getStyle() {
        return (String) getProperty("styleSheet");
    }
}

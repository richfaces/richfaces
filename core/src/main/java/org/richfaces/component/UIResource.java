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
package org.richfaces.component;

import javax.faces.application.ResourceHandler;
import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.render.Renderer;

/**
 * <p class="changed_added_4_0">
 * </p>
 *
 * @author asmirnov@exadel.com
 *
 */
public class UIResource extends UITransient {
    private final String name;
    private final String library;

    public UIResource(UIComponent parent, String name, String library) {
        this.name = name;
        this.library = library;
        setParent(parent);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.component.UITransient#hasAttribute(java.lang.Object)
     */
    @Override
    protected boolean hasAttribute(Object key) {
        if ("name".equals(key)) {
            return null != getName();
        } else if ("library".equals(key)) {
            return null != getLibrary();
        }
        return false;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.component.UITransient#setAttribute(java.lang.String, java.lang.Object)
     */
    @Override
    protected Object setAttribute(String key, Object value) {
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.component.UITransient#getAttribute(java.lang.Object)
     */
    @Override
    protected Object getAttribute(Object key) {
        if ("name".equals(key)) {
            return getName();
        } else if ("library".equals(key)) {
            return getLibrary();
        }
        return null;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @return the library
     */
    public String getLibrary() {
        return library;
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.faces.component.UIComponent#getFamily()
     */
    @Override
    public String getFamily() {
        return UIOutput.COMPONENT_FAMILY;
    }

    @Override
    public String getRendererType() {
        return getRendererType(getFacesContext());
    }

    public String getRendererType(FacesContext context) {
        ResourceHandler resourceHandler = context.getApplication().getResourceHandler();
        return resourceHandler.getRendererTypeForResourceName(getName());
    }

    @Override
    protected Renderer getRenderer(FacesContext context) {
        String rendererType = getRendererType(context);
        Renderer result = null;
        if (rendererType != null) {
            result = context.getRenderKit().getRenderer(getFamily(), rendererType);
        }
        return result;
    }
}

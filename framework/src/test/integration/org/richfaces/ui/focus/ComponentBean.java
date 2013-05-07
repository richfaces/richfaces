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

package org.richfaces.ui.focus;

import javax.enterprise.context.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.render.Renderer;
import javax.inject.Named;

@Named
@RequestScoped
public class ComponentBean {

    private UIComponent component;

    public void setComponent(UIComponent component) {
        this.component = component;
    }

    @SuppressWarnings("unchecked")
    public <T extends UIComponent> T getComponent() {
        return (T) component;
    }

    @SuppressWarnings("unchecked")
    public <T extends Renderer> T getRenderer() {
        FacesContext context = FacesContext.getCurrentInstance();
        String componentFamily = component.getFamily();
        String rendererType = component.getRendererType();
        return (T) context.getRenderKit().getRenderer(componentFamily, rendererType);
    }
}
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

package org.richfaces.renderkit.html;

import org.richfaces.component.UIResource;
import org.richfaces.resource.ResourceKey;
import org.richfaces.resource.ResourceLibrary;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.render.Renderer;

import java.io.IOException;
import java.util.Collection;

public abstract class ResourceRenderer extends Renderer {
    public ResourceRenderer() {
        super();
    }

    protected void encodeDependentResources(FacesContext context, UIComponent component, Collection<Object> scripts)
        throws IOException {
        for (Object script : scripts) {
            if (script instanceof ResourceLibrary) {
                ResourceLibrary library = (ResourceLibrary) script;
                for (ResourceKey resource : library.getResources()) {
                    encodeResource(component, context, resource);
                }
            }
        }
    }

    protected void encodeResource(UIComponent component, FacesContext context, ResourceKey resource) throws IOException {
        UIResource resourceComponent = new UIResource(component, resource.getResourceName(), resource.getLibraryName());
        resourceComponent.encodeAll(context);
    }
}
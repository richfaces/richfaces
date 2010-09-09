/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc. and individual contributors
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

import java.io.IOException;
import java.util.Map;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.render.Renderer;

import org.richfaces.application.ServiceTracker;
import org.richfaces.log.Logger;
import org.richfaces.log.RichfacesLogger;
import org.richfaces.resource.ResourceKey;
import org.richfaces.resource.ResourceLibrary;
import org.richfaces.resource.ResourceLibraryFactory;

import com.google.common.base.Joiner;

/**
 * @author Nick Belaevski
 * 
 */
public class ResourceLibraryRenderer extends Renderer {

    public static final String RENDERER_TYPE = "org.richfaces.renderkit.ResourceLibraryRenderer";

    public static final String RESOURCE_LIBRARY_EXTENSION = ".reslib";
    
    private static final Logger LOGGER = RichfacesLogger.RENDERKIT.getLogger();

    private static final Joiner COLON_JOINER = Joiner.on(':').skipNulls();
    
    public ResourceLibraryRenderer() {
        super();
    }

    private void setupResourceAttributes(UIComponent component, ResourceKey resourceKey) {
        Map<String, Object> attributes = component.getAttributes();
        
        attributes.put("name", resourceKey.getResourceName());
        
        String libraryName = resourceKey.getLibraryName();
        if (libraryName != null) {
            attributes.put("library", libraryName);
        } else {
            attributes.remove("library");
        }
    }
    
    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        Map<String, Object> attributes = component.getAttributes();
        
        String name = (String) attributes.get("name");
        
        if (!name.endsWith(RESOURCE_LIBRARY_EXTENSION)) {
            throw new IllegalArgumentException("Resource library name: " + name + " is incorrect");
        }
        
        name = name.substring(0, name.length() - RESOURCE_LIBRARY_EXTENSION.length());
        
        String library = (String) attributes.get("library");
        
        ResourceLibraryFactory factory = ServiceTracker.getService(ResourceLibraryFactory.class);
        ResourceLibrary resourceLibrary = factory.getResourceLibrary(name, library);
        
        if (resourceLibrary == null) {
            LOGGER.error("Resource library is null: " + COLON_JOINER.join(library, name));
            return;
        }
        
        Application application = context.getApplication();
        UIComponent resourceComponent = null;
        
        for (ResourceKey resourceKey: resourceLibrary.getResources(context)) {
            String rendererType = application.getResourceHandler().getRendererTypeForResourceName(resourceKey.getResourceName());
            
            if (resourceComponent == null) {
                resourceComponent = application.createComponent(UIOutput.COMPONENT_TYPE);
                resourceComponent.setTransient(true);
                component.getChildren().add(resourceComponent);
            }

            resourceComponent.setRendererType(rendererType);
            setupResourceAttributes(resourceComponent, resourceKey);

            resourceComponent.encodeAll(context);
        }
    }

}

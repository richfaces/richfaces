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
package org.richfaces.resource.optimizer.resource.handler.impl;

import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.faces.context.FacesContext;

import org.richfaces.resource.ResourceFactory;
import org.richfaces.resource.ResourceKey;
import org.richfaces.resource.ResourceRequestData;
import org.richfaces.resource.ResourceUtils;

/**
 * @author Nick Belaevski
 *
 */
public class DynamicResourceHandler extends AbstractResourceHandler {
    private static final class ResourceRequestDataImpl implements ResourceRequestData {
        private ResourceKey resourceKey;
        private Object resourceData;

        public ResourceRequestDataImpl(ResourceKey resourceKey, Object resourceData) {
            super();
            this.resourceKey = resourceKey;
            this.resourceData = resourceData;
        }

        @Override
        public String getResourceName() {
            return resourceKey.getResourceName();
        }

        @Override
        public String getLibraryName() {
            return resourceKey.getLibraryName();
        }

        @Override
        public Object getData() {
            return resourceData;
        }

        @Override
        public String getResourceKey() {
            return resourceKey.toString();
        }

        @Override
        public String getVersion() {
            return null;
        }
    }

    private ResourceFactory resourceFactory;
    private ResourceHandler staticResourceHandler;

    public DynamicResourceHandler(ResourceHandler staticResourceHandler, ResourceFactory resourceFactory) {
        this.staticResourceHandler = staticResourceHandler;
        this.resourceFactory = resourceFactory;
    }

    @Override
    public Resource createResource(String resourceName, String libraryName, String contentType) {
        ResourceKey resourceKey = new ResourceKey(resourceName, libraryName);
        Resource result = resourceFactory.createResource(resourceName, libraryName, contentType);

        if (result != null) {
            FacesContext context = FacesContext.getCurrentInstance();
            Object state = ResourceUtils.saveResourceState(context, result);
            Resource newResource = resourceFactory.createResource(context, new ResourceRequestDataImpl(resourceKey, state));
            if (newResource != null) {
                result = new DynamicResourceWrapper(newResource);
            }
        }

        if (result == null) {
            result = staticResourceHandler.createResource(resourceName, libraryName, contentType);
        }

        return result;
    }
}

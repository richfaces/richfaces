/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2012, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
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
package org.richfaces.resource.external;

import java.util.concurrent.atomic.AtomicReference;

import javax.faces.context.FacesContext;

import org.richfaces.resource.ResourceKey;
import org.richfaces.resource.ResourceSkinUtils;
import org.richfaces.util.URLUtils;

/**
 * Holds {@link ResourceKey} and external resource location in order to create {@link ExternalResource} lazily.
 *
 * @author Lukas Fryc
 */
public class ExternalMapping {

    private ResourceKey resourceKey;
    private String resourceLocation;
    private AtomicReference<ExternalResource> resourceReference = new AtomicReference<ExternalResource>();

    public ExternalMapping(ResourceKey resourceKey, String resourceLocation) {
        this.resourceKey = resourceKey;
        this.resourceLocation = resourceLocation;
    }

    /**
     * Creates new {@link ExternalResource} for given {@link ResourceKey} and external resource location
     *
     * @param facesContext current {@link FacesContext}
     * @return new {@link ExternalResource} for given {@link ResourceKey} and external resource location
     */
    public ExternalResource getResource(FacesContext facesContext) {
        ExternalResource resource = resourceReference.get();

        if (resource == null) {

            // checks if provided location is skin-dependent
            boolean skinDependent = false;
            if (ResourceSkinUtils.isSkinDependent(resourceLocation)) {
                skinDependent = true;
            }

            // checks that provided resourceLocation is valid URL = then it is considered absolute URL
            if (URLUtils.isValidURL(resourceLocation)) {
                resource = new AbsoluteRequestPathResource(resourceLocation);
            } else {
                resource = new ExternalStaticResource(resourceLocation, skinDependent);
            }

            resource.setResourceName(resourceKey.getResourceName());
            resource.setLibraryName(resourceKey.getLibraryName());
            resource.setContentType(facesContext.getExternalContext().getMimeType(resourceLocation));

            resourceReference.compareAndSet(null, resource);
            resource = resourceReference.get();
        }

        return resource;
    }

    /**
     * Returns external resource location
     *
     * @return external resource location
     */
    public String getResourceLocation() {
        return resourceLocation;
    }

}
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

import java.util.Arrays;
import java.util.Set;

import javax.faces.context.FacesContext;

import org.apache.myfaces.shared.renderkit.html.util.ResourceUtils;
import org.richfaces.application.ServiceTracker;
import org.richfaces.resource.ResourceKey;

/**
 * Tracks what external resources are renderered to the page (specific for MyFaces)
 *
 * @author Lukas Fryc
 */
public class MyFacesExternalResourceTracker implements ExternalResourceTracker {

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.resource.external.ExternalResourceTracker#isResourceRenderered(javax.faces.context.FacesContext,
     * org.richfaces.resource.ResourceKey)
     */
    @Override
    public boolean isResourceRenderered(FacesContext facesContext, ResourceKey resourceKey) {
        final String mimeType = facesContext.getExternalContext().getMimeType(resourceKey.getResourceName());

        if (MimeType.STYLESHEET.contains(mimeType)) {
            return ResourceUtils
                    .isRenderedStylesheet(facesContext, resourceKey.getLibraryName(), resourceKey.getResourceName());
        } else if (MimeType.SCRIPT.contains(mimeType)) {
            return ResourceUtils.isRenderedScript(facesContext, resourceKey.getLibraryName(), resourceKey.getResourceName());
        }

        return false;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.resource.external.ExternalResourceTracker#markResourceRendered(javax.faces.context.FacesContext,
     * org.richfaces.resource.ResourceKey)
     */
    @Override
    public void markResourceRendered(FacesContext facesContext, ResourceKey resourceKey) {
        final String mimeType = facesContext.getExternalContext().getMimeType(resourceKey.getResourceName());

        if (MimeType.STYLESHEET.contains(mimeType)) {
            ResourceUtils.markStylesheetAsRendered(facesContext, resourceKey.getLibraryName(), resourceKey.getResourceName());
        } else if (MimeType.SCRIPT.contains(mimeType)) {
            ResourceUtils.markScriptAsRendered(facesContext, resourceKey.getLibraryName(), resourceKey.getResourceName());
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.richfaces.resource.external.ExternalResourceTracker#markExternalResourceRendered(javax.faces.context.FacesContext,
     * org.richfaces.resource.external.ExternalResource)
     */
    @Override
    public void markExternalResourceRendered(FacesContext facesContext, ExternalResource resource) {
        ExternalStaticResourceFactory externalStaticResourceFactory = ServiceTracker
                .getService(ExternalStaticResourceFactory.class);

        ResourceKey originalResourceKey = ResourceKey.create(resource);
        Set<ResourceKey> resourcesKeys = externalStaticResourceFactory.getResourcesForLocation(resource.getExternalLocation());

        for (ResourceKey resourceKey : resourcesKeys) {
            if (!originalResourceKey.equals(resourceKey)) {
                markResourceRendered(facesContext, resourceKey);
            }
        }
    }

    private enum MimeType {
        SCRIPT("application/javascript", "text/javascript"),
        STYLESHEET("text/css");

        private String[] types;

        private MimeType(String... types) {
            this.types = types;
        }

        public boolean contains(String type) {
            return Arrays.asList(types).contains(type);
        }
    }
}

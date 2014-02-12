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
package org.richfaces.resource.external;

import java.lang.reflect.Method;
import java.util.Arrays;

import javax.faces.context.FacesContext;

import org.richfaces.log.Logger;
import org.richfaces.log.RichfacesLogger;
import org.richfaces.resource.ResourceKey;

/**
 * Tracks what external resources are renderered to the page (specific for MyFaces)
 *
 * @author Lukas Fryc
 */
public class ResourceTrackerForMyFaces implements ResourceTracker {

    private static final Logger LOG = RichfacesLogger.RESOURCE.getLogger();

    private final Method isRenderedStylesheet;
    private final Method isRenderedScript;
    private final Method markStylesheetAsRendered;
    private final Method markScriptAsRendered;

    public ResourceTrackerForMyFaces(Class<?> resourceUtilsClass) {
        try {
            isRenderedStylesheet = resourceUtilsClass.getMethod("isRenderedStylesheet", FacesContext.class, String.class,
                String.class);
            isRenderedScript = resourceUtilsClass.getMethod("isRenderedScript", FacesContext.class, String.class, String.class);
            markStylesheetAsRendered = resourceUtilsClass.getMethod("markStylesheetAsRendered", FacesContext.class,
                String.class, String.class);
            markScriptAsRendered = resourceUtilsClass.getMethod("markScriptAsRendered", FacesContext.class, String.class,
                String.class);
        } catch (Exception e) {
            ResourceTrackerForMyFaces.handleException(e);
            throw new ExceptionInInitializerError(e);
        }
    }

    private static void handleException(Exception e) {
        // none of these exceptions should occure in real life.
        LOG.error("error while delegating resource handling to myfaces impl", e);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.resource.external.ExternalResourceTracker#isResourceRenderered(javax.faces.context.FacesContext,
     * org.richfaces.resource.ResourceKey)
     */
    @Override
    public boolean isResourceRenderered(FacesContext facesContext, ResourceKey resourceKey) {
        final String mimeType = facesContext.getExternalContext().getMimeType(resourceKey.getResourceName());

        try {
            if (MimeType.STYLESHEET.contains(mimeType)) {
                return (Boolean) isRenderedStylesheet.invoke(null, facesContext, resourceKey.getLibraryName(),
                    resourceKey.getResourceName());
            } else if (MimeType.SCRIPT.contains(mimeType)) {
                return (Boolean) isRenderedScript.invoke(null, facesContext, resourceKey.getLibraryName(),
                    resourceKey.getResourceName());
            }
        } catch (Exception e) {
            ResourceTrackerForMyFaces.handleException(e);
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

        try {
            if (MimeType.STYLESHEET.contains(mimeType)) {
                markStylesheetAsRendered
                    .invoke(null, facesContext, resourceKey.getLibraryName(), resourceKey.getResourceName());
            } else if (MimeType.SCRIPT.contains(mimeType)) {
                markScriptAsRendered.invoke(null, facesContext, resourceKey.getLibraryName(), resourceKey.getResourceName());
            }
        } catch (Exception e) {
            ResourceTrackerForMyFaces.handleException(e);
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

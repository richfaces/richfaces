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

/**
 * Wraps known implementations of {@link ExternalResourceTracker} and decides which one to choose in runtime
 *
 * @author Lukas Fryc
 */
public class ExternalResourceTrackerWrapper implements ExternalResourceTracker {

    private static final String MYFACES_RESOURCE_UTILS_CLASS = "org.apache.myfaces.shared.renderkit.html.util.ResourceUtils";

    private AtomicReference<ExternalResourceTracker> externalResourceTracker = new AtomicReference<ExternalResourceTracker>();

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.resource.external.ExternalResourceTracker#isResourceRenderered(javax.faces.context.FacesContext,
     * org.richfaces.resource.ResourceKey)
     */
    @Override
    public boolean isResourceRenderered(FacesContext facesContext, ResourceKey resourceKey) {
        return getWrapped().isResourceRenderered(facesContext, resourceKey);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.resource.external.ExternalResourceTracker#markResourceRendered(javax.faces.context.FacesContext,
     * org.richfaces.resource.ResourceKey)
     */
    @Override
    public void markResourceRendered(FacesContext facesContext, ResourceKey resourceKey) {
        getWrapped().markResourceRendered(facesContext, resourceKey);
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
        getWrapped().markExternalResourceRendered(facesContext, resource);
    }

    private ExternalResourceTracker getWrapped() {
        ExternalResourceTracker tracker = externalResourceTracker.get();
        if (tracker == null) {
            try {
                this.getClass().getClassLoader().loadClass(MYFACES_RESOURCE_UTILS_CLASS);

                externalResourceTracker.compareAndSet(null, new MyFacesExternalResourceTracker());
            } catch (Exception e) {
                externalResourceTracker.compareAndSet(null, new MojarraExternalResourceTracker());
            }
            tracker = externalResourceTracker.get();
        }

        return tracker;
    }
}

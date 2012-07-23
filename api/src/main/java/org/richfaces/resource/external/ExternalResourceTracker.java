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

import javax.faces.context.FacesContext;

import org.richfaces.resource.ResourceKey;

/**
 * Tracks what external resources are renderered to the page
 *
 * @author Lukas Fryc
 *
 */
public interface ExternalResourceTracker {

    /**
     * Returns true if given resource has been already renderered to the page
     *
     * @return true if given resource has been already renderered to the page; false otherwise
     */
    boolean isResourceRenderered(FacesContext facesContext, ResourceKey resourceKey);

    /**
     * Marks given resource as renderered
     */
    void markResourceRendered(FacesContext facesContext, ResourceKey resourceKey);

    /**
     * Marks given {@link ExternalResource} and all other resources with same external location (
     * {@link ExternalResource#getExternalLocation()}) as rendered
     */
    void markExternalResourceRendered(FacesContext facesContext, ExternalResource resourceKey);
}

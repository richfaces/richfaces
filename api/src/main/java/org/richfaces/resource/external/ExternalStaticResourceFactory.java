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

import java.util.Set;

import javax.faces.context.FacesContext;

import org.richfaces.resource.ResourceKey;

/**
 * Creates resources pointing outside of JSF resource handler.
 *
 * @author Lukas Fryc
 */
public interface ExternalStaticResourceFactory {

    /**
     * Creates external resource
     *
     * @param facesContext {@link FacesContext}
     * @param resourceKey the resource key
     * @return external resource for given resource key
     */
    ExternalResource createResource(FacesContext facesContext, ResourceKey resourceKey);

    /**
     * Returns all resource keys which points to the given external resource location
     *
     * @param resourceLocation the external resource location
     * @return all resource keys which points to the given external resource location
     */
    Set<ResourceKey> getResourcesForLocation(String resourceLocation);
}

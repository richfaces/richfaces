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
package org.richfaces.resource.optimizer;

import java.util.Collection;

import org.richfaces.renderkit.html.ResourceLibraryRenderer;
import org.richfaces.resource.ResourceKey;
import org.richfaces.resource.ResourceLibrary;
import org.richfaces.resource.ResourceLibraryFactory;
import org.richfaces.services.ServiceTracker;

import com.google.common.collect.Sets;

/**
 * Expands resource libraries (.reslib).
 *
 * @author <a href="http://community.jboss.org/people/lfryc">Lukas Fryc</a>
 */
public class ResourceLibraryExpander {

    /**
     * Expands resource libraries (.reslib) in collection of resource keys.
     *
     * @param resources resource keys to be expanded
     * @return collection with all resource libraries expanded to particular resource keys (keeps ordering)
     */
    public Collection<ResourceKey> expandResourceLibraries(Collection<ResourceKey> resources) {
        ResourceLibraryFactory factory = ServiceTracker.getService(ResourceLibraryFactory.class);
        Collection<ResourceKey> expandedResources = Sets.newLinkedHashSet();

        for (ResourceKey resourceKey : resources) {
            if (resourceKey.getResourceName().endsWith(ResourceLibraryRenderer.RESOURCE_LIBRARY_EXTENSION)) {

                String libraryName = resourceKey.getLibraryName();
                String resourceName = resourceKey.getResourceName().substring(0,
                        resourceKey.getResourceName().length() - ResourceLibraryRenderer.RESOURCE_LIBRARY_EXTENSION.length());
                ResourceLibrary resourceLibrary = factory.getResourceLibrary(resourceName, libraryName);

                if (resourceLibrary == null) {
                    throw new IllegalArgumentException("Resource library is null: " + libraryName + ":" + resourceName);
                }

                for (ResourceKey expandedKey : resourceLibrary.getResources()) {
                    expandedResources.add(expandedKey);
                }

            } else {
                expandedResources.add(resourceKey);
            }
        }

        return expandedResources;
    }
}

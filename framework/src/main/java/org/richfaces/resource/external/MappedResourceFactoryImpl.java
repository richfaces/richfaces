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

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.faces.application.Resource;
import javax.faces.context.FacesContext;

import org.richfaces.resource.ResourceKey;
import org.richfaces.resource.URLResource;
import org.richfaces.resource.mapping.LibraryResourceMapper;
import org.richfaces.resource.mapping.PropertiesResourceMapper;
import org.richfaces.resource.mapping.ResourceAggregator;
import org.richfaces.resource.mapping.ResourceMapper;
import org.richfaces.resource.mapping.ResourceMapping;
import org.richfaces.resource.mapping.ResourcePath;
import org.richfaces.services.Initializable;
import org.richfaces.services.ServiceLoader;
import org.richfaces.services.ServiceUtils;

import com.google.common.collect.Sets;

/**
 * Creates resources pointing outside of standard JSF resource handler.
 *
 * @author Lukas Fryc
 */
public class MappedResourceFactoryImpl implements MappedResourceFactory, Initializable {

    private List<ResourceMapper> mappers;

    private List<String> resourceLibrariesToMap = Arrays.asList("org.richfaces", "bootstrap", "font-awesome", "com.jqueryui",
            "org.richfaces.ckeditor");

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.application.Initializable#init()
     */
    @Override
    public void init() {
        List<ResourceMapper> mappers = new LinkedList<ResourceMapper>();

        // default mappers
        mappers.add(new PropertiesResourceMapper());
        for (String resourceLibrary : resourceLibrariesToMap) {
            mappers.add(new LibraryResourceMapper(resourceLibrary));
        }

        // user-defined mappers
        mappers.addAll(ServiceLoader.loadServices(ResourceMapper.class));

        ServiceUtils.sortByPriority(mappers);
        ServiceUtils.initialize(mappers);

        this.mappers = Collections.unmodifiableList(mappers);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.application.Initializable#release()
     */
    @Override
    public void release() {
        ServiceUtils.release(mappers);
        mappers = null;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.resource.external.ExternalStaticResourceFactory#createResource(javax.faces.context.FacesContext,
     * org.richfaces.resource.ResourceKey)
     */
    @Override
    public Resource createResource(FacesContext context, ResourceKey resourceKey) {
        for (ResourceMapper mapper : mappers) {
            ResourceMapping mapping = mapper.mapResource(resourceKey);

            if (mapping != null) {
                ResourcePath path = mapping.getResourcePath(context);
                return new URLResource(path);
            }
        }

        return null;
    }

    /*
     * (non-Javadoc)
     * @see org.richfaces.resource.external.MappedResourceFactory#getAggregatedResources(org.richfaces.resource.mapping.ResourcePath)
     */
    @Override
    public Set<ResourceKey> getAggregatedResources(ResourcePath resourcePath) {
        Set<ResourceKey> aggregatedResources = Sets.newLinkedHashSet();

        for (ResourceMapper mapper : mappers) {
            if (mapper instanceof ResourceAggregator) {
                aggregatedResources.addAll(((ResourceAggregator) mapper).getAggregatedResources(resourcePath));
            }
        }

        return aggregatedResources;
    }
}

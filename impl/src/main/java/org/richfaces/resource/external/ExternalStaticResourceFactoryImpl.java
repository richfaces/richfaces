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

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.faces.context.FacesContext;

import org.richfaces.application.Initializable;
import org.richfaces.log.Logger;
import org.richfaces.log.RichfacesLogger;
import org.richfaces.resource.ResourceKey;
import org.richfaces.resource.ResourceMappingConfiguration;
import org.richfaces.resource.ResourceMappingFeature;
import org.richfaces.util.PropertiesUtil;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

/**
 * Creates resources pointing outside of JSF resource handler.
 *
 * @author Lukas Fryc
 */
public class ExternalStaticResourceFactoryImpl implements ExternalStaticResourceFactory, Initializable {

    private static final Logger LOGGER = RichfacesLogger.RESOURCE.getLogger();

    private Map<ResourceKey, ExternalMapping> externalStaticResources;

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.application.Initializable#init()
     */
    @Override
    public void init() {
        final Map<ResourceKey, ExternalMapping> result = Maps.newHashMap();

        final List<String> mappingFiles = ResourceMappingFeature.getMappingFiles();
        for (String mappingFile : mappingFiles) {
            if (classpathResourceExistsForLocation(mappingFile)) {
                for (Entry<String, String> entry : PropertiesUtil.loadProperties(mappingFile).entrySet()) {
                    final ResourceKey resourceKey = ResourceKey.create(entry.getKey());
                    final String resourceLocation = entry.getValue();
                    result.put(resourceKey, new ExternalMapping(resourceKey, resourceLocation));
                }
            } else {
                if (!isDefaultStaticResourceMappingLocation(mappingFile)) {
                    LOGGER.warn("Resource mapping is configured to load non-existent resource: '" + mappingFile + "'");
                }
            }
        }

        externalStaticResources = result;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.resource.external.ExternalStaticResourceFactory#createResource(javax.faces.context.FacesContext,
     * org.richfaces.resource.ResourceKey)
     */
    @Override
    public ExternalResource createResource(FacesContext facesContext, ResourceKey resourceKey) {
        ExternalMapping mapping = externalStaticResources.get(resourceKey);

        if (mapping == null) {
            return null;
        }

        return mapping.getResource(facesContext);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.resource.external.ExternalStaticResourceFactory#getResourcesForLocation(java.lang.String)
     */
    @Override
    public Set<ResourceKey> getResourcesForLocation(String resourceLocation) {
        if (externalStaticResources == null) {
            LOGGER.error("factory hasn't been initialized yet");
            return Collections.emptySet();
        }

        Set<ResourceKey> result = Sets.newHashSet();
        for (Entry<ResourceKey, ExternalMapping> entry : externalStaticResources.entrySet()) {
            final ResourceKey resourceKey = entry.getKey();
            final ExternalMapping mapping = entry.getValue();

            if (mapping.getResourceLocation().equals(resourceLocation)) {
                result.add(resourceKey);
            }
        }

        return result;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.application.Initializable#release()
     */
    @Override
    public void release() {
        externalStaticResources.clear();
        externalStaticResources = null;
    }

    /**
     * Checks whenever given class-path resource exists
     */
    private boolean classpathResourceExistsForLocation(String location) {
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        return contextClassLoader.getResource(location) != null;
    }

    /**
     * Checks whenever given location is default static resource mapping location
     */
    private boolean isDefaultStaticResourceMappingLocation(String location) {
        return ResourceMappingConfiguration.DEFAULT_STATIC_RESOURCE_MAPPING_LOCATION.equals(location);
    }
}

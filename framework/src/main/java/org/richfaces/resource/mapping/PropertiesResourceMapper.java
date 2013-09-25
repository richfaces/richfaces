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
package org.richfaces.resource.mapping;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.faces.context.FacesContext;

import org.richfaces.log.Logger;
import org.richfaces.log.RichfacesLogger;
import org.richfaces.resource.ResourceKey;
import org.richfaces.resource.ResourceSkinUtils;
import org.richfaces.services.Initializable;
import org.richfaces.services.Prioritizable;
import org.richfaces.servlet.ResourceServlet;
import org.richfaces.skin.SkinFactory;
import org.richfaces.util.PropertiesUtil;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

/**
 * Maps resources according to properties configuration of Resource Mapping and Resource Optimization
 *
 * @author Lukas Fryc
 */
public class PropertiesResourceMapper implements ResourceMapper, ResourceAggregator, Initializable, Prioritizable {

    private static final Logger LOGGER = RichfacesLogger.RESOURCE.getLogger();

    private Map<ResourceKey, ResourceMapping> mappings;

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.services.Initializable#init()
     */
    @Override
    public void init() {
        final Map<ResourceKey, ResourceMapping> result = Maps.newHashMap();

        final List<String> mappingFiles = PropertiesMappingConfiguration.getMappingFiles();
        for (String mappingFile : mappingFiles) {
            if (classpathResourceExistsForLocation(mappingFile)) {
                for (Entry<String, String> entry : PropertiesUtil.loadProperties(mappingFile).entrySet()) {
                    final ResourceKey resourceKey = ResourceKey.create(entry.getKey());
                    final ResourcePath resourceLocation = new ResourcePath(entry.getValue());
                    result.put(resourceKey, new LocationBasedResourceMapping(resourceLocation));
                }
            } else {
                if (!isDefaultStaticResourceMappingLocation(mappingFile)) {
                    LOGGER.warn("Resource mapping is configured to load non-existent resource: '" + mappingFile + "'");
                }
            }
        }

        mappings = result;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.services.Initializable#release()
     */
    @Override
    public void release() {
        mappings.clear();
        mappings = null;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.resource.mapping.ResourceMapper#mapResource(org.richfaces.resource.ResourceKey)
     */
    @Override
    public ResourceMapping mapResource(ResourceKey resourceKey) {
        return mappings.get(resourceKey);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.richfaces.resource.mapping.ResourceAggregator#getAggregatedResources(org.richfaces.resource.mapping.ResourcePath)
     */
    @Override
    public Set<ResourceKey> getAggregatedResources(ResourcePath resourcePath) {
        FacesContext context = FacesContext.getCurrentInstance();

        Set<ResourceKey> result = Sets.newHashSet();
        for (Entry<ResourceKey, ResourceMapping> entry : mappings.entrySet()) {
            final ResourceKey resourceKey = entry.getKey();
            final ResourceMapping mapping = entry.getValue();

            if (mapping.getResourcePath(context).equals(resourcePath)) {
                result.add(resourceKey);
            }
        }

        return result;
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
        return PropertiesMappingConfiguration.DEFAULT_STATIC_RESOURCE_MAPPING_LOCATION.equals(location);
    }

    /**
     * Maps resources according to properties file configuration to absolute URL resources or {@link ResourceServlet}-mapped resources.
     *
     * Configured URLs can be parametrized by skin name: %skin%
     *
     * @author Lukas Fryc
     */
    private static class LocationBasedResourceMapping implements ResourceMapping {

        private ResourcePath resourcePath;

        public LocationBasedResourceMapping(ResourcePath resourcePath) {
            this.resourcePath = resourcePath;
        }

        @Override
        public ResourcePath getResourcePath(FacesContext context) {
            if (resourcePath.isAbsoluteURL()) {
                // checks that provided resourceLocation is valid URL = then it is considered absolute URL
                return resourcePath;
            } else {
                // otherwise, create ResourceServlet-relative URL
                return createRequestPathRelativeLocation();
            }
        }

        private ResourcePath createRequestPathRelativeLocation() {
            FacesContext context = FacesContext.getCurrentInstance();

            ResourcePath location = resourcePath;

            if (ResourceSkinUtils.isSkinDependent(resourcePath.toExternalForm())) {
                SkinFactory skinFactory = SkinFactory.getInstance(context);
                String skinName = skinFactory.getSkin(context).getName();
                location = new ResourcePath(ResourceSkinUtils.evaluateSkinInPath(resourcePath.toExternalForm(), skinName));
            }

            return new ResourceServletMapping(location).getResourcePath(context);
        }

    }

    @Override
    public int getPriority() {
        return 100;
    }

}

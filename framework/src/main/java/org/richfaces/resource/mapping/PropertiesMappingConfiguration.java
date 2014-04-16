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

import java.util.Arrays;
import java.util.List;

import javax.faces.context.FacesContext;

import org.richfaces.application.configuration.ConfigurationService;
import org.richfaces.application.configuration.ConfigurationServiceHelper;
import org.richfaces.configuration.CoreConfiguration;

import com.google.common.collect.Lists;

/**
 * <p>
 * Utility class for retrieving configuration options of resource mapping feature
 * </p>
 *
 *
 * @author <a href="http://community.jboss.org/people/lfryc">Lukas Fryc</a>
 */
final class PropertiesMappingConfiguration {

    public static final String DEFAULT_STATIC_RESOURCE_MAPPING_LOCATION = "META-INF/richfaces/static-resource-mappings.properties";

    /**
     * Returns configured location for resource mapping
     *
     * @return configured location for resource mapping
     */
    static String getLocation() {
        return getConfiguration(CoreConfiguration.Items.resourceMappingLocation);
    }

    /**
     * Returns locations of static resource mapping configuration files for current application stage.
     *
     * @return locations of static resource mapping configuration files for current application stage
     */
    static List<String> getMappingFiles() {
        List<String> mappingFiles = Lists.newLinkedList();

        if (ResourceLoadingOptimizationConfiguration.isEnabled()) {
            mappingFiles.add(ResourceLoadingOptimizationConfiguration.getResourceLoadingSpecificMappingFile());
        }
        mappingFiles.add(getDefaultMappingFile());
        mappingFiles.addAll(getUserConfiguredMappingFile());

        return mappingFiles;
    }

    private static String getDefaultMappingFile() {
        return PropertiesMappingConfiguration.DEFAULT_STATIC_RESOURCE_MAPPING_LOCATION;
    }

    private static List<String> getUserConfiguredMappingFile() {
        String configured = getConfiguration(CoreConfiguration.Items.resourceMappingFile);
        if (configured == null) {
            return Arrays.asList();
        }
        return Arrays.asList(configured.split(","));
    }

    /**
     * Obtains configuration from {@link ConfigurationService}.
     */
    static String getConfiguration(CoreConfiguration.Items configurationItem) {
        return ConfigurationServiceHelper.getStringConfigurationValue(FacesContext.getCurrentInstance(), configurationItem);
    }
}

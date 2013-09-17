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

import java.util.Map;

import javax.faces.context.FacesContext;

import org.richfaces.configuration.ConfigurationService;
import org.richfaces.configuration.ConfigurationServiceHelper;
import org.richfaces.configuration.CoreConfiguration;
import org.richfaces.log.Logger;
import org.richfaces.log.RichfacesLogger;

/**
 * <p>
 * Utility class for retrieving configuration options of resource mapping feature
 * </p>
 *
 *
 * @author <a href="http://community.jboss.org/people/lfryc">Lukas Fryc</a>
 */
public final class ResourceMappingConfiguration {

    public static final String DEFAULT_STATIC_RESOURCE_MAPPING_LOCATION = "META-INF/richfaces/static-resource-mappings.properties";
    private static final Logger LOG = RichfacesLogger.CONFIG.getLogger();
    private static final String STATIC_RESOURCE_DEPRECATION_LOGGED = "org.richfaces.staticResourceLocation.deprecation.logged";

    /**
     * Returns configured location for resource mapping
     *
     * @return configured location for resource mapping
     */
    static String getLocation() {
        if (getStaticResourceLocation() != null) {
            return getStaticResourceLocation();
        }
        return getConfiguration(CoreConfiguration.Items.resourceMappingLocation);
    }

    /**
     * Returns resource mapping configuration file location
     *
     * @return resource mapping configuration file location
     */
    static String getResourceMappingFile() {
        if (getStaticResourceLocation() != null) {
            return DEFAULT_STATIC_RESOURCE_MAPPING_LOCATION;
        }
        return getConfiguration(CoreConfiguration.Items.resourceMappingFile);
    }

    /**
     * Returns static resource location configuration from RichFaces 4.0.
     *
     * <b>Deprecated in 4.1</b>
     *
     * @return static resource location configuration from RichFaces 4.0.
     */
    @SuppressWarnings("deprecation")
    private static String getStaticResourceLocation() {
        String staticResourceLocation = getConfiguration(CoreConfiguration.Items.staticResourceLocation);
        logDeprecation(staticResourceLocation);
        return staticResourceLocation;
    }

    /**
     * Log only once that resource location has been deprecated.
     *
     * @param staticResourceLocation
     */
    private static void logDeprecation(String staticResourceLocation) {
        Map<String, Object> applicationMap = FacesContext.getCurrentInstance().getExternalContext().getApplicationMap();
        if (staticResourceLocation != null && !applicationMap.containsKey(STATIC_RESOURCE_DEPRECATION_LOGGED)) {
            applicationMap.put(STATIC_RESOURCE_DEPRECATION_LOGGED, Boolean.TRUE);
            LOG.warn("Context-param 'org.richfaces.staticResourceLocation' is deprecated, it was replaced by 'org.richfaces.resourceMapping.enabled', 'org.richfaces.resourceMapping.location' and 'org.richfaces.resourceMapping.mappingFile'");
        }
    }

    /**
     * Obtains configuration from {@link ConfigurationService}.
     */
    static String getConfiguration(CoreConfiguration.Items configurationItem) {
        return ConfigurationServiceHelper.getStringConfigurationValue(FacesContext.getCurrentInstance(), configurationItem);
    }
}

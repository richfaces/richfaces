/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2010, Red Hat, Inc., and individual contributors
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
package org.richfaces.resource;

import java.util.Arrays;
import java.util.List;

import javax.el.ValueExpression;
import javax.faces.context.FacesContext;

import org.richfaces.el.util.ELUtils;

import com.google.common.collect.Lists;

/**
 * <p>
 * Determines resource mappings and request path for mapped resources.
 * </p>
 *
 * @author <a href="http://community.jboss.org/people/lfryc">Lukas Fryc</a>
 */
public final class ResourceMappingFeature {

    private ResourceMappingFeature() {
    }

    static final String DEFAULT_LOCATION = "#{facesContext.externalContext.requestContextPath}/org.richfaces.resources/javax.faces.resource/#{resourceLocation}";

    /**
     * Returns locations of static resource mapping configuration files for current application stage.
     *
     * @return locations of static resource mapping configuration files for current application stage
     */
    public static List<String> getMappingFiles() {
        List<String> mappingFiles = Lists.newLinkedList();

        if (ResourceLoadingOptimization.isEnabled()) {
            mappingFiles.add(ResourceLoadingOptimization.getResourceLoadingSpecificMappingFile());
        }
        mappingFiles.add(getDefaultMappingFile());
        mappingFiles.addAll(getUserConfiguredMappingFile());

        return mappingFiles;
    }

    private static String getDefaultMappingFile() {
        return ResourceMappingConfiguration.DEFAULT_STATIC_RESOURCE_MAPPING_LOCATION;
    }

    private static List<String> getUserConfiguredMappingFile() {
        String configured = ResourceMappingConfiguration.getResourceMappingFile();
        if (configured == null) {
            return Arrays.asList();
        }
        return Arrays.asList(configured.split(","));
    }

    /**
     * Returns the configured location of static resources as string evaluated against EL expressions in current context, either
     * from configuration option or predefined location corresponding to current application stage.
     *
     * @return the configured location of static resources as string evaluated against EL expressions in current context
     */
    public static String getLocation() {
        ValueExpression mappingLocationExpression = ELUtils.createValueExpression(getLocationAsExpression());
        return mappingLocationExpression.getValue(FacesContext.getCurrentInstance().getELContext()).toString();
    }

    /**
     * Returns the configured location of static resources as string with EL expressions, either from configuration option or
     * predefined location corresponding to current application stage.
     *
     * @return the configured location of static resources as string with EL expressions
     */
    private static String getLocationAsExpression() {
        String location = ResourceMappingConfiguration.getLocation();
        if (location == null) {
            return DEFAULT_LOCATION;
        }
        return location;
    }
}

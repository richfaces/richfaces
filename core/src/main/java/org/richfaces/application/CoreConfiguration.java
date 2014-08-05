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
package org.richfaces.application;

import org.richfaces.annotation.Experimental;
import org.richfaces.application.configuration.ConfigurationItem;
import org.richfaces.application.configuration.ConfigurationItemSource;
import org.richfaces.application.configuration.ConfigurationItemsBundle;

/**
 * @author Nick Belaevski
 *
 */
public final class CoreConfiguration {
    public static final String SKIN_PARAM_NAME = "org.richfaces.skin";
    public static final String BASE_SKIN_PARAM_NAME = "org.richfaces.baseSkin";
    public static final String RESOURCES_CACHE_SIZE_PARAM_NAME = "org.richfaces.resourceCacheSize";

    private CoreConfiguration() {
    }

    public enum Items {
        @ConfigurationItem(defaultValue = "true", names = "org.richfaces.enableControlSkinning")
        standardControlsSkinning,
        @ConfigurationItem(defaultValue = "false", names = "org.richfaces.enableControlSkinningClasses")
        standardControlsSkinningClasses,
        /**
         * Name of web application init parameter for current skin . Can be simple String for non-modified name, or
         * EL-expression for calculate current skin. If EL evaluated to <code>String</code> - used as skin name, if to instance
         * of {@link org.richfaces.skin.Skin } - used this instance. by default - "org.richfaces.skin"
         */
        @ConfigurationItem(names = SKIN_PARAM_NAME)
        skin,
        @ConfigurationItem(names = BASE_SKIN_PARAM_NAME)
        baseSkin,
        @ConfigurationItem(defaultValue = "86400" /* 24 * 60 * 60 */, names = "org.richfaces.resourceDefaultTTL", literal = true)
        resourcesTTL,
        @ConfigurationItem(defaultValue = "512", names = RESOURCES_CACHE_SIZE_PARAM_NAME, literal = true)
        resourcesCacheSize,
        @ConfigurationItem(names = "org.richfaces.resourceDefaultVersion")
        resourcesDefaultVersion,
        @ConfigurationItem(names = "org.richfaces.cache.LRU_MAP_CACHE_SIZE", literal = true)
        lruMapCacheSize,
        @ConfigurationItem(names = "org.richfaces.resourceMapping.location", literal = true)
        resourceMappingLocation,
        @ConfigurationItem(names = "org.richfaces.resourceMapping.mappingFile")
        resourceMappingFile,
        @ConfigurationItem(defaultValue = "false", names = { "org.richfaces.resourceOptimization.enabled", "org.richfaces.resourceMapping.enabled" }, literal = true)
        resourceOptimizationEnabled,
        @ConfigurationItem(defaultValue = "Production,SystemTest", names = { "org.richfaces.resourceOptimization.compressionStages", "org.richfaces.resourceMapping.compressedStages" }, literal = true)
        resourceOptimizationCompressionStages,
        @ConfigurationItem(defaultValue = "All", names = { "org.richfaces.resourceOptimization.packagingStages", "org.richfaces.resourceMapping.packedStages" }, literal = true)
        resourceOptimizationPackagingStages,
        @ConfigurationItem(defaultValue = "true", names = "org.richfaces.executeAWTInitializer", literal = true)
        executeAWTInitializer,
        @ConfigurationItem(names = "org.richfaces.push.handlerMapping", literal = true)
        pushHandlerMapping,
        @ConfigurationItem(defaultValue = "/ConnectionFactory", names = "org.richfaces.push.jms.connectionFactory")
        pushJMSConnectionFactory,
        @ConfigurationItem(defaultValue = "", names = "org.richfaces.push.jms.enabled")
        pushJMSEnabled,
        @ConfigurationItem(defaultValue = "/topic", names = "org.richfaces.push.jms.topicsNamespace")
        pushJMSTopicsNamespace,
        @ConfigurationItem(defaultValue = "", names = "org.richfaces.push.jms.connectionUsername")
        pushJMSConnectionUsername,
        @ConfigurationItem(defaultValue = "", names = "org.richfaces.push.jms.connectionUsername", source = ConfigurationItemSource.webEnvironmentEntry)
        pushJMSConnectionUsernameEnvRef,
        @ConfigurationItem(defaultValue = "", names = "org.richfaces.push.jms.connectionPassword")
        pushJMSConnectionPassword,
        @ConfigurationItem(defaultValue = "", names = "org.richfaces.push.jms.connectionPassword", source = ConfigurationItemSource.webEnvironmentEntry)
        pushJMSConnectionPasswordEnvRef,
        @ConfigurationItem(defaultValue = "false", names="org.richfaces.push.initializeOnStartup")
        pushInitializePushContextOnStartup,
        /**
         * The interval of Push session invalidation when inactive (experimental configuration option: RF-12370)
         */
        @Experimental
        @ConfigurationItem(defaultValue = "300000", names="org.richfaces.push.session.maxInactiveInterval")
        pushSessionMaxInactiveInterval,
        /**
         * Whether or not to display the built in sort controls when the sorting attributes are present on a column
         */
        @ConfigurationItem(defaultValue = "true", names = "org.richfaces.builtin.sort.enabled")
        builtInSortControlsEnabled,
        /**
         * Whether or not to display the built in filter controls when the filtering attributes are present on a column
         */
        @ConfigurationItem(defaultValue = "true", names = "org.richfaces.builtin.filter.enabled")
        builtInFilterControlsEnabled
    }

    @ConfigurationItemsBundle(propertiesFile = "org/richfaces/push.properties")
    public enum PushPropertiesItems {

        @ConfigurationItem(names = "jms.connectionFactory")
        pushPropertiesJMSConnectionFactory,
        @ConfigurationItem(names = "jms.topicsNamespace")
        pushPropertiesJMSTopicsNamespace,
        @ConfigurationItem(names = "jms.connectionUsername")
        pushPropertiesJMSConnectionUsername,
        @ConfigurationItem(names = "jms.connectionPassword")
        pushPropertiesJMSConnectionPassword

    }
}

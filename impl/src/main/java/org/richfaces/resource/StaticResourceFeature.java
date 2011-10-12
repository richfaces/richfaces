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

import javax.el.ValueExpression;
import javax.faces.application.ProjectStage;
import javax.faces.context.FacesContext;

import org.richfaces.application.CoreConfiguration;
import org.richfaces.application.configuration.ConfigurationService;
import org.richfaces.application.configuration.ConfigurationServiceHelper;
import org.richfaces.el.util.ELUtils;

/**
 * <p>
 * Determines the mappings and request path for static resources pre-generated and stored on the class-path.
 * </p>
 *
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 */
public enum StaticResourceFeature {

    /**
     * Packaging of selected static resources of certain types (CSS/JS)
     */
    Packed(CoreConfiguration.Items.staticResourcePackedPhases),
    /**
     * Compression of static resources which is supported (CSS/JS)
     */
    Compressed(CoreConfiguration.Items.staticResourceCompressedPhases);

    private static final String FEATURES_PLACEHOLDER = "%Features%";
    private static final String LOCATION = "#{facesContext.externalContext.requestContextPath}/org.richfaces.resources/javax.faces.resource/org.richfaces.staticResource/#{a4j.version.implementationVersion}/%Features%/#{resourceLocation}";
    private static final String MAPPING = "META-INF/richfaces/staticResourceMapping/%Features%.properties";

    private CoreConfiguration.Items associatedConfiguration;

    private StaticResourceFeature(CoreConfiguration.Items associatedConfiguration) {
        this.associatedConfiguration = associatedConfiguration;
    }

    /**
     * Returns true if static resource serving is enabled in configuration.
     *
     * @return true if static resource serving is enabled in configuration; false otherwise
     */
    public static boolean isStaticResourceServingEnabled() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        boolean staticResourceServing = ConfigurationServiceHelper.getBooleanConfigurationValue(facesContext,
                CoreConfiguration.Items.staticResourceServing);
        return staticResourceServing;
    }

    /**
     * Returns location of static resource mapping configuration file for current application stage.
     *
     * @return location of static resource mapping configuration file for current application stage
     */
    public static String getStaticMappingLocation() {
        checkStaticResourceServingEnabled();
        String mapping = getConfiguration(CoreConfiguration.Items.staticResourceMappingLocation);
        if (mapping == null || "".equals(mapping)) {
            mapping = MAPPING;
        }
        return format(mapping, getInfix());
    }

    /**
     * Returns the configured location of static resources as string evaluated against EL expressions in current context, either
     * from configuration option or predefined location corresponding to current application stage.
     *
     * @return the configured location of static resources as string evaluated against EL expressions in current context
     */
    public static String getStaticallyMappedRequestPath() {
        checkStaticResourceServingEnabled();
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ValueExpression mappingLocationExpression = ELUtils.createValueExpression(getStaticallyMappedRequestPathExpression());
        return mappingLocationExpression.getValue(facesContext.getELContext()).toString();
    }

    /**
     * Returns the configured location of static resources as string with EL expressions, either from configuration option or
     * predefined location corresponding to current application stage.
     *
     * @return the configured location of static resources as string with EL expressions
     */
    private static String getStaticallyMappedRequestPathExpression() {
        String location = getConfiguration(CoreConfiguration.Items.staticResourceLocation);
        if (location == null || "".equals(location)) {
            location = LOCATION;
        }
        return format(location, getInfix());
    }

    /**
     * <p>
     * Determines if the feature is enabled in current project stage.
     * </p>
     *
     * <p>
     * Can be configured with either any {@link ProjectStage} items separated by comma, or with keyword 'All' or 'None'
     * representing all application stages or no stage respectivelly.
     * </p>
     *
     * @return true if the feature is enabled in current stage; false otherwise
     */
    private boolean isEnabled() {
        String configuredPhases = getConfiguration(this.associatedConfiguration);
        if (configuredPhases == null) {
            return false;
        }
        ProjectStage projectStage = FacesContext.getCurrentInstance().getApplication().getProjectStage();
        return "All".equals(configuredPhases) || configuredPhases.matches("(^|.*,)" + projectStage.toString() + "($|,.*)");
    }

    /**
     * <p>
     * Validates that static resource feature is enabled.
     * </p>
     *
     * @throws IllegalStateException when static resource serving feature is not enabled
     */
    private static void checkStaticResourceServingEnabled() {
        if (!isStaticResourceServingEnabled()) {
            throw new IllegalStateException("Static resource serving needs to be enabled first to use static resources");
        }
    }

    /**
     * Obtains configuration from {@link ConfigurationService}.
     */
    private static String getConfiguration(CoreConfiguration.Items configurationItem) {
        return ConfigurationServiceHelper.getStringConfigurationValue(FacesContext.getCurrentInstance(), configurationItem);
    }

    /**
     * <p>
     * Constructs infix composed from items of enumeration of features which are turned on in current {@link ProjectStage}.
     * </p>
     *
     * <p>
     * If no feature is enabled, infix "Static" is returned.
     * </p>
     *
     * <p>
     * Items are composed in order in which are specified in {@link StaticResourceFeature} enumeration.
     * </p>
     *
     * @return infix composed from items of enumeration of features which are turned on in current {@link ProjectStage};
     *         "Static" is returned when no other feature enabled
     */
    private static String getInfix() {
        StringBuffer affix = new StringBuffer();
        for (StaticResourceFeature feature : StaticResourceFeature.values()) {
            if (feature.isEnabled()) {
                affix.append(feature.toString());
            }
        }
        if (affix.length() == 0) {
            return "Static";
        }
        return affix.toString();
    }

    private static String format(String string, String message) {
        return string.replace(FEATURES_PLACEHOLDER, message);
    }
}

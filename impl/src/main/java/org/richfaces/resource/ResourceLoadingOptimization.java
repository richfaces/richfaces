package org.richfaces.resource;

import javax.faces.application.ProjectStage;
import javax.faces.context.FacesContext;

import org.richfaces.application.CoreConfiguration;
import org.richfaces.application.configuration.ConfigurationServiceHelper;

/**
 * <p>
 * Determines whenever RichFaces resources should be loaded optimized and how should be optimized.
 * </p>
 *
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 */
public enum ResourceLoadingOptimization {

    /**
     * Packaging of selected static resources of certain types (CSS/JS)
     */
    Packed(CoreConfiguration.Items.resourceLoadingPackagingStages),
    /**
     * Compression of static resources which is supported (CSS/JS)
     */
    Compressed(CoreConfiguration.Items.resourceLoadingCompressionStages);

    private static final String STATIC_RESOURCES_FEATURE_NAME = "Static";
    private static final String FEATURES_PLACEHOLDER = "%Features%";
    private static final String DEFAULT_FEATURE_MAPPING_FILE = "META-INF/richfaces/staticResourceMapping/%Features%.properties";

    private CoreConfiguration.Items associatedConfiguration;

    private ResourceLoadingOptimization(CoreConfiguration.Items associatedConfiguration) {
        this.associatedConfiguration = associatedConfiguration;
    }

    static String getResourceLoadingSpecificMappingFile() {
        return format(DEFAULT_FEATURE_MAPPING_FILE, getEnabledFeatures());
    }

    /**
     * Returns true if resource loading optimization feature is enabled in configuration.
     *
     * @return true if resource loading optimization feature is enabled in configuration; false otherwise
     */
    static boolean isEnabled() {
        return ConfigurationServiceHelper.getBooleanConfigurationValue(FacesContext.getCurrentInstance(),
                CoreConfiguration.Items.resourceLoadingOptimization);
    }

    /**
     * <p>
     * Determines if the feature is enabled in current project stage.
     * </p>
     *
     * <p>
     * Can be configured with either any {@link ProjectStage} items separated by comma, or with keyword 'All' or 'None'
     * representing all application stages or no stage respectively.
     * </p>
     *
     * @return true if the feature is enabled in current stage; false otherwise
     */
    private boolean enabled() {
        String configuredPhases = ResourceMappingConfiguration.getConfiguration(associatedConfiguration);
        if (configuredPhases == null) {
            return false;
        }
        ProjectStage projectStage = FacesContext.getCurrentInstance().getApplication().getProjectStage();
        return "All".equals(configuredPhases) || configuredPhases.matches("(^|.*,)" + projectStage.toString() + "($|,.*)");
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
     * Items are composed in order in which are specified in {@link ResourceMappingFeature} enumeration.
     * </p>
     *
     * @return infix composed from items of enumeration of features which are turned on in current {@link ProjectStage};
     *         "Static" is returned when no other feature enabled
     */
    private static String getEnabledFeatures() {
        StringBuffer affix = new StringBuffer();
        for (ResourceLoadingOptimization feature : ResourceLoadingOptimization.values()) {
            if (feature.enabled()) {
                affix.append(feature.toString());
            }
        }
        if (affix.length() == 0) {
            return STATIC_RESOURCES_FEATURE_NAME;
        }
        return affix.toString();
    }

    private static String format(String string, String message) {
        return string.replace(FEATURES_PLACEHOLDER, message);
    }
}

package org.richfaces.arquillian.configuration;

import java.lang.annotation.Annotation;

import org.jboss.arquillian.config.descriptor.api.ArquillianDescriptor;
import org.jboss.arquillian.drone.configuration.ConfigurationMapper;
import org.jboss.arquillian.drone.spi.DroneConfiguration;

public class FundamentalTestConfiguration implements DroneConfiguration<FundamentalTestConfiguration> {

    private String richfacesVersion;
    private Boolean servletContainerSetup;
    private String currentBuildRichfacesVersion = "4.3.1-SNAPSHOT";
    private String jsfImplementation;
    private String containerHome;
    private String containerDistribution;
    private String containerConfiguration;
    private Boolean containerUninstall;
    private Boolean debug;

    private boolean containerInstalledFromDistribution = false;

    /**
     * Get version of RichFaces dependencies to use with the test.
     *
     * By default, current project's version will be used.
     */
    public String getRichFacesVersion() {
        if (richfacesVersion == null || richfacesVersion.isEmpty()) {
            return currentBuildRichfacesVersion;
        }
        return richfacesVersion;
    }

    /**
     * Returns true when the RichFaces version setup for testing is same as current build version
     */
    public boolean isCurrentRichFacesVersion() {
        return currentBuildRichfacesVersion.equals(getRichFacesVersion());
    }

    /**
     * Add JSF to the WebArchive for support of plain Servlet containers (Tomcat, Jetty, etc.)
     */
    public boolean servletContainerSetup() {
        return servletContainerSetup;
    }

    /**
     * Get the Maven dependency (GAV) for the JSF implementation used for testing in servlet containers
     */
    public String getJsfImplementation() {
        return jsfImplementation;
    }

    /**
     * Get the Maven dependency (GAV) for the container distribution artifact
     */
    public String getContainerDistribution() {
        return containerDistribution;
    }

    /**
     * Get the Maven dependency (GAV) for the artifact which contains a container configuration files
     */
    public String getContainerConfiguration() {
        return containerConfiguration;
    }

    /**
     * Get the directory in which the unpacked container distribution will be placed
     */
    public String getContainerHome() {
        return containerHome;
    }

    /**
     * Set the flag that the container was installed from distribution
     */
    public void setContainerInstalledFromDistribution(boolean containerInstalledFromDistribution) {
        this.containerInstalledFromDistribution = containerInstalledFromDistribution;
    }

    /**
     * Returns true if the container should be uninstalled after suite (default: true)
     */
    public boolean containerShouldBeUninstalled() {
        return containerInstalledFromDistribution && (containerUninstall == null || containerUninstall);
    }

    public boolean isDebug() {
        return debug != null && debug;
    }

    /**
     * Validates the configuration
     */
    public void validate() {
        if (servletContainerSetup == null) {
            throw new IllegalArgumentException("The servletContainerSetup configuration needs to be specified");
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.jboss.arquillian.drone.spi.DroneConfiguration#getConfigurationName()
     */
    @Override
    public String getConfigurationName() {
        return "richfaces";
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.jboss.arquillian.drone.spi.DroneConfiguration#configure(org.jboss.arquillian.config.descriptor.api.ArquillianDescriptor
     * , java.lang.Class)
     */
    @Override
    public FundamentalTestConfiguration configure(ArquillianDescriptor descriptor, Class<? extends Annotation> qualifier) {
        return ConfigurationMapper.fromArquillianDescriptor(descriptor, this, qualifier);
    }

}
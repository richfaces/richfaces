package org.richfaces.tests.page.fragments.configuration;

import java.lang.annotation.Annotation;

import org.jboss.arquillian.config.descriptor.api.ArquillianDescriptor;
import org.jboss.arquillian.drone.spi.DroneConfiguration;
import org.jboss.arquillian.drone.configuration.ConfigurationMapper;

public class RichFacesPageFragmentsConfiguration implements DroneConfiguration<RichFacesPageFragmentsConfiguration> {

    private boolean useJSInteractionStrategy = false;

    public boolean isUseJSInteractionStrategy() {
        return useJSInteractionStrategy;
    }

    public void setUseJSInteractionStrategy(boolean useJSInteractionStrategy) {
        this.useJSInteractionStrategy = useJSInteractionStrategy;
    }

    @Override
    public RichFacesPageFragmentsConfiguration configure(ArquillianDescriptor descriptor, Class<? extends Annotation> qualifier) {
        return ConfigurationMapper.fromArquillianDescriptor(descriptor, this, qualifier);
    }

    @Override
    public String getConfigurationName() {
        return "rfPageFragments";
    }
}

package org.richfaces.fragment.configuration;

import org.jboss.arquillian.config.descriptor.api.ArquillianDescriptor;
import org.jboss.arquillian.core.api.InstanceProducer;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.core.api.annotation.Observes;
import org.jboss.arquillian.drone.api.annotation.Default;
import org.jboss.arquillian.test.spi.annotation.SuiteScoped;
import org.jboss.arquillian.test.spi.event.suite.BeforeSuite;

public class RichFacesPageFragmentsConfiguratorObserver {

    @Inject
    @SuiteScoped
    private InstanceProducer<RichFacesPageFragmentsConfiguration> configuration;

    public void configure(@Observes ArquillianDescriptor descriptor) {
        RichFacesPageFragmentsConfiguration c = new RichFacesPageFragmentsConfiguration();
        c.configure(descriptor, Default.class);
        RichFacesPageFragmentsConfigurationContext.set(c);
    }

    public void setupConfigurationContext(@Observes(precedence = 500) BeforeSuite event) {
        RichFacesPageFragmentsConfiguration c = RichFacesPageFragmentsConfigurationContext.get();
        this.configuration.set(c);
    }
}

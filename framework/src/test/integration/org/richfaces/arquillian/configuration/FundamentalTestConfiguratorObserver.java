package org.richfaces.arquillian.configuration;

import org.jboss.arquillian.config.descriptor.api.ArquillianDescriptor;
import org.jboss.arquillian.core.api.InstanceProducer;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.core.api.annotation.Observes;
import org.jboss.arquillian.drone.api.annotation.Default;
import org.jboss.arquillian.test.spi.annotation.SuiteScoped;
import org.jboss.arquillian.test.spi.event.suite.BeforeSuite;

public class FundamentalTestConfiguratorObserver {

    @Inject
    @SuiteScoped
    private InstanceProducer<FundamentalTestConfiguration> configuration;

    public void configure(@Observes ArquillianDescriptor descriptor) {
        FundamentalTestConfiguration c = new FundamentalTestConfiguration();
        c.configure(descriptor, Default.class).validate();
        FundamentalTestConfigurationContext.set(c);
    }

    public void setupConfigurationContext(@Observes(precedence = 500) BeforeSuite event) {
        FundamentalTestConfiguration c = FundamentalTestConfigurationContext.get();
        this.configuration.set(c);
    }
}
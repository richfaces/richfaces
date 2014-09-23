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

    public void setConfigurationContext(@Observes(precedence = 500) BeforeSuite event) {
        RichFacesPageFragmentsConfiguration c = RichFacesPageFragmentsConfigurationContext.get();
        this.configuration.set(c);
    }
}

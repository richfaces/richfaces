/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat Middleware LLC, and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.richfaces.arquillian.drone;

import java.io.IOException;

import org.jboss.arquillian.drone.spi.Configurator;
import org.jboss.arquillian.drone.spi.Destructor;
import org.jboss.arquillian.drone.spi.Instantiator;
import org.jboss.arquillian.drone.webdriver.configuration.WebDriverConfiguration;
import org.jboss.arquillian.drone.webdriver.factory.BrowserCapabilitiesList;
import org.jboss.arquillian.drone.webdriver.factory.PhantomJSDriverFactory;
import org.jboss.arquillian.phantom.resolver.ResolvingPhantomJSDriverService;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.service.DriverService;

/**
 * Factory which combines {@link org.jboss.arquillian.drone.spi.Configurator},
 * {@link org.jboss.arquillian.drone.spi.Instantiator} and {@link org.jboss.arquillian.drone.spi.Destructor} for
 * PhantomJSDriver.
 *
 * @author <a href="kpiwko@redhat.com>Karel Piwko</a>
 *
 */
public class ResolvingPhantomJSDriverFactory extends PhantomJSDriverFactory implements
        Configurator<PhantomJSDriver, WebDriverConfiguration>, Instantiator<PhantomJSDriver, WebDriverConfiguration>,
        Destructor<PhantomJSDriver> {

    private static final String BROWSER_CAPABILITIES = new BrowserCapabilitiesList.PhantomJS().getReadableName();

    /*
     * (non-Javadoc)
     *
     * @see org.jboss.arquillian.drone.spi.Sortable#getPrecedence()
     */
    @Override
    public int getPrecedence() {
        return 100;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.jboss.arquillian.drone.spi.Destructor#destroyInstance(java.lang.Object)
     */
    @Override
    public void destroyInstance(PhantomJSDriver instance) {
        instance.quit();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.jboss.arquillian.drone.spi.Instantiator#createInstance(org.jboss.arquillian.drone.spi.DroneConfiguration)
     */
    @Override
    public PhantomJSDriver createInstance(WebDriverConfiguration configuration) {

        // set capabilities
        DesiredCapabilities capabilities = new DesiredCapabilities(configuration.getCapabilities());

        capabilities.setCapability(ResolvingPhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, "target/phantomjs");

        // create resolving service
        DriverService driverService = createResolvingDriverService(capabilities);

        // create the instance
        return SecurityActions.newInstance(configuration.getImplementationClass(), new Class<?>[] { DriverService.class,
                Capabilities.class }, new Object[] { driverService, capabilities }, PhantomJSDriver.class);

    }

    private DriverService createResolvingDriverService(DesiredCapabilities capabilities) {
        try {
            return ResolvingPhantomJSDriverService.createDefaultService(capabilities);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to resolve PhantomJS binary", e);
        }
    }

    @Override
    protected String getDriverReadableName() {
        return BROWSER_CAPABILITIES;
    }

}
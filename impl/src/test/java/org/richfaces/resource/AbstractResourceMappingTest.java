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

import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import javax.el.ELContext;
import javax.faces.application.Application;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.junit.Before;
import org.mockito.Mock;
import org.richfaces.application.ServicesFactory;
import org.richfaces.application.configuration.ConfigurationService;
import org.richfaces.resource.external.ExternalResourceTracker;
import org.richfaces.resource.external.ExternalStaticResourceFactory;
import org.richfaces.resource.external.ExternalStaticResourceFactoryImpl;
import org.richfaces.test.AbstractServicesTest;

/**
 * @author <a href="http://community.jboss.org/people/lfryc">Lukas Fryc</a>
 */
public class AbstractResourceMappingTest extends AbstractServicesTest {

    @Inject
    FacesContext facesContext;

    @Inject
    Application application;

    @Inject
    ELContext elContext;

    @Inject
    ExternalContext externalContext;

    @Mock
    ConfigurationService configurationService;

    ExternalStaticResourceFactoryImpl externalStaticResourceFactory = new ExternalStaticResourceFactoryImpl();

    @Mock
    ExternalResourceTracker externalResourceTracker;

    Map<Object, Object> facesAttributes;
    Map<String, Object> requestMap;

    @Before
    public void setUp() {
        facesAttributes = new HashMap<Object, Object>();
        when(facesContext.getAttributes()).thenReturn(facesAttributes);

        requestMap = new HashMap<String, Object>();
        when(externalContext.getRequestMap()).thenReturn(requestMap);
    }

    protected void configure(Enum<?> key, Boolean value) {
        when(configurationService.getBooleanValue(facesContext, key)).thenReturn(value);
        // reload configuration
        externalStaticResourceFactory.init();
    }

    protected void configure(Enum<?> key, String value) {
        when(configurationService.getStringValue(facesContext, key)).thenReturn(value);
        // reload configuration
        externalStaticResourceFactory.init();
    }

    @Override
    protected void configureServices(ServicesFactory injector) {
        injector.setInstance(ConfigurationService.class, configurationService);
        injector.setInstance(ExternalStaticResourceFactory.class, externalStaticResourceFactory);
        injector.setInstance(ExternalResourceTracker.class, externalResourceTracker);
    }
}

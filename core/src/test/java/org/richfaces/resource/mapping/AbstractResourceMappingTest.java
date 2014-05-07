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
package org.richfaces.resource.mapping;

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
import org.mockito.Mockito;
import org.richfaces.application.ServicesFactory;
import org.richfaces.application.configuration.ConfigurationService;
import org.richfaces.resource.external.MappedResourceFactory;
import org.richfaces.resource.external.MappedResourceFactoryImpl;
import org.richfaces.resource.external.ResourceTracker;
import org.richfaces.skin.Skin;
import org.richfaces.skin.SkinFactory;
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

    @Mock
    SkinFactory skinFactory;
    @Mock
    Skin skin;

    MappedResourceFactoryImpl mappedResourceFactory = new MappedResourceFactoryImpl();

    @Mock
    ResourceTracker externalResourceTracker;

    Map<Object, Object> facesAttributes;
    Map<String, Object> requestMap;

    @Before
    public void setUp() {
        facesAttributes = new HashMap<Object, Object>();
        when(facesContext.getAttributes()).thenReturn(facesAttributes);

        requestMap = new HashMap<String, Object>();
        when(externalContext.getRequestMap()).thenReturn(requestMap);

        when(skinFactory.getSkin(Mockito.any(FacesContext.class))).thenReturn(skin);
        when(skin.getName()).thenReturn("skin");
    }

    protected void configure(Enum<?> key, Boolean value) {
        when(configurationService.getBooleanValue(facesContext, key)).thenReturn(value);
        // reload configuration
        mappedResourceFactory.init();
    }

    protected void configure(Enum<?> key, String value) {
        when(configurationService.getStringValue(facesContext, key)).thenReturn(value);
        // reload configuration
        mappedResourceFactory.init();
    }

    @Override
    protected void configureServices(ServicesFactory injector) {
        injector.setInstance(ConfigurationService.class, configurationService);
        injector.setInstance(MappedResourceFactory.class, mappedResourceFactory);
        injector.setInstance(ResourceTracker.class, externalResourceTracker);
        injector.setInstance(SkinFactory.class, skinFactory);
        injector.setInstance(ResourceMappingConfiguration.class, new ResourceMappingConfiguration());
    }
}

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
package org.richfaces.resource.optimizer.faces;

import java.util.Collections;

import javax.faces.application.ResourceHandler;
import javax.faces.context.FacesContext;

import org.richfaces.configuration.ConfigurationService;
import org.richfaces.configuration.ConfigurationServiceImpl;
import org.richfaces.resource.external.ExternalResourceTracker;
import org.richfaces.resource.external.DefaultExternalResourceTracker;
import org.richfaces.resource.external.ExternalStaticResourceFactory;
import org.richfaces.resource.external.ExternalStaticResourceFactoryImpl;
import org.richfaces.resource.optimizer.Faces;
import org.richfaces.resource.optimizer.FileNameMapper;
import org.richfaces.resource.optimizer.skin.SkinFactoryImpl;
import org.richfaces.services.DependencyInjectionServiceImpl;
import org.richfaces.services.DependencyInjector;
import org.richfaces.services.Module;
import org.richfaces.services.ServiceTracker;
import org.richfaces.services.ServicesFactory;
import org.richfaces.services.ServicesFactoryImpl;
import org.richfaces.skin.SkinFactory;

/**
 * @author Nick Belaevski
 *
 */
public class FacesImpl implements Faces {
    private String webroot;
    private FileNameMapper fileNameMapper;
    private ResourceHandler resourceHandler;

    public FacesImpl(String webroot, FileNameMapper fileNameMapper, ResourceHandler resourceHandler) {
        super();
        this.webroot = webroot;
        this.fileNameMapper = fileNameMapper;
        this.resourceHandler = resourceHandler;
    }

    public void start() {
        
        final ServicesFactoryImpl serviceFactory = new ServicesFactoryImpl();
        Module module = new Module() {
            public void configure(ServicesFactory factory) {
                factory.setInstance(ConfigurationService.class, new ConfigurationServiceImpl());
                factory.setInstance(SkinFactory.class, new SkinFactoryImpl());
                factory.setInstance(FileNameMapper.class, fileNameMapper);
                factory.setInstance(DependencyInjector.class, new DependencyInjectionServiceImpl());
                factory.setInstance(ResourceHandler.class, resourceHandler);
                factory.setInstance(ExternalResourceTracker.class, new DefaultExternalResourceTracker());
                factory.setInstance(ExternalStaticResourceFactory.class, new ExternalStaticResourceFactoryImpl());
            }
        };
        
        ServiceTracker.setFactory(serviceFactory);
        
        // initialization with FacesContext available
        startRequest();
        serviceFactory.init(Collections.singleton(module));
        stopRequest();
    }

    public void stop() {
        ServiceTracker.release();
    }

    public void setSkin(String skinName) {
        SkinFactoryImpl.setSkinName(skinName);
    }

    public FacesContext startRequest() {
        FacesContextImpl facesContextImpl = new FacesContextImpl();
        facesContextImpl.getExternalContext().setWebRoot(webroot);
        assert FacesContext.getCurrentInstance() != null;

        return facesContextImpl;
    }

    public void stopRequest() {
        FacesContext.getCurrentInstance().release();
        assert FacesContext.getCurrentInstance() == null;
    }
}

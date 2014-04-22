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

package org.richfaces.application;

import org.richfaces.application.configuration.ConfigurationService;
import org.richfaces.application.configuration.ConfigurationServiceImpl;
import org.richfaces.application.push.PushContextFactory;
import org.richfaces.application.push.impl.PushContextFactoryImpl;
import org.richfaces.cache.Cache;
import org.richfaces.el.GenericsIntrospectionService;
import org.richfaces.el.GenericsIntrospectionServiceImpl;
import org.richfaces.focus.FocusManager;
import org.richfaces.javascript.JavaScriptService;
import org.richfaces.javascript.JavaScriptServiceImpl;
import org.richfaces.l10n.BundleLoader;
import org.richfaces.log.Logger;
import org.richfaces.log.RichfacesLogger;
import org.richfaces.renderkit.AjaxDataSerializer;
import org.richfaces.renderkit.AjaxDataSerializerImpl;
import org.richfaces.resource.DefaultResourceCodec;
import org.richfaces.resource.ResourceCodec;
import org.richfaces.resource.ResourceLibraryFactory;
import org.richfaces.resource.ResourceLibraryFactoryImpl;
import org.richfaces.resource.external.MappedResourceFactory;
import org.richfaces.resource.external.MappedResourceFactoryImpl;
import org.richfaces.resource.external.ResourceTracker;
import org.richfaces.resource.external.ResourceTrackerImpl;
import org.richfaces.resource.mapping.ResourceMappingConfiguration;
import org.richfaces.skin.SkinFactory;
import org.richfaces.skin.SkinFactoryImpl;

/**
 * <p>Default RichFaces configuration module.</p>
 *
 * <p>User can implement application-specific {@link Module} in order to rewrite this configuration.</p>
 */
public class DefaultModule implements Module {

    private static final Logger LOG = RichfacesLogger.CONFIG.getLogger();

    /*
     * (non-Javadoc)
     * @see org.richfaces.services.Module#configure(org.richfaces.services.ServicesFactory)
     */
    @Override
    public void configure(ServicesFactory factory) {
        factory.setInstance(ConfigurationService.class, new ConfigurationServiceImpl());
        factory.setInstance(SkinFactory.class, new SkinFactoryImpl());
        factory.setInstance(AjaxDataSerializer.class, new AjaxDataSerializerImpl());
        factory.setInstance(ResourceCodec.class, ServiceLoader.loadService(ResourceCodec.class, DefaultResourceCodec.class));
        factory.setInstance(Cache.class, new CacheProvider());
        factory.setInstance(Uptime.class, new Uptime());
        factory.setInstance(DependencyInjector.class, new DependencyInjectorImpl());
        factory.setInstance(MessageFactory.class, new MessageFactoryImpl(new BundleLoader()));
        factory.setInstance(ResourceLibraryFactory.class, new ResourceLibraryFactoryImpl());
        factory.setInstance(PushContextFactory.class,
                ServiceLoader.loadService(PushContextFactory.class, PushContextFactoryImpl.class));
        factory.setInstance(JavaScriptService.class, new JavaScriptServiceImpl());
        factory.setInstance(GenericsIntrospectionService.class, new GenericsIntrospectionServiceImpl());
        factory.setInstance(ResourceTracker.class, new ResourceTrackerImpl());
        factory.setInstance(MappedResourceFactory.class, new MappedResourceFactoryImpl());
//        factory.setInstance(FocusManager.class, ServiceLoader.loadService(FocusManager.class, FocusManagerImpl.class));
        factory.setInstance(ResourceMappingConfiguration.class, new ResourceMappingConfiguration());

        // workaround for loading service from richfaces-components-rich module (needs to be bypassed during tests)
        FocusManager focusManager = ServiceLoader.loadService(FocusManager.class);
        if (focusManager == null) {
            LOG.warn("There was no service " + FocusManager.class.getName() + " found");
        } else {
            factory.setInstance(FocusManager.class, focusManager);
        }
    }
}

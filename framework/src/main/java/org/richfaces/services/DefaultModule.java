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

package org.richfaces.services;

import org.richfaces.cache.Cache;
import org.richfaces.cache.CacheProvider;
import org.richfaces.configuration.ConfigurationService;
import org.richfaces.configuration.ConfigurationServiceImpl;
import org.richfaces.context.AjaxDataSerializer;
import org.richfaces.context.AjaxDataSerializerImpl;
import org.richfaces.el.GenericsIntrospectionService;
import org.richfaces.el.GenericsIntrospectionServiceImpl;
import org.richfaces.javascript.JavaScriptService;
import org.richfaces.javascript.JavaScriptServiceImpl;
import org.richfaces.l10n.BundleLoader;
import org.richfaces.l10n.MessageFactory;
import org.richfaces.l10n.MessageFactoryImpl;
import org.richfaces.push.PushContextFactory;
import org.richfaces.push.PushContextFactoryImpl;
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
import org.richfaces.ui.misc.focus.FocusManager;
import org.richfaces.ui.misc.focus.FocusManagerImpl;

/**
 * <p>Default RichFaces configuration module.</p>
 *
 * <p>User can implement application-specific {@link Module} in order to rewrite this configuration.</p>
 */
public class DefaultModule implements Module {

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
        factory.setInstance(FocusManager.class, ServiceLoader.loadService(FocusManager.class, FocusManagerImpl.class));
        factory.setInstance(ResourceMappingConfiguration.class, new ResourceMappingConfiguration());
    }
}

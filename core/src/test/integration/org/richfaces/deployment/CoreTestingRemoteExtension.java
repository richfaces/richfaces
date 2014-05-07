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
package org.richfaces.deployment;

import org.jboss.arquillian.container.test.spi.RemoteLoadableExtension;
import org.jboss.arquillian.test.spi.enricher.resource.ResourceProvider;

/**
 * Arquillian extension for testing Core
 *
 * @author Lukas Fryc
 */
public class CoreTestingRemoteExtension implements RemoteLoadableExtension {

    @Override
    public void register(ExtensionBuilder builder) {
        builder.service(ResourceProvider.class, CoreServicesEnricher.ConfigurationServiceProvider.class);
        builder.service(ResourceProvider.class, CoreServicesEnricher.SkinFactoryProvider.class);
        builder.service(ResourceProvider.class, CoreServicesEnricher.AjaxDataSerializerProvider.class);
        builder.service(ResourceProvider.class, CoreServicesEnricher.ResourceCodecProvider.class);
        builder.service(ResourceProvider.class, CoreServicesEnricher.CacheProvider.class);
        builder.service(ResourceProvider.class, CoreServicesEnricher.UptimeProvider.class);
        builder.service(ResourceProvider.class, CoreServicesEnricher.DependencyInjectorProvider.class);
        builder.service(ResourceProvider.class, CoreServicesEnricher.MessageFactoryProvider.class);
        builder.service(ResourceProvider.class, CoreServicesEnricher.ResourceLibraryFactoryProvider.class);
        builder.service(ResourceProvider.class, CoreServicesEnricher.PushContextFactoryProvider.class);
        builder.service(ResourceProvider.class, CoreServicesEnricher.JavaScriptServiceProvider.class);
        builder.service(ResourceProvider.class, CoreServicesEnricher.GenericsIntrospectionServiceProvider.class);
        builder.service(ResourceProvider.class, CoreServicesEnricher.ExternalResourceTrackerProvider.class);
        builder.service(ResourceProvider.class, CoreServicesEnricher.ExternalStaticResourceFactoryProvider.class);
    }

}

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2012, Red Hat, Inc., and individual contributors
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
package org.richfaces.integration;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.arquillian.test.spi.enricher.resource.ResourceProvider;
import org.richfaces.application.DependencyInjector;
import org.richfaces.application.MessageFactory;
import org.richfaces.application.ServiceTracker;
import org.richfaces.application.Uptime;
import org.richfaces.application.configuration.ConfigurationService;
import org.richfaces.application.push.PushContextFactory;
import org.richfaces.cache.Cache;
import org.richfaces.el.GenericsIntrospectionService;
import org.richfaces.javascript.JavaScriptService;
import org.richfaces.renderkit.AjaxDataSerializer;
import org.richfaces.resource.ResourceCodec;
import org.richfaces.resource.ResourceLibraryFactory;
import org.richfaces.resource.external.ExternalResourceTracker;
import org.richfaces.resource.external.ExternalStaticResourceFactory;
import org.richfaces.skin.SkinFactory;

/**
 * Provides Core services injection tracked by {@link ServiceTracker} using {@link ArquillianResource}.
 *
 * @author Lukas Fryc
 */
public class CoreServicesEnricher {

    public abstract static class AbstractProvider<T> implements ResourceProvider {

        private Class<T> providedClass;

        @SuppressWarnings("unchecked")
        public AbstractProvider() {
            ParameterizedType type = (ParameterizedType) getClass().getGenericSuperclass();
            Type argumentType = type.getActualTypeArguments()[0];
            providedClass = (Class<T>) argumentType;
        }

        @Override
        public boolean canProvide(Class<?> type) {
            return type == providedClass;
        }

        @Override
        public Object lookup(ArquillianResource resource, Annotation... qualifiers) {
            return ServiceTracker.getProxy(providedClass);
        }

    }

    public static class ConfigurationServiceProvider extends AbstractProvider<ConfigurationService> {
    };

    public static class SkinFactoryProvider extends AbstractProvider<SkinFactory> {
    };

    public static class AjaxDataSerializerProvider extends AbstractProvider<AjaxDataSerializer> {
    };

    public static class ResourceCodecProvider extends AbstractProvider<ResourceCodec> {
    };

    public static class CacheProvider extends AbstractProvider<Cache> {
    };

    public static class UptimeProvider extends AbstractProvider<Uptime> {
    };

    public static class DependencyInjectorProvider extends AbstractProvider<DependencyInjector> {
    };

    public static class MessageFactoryProvider extends AbstractProvider<MessageFactory> {
    };

    public static class ResourceLibraryFactoryProvider extends AbstractProvider<ResourceLibraryFactory> {
    };

    public static class PushContextFactoryProvider extends AbstractProvider<PushContextFactory> {
    };

    public static class JavaScriptServiceProvider extends AbstractProvider<JavaScriptService> {
    };

    public static class GenericsIntrospectionServiceProvider extends AbstractProvider<GenericsIntrospectionService> {
    };

    public static class ExternalResourceTrackerProvider extends AbstractProvider<ExternalResourceTracker> {
    };

    public static class ExternalStaticResourceFactoryProvider extends AbstractProvider<ExternalStaticResourceFactory> {
    };
}

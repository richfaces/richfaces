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
package org.richfaces.resource;

import java.util.Map;
import java.util.concurrent.ExecutionException;

import javax.faces.FacesException;

import org.richfaces.log.Logger;
import org.richfaces.log.RichfacesLogger;
import org.richfaces.util.FastJoiner;
import org.richfaces.util.PropertiesUtil;

import com.google.common.base.Function;
import com.google.common.base.Splitter;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Iterables;

/**
 * @author Nick Belaevski
 */
public class ResourceLibraryFactoryImpl implements ResourceLibraryFactory {

    private static final Logger LOGGER = RichfacesLogger.RESOURCE.getLogger();
    private static final FastJoiner SLASH_JOINER = FastJoiner.on('/');
    private static final Splitter COMA_SPLITTER = Splitter.on(',').omitEmptyStrings().trimResults();

    /*
     * (non-Javadoc)
     * @see org.richfaces.resource.ResourceLibraryFactory#getResourceLibrary(java.lang.String, java.lang.String)
     */
    @Override
    public ResourceLibrary getResourceLibrary(String name, String library) {
        ResourceKey resourceKey = new ResourceKey(name, library);
        try {
            return instances.get(resourceKey);
        } catch (ExecutionException e) {
            throw new FacesException(String.format("Can't resolve resource library %s", resourceKey), e);
        }
    }

    private LoadingCache<ResourceKey, ResourceLibrary> instances = CacheBuilder.newBuilder().build(CacheLoader.from(new Function<ResourceKey, ResourceLibrary>() {
        public ResourceLibrary apply(ResourceKey from) {
            String propsResourceName = from.getResourceName() + ".library.properties";

            Map<String, String> props = PropertiesUtil.loadProperties("META-INF/richfaces/"
                + SLASH_JOINER.join(from.getLibraryName(), propsResourceName));

            String libraryClass = props.get("class");
            String resources = props.get("resources");

            if (libraryClass != null) {
                try {
                    Class<?> clazz = Class.forName(libraryClass.trim(), false, Thread.currentThread()
                        .getContextClassLoader());
                    return (ResourceLibrary) clazz.newInstance();
                } catch (ClassNotFoundException e) {
                    LOGGER.error(e.getMessage(), e);
                } catch (InstantiationException e) {
                    LOGGER.error(e.getMessage(), e);
                } catch (IllegalAccessException e) {
                    LOGGER.error(e.getMessage(), e);
                }
            } else if (resources != null) {
                Iterable<ResourceKey> keys = Iterables.transform(COMA_SPLITTER.split(resources), ResourceKey.FACTORY);
                return new StaticResourceLibrary(Iterables.toArray(keys, ResourceKey.class));
            } else {
                LOGGER.error("'class' or 'resources' properties should be declared in library descriptor: " + from);
            }

            return null;
        }
    }));
}

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

/**
 *
 */
package org.richfaces.javascript;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;

import org.richfaces.log.Logger;
import org.richfaces.log.RichfacesLogger;
import org.richfaces.resource.ResourceKey;
import org.richfaces.validator.model.ClientSideScripts;
import org.richfaces.validator.model.Component;
import org.richfaces.validator.model.Resource;

import javax.faces.FacesException;
import javax.xml.bind.JAXB;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;

/**
 * @author asmirnov
 *
 */
public final class ClientServiceConfigParser {
    private static final Logger LOG = RichfacesLogger.CONFIG.getLogger();

    private ClientServiceConfigParser() {
    }

    public static Map<Class<?>, LibraryFunction> parseConfig(String name) {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        if (null == loader) {
            loader = ClientServiceConfigParser.class.getClassLoader();
        }
        Builder<Class<?>, LibraryFunction> resultBuilder = ImmutableMap.builder();
        try {
            Enumeration<URL> resources = loader.getResources(name);
            while (resources.hasMoreElements()) {
                URL url = (URL) resources.nextElement();
                resultBuilder.putAll(parse(loader, url));
            }
        } catch (IOException e) {
            return Collections.emptyMap();
        }
        return resultBuilder.build();
    }

    static Map<Class<?>, LibraryFunction> parse(ClassLoader loader, URL url) {
        Map<Class<?>, LibraryFunction> result = Maps.newHashMap();
        try {
            ClientSideScripts clientSideScripts = JAXB.unmarshal(url, ClientSideScripts.class);
            for (Component component : clientSideScripts.getComponent()) {
                try {
                    Class<?> componentClass = loader.loadClass(component.getType());
                    Iterable<ResourceKey> resources = Iterables.transform(component.getResource(),
                        new Function<Resource, ResourceKey>() {
                            public ResourceKey apply(Resource from) {
                                return ResourceKey.create(from.getName(), from.getLibrary());
                            }
                        });
                    LibraryFunctionImplementation function = new LibraryFunctionImplementation(component.getFunction(),
                        resources);
                    result.put(componentClass, function);
                } catch (ClassNotFoundException e) {
                    // throw new FacesException("Class for component not found", e);
                    LOG.warn("Found JavaScript function definition for class " + component.getType()
                        + ", but that class is not presented");
                }
            }
        } catch (Exception e) {
            throw new FacesException("Error parsing config file " + url, e);
        }
        return result;
    }
}

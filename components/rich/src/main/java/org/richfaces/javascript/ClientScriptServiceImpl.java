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

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import javax.faces.FacesException;
import javax.faces.application.Resource;
import javax.faces.application.ResourceDependency;
import javax.faces.application.ResourceHandler;
import javax.faces.context.FacesContext;

import org.richfaces.resource.ResourceKey;

import com.google.common.base.Function;
import com.google.common.base.Strings;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Lists;

/**
 * @author asmirnov
 *
 */
public class ClientScriptServiceImpl implements ClientScriptService {
    private static final String TEXT_JAVASCRIPT = "text/javascript";
    private static final String ORG_RICHFACES_CSV = "org.richfaces.csv";
    private static final LibraryFunction NO_SCRIPT = new LibraryFunction() {
        public Iterable<ResourceKey> getResources() {
            return Collections.emptySet();
        }

        public String getName() {
            return null;
        }
    };
    private static final Function<Class<?>, LibraryFunction> RESOURCE_SCRIPT_FUNCTION = new Function<Class<?>, LibraryFunction>() {
        public LibraryFunction apply(Class<?> arg0) {
            return getScriptResource(FacesContext.getCurrentInstance(), arg0);
        }
    };
    private static final Function<Class<?>, LibraryFunction> ANNOTATION_SCRIPT_FUNCTION = new Function<Class<?>, LibraryFunction>() {
        public LibraryFunction apply(Class<?> arg0) {
            return getScriptFromAnnotation(arg0);
        }
    };
    private final LoadingCache<Class<?>, LibraryFunction> resourcesMapping;
    private final LoadingCache<Class<?>, LibraryFunction> annotationsMapping;
    private final Map<Class<?>, LibraryFunction> defaultMapping;

    public ClientScriptServiceImpl(Map<Class<?>, LibraryFunction> defaultMapping) {
        this.defaultMapping = defaultMapping;
        resourcesMapping = CacheBuilder.newBuilder().initialCapacity(10).build(CacheLoader.from(RESOURCE_SCRIPT_FUNCTION));
        annotationsMapping = CacheBuilder.newBuilder().initialCapacity(10).build(CacheLoader.from(ANNOTATION_SCRIPT_FUNCTION));
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.validator.ClientScriptService#getScript(java.lang.Class)
     */
    public LibraryFunction getScript(FacesContext facesContext, Class<?> javaClass) throws ScriptNotFoundException {
        if (null == facesContext || null == javaClass) {
            throw new NullPointerException();
        }
        LibraryFunction function = NO_SCRIPT;// RF-10719, temporary disable. getFromComputationMap(resourcesMapping, javaClass);
        if (NO_SCRIPT == function) {
            if (defaultMapping.containsKey(javaClass)) {
                function = defaultMapping.get(javaClass);
            } else {
                function = getFromLoadingCache(annotationsMapping, javaClass);
            }
        }
        if (NO_SCRIPT == function) {
            throw new ScriptNotFoundException("No client-side script for class " + javaClass.getName());
        }
        return function;
    }

    private LibraryFunction getFromLoadingCache(LoadingCache<Class<?>, LibraryFunction> cache, Class<?> clazz) {
        try {
            return cache.get(clazz);
        } catch (ExecutionException e) {
            Throwable cause = e.getCause();
            throw new FacesException(cause);
        }
    }

    private static LibraryFunction getScriptFromAnnotation(Class<?> javaClass) {
        if (javaClass.isAnnotationPresent(ClientSideScript.class)) {
            ClientSideScript clientSideScript = javaClass.getAnnotation(ClientSideScript.class);
            List<ResourceKey> resources = Lists.newArrayList();
            for (ResourceDependency dependency : clientSideScript.resources()) {
                resources.add(ResourceKey.create(dependency.name(), dependency.library()));
            }
            return new LibraryFunctionImplementation(clientSideScript.function(), resources);
        } else {
            return NO_SCRIPT;
        }
    }

    private static LibraryFunction getScriptResource(FacesContext facesContext, Class<?> javaClass) {
        ResourceHandler resourceHandler = facesContext.getApplication().getResourceHandler();
        String resourceName = javaClass.getSimpleName() + ".js";
        Resource facesResource = resourceHandler.createResource(resourceName, ORG_RICHFACES_CSV, TEXT_JAVASCRIPT);
        if (null != facesResource) {
            final String functionName = firstToLowerCase(javaClass.getSimpleName());
            return new LibraryFunctionImplementation(functionName, resourceName, ORG_RICHFACES_CSV);
        } else {
            return NO_SCRIPT;
        }
    }

    /**
     * <p>
     * Change case of the first character to lower, as it required by the Java Beans property and setter/getter method name
     * conventions:
     * </p>
     * <p>
     * "PropertyFoo" will be changed to "propertyFoo"
     * </p>
     *
     * @param in
     * @return {@code in} with first character changed to lower case.
     */
    private static String firstToLowerCase(String in) {

        if (Strings.isNullOrEmpty(in)) {
            return in;
        }

        return in.substring(0, 1).toLowerCase() + in.substring(1);
    }
}

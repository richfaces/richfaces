/*
 * $Id$
 *
 * License Agreement.
 *
 * Rich Faces - Natural Ajax for Java Server Faces (JSF)
 *
 * Copyright (C) 2007 Exadel, Inc.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License version 2.1 as published by the Free Software Foundation.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA
 */
package org.richfaces.application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.regex.Pattern;

import com.google.common.collect.Iterables;

/**
 * <p class="changed_added_4_0">
 * This class loads services from files placed to the META-INF/services in classpath.
 * </p>
 *
 * @author asmirnov@exadel.com
 *
 */
public final class ServiceLoader {
    private static final String META_INF_SERVICES = "META-INF/services/";
    private static final Pattern LEGAL_JAVA_NAME = Pattern.compile("^(([A-Za-z0-9_])+\\.)+[A-Z]([A-Za-z0-9_]*)$");

    private ServiceLoader() {

    }

    /**
     * <p class="changed_added_4_0">
     * Load and instantiate all service implementations.
     * </p>
     *
     * @param <S>
     * @param serviceClass
     * @return
     * @throws ServiceException
     */
    public static <S> Collection<S> loadServices(Class<S> serviceClass) throws ServiceException {
        Collection<Class<? extends S>> serviceClasses = loadServiceClasses(serviceClass);
        List<S> instances = new ArrayList<S>();
        for (Class<? extends S> implementationClass : serviceClasses) {
            instances.add(createInstance(implementationClass));
        }
        return instances;
    }

    public static <S> S loadService(Class<S> serviceClass, Class<? extends S> defaultImplementation) {
        Collection<Class<? extends S>> serviceClasses = loadServiceClasses(serviceClass);
        try {
            return createInstance(Iterables.getLast(serviceClasses));
        } catch (NoSuchElementException e) {
            return createInstance(defaultImplementation);
        }
    }

    private static <S> S createInstance(Class<? extends S> implementationClass) {
        try {
            return implementationClass.newInstance();
        } catch (InstantiationException e) {
            throw new ServiceException("Cannot instantiate service class, does it have default constructor ?", e);
        } catch (IllegalAccessException e) {
            throw new ServiceException("Cannot instantiate service class, illegal access", e);
        }
    }

    /**
     * <p class="changed_added_4_0">
     * Load service implementation classes.
     * </p>
     *
     * @param <S>
     * @param serviceClass
     * @return
     * @throws ServiceException
     */
    public static <S> Collection<Class<? extends S>> loadServiceClasses(Class<S> serviceClass) throws ServiceException {
        ClassLoader classLoader = getClassLoader(serviceClass);
        Set<String> names = new LinkedHashSet<String>();
        Enumeration<URL> resources;
        try {
            resources = classLoader.getResources(META_INF_SERVICES + serviceClass.getName());
            while (resources.hasMoreElements()) {
                names.addAll(parse(resources.nextElement()));
            }
        } catch (IOException e) {
            throw new ServiceException("Error load service descriptions", e);
        }
        Set<Class<? extends S>> instanceClasses = new LinkedHashSet<Class<? extends S>>();
        for (String className : names) {
            instanceClasses.add(loadClass(serviceClass, classLoader, className));
        }
        return instanceClasses;
    }

    static Collection<String> parse(URL url) throws ServiceException, IOException {
        InputStream inputStream = null;
        try {
            URLConnection connection = url.openConnection();
            try {
                connection.setUseCaches(false);
            } catch (IllegalArgumentException e) {
                // Do nothing.
            }
            Set<String> names = new HashSet<String>();
            inputStream = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
            String line;
            while (null != (line = reader.readLine())) {
                parseLine(line, names);
            }
            return names;
        } finally {
            if (null != inputStream) {
                inputStream.close();
            }
        }
    }

    /**
     * <p class="changed_added_4_0">
     * Parse a single line from service description. Skips empty lines and comments started with #
     * </p>
     *
     * @param line
     * @param names
     * @throws ServiceException
     */
    static void parseLine(String line, Collection<String> names) throws ServiceException {
        String name;
        int commentIndex = line.indexOf('#');
        if (commentIndex >= 0) {
            name = line.substring(0, commentIndex);
        } else {
            name = line;
        }
        name = name.trim();
        if (name.length() > 0) {
            if (LEGAL_JAVA_NAME.matcher(name).matches()) {
                names.add(name);
            } else {
                throw new ServiceException("Invalid java class name [" + line + "]");
            }
        }
    }

    /**
     * <p class="changed_added_4_0">
     * Get class loader
     * </p>
     *
     * @param <S>
     * @param serviceClass
     * @return context class loader or loader with which service class has been loaded.
     */
    private static <S> ClassLoader getClassLoader(Class<S> serviceClass) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        if (null == classLoader) {
            classLoader = serviceClass.getClassLoader();
        }
        return classLoader;
    }

    private static <S> Class<? extends S> loadClass(Class<S> serviceClass, ClassLoader classLoader, String className)
        throws ServiceException {
        try {
            Class<?> implementationClass = classLoader.loadClass(className);
            if (serviceClass.isAssignableFrom(implementationClass)) {
                return implementationClass.asSubclass(serviceClass);
            } else {
                throw new ServiceException("Class " + className + " in not the instance of " + serviceClass.getName());
            }
        } catch (ClassNotFoundException e) {
            throw new ServiceException("Class " + className + " not found", e);
        }
    }
}

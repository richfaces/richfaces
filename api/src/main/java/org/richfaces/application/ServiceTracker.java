/*
 * JBoss, Home of Professional Open Source
 * Copyright 2009, Red Hat, Inc. and individual contributors
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

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.faces.FacesException;
import javax.faces.context.FacesContext;

/**
 * <p>
 * Tracker class to provide access to various framework implementation services. Examples of such services are:
 * {@link org.richfaces.skin.SkinFactory}, TBD.
 * </p>
 *
 *
 * <p>
 * This class managess application-scoped service factories that are stored in the map with {@link Thread#currentThread()}
 * Context classloader as the key. Therefore, there is only one instance per JEE application in the current JVM.
 * </p>
 * <p>
 * actuall cal for the service instance delegated to the current factory
 * </p>
 * <img src="services.png" alt="services tracking class diagramm"/>
 *
 *
 * <p>
 * <b>Note:</b> in initial state this class is not synchronized and presumes that all modification operations are done in a
 * context of single-thread (in JSF initialization listener).
 * </p>
 *
 * @author Nick Belaevski
 * @since 4.0
 */
public final class ServiceTracker {
    /**
     * <p class="changed_added_4_0">
     * </p>
     */
    private static final Map<ClassLoader, ServicesFactory> INSTANCES = new ConcurrentHashMap<ClassLoader, ServicesFactory>();

    /**
     * <p class="changed_added_4_0">
     * This class supposed to use with static methods only and cannot be instantiated.
     * </p>
     */
    private ServiceTracker() {
    }

    /**
     * <p class="changed_added_4_0">
     * Get service instance for given type. This is a wrapper method for {@link #getService(FacesContext, Class)} that gets
     * faces context by {@link FacesContext#getCurrentInstance()} call, if needed.
     * </p>
     *
     * @param <T> The service type, usually interface.
     * @param target Service type class.
     * @return service implementation instance.
     */
    public static <T> T getService(Class<T> target) {
        return getServicesFactory().getInstance(target);
    }

    /**
     * <p class="changed_added_4_0">
     * Get service instance for given type.
     * </p>
     *
     * @param <T> The service type, usually interface.
     * @param context current {@link FacesContext}.
     * @param target Service type class.
     * @return service instance.
     */
    public static <T> T getService(FacesContext context, Class<T> target) {
        return getServicesFactory().getInstance(target);
    }

    private static ServicesFactory getServicesFactory() {
        if (!INSTANCES.containsKey(getCurrentLoader())) {
            throw new FacesException("Service Tracker has not been initialized");
        }
        ServicesFactory service = INSTANCES.get(getCurrentLoader());
        return service;
    }

    private static ClassLoader getCurrentLoader() {
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        if (null == contextClassLoader) {
            contextClassLoader = ServiceTracker.class.getClassLoader();
        }
        return contextClassLoader;
    }

    /**
     * <p class="changed_added_4_0">
     * Set service factory implementation for the current context.
     * </p>
     *
     * @param factory
     */
    public static void setFactory(ServicesFactory factory) {
        INSTANCES.put(getCurrentLoader(), factory);
    }

    /**
     * <p class="changed_added_4_0">
     * Release factory service associated with current context.
     * </p>
     */
    public static void release() {
        ServicesFactory servicesFactory = INSTANCES.remove(getCurrentLoader());
        servicesFactory.release();
    }
}

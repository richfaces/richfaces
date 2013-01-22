/**
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
package org.ajax4jsf.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.faces.FacesException;

import org.ajax4jsf.resource.util.URLToStreamHelper;
import org.richfaces.log.Logger;
import org.richfaces.log.RichfacesLogger;

/**
 * @author shura
 */
@Deprecated
public final class ServicesUtils {
    private static final Logger LOG = RichfacesLogger.APPLICATION.getLogger();
    private static Map<String, Map<ClassLoader, Class<?>>> services = Collections
        .synchronizedMap(new HashMap<String, Map<ClassLoader, Class<?>>>());
    private static Map<String, Map<ClassLoader, Object>> instances = Collections
        .synchronizedMap(new HashMap<String, Map<ClassLoader, Object>>());

    private ServicesUtils() {

        // This is a static utility class.
    }

    /**
     * @param name
     * @return
     * @throws ClassNotFoundException
     */
    public static Class<?> getService(String name) throws ClassNotFoundException {
        Map<ClassLoader, Class<?>> contextClasses = services.get(name);

        if (null == contextClasses) {
            contextClasses = Collections.synchronizedMap(new HashMap<ClassLoader, Class<?>>());
            services.put(name, contextClasses);
        }

        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        Class<?> serviceClass = contextClasses.get(loader);

        if (null == serviceClass) {
            serviceClass = loadServiceClass(loader, name);
            contextClasses.put(loader, serviceClass);
        }

        return serviceClass;
    }

    /**
     * Get per-context instance for service.
     *
     * @param name - name ( default classname ) for service.
     * @return current instance for this service.
     * @throws ClassNotFoundException
     */
    public static Object getServiceInstance(String name) {
        Map<ClassLoader, Object> contextInstances = (Map<ClassLoader, Object>) instances.get(name);

        if (null == contextInstances) {
            contextInstances = Collections.synchronizedMap(new HashMap<ClassLoader, Object>());
            instances.put(name, contextInstances);
        }

        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        Object serviceInstance = contextInstances.get(loader);

        if (null == serviceInstance) {
            try {
                Class<?> serviceClass = loadServiceClass(loader, name);

                serviceInstance = serviceClass.newInstance();
            } catch (Exception e) {
                throw new FacesException("Error create instance for service " + name, e);
            }

            contextInstances.put(loader, serviceInstance);
        }

        return serviceInstance;
    }

    /**
     * @param loader
     * @param name
     * @return
     * @throws ClassNotFoundException
     */
    public static Class<?> loadServiceClass(ClassLoader loader, String name) throws ClassNotFoundException {
        Class<?> serviceClass;

        try {
            String resource = "META-INF/services/" + name;
            InputStream in = URLToStreamHelper.urlToStreamSafe(loader.getResource(resource));
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String serviceClassName = reader.readLine();

            reader.close();
            serviceClass = loadClass(loader, serviceClassName);

            if (LOG.isDebugEnabled()) {
                LOG.debug("Class for service " + name + " set to " + serviceClassName);
            }
        } catch (Exception e) {
            serviceClass = loadClass(loader, name);

            if (LOG.isDebugEnabled()) {
                LOG.debug("Service class set to default implementation " + name);
            }
        }

        return serviceClass;
    }

    /**
     * @param loader
     * @param name
     * @return
     * @throws ClassNotFoundException
     */
    public static Class<?> loadClass(ClassLoader loader, String name) throws ClassNotFoundException {
        Class<?> clazz;

        try {
            clazz = loader.loadClass(name);
        } catch (ClassNotFoundException e) {
            if (LOG.isWarnEnabled()) {
                LOG.warn("Class " + name + "not found by Context Classloader");
            }

            // Try internal library classloader.
            clazz = ServicesUtils.class.getClassLoader().loadClass(name);
        }

        return clazz;
    }
}

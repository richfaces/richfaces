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
package org.richfaces.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.MessageFormat;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;

import org.ajax4jsf.resource.util.URLToStreamHelper;
import org.richfaces.log.Logger;
import org.richfaces.log.RichfacesLogger;

import com.google.common.collect.Maps;

/**
 * @author Nick Belaevski
 *
 */
public final class PropertiesUtil {
    private static final Logger LOGGER = RichfacesLogger.UTIL.getLogger();

    private PropertiesUtil() {
    }

    private static ClassLoader getClassLoader() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        if (classLoader == null) {
            classLoader = PropertiesUtil.class.getClassLoader();
        }

        return classLoader;
    }

    /**
     * Loads properties into provided properties object from all resources on given classpath location.
     *
     * @see #loadProperties(String)
     */
    public static boolean loadProperties(Properties properties, String location) {
        boolean loaded = false;

        try {
            Enumeration<URL> resources = getClassLoader().getResources(location);

            while (resources.hasMoreElements()) {
                URL url = (URL) resources.nextElement();
                InputStream propertyStream = null;

                try {
                    propertyStream = URLToStreamHelper.urlToStream(url);
                    properties.load(propertyStream);
                    loaded = true;
                } catch (IOException e) {
                    LOGGER.warn(MessageFormat.format("Failure loading properties from URL: {0}", url.toExternalForm()), e);

                    continue;
                } finally {
                    if (null != propertyStream) {
                        propertyStream.close();
                    }
                }
            }
        } catch (IOException e) {

            // Do nothing - we can only log error, and continue to load next
            // property.
            if (LOGGER.isInfoEnabled()) {
                LOGGER.warn(MessageFormat.format("Failure loading properties from location: {0}", location), e);
            }
        }

        return loaded;
    }

    /**
     * Returns map with properties (key/value pairs) loaded from all resources on given classpath location.
     *
     * @see #loadProperties(Properties, String)
     */
    public static Map<String, String> loadProperties(String location) {
        Properties props = new Properties();
        loadProperties(props, location);
        return Maps.fromProperties(props);
    }
}

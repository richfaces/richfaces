/**
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc. and individual contributors
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
 **/
package org.richfaces.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.richfaces.log.Logger;
import org.richfaces.log.RichfacesLogger;

/**
 * When deserializing objects, first check that the class being deserialized is in the allowed whitelist.
 *
 * @author <a href="http://community.jboss.org/people/bleathem">Brian Leathem</a>
 */
public class LookAheadObjectInputStream extends ObjectInputStream {
    private static final Logger LOGGER = RichfacesLogger.APPLICATION.getLogger();
    private static final Map<String, Class<?>> PRIMITIVE_TYPES = new HashMap<String, Class<?>>(9, 1.0F);
    private static Set<Class> whitelistBaseClasses = new HashSet<Class>();
    private static Set<String> whitelistClassNameCache = Collections.newSetFromMap(new ConcurrentHashMap<String, Boolean>());

    static {
        PRIMITIVE_TYPES.put("bool", Boolean.TYPE);
        PRIMITIVE_TYPES.put("byte", Byte.TYPE);
        PRIMITIVE_TYPES.put("char", Character.TYPE);
        PRIMITIVE_TYPES.put("short", Short.TYPE);
        PRIMITIVE_TYPES.put("int", Integer.TYPE);
        PRIMITIVE_TYPES.put("long", Long.TYPE);
        PRIMITIVE_TYPES.put("float", Float.TYPE);
        PRIMITIVE_TYPES.put("double", Double.TYPE);
        PRIMITIVE_TYPES.put("void", Void.TYPE);

        whitelistClassNameCache.add(new Object[0].getClass().getName());
        whitelistClassNameCache.add(new String[0].getClass().getName());
        whitelistClassNameCache.add(new Boolean[0].getClass().getName());
        whitelistClassNameCache.add(new Byte[0].getClass().getName());
        whitelistClassNameCache.add(new Character[0].getClass().getName());
        whitelistClassNameCache.add(new Short[0].getClass().getName());
        whitelistClassNameCache.add(new Integer[0].getClass().getName());
        whitelistClassNameCache.add(new Long[0].getClass().getName());
        whitelistClassNameCache.add(new Float[0].getClass().getName());
        whitelistClassNameCache.add(new Double[0].getClass().getName());
        whitelistClassNameCache.add(new Void[0].getClass().getName());

        whitelistBaseClasses.add(String.class);
        whitelistBaseClasses.add(Boolean.class);
        whitelistBaseClasses.add(Byte.class);
        whitelistBaseClasses.add(Character.class);
        whitelistBaseClasses.add(Number.class);
        whitelistBaseClasses.add(Collection.class);
        whitelistBaseClasses.add(Map.class);

        loadWhitelist();
    }

    public LookAheadObjectInputStream(InputStream in) throws IOException {
        super(in);
    }

    /**
     * Only deserialize primitive or whitelisted classes
     */
    @Override
    protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
        Class<?> primitiveType = PRIMITIVE_TYPES.get(desc.getName());
        if (primitiveType != null) {
            return primitiveType;
        }
        if (!isClassValid(desc.getName())) {
            throw new InvalidClassException("Unauthorized deserialization attempt", desc.getName());
        }
        return super.resolveClass(desc);
    }

    /**
     * Determine if the given requestedClassName is allowed by the whitelist
     */
    boolean isClassValid(String requestedClassName) {
        if (whitelistClassNameCache.contains(requestedClassName)) {
            return true;
        }
        try {
            Class<?> requestedClass = Class.forName(requestedClassName);
            for (Class baseClass : whitelistBaseClasses ) {
                if (baseClass.isAssignableFrom(requestedClass)) {
                    whitelistClassNameCache.add(requestedClassName);
                    return true;
                }
            }
        } catch (ClassNotFoundException e) {
            return false;
        }
        return false;
    }

    /**
     * Load the whitelist from the properties file
     */
    static void loadWhitelist() {
        Properties whitelistProperties = new Properties();
        InputStream stream = null;
        try {
            stream =  LookAheadObjectInputStream.class.getResourceAsStream("/org/richfaces/resource/resource-serialization.properties");
            whitelistProperties.load(stream);
        } catch (IOException e) {
            throw new RuntimeException("Error loading the resource-serialization.properties file", e);
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    throw new RuntimeException("Error closing the resource-serialization.properties file", e);
                }
            }
        }
        for (String baseClassName : whitelistProperties.getProperty("whitelist").split(",")) {
            try {
                Class<?> baseClass = Class.forName(baseClassName);
                whitelistBaseClasses.add(baseClass);
            } catch (ClassNotFoundException e) {
                if (LOGGER.isInfoEnabled()) {
                    LOGGER.debug(e);
                }
            }
        }
    }
}

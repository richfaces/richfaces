/*
 * Copyright [yyyy] [name of copyright owner].
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.richfaces.cache;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import javax.faces.context.FacesContext;

import org.ajax4jsf.resource.util.URLToStreamHelper;
import org.richfaces.cache.lru.LRUMapCacheFactory;
import org.richfaces.log.Logger;
import org.richfaces.log.RichfacesLogger;

/**
 * TODO stop caches on application stop CacheManager is used in J2SE environments for looking up named caches.
 */
public class CacheManager {
    public static final String CACHE_MANAGER_FACTORY_CLASS = "org.ajax4jsf.cache.CACHE_MANAGER_FACTORY_CLASS";
    private static final String[] DEFAULT_FACTORIES_CHAIN = { "org.ajax4jsf.cache.JBossCacheCacheFactory",
            "org.ajax4jsf.cache.EhCacheCacheFactory" };
    private static final String FACTORY_PROPERTY_NAME = "org.ajax4jsf.cache.CacheFactory";
    private static final Logger LOG = RichfacesLogger.CACHE.getLogger();
    private CacheFactory cacheFactory;
    private final Map<String, Cache> caches = new ConcurrentHashMap<String, Cache>(1, 0.75f, 1);

    public Cache getCache(String cacheName) {
        return caches.get(cacheName);
    }

    public Cache createCache(FacesContext facesContext, String cacheName, Map<?, ?> env) {
        CacheFactory factory = getCacheFactory(env);
        Cache cache = factory.createCache(facesContext, cacheName, env);
        cache.start();

        caches.put(cacheName, cache);

        return cache;
    }

    public void destroyCache(String cacheName) {
        Cache cache = caches.remove(cacheName);

        cache.stop();
    }

    private CacheFactory getCacheFactory(Map<?, ?> env) {
        if (cacheFactory != null) {
            return cacheFactory;
        }

        String[] factories;
        String configuredFactoryName = findFactory(FACTORY_PROPERTY_NAME, env);

        if (configuredFactoryName != null) {
            LOG.info(MessageFormat.format("Configured to use [{0}] cache factory", configuredFactoryName));
            factories = new String[] { configuredFactoryName };
        } else {
            factories = DEFAULT_FACTORIES_CHAIN;
        }

        ClassLoader loader = findClassLoader();

        for (String factoryName : factories) {
            try {
                Class<?> spiClass = Class.forName(factoryName, true, loader);
                cacheFactory = CacheFactory.class.cast(spiClass.newInstance());
                LOG.info(MessageFormat.format("Selected [{0}]", factoryName));
                break;
            } catch (LinkageError iae) {
                // TODO LOG debug
            } catch (Exception e) {
                // TODO LOG debug
            }
        }

        if (cacheFactory == null) {
            cacheFactory = new LRUMapCacheFactory();
            LOG.info("Selected fallback cache factory");
        }

        return cacheFactory;
    }

    public Map<String, Cache> getCaches() {
        return caches;
    }

    private ClassLoader findClassLoader() {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();

        if (cl == null) {
            cl = ClassLoader.getSystemClassLoader();
        }

        return cl;
    }

    private static boolean isEmptyString(String s) {
        return (s == null) || "".equals(s);
    }

    String findFactory(String factoryId, Map<?, ?> env) {
        String envFactoryClass = (String) env.get(CACHE_MANAGER_FACTORY_CLASS);

        if (!isEmptyString(envFactoryClass)) {
            return envFactoryClass;
        }

        // Use the system property first
        String systemPropertyFactoryClass = searchInSystemProperty(factoryId);

        if (!isEmptyString(systemPropertyFactoryClass)) {
            return systemPropertyFactoryClass;
        }

        // try to read from $java.home/lib/jcache.properties
        String jcacheFactoryClass = searchInJcacheProperties(factoryId);

        if (!isEmptyString(jcacheFactoryClass)) {
            return jcacheFactoryClass;
        }

        // try to find services in CLASSPATH
        String classpathFactoryClass = searchInClasspath(factoryId);

        if (!isEmptyString(classpathFactoryClass)) {
            return classpathFactoryClass;
        }

        return null;
    }

    private String searchInClasspath(String factoryId) {
        try {
            ClassLoader cl = findClassLoader();
            InputStream is = URLToStreamHelper.urlToStreamSafe(cl.getResource("META-INF/services/" + factoryId));

            if (is != null) {
                BufferedReader r = new BufferedReader(new InputStreamReader(is, "UTF-8"));

                try {
                    return r.readLine();
                } finally {
                    r.close();
                }
            }
        } catch (IOException ignore) {

            // TODO Refactoring
        }

        return null;
    }

    private static String searchInJcacheProperties(String factoryId) {
        try {
            String configFile = System.getProperty("java.home") + File.separator + "lib" + File.separator + "jcache.properties";
            File file = new File(configFile);

            if (file.exists()) {
                InputStream in = new FileInputStream(file);

                try {
                    Properties props = new Properties();

                    props.load(in);

                    return props.getProperty(factoryId);
                } finally {
                    in.close();
                }
            }
        } catch (SecurityException ignore) {

            // TODO Refactoring
        } catch (IOException ignore) {

            // TODO Refactoring
        }

        return null;
    }

    private static String searchInSystemProperty(String factoryId) {
        try {
            return System.getProperty(factoryId);
        } catch (SecurityException ignore) {

            // TODO Refactoring
        }

        return null;
    }

    public void destroy() {
        if (caches.isEmpty()) {
            return;
        }

        for (String cacheName : caches.keySet()) {
            destroyCache(cacheName);
        }

        if (cacheFactory != null) {
            cacheFactory.destroy();
        }
    }
}

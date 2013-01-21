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
package org.richfaces.cache;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

import org.jboss.test.faces.AbstractFacesTest;
import org.junit.Assert;
import org.richfaces.application.CoreConfiguration;

/**
 * @author Nick Belaevski
 * @since 4.0
 */
public abstract class BaseCacheTest extends AbstractFacesTest {
    private int sizeLimit;
    private CacheManager cacheManager;
    private Cache cache;
    private String cacheManagerFactoryClassName;

    public BaseCacheTest(String cacheManagerFactoryClassName) {
        super();
        this.cacheManagerFactoryClassName = cacheManagerFactoryClassName;
    }

    @Override
    protected void setupJsfInitParameters() {
        super.setupJsfInitParameters();
        this.facesServer.addInitParameter(CacheManager.CACHE_MANAGER_FACTORY_CLASS, cacheManagerFactoryClassName);
        this.facesServer.addInitParameter(CoreConfiguration.RESOURCES_CACHE_SIZE_PARAM_NAME, Integer.toString(sizeLimit));
    }

    protected Cache getCache() {
        return cache;
    }

    @Override
    public void setUp() throws Exception {
        sizeLimit = 64;

        super.setUp();

        setupFacesRequest();

        cacheManager = new CacheManager();

        Map<?, ?> initParameterMap = facesContext.getExternalContext().getInitParameterMap();

        this.cache = cacheManager.createCache(facesContext, "test-cache", initParameterMap);
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();

        cacheManager.destroy();
    }

    public void testBasic() throws Exception {
        assertNull(cache.get("a"));
        cache.put("a", "value-a", null);
        assertEquals("value-a", cache.get("a"));
    }

    public void testExpiration() throws Exception {
        assertNull(cache.get("a"));

        long sleepTime = 0;
        long expirationTime = System.currentTimeMillis() + 3000;

        cache.put("a", "value-a", new Date(expirationTime));
        assertEquals("value-a", cache.get("a"));

        cache.put("b", "value-b", new Date(expirationTime));
        assertEquals("value-b", cache.get("b"));
        // interval to reach 1 second before expiration time
        sleepTime = expirationTime - 1000 - System.currentTimeMillis();
        assertTrue(sleepTime > 0);
        Thread.sleep(sleepTime);
        assertEquals("value-a", cache.get("a"));

        // interval to reach 1 second after expiration time
        sleepTime = expirationTime + 1000 - System.currentTimeMillis();
        assertTrue(sleepTime > 0);
        Thread.sleep(sleepTime);
        assertNull(cache.get("a"));
    }

    public void testMaxSize() throws Exception {
        Date expirationDate = new Date(System.currentTimeMillis() + 3600000 /* one hour - this should be enough for our test */);

        Map<String, String> data = new LinkedHashMap<String, String>();
        for (int i = 0; i < sizeLimit; i++) {
            String key = UUID.randomUUID().toString();
            String value = UUID.randomUUID().toString();
            data.put(key, value);
            cache.put(key, value, expirationDate);
        }

        for (Map.Entry<String, String> entry : data.entrySet()) {
            String key = entry.getKey();

            Object cacheValue = cache.get(key);
            assertEquals(entry.getValue(), cacheValue);
        }

        String extraEntryKey = UUID.randomUUID().toString();
        String extraEntryValue = UUID.randomUUID().toString();
        data.put(extraEntryKey, extraEntryValue);
        cache.put(extraEntryKey, extraEntryValue, expirationDate);

        // give cache time to evict
        Thread.sleep(2000);

        int nullCounter = 0;
        for (Map.Entry<String, String> entry : data.entrySet()) {
            String key = entry.getKey();

            Object cacheValue = cache.get(key);
            if (cacheValue == null) {
                nullCounter++;
            } else {
                assertEquals(entry.getValue(), cacheValue);
            }
        }

        assertTrue(nullCounter == 1);
    }

    public void testThreads() throws Exception {
        final AtomicBoolean failure = new AtomicBoolean();

        Thread[] writerThreads = new Thread[10];

        for (int i = 0; i < writerThreads.length; i++) {
            writerThreads[i] = new Thread() {
                public void run() {
                    final String key = UUID.randomUUID().toString();
                    final String value = UUID.randomUUID().toString();

                    cache.put(key, value, null);

                    Thread[] threads = new Thread[25];

                    for (int j = 0; j < threads.length; j++) {
                        threads[j] = new Thread() {
                            @Override
                            public void run() {
                                int retries = 1000;

                                for (int k = 0; k < retries; k++) {
                                    if (!value.equals(cache.get(key))) {
                                        failure.set(true);

                                        return;
                                    }
                                }
                            }
                        };
                    }

                    for (Thread thread : threads) {
                        thread.start();
                    }

                    int retries = 1000;

                    for (int k = 0; k < retries; k++) {
                        if (!value.equals(cache.get(key))) {
                            failure.set(true);
                        }
                    }

                    for (Thread thread : threads) {
                        try {
                            thread.join();
                        } catch (InterruptedException e) {
                            failure.set(true);
                        }
                    }
                }

                ;
            };
        }

        for (Thread thread : writerThreads) {
            thread.start();
        }

        for (Thread thread : writerThreads) {
            thread.join();
        }

        Assert.assertFalse(failure.get());
    }
}

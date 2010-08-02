/**
 * License Agreement.
 *
 *  JBoss RichFaces - Ajax4jsf Component Library
 *
 * Copyright (C) 2007  Exadel, Inc.
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

package org.ajax4jsf.request;

import javax.faces.context.FacesContext;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Nick Belaevski
 * @since 3.3.1
 */
final class MultipartRequestRegistry {
    private static final String REGISTRY_ATTRIBUTE_NAME = MultipartRequestRegistry.class.getName();
    private AtomicInteger atomicInteger = new AtomicInteger(0);
    private String registryId = UUID.randomUUID().toString();
    private Map<String, MultipartRequest> requestsMap = new ConcurrentHashMap<String, MultipartRequest>();

    private MultipartRequestRegistry() {
    }

    public static MultipartRequestRegistry getInstance(FacesContext context) {
        Map<String, Object> applicationMap = context.getExternalContext().getApplicationMap();
        MultipartRequestRegistry requestRegistry =
            (MultipartRequestRegistry) applicationMap.get(REGISTRY_ATTRIBUTE_NAME);

        if (requestRegistry == null) {
            synchronized (applicationMap) {
                requestRegistry = (MultipartRequestRegistry) applicationMap.get(REGISTRY_ATTRIBUTE_NAME);

                if (requestRegistry == null) {
                    requestRegistry = new MultipartRequestRegistry();
                    applicationMap.put(REGISTRY_ATTRIBUTE_NAME, requestRegistry);
                }
            }
        }

        return requestRegistry;
    }

    public String registerRequest(MultipartRequest request) {
        String key = registryId + ":" + atomicInteger.incrementAndGet();

        requestsMap.put(key, request);

        return key;
    }

    public void removeRequest(String key) {
        requestsMap.remove(key);
    }

    public MultipartRequest getRequest(String key) {
        return requestsMap.get(key);
    }
}

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
package org.richfaces.cache;

import java.util.Date;

import com.opensymphony.oscache.base.EntryRefreshPolicy;
import com.opensymphony.oscache.base.NeedsRefreshException;
import com.opensymphony.oscache.web.filter.ExpiresRefreshPolicy;

/**
 * @author Nick - mailto:nbelaevski@exadel.com created 01.05.2007
 */
public class OSCacheCache implements Cache {
    private com.opensymphony.oscache.base.Cache cache;

    public OSCacheCache(com.opensymphony.oscache.base.Cache cache) {
        super();
        this.cache = cache;
    }

    public Object get(Object key) {
        String stringKey = (String) key;

        try {
            return cache.getFromCache(stringKey);
        } catch (NeedsRefreshException e) {
            cache.removeEntry(stringKey);

            return null;
        }
    }

    public void put(Object key, Object value, Date expired) {
        EntryRefreshPolicy refreshPolicy = null;

        if (expired != null) {
            int ttl = (int) (expired.getTime() - System.currentTimeMillis()) / 1000;

            refreshPolicy = new ExpiresRefreshPolicy(ttl);
        }

        cache.putInCache((String) key, value, refreshPolicy);
    }

    public void start() {

        // TODO Auto-generated method stub
    }

    public void stop() {

        // TODO Auto-generated method stub
    }
}

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
package org.richfaces.cache;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.RollbackException;
import javax.transaction.Status;
import javax.transaction.SystemException;
import javax.transaction.Transaction;

import org.jboss.cache.Fqn;
import org.jboss.cache.eviction.ExpirationAlgorithmConfig;
import org.richfaces.log.Logger;
import org.richfaces.log.RichfacesLogger;

/**
 * @author Nick Belaevski
 * @since 4.0
 */
public class JBossCacheCache implements Cache {
    private static final Logger LOGGER = RichfacesLogger.CACHE.getLogger();
    private static final String RESOURCE = "resource";
    private org.jboss.cache.Cache<String, Object> cache;

    public JBossCacheCache(org.jboss.cache.Cache<String, Object> cache) {
        super();
        this.cache = cache;
    }

    private Fqn<Object> createFqn(Object key) {
        return Fqn.fromElements(key);
    }

    public Object get(Object key) {
        return cache.get(createFqn(key), RESOURCE);
    }

    public void put(Object key, Object value, Date expired) {
        Map<String, Object> map = new HashMap<String, Object>(3);

        map.put(RESOURCE, value);

        if (expired != null) {
            map.put(ExpirationAlgorithmConfig.EXPIRATION_KEY, expired.getTime());
        }

        cache.put(createFqn(key), map);

        Transaction transaction = cache.getInvocationContext().getTransaction();

        try {

            // TODO: to review
            if ((transaction != null) && (transaction.getStatus() == Status.STATUS_ACTIVE)) {
                transaction.commit();
            }
        } catch (SystemException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (SecurityException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (IllegalStateException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (RollbackException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (HeuristicMixedException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (HeuristicRollbackException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public void start() {
        cache.start();
    }

    public void stop() {
        cache.stop();
    }
}

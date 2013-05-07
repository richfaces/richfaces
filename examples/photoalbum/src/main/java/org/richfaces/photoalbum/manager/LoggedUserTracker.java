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
package org.richfaces.photoalbum.manager;

import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;

/**
 * Special wrapper for Map, that contains pairs(userId, sessionId) to track what sessionId is actual by specified user. Used to
 * determine need to expire user's session, that session's id not equal to stored in this map.
 *
 * @author Andrey Markhel
 */
@ApplicationScoped
public class LoggedUserTracker {

    private Map<Long, String> loginnedUserIds = new HashMap<Long, String>();

    /**
     * Add user id and session id to store after each successfull authentication. Last authentication will be current
     *
     * @param id - user id to add
     * @param sessionId - sessionId
     */
    public void addUserId(Long id, String sessionId) {
        loginnedUserIds.put(id, sessionId);
    }

    /**
     * Remove user id from store after each logout
     *
     * @param id - user id to remove
     */
    public void removeUserId(Long id) {
        loginnedUserIds.remove(id);
    }

    /**
     * Checks if in the store contained user with specified user id and session id
     *
     * @param id - user id to check
     * @param sessionId - session id to check
     * @return true if such user contained in the store, that indicating that current user is actual.
     */
    public boolean containsUser(Long id, String sessionId) {
        String sessId = loginnedUserIds.get(id);
        return sessId != null && sessId.equals(sessionId);
    }

    /**
     * Checks if in the store contained user with specified user id
     *
     * @param id - user id to check
     * @return true if user with specified id contained in the store
     */
    public boolean containsUserId(Long id) {
        return loginnedUserIds.containsKey(id);
    }
}

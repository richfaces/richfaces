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
package org.richfaces.photoalbum.search;

import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.richfaces.photoalbum.model.User;
import org.richfaces.photoalbum.util.Constants;

/**
 * Strategy to retrieve shelves, that are shared or belongs to user, that perform search
 *
 * @author Andrey Markhel
 */
public class SearchBothShelvesStrategy implements ISearchStrategy {
    /**
     * Create query to retrieve shelves, that are shared or belongs to user, that perform search
     *
     * @param em - entityManager
     * @param params - map of additional params for this query
     * @param searchQuery - string to search
     * @return List of shelves that are shared or belongs to user, that perform search
     */
    public Query getQuery(EntityManager em, Map<String, Object> params, String searchQuery) {
        Query query = em.createQuery(Constants.SEARCH_SHELVES_QUERY + Constants.SEARCH_SHELF_BOTH_ADDON);
        query.setParameter(Constants.QUERY_PARAMETER, Constants.PERCENT + searchQuery.toLowerCase() + Constants.PERCENT);
        User user = (User) params.get(Constants.USER_PARAMETER);
        if (null == user) {
            return null;
        }
        query.setParameter(Constants.LOGIN_PARAMETER, user.getLogin());
        return query;
    }

}

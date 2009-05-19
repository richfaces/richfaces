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
package org.richfaces.photoalbum.search;

import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.richfaces.photoalbum.service.Constants;
/**
 * Strategy to retrieve users
 *
 * @author Andrey Markhel
 */
public class SearchUserStrategy implements ISearchStrategy {
	/**
	 * Create query to retrieve users
	 *
	 * @param em - entityManager
	 * @param params - map of additional params for this query
	 * @param searchQuery - string to search
	 * @return List of users
	 */
	public Query getQuery(EntityManager em, Map<String, Object> params, String searchQuery) {
		Query query = em.createQuery(Constants.SEARCH_USERS_QUERY);
		query.setParameter(Constants.QUERY_PARAMETER, Constants.PERCENT + searchQuery.toLowerCase() + Constants.PERCENT);
		return query;
	}

}

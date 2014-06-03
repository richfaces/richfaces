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

import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.richfaces.photoalbum.model.User;
import org.richfaces.photoalbum.util.Constants;
import org.richfaces.photoalbum.util.PhotoAlbumException;

/**
 * This class is factory to retrieve query object, dependent of type of searched entity and other parameters
 *
 * @author Andrey Markhel
 */
@ApplicationScoped
// @Startup
public class SearchQueryFactory {
    @Inject
    EntityManager em;

    /**
     * This method is return query object,that dependent of type of searched entity and other parameters
     *
     * @param entity - one of instances from SearchEntityEnum enumeration, indicate waht type of entities user want to search
     * @param user - user, that perform search
     * @param searchInMy - boolean parameter that indicate, is user want search in her space
     * @param searchInShared - boolean parameter that indicate, is user want search in all shared entities
     * @param queryString - string to search
     * @throws PhotoAlbumException - in case of wrong search parameters
     * @return query object, that dependent of type of searched entity and other parameters
     */
    public Query getQuery(SearchEntityEnum entity, User user, boolean searchInShared, boolean searchInMy, String queryString)
        throws PhotoAlbumException {
        ISearchStrategy strategy = null;
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put(Constants.USER_PARAMETER, user);
        switch (entity) {
            case USER: {
                strategy = new SearchUserStrategy();
                break;
            }
            case SHELF: {
                if (searchInShared && searchInMy) {
                    strategy = new SearchBothShelvesStrategy();
                } else if (searchInShared) {
                    strategy = new SearchSharedShelvesStrategy();
                } else if (searchInMy) {
                    strategy = new SearchMyShelvesStrategy();
                } else {
                    throw new PhotoAlbumException(Constants.WRONG_SEARCH_PARAMETERS_ERROR);
                }
                break;
            }
            case ALBUM: {
                if (searchInShared && searchInMy) {
                    strategy = new SearchBothAlbumsStrategy();
                } else if (searchInShared) {
                    strategy = new SearchSharedAlbumsStrategy();
                } else if (searchInMy) {
                    strategy = new SearchMyAlbumsStrategy();
                } else {
                    throw new PhotoAlbumException(Constants.WRONG_SEARCH_PARAMETERS_ERROR);
                }
                break;
            }
            case IMAGE: {
                if (searchInShared && searchInMy) {
                    strategy = new SearchBothImagesStrategy();
                } else if (searchInShared) {
                    strategy = new SearchSharedImagesStrategy();
                } else if (searchInMy) {
                    strategy = new SearchMyImagesStrategy();
                } else {
                    throw new PhotoAlbumException(Constants.WRONG_SEARCH_PARAMETERS_ERROR);
                }
                break;
            }
            case METATAG: {
                strategy = new SearchMetatagsStrategy();
                break;
            }
            default:
                throw new PhotoAlbumException(Constants.WRONG_SEARCH_PARAMETERS_ERROR);
        }
        return strategy.getQuery(em, parameters, queryString);
    }
}

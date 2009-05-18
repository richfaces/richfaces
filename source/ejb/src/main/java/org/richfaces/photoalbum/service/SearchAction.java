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
package org.richfaces.photoalbum.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.richfaces.photoalbum.domain.Album;
import org.richfaces.photoalbum.domain.Image;
import org.richfaces.photoalbum.domain.MetaTag;
import org.richfaces.photoalbum.domain.Shelf;
import org.richfaces.photoalbum.domain.User;

/**
 * Search Builder
 *  EJB3 Bean
 *
 * @author Andrey Markhel
 */

@Name("searchAction")
@Stateless
@AutoCreate
@SuppressWarnings("unchecked")
public class SearchAction implements ISearchAction {

	@In(value="entityManager")
	EntityManager em;
	
	@In private User user;
	
	/**
     * Return List of albums, founded by query
     * Search albums by name and description(like)
     * @param searchQuery - string to search
     * @param searchInMyAlbums - determine is search will be making by only user's albums
     * @param searchInShared - determine is search will be making in only shared albums
     * @return list of founded albums
     */
	public List<Album> searchByAlbum(String searchQuery, boolean searchInMyAlbums, boolean searchInShared) {
		StringBuilder b = new StringBuilder(Constants.SEARCH_ALBUM_QUERY);
		//If we search in user's albums
		if (searchInMyAlbums) {
			b.append(Constants.SEARCH_ALBUM_MY_ADDON);
		}
		//If we search only in shared albums
		if(searchInShared){
			b.append(Constants.SEARCH_ALBUM_SHARED_ADDON);
		}
		//Create query
		Query query = em.createQuery(b.toString());
		//Set search string
		query.setParameter(Constants.NAME_PARAMETER, Constants.PERCENT + searchQuery.toLowerCase() + Constants.PERCENT);
		//If we search only in shared albums
		//If we search in user's albums
		if (searchInMyAlbums) {
			query.setParameter(Constants.LOGIN_PARAMETER, user.getLogin());
		}
		//Get result
		return query.getResultList();
	}

	/**
     * Return List of images, founded by query
     * Search images by name and description(like)
     * @param searchQuery - string to search
     * @param searchInMyAlbums - determine is search will be making by only user's images
     * @param searchInShared - determine is search will be making in only shared images
     * @return list of founded images
     */
	public List<Image> searchByImage(String searchQuery, boolean searchInMyAlbums, boolean searchInShared) {
		StringBuilder b = new StringBuilder(Constants.SEARCH_IMAGE_QUERY);
		//If we search in user's images
		if (searchInMyAlbums) {
			b.append(Constants.SEARCH_IMAGE_MY_ADDON);
		}
		//If we search only in shared images
		if(searchInShared){
			b.append(Constants.SEARCH_IMAGE_SHARED_ADDON);
		}
		//Create query
		Query query = em.createQuery(b.toString());
		//Set search string
		query.setParameter(Constants.NAME_PARAMETER, Constants.PERCENT + searchQuery.toLowerCase() + Constants.PERCENT);
		//If we search only in shared images
		if (searchInMyAlbums) {
			query.setParameter(Constants.LOGIN_PARAMETER, user.getLogin());
		}
		//Get result
		return query.getResultList();
	}

	/**
     * Return List of users, founded by query
     * Search users by login, firstname and secondname(like)
     * @param searchQuery - string to search
     * @param searchInMyAlbums - unused
     * @param searchInShared - unused
     * @return list of founded users
     */
	public List<User> searchByUsers(String searchQuery, boolean searchInMyAlbums, boolean searchInShared) {
		StringBuilder b = new StringBuilder(Constants.SEARCH_USERS_QUERY);
		//Create query
		Query query = em.createQuery(b.toString());
		//Set search string
		query.setParameter(Constants.NAME_PARAMETER, Constants.PERCENT + searchQuery.toLowerCase() + Constants.PERCENT);
		//Get result
		return query.getResultList();
	}

	/**
     * Return List of metatags, founded by query
     * Search users by tagname(like)
     * @param searchQuery - string to search
     * @param searchInMyAlbums - unused
     * @param searchInShared - unused
     * @return list of founded metatags
     */
	public List<MetaTag> searchByTags(String searchQuery, boolean searchInMyAlbums, boolean searchInShared) {
		StringBuilder b = new StringBuilder(Constants.SEARCH_METATAG_QUERY);
		//Create query
		Query query = em.createQuery(b.toString());
		//Set search string
		query.setParameter(Constants.NAME_PARAMETER, Constants.PERCENT + searchQuery.toLowerCase() + Constants.PERCENT);
		//Get result
		return query.getResultList();
	}

	/**
     * Return List of shelves, founded by query
     * Search images by name and description(like)
     * @param searchQuery - string to search
     * @param searchInMyAlbums - determine is search will be making by only user's shelves
     * @param searchInShared - determine is search will be making in only shared shelves
     *	@return list of founded images
     */
	public List<Shelf> searchByShelves(String searchQuery, boolean searchInMyAlbums,
			boolean searchInShared) {
		StringBuilder b = new StringBuilder(Constants.SEARCH_SHELVES_QUERY);
		//If we search in user's shelves
		if (searchInMyAlbums) {
			b.append(Constants.SEARCH_QUERY_MY_ADDON);
		}
		//If we search only in shared shelves
		if (searchInShared) {
			b.append(Constants.SEARCH_QUERY_SHARED_ADDON);
		}
		//Create query
		Query query = em.createQuery(b.toString());
		//If we search in user's shelves
		if (searchInMyAlbums) {
			query.setParameter(Constants.LOGIN_PARAMETER, user.getLogin());
		}
		//Set search string
		query.setParameter(Constants.NAME_PARAMETER, Constants.PERCENT + searchQuery.toLowerCase() + Constants.PERCENT);
		//Get result
		return query.getResultList();
	}

}
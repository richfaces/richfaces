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
import org.richfaces.photoalbum.service.Constants;
import org.richfaces.photoalbum.service.PhotoAlbumException;

/**
 * This class is entry point to retrieve search result
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
	
	@In SearchQueryFactory searchQueryFactory;
	
	@In private User user;
	
	/**
     * Return List of albums, founded by query
     * Search albums by name and description(like)
     * @param searchQuery - string to search
     * @param searchInMyAlbums - determine is search will be making by only user's albums
     * @param searchInShared - determine is search will be making in only shared albums
     * @return list of founded albums
	 * @throws PhotoAlbumException if search parameter is wrong
     */
	public List<Album> searchByAlbum(String searchQuery, boolean searchInMyAlbums, boolean searchInShared) throws PhotoAlbumException {
		Query query = searchQueryFactory.getQuery(SearchEntityEnum.ALBUM, user, searchInShared, searchInMyAlbums, searchQuery);
		if(null == query){
			throw new PhotoAlbumException(Constants.WRONG_SEARCH_PARAMETERS_ERROR);
		}
		return (List<Album>)query.getResultList();
	}

	/**
     * Return List of images, founded by query
     * Search images by name and description(like)
     * @param searchQuery - string to search
     * @param searchInMyAlbums - determine is search will be making by only user's images
     * @param searchInShared - determine is search will be making in only shared images
     * @return list of founded images
	 * @throws PhotoAlbumException if search parameter is wrong
     */
	public List<Image> searchByImage(String searchQuery, boolean searchInMyAlbums, boolean searchInShared) throws PhotoAlbumException {
		Query query = searchQueryFactory.getQuery(SearchEntityEnum.IMAGE, user, searchInShared, searchInMyAlbums, searchQuery);
		if(null == query){
			throw new PhotoAlbumException(Constants.WRONG_SEARCH_PARAMETERS_ERROR);
		}
		return (List<Image>)query.getResultList();
	}

	/**
     * Return List of users, founded by query
     * Search users by login, firstname and secondname(like)
     * @param searchQuery - string to search
     * @param searchInMyAlbums - unused
     * @param searchInShared - unused
     * @return list of founded users
	 * @throws PhotoAlbumException if search parameter is wrong
     */
	public List<User> searchByUsers(String searchQuery, boolean searchInMyAlbums, boolean searchInShared) throws PhotoAlbumException {
		Query query = searchQueryFactory.getQuery(SearchEntityEnum.USER, user, searchInShared, searchInMyAlbums, searchQuery);
		if(null == query){
			throw new PhotoAlbumException(Constants.WRONG_SEARCH_PARAMETERS_ERROR);
		}
		return (List<User>)query.getResultList();
	}

	/**
     * Return List of metatags, founded by query
     * Search users by tagname(like)
     * @param searchQuery - string to search
     * @param searchInMyAlbums - unused
     * @param searchInShared - unused
     * @return list of founded metatags
	 * @throws PhotoAlbumException if search parameter is wrong
     */
	public List<MetaTag> searchByTags(String searchQuery, boolean searchInMyAlbums, boolean searchInShared) throws PhotoAlbumException {
		Query query = searchQueryFactory.getQuery(SearchEntityEnum.METATAG, user, searchInShared, searchInMyAlbums, searchQuery);
		if(null == query){
			throw new PhotoAlbumException(Constants.WRONG_SEARCH_PARAMETERS_ERROR);
		}
		return (List<MetaTag>)query.getResultList();
	}

	/**
     * Return List of shelves, founded by query
     * Search images by name and description(like)
     * @param searchQuery - string to search
     * @param searchInMyAlbums - determine is search will be making by only user's shelves
     * @param searchInShared - determine is search will be making in only shared shelves
     *	@return list of founded images
	 * @throws PhotoAlbumException if search parameter is wrong
     */
	public List<Shelf> searchByShelves(String searchQuery, boolean searchInMyAlbums,
			boolean searchInShared) throws PhotoAlbumException {
		Query query = searchQueryFactory.getQuery(SearchEntityEnum.SHELF, user, searchInShared, searchInMyAlbums, searchQuery);
		if(null == query){
			throw new PhotoAlbumException(Constants.WRONG_SEARCH_PARAMETERS_ERROR);
		}
		return (List<Shelf>)query.getResultList();
	}

}
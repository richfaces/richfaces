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

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Startup;
import org.richfaces.photoalbum.domain.User;
import org.richfaces.photoalbum.service.Constants;
import org.richfaces.photoalbum.service.PhotoAlbumException;
/**
 * This class is factory to retrieve query object, dependent of type of searched entity and other parameters
 *
 * @author Andrey Markhel
 */
@Name("searchQueryFactory")
@Scope(ScopeType.APPLICATION)
@Startup
public class SearchQueryFactory {
	@In(value="entityManager")
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
	public Query getQuery(SearchEntityEnum entity, User user, boolean searchInShared, boolean searchInMy, String queryString) throws PhotoAlbumException{
		ISearchStrategy strategy = null;
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put(Constants.USER_PARAMETER, user);
		switch(entity){
		case USER:{
			strategy = new SearchUserStrategy();break;
		}
		case SHELF:{
			if(searchInShared && searchInMy){
				strategy = new SearchBothShelvesStrategy();
			}else if(searchInShared){
				strategy = new SearchSharedShelvesStrategy();
			}else if(searchInMy){
				strategy = new SearchMyShelvesStrategy();
			}else{
				throw new PhotoAlbumException(Constants.WRONG_SEARCH_PARAMETERS_ERROR);
			}
			break;
		}
		case ALBUM:{
			if(searchInShared && searchInMy){
				strategy = new SearchBothAlbumsStrategy();
			}else if(searchInShared){
				strategy = new SearchSharedAlbumsStrategy();
			}else if(searchInMy){
				strategy = new SearchMyAlbumsStrategy();
			}else{
				throw new PhotoAlbumException(Constants.WRONG_SEARCH_PARAMETERS_ERROR);
			}
			break;
		}
		case IMAGE:{
			if(searchInShared && searchInMy){
				strategy = new SearchBothImagesStrategy();
			}else if(searchInShared){
				strategy = new SearchSharedImagesStrategy();
			}else if(searchInMy){
				strategy = new SearchMyImagesStrategy();
			}else{
				throw new PhotoAlbumException(Constants.WRONG_SEARCH_PARAMETERS_ERROR);
			}
			break;
		}
		case METATAG:{
			strategy = new SearchMetatagsStrategy();break;
		}
		default: throw new PhotoAlbumException(Constants.WRONG_SEARCH_PARAMETERS_ERROR);
		}
		return strategy.getQuery(em, parameters, queryString);
	}
}

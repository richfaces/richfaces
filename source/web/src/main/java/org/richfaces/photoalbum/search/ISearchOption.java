/**
 * License Agreement.
 *
 * Rich Faces - Natural Ajax for Java Server Faces (JSF)
 *
 * Copyright (C) 2007 Exadel, Inc.
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

import org.richfaces.photoalbum.search.ISearchAction;
import org.richfaces.photoalbum.service.PhotoAlbumException;

/**
 * Abstract class, that represent base functionality for particular search option(album, shelf, etc..)
 * @author Andrey Markavtsov
 *
 */
public abstract class ISearchOption {
	
	private boolean selected = true;
	
	private List<?> searchResult; 
	/**
	 * Abstract method, that return name of particular search option. This name used in UI as header of rich:tab. Must be implemented in sub-classes
	 *
	 * @return name
	 */
	public abstract String getName();
	
	/**
	 * Abstract method, that return description of particular search option. This description used in UI as header of page with search result. Must be implemented in sub-classes
	 *
	 * @return description of search option
	 */
	public abstract String getSearchResultName();
	
	/**
	 * Abstract method, that perform search in given option. Must be implemented in sub-classes
	 *
	 * @param action - action will be performed
	 * @param searchQuery - query to search
	 * @param searchInMyAlbums - is search in users albums will be performed
	 * @param searchInShared - is search in shared albums will be performed
	 * @throws PhotoAlbumException - in case of wrong search parameters
	 */
	public abstract void search(ISearchAction action, String searchQuery, boolean searchInMyAlbums, boolean searchInShared) throws PhotoAlbumException;
	
	public boolean getSelected() {
		return selected;
	}
	
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
	@Override
	public String toString() {
		return getName();
	}

	/**
	 * Abstract method, that return template to render of particular search option. Must be implemented in sub-classes
	 *
	 * @return template to render
	 */
	public abstract String getSearchResultTemplate();
	
	public List<?> getSearchResult() {
		return searchResult;
	}

	public void setSearchResult(List<?> searchResult) {
		this.searchResult = searchResult;
	}
}
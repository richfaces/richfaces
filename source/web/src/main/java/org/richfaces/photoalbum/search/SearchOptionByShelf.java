/**
 * 
 */
package org.richfaces.photoalbum.search;

import java.util.ArrayList;
import java.util.List;

import org.richfaces.photoalbum.domain.Shelf;
import org.richfaces.photoalbum.service.ISearchAction;

/**
 * Class, that encapsulate functionality related to search by shelf entity.
 * @author Andrey Markavtsov
 *
 */
public class SearchOptionByShelf extends ISearchOption {

	private static final String TEMPLATE = "/includes/search/result/shelfResult.xhtml";
	private static final String SHELF_SEARCH_RESULT = "Shelf search result";
	private static final String SHELVES = "Shelves";

	/* (non-Javadoc)
	 * @see org.richfaces.photoalbum.search.ISearchOption#getName()
	 */
	@Override
	public String getName() {
		return SHELVES;
	}

	/* (non-Javadoc)
	 * @see org.richfaces.photoalbum.search.ISearchOption#getSearchResultName()
	 */
	@Override
	public String getSearchResultName() {
		return SHELF_SEARCH_RESULT;
	}

	/* (non-Javadoc)
	 * @see org.richfaces.photoalbum.search.ISearchOption#getSearchResultTemplate()
	 */
	@Override
	public String getSearchResultTemplate() {
		return TEMPLATE;
	}

	/* (non-Javadoc)
	 * @see org.richfaces.photoalbum.search.ISearchOption#search(org.richfaces.photoalbum.service.ISearchAction, java.lang.String, boolean, boolean)
	 */
	@Override
	public void search(ISearchAction action, String searchQuery,
			boolean searchInMyAlbums, boolean searchInShared) {
		List<Shelf> list = action.searchByShelves(searchQuery, searchInMyAlbums, searchInShared);
		if(list != null){
			setSearchResult(list);
		}else{
			setSearchResult(new ArrayList<Shelf>());
		}
	}
}
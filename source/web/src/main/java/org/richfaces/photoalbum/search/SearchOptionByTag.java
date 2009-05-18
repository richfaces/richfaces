/**
 * 
 */
package org.richfaces.photoalbum.search;

import java.util.ArrayList;
import java.util.List;

import org.richfaces.photoalbum.domain.MetaTag;
import org.richfaces.photoalbum.service.ISearchAction;

/**
 * Class, that encapsulate functionality related to search by metatag entity.
 * @author Andrey Markavtsov
 *
 */
public class SearchOptionByTag extends ISearchOption {

	private static final String TEMPLATE = "/includes/search/result/tagsResult.xhtml";
	private static final String TAGS_SEARCH_RESULT = "Tags search result";
	private static final String TAGS = "Tags";

	/* (non-Javadoc)
	 * @see org.richfaces.photoalbum.search.ISearchOption#getName()
	 */
	@Override
	public String getName() {
		return TAGS;
	}

	/* (non-Javadoc)
	 * @see org.richfaces.photoalbum.search.ISearchOption#getSearchResultName()
	 */
	@Override
	public String getSearchResultName() {
		return TAGS_SEARCH_RESULT;
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
		List<MetaTag> searchByTags = action.searchByTags(searchQuery, searchInMyAlbums, searchInShared);
		if(searchByTags != null){
			setSearchResult(searchByTags);
		}else{
			setSearchResult(new ArrayList<MetaTag>());
		}
	}
}
/**
 * 
 */
package org.richfaces.photoalbum.search;

import java.util.ArrayList;
import java.util.List;

import org.richfaces.photoalbum.domain.User;
import org.richfaces.photoalbum.service.ISearchAction;

/**
 * Class, that encapsulate functionality related to search by user entity.
 * @author Andrey Markavtsov
 *
 */
public class SearchOptionByUser extends ISearchOption {

	private static final String TEMPLATE = "/includes/search/result/userResult.xhtml";
	private static final String USERS_SEARCH_RESULT = "Users search result";
	private static final String USERS = "Users";

	/* (non-Javadoc)
	 * @see org.richfaces.photoalbum.search.ISearchOption#getName()
	 */
	@Override
	public String getName() {
		return USERS;
	}

	/* (non-Javadoc)
	 * @see org.richfaces.photoalbum.search.ISearchOption#getSearchResultName()
	 */
	@Override
	public String getSearchResultName() {
		return USERS_SEARCH_RESULT;
	}

	/* (non-Javadoc)
	 * @see org.richfaces.photoalbum.search.ISearchOption#getSearchResultTemplate()
	 */
	@Override
	public String getSearchResultTemplate() {
		return TEMPLATE;
	}

	/* (non-Javadoc)
	 * @see org.richfaces.photoalbum.search.ISearchOption#search(org.richfaces.photoalbum.service.ISearchAction)
	 */
	@Override
	public void search(ISearchAction action, String q, boolean searchInMyAlbums, boolean searchInShared) {
		List<User> searchByUsers = action.searchByUsers(q, searchInMyAlbums, searchInShared);
		if(searchByUsers != null){
			setSearchResult(searchByUsers);
		}else{
			setSearchResult(new ArrayList<User>());
		}
	}
}
package org.richfaces.photoalbum.search;

import java.util.ArrayList;
import java.util.List;

import org.richfaces.photoalbum.domain.Album;
import org.richfaces.photoalbum.service.ISearchAction;
/**
 * Class, that encapsulate functionality related to search by album entity.
 * @author Andrey Markavtsov
 *
 */
public class SearchOptionByAlbum extends ISearchOption {
	
	private static final String TEMPLATE = "/includes/search/result/albumsResult.xhtml";
	private static final String ALBUMS_SEARCH_RESULT = "Albums search result";
	private static final String ALBUMS = "Albums";
	
	/* (non-Javadoc)
	 * @see org.richfaces.photoalbum.search.ISearchOption#getName()
	 */
	@Override
	public String getName() {
		return ALBUMS;
	}
	
	/* (non-Javadoc)
	 * @see org.richfaces.photoalbum.search.ISearchOption#getSearchResultName()
	 */
	@Override
	public String getSearchResultName() {
		return ALBUMS_SEARCH_RESULT;
	}
	
	/* (non-Javadoc)
	 * @see org.richfaces.photoalbum.search.ISearchOption#search()
	 */
	@Override
	public void search(ISearchAction action, String q, boolean searchInMyAlbums, boolean searchInShared) {
		List<Album> searchByAlbum = action.searchByAlbum(q, searchInMyAlbums, searchInShared);
		if(searchByAlbum != null){
			setSearchResult(searchByAlbum);
		}else{
			setSearchResult(new ArrayList<Album>());
		}
	}
	
	/* (non-Javadoc)
	 * @see org.richfaces.photoalbum.search.ISearchOption#getSearchResultTemplate()
	 */
	@Override
	public String getSearchResultTemplate() {
		return TEMPLATE;
	}
}
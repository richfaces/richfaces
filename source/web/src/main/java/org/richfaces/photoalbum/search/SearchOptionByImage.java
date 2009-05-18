/**
 * 
 */
package org.richfaces.photoalbum.search;

import java.util.ArrayList;
import java.util.List;

import org.richfaces.photoalbum.domain.Image;
import org.richfaces.photoalbum.service.ISearchAction;

/**
 * Class, that encapsulate functionality related to search by image entity.
 * @author Andrey Markavtsov
 *
 */
public class SearchOptionByImage extends ISearchOption {
	
	private static final String TEMPLATE = "/includes/search/result/imageResult.xhtml";
	private static final String IMAGES_SEARCH_RESULT = "Images search result";
	private static final String IMAGES = "Images";
	
	/* (non-Javadoc)
	 * @see org.richfaces.photoalbum.search.ISearchOption#getName()
	 */
	public String getName() {
		return IMAGES;
	}
	
	/* (non-Javadoc)
	 * @see org.richfaces.photoalbum.search.ISearchOption#getSearchResultName()
	 */
	@Override
	public String getSearchResultName() {
		return IMAGES_SEARCH_RESULT;
	}
	
	/* (non-Javadoc)
	 * @see org.richfaces.photoalbum.search.ISearchOption#search()
	 */
	@Override
	public void search(ISearchAction action, String q, boolean searchInMyAlbums, boolean searchInShared) {
		List<Image> searchByImage = action.searchByImage(q, searchInMyAlbums, searchInShared);
		if(searchByImage != null){
			setSearchResult(searchByImage);
		}else{
			setSearchResult(new ArrayList<Image>());
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
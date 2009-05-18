package org.richfaces.photoalbum.search;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.core.Events;
import org.richfaces.photoalbum.manager.NavigationEnum;
import org.richfaces.photoalbum.service.Constants;
import org.richfaces.photoalbum.service.ISearchAction;
/**
 * Class, that encapsulate functionality related to search process.
 * @author Andrey Markavtsov
 *
 */
@Name("searchImageHelper")
@Scope(ScopeType.CONVERSATION)
@AutoCreate
public class ImageSearchHelper implements Serializable {

	private static final long serialVersionUID = -304368268896942902L;
	
	@In ISearchAction searchAction;

	ISearchOption selectedOption;

	List<ISearchOption> options;
	
	String selectedTab;
	
	String searchQuery;
	
	String selectedKeyword;
	
	List<String> keywords = new ArrayList<String>();
	
	boolean seachInMyAlbums;
	
	boolean searchInShared = true;
	
	/**
	 * Default constructor. During instantiation populate in field options all possible search options
	 *
	 */
	public ImageSearchHelper() {
		options = new ArrayList<ISearchOption>();
		options.add(new SearchOptionByShelf());
		options.add(new SearchOptionByAlbum());
		options.add(new SearchOptionByImage());
		options.add(new SearchOptionByUser());
		options.add(new SearchOptionByTag());
	}
	
	/**
	 * Method, used to construct criteria string, to represent this string in UI.
	 */
	public String getCriteriaString(){
		StringBuilder s = new StringBuilder();
		for(ISearchOption option:options) {
			if(option.getSelected()){
				s.append(option.getName() + Constants.COMMA);
			}
		}
		if (s.length() >= 2) {
			s.delete(s.length() - 2, s.length());
		}
		return s.toString();
	}
	
	/**
	 * Method, that perform search, when user clicks by 'Find' button.
	 */
	public void search() {
		if(!isSearchOptionSelected()){
			//If no options selected
			Events.instance().raiseEvent(Constants.ADD_ERROR_EVENT, Constants.SEARCH_NO_OPTIONS_ERROR);
			return;
		}
		if(!isWhereSearchOptionSelected()){
			//If both search in My and search is shared unselected
			Events.instance().raiseEvent(Constants.ADD_ERROR_EVENT, Constants.SEARCH_NO_WHERE_OPTIONS_ERROR);
			return;
		}
		keywords = new ArrayList<String>();
		//Update view
		Events.instance().raiseEvent(Constants.UPDATE_MAIN_AREA_EVENT, NavigationEnum.SEARCH);
		// parse query
		keywords = parse(searchQuery);
		Iterator<ISearchOption> it = options.iterator();
		//Search by first keyword by default
		selectedKeyword = keywords.get(0).trim();
		while (it.hasNext()) {
			ISearchOption option = it.next();
			if (option.getSelected()) {
				option.search(searchAction, selectedKeyword , seachInMyAlbums, searchInShared);
			}
		}
	}

	/**
	 * Method, that perform search by particular phrase
	 * @param keyword - keyword to search
	 */
	public void search(String keyword) {
		if(!isSearchOptionSelected()){
			Events.instance().raiseEvent(Constants.ADD_ERROR_EVENT, Constants.SEARCH_NO_OPTIONS_ERROR);
			return;
		}
		Iterator<ISearchOption> it = options.iterator();
		selectedKeyword = keyword.trim();
		while (it.hasNext()) {
			ISearchOption option = it.next();
			if (option.getSelected()) {
				option.search(searchAction,selectedKeyword , seachInMyAlbums, searchInShared);
			}
		}
	}
	
	/**
	 * Method, invoked when user select or unselect search option.
	 */
	public void processSelection() {
		Iterator<ISearchOption> it = options.iterator();
		while (it.hasNext()) {
			ISearchOption option = it.next();
			if (option.getSelected()) {
				selectedOption = option;
				break;
			}
		}
	}

	public ISearchOption getSelectedOption() {
		return selectedOption;
	}

	public void setSelectedOption(ISearchOption selectedOption) {
		this.selectedOption = selectedOption;
	}
	
	public List<ISearchOption> getOptions() {
		return options;
	}

	public void setOptions(List<ISearchOption> options) {
		this.options = options;
	}

	public String getSearchQuery() {
		return searchQuery;
	}

	public void setSearchQuery(String searchQuery) {
		this.searchQuery = searchQuery;
	}

	public boolean isSeachInMyAlbums() {
		return seachInMyAlbums;
	}

	public void setSeachInMyAlbums(boolean seachInMyAlbums) {
		this.seachInMyAlbums = seachInMyAlbums;
	}

	public boolean isSearchInShared() {
		return searchInShared;
	}

	public void setSearchInShared(boolean searchInShared) {
		this.searchInShared = searchInShared;
	}

	public List<String> getKeywords() {
		return keywords;
	}

	public void setKeywords(List<String> keywords) {
		this.keywords = keywords;
	}

	public String getSelectedKeyword() {
		return selectedKeyword;
	}

	public void setSelectedKeyword(String selectedKeyword) {
		this.selectedKeyword = selectedKeyword;
	}
	
	public boolean isResultExist(){
		for(ISearchOption option : options){
			if(option.getSelected() && option.getSearchResult().size() > 0){
				return true;
			}
		}
		return false;
	}
	
	private List<String> parse(String searchQuery2) {
		return Arrays.asList(searchQuery2.split(Constants.COMMA));
	}

	private boolean isWhereSearchOptionSelected() {
		return seachInMyAlbums || searchInShared;
	}
	
	boolean isOptionSelected() {
		return selectedOption != null;
	}
	
	private boolean isSearchOptionSelected() {
		boolean isOptionSelected = false;
		for(ISearchOption i : options){
			if(i.getSelected()){
				isOptionSelected = true;
				break;
			}
		}
		return isOptionSelected;
	}
}
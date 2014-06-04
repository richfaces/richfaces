/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.richfaces.photoalbum.ui.search;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.inject.Named;

import org.richfaces.photoalbum.manager.NavigationEnum;
import org.richfaces.photoalbum.model.event.ErrorEvent;
import org.richfaces.photoalbum.model.event.EventType;
import org.richfaces.photoalbum.model.event.Events;
import org.richfaces.photoalbum.model.event.NavEvent;
import org.richfaces.photoalbum.search.ISearchAction;
import org.richfaces.photoalbum.util.Constants;
import org.richfaces.photoalbum.util.PhotoAlbumException;

/**
 * Class, that encapsulate functionality related to search process.
 *
 * @author Andrey Markavtsov
 *
 */

@Named
@ApplicationScoped
public class ImageSearchHelper implements Serializable {

    private static final long serialVersionUID = -304368268896942902L;

    @Inject
    ISearchAction searchAction;

    ISearchOption selectedOption;

    List<ISearchOption> options;

    String selectedTab;

    String searchQuery;

    String selectedKeyword;

    List<String> keywords = new ArrayList<String>();

    boolean seachInMyAlbums;

    boolean searchInShared = true;
    private SearchInformationHolder searchOptionsHolder;

    @Inject
    @EventType(Events.ADD_ERROR_EVENT)
    Event<ErrorEvent> error;
    @Inject
    @EventType(Events.UPDATE_MAIN_AREA_EVENT)
    Event<NavEvent> navEvent;

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
    public String getCriteriaString() {
        StringBuilder s = new StringBuilder();
        for (ISearchOption option : options) {
            if (option.getSelected()) {
                s.append(option.getName() + Constants.COMMA + " ");
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
        searchOptionsHolder = null;
        if (!isSearchOptionSelected()) {
            // If no options selected
            error.fire(new ErrorEvent(Constants.SEARCH_NO_OPTIONS_ERROR));
            return;
        }
        if (!isWhereSearchOptionSelected()) {
            // If both search in My and search is shared unselected
            error.fire(new ErrorEvent(Constants.SEARCH_NO_WHERE_OPTIONS_ERROR));
            return;
        }
        keywords = new ArrayList<String>();
        // Update view
        navEvent.fire(new NavEvent(NavigationEnum.SEARCH));
        // parse query
        keywords = parse(searchQuery);
        Iterator<ISearchOption> it = options.iterator();
        // Search by first keyword by default
        selectedKeyword = keywords.get(0).trim();
        while (it.hasNext()) {
            ISearchOption option = it.next();
            try {
                if (option.getSelected()) {
                    option.search(searchAction, selectedKeyword, seachInMyAlbums, searchInShared);
                }
            } catch (PhotoAlbumException e) {
                error.fire(new ErrorEvent("Error", option.getName() + ":" + e.getMessage()));
            }
        }
        searchOptionsHolder = new SearchInformationHolder(new ArrayList<ISearchOption>(options), seachInMyAlbums,
            searchInShared);
        searchQuery = "";
    }

    /**
     * Method, that perform search by particular phrase
     *
     * @param keyword - keyword to search
     */
    public void searchKeyword(String keyword) {
        if (!isSearchOptionSelected()) {
            error.fire(new ErrorEvent(Constants.SEARCH_NO_OPTIONS_ERROR));
            return;
        }
        Iterator<ISearchOption> it = searchOptionsHolder.getOptions().iterator();
        selectedKeyword = keyword.trim();
        while (it.hasNext()) {
            ISearchOption option = it.next();
            try {
                if (option.getSelected()) {
                    option.search(searchAction, selectedKeyword, searchOptionsHolder.isSeachInMyAlbums(),
                        searchOptionsHolder.isSearchInShared());
                }
            } catch (PhotoAlbumException e) {
                error.fire(new ErrorEvent(option.getName() + ":" + e.getMessage()));
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

    public boolean isResultExist() {
        for (ISearchOption option : options) {
            if (option.getSelected() && option.getSearchResult() != null && option.getSearchResult().size() > 0) {
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
        for (ISearchOption i : options) {
            if (i.getSelected()) {
                isOptionSelected = true;
                break;
            }
        }
        return isOptionSelected;
    }
}

class SearchInformationHolder {
    SearchInformationHolder(List<ISearchOption> options, boolean seachInMyAlbums, boolean searchInShared) {
        this.options = options;
        this.seachInMyAlbums = seachInMyAlbums;
        this.searchInShared = searchInShared;
    }

    List<ISearchOption> options;

    boolean seachInMyAlbums;

    boolean searchInShared;

    public List<ISearchOption> getOptions() {
        return options;
    }

    public boolean isSeachInMyAlbums() {
        return seachInMyAlbums;
    }

    public boolean isSearchInShared() {
        return searchInShared;
    }
}
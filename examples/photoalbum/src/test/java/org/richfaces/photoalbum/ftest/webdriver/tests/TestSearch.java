/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2016, Red Hat, Inc. and individual contributors
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
package org.richfaces.photoalbum.ftest.webdriver.tests;

import static org.junit.Assert.assertEquals;
import static org.richfaces.fragment.dataScroller.DataScroller.DataScrollerSwitchButton.LAST;

import java.util.EnumSet;
import java.util.List;

import org.jboss.arquillian.graphene.Graphene;
import org.junit.Test;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.common.picker.ChoicePickerHelper;
import org.richfaces.fragment.list.RichFacesList;
import org.richfaces.fragment.notify.RichFacesNotifyMessage;
import org.richfaces.fragment.switchable.SwitchType;
import org.richfaces.photoalbum.ftest.webdriver.fragments.SearchPanel.SearchOptionsEnum;
import org.richfaces.photoalbum.ftest.webdriver.fragments.view.AlbumView;
import org.richfaces.photoalbum.ftest.webdriver.fragments.view.GroupView;
import org.richfaces.photoalbum.ftest.webdriver.fragments.view.GroupView.AlbumPreview;
import org.richfaces.photoalbum.ftest.webdriver.fragments.view.GroupsView;
import org.richfaces.photoalbum.ftest.webdriver.fragments.view.SearchView;
import org.richfaces.photoalbum.ftest.webdriver.utils.PhotoalbumUtils;

import com.google.common.collect.Lists;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestSearch extends AbstractPhotoalbumTest {

    private static final List<String> TABNAMES = Lists.newArrayList("Album Group search result", "Albums search result",
        "Images search result", "Users search result", "Tags search result");
    private static final List<String> ALL_CRITERIAS = Lists.newArrayList("Album Groups", "Albums", "Images", "Users", "Tags");

    @FindBy(className = "rf-ulst")
    private RichFacesList list;

    private SearchView searchView() {
        SearchView searchView = getView(SearchView.class);
        searchView.getTabPanel().advanced().setSwitchType(SwitchType.CLIENT);
        return searchView;
    }

    @Test
    public void testSearchOptions_loggedUserSearchInOwn() {
        login();

        EnumSet<SearchOptionsEnum> allExceptInShared = EnumSet.allOf(SearchOptionsEnum.class);
        allExceptInShared.remove(SearchOptionsEnum.IN_SHARED);
        getPage().getSearchPanel().searchFor("a", allExceptInShared);
        assertEquals(2, searchView().getTabPanel().getNumberOfTabs());
        assertEquals(Lists.newArrayList("Users search result", "Tags search result"),
            PhotoalbumUtils.getStringsFromElements(searchView().getTabPanel().advanced().getAllVisibleHeadersElements()));

        // users
        AlbumView albumView = searchView().getTabPanel().switchTo(TABNAMES.get(3)).getContent(AlbumView.class);
        PhotoalbumUtils.checkNotVisible(albumView.getSlider().advanced().getRootElement(), albumView.getAlbumInfo(),
            albumView.getSlideShowLink(), albumView.getAlbumHeader().getRoot(), albumView.getDataScroller().advanced()
            .getRootElement());
        List<AlbumView.PhotoInfo> users = albumView.getPhotos();
        assertEquals(3, users.size());
        users.get(0).checkAll(200, "Andrey Markhel", JAN_DATE_85, "4 albums | 21 images", "/img/shell/avatar_default.png");
        users.get(1).checkAll(200, "John Smith", JAN_DATE_70, "2 albums | 8 images", "/img/shell/avatar_w_default.png");
        users.get(2).checkAll(200, "John Smith", JAN_DATE_70, "0 albums | 0 images", "/img/shell/avatar_w_default.png");

        // tags
        searchView().getTabPanel().switchTo(TABNAMES.get(4));
        list.getItems().size();
        assertEquals(18, list.getItems().size());
        assertEquals("Animals", list.getItem(0).getText());
        assertEquals("Cars", list.getItem(1).getText());
        assertEquals("Monuments and just buildings", list.getItem(2).getText());
        assertEquals("MyAlbum_121", list.getItem(ChoicePickerHelper.byIndex().beforeLast(2)).getText());
        assertEquals("MyAlbum_122", list.getItem(ChoicePickerHelper.byIndex().beforeLast(1)).getText());
        assertEquals("MyAlbum_123", list.getItem(ChoicePickerHelper.byIndex().last()).getText());
    }

    @Test
    public void testSearchOptions_searchInNowhere_messageWillShow() {
        // set all options off
        getPage().getSearchPanel().searchFor("a", EnumSet.noneOf(SearchOptionsEnum.class));
        RichFacesNotifyMessage message = getPage().getMessage();
        message.advanced().waitUntilMessageIsVisible().perform();
        assertEquals("You must select at least one search option", message.getDetail());
        message.close();
    }

    @Test
    public void testSearchOptions_searchOnlyForUsers() {
        getPage().getSearchPanel().searchFor("a", EnumSet.of(SearchOptionsEnum.IN_SHARED, SearchOptionsEnum.USERS));
        assertEquals(1, searchView().getTabPanel().getNumberOfTabs());
        AlbumView albumView = searchView().getTabPanel().switchTo(TABNAMES.get(3)).getContent(AlbumView.class);
        PhotoalbumUtils.checkNotVisible(albumView.getSlider().advanced().getRootElement(), albumView.getAlbumInfo(),
            albumView.getSlideShowLink(), albumView.getAlbumHeader().getRoot(), albumView.getDataScroller().advanced()
            .getRootElement());
        List<AlbumView.PhotoInfo> users = albumView.getPhotos();
        assertEquals(3, users.size());
        users.get(0).checkAll(200, "Andrey Markhel", JAN_DATE_85, "4 albums | 21 images", "/img/shell/avatar_default.png");
        users.get(1).checkAll(200, "John Smith", JAN_DATE_70, "2 albums | 8 images", "/img/shell/avatar_w_default.png");
        users.get(2).checkAll(200, "John Smith", JAN_DATE_70, "0 albums | 0 images", "/img/shell/avatar_w_default.png");
    }

    @Test
    public void testSearchOptions_searchWithoutOptions_messageWillShow() {
        // set all options off
        getPage().getSearchPanel().searchFor("a", EnumSet.noneOf(SearchOptionsEnum.class));
        RichFacesNotifyMessage message = getPage().getMessage();
        message.advanced().waitUntilMessageIsVisible().perform();
        assertEquals("You must select at least one search option", message.getDetail());
        message.close();
    }

    @Test
    public void testSearchWithAllResults() {
        String key = "a";
        getPage().getSearchPanel().searchFor(key);
        searchView().checkAll(TABNAMES.get(0), TABNAMES, ALL_CRITERIAS, key);
    }

    @Test
    public void testSearchWithAllResultsAlbums() {
        testSearchWithAllResults();

        GroupView content = searchView().getTabPanel().switchTo(TABNAMES.get(1)).getContent(GroupView.class);
        List<AlbumPreview> albumPreviews = content.getAlbumPreviews();
        albumPreviews.get(0).checkAll("Animals", IMAGES_DEC_DATE, "Andrey Markhel");
        albumPreviews.get(5).checkAll("Water", IMAGES_DEC_DATE, "John Smith");
        assertEquals(6, albumPreviews.size());
    }

    @Test
    public void testSearchWithAllResultsImages() {
        testSearchWithAllResults();

        AlbumView albumView = searchView().getTabPanel().switchTo(TABNAMES.get(2)).getContent(AlbumView.class);
        albumView.checkSliderVisible();
        PhotoalbumUtils.checkNotVisible(albumView.getAlbumInfo(), albumView.getSlideShowLink(), albumView.getAlbumHeader()
            .getRoot());
        assertEquals(4, albumView.getDataScroller().advanced().getCountOfVisiblePages());
        assertEquals(1, albumView.getDataScroller().getActivePageNumber());
        List<AlbumView.PhotoInfo> photos = albumView.getPhotos();
        assertEquals(8, photos.size());
        photos.get(0).checkAll(120, "1750979205_6e51b47ce9_o.jpg", IMAGES_DEC_DATE, "Andrey Markhel");
        photos.get(1).checkAll(120, "1906662004_655d0f6ccf_o.jpg", IMAGES_DEC_DATE, "Andrey Markhel");
        photos.get(2).checkAll(120, "4845901485_62db3c5d62_o.jpg", IMAGES_DEC_DATE, "Andrey Markhel");

        // interact with the scroller
        Graphene.guardAjax(albumView.getDataScroller()).switchTo(LAST);
        assertEquals(4, albumView.getDataScroller().advanced().getCountOfVisiblePages());
        assertEquals(4, albumView.getDataScroller().getActivePageNumber());
        photos = albumView.getPhotos();
        assertEquals(8, photos.size());
        photos.get(0).checkAll(120, "103193233_860c47c909_o.jpg", IMAGES_DEC_DATE, "Andrey Markhel");
    }

    @Test
    public void testSearchWithAllResultsGroups() {
        testSearchWithAllResults();

        GroupsView groupsView = searchView().getTabPanel().switchTo(TABNAMES.get(0)).getContent(GroupsView.class);
        GroupView group = groupsView.getGroups().get(0);
        group.checkAll("Nature", "Created 2009-12-18, contains 12 images into 2 albums", "Nature pictures", true);

    }

    @Test
    public void testSearchWithAllResultsTags() {
        testSearchWithAllResults();

        searchView().getTabPanel().switchTo(TABNAMES.get(4));
        list.getItems().size();
        assertEquals(18, list.getItems().size());
        assertEquals("Animals", list.getItem(0).getText());
        assertEquals("Cars", list.getItem(1).getText());
        assertEquals("Monuments and just buildings", list.getItem(2).getText());
        assertEquals("MyAlbum_121", list.getItem(ChoicePickerHelper.byIndex().beforeLast(2)).getText());
        assertEquals("MyAlbum_122", list.getItem(ChoicePickerHelper.byIndex().beforeLast(1)).getText());
        assertEquals("MyAlbum_123", list.getItem(ChoicePickerHelper.byIndex().last()).getText());
    }

    @Test
    public void testSearchWithAllResultsUsers() {
        testSearchWithAllResults();

        AlbumView albumView = searchView().getTabPanel().switchTo(TABNAMES.get(3)).getContent(AlbumView.class);
        PhotoalbumUtils.checkNotVisible(albumView.getSlider().advanced().getRootElement(), albumView.getAlbumInfo(),
            albumView.getSlideShowLink(), albumView.getAlbumHeader().getRoot(), albumView.getDataScroller().advanced()
            .getRootElement());
        List<AlbumView.PhotoInfo> users = albumView.getPhotos();
        assertEquals(3, users.size());
        users.get(0).checkAll(200, "Andrey Markhel", JAN_DATE_85, "4 albums | 21 images", "/img/shell/avatar_default.png");
        users.get(1).checkAll(200, "John Smith", JAN_DATE_70, "2 albums | 8 images", "/img/shell/avatar_w_default.png");
        users.get(2).checkAll(200, "John Smith", JAN_DATE_70, "0 albums | 0 images", "/img/shell/avatar_w_default.png");
    }

    @Test
    public void testSearchWithNoResults() {
        getPage().getSearchPanel().searchFor("+++");
        searchView().checkEmptyResults();
    }
}

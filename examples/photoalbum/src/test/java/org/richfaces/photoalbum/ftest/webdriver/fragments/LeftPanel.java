/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2014, Red Hat, Inc. and individual contributors
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
 *******************************************************************************/
package org.richfaces.photoalbum.ftest.webdriver.fragments;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.common.picker.ChoicePickerHelper;
import org.richfaces.fragment.tree.RichFacesTree;
import org.richfaces.photoalbum.ftest.webdriver.fragments.view.AlbumView;
import org.richfaces.photoalbum.ftest.webdriver.fragments.view.GroupView;
import org.richfaces.photoalbum.ftest.webdriver.fragments.view.GroupsView;
import org.richfaces.photoalbum.ftest.webdriver.pages.PhotoalbumPage;
import org.richfaces.photoalbum.ftest.webdriver.utils.PhotoalbumUtils;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class LeftPanel {

    public static final String MONUMENTS_PAGE = "Monuments";
    public static final String NATURE_PAGE = "Nature";
    public static final String PORTRAIT_PAGE = "Portrait";
    public static final String SPORT_AND_CARS_PAGE = "Sport & Cars";
    public static final String WATER_PAGE = "Water";
    private PhotoalbumPage page;

    @FindByJQuery("a:contains('Facebook Albums')")
    private WebElement fbGroupsLink;
    @FindByJQuery("td:has(> a:contains('G+ Albums')) + td a:contains(?)")

    private WebElement gPlusGroupsHelpLink;
    @FindByJQuery("a:contains('G+ Albums')")
    private WebElement gPlusGroupsLink;

    @FindByJQuery("td:has(> a:contains('My album groups')) + td a:contains(?)")
    private WebElement myGroupsHelpLink;
    @FindByJQuery("a:contains('My album groups')")
    private WebElement myGroupsLink;
    @FindBy(css = ".rf-tr[id$='userTree']")
    private CustomTree myGroupsTree;

    @FindByJQuery("td:has(> a:contains('Public album groups')) + td a:contains(?)")
    private WebElement preDefinedGroupsHelpLink;
    @FindByJQuery("a:contains('Public album groups')")
    private WebElement preDefinedGroupsLink;
    @FindBy(css = ".rf-tr[id$='PreDefinedTree']")
    private CustomTree preDefinedGroupsTree;

    public void checkIfUserLogged(boolean hasOwnAlbums, boolean hasFBAlbums, boolean hasGPlusAlbums) {
        checkVisibleForAll();
        PhotoalbumUtils.checkVisible(myGroupsHelpLink, myGroupsLink);
        if (hasOwnAlbums) {
            PhotoalbumUtils.checkVisible(Lists.newArrayList(myGroupsTree.advanced().getRootElement()));
        } else {
            PhotoalbumUtils.checkNotVisible(Lists.newArrayList(myGroupsTree.advanced().getRootElement()));
        }
        if (hasFBAlbums) {
            PhotoalbumUtils.checkVisible(Lists.newArrayList(fbGroupsLink));
        } else {
            PhotoalbumUtils.checkNotVisible(Lists.newArrayList(fbGroupsLink));
        }
        if (hasGPlusAlbums) {
            PhotoalbumUtils.checkVisible(Lists.newArrayList(gPlusGroupsLink, gPlusGroupsHelpLink));
        } else {
            PhotoalbumUtils.checkNotVisible(Lists.newArrayList(gPlusGroupsLink, gPlusGroupsHelpLink));
        }
    }

    public void checkIfUserNotLogged() {
        checkVisibleForAll();
        PhotoalbumUtils.checkNotVisible(Lists.newArrayList(myGroupsHelpLink, myGroupsLink, myGroupsTree.advanced().getRootElement(), fbGroupsLink, gPlusGroupsHelpLink, gPlusGroupsLink));
    }

    private void checkVisibleForAll() {
        PhotoalbumUtils.checkVisible(Lists.newArrayList(preDefinedGroupsHelpLink, preDefinedGroupsLink, preDefinedGroupsTree.advanced().getRootElement()));
    }

    public WebElement getGPlusGroupsHelpLink() {
        return gPlusGroupsHelpLink;
    }

    public WebElement getGPlusGroupsLink() {
        return gPlusGroupsLink;
    }

    public WebElement getMyGroupsHelpLink() {
        return myGroupsHelpLink;
    }

    public WebElement getMyGroupsLink() {
        return myGroupsLink;
    }

    public RichFacesTree getMyGroupsTree() {
        return myGroupsTree;
    }

    public WebElement getPreDefinedGroupsHelpLink() {
        return preDefinedGroupsHelpLink;
    }

    public WebElement getPreDefinedGroupsLink() {
        return preDefinedGroupsLink;
    }

    public RichFacesTree getPreDefinedGroupsTree() {
        return preDefinedGroupsTree;
    }

    public AlbumView openAlbumInOwnGroup(String albumName, String groupName) {
        Graphene.guardAjax(myGroupsTree.expandNode(ChoicePickerHelper.byVisibleText().contains(groupName))).selectNode(ChoicePickerHelper.byVisibleText().contains(albumName));
        AlbumView albumView = page.getContentPanel().albumView();
        Graphene.waitAjax().until().element(albumView.getAlbumHeader().getNameElement()).text().contains(albumName);
        return albumView;
    }

    public AlbumView openAlbumInPredefinedGroup(String albumName, String groupName) {
        Graphene.guardAjax(preDefinedGroupsTree.expandNode(ChoicePickerHelper.byVisibleText().contains(groupName))).selectNode(ChoicePickerHelper.byVisibleText().contains(albumName));
        AlbumView albumView = page.getContentPanel().albumView();
        Graphene.waitAjax().until().element(albumView.getAlbumHeader().getNameElement()).text().contains(albumName);
        return albumView;
    }

    public GroupView openOwnGroup(String name) {
        Graphene.guardAjax(myGroupsTree).selectNode(ChoicePickerHelper.byVisibleText().contains(name));
        GroupView groupView = page.getContentPanel().groupView();
        Graphene.waitAjax().until().element(groupView.getGroupHeader().getNameElement()).text().contains(name);
        return groupView;
    }

    public GroupView openOwnGroup(int index) {
        String text = myGroupsTree.advanced().getNodes().get(index).advanced().getLabelElement().getText();
        Graphene.guardAjax(myGroupsTree).selectNode(index);
        GroupView groupView = page.getContentPanel().groupView();
        Graphene.waitAjax().until().element(groupView.getGroupHeader().getNameElement()).text().contains(text);
        return groupView;
    }

    public GroupsView openOwnGroups(final int expectedGroupsCount) {
        Graphene.waitAjax().until().element(myGroupsLink).is().visible();
        Graphene.guardAjax(myGroupsLink).click();
        Graphene.waitAjax().until(new Predicate<WebDriver>() {
            private int size;

            @Override
            public boolean apply(WebDriver t) {
                size = page.getContentPanel().groupsView().getGroups().size();
                return size == expectedGroupsCount;
            }

            @Override
            public String toString() {
                return String.format("groups to have size <%s>, have <%s>", expectedGroupsCount, size);
            }

        });
        return page.getContentPanel().groupsView();
    }

    public GroupView openPredefinedGroup(int index) {
        String text = preDefinedGroupsTree.advanced().getNodes().get(index).advanced().getLabelElement().getText();
        Graphene.guardAjax(preDefinedGroupsTree).selectNode(index);
        GroupView groupView = page.getContentPanel().groupView();
        Graphene.waitAjax().until().element(groupView.getGroupHeader().getNameElement()).text().contains(text);
        return groupView;
    }

    public GroupView openPredefinedGroup(String name) {
        Graphene.guardAjax(myGroupsTree).selectNode(ChoicePickerHelper.byVisibleText().contains(name));
        GroupView groupView = page.getContentPanel().groupView();
        Graphene.waitAjax().until().element(groupView.getGroupHeader().getNameElement()).text().contains(name);
        return groupView;
    }

    public GroupsView openPredefinedGroups(final int expectedGroupsCount) {
        Graphene.waitAjax().until().element(preDefinedGroupsLink).is().visible();
        Graphene.guardAjax(preDefinedGroupsLink).click();
        Graphene.waitAjax().until(new Predicate<WebDriver>() {
            private int size;

            @Override
            public boolean apply(WebDriver t) {
                size = page.getContentPanel().groupsView().getGroups().size();
                return size == expectedGroupsCount;
            }

            @Override
            public String toString() {
                return String.format("groups to have size <%s>, have <%s>", expectedGroupsCount, size);
            }

        });
        return page.getContentPanel().groupsView();
    }

    public LeftPanel setPage(PhotoalbumPage page) {
        this.page = page;
        return this;
    }
}

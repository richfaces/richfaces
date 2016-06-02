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
package org.richfaces.photoalbum.ftest.webdriver.tests;

import static org.junit.Assert.assertEquals;

import org.jboss.arquillian.graphene.Graphene;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;
import org.richfaces.fragment.common.picker.ChoicePickerHelper;
import org.richfaces.fragment.tree.RichFacesTree;
import org.richfaces.fragment.tree.Tree.TreeNode;
import org.richfaces.photoalbum.ftest.webdriver.fragments.AddAlbumPanel;
import org.richfaces.photoalbum.ftest.webdriver.fragments.ConfirmationPanel;
import org.richfaces.photoalbum.ftest.webdriver.fragments.view.AlbumView;
import org.richfaces.photoalbum.ftest.webdriver.fragments.view.GroupView;
import org.richfaces.photoalbum.ftest.webdriver.fragments.view.GroupsView;

/**
 * Every method starts with login(), cannot put it in @BeforeMethod because of https://issues.jboss.org/browse/ARQGRA-309
 *
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestAddAndDeleteAlbum extends AbstractPhotoalbumTest {

    private final String albumGroupName = "Nature";
    private final String albumName = "New album";
    private final DateTime dt = new DateTime();
    private final DateTimeFormatter pattern = DateTimeFormat.forPattern("EEE MMM dd");

    private void addSingleAlbum() {
        login();
        AddAlbumPanel panel = getPage().getAddAlbumPanel();

        if (getPage().getLeftPanel().getMyGroupsTree().expandNode(ChoicePickerHelper.byVisibleText().contains(albumGroupName)).advanced().getNodes().size() != 3) {
            Graphene.guardAjax(getPage().getHeaderPanel().getToolbar().getAddAlbumLink()).click();
            panel = getPage().getAddAlbumPanel();
            panel.advanced().waitUntilPopupIsVisible().perform();
            panel.addAlbum(albumGroupName, albumName);
        }
    }

    /**
     * Used to clear album when there are 3 inside nature group. This is to ensure that when
     * executing deleteAlbum() test, it will firstly create one
     */
    private void clearCreatedAlbum() {
        login();
        if (getPage().getLeftPanel().getMyGroupsTree().expandNode(ChoicePickerHelper.byVisibleText().contains(albumGroupName)).advanced().getNodes().size() == 3) {
            AlbumView albumView = getPage().getLeftPanel().openAlbumInOwnGroup(albumName, albumGroupName);
            ConfirmationPanel confirmationPanel = getPage().getConfirmationPanel();
            Graphene.guardAjax(albumView.getAlbumHeader().getDeleteAlbumLink()).click();
            confirmationPanel = getPage().getConfirmationPanel();
            confirmationPanel.advanced().waitUntilPopupIsVisible().perform();
            confirmationPanel.ok();
        }
    }

    @Test
    public void addAlbum() {
        login();

        // open and cancel
        Graphene.guardAjax(getPage().getHeaderPanel().getToolbar().getAddAlbumLink()).click();
        AddAlbumPanel panel = getPage().getAddAlbumPanel();
        panel.advanced().waitUntilPopupIsVisible().perform();
        panel.cancel();

        // open and close
        Graphene.guardAjax(getPage().getHeaderPanel().getToolbar().getAddAlbumLink()).click();
        panel = getPage().getAddAlbumPanel();
        panel.advanced().waitUntilPopupIsVisible().perform();
        panel.cancel();

        // check initial state
        RichFacesTree myAlbumGroupsTree = getPage().getLeftPanel().getMyGroupsTree();
        assertEquals(2, myAlbumGroupsTree.advanced().getNodes().size());
        TreeNode node = myAlbumGroupsTree.expandNode(ChoicePickerHelper.byVisibleText().contains(albumGroupName));
        assertEquals(2, node.advanced().getNodes().size());

        // create album
        addSingleAlbum();

        // check changed state in left panel
        myAlbumGroupsTree = getPage().getLeftPanel().getMyGroupsTree();
        assertEquals(2, myAlbumGroupsTree.advanced().getNodes().size());
        node = myAlbumGroupsTree.expandNode(ChoicePickerHelper.byVisibleText().contains(albumGroupName));
        assertEquals(3, node.advanced().getNodes().size());
        // check changed state in album groups view
        GroupsView groupsView = getPage().getLeftPanel().openOwnGroups(2);
        groupsView.checkHeader("My album groups (2)");
        groupsView.getGroups().get(0).checkGroupHeader(albumGroupName, "Created 2009-12-18, contains 12 images into 3 albums");
        // check state in group view
        GroupView groupView = getPage().getLeftPanel().openOwnGroup(albumGroupName);
        groupView.checkGroupHeader(albumGroupName, "Created 2009-12-18, contains 12 images into 3 albums");

        // open album
        AlbumView albumView = getPage().getLeftPanel().openAlbumInOwnGroup(albumName, albumGroupName);

        // check data
        albumView.checkAlbumHeader(albumName, "Created " + dt.toString(pattern) + ".*" + dt.getYear() + ", contains 0 images");
        assertEquals(0, albumView.getPhotos().size());
        albumView.checkUserOwnsAlbum(true);
    }

    @Test
    public void deleteAlbum() {
        // clear & add album first (includes login)
        // clear because of string pattern when creating album
        clearCreatedAlbum();
        addSingleAlbum();

        //navigate to albums
        GroupView groupView = getPage().getLeftPanel().openOwnGroup(albumGroupName);
        AlbumView albumView = getPage().getLeftPanel().openAlbumInOwnGroup(albumName, albumGroupName);

        // cancel before delete
        Graphene.guardAjax(albumView.getAlbumHeader().getDeleteAlbumLink()).click();
        ConfirmationPanel confirmationPanel = getPage().getConfirmationPanel();
        confirmationPanel.advanced().waitUntilPopupIsVisible().perform();
        confirmationPanel
            .check("Are you sure? All images associated with this album will also be dropped! Click OK to proceed, otherwise click Cancel.");
        confirmationPanel.cancel();

        albumView.checkAlbumHeader(albumName, "Created " + dt.toString(pattern) + ".*" + dt.getYear() + ", contains 0 images");
        assertEquals(0, albumView.getPhotos().size());
        albumView.checkUserOwnsAlbum(true);

        // close before delete
        Graphene.guardAjax(albumView.getAlbumHeader().getDeleteAlbumLink()).click();
        confirmationPanel = getPage().getConfirmationPanel();
        confirmationPanel.advanced().waitUntilPopupIsVisible().perform();
        confirmationPanel.close();

        albumView.checkAlbumHeader(albumName, "Created " + dt.toString(pattern) + ".*" + dt.getYear() + ", contains 0 images");
        assertEquals(0, albumView.getPhotos().size());
        albumView.checkUserOwnsAlbum(true);

        // delete
        Graphene.guardAjax(albumView.getAlbumHeader().getDeleteAlbumLink()).click();
        confirmationPanel = getPage().getConfirmationPanel();
        confirmationPanel.advanced().waitUntilPopupIsVisible().perform();
        confirmationPanel.ok();

        // check
        RichFacesTree myAlbumGroupsTree = getPage().getLeftPanel().getMyGroupsTree();
        assertEquals(2, myAlbumGroupsTree.advanced().getNodes().size());
        assertEquals(2, myAlbumGroupsTree.expandNode(ChoicePickerHelper.byVisibleText().contains(albumGroupName)).advanced()
            .getNodes().size());
    }
}

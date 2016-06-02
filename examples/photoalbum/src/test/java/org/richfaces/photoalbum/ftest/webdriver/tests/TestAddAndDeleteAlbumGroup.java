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
import org.richfaces.fragment.tree.RichFacesTree;
import org.richfaces.photoalbum.ftest.webdriver.fragments.AddAlbumGroupPanel;
import org.richfaces.photoalbum.ftest.webdriver.fragments.ConfirmationPanel;
import org.richfaces.photoalbum.ftest.webdriver.fragments.view.GroupView;
import org.richfaces.photoalbum.ftest.webdriver.fragments.view.GroupsView;

/**
 * Every method starts with login(), cannot put it in @BeforeMethod because of https://issues.jboss.org/browse/ARQGRA-309
 * 
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestAddAndDeleteAlbumGroup extends AbstractPhotoalbumTest {

    private static final String GROUP_NAME = "New Album group";

    /**
     * Helper method used to add album group. 
     * Firstly logs in and then checks whether album was already added.
     */
    private void addGroup() {
        login();
        
        RichFacesTree myGroupsTree = getPage().getLeftPanel().getMyGroupsTree();
        
        // if album exists, do not add anything
        if (myGroupsTree.advanced().getNodes().size() != 3) {
         // create group
            AddAlbumGroupPanel panel = getPage().getAddAlbumGroupPanel();
            Graphene.guardAjax(getPage().getHeaderPanel().getToolbar().getAddAlbumGroupLink()).click();
            panel = getPage().getAddAlbumGroupPanel();
            panel.advanced().waitUntilPopupIsVisible().perform();
            panel.addGroup(GROUP_NAME, false);
        }
    }

    @Test
    public void addAlbumGroup() {
        login();

        DateTime dt = new DateTime();
        DateTimeFormatter pattern = DateTimeFormat.forPattern("EEE MMM dd");

        // open and cancel
        Graphene.guardAjax(getPage().getHeaderPanel().getToolbar().getAddAlbumGroupLink()).click();
        AddAlbumGroupPanel panel = getPage().getAddAlbumGroupPanel();
        panel.advanced().waitUntilPopupIsVisible().perform();
        panel.cancel();

        // open and close
        Graphene.guardAjax(getPage().getHeaderPanel().getToolbar().getAddAlbumGroupLink()).click();
        panel = getPage().getAddAlbumGroupPanel();
        panel.advanced().waitUntilPopupIsVisible().perform();
        panel.cancel();

        // check initial state
        RichFacesTree myGroupsTree = getPage().getLeftPanel().getMyGroupsTree();
        assertEquals(2, myGroupsTree.advanced().getNodes().size());
        assertEquals(0, myGroupsTree.advanced().getLeafNodes().size());

        addGroup();
        
        // check changed state in left panel
        myGroupsTree = getPage().getLeftPanel().getMyGroupsTree();
        assertEquals(3, myGroupsTree.advanced().getNodes().size());
        assertEquals(0, myGroupsTree.advanced().getNodes().get(2).advanced().getNodes().size());

        // check changed state in groups view
        GroupsView ownAlbumGroups = getPage().getLeftPanel().openOwnGroups(3);
        ownAlbumGroups.checkHeader("My album groups (3)");
        ownAlbumGroups
            .getGroups()
            .get(2)
            .checkAll(GROUP_NAME,
                "Created " + dt.toString(pattern) + ".*" + dt.getYear() + ", contains 0 images into 0 albums", "", true);

        // open group
        GroupView groupView = getPage().getLeftPanel().openOwnGroup(GROUP_NAME);

        // check data
        groupView.checkGroupHeader(GROUP_NAME, "Created " + dt.toString(pattern) + ".*" + dt.getYear()
            + ", contains 0 images into 0 albums");
        assertEquals(0, groupView.getAlbumPreviews().size());
        groupView.checkUserOwnsGroup(true);
    }

    @Test
    public void deleteAlbumGroup() {
        // firstly add album group with all the asserts (includes login())
        addGroup();

        // navigate to newly created group
        getPage().getLeftPanel().openOwnGroups(3);
        getPage().getLeftPanel().openOwnGroup(GROUP_NAME);
        
        GroupView groupView = getView(GroupView.class);
        // cancel before delete
        Graphene.guardAjax(groupView.getGroupHeader().getDeleteAlbumGroupLink()).click();
        ConfirmationPanel confirmationPanel = getPage().getConfirmationPanel();
        confirmationPanel.advanced().waitUntilPopupIsVisible().perform();
        confirmationPanel
            .check("Are You sure? All nested albums and images will also be dropped! Click OK to proceed, otherwise click Cancel.");
        confirmationPanel.cancel();

        // close before delete
        Graphene.guardAjax(groupView.getGroupHeader().getDeleteAlbumGroupLink()).click();
        confirmationPanel = getPage().getConfirmationPanel();
        confirmationPanel.advanced().waitUntilPopupIsVisible().perform();
        confirmationPanel.close();

        // delete
        Graphene.guardAjax(groupView.getGroupHeader().getDeleteAlbumGroupLink()).click();
        confirmationPanel = getPage().getConfirmationPanel();
        confirmationPanel.advanced().waitUntilPopupIsVisible().perform();
        confirmationPanel.ok();

        // check
        RichFacesTree myAlbumGroupsTree = getPage().getLeftPanel().getMyGroupsTree();
        assertEquals(2, myAlbumGroupsTree.advanced().getNodes().size());
        assertEquals(0, myAlbumGroupsTree.advanced().getLeafNodes().size());
    }
}

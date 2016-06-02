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

import static org.jboss.arquillian.graphene.Graphene.waitAjax;
import static org.junit.Assert.assertEquals;

import java.util.List;

import org.jboss.arquillian.graphene.Graphene;
import org.junit.Test;
import org.richfaces.fragment.tree.Tree.TreeNode;
import org.richfaces.photoalbum.ftest.webdriver.fragments.view.AlbumView;
import org.richfaces.photoalbum.ftest.webdriver.fragments.view.GroupView;
import org.richfaces.photoalbum.ftest.webdriver.fragments.view.GroupsView;
import org.richfaces.photoalbum.ftest.webdriver.utils.PhotoalbumUtils;

import com.google.common.collect.Lists;

/**
 *
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestLeftPanelNavigations extends AbstractPhotoalbumTest {

    private static final List<String> CUSTOM_SHELVES = Lists.newArrayList("Nature", "Sport & Cars");
    private static final List<String> PREDEFINED_SHELVES = Lists.newArrayList("Monuments", "Nature", "Portrait", "Sport & Cars", "Water");

    @Test
    public void testInitialState() {
        assertEquals(PREDEFINED_SHELVES, PhotoalbumUtils.getStringsFromElements(getPage().getLeftPanel().getPreDefinedGroupsTree().advanced().getNodesElements()));
    }

    @Test
    public void testInitialStateLoggedUser() {
        login();
        assertEquals(PREDEFINED_SHELVES, PhotoalbumUtils.getStringsFromElements(getPage().getLeftPanel().getPreDefinedGroupsTree().advanced().getNodesElements()));
        assertEquals(CUSTOM_SHELVES, PhotoalbumUtils.getStringsFromElements(getPage().getLeftPanel().getMyGroupsTree().advanced().getNodesElements()));
    }

    @Test
    public void testOwnAlbumView() {
        login();

        AlbumView album = getView(AlbumView.class);
        // check Nature group
        TreeNode node = getPage().getLeftPanel().getMyGroupsTree().expandNode(0);
        assertEquals(2, node.advanced().getNodes().size());
        // open Animals album
        Graphene.guardAjax(node).selectNode(0);
        waitAjax().until().element(album.getAlbumInfo()).text().contains("Animals");
        album.checkAll("Animals", "Created 2009-12-18, contains 6 images", "Animals pictures", "Album group: Nature");
        assertEquals(6, album.getPhotos().size());
        album.getPhotos().get(0).checkAll(120, "1750979205_6e51b47ce9_o.jpg", IMAGES_DEC_DATE);
        album.getPhotos().get(1).checkAll(120, "1906662004_655d0f6ccf_o.jpg", IMAGES_DEC_DATE);
        album.getPhotos().get(4).checkAll(120, "9855284863_da027be6cf_o.jpg", IMAGES_DEC_DATE);
        album.getPhotos().get(5).checkAll(120, "5395800621_c0bc80ca53_o.jpg", IMAGES_DEC_DATE);

        // check Sport & Cars group
        node = getPage().getLeftPanel().getMyGroupsTree().expandNode(1);
        assertEquals(2, node.advanced().getNodes().size());
        // open Sport album
        Graphene.guardAjax(node).selectNode(1);
        waitAjax().until().element(album.getAlbumInfo()).text().contains("Sport");
        album.checkAll("Sport", "Created 2009-12-18, contains 3 images", "Sport pictures", "Album group: Sport & Cars");
        assertEquals(3, album.getPhotos().size());
        album.getPhotos().get(0).checkAll(120, "103193233_860c47c909_o.jpg", IMAGES_DEC_DATE);
        album.getPhotos().get(1).checkAll(120, "1350250361_2d963dd4e7_o.jpg", IMAGES_DEC_DATE);
        album.getPhotos().get(2).checkAll(120, "2042654579_d25c0db64f_o.jpg", IMAGES_DEC_DATE);
    }

    @Test
    public void testOwnGroupView() {
        login();

        // check Nature group
        GroupView albumGroup = getPage().getLeftPanel().openOwnGroup(0);
        albumGroup.checkAll("Nature", "Created 2009-12-18, contains 12 images into 2 albums", "Nature pictures", false);
        assertEquals(2, albumGroup.getAlbumPreviews().size());
        albumGroup.getAlbumPreview(0).checkAll("Animals", IMAGES_DEC_DATE);
        albumGroup.getAlbumPreview(1).checkAll("Nature", IMAGES_DEC_DATE);

        // check Sport & Cars group
        getPage().getLeftPanel().openOwnGroup(1);
        albumGroup.checkAll("Sport & Cars", "Created 2009-12-18, contains 9 images into 2 albums", "Sport & Cars pictures", false);
        assertEquals(2, albumGroup.getAlbumPreviews().size());
        albumGroup.getAlbumPreview(0).checkAll("Cars", IMAGES_DEC_DATE);
        albumGroup.getAlbumPreview(1).checkAll("Sport", IMAGES_DEC_DATE);
    }

    @Test
    public void testOwnGroupsView() {
        login();
        GroupsView albumGroupsView = getPage().getLeftPanel().openOwnGroups(2);
        albumGroupsView.checkHeader("My album groups (2)");
        List<GroupView> groups = albumGroupsView.getGroups();
        assertEquals(2, groups.size());

        // check Monuments group
        GroupView group = groups.get(0);
        group.checkAll("Nature", "Created 2009-12-18, contains 12 images into 2 albums", "Nature pictures", true);
        assertEquals(2, group.getAlbumPreviews().size());
        group.getAlbumPreview(0).checkAll("Animals", IMAGES_DEC_DATE);
        group.getAlbumPreview(1).checkAll("Nature", IMAGES_DEC_DATE);

        // check Nature group
        group = groups.get(1);
        group.checkAll("Sport & Cars", "Created 2009-12-18, contains 9 images into 2 albums", "Sport & Cars pictures", true);
        assertEquals(2, group.getAlbumPreviews().size());
        group.getAlbumPreview(0).checkAll("Cars", IMAGES_DEC_DATE);
        group.getAlbumPreview(1).checkAll("Sport", IMAGES_DEC_DATE);
    }

    @Test
    public void testPublicAlbumView() {
        AlbumView album = getView(AlbumView.class);
        // check Monuments group
        TreeNode node = getPage().getLeftPanel().getPreDefinedGroupsTree().expandNode(0);
        assertEquals(1, node.advanced().getNodes().size());
        // open Monuments and just buildings album
        Graphene.guardAjax(node).selectNode(0);
        waitAjax().until().element(album.getAlbumHeader().getNameElement()).text().contains("Monuments");

        album.checkAll("Monuments and just buildings", "Created 2009-12-18, contains 3 images", "Monuments and just buildings pictures", "Album group: Monuments");
        assertEquals(3, album.getPhotos().size());
        album.getPhotos().get(0).checkAll(120, "8561041065_43449e8697_o.jpg", IMAGES_DEC_DATE);
        album.getPhotos().get(1).checkAll(120, "2523480499_e988ddf4c1_o.jpg", IMAGES_DEC_DATE);
        album.getPhotos().get(2).checkAll(120, "4065627169_2e3cea3acf_o.jpg", IMAGES_DEC_DATE);

        // check Nature group
        node = getPage().getLeftPanel().getPreDefinedGroupsTree().expandNode(1);
        assertEquals(2, node.advanced().getNodes().size());
        // open Animals album
        Graphene.guardAjax(node).selectNode(0);
        waitAjax().until().element(album.getAlbumHeader().getNameElement()).text().contains("Animals");

        album.checkAll("Animals", "Created 2009-12-18, contains 6 images", "Animals pictures", "Album group: Nature");
        assertEquals(6, album.getPhotos().size());
        album.getPhotos().get(0).checkAll(120, "1750979205_6e51b47ce9_o.jpg", IMAGES_DEC_DATE);
        album.getPhotos().get(1).checkAll(120, "1906662004_655d0f6ccf_o.jpg", IMAGES_DEC_DATE);
        album.getPhotos().get(4).checkAll(120, "9855284863_da027be6cf_o.jpg", IMAGES_DEC_DATE);
        album.getPhotos().get(5).checkAll(120, "5395800621_c0bc80ca53_o.jpg", IMAGES_DEC_DATE);

        assertEquals(2, node.advanced().getNodes().size());
        // open Nature album
        Graphene.guardAjax(node).selectNode(1);
        waitAjax().until().element(album.getAlbumHeader().getNameElement()).text().contains("Nature");

        album.checkAll("Nature", "Created 2009-12-18, contains 6 images", "Nature pictures", "Album group: Nature");
        assertEquals(6, album.getPhotos().size());
        album.getPhotos().get(0).checkAll(120, "294928909_01ab1f5696_o.jpg", IMAGES_DEC_DATE);
        album.getPhotos().get(1).checkAll(120, "1352614209_6bfde3b6c2_o.jpg", IMAGES_DEC_DATE);
        album.getPhotos().get(4).checkAll(120, "3392730627_1cdb18cba6_o.jpg", IMAGES_DEC_DATE);
        album.getPhotos().get(5).checkAll(120, "3392993334_36d7f097df_o.jpg", IMAGES_DEC_DATE);
    }

    @Test
    public void testPublicGroupView() {
        // check Monuments group
        GroupView group = getPage().getLeftPanel().openPredefinedGroup(0);
        group.checkAll("Monuments", "Created 2009-12-18, contains 3 images into 1 albums", "Monuments pictures", false);
        assertEquals(1, group.getAlbumPreviews().size());
        group.getAlbumPreview(0).checkAll("Monuments and just buildings", IMAGES_DEC_DATE);

        // check Nature group
        getPage().getLeftPanel().openPredefinedGroup(1);
        group.checkAll("Nature", "Created 2009-12-18, contains 12 images into 2 albums", "Nature pictures", false);
        assertEquals(2, group.getAlbumPreviews().size());
        group.getAlbumPreview(0).checkAll("Animals", IMAGES_DEC_DATE);
        group.getAlbumPreview(1).checkAll("Nature", IMAGES_DEC_DATE);
    }

    @Test
    public void testPublicGroupsView() {
        GroupsView groupssView = getPage().getLeftPanel().openPredefinedGroups(5);
        groupssView.checkHeader("Public album groups (5)");
        List<GroupView> groups = groupssView.getGroups();
        assertEquals(5, groups.size());

        // check Monuments group
        GroupView group = groups.get(0);
        group.checkAll("Monuments", "Created 2009-12-18, contains 3 images into 1 albums", "Monuments pictures", true);
        assertEquals(1, group.getAlbumPreviews().size());
        group.getAlbumPreview(0).checkAll("Monuments and just buildings", IMAGES_DEC_DATE);

        // check Nature group
        group = groups.get(1);
        group.checkAll("Nature", "Created 2009-12-18, contains 12 images into 2 albums", "Nature pictures", true);
        assertEquals(2, group.getAlbumPreviews().size());
        group.getAlbumPreview(0).checkAll("Animals", IMAGES_DEC_DATE);
        group.getAlbumPreview(1).checkAll("Nature", IMAGES_DEC_DATE);
    }
}

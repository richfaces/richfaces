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
import org.junit.Test;
import org.richfaces.photoalbum.ftest.webdriver.fragments.view.GroupsView;
import org.richfaces.photoalbum.ftest.webdriver.fragments.view.AllAlbumsView;
import org.richfaces.photoalbum.ftest.webdriver.fragments.view.AllImagesView;

/**
 * Every method starts with login(), cannot put it in @BeforeMethod because of https://issues.jboss.org/browse/ARQGRA-309
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestToolbarLinks extends AbstractPhotoalbumTest {

    @Test
    public void testToolbarViewMyAlbumGroups() {
        login();

        Graphene.guardAjax(getPage().getHeaderPanel().getToolbar().getMyAlbumGroupsLink()).click();
        GroupsView groupsView = getView(GroupsView.class);
        groupsView.checkHeader("My album groups (2)");
        assertEquals(2, groupsView.getGroups().size());
        groupsView.getGroups().get(0).checkAll("Nature", "Created 2009-12-18, contains 12 images into 2 albums", "Nature pictures", true);
        groupsView.getGroups().get(1).checkAll("Sport & Cars", "Created 2009-12-18, contains 9 images into 2 albums", "Sport & Cars pictures", true);
    }

    @Test
    public void testToolbarViewMyAllAlbums() {
        login();

        Graphene.guardAjax(getPage().getHeaderPanel().getToolbar().getMyAllAlbumsLink()).click();
        AllAlbumsView albumsView = getView(AllAlbumsView.class);
        albumsView.checkAlbumsHeader("My all albums (4)");
        assertEquals(4, albumsView.getAlbumPreviews().size());
        albumsView.getAlbumPreview(0).checkAll("Animals", IMAGES_DEC_DATE);
        albumsView.getAlbumPreview(1).checkAll("Nature", IMAGES_DEC_DATE);
        albumsView.getAlbumPreview(2).checkAll("Cars", IMAGES_DEC_DATE);
        albumsView.getAlbumPreview(3).checkAll("Sport", IMAGES_DEC_DATE);
    }

    @Test
    public void testToolbarViewMyAllImages() {
        login();

        Graphene.guardAjax(getPage().getHeaderPanel().getToolbar().getMyAllImagesLink()).click();

        AllImagesView allImagesView = getView(AllImagesView.class);
        allImagesView.checkHeader("My all images (21)");
        allImagesView.checkScrollerVisible();
        allImagesView.checkSliderVisible();
        assertEquals(2, allImagesView.getDataScroller().advanced().getCountOfVisiblePages());
        assertEquals(12, allImagesView.getPhotos().size());
        allImagesView.getPhotos().get(0).checkAll(120, "1750979205_6e51b47ce9_o.jpg", IMAGES_DEC_DATE);
        allImagesView.getPhotos().get(11).checkAll(120, "3392993334_36d7f097df_o.jpg", IMAGES_DEC_DATE);

        // scroll to next page
        Graphene.guardAjax(allImagesView.getDataScroller()).switchTo(2);
        assertEquals(9, allImagesView.getPhotos().size());
        allImagesView.getPhotos().get(0).checkAll(120, "190193308_ce2a4de5fa_o.jpg", IMAGES_DEC_DATE);
        allImagesView.getPhotos().get(8).checkAll(120, "2042654579_d25c0db64f_o.jpg", IMAGES_DEC_DATE);
    }
}

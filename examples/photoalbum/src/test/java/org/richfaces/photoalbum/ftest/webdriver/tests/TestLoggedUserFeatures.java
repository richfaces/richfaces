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
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.net.URISyntaxException;

import org.jboss.arquillian.graphene.Graphene;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.richfaces.fragment.inplaceInput.RichFacesInplaceInput;
import org.richfaces.photoalbum.ftest.webdriver.fragments.view.AlbumView;
import org.richfaces.photoalbum.ftest.webdriver.fragments.view.AlbumView.AlbumHeader;
import org.richfaces.photoalbum.ftest.webdriver.fragments.view.GroupView;
import org.richfaces.photoalbum.ftest.webdriver.fragments.view.PhotoView;

import category.FailingOnPhantomJS;


/**
 * Every method starts with login(), cannot put it in @BeforeMethod because of https://issues.jboss.org/browse/ARQGRA-309
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestLoggedUserFeatures extends AbstractPhotoalbumTest {

    private static final String BAD_IMAGE_TO_UPLOAD = "bad.txt";
    private static final String GOOD_IMAGE_TO_UPLOAD = "good.jpg";

    private File createFileFromString(String filename) {
        File file = null;
        try {
            file = new File(TestLoggedUserFeatures.class.getResource(filename).toURI());
        } catch (URISyntaxException ex) {
        }
        assertTrue("File does not exist.", file != null && file.exists());
        return file;
    }

    @Test
    @Category(FailingOnPhantomJS.class)
    public void testEditAlbumName() {
        login();

        // switch to Animals album and get header
        AlbumHeader albumHeader = getPage().getLeftPanel().openAlbumInOwnGroup("Animals", "Nature").getAlbumHeader();
        assertEquals("Animals", albumHeader.getNameElement().getText().trim());
        RichFacesInplaceInput input = albumHeader.getInput();
        assertEquals("Animals", input.getTextInput().getStringValue());

        // change the name of the Animals album
        Graphene.guardAjax(input.type("Animals album")).confirm();
        // check the change in left panel (navigation tree) and on current content view
        assertEquals("Animals album", albumHeader.getNameElement().getText().trim());
        assertEquals("Animals album", getPage().getLeftPanel().getMyGroupsTree().advanced().getFirstNode() // animals are first album in first own group
            .advanced().getFirstNode().advanced().getLabelElement().getText().trim());
        // check the change in the name of the link of inner photo
        PhotoView photoView = getPage().getContentPanel().albumView().getPhotos().get(0).open();
        String albumLinkText = photoView.getPhotoHeader().getLinks().get(1).getText();
        assertEquals("Album: Animals album", albumLinkText.trim());
    }

    @Test
    @Category(FailingOnPhantomJS.class)
    public void testEditPhotoName() {
        login();

        // switch to Animals album, open first photo
        PhotoView photoView = getPage().getLeftPanel().openAlbumInOwnGroup("Animals", "Nature").getPhotos().get(0).open();
        assertEquals("1750979205_6e51b47ce9_o.jpg", photoView.getPhotoHeader().getNameElement().getText().trim());
        RichFacesInplaceInput input = photoView.getPhotoHeader().getInput();
        assertEquals("1750979205_6e51b47ce9_o.jpg", input.getTextInput().getStringValue());
//        assertEquals(photoView.getPhotoHeader().getAdditionalInfo().getText().trim(), "1750979205_6e51b47ce9_o.jpg");

        // change photo name
        Graphene.guardAjax(input.type("firstPhoto.jpg")).confirm();
        // check photo name
        assertEquals("firstPhoto.jpg", photoView.getPhotoHeader().getNameElement().getText().trim());
    }

    @Test
    @Category(FailingOnPhantomJS.class)
    public void testEditAlbumGroupName() {
        login();

        // switch to Nature group and get header
        GroupView.GroupHeader albumGroupHeader = getPage().getLeftPanel().openOwnGroup("Nature").getGroupHeader();
        assertEquals("Nature", albumGroupHeader.getNameElement().getText().trim());
        RichFacesInplaceInput input = albumGroupHeader.getInput();
        assertEquals("Nature", input.getTextInput().getStringValue());

        // change the name of the group
        String albumGroupNewName = "Nature album group";
        Graphene.guardAjax(input.type(albumGroupNewName)).confirm();
        // check the change in left panel (navigation tree) and on current content view
        assertEquals(albumGroupNewName, albumGroupHeader.getNameElement().getText().trim());
        assertEquals(albumGroupNewName, getPage().getLeftPanel().getMyGroupsTree().advanced().getFirstNode()
            .advanced().getLabelElement().getText().trim());

        // check the change in the name of the link of inner album
        AlbumView albumView = getPage().getContentPanel().groupView().getAlbumPreviews().get(0).open();
        String albumLinkText = albumView.getAlbumHeader().getLinks().get(0).getText();
        assertEquals(String.format("Album group: %s", albumGroupNewName), albumLinkText.trim());
    }
}

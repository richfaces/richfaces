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
package org.richfaces.tests.photoalbum.ftest.webdriver.tests;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.io.File;
import java.net.URISyntaxException;

import org.jboss.arquillian.graphene.Graphene;
import org.richfaces.fragment.inplaceInput.RichFacesInplaceInput;
import org.richfaces.tests.photoalbum.ftest.webdriver.fragments.view.AlbumView;
import org.richfaces.tests.photoalbum.ftest.webdriver.fragments.view.AlbumView.AlbumHeader;
import org.richfaces.tests.photoalbum.ftest.webdriver.fragments.view.GroupView;
import org.richfaces.tests.photoalbum.ftest.webdriver.fragments.view.PhotoView;
import org.testng.annotations.Test;

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
        assertTrue(file != null && file.exists(), "File does not exist.");
        return file;
    }

    @Test
    public void testEditAlbumName() {
        login();

        // switch to Animals album and get header
        AlbumHeader albumHeader = page.getLeftPanel().openAlbumInOwnGroup("Animals", "Nature").getAlbumHeader();
        assertEquals(albumHeader.getNameElement().getText().trim(), "Animals");
        RichFacesInplaceInput input = albumHeader.getInput();
        assertEquals(input.getTextInput().getStringValue(), "Animals");

        // change the name of the Animals album
        Graphene.guardAjax(input.type("Animals album")).confirm();
        // check the change in left panel (navigation tree) and on current content view
        assertEquals(albumHeader.getNameElement().getText().trim(), "Animals album");
        assertEquals(page.getLeftPanel().getMyGroupsTree().advanced().getFirstNode() // animals are first album in first own group
            .advanced().getFirstNode().advanced().getLabelElement().getText().trim(), "Animals album");
        // check the change in the name of the link of inner photo
        PhotoView photoView = page.getContentPanel().albumView().getPhotos().get(0).open();
        String albumLinkText = photoView.getPhotoHeader().getLinks().get(1).getText();
        assertEquals(albumLinkText.trim(), "Album: Animals album");
    }

    @Test
    public void testEditPhotoName() {
        login();

        // switch to Animals album, open first photo
        PhotoView photoView = page.getLeftPanel().openAlbumInOwnGroup("Animals", "Nature").getPhotos().get(0).open();
        assertEquals(photoView.getPhotoHeader().getNameElement().getText().trim(), "1750979205_6e51b47ce9_o.jpg");
        RichFacesInplaceInput input = photoView.getPhotoHeader().getInput();
        assertEquals(input.getTextInput().getStringValue(), "1750979205_6e51b47ce9_o.jpg");
//        assertEquals(photoView.getPhotoHeader().getAdditionalInfo().getText().trim(), "1750979205_6e51b47ce9_o.jpg");

        // change photo name
        Graphene.guardAjax(input.type("firstPhoto.jpg")).confirm();
        // check photo name
        assertEquals(photoView.getPhotoHeader().getNameElement().getText().trim(), "firstPhoto.jpg");
    }

    @Test
    public void testEditAlbumGroupName() {
        login();

        // switch to Nature group and get header
        GroupView.GroupHeader albumGroupHeader = page.getLeftPanel().openOwnGroup("Nature").getGroupHeader();
        assertEquals(albumGroupHeader.getNameElement().getText().trim(), "Nature");
        RichFacesInplaceInput input = albumGroupHeader.getInput();
        assertEquals(input.getTextInput().getStringValue(), "Nature");

        // change the name of the group
        String albumGroupNewName = "Nature album group";
        Graphene.guardAjax(input.type(albumGroupNewName)).confirm();
        // check the change in left panel (navigation tree) and on current content view
        assertEquals(albumGroupHeader.getNameElement().getText().trim(), albumGroupNewName);
        assertEquals(page.getLeftPanel().getMyGroupsTree().advanced().getFirstNode()
            .advanced().getLabelElement().getText().trim(), albumGroupNewName);

        // check the change in the name of the link of inner album
        AlbumView albumView = page.getContentPanel().groupView().getAlbumPreviews().get(0).open();
        String albumLinkText = albumView.getAlbumHeader().getLinks().get(0).getText();
        assertEquals(albumLinkText.trim(), String.format("Album group: %s", albumGroupNewName));
    }
}

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
import org.richfaces.fragment.fileUpload.FileUploadItem;
import org.richfaces.fragment.fileUpload.RichFacesFileUpload;
import org.richfaces.fragment.notify.RichFacesNotifyMessage;
import org.richfaces.tests.photoalbum.ftest.webdriver.fragments.view.AddImagesView;
import org.richfaces.tests.photoalbum.ftest.webdriver.utils.PhotoalbumUtils;
import org.testng.annotations.Test;

/**
 * Every method starts with login(), cannot put it in @BeforeMethod because of https://issues.jboss.org/browse/ARQGRA-309
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestAddImage extends AbstractPhotoalbumTest {

    private static final String BAD_IMAGE_TO_UPLOAD = "bad.txt";
    private static final String GOOD_IMAGE_TO_UPLOAD = "good.jpg";

    private File getFileFromFileName(String filename) {
        File file = null;
        try {
            file = new File(AbstractPhotoalbumTest.class.getResource(filename).toURI());
        } catch (URISyntaxException ex) {
        }
        assertTrue(file != null && file.exists(), "File does not exist.");
        return file;
    }

    @Test
    public void testAddImage() {
        login();

        // open view
        Graphene.guardAjax(page.getHeaderPanel().getToolbar().getAddImagesLink()).click();
        AddImagesView addImagesView = getView(AddImagesView.class);

        // select album to add the pictures
        addImagesView.getSelect().openSelect().select("Nature");

        // upload the picture
        RichFacesFileUpload fileUpload = addImagesView.getFileUpload();
        fileUpload.addFile(getFileFromFileName(GOOD_IMAGE_TO_UPLOAD));
        Graphene.guardAjax(fileUpload).upload();
        PhotoalbumUtils.waitFor(4000);// implicit wait time to handle callbacks

        assertEquals(fileUpload.advanced().getItems().size(), 1);
        FileUploadItem uploadedItem = fileUpload.advanced().getItems().getItem(0);
        assertEquals(uploadedItem.getFilename(), GOOD_IMAGE_TO_UPLOAD);
        assertTrue(uploadedItem.isUploaded());

        AddImagesView.UploadedFilesPanel uploadedFilesPanel = addImagesView.getUploadedFilesPanel();
        assertEquals(uploadedFilesPanel.getCompleteLabel().getText(), "Image upload is completed: 1 images was uploaded to Nature");
        assertTrue(uploadedFilesPanel.uploadedImagesLabelIsVisible());
        assertEquals(uploadedFilesPanel.getUploadedPhotos().size(), 1);
        uploadedFilesPanel.getUploadedPhotos().get(0).checkAll(200, GOOD_IMAGE_TO_UPLOAD, String.valueOf(getFileFromFileName(GOOD_IMAGE_TO_UPLOAD).length() * 1.0));
    }

    @Test
    public void testAddInvalidImage_messageWillShow() {
        login();

        // open view
        Graphene.guardAjax(page.getHeaderPanel().getToolbar().getAddImagesLink()).click();
        AddImagesView addImagesView = getView(AddImagesView.class);

        // select album to add the pictures
        addImagesView.getSelect().openSelect().select("Nature");

        // upload the picture
        RichFacesFileUpload fileUpload = addImagesView.getFileUpload();
        fileUpload.addFile(getFileFromFileName(BAD_IMAGE_TO_UPLOAD));

        RichFacesNotifyMessage message = page.getMessage();
        message.advanced().waitUntilMessageIsVisible().perform();
        assertEquals(message.getSummary(), "Error");
        assertEquals(message.getDetail(), "Invalid file type. Only JPG is allowed.");
    }
}

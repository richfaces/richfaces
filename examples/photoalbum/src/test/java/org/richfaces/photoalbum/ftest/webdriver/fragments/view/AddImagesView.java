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
package org.richfaces.photoalbum.ftest.webdriver.fragments.view;

import java.util.Collections;
import java.util.List;

import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.common.Utils;
import org.richfaces.fragment.fileUpload.RichFacesFileUpload;
import org.richfaces.fragment.select.RichFacesSelect;
import org.richfaces.photoalbum.ftest.webdriver.fragments.view.AlbumView.PhotoInfo;
import org.richfaces.photoalbum.ftest.webdriver.utils.PhotoalbumUtils;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class AddImagesView {

    @FindBy(className = "rf-fu")
    private RichFacesFileUpload fileUpload;
    @FindByJQuery("a:contains(?):eq(1)")
    private WebElement fileUploadHelpLink;

    @FindBy(className = "rf-sel")
    private RichFacesSelect select;
    @FindByJQuery("a:contains(?):eq(0)")
    private WebElement buttonHelpLink;
    @FindBy(css = "[id$=filesPanel]")
    private UploadedFilesPanel uploadedFilesPanel;

    public void checkAllVisible() {
        PhotoalbumUtils.checkVisible(fileUploadHelpLink, fileUpload.advanced().getRootElement(),
            buttonHelpLink, select.advanced().getRootElement());
    }

    public WebElement getButtonHelpLink() {
        return buttonHelpLink;
    }

    public RichFacesFileUpload getFileUpload() {
        return fileUpload;
    }

    public WebElement getFileUploadHelpLink() {
        return fileUploadHelpLink;
    }

    public RichFacesSelect getSelect() {
        return select;
    }

    public UploadedFilesPanel getUploadedFilesPanel() {
        return uploadedFilesPanel;
    }

    public static class UploadedFilesPanel {

        @FindBy(css = "[id$=completeLabel]")
        private WebElement completeLabel;
        @FindBy(css = ".preview_box_photo_120")
        private List<PhotoInfo> uploadedPhotos;
        @FindByJQuery("span:contains('Successfully uploaded images:')")
        private WebElement uploadedImagesLabel;

        public WebElement getCompleteLabel() {
            return completeLabel;
        }

        public List<PhotoInfo> getUploadedPhotos() {
            return Collections.unmodifiableList(uploadedPhotos);
        }

        public boolean uploadedImagesLabelIsVisible() {
            return Utils.isVisible(uploadedImagesLabel);
        }
    }
}

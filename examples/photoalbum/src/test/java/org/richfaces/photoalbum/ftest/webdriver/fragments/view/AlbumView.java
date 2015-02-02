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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.richfaces.photoalbum.ftest.webdriver.tests.AbstractPhotoalbumTest.NO_OWNER;
import static org.richfaces.photoalbum.ftest.webdriver.tests.AbstractPhotoalbumTest.UNKNOWN_IMG_SRC;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.jboss.arquillian.graphene.fragment.Root;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.common.Utils;
import org.richfaces.fragment.dataScroller.RichFacesDataScroller;
import org.richfaces.fragment.inplaceInput.RichFacesInplaceInput;
import org.richfaces.fragment.inputNumberSlider.RichFacesInputNumberSlider;
import org.richfaces.photoalbum.ftest.webdriver.utils.PhotoalbumUtils;

import com.google.common.collect.Lists;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class AlbumView {

    @FindBy(className = "album-header-table")
    private AlbumHeader albumHeader;
    @FindBy(css = ".album-header-table + div")
    private WebElement albumInfo;

    @FindBy(className = "rf-insl")
    private RichFacesInputNumberSlider slider;
    @FindByJQuery("a:contains(?):eq(0)")
    private WebElement sliderHelpLink;

    @FindBy(css = "a.slideshow-link")
    private WebElement slideShowLink;

    @FindBy(className = "rf-ds")
    private RichFacesDataScroller dataScroller;

    @FindByJQuery("div[class^='preview_box_photo_']:visible")
    private List<PhotoInfo> photos;

    public void checkAll(String headerInfo, String headerAdditionalInfo, String albumInfo, String... linkText) {
        checkAlbumHeader(headerInfo, headerAdditionalInfo, linkText);
        checkAlbumInfo(albumInfo);
        checkSliderVisible();
    }

    public void checkSliderVisible() {
        assertTrue(Utils.isVisible(slider.advanced().getRootElement()));
    }

    public void checkAlbumHeader(String headerInfo, String headerAdditionalInfo, String... linkText) {
        albumHeader.checkAll(headerInfo, headerAdditionalInfo, linkText);
    }

    public void checkAlbumInfo(String info) {
        assertEquals(info, albumInfo.getText());
    }

    public void checkAlbumHeader(String headerInfo, String headerAdditionalInfo) {
        albumHeader.checkName(headerInfo);
        albumHeader.checkAdditionalInfo(headerAdditionalInfo);
    }

    public void checkUserOwnsAlbum(boolean owns) {
        ArrayList<WebElement> ownAlbumLinks = Lists.newArrayList(albumHeader.getDeleteAlbumLink(),
            albumHeader.getEditAlbumPropertiesLink());
        if (owns) {
            PhotoalbumUtils.checkVisible(ownAlbumLinks);
        } else {
            PhotoalbumUtils.checkNotVisible(ownAlbumLinks);
        }
    }

    public AlbumHeader getAlbumHeader() {
        return albumHeader;
    }

    public WebElement getAlbumInfo() {
        return albumInfo;
    }

    public RichFacesDataScroller getDataScroller() {
        return dataScroller;
    }

    public List<PhotoInfo> getPhotos() {
        return photos;
    }

    public WebElement getSlideShowLink() {
        return slideShowLink;
    }

    public RichFacesInputNumberSlider getSlider() {
        return slider;
    }

    public WebElement getSliderHelpLink() {
        return sliderHelpLink;
    }

    public void openSlideShow() {
        Graphene.guardAjax(getSlideShowLink()).click();
    }

    public static class AlbumHeader {

        @Root
        private WebElement root;

        @FindBy(css = ".xxx a")
        private List<WebElement> links;
        @FindBy(css = "td > h1")
        private WebElement nameElement;
        @FindBy(css = "td > h1 .rf-ii")
        private RichFacesInplaceInput input;
        @FindByJQuery(".additional-info-text:not('a')")
        private WebElement additionalInfo;
        @FindByJQuery(".album-header-table-col2 a:contains('Edit album properties')")
        private WebElement editAlbumPropertiesLink;
        @FindByJQuery(".album-header-table-col2 a:contains('Delete album')")
        private WebElement deleteAlbumLink;

        public void checkAll(String info, String additionalInfo, String... linkText) {
            checkName(info);
            checkAdditionalInfo(additionalInfo);
            assertEquals(getLinks().size(), linkText.length);
            assertEquals(Lists.newArrayList(linkText), PhotoalbumUtils.getStringsFromElements(links));
        }

        public void checkName(String name) {
            assertEquals(this.nameElement.getText().trim(), name);
        }

        public void checkAdditionalInfo(String additionalInfo) {
            String message = "Was " + this.getAdditionalInfo().getText().trim() + " , expected " + additionalInfo;
            assertTrue(message, this.getAdditionalInfo().getText().trim().matches(additionalInfo));
        }

        public WebElement getAdditionalInfo() {
            return additionalInfo;
        }

        public WebElement getDeleteAlbumLink() {
            return deleteAlbumLink;
        }

        public WebElement getEditAlbumPropertiesLink() {
            return editAlbumPropertiesLink;
        }

        public RichFacesInplaceInput getInput() {
            return input;
        }

        public List<WebElement> getLinks() {
            return Collections.unmodifiableList(links);
        }

        public WebElement getNameElement() {
            return nameElement;
        }

        public WebElement getRoot() {
            return root;
        }
    }

    public static class PhotoInfo {

        @ArquillianResource
        private WebDriver driver;

        @FindBy(css = "img.pr_photo_bg")
        private WebElement photoBackgroundImage;
        @FindByJQuery("a > img")
        private WebElement photoImage;
        @FindByJQuery("div.photo_data:eq(0)")
        private WebElement firstData;
        @FindByJQuery("div.photo_data:eq(1)")
        private WebElement secondData;
        @FindBy(css = "div.photo_name")
        private WebElement photoName;
        @FindBy(tagName = "a")
        private WebElement link;
        private PhotoView photoView;

        private PhotoView getPhotoView() {
            if (photoView == null) {
                photoView = Graphene.createPageFragment(PhotoView.class, driver.findElement(By.cssSelector(".content_box")));
            }
            return photoView;
        }

        public void checkAll(int photoSize, String name, Object data) {
            checkAll(photoSize, name, data, NO_OWNER);
        }

        public void checkAll(int photoSize, String name, Object data, Object secondData) {
            checkAll(photoSize, name, data, secondData, UNKNOWN_IMG_SRC);
        }

        public void checkAll(int photoSize, String name, Object data, Object secondData, String imageSource) {
            checkBackgroundPhoto(photoSize);
            checkPhotoName(name);
            checkFirstData(data);
            if (!secondData.equals(NO_OWNER)) {
                checkSecondData(secondData);
            } else {
                PhotoalbumUtils.checkNotVisible(this.secondData);
            }
            if (!imageSource.equals(UNKNOWN_IMG_SRC)) {
                checkPhotoSource(imageSource);
            }
        }

        private void checkSecondData(Object authorName) {
            assertEquals(authorName, secondData.getText());
        }

        private void checkBackgroundPhoto(int photoSize) {
            assertTrue(photoBackgroundImage.getAttribute("src").contains("/img/shell/frame_photo_" + photoSize + ".png"));
        }

        private void checkPhotoSource(String photoSrc) {
            assertTrue(photoImage.getAttribute("src").contains(photoSrc));
        }

        private void checkFirstData(Object data) {
            assertEquals(data, firstData.getText());
        }

        private void checkPhotoName(String name) {
            assertEquals(name, photoName.getText());
        }

        public PhotoView open() {
            Graphene.guardAjax(link).click();
            PhotoView pw = getPhotoView();
            Graphene.waitAjax().until().element(pw.getPhoto()).is().visible();
            return pw;
        }
    }
}

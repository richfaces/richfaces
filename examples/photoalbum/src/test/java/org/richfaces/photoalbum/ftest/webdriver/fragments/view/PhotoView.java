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
package org.richfaces.tests.photoalbum.ftest.webdriver.fragments.view;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.Collections;
import java.util.List;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.jboss.arquillian.graphene.fragment.Root;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.common.Utils;
import org.richfaces.fragment.editor.RichFacesEditor;
import org.richfaces.tests.photoalbum.ftest.webdriver.utils.PhotoalbumUtils;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class PhotoView {

    @FindBy(className = "image-header-table")
    private PhotoHeader photoHeader;
    @FindBy(tagName = "p")
    private WebElement photoInfo;

    @FindBy(css = "a.slideshow-link")
    private WebElement slideShowLink;

    @FindByJQuery("a:contains(?):last")
    private WebElement slideShowLinkHelp;
    @FindBy(css = "div[id$='imagesTable']")
    private ImagesScroller imagesScroller;

    @FindBy(css = "img[id$='img']")
    private WebElement photo;

    @FindByJQuery("> .additional-info-text")
    private List<WebElement> additionalInfos;

    @FindBy(css = "div[id$='commentPanel']")
    private CommentsPanel commentPanel;

    public void checkAll(String headerInfo, String headerAdditionalInfo, String albumInfo, String... linkText) {
        checkPhotoHeader(headerInfo, headerAdditionalInfo, linkText);
        checkPhotoInfo(albumInfo);
    }

    private void checkPhotoHeader(String headerInfo, String headerAdditionalInfo, String... linkText) {
        photoHeader.checkAll(headerInfo, headerAdditionalInfo, linkText);
    }

    private void checkPhotoInfo(String info) {
        assertEquals(photoInfo.getText(), info);
    }

    public List<WebElement> getAdditionalInfos() {
        return Collections.unmodifiableList(additionalInfos);
    }

    public CommentsPanel getCommentPanel() {
        return commentPanel;
    }

    public ImagesScroller getImagesScroller() {
        return imagesScroller;
    }

    public WebElement getPhoto() {
        return photo;
    }

    public AlbumView.AlbumHeader getPhotoHeader() {
        return photoHeader;
    }

    public WebElement getPhotoInfo() {
        return photoInfo;
    }

    public WebElement getSlideShowLink() {
        return slideShowLink;
    }

    public WebElement getSlideShowHelp() {
        return slideShowLinkHelp;
    }

    public static class ImagesScroller {

        @FindByJQuery("a:contains(?):last")
        private WebElement imageScrollerHelp;
        @FindBy(css = "span.image-scroller-left-arrow img")
        private WebElement scrollLeftImage;
        @FindBy(css = "span.image-scroller-right-arrow img")
        private WebElement scrollRightImage;
        @FindByJQuery("td:first > div")
        private List<ImagePreview> previews;

        public WebElement getImageScrollerHelp() {
            return imageScrollerHelp;
        }

        public List<ImagePreview> getPreviews() {
            return Collections.unmodifiableList(previews);
        }

        public ImagesScroller scrollLeft() {
            Graphene.guardAjax(scrollLeftImage).click();
            return this;
        }

        public ImagesScroller scrollRight() {
            Graphene.guardAjax(scrollRightImage).click();
            return this;
        }

        public static class ImagePreview {

            @Root
            private WebElement root;
            @FindBy(css = "img.pr_photo_bg")
            private WebElement photoBackgroundImage;
            @FindBy(css = "img[id$='scrollerImage']")
            private WebElement image;
            @FindBy(css = "div.photo_data")
            private WebElement photoData;
            @FindBy(css = "div.photo_name")
            private WebElement photoName;

            public void open() {
                Graphene.guardAjax(image).click();
            }

            public WebElement getImage() {
                return image;
            }

            public boolean isCurrent() {
                return root.getAttribute("class").contains("preview_box_photo_current");
            }

            public void checkAll(String data, String name, String photoSrc) {
                checkBackgroundPhoto();
                checkPhotoData(data);
                checkPhotoName(name);
            }

            private void checkBackgroundPhoto() {
                assertTrue(photoBackgroundImage.getAttribute("css").endsWith("/img/shell/frame_photo_80.png"));
            }

            private void checkPhotoData(String data) {
                assertEquals(photoData.getText(), data);
            }

            private void checkPhotoName(String name) {
                assertEquals(photoName.getText(), name);
            }
        }
    }

    public static class PhotoHeader extends AlbumView.AlbumHeader {

        @FindBy(css = "a")
        private List<WebElement> links;

        @Override
        public List<WebElement> getLinks() {
            return Collections.unmodifiableList(links);
        }
    }

    public static class CommentsPanel {

        @FindBy(tagName = "span")
        private WebElement panelName;
        @FindByJQuery("> div.comment")
        private List<Comment> comments;
        @FindBy(css = ".rf-ed[id$='editor2']")
        private RichFacesEditor addCommentEditor;
        @FindBy(css = ".photoalbumButton input")
        private WebElement addCommentButton;

        private void checkPanelName() {
            assertEquals(panelName.getText(), "Comments");
        }

        public void addComment(final String comment) {
            addCommentEditor.type(comment);
            //the page gets broken without the scrolling
            PhotoalbumUtils.scrollToElement(getAddCommentButton());
            Graphene.guardAjax(getAddCommentButton()).click();
        }

        public WebElement getAddCommentButton() {
            return addCommentButton;
        }

        public List<Comment> getComments() {
            return Collections.unmodifiableList(comments);
        }

        public static class Comment {

            @FindBy(css = "img.pr_photo_bg")
            private WebElement imageBackground;
            @FindBy(css = "a > img")
            private WebElement userImage;
            @FindBy(css = "div.comment-text")
            private WebElement commentText;
            @FindBy(css = "div.comment-deleteLink a")
            private WebElement deleteLink;
            @FindByJQuery("div.comment-deleteLink > span:eq(0)")
            private WebElement userName;
            @FindByJQuery("div.comment-deleteLink > span.additional-info-text")
            private WebElement additionalInfo;

            public void checkAll(Object info, String commentText, String userImage, String userName) throws AssertionError {
                checkAdditionalInfo(info);
                checkCommentText(commentText);
                checkImageBackground();
                checkUserImage(userImage);
                checkUserName(userName);
            }

            private void checkAdditionalInfo(Object info) {
                assertEquals(this.additionalInfo.getText(), info, "Additional info");
            }

            private void checkCommentText(String commentText) {
                assertEquals(this.commentText.getText(), commentText, "Comment text");
            }

            public void checkIfUsersComment() {
                assertTrue(Utils.isVisible(deleteLink), "Delete link is not visible.");
            }

            private void checkImageBackground() {
                assertTrue(imageBackground.getAttribute("src").contains("/img/shell/frame_photo_200.png"), "Image bg");
            }

            private void checkUserImage(String userImage) {
                assertTrue(this.userImage.getAttribute("src").contains(userImage), "User Image");
            }

            private void checkUserName(String userName) {
                assertEquals(this.userName.getText(), userName, "User name");
            }

            public void delete() {
                //the page gets broken without the scrolling
                PhotoalbumUtils.scrollToElement(deleteLink);
                Graphene.guardAjax(deleteLink).click();
            }

            public WebElement getUserImage() {
                return userImage;
            }
        }
    }
}

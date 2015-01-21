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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.richfaces.photoalbum.ftest.webdriver.tests.AbstractPhotoalbumTest.NO_OWNER;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.common.Utils;
import org.richfaces.fragment.inplaceInput.RichFacesInplaceInput;
import org.richfaces.photoalbum.ftest.webdriver.utils.PhotoalbumUtils;

import com.google.common.collect.Lists;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class GroupView {

    @FindBy(className = "shelf-header-table")
    private GroupHeader groupHeader;
    @FindBy(tagName = "p")
    private WebElement groupDescription;
    @FindBy(css = "div.preview_box_album_120")
    private List<AlbumPreview> albumPreviews;

    public void checkAll(String headerInfo, String headerAdditionalInfo, String groupInfo, boolean showGroupLinkVisible) {
        GroupView.this.checkGroupHeader(headerInfo, headerAdditionalInfo, showGroupLinkVisible);
        checkGroupDescription(groupInfo);
    }

    public void checkUserOwnsGroup(boolean owns) {
        ArrayList<WebElement> ownGroupLinks = Lists.newArrayList(groupHeader.getDeleteAlbumGroupLink(),
            groupHeader.getEditAlbumGroupPropertiesLink());
        if (owns) {
            PhotoalbumUtils.checkVisible(ownGroupLinks);
        } else {
            PhotoalbumUtils.checkNotVisible(ownGroupLinks);
        }
    }

    public void checkGroupHeader(String headerInfo, String headerAdditionalInfo, boolean showGroupLinkVisible) {
        groupHeader.checkAll(headerInfo, headerAdditionalInfo, showGroupLinkVisible);
    }

    public void checkGroupHeader(String headerInfo, String headerAdditionalInfo) {
        groupHeader.checkName(headerInfo);
        groupHeader.checkAdditionalInfo(headerAdditionalInfo);
    }

    private void checkGroupDescription(String info) {
        assertEquals(info, groupDescription.getText().trim());
    }

    public AlbumPreview getAlbumPreview(int index) {
        return albumPreviews.get(index);
    }

    public List<AlbumPreview> getAlbumPreviews() {
        return Collections.unmodifiableList(albumPreviews);
    }

    public GroupHeader getGroupHeader() {
        return groupHeader;
    }

    public WebElement getGroupInfo() {
        return groupDescription;
    }

    public static class GroupHeader {

        @FindByJQuery(".additional-info-text:not('a')")
        private WebElement additionalInfo;
        @FindByJQuery(".shelf-header-table-col2 a:contains('Delete album group')")
        private WebElement deleteAlbumGroupLink;
        @FindByJQuery(".shelf-header-table-col2 a:contains('Edit album group properties')")
        private WebElement editAlbumGroupPropertiesLink;
        @FindBy(css = "td > h1 .rf-ii")
        private RichFacesInplaceInput input;
        @FindBy(css = "td > h1")
        private WebElement name;
        @FindByJQuery(value = ".shelf-header-table-col2 a:contains('View album group')")
        private WebElement viewGroupLink;

        public void checkAdditionalInfo(String additionalInfo) {
            String val = this.getAdditionalInfo().getText().trim();
            assertTrue(String.format("Was <%s>, expected <%s%s", val, additionalInfo, '>'), val.matches(additionalInfo));
        }

        public void checkAll(String name, String additionalInfo, boolean visible) {
            checkAdditionalInfo(additionalInfo);
            checkName(name);
            checkViewGroupLinkVisible(visible);
        }

        public void checkName(String name) {
            assertEquals(name, this.name.getText().trim());
        }

        public void checkViewGroupLinkVisible(boolean visible) {
            assertEquals(visible, Utils.isVisible(viewGroupLink));
        }

        public WebElement getAdditionalInfo() {
            return additionalInfo;
        }

        public WebElement getDeleteAlbumGroupLink() {
            return deleteAlbumGroupLink;
        }

        public WebElement getEditAlbumGroupPropertiesLink() {
            return editAlbumGroupPropertiesLink;
        }

        public RichFacesInplaceInput getInput() {
            return input;
        }

        public WebElement getNameElement() {
            return name;
        }

        public WebElement getViewGroupLink() {
            return viewGroupLink;
        }
    }

    public static class AlbumPreview {

        @ArquillianResource
        private WebDriver driver;

        @FindBy(css = "img.pr_album_bg")
        private WebElement albumBackgroundImage;
        @FindBy(css = "img.album-cover-image")
        private WebElement albumCoverImage;
        @FindBy(css = "div.album_data")
        private WebElement albumData;
        @FindBy(css = ".album_data a")
        private WebElement albumOwnerLink;
        @FindBy(css = "div.album_name")
        private WebElement albumName;
        @FindBy(tagName = "a")
        private WebElement link;
        private AlbumView albumInfo;

        private AlbumView getAlbumView() {
            if (albumInfo == null) {
                albumInfo = Graphene.createPageFragment(AlbumView.class, driver.findElement(By.cssSelector(".content_box")));
            }
            return albumInfo;
        }

        public AlbumView open() {
            Graphene.guardAjax(link).click();
            return getAlbumView();
        }

        public void checkAll(String albumName, Object albumData) {
            checkAll(albumName, albumData, NO_OWNER);
        }

        public void checkAll(String albumName, Object albumData, String albumOwner) {
            checkAlbumBackGroundImage();
            checkAlbumData(albumData);
            checkAlbumName(albumName);
            checkAlbumOwner(albumOwner);
        }

        private void checkAlbumBackGroundImage() {
            assertTrue(albumBackgroundImage.isDisplayed());
        }

        private void checkAlbumData(Object data) {
            assertEquals(data, albumData.getText());
        }

        private void checkAlbumName(String name) {
            assertEquals(name, albumName.getText());
        }

        private void checkAlbumOwner(String owner) {
            if (owner.equals(NO_OWNER)) {
                assertFalse(Utils.isVisible(albumOwnerLink));
            } else {
                assertEquals(owner, albumOwnerLink.getText());
            }
        }
    }
}

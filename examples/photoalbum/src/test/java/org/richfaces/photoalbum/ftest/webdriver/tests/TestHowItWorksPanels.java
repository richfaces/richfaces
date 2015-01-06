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

import java.util.concurrent.TimeUnit;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.photoalbum.ftest.webdriver.fragments.HowItWorksPopupPanel;
import org.richfaces.tests.photoalbum.ftest.webdriver.fragments.view.AddImagesView;
import org.richfaces.tests.photoalbum.ftest.webdriver.fragments.view.EditUserProfileView;
import org.richfaces.tests.photoalbum.ftest.webdriver.fragments.view.PhotoView;
import org.richfaces.tests.photoalbum.ftest.webdriver.fragments.view.UserProfileView;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestHowItWorksPanels extends AbstractPhotoalbumTest {

    @FindBy(className = "rf-pp-cntr[id*='helpPanel']")
    private HowItWorksPopupPanel howItWorksPanel;
    @FindByJQuery("a:contains('?'):first")
    private WebElement howItWorksStatus;

    @Test
    public void testHowItWorksCustomButton() {
        login();
        Graphene.guardAjax(page.getHeaderPanel().getToolbar().getAddImagesLink()).click();
        Graphene.guardAjax(getView(AddImagesView.class).getButtonHelpLink()).click();
        howItWorksPanel.advanced().waitUntilPopupIsVisible().perform();
        howItWorksPanel.checkAll("How the button is created and how it acts");
    }

    @Test
    public void testHowItWorksFileUpload() {
        login();
        Graphene.guardAjax(page.getHeaderPanel().getToolbar().getAddImagesLink()).click();
        Graphene.guardAjax(getView(AddImagesView.class).getFileUploadHelpLink()).click();
        howItWorksPanel.advanced().waitUntilPopupIsVisible().perform();
        howItWorksPanel.checkAll("Upload Images");
    }

    @Test
    public void testHowItWorksImagesScroller() {
        PhotoView photoView = page.getLeftPanel().openAlbumInPredefinedGroup("Animals", "Nature").getPhotos().get(0).open();
        Graphene.guardAjax(photoView.getImagesScroller().getImageScrollerHelp()).click();
        howItWorksPanel.advanced().waitUntilPopupIsVisible().perform();
        howItWorksPanel.checkAll("Images Scroller");
    }

    @Test
    public void testHowItWorksInputNumberSlider() {
        Graphene.guardAjax(page.getLeftPanel().openAlbumInPredefinedGroup("Nature", "Nature").getSliderHelpLink()).click();
        howItWorksPanel.advanced().waitUntilPopupIsVisible().perform();
        howItWorksPanel.checkAll("Image Size Control with <rich:inputNumberSlider>");
    }

    @Test(enabled = false)
    public void testHowItWorksNavigationForGPlusAlbums() {
        gPlusLogin();

        Graphene.guardAjax(page.getLeftPanel().getGPlusGroupsHelpLink()).click();
        howItWorksPanel.advanced().waitUntilPopupIsVisible().perform();
        howItWorksPanel.checkAll("Google+ albums");
    }

    @Test
    public void testHowItWorksNavigationForOwnGroups() {
        login();

        Graphene.guardAjax(page.getLeftPanel().getMyGroupsHelpLink()).click();
        howItWorksPanel.advanced().waitUntilPopupIsVisible().perform();
        howItWorksPanel.checkAll("Navigation tree for a registered user");
    }

    @Test
    public void testHowItWorksNavigationForPredefinedGroups() {
        Graphene.waitAjax().until().element(page.getLeftPanel().getPreDefinedGroupsHelpLink()).is().visible();
        Graphene.guardAjax(page.getLeftPanel().getPreDefinedGroupsHelpLink()).click();
        howItWorksPanel.advanced().waitUntilPopupIsVisible().perform();
        howItWorksPanel.checkAll("Navigation tree with public album groups.");
    }

    @Test
    public void testHowItWorksSlideShow() {
        page.getLeftPanel().openAlbumInPredefinedGroup("Animals", "Nature").getPhotos().get(0).open();
        PhotoView photoView = getView(PhotoView.class);
        Graphene.guardAjax(photoView.getSlideShowHelp()).click();
        howItWorksPanel.advanced().waitUntilPopupIsVisible().perform();
        howItWorksPanel.checkAll("Slideshow mechanism.");
    }

    @Test
    public void testHowItWorksStatus() {
        Graphene.guardAjax(howItWorksStatus).click();
        howItWorksPanel.advanced().waitUntilPopupIsVisible().perform();
        howItWorksPanel.checkAll("The <a4j:status> component");
    }

    @Test
    public void testHowItWorksValidation() {
        login();

        Graphene.guardAjax(page.getHeaderPanel().getLoggedUserLink()).click();
        UserProfileView preView = getView(UserProfileView.class);
        preView.openEditProfile();

        EditUserProfileView view = getView(EditUserProfileView.class);
        Graphene.guardAjax(view.getValidationHelpLink()).click();
        howItWorksPanel.advanced().waitUntilPopupIsVisible().withTimeout(10, TimeUnit.SECONDS).perform();
        howItWorksPanel.checkAll("User Input Data Validation");
    }
}

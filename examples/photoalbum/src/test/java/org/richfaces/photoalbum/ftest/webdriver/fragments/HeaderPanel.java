/*
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
 */
package org.richfaces.tests.photoalbum.ftest.webdriver.fragments;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.jboss.arquillian.graphene.findby.ByJQuery;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.photoalbum.ftest.webdriver.pages.FBLoginPage;
import org.richfaces.tests.photoalbum.ftest.webdriver.pages.GPlusLoginPage;
import org.richfaces.tests.photoalbum.ftest.webdriver.pages.SocialLoginPage;
import org.richfaces.tests.photoalbum.ftest.webdriver.utils.PhotoalbumUtils;

import com.google.common.collect.Lists;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class HeaderPanel {

    @ArquillianResource
    private WebDriver driver;

    @FindByJQuery("div.header-content-div a:has('> img')")
    private WebElement imageIndexLink;

    @FindByJQuery("div.header-content-div div[id$='logInOutMenu'] span.logged-user")
    private WebElement loggedUserSpan;
    @FindByJQuery("div.header-content-div div[id$='logInOutMenu'] span.logged-user + a")
    private WebElement loggedUserLink;
    @FindByJQuery("div.header-content-div div[id$='logInOutMenu'] a:contains('Login')")
    private WebElement loginLink;
    @FindByJQuery("div.header-content-div div[id$='logInOutMenu'] a:contains('Logout')")
    private WebElement logoutLink;
    @FindByJQuery("div.header-content-div div[id$='logInOutMenu'] a:contains('Register')")
    private WebElement registerLink;
    @FindBy(css = "div.header-content-div div[id$='logInOutMenu'] a.FB-btn-small")
    private WebElement loginToFBLink;
    @FindBy(css = "div.header-content-div div[id$='logInOutMenu'] span.FB-btn-small")
    private WebElement loggedInWithFB;
    @FindByJQuery("div.header-content-div div[id$='logInOutMenu'] span.logged-user + a + img")
    private WebElement fbLoggedUserImage;
    @FindBy(css = "div.header-content-div div[id$='logInOutMenu'] a.G-btn-small")
    private WebElement loginToGPlusLink;
    @FindBy(css = "div.header-content-div div[id$='logInOutMenu'] span.G-btn-small")
    private WebElement loggedInWithGPlus;

    @FindByJQuery("div.header-content-div div.top-right-menu a:contains('Wiki page')")
    private WebElement wikiPageLink;
    @FindByJQuery("div.header-content-div div.top-right-menu a:contains('Downloads')")
    private WebElement downloadsLink;
    @FindByJQuery("div.header-content-div div.top-right-menu a:contains('Community')")
    private WebElement communityLink;

    @FindByJQuery("div[id$='menuPanel'] .rf-tb")
    private Toolbar toolbar;

    private void checkAlwaysPresentElements() {
        PhotoalbumUtils.checkVisible(Lists.newArrayList(imageIndexLink, loggedUserSpan, wikiPageLink, downloadsLink, communityLink));
        WebElement image = imageIndexLink.findElement(By.tagName("img"));
        assertTrue(image.getAttribute("src").contains("img/shell/logo_top.gif"));
    }

    public void checkUserLogged(String user, boolean hasOwnAlbums, boolean isLoggedWithFB, boolean isloggedWithGPlus) {
        checkAlwaysPresentElements();
        PhotoalbumUtils.checkVisible(Lists.newArrayList(loggedUserLink, logoutLink));
        PhotoalbumUtils.checkNotVisible(loginLink, registerLink);
        assertEquals(loggedUserSpan.getText(), "Welcome,");
        assertEquals(loggedUserLink.getText(), user);
        if (isLoggedWithFB) {
            PhotoalbumUtils.checkVisible(loggedInWithFB, fbLoggedUserImage);
            PhotoalbumUtils.checkNotVisible(loginToFBLink);
        } else {
            PhotoalbumUtils.checkVisible(loginToFBLink);
            PhotoalbumUtils.checkNotVisible(loggedInWithFB, fbLoggedUserImage);
        }
        if (isloggedWithGPlus) {
            PhotoalbumUtils.checkVisible(loggedInWithGPlus);
            PhotoalbumUtils.checkNotVisible(loginToGPlusLink);
        } else {
            PhotoalbumUtils.checkVisible(loginToGPlusLink);
            PhotoalbumUtils.checkNotVisible(loggedInWithGPlus);
        }
        getToolbar().checkIfUserLoggedToolbar(hasOwnAlbums);
    }

    public void checkIfUserNotLogged() {
        checkAlwaysPresentElements();
        PhotoalbumUtils.checkVisible(Lists.newArrayList(loginLink, registerLink));
        PhotoalbumUtils.checkNotVisible(loggedUserLink, logoutLink, loggedInWithFB, loggedInWithGPlus, loginToFBLink, loginToGPlusLink, fbLoggedUserImage);
        assertEquals(loggedUserSpan.getText().trim(), "Welcome, guest! If you want access to full version of application, please register or login.");
        getToolbar().checkIfUserNotLoggedToolbar();
    }

    public WebElement getCommunityLink() {
        return communityLink;
    }

    public WebElement getDownloadsLink() {
        return downloadsLink;
    }

    public WebElement getLoggedUserLink() {
        return loggedUserLink;
    }

    public WebElement getLoggedUserSpan() {
        return loggedUserSpan;
    }

    public WebElement getLoginLink() {
        return loginLink;
    }

    public WebElement getLogoutLink() {
        return logoutLink;
    }

    public WebElement getRegisterLink() {
        return registerLink;
    }

    public WebElement getStatusHelpLink() {
        return driver.findElement(ByJQuery.selector("a:visible:last"));
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    public WebElement getWikiPageLink() {
        return wikiPageLink;
    }

    public void loginToFB() {
        loginToSocial(FBLoginPage.class);
    }

    public void loginToGPlus() {
        loginToSocial(GPlusLoginPage.class);
    }

    private void loginToSocial(Class<? extends SocialLoginPage> pageClass) {
        PhotoalbumUtils.loginWithSocial(pageClass, driver, GPlusLoginPage.class.equals(pageClass) ? loginToGPlusLink : loginToFBLink);
    }

    public static class Toolbar {

        @FindByJQuery(".rf-tb-cntr > td > * > div:eq(1)")
        private WebElement myAlbumGroupsLink;
        @FindByJQuery(".rf-tb-cntr > td > * > div:eq(2)")
        private WebElement myAllAlbumsLink;
        @FindByJQuery(".rf-tb-cntr > td > * > div:eq(3)")
        private WebElement myAllImagesLink;
        @FindByJQuery(".rf-tb-cntr > td > * > div:eq(4) > a:eq(0)")
        private WebElement addAlbumGroupLink;
        @FindByJQuery(".rf-tb-cntr > td > * > div:eq(4) > a:eq(1)")
        private WebElement addAlbumLink;
        @FindByJQuery(".rf-tb-cntr > td > * > div:eq(4) > span")
        private WebElement addAlbumLinkSpan;
        @FindByJQuery(".rf-tb-cntr > td > * > div:eq(4) > a:eq(2)")
        private WebElement addImagesLink;
        @FindByJQuery(".rf-tb-cntr > td > * > div:eq(4) > span")
        private WebElement addImagesLinkSpan;

        public void checkIfUserLoggedToolbar(boolean hasOwnAlbums) {
            PhotoalbumUtils.checkVisible(
                hasOwnAlbums
                    ? Lists.newArrayList(myAlbumGroupsLink, myAllAlbumsLink, myAllImagesLink, addAlbumGroupLink, addAlbumLink, addImagesLink)
                    : Lists.newArrayList(myAlbumGroupsLink, myAllAlbumsLink, myAllImagesLink, addAlbumGroupLink, addAlbumLinkSpan, addImagesLinkSpan)
            );
        }

        public void checkIfUserNotLoggedToolbar() {
            PhotoalbumUtils.checkNotVisible(Lists.newArrayList(myAlbumGroupsLink, myAllAlbumsLink, myAllImagesLink, addAlbumGroupLink, addAlbumLink, addImagesLink, addAlbumLinkSpan, addImagesLinkSpan));
        }

        public WebElement getAddAlbumLink() {
            return addAlbumLink;
        }

        public WebElement getAddImagesLink() {
            return addImagesLink;
        }

        public WebElement getAddAlbumGroupLink() {
            return addAlbumGroupLink;
        }

        public WebElement getMyAlbumGroupsLink() {
            return myAlbumGroupsLink;
        }

        public WebElement getMyAllAlbumsLink() {
            return myAllAlbumsLink;
        }

        public WebElement getMyAllImagesLink() {
            return myAllImagesLink;
        }
    }
}

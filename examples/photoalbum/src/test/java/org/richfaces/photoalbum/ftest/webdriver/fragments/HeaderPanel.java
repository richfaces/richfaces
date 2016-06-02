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
package org.richfaces.photoalbum.ftest.webdriver.fragments;

import static org.junit.Assert.assertEquals;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.findby.ByJQuery;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.photoalbum.ftest.webdriver.pages.FBLoginPage;
import org.richfaces.photoalbum.ftest.webdriver.pages.GPlusLoginPage;
import org.richfaces.photoalbum.ftest.webdriver.pages.SocialLoginPage;
import org.richfaces.photoalbum.ftest.webdriver.utils.PhotoalbumUtils;

import com.google.common.collect.Lists;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class HeaderPanel {

    @ArquillianResource
    private WebDriver driver;

    @FindBy(css = "div.header-content-div a img")
    private WebElement image;
    @FindBy(css = "div.header-content-div div.top-right-menu")
    private TopRightMenuLinks links;
    @FindBy(css = "div.header-content-div div[id$='logInOutMenu']")
    private LoginLogoutMenu llm;
    @FindBy(css = "div[id$='menuPanel'] .rf-tb")
    private Toolbar toolbar;

    private void checkAlwaysPresentElements() {
        PhotoalbumUtils.checkVisible(Lists.newArrayList(image, llm.getLoggedUserSpan(), links.getWikiPageLink(), links.getDownloadsLink(),
            links.getCommunityLink()));
        Graphene.waitAjax()
            .ignoring(StaleElementReferenceException.class)
            .ignoring(NoSuchElementException.class)
            .until().element(image).attribute("src").contains("img/shell/logo_top.gif");
    }

    public void checkIfUserNotLogged() {
        checkAlwaysPresentElements();
        PhotoalbumUtils.checkVisible(Lists.newArrayList(llm.getLoginLink(), llm.getRegisterLink()));
        PhotoalbumUtils.checkNotVisible(llm.getLoggedUserLink(), llm.getLogoutLink(), llm.getLoggedInWithFB(), llm.getLoggedInWithGPlus(), llm.getLoginToFBLink(),
            llm.getLoginToGPlusLink(), llm.getFbLoggedUserImage());
        assertEquals("Welcome, guest! If you want access to full version of application, please register or login.",
            llm.getLoggedUserSpan().getText().trim());
        getToolbar().checkIfUserNotLoggedToolbar();
    }

    public void checkUserLogged(String user, boolean hasOwnAlbums, boolean isLoggedWithFB, boolean isloggedWithGPlus) {
        checkAlwaysPresentElements();
        PhotoalbumUtils.checkVisible(Lists.newArrayList(llm.getLoggedUserLink(), llm.getLogoutLink()));
        PhotoalbumUtils.checkNotVisible(llm.getLoginLink(), llm.getRegisterLink());
        assertEquals("Welcome,", llm.getLoggedUserSpan().getText());
        assertEquals(user, llm.getLoggedUserLink().getText());
        if (isLoggedWithFB) {
            PhotoalbumUtils.checkVisible(llm.getLoggedInWithFB(), llm.getFbLoggedUserImage());
            PhotoalbumUtils.checkNotVisible(llm.getLoginToFBLink());
        } else {
            PhotoalbumUtils.checkVisible(llm.getLoginToFBLink());
            PhotoalbumUtils.checkNotVisible(llm.getLoggedInWithFB(), llm.getFbLoggedUserImage());
        }
        if (isloggedWithGPlus) {
            PhotoalbumUtils.checkVisible(llm.getLoggedInWithGPlus());
            PhotoalbumUtils.checkNotVisible(llm.getLoginToGPlusLink());
        } else {
            PhotoalbumUtils.checkVisible(llm.getLoginToGPlusLink());
            PhotoalbumUtils.checkNotVisible(llm.getLoggedInWithGPlus());
        }
        getToolbar().checkIfUserLoggedToolbar(hasOwnAlbums);
    }

    public WebElement getLoggedUserLink() {
        return llm.getLoggedUserLink();
    }

    public WebElement getLoginLink() {
        return llm.getLoginLink();
    }

    public WebElement getLogoutLink() {
        return llm.getLogoutLink();
    }

    public WebElement getStatusHelpLink() {
        return driver.findElement(ByJQuery.selector("a:visible:last"));
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    public void loginToFB() {
        loginToSocial(FBLoginPage.class);
    }

    public void loginToGPlus() {
        loginToSocial(GPlusLoginPage.class);
    }

    private void loginToSocial(Class<? extends SocialLoginPage> pageClass) {
        PhotoalbumUtils.loginWithSocial(pageClass, driver, GPlusLoginPage.class.equals(pageClass) ? llm.getLoginToGPlusLink()
            : llm.getLoginToFBLink());
    }

    public static class LoginLogoutMenu {

        @FindBy(css = "span.logged-user + a + img")
        private WebElement fbLoggedUserImage;
        @FindBy(css = "span.FB-btn-small")
        private WebElement loggedInWithFB;
        @FindBy(css = "span.G-btn-small")
        private WebElement loggedInWithGPlus;
        @FindBy(css = "span.logged-user + a")
        private WebElement loggedUserLink;
        @FindBy(css = "span.logged-user")
        private WebElement loggedUserSpan;
        @FindBy(partialLinkText = "Login")
        private WebElement loginLink;
        @FindBy(css = "a.FB-btn-small")
        private WebElement loginToFBLink;
        @FindBy(css = "a.G-btn-small")
        private WebElement loginToGPlusLink;
        @FindBy(partialLinkText = "Logout")
        private WebElement logoutLink;
        @FindBy(partialLinkText = "Register")
        private WebElement registerLink;

        public WebElement getFbLoggedUserImage() {
            return fbLoggedUserImage;
        }

        public WebElement getLoggedInWithFB() {
            return loggedInWithFB;
        }

        public WebElement getLoggedInWithGPlus() {
            return loggedInWithGPlus;
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

        public WebElement getLoginToFBLink() {
            return loginToFBLink;
        }

        public WebElement getLoginToGPlusLink() {
            return loginToGPlusLink;
        }

        public WebElement getLogoutLink() {
            return logoutLink;
        }

        public WebElement getRegisterLink() {
            return registerLink;
        }

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
            PhotoalbumUtils.checkVisible(hasOwnAlbums ? Lists.newArrayList(myAlbumGroupsLink, myAllAlbumsLink, myAllImagesLink,
                addAlbumGroupLink, addAlbumLink, addImagesLink) : Lists.newArrayList(myAlbumGroupsLink, myAllAlbumsLink,
                    myAllImagesLink, addAlbumGroupLink, addAlbumLinkSpan, addImagesLinkSpan));
        }

        public void checkIfUserNotLoggedToolbar() {
            PhotoalbumUtils.checkNotVisible(Lists.newArrayList(myAlbumGroupsLink, myAllAlbumsLink, myAllImagesLink,
                addAlbumGroupLink, addAlbumLink, addImagesLink, addAlbumLinkSpan, addImagesLinkSpan));
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

    public static class TopRightMenuLinks {

        @FindBy(partialLinkText = "Community")
        private WebElement communityLink;
        @FindBy(partialLinkText = "Downloads")
        private WebElement downloadsLink;
        @FindBy(partialLinkText = "Wiki page")
        private WebElement wikiPageLink;

        public WebElement getCommunityLink() {
            return communityLink;
        }

        public WebElement getDownloadsLink() {
            return downloadsLink;
        }

        public WebElement getWikiPageLink() {
            return wikiPageLink;
        }

    }
}

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

import java.util.concurrent.TimeUnit;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.enricher.WebElementUtils;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.common.TextInputComponentImpl;
import org.richfaces.fragment.messages.RichFacesMessages;
import org.richfaces.fragment.notify.RichFacesNotifyMessage;
import org.richfaces.fragment.panel.TextualFragmentPart;
import org.richfaces.fragment.popupPanel.RichFacesPopupPanel;
import org.richfaces.photoalbum.ftest.webdriver.fragments.HowItWorksPopupPanel.Controls;
import org.richfaces.photoalbum.ftest.webdriver.fragments.LoginPanel.Body;
import org.richfaces.photoalbum.ftest.webdriver.pages.FBLoginPage;
import org.richfaces.photoalbum.ftest.webdriver.pages.GPlusLoginPage;
import org.richfaces.photoalbum.ftest.webdriver.pages.SocialLoginPage;
import org.richfaces.photoalbum.ftest.webdriver.utils.PhotoalbumUtils;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class LoginPanel extends RichFacesPopupPanel<TextualFragmentPart, Controls, Body> {
    private static final int NUMBER_OF_TRIES_SOCIAL_LOGIN = 5;

    @Drone
    private WebDriver browser;

    public void close() {
        getHeaderControlsContent().close();
        advanced().waitUntilPopupIsNotVisible().perform();
    }

    public void login(String user, String password) {
        loginWithoutWait(user, password);
        advanced().waitUntilPopupIsNotVisible().perform();
    }

    public void loginWithFB() {
        loginWithSocialAcc(FBLoginPage.class);
    }

    public void loginWithGPlus() {
        loginWithSocialAcc(GPlusLoginPage.class);
    }

    private void loginWithSocialAcc(Class<? extends SocialLoginPage> pageClass) {
        for (int i = 0; i < NUMBER_OF_TRIES_SOCIAL_LOGIN; i++) {
            if (tryLoginWithSocialAcc(pageClass)) {
                break;
            }
        }
    }

    public void loginWithoutWait(String user, String password) {
        getBodyContent().getLoginInput().clear().sendKeys(user);
        getBodyContent().getPasswordInput().clear().sendKeys(password);
        Graphene.guardAjax(getBodyContent().getLoginButton()).click();
    }

    private boolean tryLoginWithSocialAcc(Class<? extends SocialLoginPage> pageClass) {
        PhotoalbumUtils.loginWithSocial(pageClass, browser, (GPlusLoginPage.class.equals(pageClass) ? getBodyContent().getGplusLoginButton() : getBodyContent().getFbLoginButton()));
        advanced().waitUntilPopupIsNotVisible().withTimeout(20, TimeUnit.SECONDS).perform();
        try {
            RichFacesNotifyMessage msg = Graphene.createPageFragment(RichFacesNotifyMessage.class, WebElementUtils.findElementLazily(By.cssSelector(".rf-ntf.photoalbum-message"), browser));
            // wait up to 2 seconds to check whether there is no error with FB/G+ login
            msg.advanced().waitUntilMessageIsVisible().withTimeout(2, TimeUnit.SECONDS).perform();
            return false;// error message is visible >>> login was not successful
        } catch (TimeoutException e) {
            return true;// error message is not visible >>> login was successful
        }
    }

    public static class Body {

        @FindByJQuery("td.login-table-col2:contains('Login') + td input")
        private TextInputComponentImpl loginInput;
        @FindByJQuery("td.login-table-col2:contains('Password') + td input")
        private TextInputComponentImpl passwordInput;
        @FindByJQuery("div:contains('Login') + input")
        private WebElement loginButton;
        @FindByJQuery("div:contains('Register') + input")
        private WebElement registerButton;
        @FindBy(className = "rf-msgs")
        private RichFacesMessages messages;
        @FindBy(css = "a.FB-btn-medium")
        private WebElement fbLoginButton;
        @FindBy(css = "a.G-btn-medium")
        private WebElement gplusLoginButton;

        public WebElement getFbLoginButton() {
            return fbLoginButton;
        }

        public WebElement getGplusLoginButton() {
            return gplusLoginButton;
        }

        public WebElement getLoginButton() {
            return loginButton;
        }

        public TextInputComponentImpl getLoginInput() {
            return loginInput;
        }

        public RichFacesMessages getMessages() {
            return messages;
        }

        public TextInputComponentImpl getPasswordInput() {
            return passwordInput;
        }

        public WebElement getRegisterButton() {
            return registerButton;
        }
    }
}

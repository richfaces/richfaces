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
package org.richfaces.photoalbum.ftest.webdriver.utils;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.richfaces.fragment.common.Utils;
import org.richfaces.photoalbum.ftest.webdriver.pages.GPlusLoginPage;
import org.richfaces.photoalbum.ftest.webdriver.pages.SocialLoginPage;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public final class PhotoalbumUtils {

    private PhotoalbumUtils() {
    }

    public static void checkNotVisible(WebElement... elements) {
        checkNotVisible(Lists.newArrayList(elements));
    }

    public static void checkNotVisible(List<WebElement> list) {
        for (WebElement webElement : list) {
            Graphene.waitAjax()
                .ignoring(StaleElementReferenceException.class)
                .ignoring(NoSuchElementException.class)
                .until().element(webElement).is().not().visible();
        }
    }

    public static void checkVisible(WebElement... elements) {
        checkVisible(Lists.newArrayList(elements));
    }

    public static void checkVisible(List<WebElement> list) {
        for (WebElement webElement : list) {
            Graphene.waitAjax()
                .ignoring(StaleElementReferenceException.class)
                .ignoring(NoSuchElementException.class)
                .until().element(webElement).is().visible();
        }
    }

    public static List<String> getStringsFromElements(List<WebElement> elements) {
        List<String> result = Lists.newArrayList();
        for (WebElement webElement : elements) {
            result.add(webElement.getText());
        }
        return result;
    }

    public static void loginWithSocial(Class<? extends SocialLoginPage> pageClass, final WebDriver browser, WebElement loginLink) {
        // in case this incovation targets G+ login, check if G+ credentials were provided; if not then throw IllegalArgumentException
        if (GPlusLoginPage.class.equals(pageClass) && (System.getProperty("googlePlus.username").equals("undefined") || System.getProperty("googlePlus.password").equals("undefined"))) {
            throw new IllegalArgumentException("G+ login was invoked but no login parameters were provided. Please specify them via -DgooglePlus.username and -DgooglePlus.password!");
        }
        String originalWindow = browser.getWindowHandle();
        checkVisible(loginLink);
        Graphene.guardAjax(loginLink).click();
        Graphene.waitModel().withTimeout(20, TimeUnit.SECONDS).until(new NumberOfWindowsOpenedPredicate(browser, 2));
        Set<String> windowHandles = browser.getWindowHandles();
        windowHandles.remove(originalWindow);
        try {
            WebDriver window = browser.switchTo().window(windowHandles.iterator().next());
            Graphene.waitModel().until().element(By.tagName("body")).is().visible();
            SocialLoginPage loginPage = Graphene.createPageFragment(pageClass, window.findElement(By.tagName("body")));
            if (GPlusLoginPage.class.equals(pageClass)) {
                //G+ credential are extracted from system property
                loginPage.login(System.getProperty("googlePlus.username"), System.getProperty("googlePlus.password"));
            } else {
                //FB test account credentials are hardcoded and usable by anyone, should not result in acc being locked
                loginPage.login("vocfryc_wongwitz_1429527192@tfbnw.net", "12345");
            }
        } finally {
            browser.switchTo().window(originalWindow);
            Graphene.waitModel().withTimeout(20, TimeUnit.SECONDS).until(new NumberOfWindowsOpenedPredicate(browser, 1));
        }
    }

    public static void scrollToElement(WebElement element) {
        Utils.getExecutorFromElement(element).executeScript("arguments[0].scrollIntoView(false);", element);
    }

    public static void waitFor(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ex) {
        }
    }

    public static class NumberOfWindowsOpenedPredicate implements Predicate<WebDriver> {

        private final WebDriver browser;
        private final int numberOfWindows;

        public NumberOfWindowsOpenedPredicate(WebDriver browser, int numberOfWindows) {
            this.browser = browser;
            this.numberOfWindows = numberOfWindows;
        }

        @Override
        public boolean apply(WebDriver input) {
            return browser.getWindowHandles().size() == numberOfWindows;
        }

        @Override
        public String toString() {
            return numberOfWindows + " window(s) should be opened, but have: " + browser.getWindowHandles().size();
        }
    }
}

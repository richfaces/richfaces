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
package org.richfaces.tests.photoalbum.ftest.webdriver.utils;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.List;
import java.util.Set;

import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.richfaces.fragment.common.Utils;
import org.richfaces.tests.photoalbum.ftest.webdriver.pages.SocialLoginPage;

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
        int i = 0;
        for (WebElement webElement : list) {
            assertFalse(Utils.isVisible(webElement), i + "(nth) element should not be visible");
            i++;
        }
    }

    public static void checkVisible(WebElement... elements) {
        checkVisible(Lists.newArrayList(elements));
    }

    public static void checkVisible(List<WebElement> list) {
        int i = 0;
        for (WebElement webElement : list) {
            assertTrue(Utils.isVisible(webElement), i + "(nth) element should be visible");
            i++;
        }
    }

    public static List<String> getStringsFromElements(List<WebElement> elements) {
        List<String> result = Lists.newArrayList();
        for (WebElement webElement : elements) {
            result.add(webElement.getText());
        }
        return result;
    }

    public static void loginWithSocial(Class<? extends SocialLoginPage> pageClass, final WebDriver browser, WebElement loginLink, boolean workaroundGplusAccInChache) {
        String originalWindow = browser.getWindowHandle();
        Graphene.guardAjax(loginLink).click();
        Graphene.waitAjax().until(new Predicate<WebDriver>() {
            @Override
            public boolean apply(WebDriver input) {
                return browser.getWindowHandles().size() == 2;
            }

            @Override
            public String toString() {
                return "2 windows should be opened, but have: " + browser.getWindowHandles().size();
            }
        });
        Set<String> windowHandles = browser.getWindowHandles();
        windowHandles.remove(originalWindow);
        try {
            WebDriver window = browser.switchTo().window(windowHandles.iterator().next());
            Graphene.waitModel().until().element(By.tagName("body")).is().visible();
            Graphene.createPageFragment(pageClass, window.findElement(By.tagName("body")))
                .login("rf.photoalbum@gmail.com", "rf.photoalbumrf.photoalbum");
        } finally {
            browser.switchTo().window(originalWindow);
            PhotoalbumUtils.waitFor(5000);// FIXME: replace with some wait condition
            Graphene.waitModel().until(new Predicate<WebDriver>() {
                @Override
                public boolean apply(WebDriver input) {
                    return browser.getWindowHandles().size() == 1;
                }

                @Override
                public String toString() {
                    return "Only one window should be opened, but have: " + browser.getWindowHandles().size();
                }
            });
        }
    }

    public static void loginWithSocial(Class<? extends SocialLoginPage> pageClass, final WebDriver browser, WebElement loginLink) {
        String originalWindow = browser.getWindowHandle();
        Graphene.guardAjax(loginLink).click();
        Graphene.waitModel().until(new Predicate<WebDriver>() {
            @Override
            public boolean apply(WebDriver input) {
                return browser.getWindowHandles().size() == 2;
            }

            @Override
            public String toString() {
                return "2 windows should be opened, but have: " + browser.getWindowHandles().size();
            }
        });
        Set<String> windowHandles = browser.getWindowHandles();
        windowHandles.remove(originalWindow);
        try {
            WebDriver window = browser.switchTo().window(windowHandles.iterator().next());
            Graphene.waitModel().until().element(By.tagName("body")).is().visible();
            Graphene.createPageFragment(pageClass, window.findElement(By.tagName("body")))
                .login("rf.photoalbum@gmail.com", "rf.photoalbumrf.photoalbum");
        } finally {
            browser.switchTo().window(originalWindow);
            PhotoalbumUtils.waitFor(5000);// FIXME: replace with some wait condition
            Graphene.waitModel().until(new Predicate<WebDriver>() {
                @Override
                public boolean apply(WebDriver input) {
                    return browser.getWindowHandles().size() == 1;
                }

                @Override
                public String toString() {
                    return "Only one window should be opened, but have: " + browser.getWindowHandles().size();
                }
            });
        }
    }

    public static void scrollToElement(WebElement element) {
        Point location = element.getLocation();
        Utils.getExecutorFromElement(element).executeScript("scrollTo(" + location.x + "," + location.y + ")");
    }

    public static void waitFor(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ex) {
        }
    }
}

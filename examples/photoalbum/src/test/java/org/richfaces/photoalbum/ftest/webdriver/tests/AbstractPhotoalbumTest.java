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
package org.richfaces.photoalbum.ftest.webdriver.tests;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Arrays;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.Assume;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.richfaces.fragment.common.Utils;
import org.richfaces.photoalbum.ftest.webdriver.pages.PhotoalbumPage;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
@RunAsClient
@RunWith(Arquillian.class)
public abstract class AbstractPhotoalbumTest {

    public static final String IMAGES_DEC_DATE1 = "Dec 17, 2009";
    public static final String IMAGES_DEC_DATE2 = "Dec 18, 2009";

    public static final String JAN_DATE1 = "Jan 7, 1985";
    public static final String JAN_DATE2 = "Jan 8, 1985";

    public static final PossibleStringOptions IMAGES_DEC_DATE = new PossibleStringOptions(IMAGES_DEC_DATE1, IMAGES_DEC_DATE2);
    public static final PossibleStringOptions JAN_DATE_70 = new PossibleStringOptions("Jan 7, 1970", "Jan 8, 1970");
    public static final PossibleStringOptions JAN_DATE_85 = new PossibleStringOptions(JAN_DATE1, JAN_DATE2);

    public static final String NO_OWNER = "-1";
    public static final String UNKNOWN_IMG_SRC = "-1";

    @Deployment(testable = false)
    public static WebArchive createTestArchive() {
        return ShrinkWrap.createFromZipFile(WebArchive.class, new File("target/richfaces-photoalbum.war"));
    }

    @Drone
    private WebDriver browser;

    @ArquillianResource
    private URL contextPath;

    @Page
    private PhotoalbumPage page;

    public void deleteCookies() {
        Assume.assumeNotNull(browser);
        browser.manage().deleteAllCookies();
    }

    public WebDriver getBrowser() {
        return browser;
    }

    public PhotoalbumPage getPage() {
        return page;
    }

    protected <T> T getView(Class<T> klass) {
        for (Method m : page.getContentPanel().getClass().getMethods()) {
            if (m.getReturnType().equals(klass)) {
                try {
                    return (T) m.invoke(page.getContentPanel());
                } catch (Exception ex) {
                    throw new RuntimeException("Cannot obtain view " + klass.getSimpleName());
                }
            }
        }
        throw new UnsupportedOperationException("Not supported view " + klass.getSimpleName());
    }

    public void login() {
        login("amarkhel");
        page.checkUserLogged("amarkhel", true, false, false);
    }

    public void login(String user) {
        login(user, "12345");
    }

    public void login(String user, String password) {
        page.login(user, password);
    }

    public void logout() {
        page.logout();
    }

    @After
    public void logoutAfter() throws Exception {
        if (Utils.isVisible(page.getHeaderPanel().getLogoutLink())) {
            logout();
            browser.manage().deleteAllCookies();
        }
    }

    @Before
    public void prepare() {
        Assume.assumeNotNull(browser);

        // the address needs to contain "localhost" instead of local loop IP, workaround for socials login
        String replaced = contextPath.toString();
        replaced = "http://localhost" + replaced.substring(replaced.indexOf(":8080"));
        browser.get(replaced + "index.jsf");
        // this method could also handle user logging
        // i.e by introducing an annotation @LoggedUser used for marking test methods
        // but it cannot be done because of https://issues.jboss.org/browse/ARQGRA-309
        // so the logging needs to be done manually
        /**
         * E.g.:
         *
         * public void loadPage(Method m) { if(m.isAnnotationPresent(LoggedUser.class)){ login(LoggedUser.getName(),
         * LoggedUser.getPassword()); } }
         */
    }

    public static class PossibleStringOptions {

        private final String[] strings;

        public PossibleStringOptions(String... strings) {
            this.strings = strings;
        }

        @Override
        public boolean equals(Object o) {
            for (String string : strings) {
                if (string.equals(o)) {
                    return Boolean.TRUE;
                }
            }
            return Boolean.FALSE;
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 23 * hash + Arrays.deepHashCode(this.strings);
            return hash;
        }

        @Override
        public String toString() {
            return Arrays.asList(strings).toString();
        }
    }
}

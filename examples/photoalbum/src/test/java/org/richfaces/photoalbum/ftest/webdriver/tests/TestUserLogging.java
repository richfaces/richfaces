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

import static org.testng.Assert.assertEquals;

import org.jboss.arquillian.graphene.Graphene;
import org.richfaces.fragment.message.Message;
import org.richfaces.tests.photoalbum.ftest.webdriver.fragments.LoginPanel;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestUserLogging extends AbstractPhotoalbumTest {

    @Test
    public void testBadLoginWillShowErrorMessages() {
        Graphene.guardAjax(page.getHeaderPanel().getLoginLink()).click();
        LoginPanel loginPanel = page.getLoginPanel();
        loginPanel.advanced().waitUntilPopupIsVisible();
        loginPanel.loginWithoutWait("amarkhel", "bad password");
        assertEquals(loginPanel.getBodyContent().getMessages().size(), 1);
        assertEquals(loginPanel.getBodyContent().getMessages().getItem(0).getSummary(), "Invalid login or password");
        assertEquals(loginPanel.getBodyContent().getMessages().getItem(0).getType(), Message.MessageType.ERROR);
        loginPanel.getHeaderControlsContent().close();
        loginPanel.advanced().waitUntilPopupIsNotVisible().perform();
        page.checkNotLogged();
    }

    @Test
    public void testLogInAndOut() {
        page.login("amarkhel", "12345");
        page.checkUserLogged("amarkhel", true, false, false);
        page.logout();
        page.checkNotLogged();
    }

    @Test
    public void testLogInAndOutWithFB() {
        page.openLoginPanel().loginWithFB();
        page.checkUserLogged("rich.faces.3", false, true, false);
        page.logout();
        page.checkNotLogged();
    }

    @Test
    public void testLogInAndOutWithGPlus() {
        page.openLoginPanel().loginWithGPlus();
        page.checkUserLogged("Rich", false, false, true);
        page.logout();
        page.checkNotLogged();
    }
}

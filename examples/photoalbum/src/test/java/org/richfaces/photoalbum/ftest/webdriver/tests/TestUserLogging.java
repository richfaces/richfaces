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

import static org.junit.Assert.assertEquals;

import org.jboss.arquillian.graphene.Graphene;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.richfaces.fragment.message.Message;
import org.richfaces.photoalbum.ftest.webdriver.fragments.LoginPanel;

import category.FailingOnPhantomJS;
import category.Smoke;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestUserLogging extends AbstractPhotoalbumTest {

    @Test
    public void testBadLoginWillShowErrorMessages() {
        Graphene.guardAjax(getPage().getHeaderPanel().getLoginLink()).click();
        LoginPanel loginPanel = getPage().getLoginPanel();
        loginPanel.advanced().waitUntilPopupIsVisible();
        loginPanel.loginWithoutWait("amarkhel", "bad password");
        assertEquals(1, loginPanel.getBodyContent().getMessages().size());
        assertEquals("Invalid login or password", loginPanel.getBodyContent().getMessages().getItem(0).getSummary());
        assertEquals(Message.MessageType.ERROR, loginPanel.getBodyContent().getMessages().getItem(0).getType());
        loginPanel.getHeaderControlsContent().close();
        loginPanel.advanced().waitUntilPopupIsNotVisible().perform();
        getPage().checkNotLogged();
    }

    @Test
    @Category(Smoke.class)
    public void testLogInAndOut() {
        getPage().login("amarkhel", "12345");
        getPage().checkUserLogged("amarkhel", true, false, false);
        getPage().logout();
        getPage().checkNotLogged();
    }

    @Test
    @Category({ FailingOnPhantomJS.class })
    public void testLogInAndOutWithFB() {
        getPage().openLoginPanel().loginWithFB();
        getPage().checkUserLogged("Tom", false, true, false);
        getPage().logout();
        getPage().checkNotLogged();
    }

    @Test
    @Category({ FailingOnPhantomJS.class })
    public void testLogInAndOutWithGPlus() {
        getPage().openLoginPanel().loginWithGPlus();
        getPage().checkUserLogged("Rich", false, false, true);
        getPage().logout();
        getPage().checkNotLogged();
    }
}

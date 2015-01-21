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
package org.richfaces.photoalbum.ftest.webdriver.fragments;



import static org.junit.Assert.assertTrue;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.richfaces.fragment.common.Utils;
import org.richfaces.fragment.notify.RichFacesNotifyMessage;

/**
 *
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class ErrorPanel extends RichFacesNotifyMessage {

    @FindByJQuery(".rf-ntf-ico")
    private WebElement icon;

    @Drone
    private WebDriver browser;

    public void checkAll(String contentStartsWith) {
        checkContent(contentStartsWith);
        checkContainsWarning();
        checkCloseWithControls();
    }

    public void checkCloseWithControls() {
        close();
        advanced().waitUntilMessageIsNotVisible().perform();
    }

    public void checkContainsWarning() {
        assertTrue(Utils.isVisible(icon));
    }

    public void checkContent(String contentContains) {
        assertTrue(getContentText().contains(contentContains));
    }

    public String getContentText() {
        return advanced().getRootElement().getText();
    }
}

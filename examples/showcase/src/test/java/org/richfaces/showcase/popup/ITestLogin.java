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
package org.richfaces.showcase.popup;

import static org.jboss.arquillian.graphene.Graphene.waitGui;

import org.jboss.arquillian.graphene.page.Page;
import org.junit.Test;
import org.openqa.selenium.WebElement;
import org.richfaces.showcase.panel.AbstractPanelTest;
import org.richfaces.showcase.popup.page.LoginPage;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class ITestLogin extends AbstractPanelTest {

    @Page
    private LoginPage page;

    @Test
    public void testLoginPopup() {
        checkPopupPanel(page.getLoginOnToolbar(), page.getLoginOnPopup());
    }

    @Test
    public void testSearchPopup() {
        checkPopupPanel(page.getSearchOnToolbar(), page.getSearchOnPopup());
    }

    /**
     * Call the poppup panel, and then hides it, check for presence
     *
     * @param callPopupButton
     *            the button by which the poppup is called
     * @param closingPopupButton
     *            the button by which the poppup is closed
     */
    private void checkPopupPanel(WebElement callPopupButton, WebElement closingPopupButton) {
        callPopupButton.click();
        waitGui(webDriver).until("The popup panel should be visible!")
                .element(closingPopupButton)
                .is()
                .visible();
        closingPopupButton.click();
        waitGui(webDriver).until("The popup panel should not be visible!")
                .element(closingPopupButton)
                .is()
                .not()
                .visible();
    }
}

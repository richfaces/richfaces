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
package org.richfaces.showcase.popup;

import static org.jboss.arquillian.graphene.Graphene.waitGui;

import org.jboss.arquillian.graphene.page.Page;
import org.junit.Test;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.common.Actions;
import org.richfaces.showcase.panel.AbstractPanelTest;
import org.richfaces.showcase.popup.page.PopupPage;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 */
public class ITestModalPopup extends AbstractPanelTest {

    @Page
    private PopupPage page;

    @FindBy(css = "div[id$='popup_shade']")
    private WebElement popupPanelShadow;

    protected final String BODY_OF_POPUP = "You can also check and trigger events if the use clicks outside of the panel.\n"
        + "In this example clicking outside closes the panel.";

    @Test
    public void testModalPopupPanel() {
        page.getCallthePopupButton().click();
        waitGui(webDriver).until("The popup panel should be visible now!")
                .element(page.getPopupPanelContent())
                .is()
                .visible();
        checkContentOfPanel(page.getPopupPanelContent(), BODY_OF_POPUP);
        // move to the corner of the shadow or the click will be invoked on the popup panel (middle of the shadow element)
        new Actions(webDriver).moveToElement(popupPanelShadow, 1, 1).click().perform();
        waitGui(webDriver).until("The popup panel should not be visible now!")
                .element(page.getPopupPanelContent())
                .is()
                .not()
                .visible();
    }

}

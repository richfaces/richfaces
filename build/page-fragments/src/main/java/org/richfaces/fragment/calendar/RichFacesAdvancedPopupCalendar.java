/**
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat, Inc. and individual contributors
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
package org.richfaces.fragment.calendar;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.GrapheneElement;
import org.jboss.arquillian.graphene.fragment.Root;
import org.joda.time.DateTime;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.common.Locations;
import org.richfaces.fragment.common.TextInputComponentImpl;
import org.richfaces.fragment.common.Utils;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class RichFacesAdvancedPopupCalendar {

    @Root
    private WebElement root;

    @FindBy(css = "span[id$=Popup] > input[id$='InputDate']")
    private TextInputComponentImpl input;
    @FindBy(css = "span[id$=Popup] > .rf-cal-btn")
    private GrapheneElement popupButton;
    @FindBy(css = "table[id$=Content]")
    private GrapheneElement popup;

    private PopupCalendar calendarPopup;

    public static enum OpenedBy {

        INPUT_CLICKING,
        OPEN_BUTTON_CLICKING,;
    }

    public void closePopup() {
        closePopup(OpenedBy.OPEN_BUTTON_CLICKING);
    }

    public void closePopup(OpenedBy by) {
        if (getPopup().isVisible()) {
            switch (by) {
                case INPUT_CLICKING:
                    input.advanced().getInputElement().click();
                    break;
                case OPEN_BUTTON_CLICKING:
                    popupButton.click();
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
        getPopup().waitUntilIsNotVisible()
                  .perform();
    }

    public TextInputComponentImpl getInput() {
        return input;
    }

    public Locations getLocations() {
        return Utils.getLocations(root);
    }

    public PopupCalendar getPopup() {
        if (calendarPopup == null) {
            calendarPopup = Graphene.createPageFragment(PopupCalendar.class, root);
        }
        return calendarPopup;
    }

    public WebElement getPopupButtonElement() {
        return popupButton;
    }

    public WebElement getRootElement() {
        return root;
    }

    public boolean isVisible() {
        return Utils.isVisible(root);
    }

    public PopupCalendar openPopup(OpenedBy by) {
        if (Utils.isVisible(popup)) {
            return getPopup();
        }
        switch (by) {
            case INPUT_CLICKING:
                if (!Utils.isVisible(input.advanced().getInputElement())) {
                throw new RuntimeException("input is not displayed");
            }
                input.advanced().getInputElement().click();
                break;
            case OPEN_BUTTON_CLICKING:
                if (!popupButton.isDisplayed()) {
                throw new RuntimeException("popup button is not displayed");
            }
                popupButton.click();
                break;
            default:
                throw new IllegalArgumentException();
        }
        getPopup().waitUntilIsVisible()
                  .perform();
        return getPopup();
    }

    public PopupCalendar openPopup() {
        return openPopup(OpenedBy.INPUT_CLICKING);
    }

    public void setDateTime(DateTime dt) {
        openPopup().setDateTime(dt);
    }
}

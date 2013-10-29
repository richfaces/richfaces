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
package org.richfaces.tests.page.fragments.impl.calendar;

import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.joda.time.DateTime;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.page.fragments.impl.Utils;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class PopupCalendar extends RichFacesAdvancedInlineCalendar {

    @FindBy(css = "td[id$=Footer]")
    private PopupFooterControls popupFooterControls;
    @FindBy(css = "td[id$=Header]")
    private PopupHeaderControls popupHeaderControls;

    @Override
    public PopupFooterControls getFooterControls() {
        popupFooterControls.setCalendarEditor(getCalendarEditor());
        return popupFooterControls;
    }

    @Override
    public PopupHeaderControls getHeaderControls() {
        popupHeaderControls.setCalendarEditor(getCalendarEditor());
        return popupHeaderControls;
    }

    @Override
    public WebElement getRoot() {
        return super.getRoot().findElement(By.cssSelector("table.rf-cal-popup"));
    }

    @Override
    public void setDateTime(DateTime dt) {
        if (Utils.isVisible(getFooterControls().getApplyButtonElement())) {
            super.setDateTime(dt);
            getFooterControls().getApplyButtonElement().click();
        } else {
            getHeaderControls().openYearAndMonthEditor().selectDate(dt).confirmDate();
            getDayPicker().selectDayInMonth(dt);
            if (Utils.isVisible(getFooterControls().getTimeEditorOpenerElement())) {
                getFooterControls().openTimeEditor().setTime(dt, TimeEditor.SetValueBy.TYPING).confirmTime();
            }
        }
    }

    public static class PopupFooterControls extends FooterControls {

        @FindByJQuery("div.rf-cal-tl-btn:contains('Apply')")
        private WebElement applyButtonElement;

        public void applyDate() {
            if (!isVisible()) {
                throw new RuntimeException("Footer controls are not displayed, cannot interact with apply button");
            }
            if (!Utils.isVisible(applyButtonElement)) {
                throw new RuntimeException("Apply button is not displayed.");
            }
            applyButtonElement.click();
        }

        public WebElement getApplyButtonElement() {
            return applyButtonElement;
        }

        @Override
        public void setTodaysDate() {
            todayDate();
            if (Utils.isVisible(applyButtonElement)) {
                applyDate();
            }
        }

        @Override
        public void todayDate() {
            if (!isVisible()) {
                throw new RuntimeException("Footer controls are not displayed, cannot interact with today button");
            }
            if (!getTodayButtonElement().isDisplayed()) {
                throw new RuntimeException("Today button is not displayed.");
            }
            getTodayButtonElement().click();
        }
    }

    public static class PopupHeaderControls extends HeaderControls {

        @FindBy(css = ".rf-cal-btn-close > div")
        private WebElement closeButtonElement;

        public void closePopup() {
            if (!isVisible() || !Utils.isVisible(closeButtonElement)) {
                throw new RuntimeException("Cannot interact with close button. "
                    + "Ensure that calendar popup and header controls are displayed.");
            }
            closeButtonElement.click();
            waitUntilIsNotVisible().perform();
        }

        public WebElement getCloseButtonElement() {
            return closeButtonElement;
        }
    }
}

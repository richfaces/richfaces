/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and individual contributors
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

import java.util.concurrent.TimeUnit;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.fragment.Root;
import org.jboss.arquillian.graphene.wait.FluentWait;
import org.joda.time.DateTime;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.common.Locations;
import org.richfaces.fragment.common.TextInputComponentImpl;
import org.richfaces.fragment.common.Utils;
import org.richfaces.fragment.common.WaitingWrapper;
import org.richfaces.fragment.common.WaitingWrapperImpl;

import com.google.common.base.Predicate;

/**
 *
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class RichFacesAdvancedInlineCalendar {

    @Root
    private WebElement root;

    @Drone
    private WebDriver browser;

    @FindBy(css = "span[id$=Popup] > input[id$='InputDate']")
    private TextInputComponentImpl input;
    @FindBy(css = "td[id$=Header]")
    private HeaderControls headerControls;
    @FindBy(css = "td[id$=Footer]")
    private FooterControls footerControls;
    @FindBy(css = "table[id$=Content] > tbody")
    private DayPicker dayPicker;
    @FindBy(css = "table[id$=Editor]")
    private CalendarEditor calendarEditor;

    private long _timeoutForPopupToBeNotVisible = -1;
    private long _timeoutPopupToBeVisible = -1;

    protected CalendarEditor getCalendarEditor() {
        return calendarEditor;
    }

    public DayPicker getDayPicker() {
        return dayPicker;
    }

    public FooterControls getFooterControls() {
        footerControls.setCalendarEditor(getCalendarEditor());
        return footerControls;
    }

    public HeaderControls getHeaderControls() {
        headerControls.setCalendarEditor(getCalendarEditor());
        return headerControls;
    }

    public TextInputComponentImpl getInput() {
        return input;
    }

    public Locations getLocations() {
        return Utils.getLocations(getRoot());
    }

    public WebElement getRoot() {
        return root;
    }

    public boolean isVisible() {
        return Utils.isVisible(getRoot());
    }

    public void setDateTime(DateTime dt) {
        getHeaderControls().openYearAndMonthEditor().selectDate(dt).confirmDate();
        getDayPicker().selectDayInMonth(dt);
        if (Utils.isVisible(getFooterControls().getTimeEditorOpenerElement())) {
            getFooterControls().openTimeEditor().setTime(dt, TimeEditor.SetValueBy.TYPING).confirmTime();
        }
    }

    public void setTimeoutForPopupToBeNotVisible(long timeoutInMilliseconds) {
        this._timeoutForPopupToBeNotVisible = timeoutInMilliseconds;
    }

    public long getTimeoutForPopupToBeNotVisible() {
        return _timeoutForPopupToBeNotVisible == -1 ? Utils.getWaitAjaxDefaultTimeout(browser) : _timeoutForPopupToBeNotVisible;
    }

    public void setTimeoutForPopupToBeVisible(long timeoutInMilliseconds) {
        this._timeoutPopupToBeVisible = timeoutInMilliseconds;
    }

    public long getTimeoutForPopupToBeVisible() {
        return _timeoutPopupToBeVisible == - 1 ? Utils.getWaitAjaxDefaultTimeout(browser) : _timeoutPopupToBeVisible;
    }

    public WaitingWrapper waitUntilIsNotVisible() {
        return new WaitingWrapperImpl() {
            @Override
            protected void performWait(FluentWait<WebDriver, Void> wait) {
                wait.withTimeout(getTimeoutForPopupToBeNotVisible(), TimeUnit.MILLISECONDS).until(new Predicate<WebDriver>() {
                    @Override
                    public boolean apply(WebDriver input) {
                        return !isVisible();
                    }
                });
            }
        }.withMessage("Waiting for calendar to be not visible.");
    }

    public WaitingWrapper waitUntilIsVisible() {
        return new WaitingWrapperImpl() {

            @Override
            protected void performWait(FluentWait<WebDriver, Void> wait) {
                wait.withTimeout(getTimeoutForPopupToBeVisible(), TimeUnit.MILLISECONDS).until(new Predicate<WebDriver>() {
                    @Override
                    public boolean apply(WebDriver input) {
                        return isVisible();
                    }
                });
            }
        }.withMessage("Waiting for calendar to be visible.");
    }

    public static class CalendarEditor {

        @Root
        private WebElement root;
        @FindBy(className = "rf-cal-monthpicker-cnt")
        private YearAndMonthEditor dateEditor;
        @FindBy(className = "rf-cal-timepicker-cnt")
        private TimeEditor timeEditor;

        public YearAndMonthEditor getDateEditor() {
            return dateEditor;
        }

        public boolean isVisible() {
            return Utils.isVisible(root);
        }

        public TimeEditor getTimeEditor() {
            return timeEditor;
        }
    }
}

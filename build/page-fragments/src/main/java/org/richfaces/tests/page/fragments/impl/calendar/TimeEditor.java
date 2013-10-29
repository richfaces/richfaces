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

import java.util.concurrent.TimeUnit;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.GrapheneElement;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.jboss.arquillian.graphene.fragment.Root;
import org.jboss.arquillian.graphene.wait.FluentWait;
import org.joda.time.DateTime;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.page.fragments.impl.Utils;
import org.richfaces.tests.page.fragments.impl.calendar.TimeSpinner.TimeSign;
import org.richfaces.tests.page.fragments.impl.calendar.TimeSpinner.TimeSignSpinner;
import org.richfaces.tests.page.fragments.impl.calendar.TimeSpinner.TimeSpinner12;
import org.richfaces.tests.page.fragments.impl.calendar.TimeSpinner.TimeSpinner24;
import org.richfaces.tests.page.fragments.impl.calendar.TimeSpinner.TimeSpinner60;
import org.richfaces.tests.page.fragments.impl.utils.WaitingWrapper;
import org.richfaces.tests.page.fragments.impl.utils.WaitingWrapperImpl;

/**
 * Component for editing calendar's time
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TimeEditor {

    @Root
    private WebElement root;

    @Drone
    private WebDriver browser;

    @FindByJQuery(".rf-cal-timepicker-inp table table:has('input[id$=TimeHours]')")
    private TimeSpinner12 hoursSpinner12;
    @FindByJQuery(".rf-cal-timepicker-inp table table:has('input[id$=TimeHours]')")
    private TimeSpinner24 hoursSpinner24;
    @FindByJQuery(".rf-cal-timepicker-inp table table:has('input[id$=TimeMinutes]')")
    private TimeSpinner60 minutesSpinner;
    @FindByJQuery(".rf-cal-timepicker-inp table table:has('input[id$=TimeSeconds]')")
    private TimeSpinner60 secondsSpinner;
    @FindByJQuery(".rf-cal-timepicker-inp table table:has('input[id$=TimeSign]')")
    private TimeSignSpinner timeSignSpinner;

    @FindBy(css = "div[id$=TimeEditorButtonOk]")
    private GrapheneElement okButtonElement;
    @FindBy(css = "div[id$=TimeEditorButtonCancel]")
    private GrapheneElement cancelButtonElement;

    private static final int defaultHours = 12;
    private static final int defaultMinutes = 0;
    private static final int defaultSeconds = 0;

    private long _timeoutForTimeEditorToBeNotVisible = -1;
    private long _timeoutForTimeEditorToBeVisible = -1;

    public WebElement getRoot() {
        return root;
    }

    public enum SetValueBy {

        TYPING, BUTTONS;
    }

    public void cancelTime() {
        if (!isVisible()) {
            throw new RuntimeException("Cannot interact with TimePicker. "
                + "Ensure that it it is opened.");
        }
        if (!cancelButtonElement.isDisplayed()) {
            throw new RuntimeException("Cancel button is not visible.");
        }
        cancelButtonElement.click();
        waitUntilIsNotVisible().perform();
    }

    public void confirmTime() {
        if (!isVisible()) {
            throw new RuntimeException("Cannot interact with TimePicker. "
                + "Ensure that it it is opened.");
        }
        if (!okButtonElement.isDisplayed()) {
            throw new RuntimeException("Ok button is not visible.");
        }
        okButtonElement.click();
        waitUntilIsNotVisible().perform();
    }

    public WebElement getCancelButtonElement() {
        return cancelButtonElement;
    }

    private TimeSpinner<Integer> getHoursSpinner() {
        if (getTimeSignSpinner() == null) {
            if (hoursSpinner24.isVisible()) {
                return hoursSpinner24;
            }
        } else {
            if (hoursSpinner12.isVisible()) {
                return hoursSpinner12;
            }
        }
        return null;
    }

    private TimeSpinner<Integer> getMinutesSpinner() {
        if (minutesSpinner.isVisible()) {
            return minutesSpinner;
        }
        return null;
    }

    public WebElement getOkButtonElement() {
        return okButtonElement;
    }

    private TimeSpinner<Integer> getSecondsSpinner() {
        if (secondsSpinner.isVisible()) {
            return secondsSpinner;
        }
        return null;
    }

    public DateTime getTime() {
        int seconds = (getSecondsSpinner() != null ? getSecondsSpinner().getValue() : defaultSeconds);
        int minutes = (getMinutesSpinner() != null ? getMinutesSpinner().getValue() : defaultMinutes);
        int hours = (getHoursSpinner() != null ? getHoursSpinner().getValue() : defaultHours);
        DateTime result = new DateTime()
            .withHourOfDay(hours)
            .withMinuteOfHour(minutes)
            .withSecondOfMinute(seconds);
        TimeSignSpinner tss = getTimeSignSpinner();
        if (tss != null) {
            switch (tss.getValue()) {
                case AM:
                    if (result.getHourOfDay() == 12) {//12:xx am -> 00:xx
                    result = result.minusHours(12);
                }
                    break;
                case PM:
                    if (result.getHourOfDay() != 12) {
                    result = result.plusHours(12);
                }
                    break;
                default:
                    throw new IllegalArgumentException("Unknown switch");
            }
        }
        return result;
    }

    private TimeSignSpinner getTimeSignSpinner() {
        if (timeSignSpinner.isVisible()) {
            return timeSignSpinner;
        }
        return null;
    }

    public boolean isVisible() {
        return Utils.isVisible(root);
    }

    private TimeEditor setTime(int hours, int minutes, int seconds, SetValueBy by) {
        TimeSign timeSign = null;
        TimeSpinner<Integer> actSecondsSpinner = getSecondsSpinner();
        TimeSignSpinner acttimeSignSpinner = getTimeSignSpinner();
        if (acttimeSignSpinner != null) {//there is a time sign spinner, --> fix the hours
            timeSign = TimeSign.AM;
            if (hours >= 12) {
                timeSign = TimeSign.PM;
            }
            if (hours > 12) {//>12h -> XXh pm
                hours -= 12;
            }
            if (hours == 0) {//00:xx -> 12:xx am
                hours = 12;
            }
        }
        getHoursSpinner().setValueBy(hours, by);
        getMinutesSpinner().setValueBy(minutes, by);
        if (actSecondsSpinner != null) {
            actSecondsSpinner.setValueBy(seconds, by);
        }
        if (acttimeSignSpinner != null) {
            acttimeSignSpinner.setValueBy(timeSign, by);
        }
        return this;
    }

    public TimeEditor setTime(DateTime time, SetValueBy inputType) {
        return setTime(time.getHourOfDay(), time.getMinuteOfHour(), time.getSecondOfMinute(), inputType);
    }

    public void setupTimeouFortTimeEditorIsNotVisible(long timeoutInMilliseconds) {
        this._timeoutForTimeEditorToBeNotVisible = timeoutInMilliseconds;
    }

    public long getTimeoutForTimeEditorToBeNotVisible() {
        return _timeoutForTimeEditorToBeNotVisible == -1 ? Utils.getWaitAjaxDefaultTimeout(browser) : _timeoutForTimeEditorToBeNotVisible;
    }

    public void setupTimeoutForTimeEditorToBeVisible(long timeoutInMilliseconds) {
        this._timeoutForTimeEditorToBeVisible = timeoutInMilliseconds;
    }

    public long getTimeoutForTimeEditorToBeVisible() {
        return _timeoutForTimeEditorToBeVisible == -1 ? Utils.getWaitAjaxDefaultTimeout(browser) : _timeoutForTimeEditorToBeVisible;
    }

    public WaitingWrapper waitUntilIsNotVisible() {
        return new WaitingWrapperImpl() {
            @Override
            protected void performWait(FluentWait<WebDriver, Void> wait) {
                wait.until().element(root).is().not().visible();
            }
        }.withMessage("Time editor to be not visible.").withTimeout(getTimeoutForTimeEditorToBeNotVisible(), TimeUnit.MILLISECONDS);
    }

    public WaitingWrapper waitUntilIsVisible() {
        return new WaitingWrapperImpl() {
            @Override
            protected void performWait(FluentWait<WebDriver, Void> wait) {
                wait.until().element(root).is().visible();
            }
        }.withMessage("Time editor to be visible.").withTimeout(getTimeoutForTimeEditorToBeVisible(), TimeUnit.MILLISECONDS);
    }
}

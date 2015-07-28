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
import org.jboss.arquillian.graphene.GrapheneElement;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.jboss.arquillian.graphene.fragment.Root;
import org.jboss.arquillian.graphene.wait.FluentWait;
import org.joda.time.DateTime;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.calendar.TimeSpinner.TimeSign;
import org.richfaces.fragment.calendar.TimeSpinner.TimeSignSpinner;
import org.richfaces.fragment.calendar.TimeSpinner.TimeSpinner12;
import org.richfaces.fragment.calendar.TimeSpinner.TimeSpinner24;
import org.richfaces.fragment.calendar.TimeSpinner.TimeSpinner60;
import org.richfaces.fragment.common.Utils;
import org.richfaces.fragment.common.WaitingWrapper;
import org.richfaces.fragment.common.WaitingWrapperImpl;

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

    public WebElement getRootElement() {
        return root;
    }

    protected int getDefaultHours() {
        return defaultHours;
    }

    protected int getDefaultMinutes() {
        return defaultMinutes;
    }

    protected int getDefaultSeconds() {
        return defaultSeconds;
    }

    protected TimeSpinner12 getHoursSpinner12() {
        return hoursSpinner12;
    }

    protected TimeSpinner24 getHoursSpinner24() {
        return hoursSpinner24;
    }

    public enum SetValueBy {

        TYPING, BUTTONS;
    }

    public void cancelTime() {
        if (!isVisible()) {
            throw new RuntimeException("Cannot interact with TimePicker. "
                + "Ensure that it it is opened.");
        }
        if (!getCancelButtonElement().isDisplayed()) {
            throw new RuntimeException("Cancel button is not visible.");
        }
        getCancelButtonElement().click();
        waitUntilIsNotVisible().perform();
    }

    public void confirmTime() {
        if (!isVisible()) {
            throw new RuntimeException("Cannot interact with TimePicker. "
                + "Ensure that it it is opened.");
        }
        if (!getOkButtonElement().isDisplayed()) {
            throw new RuntimeException("Ok button is not visible.");
        }
        getOkButtonElement().click();
        waitUntilIsNotVisible().perform();
    }

    public WebElement getCancelButtonElement() {
        return cancelButtonElement;
    }

    private TimeSpinner<Integer> getHoursSpinner() {
        if (getTimeSignSpinner() == null) {
            if (getHoursSpinner24().isVisible()) {
                return getHoursSpinner24();
            }
        } else {
            if (getHoursSpinner12().isVisible()) {
                return getHoursSpinner12();
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
        int seconds = (getSecondsSpinner() != null ? getSecondsSpinner().getValue() : getDefaultSeconds());
        int minutes = (getMinutesSpinner() != null ? getMinutesSpinner().getValue() : getDefaultMinutes());
        int hours = (getHoursSpinner() != null ? getHoursSpinner().getValue() : getDefaultHours());
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
        return Utils.isVisible(getRootElement());
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

    public void setTimeouFortTimeEditorIsNotVisible(long timeoutInMilliseconds) {
        this._timeoutForTimeEditorToBeNotVisible = timeoutInMilliseconds;
    }

    public long getTimeoutForTimeEditorToBeNotVisible() {
        return _timeoutForTimeEditorToBeNotVisible == -1 ? Utils.getWaitAjaxDefaultTimeout(browser) : _timeoutForTimeEditorToBeNotVisible;
    }

    public void setTimeoutForTimeEditorToBeVisible(long timeoutInMilliseconds) {
        this._timeoutForTimeEditorToBeVisible = timeoutInMilliseconds;
    }

    public long getTimeoutForTimeEditorToBeVisible() {
        return _timeoutForTimeEditorToBeVisible == -1 ? Utils.getWaitAjaxDefaultTimeout(browser) : _timeoutForTimeEditorToBeVisible;
    }

    public WaitingWrapper waitUntilIsNotVisible() {
        return new WaitingWrapperImpl() {
            @Override
            protected void performWait(FluentWait<WebDriver, Void> wait) {
                wait.until().element(getRootElement()).is().not().visible();
            }
        }.withMessage("Waiting for time editor to be not visible.").withTimeout(getTimeoutForTimeEditorToBeNotVisible(), TimeUnit.MILLISECONDS);
    }

    public WaitingWrapper waitUntilIsVisible() {
        return new WaitingWrapperImpl() {
            @Override
            protected void performWait(FluentWait<WebDriver, Void> wait) {
                wait.until().element(getRootElement()).is().visible();
            }
        }.withMessage("Waiting for time editor to be visible.").withTimeout(getTimeoutForTimeEditorToBeVisible(), TimeUnit.MILLISECONDS);
    }
}

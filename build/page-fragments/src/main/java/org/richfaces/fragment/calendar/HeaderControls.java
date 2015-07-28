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
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.GrapheneElement;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.jboss.arquillian.graphene.fragment.Root;
import org.jboss.arquillian.graphene.wait.FluentWait;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.calendar.RichFacesAdvancedInlineCalendar.CalendarEditor;
import org.richfaces.fragment.common.Utils;
import org.richfaces.fragment.common.WaitingWrapper;
import org.richfaces.fragment.common.WaitingWrapperImpl;

/**
 * Component for header controls of calendar.
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class HeaderControls {

    @Root
    private WebElement root;

    @Drone
    private WebDriver browser;

    @FindByJQuery(".rf-cal-tl:eq(0) > div")
    private GrapheneElement previousYearElement;
    @FindByJQuery(".rf-cal-tl:eq(1) > div")
    private GrapheneElement previousMonthElement;
    @FindBy(css = ".rf-cal-hdr-month > div")
    private GrapheneElement yearAndMonthEditorOpenerElement;
    @FindByJQuery(".rf-cal-tl:eq(2) > div")
    private GrapheneElement nextMonthElement;
    @FindByJQuery(".rf-cal-tl:eq(3) > div")
    private GrapheneElement nextYearElement;

    private CalendarEditor calendarEditor;
    private final DateTimeFormatter formatter = DateTimeFormat.forPattern("MMMM, yyyy");

    private long _timeoutForPopupToBeNotVisible = -1;

    private void _openYearAndMonthEditor() {
        if (!isVisible() || !getYearAndMonthEditorOpenerElement().isDisplayed()) {
            throw new RuntimeException("Cannot open date editor. "
                + "Ensure that calendar popup and header controls are displayed and some date is set.");
        }
        getYearAndMonthEditorOpenerElement().click();
        calendarEditor.getDateEditor().waitUntilIsVisible()
            .perform();
    }

    public WebElement getNextMonthElement() {
        return nextMonthElement;
    }

    public WebElement getNextYearElement() {
        return nextYearElement;
    }

    public WebElement getPreviousMonthElement() {
        return previousMonthElement;
    }

    public WebElement getPreviousYearElement() {
        return previousYearElement;
    }

    public DateTime getYearAndMonth() {
        return getFormatter().parseDateTime(getYearAndMonthEditorOpenerElement().getText());
    }

    public YearAndMonthEditor getYearAndMonthEditor() {
        return calendarEditor.getDateEditor();
    }

    public WebElement getYearAndMonthEditorOpenerElement() {
        return yearAndMonthEditorOpenerElement;
    }

    public boolean isVisible() {
        return Utils.isVisible(getRoot());
    }

    public void nextMonth() {
        if (!isVisible() || !getNextMonthElement().isDisplayed()) {
            throw new RuntimeException("Cannot interact with nextMonth button. "
                + "Ensure that calendar popup and header controls are displayed.");
        }
        String before = getYearAndMonthEditorOpenerElement().getText();
        getNextMonthElement().click();
        Graphene.waitAjax().withMessage("Waiting for month to change.").until().element(getYearAndMonthEditorOpenerElement()).text().not().equalTo(before);
    }

    public void nextYear() {
        if (!isVisible() || !getNextYearElement().isDisplayed()) {
            throw new RuntimeException("Cannot interact with nextYear button. "
                + "Ensure that calendar popup and header controls are displayed.");
        }
        String before = getYearAndMonthEditorOpenerElement().getText();
        getNextYearElement().click();
        Graphene.waitAjax().withMessage("Waiting for year to change.").until().element(getYearAndMonthEditorOpenerElement()).text().not().equalTo(before);
    }

    public YearAndMonthEditor openYearAndMonthEditor() {
        if (Utils.isVisible(calendarEditor.getDateEditor().getRootElement())) {
            return calendarEditor.getDateEditor();
        } else {
            _openYearAndMonthEditor();
            return calendarEditor.getDateEditor();
        }
    }

    public void previousYear() {
        if (!isVisible() || !getPreviousYearElement().isDisplayed()) {
            throw new RuntimeException("Cannot interact with previousYear button. "
                + "Ensure that calendar popup and header controls are displayed.");
        }
        String before = getYearAndMonthEditorOpenerElement().getText();
        getPreviousYearElement().click();
        Graphene.waitAjax().withMessage("Waiting for year to change.").until().element(getYearAndMonthEditorOpenerElement()).text().not().equalTo(before);
    }

    public void previousMonth() {
        if (!isVisible() || !getPreviousMonthElement().isDisplayed()) {
            throw new RuntimeException("Cannot interact with previousMonth button. "
                + "Ensure that calendar popup and header controls are displayed.");
        }
        String before = getYearAndMonthEditorOpenerElement().getText();
        getPreviousMonthElement().click();
        Graphene.waitAjax().withMessage("Waiting for month to change.").until().element(getYearAndMonthEditorOpenerElement()).text().not().equalTo(before);
    }

    public void setCalendarEditor(CalendarEditor calendarEditor) {
        this.calendarEditor = calendarEditor;
    }

    public void setTimeoutForPopupToBeNotVisible(long timeoutInMilliseconds) {
        this._timeoutForPopupToBeNotVisible = timeoutInMilliseconds;
    }

    public long getTimeoutForPopupToBeNotVisible() {
        return _timeoutForPopupToBeNotVisible == - 1 ? Utils.getWaitAjaxDefaultTimeout(browser) : _timeoutForPopupToBeNotVisible;
    }

    public WaitingWrapper waitUntilIsNotVisible() {
        return new WaitingWrapperImpl() {
            @Override
            protected void performWait(FluentWait<WebDriver, Void> wait) {
                wait.until().element(getRoot()).is().not().visible();
            }
        }.withMessage("Waiting for header controls to be not visible.")
            .withTimeout(getTimeoutForPopupToBeNotVisible(), TimeUnit.MILLISECONDS);
    }

    public WaitingWrapper waitUntilIsVisible() {
        return new WaitingWrapperImpl() {
            @Override
            protected void performWait(FluentWait<WebDriver, Void> wait) {
                wait.until().element(getRoot()).is().visible();
            }
        }.withMessage("Waiting for header controls to be visible.");
    }

    public WebElement getRoot() {
        return root;
    }

    protected DateTimeFormatter getFormatter() {
        return formatter;
    }
}

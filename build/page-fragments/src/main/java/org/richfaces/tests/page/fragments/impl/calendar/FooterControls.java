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

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.GrapheneElement;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.jboss.arquillian.graphene.fragment.Root;
import org.jboss.arquillian.graphene.wait.FluentWait;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.page.fragments.impl.Utils;
import org.richfaces.tests.page.fragments.impl.calendar.RichFacesAdvancedInlineCalendar.CalendarEditor;
import org.richfaces.tests.page.fragments.impl.utils.WaitingWrapper;
import org.richfaces.tests.page.fragments.impl.utils.WaitingWrapperImpl;

/**
 * Component for footer controls of calendar.
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class FooterControls {

    @Root
    private WebElement root;

    private CalendarEditor calendarEditor;

    @FindByJQuery("div.rf-cal-tl-btn:contains('Clean')")
    private GrapheneElement cleanButtonElement;
    @FindBy(css = "td.rf-cal-tl-ftr > div[onclick*='showTimeEditor']")
    private GrapheneElement timeEditorOpenerElement;
    @FindBy(css = "td.rf-cal-tl-ftr > div[onclick*='showSelectedDate']")
    private GrapheneElement selectedDateElement;
    @FindByJQuery("div.rf-cal-tl-btn:contains('Today')")
    private GrapheneElement todayButtonElement;

    @Drone
    private WebDriver browser;

    private void _openTimeEditor() {
        if (!isVisible()) {
            throw new RuntimeException("Cannot open time editor. "
                + "Ensure that calendar popup and footer controls are displayed.");
        }
        if (!timeEditorOpenerElement.isDisplayed()) {
            throw new RuntimeException("Cannot open time editor. "
                + "Ensure that the date is set before setting time.");
        }
        timeEditorOpenerElement.click();
        calendarEditor.getTimeEditor().waitUntilIsVisible().perform();
    }

    public void cleanDate() {
        if (!isVisible()) {
            throw new RuntimeException("Footer controls are not displayed, cannot interact with  clean button");
        }
        if (!cleanButtonElement.isDisplayed()) {
            throw new RuntimeException("Clean button is not displayed.");
        }
        cleanButtonElement.click();
        Graphene.waitGui().until().element(cleanButtonElement).is().not().visible();
    }

    public WebElement getCleanButtonElement() {
        return cleanButtonElement;
    }

    public TimeEditor getTimeEditor() {
        return calendarEditor.getTimeEditor();
    }

    public WebElement getTimeEditorOpenerElement() {
        return timeEditorOpenerElement;
    }

    public WebElement getTodayButtonElement() {
        return todayButtonElement;
    }

    public WebElement getSelectedDateElement() {
        return selectedDateElement;
    }

    public boolean isVisible() {
        return Utils.isVisible(root);
    }

    public TimeEditor openTimeEditor() {
        if (Utils.isVisible(calendarEditor.getTimeEditor().getRoot())) {
            return calendarEditor.getTimeEditor();
        } else {
            _openTimeEditor();
            return calendarEditor.getTimeEditor();
        }
    }

    public void setCalendarEditor(CalendarEditor calendarEditor) {
        this.calendarEditor = calendarEditor;
    }

    public void setTodaysDate() {
        todayDate();
    }

    public void todayDate() {
        if (!isVisible()) {
            throw new RuntimeException("Footer controls are not displayed, cannot interact with today button");
        }
        if (!todayButtonElement.isDisplayed()) {
            throw new RuntimeException("Today button is not displayed.");
        }
        todayButtonElement.click();
    }

    public WaitingWrapper waitUntilIsNotVisible() {
        return new WaitingWrapperImpl() {
            @Override
            protected void performWait(FluentWait<WebDriver, Void> wait) {
                wait.until().element(root).is().not().visible();
            }
        }.withMessage("Footer controls to be not visible.");
    }

    public WaitingWrapper waitUntilIsVisible() {
        return new WaitingWrapperImpl() {
            @Override
            protected void performWait(FluentWait<WebDriver, Void> wait) {
                wait.until().element(root).is().visible();
            }
        }.withMessage("Footer controls to be visible.");
    }
}

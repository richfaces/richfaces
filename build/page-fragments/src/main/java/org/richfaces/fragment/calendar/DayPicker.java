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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.GrapheneElement;
import org.jboss.arquillian.graphene.findby.ByJQuery;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.jboss.arquillian.graphene.fragment.Root;
import org.jboss.arquillian.graphene.wait.FluentWait;
import org.joda.time.DateTime;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.common.Utils;
import org.richfaces.fragment.common.Validate;
import org.richfaces.fragment.common.WaitingWrapper;
import org.richfaces.fragment.common.WaitingWrapperImpl;
import org.richfaces.fragment.common.picker.ChoicePickerHelper;
import org.richfaces.fragment.common.picker.ChoicePickerHelper.ByIndexChoicePicker;
import org.richfaces.fragment.list.AbstractListComponent;
import org.richfaces.fragment.list.ListItem;
import org.richfaces.fragment.orderingList.AbstractSelectableListItem;

import com.google.common.base.Preconditions;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class DayPicker {

    @Root
    private WebElement root;

    @FindBy(css = "tr[id$=WeekDay]")
    private GrapheneElement weekDaysBarElement;
    @FindBy(css = "tr[id$=WeekDay] > td")
    private List<WebElement> weekDaysLabelsElements;
    @FindBy(css = "tr[id*=WeekNum]")
    private List<CalendarWeek> weeks;
    @FindBy(css = "td[id*=DayCell]:not(.rf-cal-boundary-day):not(.rf-cal-day-lbl)")
    private List<WebElement> monthDaysElements;
    @FindBy(css = "td[id*=DayCell].rf-cal-sel")
    private GrapheneElement selectedDayElement;
    @FindBy(css = "td[id*=DayCell].rf-cal-today")
    private GrapheneElement todayDayElement;
    private CalendarDaysImpl days;

    public List<? extends CalendarDay> getBoundaryDays() {
        if (!isVisible()) {
            throw new RuntimeException("Cannot interact with DayPicker.");
        }
        List<CalendarDayImpl> items = getDays().getItems(ChoicePickerHelper.byWebElement()
            .attribute("class").contains("rf-cal-boundary-day"));
        return items;
    }

    public CalendarDaysImpl getDays() {
        if (days == null) {
            days = Graphene.createPageFragment(CalendarDaysImpl.class, getRootElement());
        }
        return days;
    }

    public List<? extends CalendarDay> getMonthDays() {
        if (!isVisible()) {
            throw new RuntimeException("Cannot interact with DayPicker.");
        }
        List<CalendarDayImpl> items = getDays().getItems(ChoicePickerHelper
            .byWebElement()
            .attribute("class").contains("rf-cal-boundary-day").not()
            .and()
            .attribute("class").contains("rf-cal-day-lbl").not());
        return items;
    }

    public CalendarDay getSelectedDay() {
        if (!isVisible()) {
            throw new RuntimeException("Cannot interact with DayPicker.");
        }
        if (getSelectedDayElement().isPresent()) {
            return Graphene.createPageFragment(CalendarDayImpl.class, getSelectedDayElement());
        }
        return null;
    }

    /**
     * @param weekDayPosition indexes from &lt;1;7&gt;.
     * @return
     */
    public List<? extends CalendarDay> getSpecificDays(Integer... weekDayPosition) {
        if (!isVisible()) {
            throw new RuntimeException("Cannot interact with DayPicker.");
        }
        Preconditions.checkNotNull(weekDayPosition);
        for (Integer integer : weekDayPosition) {
            Preconditions.checkArgument(integer > 0 && integer < 8);
        }

        ByIndexChoicePicker indexPicker = ChoicePickerHelper.byIndex();
        for (Integer integer : weekDayPosition) {
            indexPicker.everyNth(7, integer - 1);
        }
        return getDays().getItems(indexPicker);
    }

    public CalendarDay getTodayDay() {
        if (!isVisible()) {
            throw new RuntimeException("Cannot interact with DayPicker.");
        }
        if (getTodayDayElement().isPresent()) {
            return Graphene.createPageFragment(CalendarDayImpl.class, getTodayDayElement());
        }
        return null;
    }

    public CalendarWeek getWeek(int weekFromActCalendar) {
        if (!isVisible()) {
            throw new RuntimeException("Cannot interact with DayPicker.");
        }
        Validate.isTrue(weekFromActCalendar > 0 && weekFromActCalendar < 7, "weekFromActCalendar needs to be an integer between 0 and 7");
        return getWeeks().get(weekFromActCalendar - 1);
    }

    public List<String> getWeekDayShortNames() {
        if (!isVisible()) {
            throw new RuntimeException("Cannot interact with DayPicker.");
        }
        if (!getWeekDaysBarElement().isDisplayed()) {
            throw new RuntimeException("Week days bar is not visible");
        }
        List<String> result = new ArrayList<String>(8);

        for (WebElement label : getWeekDaysLabelsElements()) {
            result.add(label.getText().trim());
        }
        result.remove(0);
        return result;
    }

    public WebElement getWeekDaysBarElement() {
        return weekDaysBarElement;
    }

    public List<CalendarWeek> getWeeks() {
        if (!isVisible()) {
            throw new RuntimeException("Cannot interact with DayPicker.");
        }
        return Collections.unmodifiableList(weeks);
    }

    public List<Integer> getWeeksNumbers() {
        if (!isVisible()) {
            throw new RuntimeException("Cannot interact with DayPicker.");
        }
        List<Integer> result = new ArrayList<Integer>(6);

        for (CalendarWeek week : getWeeks()) {
            result.add(week.getWeekNumber());
        }
        return result;
    }

    public boolean isVisible() {
        return Utils.isVisible(getRootElement());
    }

    public WebElement getRootElement() {
        return root;
    }

    public void selectDayInMonth(DateTime dateTime) {
        selectDayInMonth(dateTime.getDayOfMonth());
    }

    public void selectDayInMonth(int day) {
        if (!isVisible()) {
            throw new RuntimeException("Cannot interact with DayPicker.");
        }
        Validate.isTrue(day > 0 && day < 32, "day needs to be an integer between 0 and 32");
        Validate.isTrue(getMonthDaysElements().size() >= day, "given month has less days (%s) then provided day (%s)", getMonthDaysElements().size(), day);

        String jq = "td[id*=DayCell]:not('.rf-cal-boundary-day'):not('.rf-cal-day-lbl'):contains('" + day + "')";
        Graphene.createPageFragment(CalendarDayImpl.class, getRootElement().findElement(ByJQuery.selector(jq))).select();
    }

    public WaitingWrapper waitUntilIsNotVisible() {
        return new WaitingWrapperImpl() {
            @Override
            protected void performWait(FluentWait<WebDriver, Void> wait) {
                wait.until().element(getRootElement()).is().not().visible();
            }
        }.withMessage("Waiting for day picker to be not visible.");
    }

    public WaitingWrapper waitUntilIsVisible() {
        return new WaitingWrapperImpl() {
            @Override
            protected void performWait(FluentWait<WebDriver, Void> wait) {
                wait.until().element(getRootElement()).is().visible();
            }
        }.withMessage("Waiting for day picker to be visible.");
    }

    public List<WebElement> getWeekDaysLabelsElements() {
        return weekDaysLabelsElements;
    }

    public List<WebElement> getMonthDaysElements() {
        return monthDaysElements;
    }

    public GrapheneElement getSelectedDayElement() {
        return selectedDayElement;
    }

    public GrapheneElement getTodayDayElement() {
        return todayDayElement;
    }

    public interface CalendarDay {

        /**
         * Parses and returns the element's text representing day number.
         */
        Integer getDayNumber();

        WebElement getDayElement();

        boolean containsStyleClass(String styleClass);

        boolean is(DayType type);

        void select();

        enum DayType {

            boundaryDay("rf-cal-boundary-day"),
            holidayDay("rf-cal-holiday"),
            selectableDay("rf-cal-btn"),
            selectedDay("rf-cal-sel"),
            todayDay("rf-cal-today"),
            weekendDay("rf-cal-holiday");

            private final String styleClass;

            private DayType(String styleClass) {
                this.styleClass = styleClass;
            }

            public boolean isType(WebElement day) {
                return check(day, styleClass);
            }

            public String getStyle() {
                return styleClass;
            }

            public static boolean check(WebElement day, String styleClass) {
                String attribute = day.getAttribute("class");
                if (attribute == null) {
                    return false;
                }
                return attribute.contains(styleClass);
            }
        }
    }

    public static class CalendarDayImpl extends AbstractSelectableListItem implements CalendarDay, ListItem {

        /**
         * Checks if this day contains chosen styleClass
         * @param styleClass
         * @return
         */
        @Override
        public boolean containsStyleClass(String styleClass) {
            return CalendarDay.DayType.check(getRootElement(), styleClass);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final CalendarDayImpl other = (CalendarDayImpl) obj;
            if (!this.getDayElement().equals(other.getDayElement())) {
                return false;
            }
            return true;
        }

        @Override
        public WebElement getDayElement() {
            return getRootElement();
        }

        /**
         * Parses and returns the element's text representing day number.
         * @return
         */
        @Override
        public Integer getDayNumber() {
            return Integer.parseInt(getRootElement().getText().trim());
        }

        @Override
        protected String getStyleClassForSelectedItem() {
            return CalendarDay.DayType.selectedDay.getStyle();
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 79 * hash + (this.getRootElement() != null ? this.getRootElement().hashCode() : 0);
            return hash;
        }

        @Override
        public boolean is(CalendarDay.DayType type) {
            return type.isType(getRootElement());
        }

        @Override
        public void select(boolean deselectOthers) {
            // https://issues.jboss.org/browse/RF-14033
            getRootElement().click();
            Graphene.waitGui().withMessage("Waiting for day to be selected.").until().element(getRootElement()).attribute("class").contains(getStyleClassForSelectedItem());
        }
    }

    public static class CalendarDaysImpl extends AbstractListComponent<CalendarDayImpl> {

        @FindByJQuery("td[id*='DayCell']:not(td[id*='WeekDayCell'])")
        private List<WebElement> items;

        @Override
        protected List<WebElement> getItemsElements() {
            return Collections.unmodifiableList(items);
        }
    }

    public static class CalendarWeek {

        @FindBy(css = "td[id*=WeekNumCell]")
        private WebElement weekNumberElement;
        @FindBy(css = "td[id*=DayCell]")
        private List<CalendarDayImpl> days;

        /**
         * Returns calendar days in this week.
         */
        public List<? extends CalendarDay> getCalendarDays() {
            return Collections.unmodifiableList(days);
        }

        /**
         * Returns week number.
         */
        public Integer getWeekNumber() {
            if (!weekNumberElement.isDisplayed()) {
                throw new RuntimeException("Week numbers are not displayed");
            }
            return Integer.parseInt(getWeekNumberElement().getText());
        }

        public WebElement getWeekNumberElement() {
            return weekNumberElement;
        }
    }
}

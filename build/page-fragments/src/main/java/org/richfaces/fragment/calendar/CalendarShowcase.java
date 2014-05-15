package org.richfaces.fragment.calendar;

import org.joda.time.DateTime;
import org.openqa.selenium.support.FindBy;

public class CalendarShowcase {

    @FindBy
    private RichFacesCalendar calendar;

    public void showcase_calendar() {
        // will set current date via r:calendar JS API
        calendar.setDate(new DateTime());
        DateTime selectedDate = calendar.getDate();

        // will use interactive way of setting of date (clicking on calendar elements via WebDriver - much slower)
        calendar.advanced().setupInteractiveStrategy();
        calendar.setDate(new DateTime());
    }
}

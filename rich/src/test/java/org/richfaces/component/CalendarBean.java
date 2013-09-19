package org.richfaces.component;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.faces.event.ValueChangeEvent;

public class CalendarBean {
    public static int CURRENT_YEAR = 2010;
    public static int CURRENT_MONTH = 10;
    public static int CURRENT_DAY = 16;
    private Locale locale;
    private boolean popup;
    private String pattern;
    private Date selectedDate = null;
    private boolean showApply = true;
    private boolean useCustomDayLabels;
    private String mode;

    public CalendarBean() {

        locale = Locale.US;
        popup = true;
        pattern = "d/M/yy HH:mm";
        mode = "client";

        Calendar calendar = Calendar.getInstance();
        calendar.set(CURRENT_YEAR, CURRENT_MONTH, CURRENT_DAY, 0, 0, 0);
        selectedDate = calendar.getTime();
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public boolean isPopup() {
        return popup;
    }

    public void setPopup(boolean popup) {
        this.popup = popup;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public void selectLocale(ValueChangeEvent event) {

        String tLocale = (String) event.getNewValue();
        if (tLocale != null) {
            String lang = tLocale.substring(0, 2);
            String country = tLocale.substring(3);
            locale = new Locale(lang, country, "");
        }
    }

    public boolean isUseCustomDayLabels() {
        return useCustomDayLabels;
    }

    public void setUseCustomDayLabels(boolean useCustomDayLabels) {
        this.useCustomDayLabels = useCustomDayLabels;
    }

    public Date getSelectedDate() {
        return selectedDate;
    }

    public void setSelectedDate(Date selectedDate) {
        this.selectedDate = selectedDate;
    }

    public boolean isShowApply() {
        return showApply;
    }

    public void setShowApply(boolean showApply) {
        this.showApply = showApply;
    }
}
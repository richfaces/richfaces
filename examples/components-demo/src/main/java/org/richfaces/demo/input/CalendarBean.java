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

package org.richfaces.demo.input;

import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ValueChangeEvent;

import org.richfaces.component.Positioning;
import org.richfaces.event.CurrentDateChangeEvent;

@ManagedBean
@SessionScoped
public class CalendarBean {
    private Locale locale;
    private boolean popup;
    private String pattern;
    private Date selectedDate;
    private boolean showApply = true;
    private boolean useCustomDayLabels;
    private String mode = "client";
    private Positioning jointPoint = Positioning.DEFAULT;
    private Positioning direction = Positioning.DEFAULT;
    private int horizontalOffset = 0;
    private int verticalOffset = 0;
    private Positioning[] positioningValues = Positioning.values();
    private TimeZone timeZone = TimeZone.getTimeZone("EST");

    public CalendarBean() {

        locale = Locale.US;
        popup = true;
        pattern = "d/M/yy HH:mm";
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

    public void setJointPoint(Positioning jointPoint) {
        this.jointPoint = jointPoint;
    }

    public Positioning getJointPoint() {
        return jointPoint;
    }

    public void setDirection(Positioning direction) {
        this.direction = direction;
    }

    public Positioning getDirection() {
        return direction;
    }

    public void setHorizontalOffset(int horizontalOffset) {
        this.horizontalOffset = horizontalOffset;
    }

    public int getHorizontalOffset() {
        return horizontalOffset;
    }

    public void setVerticalOffset(int verticalOffset) {
        this.verticalOffset = verticalOffset;
    }

    public int getVerticalOffset() {
        return verticalOffset;
    }

    public void doValueChangeListener(ValueChangeEvent event) {
        System.out
                .println("doValueChangeListener: old value = " + event.getOldValue() + ", new value = " + event.getNewValue());
    }

    public void doCurrentDataChangeListener(CurrentDateChangeEvent event) {
        System.out.println("doCurrentDataChangeListener: " + event.getCurrentDateString());
    }

    public Positioning[] getPositioningValues() {
        return positioningValues;
    }

    public void setTimeZone(TimeZone timeZone) {
        this.timeZone = timeZone;
    }

    public TimeZone getTimeZone() {
        return timeZone;
    }
}
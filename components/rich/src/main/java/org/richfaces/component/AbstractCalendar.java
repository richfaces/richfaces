/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc. and individual contributors
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
package org.richfaces.component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UIViewRoot;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitResult;
import javax.faces.context.FacesContext;
import javax.faces.convert.DateTimeConverter;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;

import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.EventName;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.component.attribute.CoreProps;
import org.richfaces.component.attribute.EventsPopupsProps;
import org.richfaces.component.attribute.PopupsProps;
import org.richfaces.component.attribute.PositionProps;
import org.richfaces.component.attribute.StyleClassProps;
import org.richfaces.component.attribute.StyleProps;
import org.richfaces.context.ExtendedVisitContext;
import org.richfaces.context.ExtendedVisitContextMode;
import org.richfaces.event.CurrentDateChangeEvent;
import org.richfaces.event.CurrentDateChangeListener;
import org.richfaces.log.Logger;
import org.richfaces.log.RichfacesLogger;
import org.richfaces.model.CalendarDataModel;
import org.richfaces.model.CalendarDataModelItem;
import org.richfaces.renderkit.MetaComponentRenderer;
import org.richfaces.utils.CalendarHelper;
import org.richfaces.view.facelets.CalendarHandler;

/**
 * <p> The &lt;rich:calendar&gt; component allows the user to enter a date and time through an in-line or pop-up
 * calendar. The pop-up calendar can navigate through months and years, and its look and feel can be highly customized.
 * </p>
 *
 * @author amarkhel
 */
@JsfComponent(type = AbstractCalendar.COMPONENT_TYPE, family = AbstractCalendar.COMPONENT_FAMILY,
        renderer = @JsfRenderer(type = "org.richfaces.CalendarRenderer"),
        tag = @Tag(name = "calendar", handlerClass = CalendarHandler.class))
public abstract class AbstractCalendar extends UIInput implements MetaComponentResolver, MetaComponentEncoder, CoreProps, EventsPopupsProps, PopupsProps, PositionProps, StyleClassProps, StyleProps {
    public static final String DAYSDATA_META_COMPONENT_ID = "daysData";
    public static final String COMPONENT_TYPE = "org.richfaces.Calendar";
    public static final String COMPONENT_FAMILY = "org.richfaces.Calendar";
    public static final String SUB_TIME_PATTERN = "\\s*[hHkKma]+[\\W&&\\S]+[hHkKma]+[\\W&&\\S]*[s]*\\s*";
    public static final String TIME_PATTERN = "HH:mm:ss";
    public static final String DEFAULT_DATE_PATTERN = "MMM d, yyyy";
    Logger log = RichfacesLogger.COMPONENTS.getLogger();

    protected enum PropertyKeys {
        locale
    }

    public enum Mode {
        client, ajax
    }

    /**
     * Used to format the date and time strings, according to ISO 8601 (for example, d/M/yy HH:mm a)
     */
    @Attribute
    public abstract String getDatePattern();

    /**
     * <p>
     * Used for current date calculations
     * </p>
     * <p>
     * Default value is "getDefaultTimeZone()"
     * </p>
     */
    @Attribute
    public abstract TimeZone getTimeZone();

    /**
     * <p>
     * Determines the first day of the week is; e.g., SUNDAY in the U.S., MONDAY in France. Possible values should be integers
     * from 0 to 6, 0 corresponds to Sunday
     * </p>
     * <p>
     * Default value is "getDefaultFirstWeekDay()"
     * </p>
     */
    @Attribute
    public abstract int getFirstWeekDay();

    /**
     * <p>
     * Gets what the minimal days required in the first week of the year are; e.g., if the first week is defined as one that
     * contains the first day of the first month of a year, this method returns 1. If the minimal days required must be a full
     * week, this method returns 7.
     * </p>
     * <p>
     * Default value is "getDefaultMinDaysInFirstWeek()"
     * </p>
     */
    @Attribute
    public abstract int getMinDaysInFirstWeek();

    /**
     * <p>
     * This attribute defines the mode for "today" control. Possible values are "scroll", "select", "hidden"
     * </p>
     * <p>
     * Default value is "select"
     * </p>
     */
    @Attribute
    public abstract String getTodayControlMode();

    /**
     * <p>
     * If false this bar should not be shown
     * </p>
     * <p>
     * Default value is "true"
     * </p>
     */
    @Attribute(defaultValue = "true")
    public abstract boolean isShowWeekDaysBar();

    /**
     * <p>
     * If false this bar should not be shown
     * </p>
     * <p>
     * Default value is "true"
     * </p>
     */
    @Attribute(defaultValue = "true")
    public abstract boolean isShowWeeksBar();

    /**
     * <p>
     * If false Calendar's footer should not be shown
     * </p>
     * <p>
     * Default value is "true"
     * </p>
     */
    @Attribute(defaultValue = "true")
    public abstract boolean isShowFooter();

    /**
     * <p>
     * If false Calendar's header should not be shown
     * </p>
     * <p>
     * Default value is "true"
     * </p>
     */
    @Attribute(defaultValue = "true")
    public abstract boolean isShowHeader();

    /**
     * <p>
     * "false" value for this attribute makes text field invisible. It works only if popupMode="true" If showInput is "true" -
     * input field will be shown
     * </p>
     * <p>
     * Default value is "true"
     * </p>
     */
    @Attribute(defaultValue = "true")
    public abstract boolean isShowInput();

    /**
     * <p>
     * If "true", the calendar will be rendered initially as hidden with additional elements for calling as popup
     * </p>
     * <p>
     * Default value is "true"
     * </p>
     */
    @Attribute(defaultValue = "true")
    public abstract boolean isPopup();

    /**
     * <p>
     * If "true", rendered is disabled. In "popup" mode both controls are disabled
     * </p>
     * <p>
     * Default value is "false"
     * </p>
     */
    @Attribute
    public abstract boolean isDisabled();

    /**
     * <p>
     * If "true" calendar input will be editable and it will be possible to change the date manually. If "false" the text field
     * will be "read-only", so the value can be changed only from a handle.
     * </p>
     * <p>
     * Default value is "false"
     * </p>
     */
    @Attribute
    public abstract boolean isEnableManualInput();

    /**
     * <p>
     * If "true". Date and time are not selectable. In "popup" mode input is disabled and button is enabled.
     * </p>
     * <p>
     * Default value is "false"
     * </p>
     */
    @Attribute(defaultValue = "false")
    public abstract boolean isReadonly();

    /**
     * <p>
     * The javascript function that enables or disables a day cell
     * </p>
     */
    @Attribute
    public abstract String getDayDisableFunction();

    /**
     * <p>
     * If false ApplyButton should not be shown
     * </p>
     * <p>
     * Default value is "false"
     * </p>
     */
    @Attribute
    public abstract boolean isShowApplyButton();

    /**
     * <p>
     * If value is true then calendar should change time to defaultTime for newly-selected dates
     * </p>
     * <p>
     * Default value is "false"
     * </p>
     */
    @Attribute
    public abstract boolean isResetTimeOnDateSelect();

    /**
     * <p>
     * This attribute is responsible for behaviour of dates from the previous and next months which are displayed in the current
     * month. Valid values are "inactive" (Default) dates inactive and gray colored, "scroll" boundaries work as month scrolling
     * controls, and "select" boundaries work in the same way as "scroll" but with the date clicked selection
     * </p>
     * <p>
     * Default value is "inactive"
     * </p>
     */
    @Attribute
    public abstract String getBoundaryDatesMode();

    /**
     * <p>
     * Valid values: ajax or client
     * </p>
     * <p>
     * Default value is "client"
     * </p>
     */
    @Attribute
    public abstract Mode getMode();

    /**
     * The starting label can be set when in the initial view state. If the initial value is already set through the value
     * attribute, this is displayed instead.
     */
    @Attribute
    public abstract String getDefaultLabel();

    /**
     * CSS style(s) to be applied to the popup element
     */
    @Attribute
    public abstract String getPopupStyle();

    /**
     * Space-separated list of CSS style class(es) to be applied to the popup element. This value must be passed through as the
     * "class" attribute on generated markup.
     */
    @Attribute
    public abstract String getPopupClass();

    /**
     * Attribute that allows to customize names of the months. Should accept list with the month names
     */
    @Attribute
    public abstract Object getMonthLabels();

    /**
     * Attribute that allows to customize short names of the months. Should accept list with the month names
     */
    @Attribute
    public abstract Object getMonthLabelsShort();

    /**
     * Attribute that allows to customize short names of the weekdays. Should accept list with the weekday's names.
     */
    @Attribute
    public abstract Object getWeekDayLabelsShort();

    /**
     * The javascript function that determines the CSS style class for each day cell
     */
    @Attribute
    public abstract String getDayClassFunction();

    /**
     * Position of this element in the tabbing order for the current document. This value must be an integer between 0 and
     * 32767.
     */
    @Attribute
    public abstract String getTabindex();

    /**
     * CSS style(s) to be applied to the input element
     */
    @Attribute
    public abstract String getInputStyle();

    /**
     * Space-separated list of CSS style class(es) to be applied to the button element. This value must be passed through as the
     * "class" attribute on generated markup.
     */
    @Attribute
    public abstract String getButtonClass();

    /**
     * Space-separated list of CSS style class(es) to be applied to the input element. This value must be passed through as the
     * "class" attribute on generated markup.
     */
    @Attribute
    public abstract String getInputClass();

    /**
     * Defines label for the popup button element. If the attribute is set "buttonIcon" and "buttonIconDisabled" are ignored
     */
    @Attribute
    public abstract String getButtonLabel();

    /**
     * Defines the size of an input field. Similar to the "size" attribute of &lt;h:inputText/&gt;
     */
    @Attribute
    public abstract String getInputSize();

    /**
     * Used to define the month and year which will be displayed
     */
    @Attribute
    public abstract Object getCurrentDate();

    @Attribute
    public abstract void setCurrentDate(Object date);

    /**
     * Defines icon for the popup button element. The attribute is ignored if the "buttonLabel" is set
     */
    @Attribute
    public abstract String getButtonIcon();

    /**
     * Defines disabled icon for the popup button element. The attribute is ignored if the "buttonLabel" is set
     */
    @Attribute
    public abstract String getButtonDisabledIcon();

    /**
     * <p>
     * Defines time that will be used:
     * </p>
     * <ol>
     * <li>to set time when the value is empty</li>
     * <li>to set time when date changes and flag "resetTimeOnDateSelect" is true</li>
     * </ol>
     * <p>
     * Default value is "getDefaultValueOfDefaultTime()"
     * </p>
     */
    @Attribute
    public abstract Object getDefaultTime();

    /**
     * <p>
     * Defines the last range of date which will be loaded to client from dataModel under rendering
     * </p>
     * <p>
     * Default value is "getDefaultPreloadEnd(getCurrentDateOrDefault())"
     * </p>
     */
    @Attribute
    public abstract Object getPreloadDateRangeBegin();

    public abstract void setPreloadDateRangeBegin(Object date);

    /**
     * <p>
     * Define the initial range of date which will be loaded to client from dataModel under rendering
     * </p>
     * <p>
     * Default value is "getDefaultPreloadBegin(getCurrentDateOrDefault())"
     * </p>
     */
    @Attribute
    public abstract Object getPreloadDateRangeEnd();

    public abstract void setPreloadDateRangeEnd(Object date);

    /**
     * Used to provide data for calendar elements. If data is not provided, all Data Model related functions are disabled
     */
    @Attribute
    public abstract CalendarDataModel getDataModel();

    // ---------------- Input events

    /**
     * Javascript code executed when a pointer button is clicked over the input element.
     */
    @Attribute(events = @EventName("inputclick"))
    public abstract String getOninputclick();

    /**
     * Javascript code executed when a pointer button is double clicked over the input element.
     */
    @Attribute(events = @EventName("inputdblclick"))
    public abstract String getOninputdblclick();

    /**
     * Javascript code executed when the input field value is changed manually
     */
    @Attribute(events = @EventName("inputchange"))
    public abstract String getOninputchange();

    /**
     * Javascript code executed called when the input field value is selected
     */
    @Attribute(events = @EventName("inputselect"))
    public abstract String getOninputselect();

    /**
     * Javascript code executed when a pointer button is pressed down over the input element.
     */
    @Attribute(events = @EventName("inputmousedown"))
    public abstract String getOninputmousedown();

    /**
     * Javascript code executed when a pointer button is moved within the input element.
     */
    @Attribute(events = @EventName("inputmousemove"))
    public abstract String getOninputmousemove();

    /**
     * Javascript code executed when a pointer button is moved away from the input element.
     */
    @Attribute(events = @EventName("inputmouseout"))
    public abstract String getOninputmouseout();

    /**
     * Javascript code executed when a pointer button is moved onto the input element.
     */
    @Attribute(events = @EventName("inputmouseover"))
    public abstract String getOninputmouseover();

    /**
     * Javascript code executed when a pointer button is released over the input element.
     */
    @Attribute(events = @EventName("inputmouseup"))
    public abstract String getOninputmouseup();

    /**
     * Javascript code executed when a key is pressed down over the input element.
     */
    @Attribute(events = @EventName("inputkeydown"))
    public abstract String getOninputkeydown();

    /**
     * Javascript code executed when a key is pressed and released over the input element.
     */
    @Attribute(events = @EventName("inputkeypress"))
    public abstract String getOninputkeypress();

    /**
     * Javascript code executed when a key is released over the input element.
     */
    @Attribute(events = @EventName("inputkeyup"))
    public abstract String getOninputkeyup();

    /**
     * Javascript code executed when the input element receives focus.
     */
    @Attribute(events = @EventName("inputfocus"))
    public abstract String getOninputfocus();

    /**
     * Javascript code executed when the input element loses focus.
     */
    @Attribute(events = @EventName("inputblur"))
    public abstract String getOninputblur();

    // ---------------------

    /**
     * Javascript code executed when this element loses focus and its value has been modified since gaining focus.
     */
    @Attribute(events = @EventName(value = "change", defaultEvent = true))
    public abstract String getOnchange();

    /**
     * The client-side script method to be called when some date cell is selected
     */
    // -------------- Date select events
    @Attribute(events = @EventName("dateselect"))
    public abstract String getOndateselect();

    /**
     * The client-side script method to be called before some date cell is selected
     */
    @Attribute(events = @EventName("beforedateselect"))
    public abstract String getOnbeforedateselect();

    /**
     * The client-side script method to be called when the current month or year is changed
     */
    @Attribute(events = @EventName("currentdateselect"))
    public abstract String getOncurrentdateselect();

    /**
     * The client-side script method to be called before the current month or year is changed
     */
    @Attribute(events = @EventName("beforecurrentdateselect"))
    public abstract String getOnbeforecurrentdateselect();

    // ----------------

    /**
     * The client-side script method to be called after the DOM is updated
     */
    @Attribute(events = @EventName("complete"))
    public abstract String getOncomplete();

    /**
     * The client-side script method to be called when a pointer is moved away from the date cell
     */
    @Attribute(events = @EventName("datemouseout"))
    public abstract String getOndatemouseout();

    /**
     * The client-side script method to be called when a pointer is moved onto the date cell
     */
    @Attribute(events = @EventName("datemouseover"))
    public abstract String getOndatemouseover();

    /**
     * The client-side script method to be called after time is selected
     */
    @Attribute(events = @EventName("timeselect"))
    public abstract String getOntimeselect();

    /**
     * The client-side script method to be called before time is selected
     */
    @Attribute(events = @EventName("beforetimeselect"))
    public abstract String getOnbeforetimeselect();

    /**
     * The client-side script method to be called before the component is cleaned
     */
    @Attribute(events = @EventName("clean"))
    public abstract String getOnclean();

    /**
     * <p>
     * Used for locale definition
     * </p>
     * <p>
     * Default value is "getDefaultLocale()"
     * </p>
     */
    @Attribute
    public Object getLocale() {
        Object locale = getStateHelper().eval(PropertyKeys.locale);
        if (locale == null) {
            FacesContext facesContext = getFacesContext();
            UIViewRoot viewRoot = facesContext.getViewRoot();
            if (viewRoot != null) {
                locale = viewRoot.getLocale();
            }
        }
        return locale != null ? locale : Locale.US;
    }

    public void setLocale(Object locale) {
        getStateHelper().put(PropertyKeys.locale, locale);
    }

    @Override
    public void broadcast(FacesEvent event) throws AbortProcessingException {
        if (event instanceof CurrentDateChangeEvent) {
            FacesContext facesContext = getFacesContext();
            CurrentDateChangeEvent currentDateChangeEvent = (CurrentDateChangeEvent) event;
            String currentDateString = currentDateChangeEvent.getCurrentDateString();
            try {
                // we should use datePattern attribute-based converter only for
                // selectedDate
                // current date string always has predefined format: m/y
                Date currentDate = CalendarHelper.getAsDate(facesContext, this, getCurrentDate());
                Date submittedCurrentDate = CalendarHelper.convertCurrentDate(currentDateString, facesContext, this);
                currentDateChangeEvent.setCurrentDate(submittedCurrentDate);

                if (!submittedCurrentDate.equals(currentDate)) {
                    updateCurrentDate(facesContext, submittedCurrentDate);
                }
            } catch (Exception e) {
                if (log.isDebugEnabled()) {
                    log.debug(" currentDate convertion fails with following exception: " + e.toString(), e);
                }
                setValid(false);
                String messageString = e.toString();
                FacesMessage message = new FacesMessage(messageString);
                message.setSeverity(FacesMessage.SEVERITY_ERROR);
                facesContext.addMessage(getClientId(facesContext), message);
                facesContext.renderResponse();
            }
        }
        super.broadcast(event);
    }

    public void updateCurrentDate(FacesContext facesContext, Object currentDate) {
        if (facesContext == null) {
            throw new NullPointerException();
        }
        // RF-1073
        try {
            ValueExpression ve = getValueExpression("currentDate");
            if (ve != null) {
                ELContext elContext = facesContext.getELContext();
                if (ve.getType(elContext).equals(String.class)) {
                    DateTimeConverter convert = new DateTimeConverter();
                    convert.setLocale(CalendarHelper.getAsLocale(facesContext, this, getLocale()));
                    convert.setPattern(CalendarHelper.getDatePatternOrDefault(this));
                    ve.setValue(facesContext.getELContext(), convert.getAsString(facesContext, this, currentDate));
                    return;
                } else if (ve.getType(elContext).equals(Calendar.class)) {
                    Calendar c = CalendarHelper.getCalendar(facesContext, this);
                    c.setTime((Date) currentDate);
                    ve.setValue(elContext, c);
                    return;
                } else {
                    ve.setValue(elContext, currentDate);
                    return;
                }
            } else {
                setCurrentDate(currentDate);
            }
        } catch (Exception e) {
            setValid(false);
            if (log.isDebugEnabled()) {
                log.debug(" updateCurrentDate method throws exception: " + e.toString(), e);
            }

            String messageString = e.toString();
            FacesMessage message = new FacesMessage(messageString);
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            facesContext.addMessage(getClientId(facesContext), message);
        }
    }

    public void addCurrentDateChangeListener(CurrentDateChangeListener listener) {
        addFacesListener(listener);
    }

    public void removeCurrentDateChangeListener(CurrentDateChangeListener listener) {
        removeFacesListener(listener);
    }

    public CurrentDateChangeListener[] getCurrentDateChangeListeners() {
        return (CurrentDateChangeListener[]) getFacesListeners(CurrentDateChangeListener.class);
    }

    public static Object getDefaultValueOfDefaultTime(FacesContext facesContext, AbstractCalendar calendarComponent) {
        if (calendarComponent == null) {
            return null;
        }

        Calendar calendar = CalendarHelper.getCalendar(facesContext, calendarComponent);
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    protected Date getDefaultPreloadBegin(Date date) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Calendar calendar = Calendar.getInstance(CalendarHelper.getTimeZoneOrDefault(this),
                CalendarHelper.getAsLocale(facesContext, this, getLocale()));
        calendar.setTime(date);
        calendar.set(Calendar.DATE, calendar.getActualMinimum(Calendar.DATE));
        return calendar.getTime();
    }

    protected Date getDefaultPreloadEnd(Date date) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Calendar calendar = Calendar.getInstance(CalendarHelper.getTimeZoneOrDefault(this),
                CalendarHelper.getAsLocale(facesContext, this, getLocale()));
        calendar.setTime(date);
        calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
        /*
         * //force recalculation calendar.getTimeInMillis(); calendar.set(Calendar.DAY_OF_WEEK, getLastDayOfWeek(calendar));
         */
        return calendar.getTime();
    }

    public Date getCurrentDateOrDefault() {
        FacesContext facesContext = FacesContext.getCurrentInstance();

        Date date = CalendarHelper.getAsDate(facesContext, this, getCurrentDate());

        if (date != null) {
            return date;
        } else {
            Date value = CalendarHelper.getAsDate(facesContext, this, this.getValue());
            if (value != null) {
                return value;
            } else {
                return java.util.Calendar.getInstance(CalendarHelper.getTimeZoneOrDefault(this)).getTime();
            }
        }
    }

    public String resolveClientId(FacesContext facesContext, UIComponent contextComponent, String metaComponentId) {
        if (DAYSDATA_META_COMPONENT_ID.equals(metaComponentId)) {
            return getClientId(facesContext) + MetaComponentResolver.META_COMPONENT_SEPARATOR_CHAR + metaComponentId;
        }
        return null;
    }

    public String substituteUnresolvedClientId(FacesContext facesContext, UIComponent contextComponent, String metaComponentId) {
        return null;
    }

    @Override
    public boolean visitTree(VisitContext context, VisitCallback callback) {
        if (!isVisitable(context)) {
            return false;
        }

        FacesContext facesContext = context.getFacesContext();
        pushComponentToEL(facesContext, null);

        try {
            VisitResult result = context.invokeVisitCallback(this, callback);

            if (result == VisitResult.COMPLETE) {
                return true;
            }

            if (result == VisitResult.ACCEPT) {
                if (context instanceof ExtendedVisitContext) {
                    ExtendedVisitContext extendedVisitContext = (ExtendedVisitContext) context;
                    if (extendedVisitContext.getVisitMode() == ExtendedVisitContextMode.RENDER) {

                        result = extendedVisitContext.invokeMetaComponentVisitCallback(this, callback,
                                DAYSDATA_META_COMPONENT_ID);
                        if (result == VisitResult.COMPLETE) {
                            return true;
                        }
                    }
                }
            }

            if (result == VisitResult.ACCEPT) {
                Iterator<UIComponent> kids = this.getFacetsAndChildren();

                while (kids.hasNext()) {
                    boolean done = kids.next().visitTree(context, callback);

                    if (done) {
                        return true;
                    }
                }
            }
        } finally {
            popComponentFromEL(facesContext);
        }

        return false;
    }

    public void encodeMetaComponent(FacesContext context, String metaComponentId) throws IOException {
        ((MetaComponentRenderer) getRenderer(context)).encodeMetaComponent(context, this, metaComponentId);
    }

    public Object getPreload() {
        Date[] preloadDateRange = getPreloadDateRange();
        if (preloadDateRange != null && preloadDateRange.length != 0) {
            CalendarDataModel calendarDataModel = (CalendarDataModel) getDataModel();
            if (calendarDataModel != null) {
                CalendarDataModelItem[] calendarDataModelItems = calendarDataModel.getData(preloadDateRange);

                HashMap<String, Object> args = new HashMap<String, Object>();

                args.put("startDate", formatStartDate(preloadDateRange[0]));
                args.put("days", deleteEmptyPropeties(calendarDataModelItems));
                return args;
            }
        }
        return null;
    }

    public static Object formatStartDate(Date date) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        AbstractCalendar calendarInstance = (AbstractCalendar) AbstractCalendar.getCurrentComponent(facesContext);
        Calendar calendar = CalendarHelper.getCalendar(facesContext, calendarInstance);
        calendar.setTime(date);
        HashMap<String, Object> hashDate = new HashMap<String, Object>();
        hashDate.put("month", calendar.get(Calendar.MONTH));
        hashDate.put("year", calendar.get(Calendar.YEAR));
        return hashDate;
    }

    public ArrayList<Object> deleteEmptyPropeties(CalendarDataModelItem[] calendarDataModelItems) {
        ArrayList<Object> hashItems = new ArrayList<Object>();
        for (CalendarDataModelItem item : calendarDataModelItems) {
            HashMap<String, Object> itemPropertiesMap = new HashMap<String, Object>();
            if (null != item) {
                if (!item.isEnabled()) {
                    itemPropertiesMap.put("enabled", item.isEnabled());
                }
                if (null != item.getStyleClass() && !item.getStyleClass().equalsIgnoreCase("")) {
                    itemPropertiesMap.put("styleClass", item.getStyleClass());
                }
            }
            hashItems.add(itemPropertiesMap);
        }
        return hashItems;
    }

    public Date[] getPreloadDateRange() {
        FacesContext facesContext = FacesContext.getCurrentInstance();

        Date dateRangeBegin = null;
        Date dateRangeEnd = null;

        Mode mode = getMode();
        if (mode == null) {
            mode = Mode.client;
        }

        if (Mode.ajax.equals(mode)) {
            dateRangeBegin = CalendarHelper.getAsDate(facesContext, this,
                    getDefaultPreloadBegin((Date) getCurrentDateOrDefault()));
            dateRangeEnd = CalendarHelper.getAsDate(facesContext, this, getDefaultPreloadEnd((Date) getCurrentDateOrDefault()));
        } else {

            Object date = getPreloadDateRangeBegin();
            if (date == null) {
                date = getDefaultPreloadBegin(getCurrentDateOrDefault());
            }
            dateRangeBegin = CalendarHelper.getAsDate(facesContext, this, date);

            date = getPreloadDateRangeEnd();
            if (date == null) {
                date = getDefaultPreloadEnd(getCurrentDateOrDefault());
            }
            dateRangeEnd = CalendarHelper.getAsDate(facesContext, this, date);
        }

        if (dateRangeBegin == null && dateRangeEnd == null) {
            return null;
        } else {
            if (dateRangeBegin.after(dateRangeEnd)) {
                // XXX add message
                FacesMessage message = new FacesMessage("preloadDateRangeBegin is greater than preloadDateRangeEnd");
                message.setSeverity(FacesMessage.SEVERITY_ERROR);
                facesContext.addMessage(getClientId(facesContext), message);
                throw new IllegalArgumentException();
            }

            List<Date> dates = new ArrayList<Date>();

            Calendar calendar = Calendar.getInstance(CalendarHelper.getTimeZoneOrDefault(this),
                    CalendarHelper.getAsLocale(facesContext, this, this.getLocale()));
            Calendar calendar2 = (Calendar) calendar.clone();
            calendar.setTime(dateRangeBegin);
            calendar2.setTime(dateRangeEnd);

            do {
                dates.add(calendar.getTime());
                calendar.add(Calendar.DATE, 1);
            } while (!calendar.after(calendar2));

            return (Date[]) dates.toArray(new Date[dates.size()]);
        }
    }
}

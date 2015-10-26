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
package org.richfaces.renderkit;

import java.io.IOException;
import java.text.DateFormatSymbols;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.TimeZone;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.PartialViewContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.DateTimeConverter;

import org.ajax4jsf.javascript.JSFunction;
import org.ajax4jsf.javascript.JSReference;
import org.richfaces.component.AbstractCalendar;
import org.richfaces.component.MetaComponentResolver;
import org.richfaces.component.Positioning;
import org.richfaces.component.util.HtmlUtil;
import org.richfaces.component.util.InputUtils;
import org.richfaces.component.util.InputUtils.ConverterLookupStrategy;
import org.richfaces.component.util.MessageUtil;
import org.richfaces.context.ExtendedPartialViewContext;
import org.richfaces.event.CurrentDateChangeEvent;
import org.richfaces.utils.CalendarHelper;

/**
 * @author amarkhel
 *
 */
@ResourceDependencies({ @ResourceDependency(library = "javax.faces", name = "jsf.js"),
        @ResourceDependency(library = "org.richfaces", name = "jquery.js"),
        @ResourceDependency(library = "org.richfaces", name = "richfaces.js"),
        @ResourceDependency(library = "org.richfaces", name = "richfaces-queue.reslib"),
        @ResourceDependency(library = "org.richfaces", name = "richfaces-base-component.js"),
        @ResourceDependency(library = "org.richfaces", name = "jquery.position.js"),
        @ResourceDependency(library = "org.richfaces", name = "richfaces-event.js"),
        @ResourceDependency(library = "org.richfaces", name = "json-dom.js"),
        @ResourceDependency(library = "com.jqueryui", name = "effect.js"),
        @ResourceDependency(library = "com.jqueryui", name = "effect-highlight.js"),
        @ResourceDependency(library = "org.richfaces", name = "JQuerySpinBtn.js"),
        @ResourceDependency(library = "org.richfaces", name = "calendar-utils.js"),
        @ResourceDependency(library = "org.richfaces", name = "calendar.js"),
        @ResourceDependency(library = "org.richfaces", name = "calendar.ecss") })
public class CalendarRendererBase extends InputRendererBase implements MetaComponentRenderer {
    public static final String CALENDAR_BUNDLE = "org.richfaces.renderkit.calendar";
    public static final String OPTION_DISABLED = "disabled";
    public static final String OPTION_MIN_DAYS_IN_FIRST_WEEK = "minDaysInFirstWeek";
    public static final String MONTH_LABELS_SHORT = "monthLabelsShort";
    public static final String MONTH_LABELS = "monthLabels";
    public static final String WEEK_DAY_LABELS_SHORT = "weekDayLabelsShort";
    public static final String FIRST_DAY_WEEK = "firstWeekDay";
    public static final String MIN_DAYS_IN_FIRST_WEEK = "minDaysInFirstWeek";
    public static final String CALENDAR_ICON_RESOURCE_NAME = "calendarIcon.png";
    public static final String CALENDAR_DISABLE_ICON_RESOURCE_NAME = "disabledCalendarIcon.png";
    public static final String CURRENT_DATE_INPUT = "InputCurrentDate";
    protected static final Map<String, ComponentAttribute> CALENDAR_INPUT_HANDLER_ATTRIBUTES = Collections
        .unmodifiableMap(ComponentAttribute.createMap(
            new ComponentAttribute(HtmlConstants.ONCLICK_ATTRIBUTE).setEventNames("inputclick").setComponentAttributeName(
                "oninputclick"),
            new ComponentAttribute(HtmlConstants.ONDBLCLICK_ATTRIBUTE).setEventNames("inputdblclick")
                .setComponentAttributeName("oninputdblclick"),
            new ComponentAttribute(HtmlConstants.ONMOUSEDOWN_ATTRIBUTE).setEventNames("inputmousedown")
                .setComponentAttributeName("oninputmousedown"),
            new ComponentAttribute(HtmlConstants.ONMOUSEUP_ATTRIBUTE).setEventNames("inputmouseup").setComponentAttributeName(
                "oninputmouseup"),
            new ComponentAttribute(HtmlConstants.ONMOUSEOVER_ATTRIBUTE).setEventNames("inputmouseover")
                .setComponentAttributeName("oninputmouseover"),
            new ComponentAttribute(HtmlConstants.ONMOUSEMOVE_ATTRIBUTE).setEventNames("inputmousemove")
                .setComponentAttributeName("oninputmousemove"),
            new ComponentAttribute(HtmlConstants.ONMOUSEOUT_ATTRIBUTE).setEventNames("inputmouseout")
                .setComponentAttributeName("oninputmouseout"),
            new ComponentAttribute(HtmlConstants.ONKEYPRESS_ATTRIBUTE).setEventNames("inputkeypress")
                .setComponentAttributeName("oninputkeypress"),
            new ComponentAttribute(HtmlConstants.ONKEYDOWN_ATTRIBUTE).setEventNames("inputkeydown").setComponentAttributeName(
                "oninputkeydown"),
            new ComponentAttribute(HtmlConstants.ONKEYUP_ATTRIBUTE).setEventNames("inputkeyup").setComponentAttributeName(
                "oninputkeyup"),
            new ComponentAttribute(HtmlConstants.ONBLUR_ATTRIBUTE).setEventNames("inputblur").setComponentAttributeName(
                "oninputblur"),
            new ComponentAttribute(HtmlConstants.ONFOCUS_ATTRIBUTE).setEventNames("inputfocus").setComponentAttributeName(
                "oninputfocus"),
            new ComponentAttribute(HtmlConstants.ONCHANGE_ATTRIBUTE).setEventNames("inputchange").setComponentAttributeName(
                "oninputchange"),
            new ComponentAttribute(HtmlConstants.ONSELECT_ATTRIBUTE).setEventNames("inputselect").setComponentAttributeName(
                "oninputselect")));
    private static final String HOURS_VALUE = "hours";
    private static final String MINUTES_VALUE = "minutes";
    private static final String SECONDS_VALUE = "seconds";
    protected final ConverterLookupStrategy calendarConverterLookupStrategy = new ConverterLookupStrategy() {
        public Converter getConverterByValue(FacesContext context, UIComponent component, Object value)
            throws ConverterException {
            AbstractCalendar calendar = (AbstractCalendar) component;
            Converter converter = calendar.getConverter();

            if (converter == null && value != null) {
                converter = InputUtils.getConverterForType(context, value.getClass());
            }

            // in case the converter hasn't been set, try to use default
            // DateTimeConverter
            if (converter == null) {
                converter = createDefaultConverter(context);
            }

            setupConverter(context, converter, (AbstractCalendar) component);
            return converter;
        }

        public Converter getConverterByProperty(FacesContext context, UIComponent component) throws ConverterException {
            AbstractCalendar calendar = (AbstractCalendar) component;
            Converter converter = InputUtils.findConverter(context, calendar, "value");

            // in case the converter hasn't been set, try to use default
            // DateTimeConverter
            if (converter == null) {
                converter = createDefaultConverter(context);
            }

            setupConverter(context, converter, calendar);
            return converter;
        }
    };

    protected void doDecode(FacesContext context, UIComponent component) {
        if (!(component instanceof AbstractCalendar)) {
            return;
        }

        AbstractCalendar calendar = (AbstractCalendar) component;
        if (calendar.isDisabled()) {
            return;
        }

        Map<String, String> requestParameterMap = context.getExternalContext().getRequestParameterMap();

        String clientId = calendar.getClientId(context);
        String currentDateString = (String) requestParameterMap.get(clientId + CURRENT_DATE_INPUT);
        if (currentDateString != null) {
            calendar.queueEvent(new CurrentDateChangeEvent(calendar, currentDateString));
        }

        String selectedDateString = requestParameterMap.get(clientId + "InputDate");
        if (selectedDateString != null) {
            calendar.setSubmittedValue(selectedDateString);
        }

        if (requestParameterMap.get(component.getClientId(context) + ".ajax") != null) {
            PartialViewContext pvc = context.getPartialViewContext();
            pvc.getRenderIds().add(
                component.getClientId(context) + MetaComponentResolver.META_COMPONENT_SEPARATOR_CHAR
                    + AbstractCalendar.DAYSDATA_META_COMPONENT_ID);

            context.renderResponse();
        }
    }

    public void renderInputHandlers(FacesContext facesContext, UIComponent component) throws IOException {
        RenderKitUtils.renderPassThroughAttributesOptimized(facesContext, component, CALENDAR_INPUT_HANDLER_ATTRIBUTES);
    }

    @Override
    public Object getConvertedValue(FacesContext facesContext, UIComponent component, Object submittedValue)
        throws ConverterException {
        if ((facesContext == null) || (component == null)) {
            throw new NullPointerException();
        }

        // skip conversion of already converted date
        if (submittedValue instanceof Date) {
            return submittedValue;
        }

        return InputUtils.getConvertedValue(facesContext, component, calendarConverterLookupStrategy, submittedValue);
    }

    @Override
    public String getInputValue(FacesContext facesContext, UIComponent component) {
        if (!(component instanceof AbstractCalendar)) {
            return null;
        }

        return InputUtils.getInputValue(facesContext, component, calendarConverterLookupStrategy);
    }

    public String getButtonIcon(FacesContext facesContext, UIComponent component) {
        boolean disable = (Boolean) component.getAttributes().get(OPTION_DISABLED);
        String buttonIcon = (String) component.getAttributes().get("buttonIcon");
        if (disable) {
            buttonIcon = (String) component.getAttributes().get("buttonDisabledIcon");
        }

        if (buttonIcon != null && buttonIcon.trim().length() != 0) {
            buttonIcon = RenderKitUtils.getResourceURL(buttonIcon, facesContext);
        } else {
            buttonIcon = disable ? CALENDAR_DISABLE_ICON_RESOURCE_NAME : CALENDAR_ICON_RESOURCE_NAME;
            buttonIcon = RenderKitUtils.getResourcePath(facesContext, "org.richfaces.images", buttonIcon);
        }
        return buttonIcon;
    }

    public Object getSelectedDate(FacesContext facesContext, UIComponent component) throws IOException {
        Object returnValue = null;
        AbstractCalendar calendar = (AbstractCalendar) component;
        if (calendar.isValid()) {
            Date date;
            Object value = calendar.getValue();
            date = CalendarHelper.getAsDate(facesContext, calendar, value);
            if (date != null) {
                returnValue = formatSelectedDate(CalendarHelper.getTimeZoneOrDefault(calendar), date);
            }
        }
        return returnValue;
    }

    public static Object formatSelectedDate(TimeZone timeZone, Date date) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        AbstractCalendar calendarInstance = (AbstractCalendar) AbstractCalendar.getCurrentComponent(facesContext);
        Calendar calendar = CalendarHelper.getCalendar(facesContext, calendarInstance);

        calendar.setTimeZone(timeZone);
        calendar.setTime(date);

        JSFunction result = new JSFunction("new Date");
        result.addParameter(calendar.get(Calendar.YEAR));
        result.addParameter(calendar.get(Calendar.MONTH));
        result.addParameter(calendar.get(Calendar.DATE));
        result.addParameter(calendar.get(Calendar.HOUR_OF_DAY));
        result.addParameter(calendar.get(Calendar.MINUTE));
        result.addParameter(0);

        return result;
    }

    public Object getCurrentDate(FacesContext facesContext, UIComponent component) throws IOException {
        AbstractCalendar calendar = (AbstractCalendar) component;
        Date date = CalendarHelper.getCurrentDateOrDefault(facesContext, calendar);
        return formatDate(date);
    }

    public String getCurrentDateAsString(FacesContext facesContext, UIComponent component) throws IOException {
        AbstractCalendar calendar = (AbstractCalendar) component;
        Format formatter = new SimpleDateFormat("MM/yyyy");

        Date currentDate = CalendarHelper.getCurrentDateOrDefault(facesContext, calendar);
        return formatter.format(currentDate);
    }

    public static Object formatDate(Date date) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        AbstractCalendar calendarInstance = (AbstractCalendar) AbstractCalendar.getCurrentComponent(facesContext);
        Calendar calendar = CalendarHelper.getCalendar(facesContext, calendarInstance);

        calendar.setTime(date);
        JSFunction result = new JSFunction("new Date");
        result.addParameter(calendar.get(Calendar.YEAR));
        result.addParameter(calendar.get(Calendar.MONTH));
        result.addParameter(calendar.get(Calendar.DATE));

        return result;
    }

    public String getDayCellClass(FacesContext facesContext, UIComponent component) {
        // TODO: refactor this
        /*
         * String cellwidth = (String) component.getAttributes().get("cellWidth"); String cellheight = (String)
         * component.getAttributes().get("cellHeight"); if (cellwidth != null && cellwidth.length() != 0 || cellheight != null
         * && cellheight.length() != 0) { String clientId = component.getClientId(context); String value = clientId.replace(':',
         * '_') + "DayCell"; return value; }
         */
        return null;
    }

    public JSReference getDayEnabled(FacesContext facesContext, UIComponent component) {
        AbstractCalendar calendar = (AbstractCalendar) component;
        String dayEnabled = calendar.getDayDisableFunction();
        return ((dayEnabled != null && dayEnabled.trim().length() != 0)) ? new JSReference(dayEnabled) : null;
    }

    public JSReference getDayStyleClass(FacesContext context, UIComponent component) {
        AbstractCalendar calendar = (AbstractCalendar) component;
        String dayStyleClass = calendar.getDayClassFunction();
        return ((dayStyleClass != null && dayStyleClass.trim().length() != 0)) ? new JSReference(dayStyleClass) : null;
    }

    public Map<String, Object> getLabels(FacesContext facesContext, UIComponent component) {
        AbstractCalendar calendar = (AbstractCalendar) component;

        ResourceBundle bundle1 = null;
        ResourceBundle bundle2 = null;

        ClassLoader loader = Thread.currentThread().getContextClassLoader();

        String messageBundle = facesContext.getApplication().getMessageBundle();
        Locale locale = CalendarHelper.getAsLocale(facesContext, calendar);
        if (null != messageBundle) {
            bundle1 = ResourceBundle.getBundle(messageBundle, locale, loader);
        }

        try {
            bundle2 = ResourceBundle.getBundle(CALENDAR_BUNDLE, locale, loader);
        } catch (MissingResourceException e) {
            // No external bundle was found, ignore this exception.
        }

        ResourceBundle[] bundles = { bundle1, bundle2 };
        String[] names = { "apply", "today", "clean", "cancel", "ok", "close" };

        return getCollectedLabels(bundles, names);
    }

    protected Map<String, Object> getCollectedLabels(ResourceBundle[] bundles, String[] names) {
        Map<String, Object> labels = new HashMap<String, Object>();
        if (bundles != null && names != null) {
            for (String name : names) {
                String label = null;
                String bundleKey = "RICH_CALENDAR_" + name.toUpperCase() + "_LABEL";
                for (ResourceBundle bundle : bundles) {
                    if (bundle != null) {
                        try {
                            label = bundle.getString(bundleKey);
                        } catch (MissingResourceException mre) {
                            // Current key was not found, ignore this exception;
                        }
                    }
                    if (label != null) {
                        break;
                    }
                }
                RenderKitUtils.addToScriptHash(labels, name, label);
            }
        }
        return labels;
    }

    public Map<String, Object> getPreparedDefaultTime(FacesContext facesContext, UIComponent component) {
        AbstractCalendar abstractCalendar = (AbstractCalendar) component;

        Date date = CalendarHelper.getFormattedDefaultTime(abstractCalendar);
        Map<String, Object> result = new HashMap<String, Object>();
        if (date != null) {
            Calendar calendar = CalendarHelper.getCalendar(null, null);
            calendar.setTime(date);
            int hours = calendar.get(Calendar.HOUR_OF_DAY);
            int minutes = calendar.get(Calendar.MINUTE);
            int seconds = calendar.get(Calendar.SECOND);

            if (hours != 12 || minutes != 0 || seconds != 0) {
                result.put(HOURS_VALUE, hours);
                result.put(MINUTES_VALUE, minutes);
                result.put(SECONDS_VALUE, seconds);
            }
        }
        if (result.size() > 0) {
            return result;
        } else {
            return null;
        }
    }

    private static String[] shiftDates(int minimum, int maximum, String[] labels) {
        if (minimum == 0 && (maximum - minimum == labels.length - 1)) {
            return labels;
        }

        String[] shiftedLabels = new String[maximum - minimum + 1];
        System.arraycopy(labels, minimum, shiftedLabels, 0, maximum - minimum + 1);

        return shiftedLabels;
    }

    protected Map<String, Object> getLocaleOptions(FacesContext facesContext, UIComponent component) {
        AbstractCalendar calendarComponent = (AbstractCalendar) component;

        Map<String, Object> map = new HashMap<String, Object>();

        Locale locale = CalendarHelper.getAsLocale(facesContext, calendarComponent);
        DateFormatSymbols dateFormat = new DateFormatSymbols(locale);

        Calendar calendar = CalendarHelper.getCalendar(facesContext, calendarComponent);
        int maximum = calendar.getActualMaximum(Calendar.DAY_OF_WEEK);
        int minimum = calendar.getActualMinimum(Calendar.DAY_OF_WEEK);

        int monthMax = calendar.getActualMaximum(Calendar.MONTH);
        int monthMin = calendar.getActualMinimum(Calendar.MONTH);

        String[] weekDayLabelsShort = RenderKitUtils.asArray(calendarComponent.getWeekDayLabelsShort());
        if (isEmptyArray(weekDayLabelsShort)) {
            weekDayLabelsShort = dateFormat.getShortWeekdays();
            weekDayLabelsShort = shiftDates(minimum, maximum, weekDayLabelsShort);
        }
        RenderKitUtils.addToScriptHash(map, WEEK_DAY_LABELS_SHORT, weekDayLabelsShort);

        String[] monthLabels = RenderKitUtils.asArray(calendarComponent.getMonthLabels());
        if (isEmptyArray(monthLabels)) {
            monthLabels = dateFormat.getMonths();
            monthLabels = shiftDates(monthMin, monthMax, monthLabels);
        }
        RenderKitUtils.addToScriptHash(map, MONTH_LABELS, monthLabels);

        String[] monthLabelsShort = RenderKitUtils.asArray(calendarComponent.getMonthLabelsShort());
        if (isEmptyArray(monthLabelsShort)) {
            monthLabelsShort = dateFormat.getShortMonths();
            monthLabelsShort = shiftDates(monthMin, monthMax, monthLabelsShort);
        }
        RenderKitUtils.addToScriptHash(map, MONTH_LABELS_SHORT, monthLabelsShort);

        int minDaysInFirstWeek = calendarComponent.getMinDaysInFirstWeek();

        if (1 > minDaysInFirstWeek || minDaysInFirstWeek > 7) {
            minDaysInFirstWeek = calendar.getMinimalDaysInFirstWeek();
        }

        if (0 <= minDaysInFirstWeek && minDaysInFirstWeek <= 7) {
            RenderKitUtils.addToScriptHash(map, MIN_DAYS_IN_FIRST_WEEK, minDaysInFirstWeek);
        }

        int day = calendarComponent.getFirstWeekDay();
        if (day < 0 || 6 < day) {
            day = calendar.getFirstDayOfWeek() - calendar.getActualMinimum(Calendar.DAY_OF_WEEK);
        }

        if (0 <= day && day <= 6) {
            RenderKitUtils.addToScriptHash(map, FIRST_DAY_WEEK, day);
        } else {
            throw new IllegalArgumentException(day + " value of firstWeekDay attribute is not a legal one for component: "
                + MessageUtil.getLabel(facesContext, calendarComponent));
        }
        return map;
    }

    private boolean isEmptyArray(String[] array) {
        if (array != null) {
            for (String str : array) {
                if (str.trim().length() > 0) {
                    return false;
                }
            }
        }
        return true;
    }

    public String getPopupStyle(FacesContext facesContext, UIComponent component) {
        AbstractCalendar calendar = (AbstractCalendar) component;
        int zindex = calendar.getZindex();
        if (zindex < 0) {
            zindex = 3;
        }

        return HtmlUtil.concatStyles("z-index: " + zindex, calendar.getPopupStyle());
    }

    public Locale getAsLocale(FacesContext facesContext, UIComponent component) {
        return CalendarHelper.getAsLocale(facesContext, component);
    }

    public String writePreloadBody(FacesContext context, UIComponent component) throws IOException {
        AbstractCalendar calendar = (AbstractCalendar) component;
        Object preload = calendar.getPreload();
        if (preload != null) {
            return RenderKitUtils.toScriptArgs(preload);
        } else {
            return null;
        }
    }

    public boolean isUseIcons(FacesContext facesContext, UIComponent component) {
        Object label = component.getAttributes().get("buttonLabel");
        return (label == null || ((String) label).trim().length() == 0);
    }

    protected Converter createDefaultConverter(FacesContext facesContext) {
        if (facesContext == null) {
            return null;
        }
        return facesContext.getApplication().createConverter(DateTimeConverter.CONVERTER_ID);
    }

    protected Converter setupConverter(FacesContext facesContext, Converter converter, AbstractCalendar calendar) {
        if (converter == null || calendar == null) {
            return null;
        }

        if (converter instanceof DateTimeConverter) {
            DateTimeConverter defaultConverter = (DateTimeConverter) converter;
            defaultConverter.setPattern(CalendarHelper.getDatePatternOrDefault(calendar));
            defaultConverter.setLocale(CalendarHelper.getAsLocale(facesContext, calendar));
            defaultConverter.setTimeZone(CalendarHelper.getTimeZoneOrDefault(calendar));
        }
        return converter;
    }

    public void encodeMetaComponent(FacesContext context, UIComponent component, String metaComponentId) throws IOException {
        if (AbstractCalendar.DAYSDATA_META_COMPONENT_ID.equals(metaComponentId)) {
            Object preload = ((AbstractCalendar) component).getPreload();
            Map<String, Object> dataMap = ExtendedPartialViewContext.getInstance(context).getResponseComponentDataMap();
            dataMap.put(component.getClientId(context), preload);
        } else {
            throw new IllegalArgumentException(metaComponentId);
        }
    }

    public void decodeMetaComponent(FacesContext context, UIComponent component, String metaComponentId) {
        throw new UnsupportedOperationException();
    }

    protected String getTodayControlModeOrDefault(UIComponent component) {
        String value = "";
        if (component instanceof AbstractCalendar) {
            value = ((AbstractCalendar) component).getTodayControlMode();
            if (value == null || value.length() == 0) {
                value = "select";
            }
        }
        return value;
    }

    protected String getJointPoint(UIComponent component) {
        if (component instanceof AbstractCalendar) {
            Positioning jointPoint = ((AbstractCalendar) component).getJointPoint();
            if (jointPoint != null) {
                return jointPoint.getValue();
            }
        }
        // return null to use default value on client
        return null;
    }

    protected String getDirection(UIComponent component) {
        if (component instanceof AbstractCalendar) {
            Positioning direction = ((AbstractCalendar) component).getDirection();
            if (direction != null) {
                return direction.getValue();
            }
        }
        // return null to use default value on client
        return null;
    }

    protected String getBoundaryDatesModeOrDefault(UIComponent component) {
        String value = "";
        if (component instanceof AbstractCalendar) {
            value = ((AbstractCalendar) component).getBoundaryDatesMode();
            if (value == null || value.length() == 0) {
                value = "inactive";
            }
        }
        return value;
    }

    protected AbstractCalendar.Mode getModeOrDefault(UIComponent component) {
        AbstractCalendar.Mode value = ((AbstractCalendar) component).getMode();
        if (value == null) {
            value = AbstractCalendar.Mode.client;
        }
        return value;
    }
}
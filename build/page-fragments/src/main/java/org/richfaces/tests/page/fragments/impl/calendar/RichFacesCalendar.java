/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2013, Red Hat, Inc. and individual contributors
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
 *******************************************************************************/
package org.richfaces.tests.page.fragments.impl.calendar;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.findby.ByJQuery;
import org.jboss.arquillian.graphene.fragment.Root;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.openqa.selenium.WebElement;
import org.richfaces.tests.page.fragments.configuration.RichFacesPageFragmentsConfiguration;
import org.richfaces.tests.page.fragments.configuration.RichFacesPageFragmentsConfigurationContext;
import org.richfaces.tests.page.fragments.impl.Utils;
import org.richfaces.tests.page.fragments.impl.common.AdvancedInteractions;
import org.richfaces.tests.page.fragments.impl.common.TextInputComponent;

import com.google.common.base.Optional;

/**
 *
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class RichFacesCalendar implements Calendar, AdvancedInteractions<RichFacesCalendar.AdvancedCalendarInteractions> {

    @Root
    private WebElement root;

    private final AdvancedCalendarInteractions interactions = new AdvancedCalendarInteractions();
    private final RichFacesPageFragmentsConfiguration configuration = RichFacesPageFragmentsConfigurationContext
        .getProxy();

    private Calendar strategy = configuration.isUseJSInteractionStrategy() ? new CalendarJavaScriptStrategy() : new CalendarInteractiveStrategy();

    @Override
    public AdvancedCalendarInteractions advanced() {
        return interactions;
    }

    @Override
    public DateTime getDate() {
        return strategy.getDate();
    }

    @Override
    public void setDate(DateTime d) {
        strategy.setDate(d);
    }

    /**
     * Slower strategy for setting date using fragments advanced API.
     */
    private class CalendarInteractiveStrategy implements Calendar {

        @Override
        public DateTime getDate() {
            TextInputComponent input;
            if (advanced().isPopup()) {
                input = advanced().getPopupCalendar().getInput();
            } else {
                input = advanced().getInlineCalendar().getInput();
            }
            return advanced().getDatePattern().parseDateTime(input.getStringValue());
        }

        @Override
        public void setDate(DateTime d) {
            if (advanced().isPopup()) {
                advanced().getPopupCalendar().setDateTime(d);
            } else {
                advanced().getInlineCalendar().setDateTime(d);
            }
        }
    }

    /**
     * Fastest strategy. Sets/gets value by JS API.
     */
    private class CalendarJavaScriptStrategy implements Calendar {

        private final DateTimeFormatter dateTimeFormatForJS = DateTimeFormat.forPattern("MMMM d, yyyy HH:mm:ss");

        @Override
        public DateTime getDate() {
            return DateTime.parse(Utils.invokeRichFacesJSAPIFunction(root, "getValueAsString()"), advanced().getDatePattern());
        }

        @Override
        public void setDate(DateTime d) {
            Utils.invokeRichFacesJSAPIFunction(root, "setValue(new Date('" + d.toString(dateTimeFormatForJS) + "'))");
        }
    }

    public class AdvancedCalendarInteractions {

        private static final String DATE_PATTERN_DEFAULT = "MMM d , yyyy";
        private String datePattern;

        private DateTimeFormatter getDatePattern() {
            return DateTimeFormat.forPattern(Optional.fromNullable(datePattern).or(DATE_PATTERN_DEFAULT));
        }

        public RichFacesAdvancedInlineCalendar getInlineCalendar() {
            if (!isPopup()) {
                return Graphene.createPageFragment(RichFacesAdvancedInlineCalendar.class, root);
            }
            throw new RuntimeException("This is a popup calendar. Cannot get its inline version.");
        }

        public RichFacesAdvancedPopupCalendar getPopupCalendar() {
            if (isPopup()) {
                return Graphene.createPageFragment(RichFacesAdvancedPopupCalendar.class, root);
            }
            throw new RuntimeException("This is an inline calendar. Cannot get its popup version.");
        }

        private boolean isPopup() {
            return Utils.isVisible(root, ByJQuery.selector("span[id$='Popup']"));
        }

        public void setupDatePattern(String datePattern) {
            this.datePattern = datePattern;
        }

        /**
         * Setups the interactive strategy for setting of date. The getting of date uses JS API of component. The strategy uses
         * advanced API of this fragment.
         */
        public void setupInteractiveStrategy() {
            strategy = new CalendarInteractiveStrategy();
        }

        /**
         * Setups the much faster strategy for setting and getting of date. Default value. The strategy uses JS API of the
         * component.
         */
        public void setupJavaScriptStrategy() {
            strategy = new CalendarJavaScriptStrategy();
        }
    }
}

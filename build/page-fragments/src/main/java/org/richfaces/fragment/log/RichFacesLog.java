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
package org.richfaces.fragment.log;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.GrapheneElement;
import org.jboss.arquillian.graphene.fragment.Root;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;
import org.richfaces.fragment.common.AdvancedVisibleComponentIteractions;
import org.richfaces.fragment.common.Utils;
import org.richfaces.fragment.common.VisibleComponentInteractions;
import org.richfaces.fragment.list.AbstractListComponent;
import org.richfaces.fragment.list.ListComponent;
import org.richfaces.fragment.list.RichFacesListItem;

import com.google.common.base.Predicate;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 */
public class RichFacesLog implements Log, AdvancedVisibleComponentIteractions<RichFacesLog.AdvancedLogInteractions> {

    @Root
    private GrapheneElement root;

    @FindBy(css = "div.rf-log-contents")
    private RichFacesLogEntries logEntries;

    @FindBy(tagName = "button")
    private GrapheneElement clearButton;
    @FindBy(tagName = "select")
    private Select levelSelect;

    private final AdvancedLogInteractions interactions = new AdvancedLogInteractions();

    @Override
    public AdvancedLogInteractions advanced() {
        return interactions;
    }

    @Override
    public void clear() {
        advanced().getClearButtonElement().click();
        Graphene.waitGui().until(new Predicate<WebDriver>() {

            @Override
            public boolean apply(WebDriver input) {
                return getLogEntries().isEmpty();
            }
        });
    }

    @Override
    public void changeLevel(LogEntryLevel level) {
        advanced().getLevelSelectElement().selectByVisibleText(level.toString().toLowerCase());
    }

    @Override
    public ListComponent<? extends LogEntry> getLogEntries() {
        return logEntries;
    }

    public static class RichFacesLogEntries extends AbstractListComponent<RichFacesLogEntry> {
    }

    public static class RichFacesLogEntry extends RichFacesListItem implements LogEntry {

        @FindBy(css = "span.rf-log-entry-lbl")
        private WebElement labelElement;
        @FindBy(css = "span.rf-log-entry-msg")
        private WebElement messageElement;

        @Override
        public String getContent() {
            return getMessageElement().getText();
        }

        @Override
        public LogEntryLevel getLevel() {
            return RichFacesLogEntryLevel.getLevelFromLabel(getLabelElement());
        }

        @Override
        public DateTime getTimeStamp() {
            DateTime dt = null;
            String text = getLabelElement().getText();
            String timeStamp = text.substring(text.indexOf('[') + 1, text.indexOf(']'));
            DateTimeFormatter formatter = DateTimeFormat.forPattern("HH:m:s.S");
            try {
                dt = formatter.parseDateTime(timeStamp);
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Something went wrong with parsing of log entry timestamp!", e);
            }
            return dt;
        }

        /**
         * @return the labelElement
         */
        protected WebElement getLabelElement() {
            return labelElement;
        }

        /**
         * @return the messageElement
         */
        protected WebElement getMessageElement() {
            return messageElement;
        }
    }

    private static enum RichFacesLogEntryLevel {

        DEBUG(LogEntryLevel.DEBUG, "rf-log-entry-lbl-debug"),
        INFO(LogEntryLevel.INFO, "rf-log-entry-lbl-info"),
        WARN(LogEntryLevel.WARN, "rf-log-entry-lbl-warn"),
        ERROR(LogEntryLevel.ERROR, "rf-log-entry-lbl-error");

        private final LogEntryLevel level;
        private final String containsClass;

        private RichFacesLogEntryLevel(LogEntryLevel level, String containsClass) {
            this.level = level;
            this.containsClass = containsClass;
        }

        private static LogEntryLevel getLevelFromLabel(WebElement label) {
            String styleClasses = label.getAttribute("class");
            for (RichFacesLogEntryLevel logEntryLevel : values()) {
                if (styleClasses.contains(logEntryLevel.containsClass)) {
                    return logEntryLevel.level;
                }
            }
            throw new RuntimeException("Cannot obtain level from label: " + label);
        }
    }

    public class AdvancedLogInteractions implements VisibleComponentInteractions {

        public GrapheneElement getRootElement() {
            return root;
        }

        public GrapheneElement getClearButtonElement() {
            return clearButton;
        }

        @Override
        public boolean isVisible() {
            return Utils.isVisible(getRootElement());
        }

        protected Select getLevelSelectElement() {
            return levelSelect;
        }
    }
}

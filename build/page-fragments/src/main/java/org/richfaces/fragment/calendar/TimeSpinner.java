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

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.fragment.Root;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.calendar.TimeEditor.SetValueBy;
import org.richfaces.fragment.common.TextInputComponentImpl;
import org.richfaces.fragment.common.Utils;

/**
 * Abstract class for spinner component.
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 * @param <T> type of value
 */
public abstract class TimeSpinner<T> {

    @Root
    private WebElement root;

    @FindBy(css = "input.rf-cal-sp-inp")
    private TextInputComponentImpl input;
    @FindBy(className = "rf-cal-sp-up")
    private WebElement buttonUpElement;
    @FindBy(className = "rf-cal-sp-down")
    private WebElement buttonDownElement;

    public WebElement getButtonDownElement() {
        return buttonDownElement;
    }

    public WebElement getButtonUpElement() {
        return buttonUpElement;
    }

    public TextInputComponentImpl getInput() {
        return input;
    }

    /**
     * Returns value set in this spinner
     * @return
     */
    public abstract T getValue();

    protected boolean isSameValueAreadySet(T value) {
        return getValue().equals(value);
    }

    public boolean isVisible() {
        return Utils.isVisible(getRootElement());
    }

    public void setValueBy(T value, SetValueBy by) {
        switch (by) {
            case BUTTONS:
                setValueByButtons(value);
                break;
            case TYPING:
                setValueByTyping(value);
                break;
            default:
                throw new RuntimeException("Unknown switch.");
        }
    }

    /**
     * Sets spinner's value by clicking on the buttons
     * @param value value to be set
     */
    public abstract void setValueByButtons(T value);

    /**
     * Sets spinner value by direct input writing
     * @param value value to be set
     */
    public void setValueByTyping(T value) {
        if (!isSameValueAreadySet(value)) {
            getInput().clear().sendKeys(value.toString());
        }
    }

    public void waitUntilIsVisible() {
        Graphene.waitModel().withMessage("Waiting for time spinner to be visible.").until().element(getRootElement()).is().visible();
    }

    /**
     * @return the root
     */
    protected WebElement getRootElement() {
        return root;
    }

    public static class TimeSpinner60 extends TimeSpinner<Integer> {

        private final int maxValue;

        public TimeSpinner60() {
            this(60);
        }

        public TimeSpinner60(int maxValue) {
            this.maxValue = maxValue;
        }

        @Override
        public Integer getValue() {
            return getInput().getIntValue();
        }

        @Override
        public void setValueByButtons(Integer value) {
            if (!isSameValueAreadySet(value)) {
                int actual = getValue();
                int difference = actual - value;
                int optimizedDifference = (difference < 0 ? maxValue + difference : difference - maxValue);
                optimizedDifference = (Math.abs(optimizedDifference) > Math.abs(difference) ? difference : optimizedDifference);
                if (optimizedDifference < 0) {
                    clickUp(optimizedDifference);
                } else if (optimizedDifference > 0) {
                    clickDown(optimizedDifference);
                }
            }
        }

        private void clickUp(int times) {
            for (int i = 0; i > times; i--) {
                getButtonUpElement().click();
            }
        }

        private void clickDown(int times) {
            for (int i = 0; i < times; i++) {
                getButtonDownElement().click();
            }
        }
    }

    public static class TimeSpinner12 extends TimeSpinner60 {

        public TimeSpinner12() {
            super(12);
        }
    }

    public static class TimeSpinner24 extends TimeSpinner60 {

        public TimeSpinner24() {
            super(24);
        }
    }

    public static class TimeSignSpinner extends TimeSpinner<TimeSign> {

        @Override
        public TimeSign getValue() {
            return TimeSign.valueOf(getInput().getStringValue().toUpperCase());
        }

        @Override
        public void setValueByButtons(TimeSign value) {
            if (!isSameValueAreadySet(value)) {
                getButtonDownElement().click();
                if (!getValue().equals(value)) {
                    throw new RuntimeException("The time sign spinner should contain only 2 values: am, pm");
                }
            }
        }

    }

    public static enum TimeSign {

        AM,
        PM;
    }
}

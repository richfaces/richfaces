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
package org.richfaces.fragment.autocomplete;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.fragment.Root;
import org.jboss.arquillian.graphene.wait.FluentWait;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.common.Actions;
import org.richfaces.fragment.common.AdvancedVisibleComponentIteractions;
import org.richfaces.fragment.common.ClearType;
import org.richfaces.fragment.common.ScrollingType;
import org.richfaces.fragment.common.TextInputComponentImpl;
import org.richfaces.fragment.common.Utils;
import org.richfaces.fragment.common.VisibleComponentInteractions;
import org.richfaces.fragment.common.WaitingWrapper;
import org.richfaces.fragment.common.WaitingWrapperImpl;
import org.richfaces.fragment.common.picker.ChoicePicker;
import org.richfaces.fragment.common.picker.ChoicePickerHelper;

import com.google.common.base.Predicate;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class RichFacesAutocomplete implements Autocomplete, AdvancedVisibleComponentIteractions<RichFacesAutocomplete.AdvancedAutocompleteInteractions> {

    @Drone
    private WebDriver driver;

    @Root
    private WebElement root;

    @FindBy(css = "input[type='text']")
    private TextInputComponentImpl input;

    private final AdvancedAutocompleteInteractions advancedInteractions = new AdvancedAutocompleteInteractions();

    public AdvancedAutocompleteInteractions advanced() {
        return advancedInteractions;
    }

    @Override
    public void clear() {
        advanced().clear(ClearType.DEFAULT_CLEAR_TYPE);
    }

    @Override
    public SelectOrConfirm type(String str) {
        if (!advanced().getInput().getStringValue().isEmpty()) {
            advanced().getInput().sendKeys(advanced().getToken() + " ");
        }
        advanced().getInput().sendKeys(str);
        return new SelectOrConfirmImpl();
    }

    public class AdvancedAutocompleteInteractions implements VisibleComponentInteractions {

        private static final String SUGGESTIONS_CSS_SELECTOR_TEMPLATE = ".rf-au-lst-cord[id='%sList'] .rf-au-itm";
        private static final String DEFAULT_TOKEN = ",";
        private static final String SELECT_FIRST_ATT_NAME = "selectFirst";

        private final ScrollingType DEFAULT_SCROLLING_TYPE = ScrollingType.BY_MOUSE;
        private ScrollingType scrollingType = DEFAULT_SCROLLING_TYPE;
        private String token = DEFAULT_TOKEN;

        private long _timeoutForSuggestionsToBeNotVisible = -1;
        private long _timeoutForSuggestionsToBeVisible = -1;

        /**
         * Clears the value of autocomplete's input field.
         *
         * @param clearType defines how the input should be cleared, e.g. by using backspace key, by delete key, by JavaScript,
         *        etc.
         * @return input component
         */
        public TextInputComponentImpl clear(ClearType clearType) {
            return advanced().getInput().advanced().clear(clearType);
        }

        public TextInputComponentImpl getInput() {
            return input;
        }

        public WebElement getRootElement() {
            return root;
        }

        protected ScrollingType getScrollingType() {
            return scrollingType;
        }

        protected String getSuggestionsSelectorTemplate() {
            return SUGGESTIONS_CSS_SELECTOR_TEMPLATE;
        }

        public List<WebElement> getSuggestionsElements() {
            String id = getRootElement().getAttribute("id");
            String selectorOfRoot = String.format(getSuggestionsSelectorTemplate(), id);
            List<WebElement> foundElements = driver.findElements(By.cssSelector(selectorOfRoot));
            if (!foundElements.isEmpty() && foundElements.get(0).isDisplayed()) {
                // prevent returning of not visible elements
                return Collections.unmodifiableList(foundElements);
            } else {
                return Collections.emptyList();
            }
        }

        public String getToken() {
            return token;
        }

        public void setToken() {
            token = DEFAULT_TOKEN;
        }

        public void setToken(String value) {
            token = value;
        }

        public void setScrollingType() {
            scrollingType = DEFAULT_SCROLLING_TYPE;
        }

        public void setScrollingType(ScrollingType type) {
            scrollingType = type;
        }

        public WaitingWrapper waitForSuggestionsToBeNotVisible() {
            return new WaitingWrapperImpl() {

                @Override
                protected void performWait(FluentWait<WebDriver, Void> wait) {
                    wait.until(new Predicate<WebDriver>() {
                        @Override
                        public boolean apply(WebDriver input) {
                            return getSuggestionsElements().isEmpty();
                        }
                    });
                }
            }.withMessage("Waiting for suggestions to be not visible")
                .withTimeout(getTimeoutForSuggestionsToBeNotVisible(), TimeUnit.MILLISECONDS);
        }

        public WaitingWrapper waitForSuggestionsToBeVisible() {
            return new WaitingWrapperImpl() {

                @Override
                protected void performWait(FluentWait<WebDriver, Void> wait) {
                    wait.until(new Predicate<WebDriver>() {
                        @Override
                        public boolean apply(WebDriver input) {
                            return !getSuggestionsElements().isEmpty();
                        }
                    });
                }
            }.withMessage("Waiting for suggestions to be visible")
                .withTimeout(getTimeoutForSuggestionsToBeVisible(), TimeUnit.MILLISECONDS);
        }

        public void setTimeoutForSuggestionsToBeNotVisible(long timeoutInMilliseconds) {
            _timeoutForSuggestionsToBeNotVisible = timeoutInMilliseconds;
        }

        public void setTimeoutForSuggestionsToBeVisible(long timeoutInMilliseconds) {
            _timeoutForSuggestionsToBeVisible = timeoutInMilliseconds;
        }

        public long getTimeoutForSuggestionsToBeNotVisible() {
            return (_timeoutForSuggestionsToBeNotVisible == -1L) ? Utils.getWaitAjaxDefaultTimeout(driver) : _timeoutForSuggestionsToBeNotVisible;
        }

        public long getTimeoutForSuggestionsToBeVisible() {
            return (_timeoutForSuggestionsToBeVisible == -1L) ? Utils.getWaitAjaxDefaultTimeout(driver) : _timeoutForSuggestionsToBeVisible;
        }

        protected boolean isSelectFirst() {
            return Utils.<Boolean>getComponentOption(getRootElement(), SELECT_FIRST_ATT_NAME).or(Boolean.TRUE);
        }

        @Override
        public boolean isVisible() {
            return Utils.isVisible(getRootElement());
        }
    }

    public class SelectOrConfirmImpl implements SelectOrConfirm {

        @Override
        public Autocomplete confirm() {
            // in normal circumstances the confirmation could be done with `ENTER` key, but in WebDriver this causes the form
            // to be submitted with HTTP, so we need to workaround it
            boolean selectFirst = advanced().isSelectFirst();
            if (selectFirst && !advanced().getSuggestionsElements().isEmpty()) {
                select();// select the first item
            } else {
                // blur the input and focus on it again >>> the change event will be triggered, but we do not lose focus
                Utils.performUniversalBlur(driver);
                Graphene.waitModel().until().element(advanced().getInput().advanced().getInputElement()).is().present();
                advanced().getInput().advanced().getInputElement().click();
            }
            return RichFacesAutocomplete.this;
        }

        @Override
        public Autocomplete select() {
            return select(ChoicePickerHelper.byIndex().first());
        }

        @Override
        public Autocomplete select(ChoicePicker picker) {
            advanced().waitForSuggestionsToBeVisible().perform();
            WebElement foundValue = picker.pick(advanced().getSuggestionsElements());
            if (foundValue == null) {
                throw new RuntimeException("The value was not found by " + picker.toString());
            }

            if (advanced().getScrollingType() == ScrollingType.BY_KEYS) {
                selectWithKeys(foundValue);
            } else {
                new Actions(driver).moveToElement(foundValue).click(foundValue).perform();
            }

            advanced().waitForSuggestionsToBeNotVisible().perform();
            return RichFacesAutocomplete.this;
        }

        @Override
        public Autocomplete select(int index) {
            return select(ChoicePickerHelper.byIndex().index(index));
        }

        @Override
        public Autocomplete select(String match) {
            return select(ChoicePickerHelper.byVisibleText().match(match));
        }

        protected void selectWithKeys(WebElement foundValue) {
            // if selectFirst attribute of autocomplete is set, we don't have to press arrow down key for first item
            int steps = Utils.getIndexOfElement(foundValue) + (advanced().isSelectFirst() ? 0 : 1);
            Actions actions = new Actions(driver);
            for (int i = 0; i < steps; i++) {
                actions.sendKeys(Keys.ARROW_DOWN);
            }
            actions.sendKeys(foundValue, Keys.TAB).perform();
        }
    }
}

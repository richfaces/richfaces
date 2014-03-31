/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2014, Red Hat, Inc. and individual contributors
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
import org.richfaces.fragment.common.AdvancedInteractions;
import org.richfaces.fragment.common.ClearType;
import org.richfaces.fragment.common.ScrollingType;
import org.richfaces.fragment.common.TextInputComponentImpl;
import org.richfaces.fragment.common.Utils;
import org.richfaces.fragment.common.WaitingWrapper;
import org.richfaces.fragment.common.WaitingWrapperImpl;
import org.richfaces.fragment.common.picker.ChoicePicker;
import org.richfaces.fragment.common.picker.ChoicePickerHelper;

import com.google.common.base.Predicate;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class RichFacesAutocomplete implements Autocomplete, AdvancedInteractions<RichFacesAutocomplete.AdvancedAutocompleteInteractions> {

    private static final String SUGGESTIONS_CSS_SELECTOR_TEMPLATE = ".rf-au-lst-cord[id='%sList'] .rf-au-itm";
    private static final String CSS_INPUT = "input[type='text']";

    @Drone
    private WebDriver driver;

    @Root
    private WebElement root;

    @FindBy(css = CSS_INPUT)
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
        if (!input.getStringValue().isEmpty()) {
            input.sendKeys(advanced().getToken() + " ");
        }
        input.sendKeys(str);
        return new SelectOrConfirmImpl();
    }

    public class AdvancedAutocompleteInteractions {

        private static final String DEFAULT_TOKEN = ",";
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

        public List<WebElement> getSuggestionsElements() {
            String id = root.getAttribute("id");
            String selectorOfRoot = String.format(SUGGESTIONS_CSS_SELECTOR_TEMPLATE, id);
            List<WebElement> foundElements = driver.findElements(By.cssSelector(selectorOfRoot));
            if (!foundElements.isEmpty() && foundElements.get(0).isDisplayed()) { // prevent
                                                                                  // returning
                                                                                  // of
                                                                                  // not
                                                                                  // visible
                // elements
                return Collections.unmodifiableList(foundElements);
            } else {
                return Collections.emptyList();
            }
        }

        public String getToken() {
            return token;
        }

        public void setupToken() {
            token = DEFAULT_TOKEN;
        }

        public void setupToken(String value) {
            token = value;
        }

        public void setupScrollingType() {
            scrollingType = DEFAULT_SCROLLING_TYPE;
        }

        public void setupScrollingType(ScrollingType type) {
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

        public void setupTimeoutForSuggestionsToBeNotVisible(long timeoutInMilliseconds) {
            _timeoutForSuggestionsToBeNotVisible = timeoutInMilliseconds;
        }

        public void setupTimeoutForSuggestionsToBeVisible(long timeoutInMilliseconds) {
            _timeoutForSuggestionsToBeVisible = timeoutInMilliseconds;
        }

        public long getTimeoutForSuggestionsToBeNotVisible() {
            return (_timeoutForSuggestionsToBeNotVisible == -1L) ? Utils.getWaitAjaxDefaultTimeout(driver) : _timeoutForSuggestionsToBeNotVisible;
        }

        public long getTimeoutForSuggestionsToBeVisible() {
            return (_timeoutForSuggestionsToBeVisible == -1L) ? Utils.getWaitAjaxDefaultTimeout(driver) : _timeoutForSuggestionsToBeVisible;
        }
    }

    public class SelectOrConfirmImpl implements SelectOrConfirm {

        @Override
        public Autocomplete confirm() {
            // these two actions need to be split in order to prevent NoSuchElementException
            new Actions(driver).sendKeys(Keys.RETURN).perform();
            Graphene.waitModel().until().element(By.cssSelector("body")).is().present();
            new Actions(driver).click(driver.findElement(Utils.BY_BODY)).perform();
            advanced().waitForSuggestionsToBeNotVisible().perform();
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

        private void selectWithKeys(WebElement foundValue) {
            List<WebElement> suggestions = advanced().getSuggestionsElements();
            // if selectFirst attribute of autocomplete is set, we don't have to
            // press arrow down key for first item
            boolean skip = suggestions.get(0).getAttribute("class").contains("rf-au-itm-sel");
            int index = Utils.getIndexOfElement(foundValue);
            int steps = index + (skip ? 0 : 1);
            Actions actions = new Actions(driver);
            for (int i = 0; i < steps; i++) {
                actions.sendKeys(Keys.ARROW_DOWN);
            }
            actions.sendKeys(foundValue, Keys.RETURN).perform();
        }
    }
}

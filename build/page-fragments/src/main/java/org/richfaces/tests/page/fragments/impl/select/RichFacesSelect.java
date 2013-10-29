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
package org.richfaces.tests.page.fragments.impl.select;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.GrapheneElement;
import org.jboss.arquillian.graphene.findby.ByJQuery;
import org.jboss.arquillian.graphene.fragment.Root;
import org.jboss.arquillian.graphene.wait.FluentWait;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.page.fragments.impl.Utils;
import org.richfaces.tests.page.fragments.impl.common.AdvancedInteractions;
import org.richfaces.tests.page.fragments.impl.common.ClearType;
import org.richfaces.tests.page.fragments.impl.common.ScrollingType;
import org.richfaces.tests.page.fragments.impl.common.TextInputComponentImpl;
import org.richfaces.tests.page.fragments.impl.utils.Actions;
import org.richfaces.tests.page.fragments.impl.utils.WaitingWrapper;
import org.richfaces.tests.page.fragments.impl.utils.WaitingWrapperImpl;
import org.richfaces.tests.page.fragments.impl.utils.picker.ChoicePicker;
import org.richfaces.tests.page.fragments.impl.utils.picker.ChoicePickerHelper;

import com.google.common.base.Predicate;

public class RichFacesSelect implements Select, AdvancedInteractions<RichFacesSelect.AdvancedSelectInteractions> {

    @Drone
    private WebDriver driver;

    @Root
    private WebElement root;

    @FindBy(className = "rf-sel-inp")
    private TextInputComponentImpl input;
    @FindBy(className = "rf-sel-btn-arrow")
    private WebElement showButton;
    @FindBy(className = "rf-sel-shdw")
    private GrapheneElement localPopup;

    private static final ByJQuery GLOBAL_POPUP = ByJQuery.selector("div.rf-sel-shdw:visible");

    private final AdvancedSelectInteractions interactions = new AdvancedSelectInteractions();
    private final SelectSuggestionsImpl selectSuggestions = new SelectSuggestionsImpl();

    @Override
    public AdvancedSelectInteractions advanced() {
        return interactions;
    }

    private SelectSuggestionsImpl getSuggestions() {
        advanced().waitUntilSuggestionsAreVisible().perform();
        return selectSuggestions;
    }

    private List<WebElement> getSuggestionsElements() {
        return driver.findElement(GLOBAL_POPUP).findElements(By.className("rf-sel-opt"));
    }

    private boolean isPopupPresent() {
        return !driver.findElements(GLOBAL_POPUP).isEmpty() && !localPopup.isPresent();
    }

    @Override
    public SelectSuggestions openSelect() {
        if (!Utils.isVisible(driver, GLOBAL_POPUP) && !Utils.isVisible(localPopup)) {
            (advanced().getOpenByInputClick() ? input.advanced().getInputElement() : showButton).click();
        }
        return getSuggestions();
    }

    @Override
    public SelectSuggestions type(String text) {
        input.advanced().clear(ClearType.DELETE).sendKeys(text);
        return getSuggestions();
    }

    public class SelectSuggestionsImpl implements SelectSuggestions {

        @Override
        public void select(ChoicePicker picker) {
            WebElement foundValue = picker.pick(getSuggestionsElements());
            if (foundValue == null) {
                throw new RuntimeException("The value was not found by " + picker.toString());
            }

            if (advanced().getScrollingType() == ScrollingType.BY_KEYS) {
                selectWithKeys(foundValue);
            } else {
                foundValue.click();
            }
            advanced().waitUntilSuggestionsAreNotVisible().perform();
            input.advanced().trigger("blur");
        }

        @Override
        public void select(String match) {
            select(ChoicePickerHelper.byVisibleText().match(match));
        }

        @Override
        public void select(int index) {
            select(ChoicePickerHelper.byIndex().index(index));
        }

        private void selectWithKeys(WebElement foundValue) {
            // if selectFirst attribute is set, we don't have to press arrow down key for first item
            boolean skip = getSuggestionsElements().get(0).getAttribute("class").contains("rf-sel-sel");
            int index = Utils.getIndexOfElement(foundValue);
            int steps = index + (skip ? 0 : 1);
            Actions actions = new Actions(driver);
            for (int i = 0; i < steps; i++) {
                actions.sendKeys(Keys.ARROW_DOWN);
            }
            actions.sendKeys(foundValue, Keys.RETURN).perform();
        }

    }

    public class AdvancedSelectInteractions {

        private final ScrollingType DEFAULT_SCROLLING_TYPE = ScrollingType.BY_MOUSE;
        private static final boolean DEFAULT_OPEN_BY_INPUT_CLICK = true;
        private ScrollingType scrollingType = DEFAULT_SCROLLING_TYPE;
        private Boolean openByInputClick = DEFAULT_OPEN_BY_INPUT_CLICK;

        private long _timeoutForSuggestionsToBeNotVisible = -1;
        private long _timeoutForSuggestionsToBeVisible = -1;

        public TextInputComponentImpl getInput() {
            return input;
        }

        protected boolean getOpenByInputClick() {
            return openByInputClick;
        }

        public WebElement getRootElement() {
            return root;
        }

        protected ScrollingType getScrollingType() {
            return scrollingType;
        }

        public WebElement getShowButtonElement() {
            return showButton;
        }

        public List<WebElement> getSuggestionsElements() {
            return Collections.unmodifiableList(getSuggestionsElements());
        }

        public boolean isPopupPresent() {
            return RichFacesSelect.this.isPopupPresent();
        }

        public void setupOpenByInputClick() {
            openByInputClick = DEFAULT_OPEN_BY_INPUT_CLICK;
        }

        /**
         * Setups opening of select. Default open method is by clicking on the input.
         *
         * @param openByClickOnInput
         *            if true, select will be opened by input clicking (default). If false, the select will be opened by
         *            'show' button of the select.
         */
        public void setupOpenByInputClick(boolean openByClickOnInput) {
            openByInputClick = openByClickOnInput;
        }

        public void setupScrollingType() {
            scrollingType = DEFAULT_SCROLLING_TYPE;
        }

        /**
         * Setups scrolling type. Default value is By_MOUSE.
         *
         * @param type
         *            type of scrolling through the list of options and selecting on of them.
         */
        public void setupScrollingType(ScrollingType type) {
            scrollingType = type;
        }

        public WaitingWrapper waitUntilSuggestionsAreNotVisible() {
            return new WaitingWrapperImpl() {

                @Override
                protected void performWait(FluentWait<WebDriver, Void> wait) {
                    wait.until().element(localPopup).is().present();
                }
            }.withMessage("Waiting for popup to be not visible")
             .withTimeout(getTimeoutForSuggestionsToBeNotVisible(), TimeUnit.MILLISECONDS);
        }

        public WaitingWrapper waitUntilSuggestionsAreVisible() {
            return new WaitingWrapperImpl() {

                @Override
                protected void performWait(FluentWait<WebDriver, Void> wait) {
                    wait.until(new Predicate<WebDriver>() {

                        @Override
                        public boolean apply(WebDriver input) {
                            return isPopupPresent();
                        }
                    });
                }
            }.withMessage("Waiting for popup to be visible")
             .withTimeout(getTimeoutForSuggestionsToBeVisible(), TimeUnit.MILLISECONDS);
        }

        public void setupTimeoutForSuggestionsToBeNotVisible(long timeoutInMilliseconds) {
            _timeoutForSuggestionsToBeNotVisible = timeoutInMilliseconds;
        }

        public long getTimeoutForSuggestionsToBeNotVisible() {
            return _timeoutForSuggestionsToBeNotVisible == -1 ? Utils.getWaitAjaxDefaultTimeout(driver)
                : _timeoutForSuggestionsToBeNotVisible;
        }

        public void setupTimeoutForSuggestionsToBeVisible(long timeoutInMilliseconds) {
            _timeoutForSuggestionsToBeVisible = timeoutInMilliseconds;
        }

        public long getTimeoutForSuggestionsToBeVisible() {
            return _timeoutForSuggestionsToBeVisible == -1 ? Utils.getWaitAjaxDefaultTimeout(driver)
                : _timeoutForSuggestionsToBeVisible;
        }
    }
}

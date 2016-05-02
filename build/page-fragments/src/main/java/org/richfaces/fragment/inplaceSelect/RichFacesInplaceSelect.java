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
package org.richfaces.fragment.inplaceSelect;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.condition.element.WebElementConditionFactory;
import org.jboss.arquillian.graphene.fragment.Root;
import org.jboss.arquillian.graphene.wait.FluentWait;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.common.Actions;
import org.richfaces.fragment.common.AdvancedVisibleComponentIteractions;
import org.richfaces.fragment.common.Event;
import org.richfaces.fragment.common.TextInputComponentImpl;
import org.richfaces.fragment.common.Utils;
import org.richfaces.fragment.common.VisibleComponentInteractions;
import org.richfaces.fragment.common.WaitingWrapper;
import org.richfaces.fragment.common.WaitingWrapperImpl;
import org.richfaces.fragment.common.picker.ChoicePicker;
import org.richfaces.fragment.common.picker.ChoicePickerHelper;
import org.richfaces.fragment.inplaceInput.AbstractConfirmOrCancel;
import org.richfaces.fragment.inplaceInput.ConfirmOrCancel;
import org.richfaces.fragment.inplaceInput.InplaceComponentState;

public class RichFacesInplaceSelect implements InplaceSelect, AdvancedVisibleComponentIteractions<RichFacesInplaceSelect.AdvancedInplaceSelectInteractions> {

    @FindBy(css = "input[id$=Okbtn]")
    private WebElement confirmButton;

    @FindBy(css = "input[id$=Cancelbtn]")
    private WebElement cancelButton;

    @FindBy(className = "rf-is-fld")
    private TextInputComponentImpl textInput;

    @FindBy(className = "rf-is-lbl")
    private WebElement label;

    @FindBy(css = "span[id$=Edit] > input[id$=Input]")
    private WebElement editInputElement;

    @FindBy(className = "rf-is-lst-cord")
    private WebElement localList;

    @FindBy(xpath = "//body/span[contains(@class, rf-is-lst-cord)]")// whole page search
    private WebElement globalList;

    private static final String OPTIONS_CLASS = "rf-is-opt";

    @Root
    private WebElement root;

    @Drone
    private WebDriver browser;

    private final AdvancedInplaceSelectInteractions advancedInteractions = new AdvancedInplaceSelectInteractions();

    @Override
    public AdvancedInplaceSelectInteractions advanced() {
        return advancedInteractions;
    }

    @Override
    public TextInputComponentImpl getTextInput() {
        return textInput;
    }

    @Override
    public ConfirmOrCancel select(ChoicePicker picker) {
        advanced().switchToEditingState();
        if (!advanced().isInState(InplaceComponentState.ACTIVE)) {
            throw new IllegalStateException("You should set correct editBy event. Current: " + advanced().getEditByEvent()
                + " did not changed the inplace select for editing!");
        }
        WebElement optionToBeSelected = picker.pick(advanced().getOptions());
        if (optionToBeSelected == null) {
            throw new IllegalArgumentException("There is no such option to be selected, which satisfied the given rules!");
        }
        new Actions(browser).moveToElement(optionToBeSelected).click(optionToBeSelected).perform();
        if (advanced().isSaveOnSelect() && !isShowControlls()) {
            advanced().waitForPopupToHide().perform();
        }
        return new ConfirmOrCancelImpl();
    }

    @Override
    public ConfirmOrCancel select(int index) {
        return select(ChoicePickerHelper.byIndex().index(index));
    }

    @Override
    public ConfirmOrCancel select(String text) {
        return select(ChoicePickerHelper.byVisibleText().match(text));
    }

    public class ConfirmOrCancelImpl extends AbstractConfirmOrCancel {

        @Override
        public WebDriver getBrowser() {
            return browser;
        }

        @Override
        public WebElement getConfirmButton() {
            return advanced().getConfirmButtonElement();
        }

        @Override
        public WebElement getInput() {
            return getTextInput().advanced().getInputElement();
        }

        @Override
        public WebElement getCancelButton() {
            return advanced().getCancelButtonElement();
        }

        @Override
        public void waitAfterConfirmOrCancel() {
            advanced().waitForPopupToHide().perform();
        }
    }

    public class AdvancedInplaceSelectInteractions implements VisibleComponentInteractions {

        private final Event DEFAULT_EDIT_EVENT = Event.CLICK;
        private Event editByEvent = DEFAULT_EDIT_EVENT;
        private boolean saveOnSelect = false;

        private static final String RF_IS_CHNG_CLASS = "rf-is-chng";
        private static final String RF_IS_ACT_CLASS = "rf-is-act";
        private long _timeoutForPopupToHide = -1;
        private long _timeoutForPopupToShow = -1;

        protected String getChangedClass() {
            return RF_IS_CHNG_CLASS;
        }

        protected String getActiveClass() {
            return RF_IS_ACT_CLASS;
        }

        private Event getEditByEvent() {
            return editByEvent;
        }

        protected String getOptionsClass() {
            return OPTIONS_CLASS;
        }

        public void setEditByEvent() {
            editByEvent = DEFAULT_EDIT_EVENT;
        }

        public void setEditByEvent(Event event) {
            editByEvent = event;
        }

        public void setSaveOnSelect(boolean saveOnSelect) {
            this.saveOnSelect = saveOnSelect;
        }

        protected WebElement getGlobalList() {
            return globalList;
        }

        protected WebElement getLocalList() {
            return localList;
        }

        public WebElement getRootElement() {
            return root;
        }

        /**
         * Switch component to editing state, if it is not there already, by triggering the @editEvent on the label element.
         */
        public void switchToEditingState() {
            if (!isInState(InplaceComponentState.ACTIVE)) {
                new Actions(browser).moveToElement(getLabelInputElement()).triggerEventByWDOtherwiseByJS(getEditByEvent(),
                    getLabelInputElement()).perform();
                waitForPopupToShow().perform();
            }
        }

        public void setTimeoutForPopupToHide(long timeoutInMilliseconds) {
            _timeoutForPopupToHide = timeoutInMilliseconds;
        }

        public void setTimeoutForPopupToShow(long timeoutInMilliseconds) {
            _timeoutForPopupToShow = timeoutInMilliseconds;
        }

        public List<WebElement> getOptions() {
            return Collections.unmodifiableList(browser.findElements(By.className(advanced().getOptionsClass())));
        }

        public boolean isInState(InplaceComponentState state) {
            return root.getAttribute("class").contains(getClassForState(state));
        }

        public String getClassForState(InplaceComponentState state) {
            switch (state) {
                case ACTIVE:
                    return getActiveClass();
                case CHANGED:
                    return getChangedClass();
                default:
                    throw new UnsupportedOperationException();
            }
        }

        public String getLabelValue() {
            return getLabelInputElement().getText().trim();
        }

        public WebElement getEditInputElement() {
            return editInputElement;
        }

        public WebElement getLabelInputElement() {
            return label;
        }

        public WebElement getCancelButtonElement() {
            return cancelButton;
        }

        public WebElement getConfirmButtonElement() {
            return confirmButton;
        }

        public WebElement getSelectedOption() {
            for (WebElement element : getOptions()) {
                if (element.getAttribute("class").contains("rf-is-sel")) {
                    return element;
                }
            }
            return null;
        }

        public long getTimeoutForPopupToHide() {
            return (_timeoutForPopupToHide == -1L) ? Utils.getWaitAjaxDefaultTimeout(browser) : _timeoutForPopupToHide;
        }

        public long getTimeoutForPopupToShow() {
            return (_timeoutForPopupToShow == -1L) ? Utils.getWaitAjaxDefaultTimeout(browser) : _timeoutForPopupToShow;
        }

        public boolean isSaveOnSelect() {
            return saveOnSelect;
        }

        public WaitingWrapper waitForPopupToHide() {
            return new WaitingWrapperImpl() {

                @Override
                protected void performWait(FluentWait<WebDriver, Void> wait) {
                    wait.until().element(getLocalList()).is().present();
                    wait.until().element(getGlobalList()).is().not().visible();
                }
            }.withMessage("Waiting for popup to hide.")
                .withTimeout(getTimeoutForPopupToHide(), TimeUnit.MILLISECONDS);
        }

        public WaitingWrapper waitForPopupToShow() {
            return new WaitingWrapperImpl() {

                @Override
                protected void performWait(FluentWait<WebDriver, Void> wait) {
                    wait.until().element(getGlobalList()).is().visible();
                }
            }.withMessage("Waiting for popup to show.")
                .withTimeout(getTimeoutForPopupToShow(), TimeUnit.MILLISECONDS);
        }

        @Override
        public boolean isVisible() {
            return Utils.isVisible(getRootElement());
        }
    }

    private boolean isShowControlls() {
        return new WebElementConditionFactory(advanced().getCancelButtonElement()).isPresent().apply(browser);
    }
}

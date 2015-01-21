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
package org.richfaces.photoalbum.ftest.webdriver.fragments;

import java.util.Collections;
import java.util.List;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.condition.element.WebElementConditionFactory;
import org.jboss.arquillian.graphene.fragment.Root;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.common.Event;
import org.richfaces.fragment.common.TextInputComponentImpl;
import org.richfaces.fragment.common.Utils;
import org.richfaces.fragment.common.picker.ChoicePicker;
import org.richfaces.fragment.common.picker.ChoicePickerHelper;
import org.richfaces.fragment.inplaceInput.AbstractConfirmOrCancel;
import org.richfaces.fragment.inplaceInput.ConfirmOrCancel;
import org.richfaces.fragment.inplaceInput.InplaceComponentState;
import org.richfaces.fragment.inplaceSelect.InplaceSelect;

/**
 * InplaceSelect with predefined saveOnSelect.
 * FIXME: remove and replace with RichFacesInplaceSelect fragment after fixed.
 *
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class RFInplaceSelect implements InplaceSelect {

    @FindBy(css = "input[id$=Okbtn]")
    private WebElement confirmButton;

    @FindBy(css = "input[id$=Cancelbtn]")
    private WebElement cancelButton;

    @FindBy(className = "rf-is-fld")
    private TextInputComponentImpl textInput;

    @FindBy(css = ".rf-is-lbl")
    private WebElement label;

    @FindBy(css = "span[id$=Edit] > input[id$=Input]")
    private WebElement editInputElement;

    @FindBy(tagName = "script")
    private WebElement script;

    @FindBy(css = "span.rf-is-lst-cord")
    private WebElement localList;

    @FindBy(xpath = "//body/span[contains(@class, rf-is-lst-cord)]")
    // whole page search
    private WebElement globalList;

    private static final String OPTIONS_CLASS = "rf-is-opt";

    @Root
    private WebElement root;

    @Drone
    private WebDriver browser;

    @ArquillianResource
    private JavascriptExecutor executor;

    private final AdvancedInplaceSelectInteractions advancedInteractions = new AdvancedInplaceSelectInteractions();

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
        WebElement optionToBeSelected = picker.pick(advanced().getOptions());
        optionToBeSelected.click();
        if (isSaveOnSelect() && !isShowControlls()) {
            textInput.advanced().trigger("selectitem");
            waitForPopupHide();
        }
        return new ConfirmOrCancelImpl();
    }

    @Override
    public ConfirmOrCancel select(int index) {
        return select(ChoicePickerHelper.byIndex().index(index));
    }

    @Override
    public ConfirmOrCancel select(String text) {
        return select(ChoicePickerHelper.byVisibleText().contains(text));
    }

    public class ConfirmOrCancelImpl extends AbstractConfirmOrCancel {

        @Override
        public WebDriver getBrowser() {
            return browser;
        }

        @Override
        public WebElement getConfirmButton() {
            return confirmButton;
        }

        @Override
        public WebElement getInput() {
            return textInput.advanced().getInputElement();
        }

        @Override
        public WebElement getCancelButton() {
            return cancelButton;
        }

        @Override
        public void waitAfterConfirmOrCancel() {
            waitForPopupHide();
        }
    }

    public class AdvancedInplaceSelectInteractions {

        private final Event DEFAULT_EDIT_EVENT = Event.CLICK;
        private Event editByEvent = DEFAULT_EDIT_EVENT;

        private static final String RF_IS_CHNG_CLASS = "rf-is-chng";
        private static final String RF_IS_ACT_CLASS = "rf-is-act";

        public void setupEditByEvent() {
            editByEvent = DEFAULT_EDIT_EVENT;
        }

        public void setupEditByEvent(Event event) {
            editByEvent = event;
        }

        public WebElement getRootElement() {
            return root;
        }

        public void switchToEditingState() {
            Utils.triggerJQ(executor, editByEvent.getEventName(), root);
        }

        public List<WebElement> getOptions() {
            return Collections.unmodifiableList(browser.findElements(By.className(OPTIONS_CLASS)));
        }

        public boolean isInState(InplaceComponentState state) {
            return root.getAttribute("class").contains(getClassForState(state));
        }

        public String getClassForState(InplaceComponentState state) {
            switch (state) {
                case ACTIVE:
                    return RF_IS_ACT_CLASS;
                case CHANGED:
                    return RF_IS_CHNG_CLASS;
                default:
                    throw new UnsupportedOperationException();
            }
        }

        public String getLabelValue() {
            return label.getText().trim();
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
    }

    private boolean isSaveOnSelect() {
        return Boolean.TRUE;
    }

    private boolean isShowControlls() {
        return new WebElementConditionFactory(cancelButton).isPresent().apply(browser);
    }

    private void waitForPopupHide() {
        Graphene.waitModel().until().element(localList).is().present();
        Graphene.waitModel().until().element(globalList).is().not().visible();
    }
}

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
package org.richfaces.tests.page.fragments.impl.inplaceInput;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.jboss.arquillian.graphene.fragment.Root;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.page.fragments.impl.Utils;
import org.richfaces.tests.page.fragments.impl.common.AdvancedInteractions;
import org.richfaces.tests.page.fragments.impl.common.TextInputComponentImpl;
import org.richfaces.tests.page.fragments.impl.utils.Event;

public class RichFacesInplaceInput implements InplaceInput, AdvancedInteractions<RichFacesInplaceInput.AdvancedInplaceInputInteractions> {

    @FindBy(className = "rf-ii-fld")
    private TextInputComponentImpl textInput;

    @FindByJQuery(".rf-ii-btn:eq(0)")
    private WebElement confirmButton;

    @FindByJQuery(".rf-ii-btn:eq(1)")
    private WebElement cancelButton;

    @FindBy(css = "span[id$=Label]")
    private WebElement label;

    @FindBy(css = "span[id$=Edit] span[id$=Btn]")
    private WebElement controls;

    @FindBy(css = "span[id$=Edit] > input[id$=Input]")
    private WebElement editInputElement;

    @Root
    private WebElement root;

    @Drone
    private WebDriver browser;

    @ArquillianResource
    private JavascriptExecutor executor;

    private final AdvancedInplaceInputInteractions advancedInteractions = new AdvancedInplaceInputInteractions();

    @Override
    public AdvancedInplaceInputInteractions advanced() {
        return advancedInteractions;
    }

    @Override
    public TextInputComponentImpl getTextInput() {
        return textInput;
    }

    @Override
    public ConfirmOrCancel type(String text) {
        Utils.triggerJQ(executor, advanced().getEditByEvent().getEventName(), root);
        if (!advanced().isInState(InplaceComponentState.ACTIVE)) {
            throw new IllegalStateException("You should set correct editBy event. Current: " + advanced().getEditByEvent()
                + " did not changed the inplace input for editing!");
        }
        textInput.clear().sendKeys(text);
        return new ConfirmOrCancelImpl();
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
            return editInputElement;
        }

        @Override
        public WebElement getCancelButton() {
            return cancelButton;
        }

        @Override
        public void waitAfterConfirmOrCancel() {
        }
    }

    public class AdvancedInplaceInputInteractions {

        private static final String RF_II_CHNG_CLASS = "rf-ii-chng";
        private static final String RF_II_ACT_CLASS = "rf-ii-act";
        private final Event DEFAULT_EDIT_EVENT = Event.CLICK;
        private Event editByEvent = DEFAULT_EDIT_EVENT;

        protected Event getEditByEvent() {
            return editByEvent;
        }

        public void setupEditByEvent() {
            editByEvent = DEFAULT_EDIT_EVENT;
        }

        public void setupEditByEvent(Event event) {
            editByEvent = event;
        }

        public WebElement getRootElement() {
            return root;
        }

        public boolean isInState(InplaceComponentState state) {
            return root.getAttribute("class").contains(getClassForState(state));
        }

        public String getClassForState(InplaceComponentState state) {
            switch (state) {
                case ACTIVE:
                    return RF_II_ACT_CLASS;
                case CHANGED:
                    return RF_II_CHNG_CLASS;
                default:
                    throw new UnsupportedOperationException();
            }
        }

        public String getLabelValue() {
            return label.getText().trim();
        }

        public WebElement getCancelButtonElement() {
            return cancelButton;
        }

        public WebElement getConfirmButtonElement() {
            return confirmButton;
        }

        public WebElement getEditInputElement() {
            return editInputElement;
        }

        public WebElement getLabelInputElement() {
            return label;
        }
    }
}

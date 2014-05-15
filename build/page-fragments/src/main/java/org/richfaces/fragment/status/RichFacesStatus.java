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
package org.richfaces.fragment.status;

import org.jboss.arquillian.graphene.fragment.Root;
import org.jboss.arquillian.graphene.wait.FluentWait;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.common.AdvancedInteractions;
import org.richfaces.fragment.common.Utils;
import org.richfaces.fragment.common.WaitingWrapper;
import org.richfaces.fragment.common.WaitingWrapperImpl;
import org.richfaces.fragment.status.RichFacesStatus.AdvancedStatusInteractions;

import com.google.common.base.Predicate;

public class RichFacesStatus implements Status, AdvancedInteractions<AdvancedStatusInteractions> {

    @Root
    private WebElement rootElement;
    @FindBy(className = "rf-st-error")
    private WebElement errorElement;
    @FindBy(className = "rf-st-start")
    private WebElement startElement;
    @FindBy(className = "rf-st-stop")
    private WebElement stopElement;

    private final AdvancedStatusInteractions interactions = new AdvancedStatusInteractions();

    @Override
    public AdvancedStatusInteractions advanced() {
        return interactions;
    }

    public AdvancedStatusInteractions getInteractions() {
        return interactions;
    }

    @Override
    public StatusState getStatusState() {
        return Utils.isVisible(startElement) ? StatusState.START : Utils.isVisible(stopElement) ? StatusState.STOP : StatusState.ERROR;
    }

    @Override
    public String getStatusText() {
        return rootElement.getText();
        // return Utils.isVisible(start) ? start.getText() : Utils.isVisible(stop) ? stop.getText() : error.getText();
    }

    public class AdvancedStatusInteractions {

        public WebElement getErrorElement() {
            return errorElement;
        }

        public WebElement getRootElement() {
            return rootElement;
        }

        public WebElement getStartElement() {
            return startElement;
        }

        public WebElement getStopElement() {
            return stopElement;
        }

        public WaitingWrapper waitUntilStatusStateChanges(final StatusState state) {
            return new WaitingWrapperImpl() {

                @Override
                protected void performWait(FluentWait<WebDriver, Void> wait) {
                    wait.until(new Predicate<WebDriver>() {
                        @Override
                        public boolean apply(WebDriver input) {
                            return getStatusState().equals(state);
                        }
                    });
                }
            }.withMessage("Waiting for status state changes to <" + state + ">");
        }

        public WaitingWrapper waitUntilStatusTextChanges(final String statusText) {
            return new WaitingWrapperImpl() {

                @Override
                protected void performWait(FluentWait<WebDriver, Void> wait) {
                    wait.until(new Predicate<WebDriver>() {
                        @Override
                        public boolean apply(WebDriver input) {
                            return getStatusText().equals(statusText);
                        }
                    });
                }
            }.withMessage("Waiting for status text changes to <" + statusText + ">");
        }

        public WaitingWrapper waitUntilStatusTextChanges() {
            final String before = getStatusText();
            return new WaitingWrapperImpl() {
                @Override
                protected void performWait(FluentWait<WebDriver, Void> wait) {
                    wait.until(new Predicate<WebDriver>() {
                        @Override
                        public boolean apply(WebDriver input) {
                            return !getStatusText().equals(before);
                        }
                    });
                }
            }.withMessage("Waiting for status text changes from <" + before + "> to something else.");
        }
    }
}

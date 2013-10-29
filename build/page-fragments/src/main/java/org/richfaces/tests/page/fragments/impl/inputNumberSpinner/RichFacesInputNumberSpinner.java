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
package org.richfaces.tests.page.fragments.impl.inputNumberSpinner;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.fragment.Root;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.page.fragments.impl.common.AdvancedInteractions;
import org.richfaces.tests.page.fragments.impl.common.TextInputComponentImpl;
import org.richfaces.tests.page.fragments.impl.inputNumberSlider.AbstractNumberInput;
import org.richfaces.tests.page.fragments.impl.inputNumberSlider.NumberInput;

public class RichFacesInputNumberSpinner extends AbstractNumberInput implements NumberInput, AdvancedInteractions<RichFacesInputNumberSpinner.AdvancedInputNumberSpinnerInteractions> {

    @FindBy(css = "input.rf-insp-inp")
    private TextInputComponentImpl input;

    @FindBy(css = "span.rf-insp-btns > span.rf-insp-inc")
    private WebElement arrowIncrease;

    @FindBy(css = "span.rf-insp-btns > span.rf-insp-dec")
    private WebElement arrowDecrease;

    @Drone
    private WebDriver browser;

    @Root
    private WebElement root;

    private final AdvancedInputNumberSpinnerInteractions advancedInteractions = new AdvancedInputNumberSpinnerInteractions();

    @Override
    public AdvancedInputNumberSpinnerInteractions advanced() {
        return advancedInteractions;
    }

    @Override
    protected WebElement getArrowDecreaseElement() {
        return arrowDecrease;
    }

    @Override
    protected WebElement getArrowIncreaseElement() {
        return arrowIncrease;
    }

    @Override
    protected WebDriver getBrowser() {
        return browser;
    }

    @Override
    protected TextInputComponentImpl getInput() {
        return input;
    }

    public class AdvancedInputNumberSpinnerInteractions extends AbstractNumberInput.AdvancedNumberInputInteractions {

        public WebElement getRootElement() {
            return root;
        }
    }
}

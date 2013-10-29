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
package org.richfaces.tests.page.fragments.impl.inputNumberSlider;

import org.jboss.arquillian.graphene.condition.element.WebElementConditionFactory;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.richfaces.tests.page.fragments.impl.common.TextInputComponentImpl;

public abstract class AbstractNumberInput implements NumberInput {

    @Override
    public void increase() {
        if (!new WebElementConditionFactory(getArrowIncreaseElement()).isVisible().apply(getBrowser())) {
            throw new RuntimeException("Arrow for increasing value is not visible.");
        }
        getArrowIncreaseElement().click();
    }

    @Override
    public void decrease() {
        if (!new WebElementConditionFactory(getArrowDecreaseElement()).isVisible().apply(getBrowser())) {
            throw new RuntimeException("arrow for decreasing value is not visible.");
        }
        getArrowDecreaseElement().click();
    }

    @Override
    public void increase(int n) {
        for (int i = 0; i < n; i++) {
            increase();
        }
    }

    @Override
    public void decrease(int n) {
        for (int i = 0; i < n; i++) {
            decrease();
        }
    }

    @Override
    public void setValue(double value) {
        getInput().sendKeys(String.valueOf(value));
    }

    @Override
    public double getValue() {
        return Double.valueOf(getInput().getStringValue());
    }

    protected abstract WebElement getArrowIncreaseElement();

    protected abstract WebDriver getBrowser();

    protected abstract WebElement getArrowDecreaseElement();

    protected abstract TextInputComponentImpl getInput();

    public class AdvancedNumberInputInteractions {

        public TextInputComponentImpl getInput() {
            return AbstractNumberInput.this.getInput();
        }

        public WebElement getArrowIncreaseElement() {
            return AbstractNumberInput.this.getArrowIncreaseElement();
        }

        public WebElement getArrowDecreaseElement() {
            return AbstractNumberInput.this.getArrowDecreaseElement();
        }
    }
}

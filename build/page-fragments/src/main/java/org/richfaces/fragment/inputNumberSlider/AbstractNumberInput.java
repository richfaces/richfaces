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
package org.richfaces.fragment.inputNumberSlider;

import org.jboss.arquillian.graphene.condition.element.WebElementConditionFactory;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.richfaces.fragment.common.ClearType;
import org.richfaces.fragment.common.TextInputComponentImpl;

public abstract class AbstractNumberInput implements NumberInput {

    @ArquillianResource
    private WebDriver browser;

    public abstract AdvancedNumberInputInteractions advanced();

    @Override
    public void increase() {
        if (!new WebElementConditionFactory(advanced().getArrowIncreaseElement()).isVisible().apply(browser)) {
            throw new RuntimeException("Arrow for increasing value is not visible.");
        }
        advanced().getArrowIncreaseElement().click();
    }

    @Override
    public void decrease() {
        if (!new WebElementConditionFactory(advanced().getArrowDecreaseElement()).isVisible().apply(browser)) {
            throw new RuntimeException("arrow for decreasing value is not visible.");
        }
        advanced().getArrowDecreaseElement().click();
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
        advanced().getInput().advanced().clear(ClearType.BACKSPACE).sendKeys(String.valueOf(value));
    }

    @Override
    public double getValue() {
        return Double.valueOf(advanced().getInput().getStringValue());
    }

    public abstract class AdvancedNumberInputInteractions {

        public abstract WebElement getArrowIncreaseElement();

        public abstract WebElement getArrowDecreaseElement();

        public abstract TextInputComponentImpl getInput();
    }
}

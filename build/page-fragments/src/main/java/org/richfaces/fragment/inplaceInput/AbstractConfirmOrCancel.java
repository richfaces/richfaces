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
package org.richfaces.fragment.inplaceInput;

import org.jboss.arquillian.graphene.condition.element.WebElementConditionFactory;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.richfaces.fragment.common.Utils;

public abstract class AbstractConfirmOrCancel implements ConfirmOrCancel {

    @Override
    public void confirm() {
        new Actions(getBrowser()).sendKeys(Keys.chord(Keys.CONTROL, Keys.RETURN)).perform();
        waitAfterConfirmOrCancel();
    }

    @Override
    public void confirmByControlls() {
        checkControllsAreAvailable();
        Utils.triggerJQ("mousedown", getConfirmButton());
        waitAfterConfirmOrCancel();
    }

    @Override
    public void cancel() {
        getInput().sendKeys(Keys.chord(Keys.CONTROL, Keys.ESCAPE));
        waitAfterConfirmOrCancel();
    }

    @Override
    public void cancelByControlls() {
        checkControllsAreAvailable();
        Utils.triggerJQ("mousedown", getCancelButton());
        waitAfterConfirmOrCancel();
    }

    private void checkControllsAreAvailable() {
        boolean condition = new WebElementConditionFactory(getConfirmButton()).isPresent().apply(getBrowser());
        if (!condition) {
            throw new IllegalStateException(
                "You are trying to use cotrolls to confirm/cancel the input, however, there are no controlls!");
        }
    }

    public abstract WebDriver getBrowser();

    public abstract WebElement getConfirmButton();

    public abstract WebElement getInput();

    public abstract WebElement getCancelButton();

    public abstract void waitAfterConfirmOrCancel();
}

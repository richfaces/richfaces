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

package org.richfaces.component.validation;

import static org.junit.Assert.assertThat;

import java.net.URL;

import org.hamcrest.Matcher;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public abstract class ValidationTestBase {

    @Drone
    protected WebDriver browser;

    @ArquillianResource
    protected URL contextPath;

    @FindBy(name = "form:text")
    protected WebElement inputText;

    @FindBy(id = "blurButton")
    protected WebElement blurButton;

    protected void submitValueAndCheckMessage(String value, Matcher<String> matcher) throws Exception {
        browser.get(contextPath.toString());
        inputText.clear();
        inputText.sendKeys(value);
        submitValue();
        checkMessage("uiMessage", matcher);
    }

    protected void submitValue() {
        blurButton.click();// blur
    }

    protected void checkMessage(String messageId, Matcher<String> matcher) {
        WebElement message = browser.findElement(By.id(messageId));
        assertThat(message.getText().trim(), matcher);
    }
}

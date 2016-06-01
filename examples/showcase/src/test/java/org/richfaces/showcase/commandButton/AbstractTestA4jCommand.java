/*
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
 */
package org.richfaces.showcase.commandButton;

import static org.junit.Assert.assertEquals;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.showcase.AbstractWebDriverTest;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public abstract class AbstractTestA4jCommand extends AbstractWebDriverTest {

    @FindByJQuery("form input[type=text]:first")
    protected WebElement input;
    @FindBy(className = "outhello")
    protected WebElement output;

    protected void checkClickOnTheButtonWhileInputIsEmpty(String empty) {
        // click on the button, the output should be empty string
        Graphene.guardAjax(getCommand()).click();

        try {
            output.isDisplayed();
            assertEquals("The output should be emtpy string", empty, output.getText().trim());
        } catch (NoSuchElementException ignored) {
        }
    }

    protected void checkTypeSomeCharactersAndClickOnTheButton() {
        // type a string and click on the button, check the outHello
        String testString = "Test string";

        input.click();
        input.clear();
        input.sendKeys(testString);

        Graphene.guardAjax(getCommand()).click();

        String expectedOutput = "Hello " + testString + " !";
        assertEquals("The output should be: " + expectedOutput, output.getText(), expectedOutput);
    }

    protected void checkEraseSomeStringAndClickOnTheButton(String empty) {
        // erases string and check the output is empty string
        String testString = "Test string";

        input.click();
        input.clear();
        input.sendKeys(testString);

        Graphene.guardAjax(getCommand()).click();

        input.clear();

        Graphene.guardAjax(getCommand()).click();

        try {
            output.isDisplayed();
            assertEquals("The output should be emtpy string", empty, output.getText().trim());
        } catch (NoSuchElementException ignored) {
        }
    }

    protected abstract WebElement getCommand();

}

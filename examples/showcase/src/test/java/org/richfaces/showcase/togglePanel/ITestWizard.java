/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2015, Red Hat, Inc. and individual contributors
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
package org.richfaces.showcase.togglePanel;

import static org.jboss.arquillian.graphene.Graphene.guardAjax;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jboss.arquillian.graphene.page.Page;
import org.junit.Test;
import org.openqa.selenium.WebElement;
import org.richfaces.showcase.AbstractWebDriverTest;
import org.richfaces.showcase.togglePanel.page.WizardPage;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 */
public class ITestWizard extends AbstractWebDriverTest {

    private static final String ERROR_MSG_VALUE_REQUIRED = "Validation Error: Value is required.";
    private static final String ERROR_MSG_FIRST_NAME = "First Name: " + ERROR_MSG_VALUE_REQUIRED;
    private static final String ERROR_MSG_LAST_NAME = "Last Name: " + ERROR_MSG_VALUE_REQUIRED;
    private static final String ERROR_MSG_COMPANY = "Company: " + ERROR_MSG_VALUE_REQUIRED;
    private static final String ERROR_MSG_NOTES = "Notes: " + ERROR_MSG_VALUE_REQUIRED;

    @Page
    private WizardPage page;

    @Test
    public void testStep1ValidationEmptyInputs() {
        // do not enter any values and try to proceed to the next step
        guardAjax(page.getNextButton()).click();
        checkForAllErrorMessagesFromFirstStep(true);
        // fill in correct values and proceed to the next step
        fillInputsWithText("xxxxx");
        guardAjax(page.getNextButton()).click();
        checkForAllErrorMessagesFromFirstStep(false);
    }

    @Test
    public void testStep1ValidationCorrectValues() {
        fillInputsWithText("xxxxx");
        guardAjax(page.getNextButton()).click();
        checkForAllErrorMessagesFromFirstStep(false);
    }

    @Test
    public void testStep2ValidationEmptyInput() {
        testStep1ValidationCorrectValues();
        // do not enter any values and try to proceed to the next step
        guardAjax(page.getNextButton()).click();
        checkForErrors(page.getErrorMessageNotes(), ERROR_MSG_NOTES, true);
        page.getNotesInput().sendKeys("blablabla");
        guardAjax(page.getNextButton()).click();
        checkForErrors(page.getErrorMessageNotes(), ERROR_MSG_NOTES, false);
    }

    @Test
    public void testStep2ValidationCorrectValue() {
        testStep1ValidationCorrectValues();
        page.getNotesInput().sendKeys("notes");
        guardAjax(page.getNextButton()).click();
        checkForErrors(page.getErrorMessageNotes(), ERROR_MSG_NOTES, false);
    }

    @Test
    public void testStep3CheckSummaryOfPreviousSteps() {
        testStep1ValidationCorrectValues();
        page.getNotesInput().sendKeys("notes");
        guardAjax(page.getNextButton()).click();
        assertTrue("Notes should be displayed in the summary", page.getSummaryOfAllSteps().getText().contains("Notes: notes"));
    }

    private void checkForErrors(WebElement element, String errorMessage, boolean shouldErrorBePresented) {
        if (shouldErrorBePresented) {
            assertEquals("Error " + errorMessage + " was expected!", errorMessage, element.getText());
        } else {
            assertFalse("There should be no error message!", isElementPresent(element));
        }
    }

    private void fillInputsWithText(String text) {
        page.getFirstNameInput().sendKeys(text);
        page.getLastNameInput().sendKeys(text);
        page.getCompanyInput().sendKeys(text);
    }

    private void checkForAllErrorMessagesFromFirstStep(boolean shouldErrorMessagePresented) {
        if (shouldErrorMessagePresented) {
            assertEquals(ERROR_MSG_FIRST_NAME, page.getErrorMessageFirstName().getText());
            assertEquals(ERROR_MSG_LAST_NAME, page.getErrorMessageLastName().getText());
            assertEquals(ERROR_MSG_COMPANY, page.getErrorMessageCompany().getText());
        } else {
            assertFalse(isElementPresent(page.getErrorMessageFirstName()));
            assertFalse(isElementPresent(page.getErrorMessageLastName()));
            assertFalse(isElementPresent(page.getErrorMessageCompany()));
        }
    }
}

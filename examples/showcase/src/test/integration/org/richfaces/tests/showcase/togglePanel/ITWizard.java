/**
 * *****************************************************************************
 * JBoss, Home of Professional Open Source Copyright 2010-2013, Red Hat, Inc.
 * and individual contributors by the
 *
 * @authors tag. See the copyright.txt in the distribution for a full listing of
 * individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this software; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA, or see the FSF
 * site: http://www.fsf.org.
 ******************************************************************************
 */
package org.richfaces.tests.showcase.togglePanel;

import static org.jboss.arquillian.graphene.Graphene.guardAjax;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jboss.arquillian.graphene.spi.annotations.Page;
import org.junit.Test;
import org.openqa.selenium.WebElement;
import org.richfaces.tests.showcase.AbstractWebDriverTest;
import org.richfaces.tests.showcase.togglePanel.page.WizardPage;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class ITWizard extends AbstractWebDriverTest {

    private static final String ERROR_MSG_VALUE_REQUIRED = "Validation Error: Value is required.";
    private static final String ERROR_MSG_FIRST_NAME = "First Name: " + ERROR_MSG_VALUE_REQUIRED;
    private static final String ERROR_MSG_LAST_NAME = "Last Name: " + ERROR_MSG_VALUE_REQUIRED;
    private static final String ERROR_MSG_COMPANY = "Company: " + ERROR_MSG_VALUE_REQUIRED;
    private static final String ERROR_MSG_NOTES = "Notes: " + ERROR_MSG_VALUE_REQUIRED;

    @Page
    private WizardPage page;

    @Test
    public void testStep1ValidationEmptyInputs() {
        guardAjax(page.nextButton).click();
        checkForAllErrorMessagesFromFirstStep(true);
        fillInputsWithText(" ");
        guardAjax(page.nextButton).click();
        checkForAllErrorMessagesFromFirstStep(false);
    }

    @Test
    public void testStep1ValidationCorrectValues() {
        fillInputsWithText("xxxxx");
        guardAjax(page.nextButton).click();
        checkForAllErrorMessagesFromFirstStep(false);
    }

    @Test
    public void testStep2ValidationEmptyInput() {
        fillInputsWithText("xxxxx");
        guardAjax(page.nextButton).click();
        guardAjax(page.nextButton).click();
        checkForErrors(page.errorMessageNotes, ERROR_MSG_NOTES, true);
        page.notesInput.sendKeys("blablabla");
        guardAjax(page.nextButton).click();
        checkForErrors(page.errorMessageNotes, ERROR_MSG_NOTES, false);
    }

    @Test
    public void testStep2ValidationCorrectValue() {
        testStep1ValidationCorrectValues();
        page.notesInput.sendKeys("notes");
        guardAjax(page.nextButton).click();
        checkForErrors(page.errorMessageNotes, ERROR_MSG_NOTES, false);
    }

    @Test
    public void testStep3CheckSummaryOfPreviousSteps() {
        testStep1ValidationCorrectValues();
        page.notesInput.sendKeys("notes");
        guardAjax(page.nextButton).click();
        assertTrue("Notes should be displayed in the summary", page.summaryOfAllSteps.getText().contains("Notes: notes"));
    }

    private void checkForErrors(WebElement element, String errorMessage, boolean shouldErrorBePresented) {
        if(shouldErrorBePresented) {
            assertEquals("Error " + errorMessage + " was expected!", element.getText(), errorMessage);
        } else {
            assertFalse("There should be no error message!", isElementPresent(element));
        }
    }

    private void fillInputsWithText(String text) {
        page.firstNameInput.sendKeys(text);
        page.lastNameInput.sendKeys(text);
        page.companyInput.sendKeys(text);
    }

    private void checkForAllErrorMessagesFromFirstStep(boolean shouldErrorMessagePresented) {
        if (shouldErrorMessagePresented) {
            assertEquals(page.errorMessageFirstName.getText(), ERROR_MSG_FIRST_NAME);
            assertEquals(page.errorMessageLastName.getText(), ERROR_MSG_LAST_NAME);
            assertEquals(page.errorMessageCompany.getText(), ERROR_MSG_COMPANY);
        } else {
            assertFalse(isElementPresent(page.errorMessageFirstName));
            assertFalse(isElementPresent(page.errorMessageLastName));
            assertFalse(isElementPresent(page.errorMessageCompany));
        }
    }
}

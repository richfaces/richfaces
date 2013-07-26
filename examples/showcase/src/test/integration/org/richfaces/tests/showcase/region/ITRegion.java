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
package org.richfaces.tests.showcase.region;

import static org.junit.Assert.assertEquals;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.spi.annotations.Page;
import org.junit.Test;
import org.openqa.selenium.WebElement;
import org.richfaces.tests.showcase.AbstractWebDriverTest;
import org.richfaces.tests.showcase.region.page.RegionPage;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 * @version $Revision$
 */
public class ITRegion extends AbstractWebDriverTest {

    /* *******************************************************************************************************
     * Locators ****************************************************************** *************************************
     */

    @Page
    private RegionPage page;

    /* ********************************************************************************************************
     * Tests ********************************************************************* ***********************************
     */

    @Test
    public void testFirstWrongSubmitUserName() {

        typeSomethingToInputTestWhetherOutputIsEmpty(page.brokenNameInput, page.brokenNameOutput, page.brokenSubmit, true);
    }

    @Test
    public void testFirstWrongSubmitEmail() {

        typeSomethingToInputTestWhetherOutputIsEmpty(page.brokenEmailInput, page.brokenEmailOutput, page.brokenSubmit, true);
    }

    @Test
    public void testFirstWrongSubmitUserNameAndEmail() {

        typeSomethingToTwoInputsTestWhetherOuputIsEmpty(page.brokenNameInput, page.brokenEmailInput, page.brokenNameOutput, page.brokenEmailOutput,
            page.brokenSubmit, true);
    }

    @Test
    public void testSecondRightSubmitUserName() {

        typeSomethingToInputTestWhetherOutputIsEmpty(page.nameInput, page.nameOutput, page.submit, false);
    }

    @Test
    public void testSecondRightSubmitEmail() {

        typeSomethingToInputTestWhetherOutputIsEmpty(page.emailInput, page.emailOutput, page.submit, false);
    }

    @Test
    public void testSecondRightSubmitUserNameAndEmail() {

        typeSomethingToTwoInputsTestWhetherOuputIsEmpty(page.nameInput, page.emailInput, page.nameOutput,
            page.emailOutput, page.submit, false);
    }

    /* ********************************************************************************************************
     * Help methods *********************************************************************
     * ***********************************
     */

    /**
     * Types some string to the input and checks whether the output is empty string
     *
     * @param input
     *            input where string will be typed
     * @param output
     *            output which should be empty
     */
    private void typeSomethingToInputTestWhetherOutputIsEmpty(WebElement input, WebElement output,
        WebElement submit, boolean shouldBeEmpty) {

        String testString = "Test String";

        input.click();
        input.clear();
        input.sendKeys(testString);

        Graphene.guardAjax(submit).click();

        String actualString = output.getText().trim();

        if (shouldBeEmpty) {
            assertEquals("The string should be empty!", actualString, "");
        } else {
            assertEquals(actualString, testString);
        }
    }

    /**
     * Types something to two inputs and checks outputs whether they are empty or not
     *
     * @param input1
     *            first input where something will be typed
     * @param input2
     *            second input where something will be typed
     * @param output1
     *            first checked output
     * @param output2
     *            second checked output
     * @param submit
     *            submit button where will be clicked
     * @param shouldBeEmpty
     *            should be outputs empty?
     */
    private void typeSomethingToTwoInputsTestWhetherOuputIsEmpty(WebElement input1, WebElement input2,
        WebElement output1, WebElement output2, WebElement submit, boolean shouldBeEmpty) {

        String testStringUserName = "Test String user name";
        String testStringEmail = "Test String email";

        input1.click();
        input1.clear();
        input1.sendKeys(testStringUserName);

        input2.click();
        input2.clear();
        input2.sendKeys(testStringEmail);

        Graphene.guardAjax(submit).click();

        String actualName = output1.getText().trim();
        String actualEmail = output2.getText().trim();

        if (shouldBeEmpty) {
            assertEquals("The string retrieved from entered name should be empty!", "", actualName);
            assertEquals("The string retrieved from entered emial should be empty!", "", actualEmail);
        } else {
            assertEquals(testStringUserName, actualName);
            assertEquals(testStringEmail, actualEmail);
        }

    }
}

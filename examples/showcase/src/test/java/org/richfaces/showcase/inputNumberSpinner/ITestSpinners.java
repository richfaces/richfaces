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
package org.richfaces.showcase.inputNumberSpinner;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.inputNumberSpinner.RichFacesInputNumberSpinner;
import org.richfaces.showcase.AbstractWebDriverTest;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 * @version $Revision$
 */
public class ITestSpinners extends AbstractWebDriverTest {

    protected final int MAX_VALUE = 100;
    protected final int MIN_VALUE = 0;

    protected final int FIRST_INPUT_STEP = 1;
    protected final int SECOND_INPUT_STEP = 10;

    @FindBy(className = "rf-insp")
    private List<RichFacesInputNumberSpinner> spinners;

    /* *********************************************************************************
     * Tests ********************************************************************* ************
     */
    @Test
    public void testInputIncreasedByOne() {
        increaseByStep(spinners.get(0), FIRST_INPUT_STEP);
    }

    @Test
    public void testInputIcreaseByTen() {
        increaseByStep(spinners.get(1), SECOND_INPUT_STEP);
    }

    @Test
    public void testInputDecreaseByOne() {
        decreaseByStep(spinners.get(0), FIRST_INPUT_STEP);
    }

    @Test
    public void testInputDecreaseByTen() {
        decreaseByStep(spinners.get(1), SECOND_INPUT_STEP);
    }

    @Test
    public void testInputSetAndDecreaseByTen() {
        setAndDecrease(spinners.get(1), SECOND_INPUT_STEP);
    }

    @Test
    public void testInputSetAndDecreaseByOne() {
        setAndDecrease(spinners.get(0), FIRST_INPUT_STEP);
    }

    /* **************************************************************************************************************************
     * Help methods **************************************************************
     * ************************************************************
     */
    /**
     * Decrease by step via decrease spinner
     * 
     * @param input which value will be decreased
     * @param increaseSpinner the decrease spinner which will be clicked on
     * @param step the step by which will be the value decreased
     */
    private void decreaseByStep(RichFacesInputNumberSpinner spinner, int step) {

        int currentValueOfInput = spinner.advanced().getInput().getIntValue();

        for (int i = currentValueOfInput; i > MIN_VALUE; i -= step) {

            spinner.decrease();

            assertEquals("The value should be decreased by " + step, currentValueOfInput - step, spinner.advanced().getInput()
                .getIntValue());

            currentValueOfInput = spinner.advanced().getInput().getIntValue();
        }

        spinner.decrease();

        currentValueOfInput = spinner.advanced().getInput().getIntValue();

        assertEquals("The current value in the input should be maximal, so " + MAX_VALUE, MAX_VALUE, currentValueOfInput);
    }

    /**
     * Increase by step via increase spinner
     * 
     * @param spinner
     * @param step the step by which will be the value increased
     */
    private void increaseByStep(RichFacesInputNumberSpinner spinner, int step) {

        int currentValueOfInput = spinner.advanced().getInput().getIntValue();

        for (int i = currentValueOfInput; i < MAX_VALUE; i += step) {

            spinner.increase();

            assertEquals("The value should be increased by " + step, currentValueOfInput + step, spinner.advanced().getInput()
                .getIntValue());

            currentValueOfInput = spinner.advanced().getInput().getIntValue();
        }

        spinner.increase();

        currentValueOfInput = spinner.advanced().getInput().getIntValue();

        assertEquals("The current value in the input should be minimal, so " + MIN_VALUE, MIN_VALUE, currentValueOfInput);
    }

    private void setAndDecrease(RichFacesInputNumberSpinner spinner, int step) {
        spinner.advanced().getInput().clear();
        spinner.advanced().getInput().sendKeys("30");
        spinner.advanced().getInput().advanced().trigger("blur");
        spinner.decrease();
        assertEquals(30 - step, spinner.advanced().getInput().getIntValue());
    }

}

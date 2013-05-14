/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richfaces.tests.showcase.progressBar;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.richfaces.tests.showcase.AbstractWebDriverTest;

/**
 * 
 * @author pmensik
 */
public class AbstractProgressBarTest extends AbstractWebDriverTest {

    protected List<Integer> numbersOfProcess = new ArrayList<Integer>();

    protected void getTheNumberFromValueAndSaveToList(String value) {
        int number = Integer.valueOf(value.trim().substring(0, value.length() - 2));// trims the % sign
        if (!numbersOfProcess.contains(number)) {
            numbersOfProcess.add(number);
        }
    }

    protected void checkTheDeviationInList(int maxDeviation) {
        for (int i = 0; i < numbersOfProcess.size() - 1; i++) {
            assertTrue("The deviation between each step in the progress should not be higher than " + maxDeviation,
                (numbersOfProcess.get(i + 1) - numbersOfProcess.get(i)) <= maxDeviation);
        }
    }
}

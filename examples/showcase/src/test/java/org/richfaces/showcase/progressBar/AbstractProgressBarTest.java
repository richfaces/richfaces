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
package org.richfaces.showcase.progressBar;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.richfaces.showcase.AbstractWebDriverTest;

/**
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

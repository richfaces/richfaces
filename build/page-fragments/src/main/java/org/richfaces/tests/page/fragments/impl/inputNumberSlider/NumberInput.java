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
package org.richfaces.tests.page.fragments.impl.inputNumberSlider;

public interface NumberInput {

    /**
     * Increases the current value of this number input by the value of the step.
     *
     * @throws RuntimeException if there is no handler for increasing the value (for example arrow)
     */
    void increase();

    /**
     * Decreases the current value of this number input by the value of the step.
     *
     * @throws RuntimeException if there is no handler for decreasing the value (for example arrow)
     */
    void decrease();

    /**
     * Increases the current value of this number input by the value of the step <code>n</code> times.
     *
     * In other words, if the step is 2, and given param <code>n</code> is 3, the input value is increased by 6.
     *
     * @param n how many times the increasing should be executed
     * @throws RuntimeException if there is no handler for increasing the value (for example arrow)
     */
    void increase(int n);

    /**
     * Dcreases the current value of this number input by the value of the step <code>n</code> times.
     *
     * In other words, if the step is 2, and given param <code>n</code> is 3, the input value is decreased by 6.
     *
     * @param n how many times the decreasing should be executed
     * @throws RuntimeException if there is no handler for decreasing the value (for example arrow)
     */
    void decrease(int n);

    /**
     * Sets the exact number value to the input.
     *
     * @param value value to be set
     */
    void setValue(double value);

    /**
     * Gets the current number value of this number input.
     *
     * @return the current value of the number input
     */
    double getValue();
}
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

package org.richfaces.demo.validator;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotEmpty;

@ManagedBean(name = "test")
@RequestScoped
public class Bean {
    public static final String FOO_VALUE = "fooValue";
    private String value = FOO_VALUE;
    private String required;
    private int intValue;
    private long longValue;
    private double doubleValue;
    private String email;
    private String numbers;

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @return the required
     */
    public String getRequired() {
        return this.required;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param required the required to set
     */
    public void setRequired(String required) {
        this.required = required;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @return the intValue
     */
    public int getIntValue() {
        return this.intValue;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param intValue the intValue to set
     */
    public void setIntValue(int intValue) {
        this.intValue = intValue;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @return the longValue
     */
    public long getLongValue() {
        return this.longValue;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param longValue the longValue to set
     */
    public void setLongValue(long longValue) {
        this.longValue = longValue;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @return the doubleValue
     */
    public double getDoubleValue() {
        return this.doubleValue;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param doubleValue the doubleValue to set
     */
    public void setDoubleValue(double doubleValue) {
        this.doubleValue = doubleValue;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @return the email
     */
    public String getEmail() {
        return this.email;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    @NotNull
    @NotEmpty
    @Digits(fraction = 0, integer = 2)
    @Pattern(regexp = "^[0-9-]+$", message = "must contain only numbers")
    public String getNumbers() {
        return this.numbers;
    }

    public void setNumbers(String numbers) {
        this.numbers = numbers;
    }
}

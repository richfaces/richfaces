package org.richfaces.example;

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

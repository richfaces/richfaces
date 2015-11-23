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
package org.richfaces.showcase.message.page;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class AbstractMessagePage {

    protected static final int MINIMUM_OF_NAME = 3;
    protected static final int MINIMUM_OF_JOB = 3;
    protected static final int MINIMUM_OF_ADDRESS = 10;
    protected static final int MINIMUM_OF_ZIP = 4;

    protected static final int MAXIMUM_OF_JOB = 50;
    protected static final int MAXIMUM_OF_ZIP = 9;

    public static final String NAME_ERROR_VALUE_REQUIRED = "Name: Validation Error: Value is required.";
    public static final String JOB_ERROR__VALUE_REQUIRED = "Job: Validation Error: Value is required.";
    public static final String ADDRESS_ERROR__VALUE_REQUIRED = "Address: Validation Error: Value is required.";
    public static final String ZIP_ERROR__VALUE_REQUIRED = "Zip: Validation Error: Value is required.";

    public static final String NAME_ERROR_LESS_THAN_MINIMUM = "Name: Validation Error: Length is less than allowable minimum of '"
        + MINIMUM_OF_NAME + "'";
    public static final String JOB_ERROR_LESS_THAN_MINIMUM = "Job: Validation Error: Length is less than allowable minimum of '"
        + MINIMUM_OF_JOB + "'";
    public static final String ADDRESS_ERROR_LESS_THAN_MINIMUM = "Address: Validation Error: Length is less than allowable minimum of '"
        + MINIMUM_OF_ADDRESS + "'";
    public static final String ZIP_ERROR_LESS_THAN_MINIMUM = "Zip: Validation Error: Length is less than allowable minimum of '"
        + MINIMUM_OF_ZIP + "'";

    public static final String JOB_ERROR_GREATER_THAN_MAXIMUM = "Job: Validation Error: Length is greater than allowable maximum of '"
        + MAXIMUM_OF_JOB + "'";
    public static final String ZIP_ERROR_GREATER_THAN_MAXIMUM = "Zip: Validation Error: Length is greater than allowable maximum of '"
        + MAXIMUM_OF_ZIP + "'";
    public static final String JOB_ERROR_NOT_BETWEEN = "Job: Validation Error: Specified attribute is not between the expected values of 3 and 50.";
    public static final String ZIP_ERROR_NOT_BETWEEN = "Zip: Validation Error: Specified attribute is not between the expected values of 4 and 9.";

    @FindByJQuery("input[id$=name]")
    private WebElement nameInput;

    @FindByJQuery("input[id$=job]")
    private WebElement jobInput;

    @FindByJQuery("input[id$=address]")
    private WebElement addressInput;

    @FindByJQuery("input[id$=zip]")
    private WebElement zipInput;

    @FindBy(css = "input[type=submit]")
    protected WebElement ajaxValidateButton;

    public void fillLongerJob() {
        fillInputWithStringOfLength(jobInput, MAXIMUM_OF_JOB + 1);
    }

    public void fillLongerZip() {
        fillInputWithStringOfLength(zipInput, MAXIMUM_OF_ZIP + 1);
    }

    public void fillShorterValues() {
        fillInputWithStringOfLength(nameInput, MINIMUM_OF_NAME - 1);
        fillInputWithStringOfLength(jobInput, MINIMUM_OF_JOB - 1);
        fillInputWithStringOfLength(addressInput, MINIMUM_OF_ADDRESS - 1);
        fillInputWithStringOfLength(zipInput, MINIMUM_OF_ZIP - 1);
    }

    public void fillShorterValueName() {
        fillInputWithStringOfLength(nameInput, MINIMUM_OF_NAME - 1);
    }

    public void fillShorterValueJob() {
        fillInputWithStringOfLength(jobInput, MINIMUM_OF_NAME - 1);
    }

    public void fillShorterValueAddress() {
        fillInputWithStringOfLength(addressInput, MINIMUM_OF_NAME - 1);
    }

    public void fillShorterValueZip() {
        fillInputWithStringOfLength(zipInput, MINIMUM_OF_NAME - 1);
    }

    public void fillCorrectValues() {
        fillInputWithStringOfLength(nameInput, MINIMUM_OF_NAME);
        fillInputWithStringOfLength(jobInput, MINIMUM_OF_JOB);
        fillInputWithStringOfLength(addressInput, MINIMUM_OF_ADDRESS);
        fillInputWithStringOfLength(zipInput, MINIMUM_OF_ZIP);
    }

    public void eraseAll() {
        for (WebElement input : allInputs()) {
            input.click();
            input.clear();
        }
    }

    protected void fillInputWithStringOfLength(WebElement input, int length) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            builder.append("x");
        }
        input.click();
        input.clear();
        input.sendKeys(builder.toString());
    }

    public void validate() {
        Graphene.guardAjax(ajaxValidateButton).click();
    }

    public void blur() {
        nameInput.click();
        jobInput.click();
    }

    protected WebElement[] allInputs() {
        return new WebElement[] { nameInput, jobInput, addressInput, zipInput };
    }

}

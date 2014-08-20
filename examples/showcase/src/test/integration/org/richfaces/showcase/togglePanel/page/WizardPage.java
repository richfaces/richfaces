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
package org.richfaces.showcase.togglePanel.page;

import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * @author <a href="mailto:pmensik@redhat.com">Petr Mensik</a>
 */
public class WizardPage {

    @FindByJQuery("input[type='text']:eq(0)")
    private WebElement firstNameInput;

    @FindByJQuery("input[type='text']:eq(1)")
    private WebElement lastNameInput;

    @FindByJQuery("input[type='text']:eq(2)")
    private WebElement companyInput;

    @FindBy(tagName = "textarea")
    private WebElement notesInput;

    @FindByJQuery("div[class*='rf-p wizard'] div[class*='rf-tgp'] table:visible tbody:visible")
    private WebElement summaryOfAllSteps;

    @FindByJQuery("input[value*='Next']:visible")
    private WebElement nextButton;

    @FindByJQuery("input[value*='Previous']:visible")
    private WebElement previousButton;

    @FindByJQuery("span[class='rf-msg-det']:contains('First Name: Validation Error'):visible")
    private WebElement errorMessageFirstName;

    @FindByJQuery("span[class='rf-msg-det']:contains('Last Name: Validation Error'):visible")
    private WebElement errorMessageLastName;

    @FindByJQuery("span[class='rf-msg-det']:contains('Company: Validation Error'):visible")
    private WebElement errorMessageCompany;

    @FindByJQuery("span[class='rf-msg-det']:contains('Notes: Validation Error: Value is required.'):visible")
    private WebElement errorMessageNotes;

    @FindByJQuery("span[class='rf-msg-det']:contains('Validation Error'):visible")
    private WebElement errorMessage;

    public WebElement getFirstNameInput() {
        return firstNameInput;
    }

    public WebElement getLastNameInput() {
        return lastNameInput;
    }

    public WebElement getCompanyInput() {
        return companyInput;
    }

    public WebElement getNotesInput() {
        return notesInput;
    }

    public WebElement getSummaryOfAllSteps() {
        return summaryOfAllSteps;
    }

    public WebElement getNextButton() {
        return nextButton;
    }

    public WebElement getPreviousButton() {
        return previousButton;
    }

    public WebElement getErrorMessageFirstName() {
        return errorMessageFirstName;
    }

    public WebElement getErrorMessageLastName() {
        return errorMessageLastName;
    }

    public WebElement getErrorMessageCompany() {
        return errorMessageCompany;
    }

    public WebElement getErrorMessageNotes() {
        return errorMessageNotes;
    }

    public WebElement getErrorMessage() {
        return errorMessage;
    }

}

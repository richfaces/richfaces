/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richfaces.tests.showcase.togglePanel.page;

import org.jboss.arquillian.graphene.enricher.findby.FindBy;
import org.openqa.selenium.WebElement;

/**
 *
 * @author <a href="mailto:pmensik@redhat.com">Petr Mensik</a>
 */
public class WizardPage {

    @FindBy(jquery = "input[type='text']:eq(0)")
    public WebElement firstNameInput;

    @FindBy(jquery = "input[type='text']:eq(1)")
    public WebElement lastNameInput;

    @FindBy(jquery = "input[type='text']:eq(2)")
    public WebElement companyInput;

    @FindBy(tagName = "textarea")
    public WebElement notesInput;

    @FindBy(jquery = "div[class*='rf-p wizard'] div[class*='rf-tgp'] table:visible tbody:visible")
    public WebElement summaryOfAllSteps;

    @FindBy(jquery = "input[value*='Next']:visible")
    public WebElement nextButton;

    @FindBy(jquery = "input[value*='Previous']:visible")
    public WebElement previousButton;

    @FindBy(jquery = "span[class='rf-msg-det']:contains('First Name: Validation Error'):visible")
    public WebElement errorMessageFirstName;

    @FindBy(jquery = "span[class='rf-msg-det']:contains('Last Name: Validation Error'):visible")
    public WebElement errorMessageLastName;

    @FindBy(jquery = "span[class='rf-msg-det']:contains('Company: Validation Error'):visible")
    public WebElement errorMessageCompany;

    @FindBy(jquery = "span[class='rf-msg-det']:contains('Notes: Validation Error: Value is required.'):visible")
    public WebElement errorMessageNotes;

    @FindBy(jquery = "span[class='rf-msg-det']:contains('Validation Error'):visible")
    public WebElement errorMessage;

}

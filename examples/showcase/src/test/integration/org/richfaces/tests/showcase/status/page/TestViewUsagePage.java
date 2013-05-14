/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richfaces.tests.showcase.status.page;

import org.jboss.arquillian.graphene.enricher.findby.FindBy;
import org.openqa.selenium.WebElement;

/**
 *
 * @author pmensik
 */
public class TestViewUsagePage {

    @FindBy(jquery = "input[type='text']:eq(0)")
    public WebElement userNameInput;

    @FindBy(jquery = "input[type='text']:eq(1)")
    public WebElement addressInput;

    @FindBy(jquery = "input[type='button']:eq(0)")
    public WebElement submitButton;

    @FindBy(jquery = "input[type='button']:eq(1)")
    public WebElement searchButton;

    @FindBy(jquery = "span[class='rf-st-start'] img")
    public WebElement progressImage;
}

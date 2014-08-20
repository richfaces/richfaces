/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richfaces.showcase.functions.page;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 *
 * @author pmensik
 */
public class FunctionsPage {

    @FindBy(css = "input[id$='input']")
    private WebElement input;

    @FindBy(css = "span[id$='out']")
    private WebElement output;

    public WebElement getInput() {
        return input;
    }

    public WebElement getOutput() {
        return output;
    }

}

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
    public WebElement input;

    @FindBy(css = "span[id$='out']")
    public WebElement output;

}

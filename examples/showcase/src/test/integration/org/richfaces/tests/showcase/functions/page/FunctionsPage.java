/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richfaces.tests.showcase.functions.page;

import org.jboss.arquillian.graphene.enricher.findby.FindBy;
import org.openqa.selenium.WebElement;

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

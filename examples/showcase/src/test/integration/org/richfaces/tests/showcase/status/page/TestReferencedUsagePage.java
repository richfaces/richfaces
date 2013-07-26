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
public class TestReferencedUsagePage {

    @FindBy(jquery = "input[type='text']:eq(0)")
    public WebElement userNameInput;

    @FindBy(jquery = "input[type='text']:eq(1)")
    public WebElement addressInput;

    @FindBy(jquery = "span[class=rf-st-start] img:first")
    public WebElement firstAjaxRequestProgressImage;

    @FindBy(jquery = "span[class=rf-st-start] img:last")
    public WebElement secondAjaxRequestProgressImage;
}

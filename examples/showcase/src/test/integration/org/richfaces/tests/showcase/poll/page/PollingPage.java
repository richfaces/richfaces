/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richfaces.tests.showcase.poll.page;

import org.jboss.arquillian.graphene.enricher.findby.FindBy;
import org.openqa.selenium.WebElement;

/**
 *
 * @author pmensik
 */
public class PollingPage {

    @FindBy(jquery = "span[id$='serverDate']")
    public WebElement serverDate;

    @FindBy(jquery = "input[type='submit']:eq(0)")
    public WebElement stopButton;
}

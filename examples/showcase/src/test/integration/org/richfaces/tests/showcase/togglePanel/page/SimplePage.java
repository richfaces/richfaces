/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richfaces.tests.showcase.togglePanel.page;

import org.jboss.arquillian.graphene.enricher.findby.FindBy;
import org.openqa.selenium.WebElement;

/**
 *
 * @author pmensik
 */
public class SimplePage {

    @FindBy(jquery = "div[id$='tabs'] > div:eq(0)")
    public WebElement toggleTab1;

    @FindBy(jquery = "div[id$='tabs'] > div:eq(1)")
    public WebElement toggleTab2;

    @FindBy(jquery = "div[class='rf-tgp-itm']:visible")
    public WebElement panelContent;
}

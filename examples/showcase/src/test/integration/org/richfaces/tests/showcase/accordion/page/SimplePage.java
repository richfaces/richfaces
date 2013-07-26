/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richfaces.tests.showcase.accordion.page;

import org.jboss.arquillian.graphene.enricher.findby.FindBy;
import org.openqa.selenium.WebElement;

/**
 *
 * @author pmensik
 */
public class SimplePage {

    @FindBy(jquery = "div[class*='rf-ac-itm-hdr']:eq(0)")
    public WebElement firstPanel;

    @FindBy(jquery = "div[class*='rf-ac-itm-hdr']:eq(1)")
    public WebElement secondPanel;

    @FindBy(jquery = "div[class='rf-ac-itm-cnt']:eq(0)")
    public WebElement firstPanelContent;

    @FindBy(jquery = "div[class='rf-ac-itm-cnt']:eq(1)")
    public WebElement secondPanelContent;
}

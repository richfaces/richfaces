package org.richfaces.tests.showcase.collapsiblePanel.page;

import org.jboss.arquillian.graphene.enricher.findby.FindBy;
import org.openqa.selenium.WebElement;

/**
 *
 * @author pmensik
 */
public class SimplePage {

    @FindBy(jquery = "div[class*='rf-cp-hdr']:eq(0)")
    public WebElement firstPanel;

    @FindBy(jquery = "div[class*='rf-cp-hdr']:eq(1)")
    public WebElement secondPanel;

    @FindBy(jquery = "div[class='rf-cp-b']:eq(0)")
    public WebElement firstPanelContent;

    @FindBy(jquery = "div[class='rf-cp-b']:eq(1)")
    public WebElement secondPanelContent;
}

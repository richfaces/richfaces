/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richfaces.showcase.collapsiblePanel.page;

import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.openqa.selenium.WebElement;

/**
 *
 * @author pmensik
 */
public class SimplePage {

    @FindByJQuery("div[class*='rf-cp-hdr']:eq(0)")
    private WebElement firstPanel;

    @FindByJQuery("div[class*='rf-cp-hdr']:eq(1)")
    private WebElement secondPanel;

    @FindByJQuery("div[class='rf-cp-b']:eq(0)")
    private WebElement firstPanelContent;

    @FindByJQuery("div[class='rf-cp-b']:eq(1)")
    private WebElement secondPanelContent;

    public WebElement getFirstPanel() {
        return firstPanel;
    }

    public WebElement getSecondPanel() {
        return secondPanel;
    }

    public WebElement getFirstPanelContent() {
        return firstPanelContent;
    }

    public WebElement getSecondPanelContent() {
        return secondPanelContent;
    }

}

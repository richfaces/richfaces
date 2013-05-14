/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richfaces.tests.showcase.popup.page;

import org.jboss.arquillian.graphene.enricher.findby.FindBy;
import org.openqa.selenium.WebElement;

/**
 *
 * @author pmensik
 */
public class PopupPage {

    @FindBy(jquery = "input[type='submit']:eq(0)")
    public WebElement callthePopupButton;

    @FindBy(jquery = "td[class*='gutter']:visible")
    public WebElement sourceOfPage;

    @FindBy(css = "a[class*='show']")
    public WebElement anchorOfSource;

    @FindBy(jquery = "div[id$='popup_content']")
    public WebElement popupPanelContent;
}

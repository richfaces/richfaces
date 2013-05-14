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
public class LoginPage {

    @FindBy(jquery = "td[id$='ll_itm'] a:contains('Login')")
    public WebElement loginOnToolbar;

    @FindBy(jquery = "div[id$='lp_content'] a:contains('Login')")
    public WebElement loginOnPopup;

    @FindBy(jquery = "a:contains('Search'):eq(0)")
    public WebElement searchOnToolbar;

    @FindBy(jquery = "a:contains('Search'):eq(1)")
    public WebElement searchOnPopup;
}

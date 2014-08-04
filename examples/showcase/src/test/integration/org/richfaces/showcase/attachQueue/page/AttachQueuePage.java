/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richfaces.showcase.attachQueue.page;

import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 *
 * @author <a href="mailto:pmensik@redhat.com">Petr Mensik</a>
 */
public class AttachQueuePage {

    @FindByJQuery("input[type='text']:visible")
    public WebElement input;

    @FindByJQuery("input[type='submit']:eq(0)")
    public WebElement submit;

    @FindBy(css = "span[class*='rf-st-start']")
    public WebElement ajaxRequestProcessing;
}

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
    private WebElement input;

    @FindByJQuery("input[type='submit']:eq(0)")
    private WebElement submit;

    @FindBy(css = "span[class*='rf-st-start']")
    private WebElement ajaxRequestProcessing;

    public WebElement getInput() {
        return input;
    }

    public WebElement getSubmit() {
        return submit;
    }

    public WebElement getAjaxRequestProcessing() {
        return ajaxRequestProcessing;
    }

}

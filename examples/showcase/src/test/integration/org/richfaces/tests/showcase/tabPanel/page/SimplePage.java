/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richfaces.tests.showcase.tabPanel.page;

import org.jboss.arquillian.graphene.enricher.findby.FindBy;
import org.openqa.selenium.WebElement;

/**
 *
 * @author <a href="mailto:pmensik@redhat.com">Petr Mensik</a>
 */
public class SimplePage {

    @FindBy(jquery = "div[class='rf-tab']:visible")
    public WebElement panelBody;

    @FindBy(jquery = "fieldset[class='example-cnt'] td[class*=rf-tab-hdr]:visible:eq(1)")
    public WebElement firstTabButton;

    @FindBy(jquery = "fieldset[class='example-cnt'] td[class*=rf-tab-hdr]:visible:eq(3)")
    public WebElement secondTabButton;
}

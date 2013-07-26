/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richfaces.tests.showcase.status;

import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.WebElement;
import org.richfaces.tests.showcase.AbstractWebDriverTest;

/**
 *
 * @author pmensik
 */
public abstract class ITUsage extends AbstractWebDriverTest {

    protected void assertProgressPictureAppearsOnAjaxRequest(WebElement progressImage) {
        Graphene.waitAjax(webDriver).until("There should be an image of ajax request progress!")
                .element(progressImage)
                .is()
                .visible();
        Graphene.waitGui(webDriver).until("There can not be image of ajax request, since it is completed!")
                .element(progressImage)
                .is()
                .not()
                .visible();
    }
}

package org.richfaces.tests.showcase.push.page;

import org.jboss.arquillian.graphene.enricher.findby.FindBy;
import org.openqa.selenium.WebElement;

public class JMSPushPage {

    @FindBy(jquery = "div[id*='serverDate']")
    private WebElement serverDate;

    public WebElement getServerDate() {
        return serverDate;
    }
}

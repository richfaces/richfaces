package org.richfaces.component.autocomplete;

import static org.jboss.arquillian.graphene.Graphene.waitGui;

import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.integration.UIDeployment;
import org.richfaces.shrinkwrap.descriptor.FaceletAsset;

@RunAsClient
@RunWith(Arquillian.class)
public class ITAutocompleteEvents {

    @Drone
    WebDriver browser;

    @ArquillianResource
    URL contextPath;

    @FindBy(id = "form:render")
    WebElement renderButton;

    @FindBy(css = "input.rf-au-inp")
    WebElement autocompleteInput;

    @FindBy(css = ".rf-au-itm")
    WebElement autocompleteItem;

    @FindBy(css = "body")
    WebElement body;

    By suggestionList = By.cssSelector(".rf-au-lst-cord");

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        UIDeployment deployment = new UIDeployment(ITAutocompleteEvents.class);

        deployment.archive().addClasses(AutocompleteBean.class).addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");

        addIndexPage(deployment);

        return deployment.getFinalArchive();
    }

    /**
     * onblur should have input value available via 'this.value' expression
     */
    @Test
    // RF-12605
    public void testOnblurEventPayload() {
        // given
        browser.get(contextPath.toExternalForm());
        autocompleteInput.sendKeys("t");
        waitGui().withMessage("suggestion list is visible").until().element(suggestionList).is().visible();
        autocompleteItem.click();

        // when
        body.click();

        // then
        waitGui().until().element(autocompleteInput).attribute("value").equalTo("TORONTO");
    }

    private static void addIndexPage(UIDeployment deployment) {
        FaceletAsset p = new FaceletAsset();

        p.body("<h:form id='form'>");
        p.body("    <rich:autocomplete autocompleteList='#{autocompleteBean.suggestions}'");
        p.body("                       onblur='this.value = this.value.toUpperCase()'  />");
        p.body("</h:form>");

        deployment.archive().addAsWebResource(p, "index.xhtml");
    }
}

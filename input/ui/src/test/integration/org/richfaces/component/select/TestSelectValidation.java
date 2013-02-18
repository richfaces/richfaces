package org.richfaces.component.select;

import static org.jboss.arquillian.graphene.Graphene.guardXhr;
import static org.junit.Assert.assertTrue;

import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.enricher.findby.FindBy;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.richfaces.component.autocomplete.AutocompleteBean;
import org.richfaces.integration.InputDeployment;
import org.richfaces.shrinkwrap.descriptor.FaceletAsset;

@RunAsClient
@RunWith(Arquillian.class)
public class TestSelectValidation {

    @Drone
    private WebDriver browser;

    @ArquillianResource
    private URL contextPath;

    @FindBy(className = "rf-sel-inp")
    private WebElement selectInput;

    @FindBy(id = "submit")
    private WebElement submit;

    @FindBy(id="message")
    private WebElement message;

    @Deployment
    public static WebArchive createDeployment() {
        InputDeployment deployment = new InputDeployment(TestSelectValidation.class);

        deployment.archive().addClasses(AutocompleteBean.class).addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");

        addIndexPage(deployment);

        return deployment.getFinalArchive();
    }

    @Test
    public void test() {
        browser.get(contextPath.toExternalForm());

        selectInput.sendKeys("invalid");

        guardXhr(submit).click();

        assertTrue("contains invalid message", message.getText().contains("Value is not valid"));
    }

    private static void addIndexPage(InputDeployment deployment) {
        FaceletAsset p = new FaceletAsset();
        p.xmlns("a4j", "http://richfaces.org/a4j");
        p.xmlns("rich", "http://richfaces.org/input");

        p.form("<rich:select id='select'  enableManualInput='true'>");
        p.form("    <f:selectItems value='#{autocompleteBean.suggestions}' />");
        p.form("</rich:select>");

        p.form("<a4j:commandButton id='submit' />");

        p.form("<a4j:outputPanel ajaxRendered='true'>");
        p.form("    <h:message id='message' for='select' />");
        p.form("</a4j:outputPanel>");

        deployment.archive().addAsWebResource(p, "index.xhtml");
    }
}

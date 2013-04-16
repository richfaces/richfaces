package org.richfaces.ui.select;

import static org.jboss.arquillian.graphene.Graphene.guardAjax;
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
import org.richfaces.deployment.FrameworkDeployment;
import org.richfaces.shrinkwrap.descriptor.FaceletAsset;
import org.richfaces.ui.autocomplete.AutocompleteBean;

@RunAsClient
@RunWith(Arquillian.class)
public class ITSelectValidation {

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
        FrameworkDeployment deployment = new FrameworkDeployment(ITSelectValidation.class);

        deployment.archive().addClasses(AutocompleteBean.class).addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");

        addIndexPage(deployment);

        return deployment.getFinalArchive();
    }

    @Test
    public void test() {
        browser.get(contextPath.toExternalForm());

        selectInput.sendKeys("invalid");

        guardAjax(submit).click();

        assertTrue("contains invalid message", message.getText().contains("Value is not valid"));
    }

    private static void addIndexPage(FrameworkDeployment deployment) {
        FaceletAsset p = new FaceletAsset();

        p.form("<r:select id='select'  enableManualInput='true'>");
        p.form("    <f:selectItems value='#{autocompleteBean.suggestions}' />");
        p.form("</r:select>");

        p.form("<r:commandButton id='submit' />");

        p.form("<r:outputPanel ajaxRendered='true'>");
        p.form("    <h:message id='message' for='select' />");
        p.form("</r:outputPanel>");

        deployment.archive().addAsWebResource(p, "index.xhtml");
    }
}

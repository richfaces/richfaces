package org.richfaces.component.select;

import static org.jboss.arquillian.graphene.Graphene.guardAjax;
import static org.junit.Assert.assertTrue;

import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.component.autocomplete.AutocompleteBean;
import org.richfaces.integration.RichDeployment;
import org.richfaces.shrinkwrap.descriptor.FaceletAsset;

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

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        RichDeployment deployment = new RichDeployment(ITSelectValidation.class);

        deployment.archive().addClasses(AutocompleteBean.class);

        addIndexPage(deployment);

        deployment.addHibernateValidatorWhenUsingServletContainer();

        return deployment.getFinalArchive();
    }

    @Test
    public void test() {
        browser.get(contextPath.toExternalForm());

        selectInput.sendKeys("invalid");

        guardAjax(submit).click();

        assertTrue("contains invalid message", message.getText().contains("Value is not valid"));
    }

    private static void addIndexPage(RichDeployment deployment) {
        FaceletAsset p = new FaceletAsset();

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

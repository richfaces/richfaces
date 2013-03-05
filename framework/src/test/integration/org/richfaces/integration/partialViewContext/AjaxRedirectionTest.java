package org.richfaces.integration.partialViewContext;

import static org.jboss.arquillian.graphene.Graphene.waitAjax;

import java.net.URL;

import javax.faces.context.PartialResponseWriter;

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
import org.richfaces.integration.CoreDeployment;
import org.richfaces.shrinkwrap.descriptor.FaceletAsset;

/**
 * Tests that the {@link PartialResponseWriter#redirect(String)} writes partial-response correctly
 * for redirected requests (RF-12824)
 */
@RunAsClient
@RunWith(Arquillian.class)
public class AjaxRedirectionTest {

    @Drone
    private WebDriver browser;

    @ArquillianResource
    private URL contextPath;

    @FindBy(id = "button")
    private WebElement button;

    @FindBy(tagName = "body")
    private WebElement body;

    @Deployment
    public static WebArchive createDeployment() {
        CoreDeployment deployment = new CoreDeployment(AjaxRedirectionTest.class);

        deployment.withWholeCore();

        addIndexPage(deployment);
        addRedirectedPage(deployment);

        return deployment.getFinalArchive();
    }

    @Test
    public void test() {
        browser.get(contextPath.toExternalForm());

        button.click();

        waitAjax().until().element(body).text().contains("Redirected");
    }

    private static void addIndexPage(CoreDeployment deployment) {
        FaceletAsset p = new FaceletAsset();

        p.head("<h:outputScript name='jsf.js' library='javax.faces' />");
        p.head("<h:outputScript name='jquery.js' />");
        p.head("<h:outputScript name='richfaces.js' />");

        p.form("<h:panelGroup id='panel'>");
        p.form("    <h:commandButton id='button' action='redirected?faces-redirect=true'>");
        p.form("        <f:ajax />");
        p.form("    </h:commandButton>");
        p.form("</h:panelGroup>");

        deployment.archive().addAsWebResource(p, "index.xhtml");
    }

    private static void addRedirectedPage(CoreDeployment deployment) {
        FaceletAsset p = new FaceletAsset();

        p.body("Redirected");

        deployment.archive().addAsWebResource(p, "redirected.xhtml");
    }
}

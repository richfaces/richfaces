package org.richfaces.integration;

import static org.junit.Assert.assertEquals;

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
import org.richfaces.deployment.CoreDeployment;
import org.richfaces.shrinkwrap.descriptor.FaceletAsset;

@RunWith(Arquillian.class)
@RunAsClient
public class ITTestBase {

    @Drone
    private WebDriver browser;

    @ArquillianResource
    private URL contextPath;

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        CoreDeployment deployment = new CoreDeployment(ITTestBase.class);
        addIndexPage(deployment);

        return deployment.getFinalArchive();
    }

    @FindBy
    private WebElement input;

    @Test
    public void testBasicDeployment() throws InterruptedException {
        browser.get(contextPath.toExternalForm());

        assertEquals("xyz", input.getAttribute("value"));
    }

    private static void addIndexPage(CoreDeployment deployment) {
        FaceletAsset p = new FaceletAsset();

        p.form("<h:inputText id='input' value='xyz'><a4j:ajax /></h:inputText>");

        deployment.archive().addAsWebResource(p, "index.xhtml");
    }
}

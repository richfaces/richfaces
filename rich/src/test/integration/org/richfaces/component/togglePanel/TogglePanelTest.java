package org.richfaces.component.togglePanel;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.integration.OutputDeployment;
import org.richfaces.shrinkwrap.descriptor.FaceletAsset;

import java.net.URL;

import static org.jboss.arquillian.graphene.Graphene.guardXhr;

@RunAsClient
@RunWith(Arquillian.class)
public class TogglePanelTest {

    @Drone
    private WebDriver browser;

    @ArquillianResource
    private URL contextPath;

    @FindBy(id = "myForm:fieldsPanel")
    private WebElement togglePanel;

    @FindBy(className = "submit")
    private WebElement button;

    @Deployment
    public static WebArchive createDeployment() {
        OutputDeployment deployment = new OutputDeployment(TogglePanelTest.class);
        addIndexPage(deployment);

        WebArchive archive = deployment.getFinalArchive();
        return archive;
    }

    @Test
    public void check_toggle_panel() {
        browser.get(contextPath.toExternalForm() + "index.jsf");
        guardXhr(button).click();
    }

    private static void addIndexPage(OutputDeployment deployment) {
        FaceletAsset p = new FaceletAsset();
        p.xmlns("rich", "http://richfaces.org/output");
        p.xmlns("a4j", "http://richfaces.org/a4j");
        p.xmlns("c", "http://java.sun.com/jsp/jstl/core");

        p.body("<h:form id='myForm'>");
        p.body("<rich:togglePanel id='fieldsPanel' activeItem='closed'> ");
        p.body("    <rich:togglePanelItem name='closed'> ");
        p.body("        <h:commandButton value='Show fields' styleClass='submit'> ");
        p.body("            <rich:toggleControl targetItem='checkboxes' /> ");
        p.body("        </h:commandButton> ");
        p.body("    </rich:togglePanelItem> ");
        p.body("    <rich:togglePanelItem name='checkboxes'> ");
        p.body("        Lorem ipsum dolor sit amet, ... ");
        p.body("    </rich:togglePanelItem> ");
        p.body("</rich:togglePanel> ");
        p.body("</h:form>");

        deployment.archive().addAsWebResource(p, "index.xhtml");
    }

}

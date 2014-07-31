package org.richfaces.component.togglePanel;

import static org.jboss.arquillian.graphene.Graphene.guardAjax;

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
import org.richfaces.integration.RichDeployment;
import org.richfaces.shrinkwrap.descriptor.FaceletAsset;

@RunAsClient
@RunWith(Arquillian.class)
public class ITTogglePanel {

    @Drone
    private WebDriver browser;

    @ArquillianResource
    private URL contextPath;

    @FindBy(id = "myForm:fieldsPanel")
    private WebElement togglePanel;

    @FindBy(className = "submit")
    private WebElement button;

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        RichDeployment deployment = new RichDeployment(ITTogglePanel.class);
        addIndexPage(deployment);

        WebArchive archive = deployment.getFinalArchive();
        return archive;
    }

    @Test
    public void check_toggle_panel() {
        browser.get(contextPath.toExternalForm() + "index.jsf");
        guardAjax(button).click();
    }

    private static void addIndexPage(RichDeployment deployment) {
        FaceletAsset p = new FaceletAsset();

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

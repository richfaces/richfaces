package org.richfaces.ui.togglePanel;

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
import org.richfaces.deployment.FrameworkDeployment;
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

    @Deployment
    public static WebArchive createDeployment() {
        FrameworkDeployment deployment = new FrameworkDeployment(ITTogglePanel.class);
        addIndexPage(deployment);

        WebArchive archive = deployment.getFinalArchive();
        return archive;
    }

    @Test
    public void check_toggle_panel() {
        browser.get(contextPath.toExternalForm() + "index.jsf");
        guardAjax(button).click();
    }

    private static void addIndexPage(FrameworkDeployment deployment) {
        FaceletAsset p = new FaceletAsset();

        p.body("<r:log mode='console' level='DEBUG' />");
        p.body("<ui:debug hotkey='0' />");

        p.body("<h:form id='myForm'>");
        p.body("<r:togglePanel id='fieldsPanel' activeItem='closed'> ");
        p.body("    <r:togglePanelItem name='closed'> ");
        p.body("        <h:commandButton value='Show fields' styleClass='submit'> ");
        p.body("            <r:toggleControl targetItem='checkboxes' /> ");
        p.body("        </h:commandButton> ");
        p.body("    </r:togglePanelItem> ");
        p.body("    <r:togglePanelItem name='checkboxes'> ");
        p.body("        Lorem ipsum dolor sit amet, ... ");
        p.body("    </r:togglePanelItem> ");
        p.body("</r:togglePanel> ");
        p.body("</h:form>");

        deployment.archive().addAsWebResource(p, "index.xhtml");
    }

}

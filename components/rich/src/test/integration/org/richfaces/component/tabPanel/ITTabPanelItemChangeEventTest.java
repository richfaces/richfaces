package org.richfaces.component.tabPanel;

import static java.lang.Boolean.parseBoolean;

import static org.jboss.arquillian.graphene.Graphene.guardAjax;
import static org.junit.Assert.assertFalse;
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
import org.richfaces.component.tabPanel.model.TabPanelItemChangeEventBean;
import org.richfaces.fragment.tabPanel.RichFacesTabPanel;
import org.richfaces.integration.RichDeployment;
import org.richfaces.shrinkwrap.descriptor.FaceletAsset;

@RunAsClient
@RunWith(Arquillian.class)
public class ITTabPanelItemChangeEventTest {

    @Drone
    private WebDriver browser;
    @ArquillianResource
    private URL contextPath;
    @FindBy(css = "[id$='invoked']")
    private WebElement invokedElement;
    @FindBy(css = "[id$='reset']")
    private WebElement resetButton;
    @FindBy(css = "[id$='tabPanel']")
    private RichFacesTabPanel tabPanel;

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        RichDeployment deployment = new RichDeployment(ITTabPanelItemChangeEventTest.class);

        deployment.archive().addClasses(TabPanelItemChangeEventBean.class);

        addIndexPage(deployment);

        WebArchive archive = deployment.getFinalArchive();
        return archive;
    }

    @Test
    public void test() {
        browser.get(contextPath.toExternalForm());

        assertFalse(isInvoked());

        guardAjax(tabPanel).switchTo(1);
        assertTrue(isInvoked());

        // reset invocation
        guardAjax(resetButton).click();
        assertFalse(isInvoked());

        guardAjax(tabPanel).switchTo(0);
        assertTrue(isInvoked());

        // reset invocation
        guardAjax(resetButton).click();
        assertFalse(isInvoked());

        // switch to the same element
        guardAjax(tabPanel).switchTo(0);
        assertFalse(isInvoked());
    }

    private boolean isInvoked() {
        return parseBoolean(invokedElement.getText());
    }

    private static void addIndexPage(RichDeployment deployment) {
        FaceletAsset p = new FaceletAsset();
        p.body("<h:form id='myForm'>");
        p.body("    <rich:tabPanel id='tabPanel' binding='#{tabPanelItemChangeEventBean.tabPanel}' itemChangeListener='#{tabPanelItemChangeEventBean.itemChangeListener}'>");
        p.body("        <rich:tab id='tab0' name='tab0' header='tab0 header' render='invoked'>");
        p.body("            content of tab 1");
        p.body("        </rich:tab>");
        p.body("        <rich:tab id='tab1' name='tab1' header='tab1 header' render='invoked'>");
        p.body("            content of tab 2");
        p.body("        </rich:tab>");
        p.body("    </rich:tabPanel>");
        p.body("    <br/>");
        p.body("    invoked: <h:outputText id='invoked' value='#{tabPanelItemChangeEventBean.invoked}' />");
        p.body("    <br/>");
        p.body("    <a4j:commandButton id='reset' action='#{tabPanelItemChangeEventBean.reset}' value='clear events' render='invoked' />");
        p.body("</h:form>");

        deployment.archive().addAsWebResource(p, "index.xhtml");
    }
}

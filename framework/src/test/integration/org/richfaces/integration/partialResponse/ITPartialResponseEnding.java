package org.richfaces.integration.partialResponse;

import static org.junit.Assert.assertEquals;

import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.deployment.FrameworkDeployment;
import org.richfaces.shrinkwrap.descriptor.FaceletAsset;

@RunWith(Arquillian.class)
@RunAsClient
public class ITPartialResponseEnding {

    @Drone
    private WebDriver browser;

    @ArquillianResource
    private URL contextPath;

    @ArquillianResource
    private JavascriptExecutor executor;
    
    @FindBy
    private WebElement inputText;
    
    @FindBy
    private WebElement outputPanel;
    
    @FindBy
    private WebElement btn;

    @Deployment(testable = false)
    public static WebArchive deployment() {
        FrameworkDeployment deployment = new FrameworkDeployment(ITPartialResponseEnding.class);

        addIndexPage(deployment);
        deployment.addMavenDependency("org.omnifaces:omnifaces:1.3");

        return deployment.getFinalArchive().addClass(SimpleBean.class);
    }
    
    @Test
    public void should_update_components_after_button_click() {
        browser.get(contextPath + "index.jsf");
        inputText.sendKeys("test value");
        Graphene.guardAjax(btn).click();
        assertEquals("test value", outputPanel.getText());
    }

    private static void addIndexPage(FrameworkDeployment deployment) {
        FaceletAsset p = new FaceletAsset();

        p.form("<h:inputText id='inputText' value='#{simpleBean.test}'></h:inputText>");
        p.form("<r:outputPanel id='outputPanel'>");
        p.form("  <h:outputText value='#{simpleBean.test}'/>");
        p.form("</r:outputPanel>");
        
        p.form("<r:commandButton value='Test' action='#{simpleBean.doAction}'  " +
        		"id='btn' execute='@form' render='outputPanel' />");
        
        deployment.archive().addAsWebResource(p, "index.xhtml");
    }
}
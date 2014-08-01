package org.richfaces.component.popupPanel;

import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.richfaces.integration.RichDeployment;
import org.richfaces.shrinkwrap.descriptor.FaceletAsset;

@RunAsClient
@RunWith(Arquillian.class)
public class ITPopupPanel {

    @Drone
    private WebDriver browser;

    @ArquillianResource
    private URL contextPath;

    @FindBy(id = "myForm:popup")
    private WebElement popupPanel;

    @FindBy(className = "rf-pp-cntr")
    private WebElement container;

    @FindBy(className = "rf-pp-shdw")
    private WebElement shadow;

    @FindBy(className = "showButton")
    private WebElement button;

    @FindBy(className = "closeLink")
    private WebElement closeLink;

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        RichDeployment deployment = new RichDeployment(ITPopupPanel.class);
        addIndexPage(deployment);

        WebArchive archive = deployment.getFinalArchive();
        return archive;
    }

    @Test
    public void check_toggle_panel() {
        browser.get(contextPath.toExternalForm() + "index.jsf");
        button.click();
    }

    @Test
    public void check_toggle_panel_resize() {
        browser.get(contextPath.toExternalForm() + "index.jsf");
        button.click();

        Assert.assertEquals("Container width", "300px", container.getCssValue("width"));
        Assert.assertEquals("Shadow width", "300px", shadow.getCssValue("width"));
        Assert.assertEquals("Container height", "200px", container.getCssValue("height"));
        Assert.assertEquals("Shadow height", "200px", shadow.getCssValue("height"));

        WebElement resizeHandle = browser.findElement(By.id("myForm:popupResizerSE"));
        Actions builder = new Actions(browser);

        final Action dragAndDrop = builder.dragAndDropBy(resizeHandle, 40, 40).build();
        dragAndDrop.perform();

        Assert.assertEquals("Container width", "340px", container.getCssValue("width"));
        Assert.assertEquals("Shadow width", "340px", shadow.getCssValue("width"));
        Assert.assertEquals("Container height", "240px", container.getCssValue("height"));
        Assert.assertEquals("Shadow height", "240px", shadow.getCssValue("height"));

        closeLink.click();
        button.click();

        Assert.assertEquals("Container width", "300px", container.getCssValue("width"));
        Assert.assertEquals("Shadow width", "300px", shadow.getCssValue("width"));
        Assert.assertEquals("Container height", "200px", container.getCssValue("height"));
        Assert.assertEquals("Shadow height", "200px", shadow.getCssValue("height"));
    }

    private static void addIndexPage(RichDeployment deployment) {
        FaceletAsset p = new FaceletAsset();

        p.body("<h:form id='myForm'>");
        p.body("    <h:commandButton value='Call the popup' class='showButton'> ");
        p.body("        <rich:componentControl target='popup' operation='show' /> ");
        p.body("    </h:commandButton> ");
        p.body("    <rich:popupPanel id='popup' modal='true' resizeable='true' onmaskclick='#{rich:component(\"popup\")}.hide()' > ");
        p.body("        <f:facet name='header'> ");
        p.body("            <h:outputText value='Simple popup panel' /> ");
        p.body("        </f:facet> ");
        p.body("        <f:facet name='controls'> ");
        p.body("            <h:outputLink styleClass='closeLink' value='#' onclick='#{rich:component(\"popup\")}.hide(); return false;'>X</h:outputLink> ");
        p.body("        </f:facet> ");
        p.body("        <p>You can also check and trigger events if the use clicks outside of the panel.</p> ");
        p.body("        <p>In this example clicking outside closes the panel.</p> ");
        p.body("    </rich:popupPanel> ");
        p.body("</h:form>");

        deployment.archive().addAsWebResource(p, "index.xhtml");
    }

}
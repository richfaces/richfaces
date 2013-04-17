package org.richfaces.integration.tabPanel;

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
import org.openqa.selenium.support.FindBy;
import org.richfaces.integration.OutputDeployment;
import org.richfaces.shrinkwrap.descriptor.FaceletAsset;

import java.net.URL;
import java.util.List;

import static org.jboss.arquillian.graphene.Graphene.guardXhr;

@RunAsClient
@RunWith(Arquillian.class)
public class RF12839_Test {

    @Drone
    private WebDriver browser;

    @ArquillianResource
    private URL contextPath;

    @FindBy(id = "myForm:tabPanel")
    private WebElement tabPanel;

    @FindBy(id = "myForm:tab1:header:inactive")
    private WebElement tab1;

    @FindBy(id = "myForm:tab2:header:inactive")
    private WebElement tab2;

    @FindBy(id = "out")
    private WebElement out;

    @FindBy(id = "myForm:inputText")
    private WebElement inputText;

    @FindBy(id = "myForm:outputText")
    private WebElement outputText;

    @Deployment
    public static WebArchive createDeployment() {
        OutputDeployment deployment = new OutputDeployment(RF12839_Test.class);
        deployment.archive().addClass(SimpleBean.class);

        addIndexPage(deployment);

        WebArchive archive = deployment.getFinalArchive();
        return archive;
    }

    @Test
    public void check_tab_switch() {
        browser.get(contextPath.toExternalForm() + "index.jsf");

        guardXhr(tab2).click();
        Assert.assertTrue(out.getText().contains("begin"));
        Assert.assertTrue(out.getText().contains("tabpanel_complete"));
        Assert.assertTrue(out.getText().contains("beforedomupdate"));

        // Assert the oncomplete on the tab doesn't work
        Assert.assertFalse(out.getText().contains("tab2_complete"));

    }

    @Test
    public void check_tab_execute() {
        browser.get(contextPath.toExternalForm() + "index.jsf");

        inputText.sendKeys("abcd");

        guardXhr(tab2).click();

        Assert.assertEquals("abcd", outputText.getText());
    }


    private static void addIndexPage(OutputDeployment deployment) {
        FaceletAsset p = new FaceletAsset();
        p.xmlns("rich", "http://richfaces.org/output");
        p.xmlns("a4j", "http://richfaces.org/a4j");
        p.xmlns("c", "http://java.sun.com/jsp/jstl/core");
        p.body("<h:form id='myForm'>");
        p.body("<rich:tabPanel id='tabPanel' ");
        p.body("               execute='inputText'");
        p.body("               onbegin='$(\"#out\").append(\"begin \\n\")'");
        p.body("               oncomplete='$(\"#out\").append(\"tabpanel_complete \\n\")'");
        p.body("               onbeforedomupdate='$(\"#out\").append(\"beforedomupdate \\n\")'>");
        p.body("    <rich:tab id='tab1' name='tab1' header='tab1 header' ");
        p.body("               oncomplete='$(\"#out\").append(\"tab1_complete \\n\")'>");
        p.body("        content of tab 1");
        p.body("    </rich:tab>");
        p.body("    <rich:tab id='tab2' name='tab2' header='tab2 header' ");
        p.body("               execute='inputText'");
        p.body("               oncomplete='$(\"#out\").append(\"tab2_complete \\n\")'>");
        p.body("        content of tab 2");
        p.body("        <h:outputText id = 'outputText' value='#{simpleBean.string}' />");
        p.body("    </rich:tab>");
        p.body("</rich:tabPanel> ");
        p.body("<h:inputText id = 'inputText' value='#{simpleBean.string}' />");
        p.body("<div id='out'></div>");
        p.body("</h:form>");

        deployment.archive().addAsWebResource(p, "index.xhtml");
    }

}

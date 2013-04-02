package org.richfaces.ui.ajax;

import java.net.URL;

import junit.framework.Assert;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.richfaces.integration.CoreUIDeployment;
import org.richfaces.shrinkwrap.descriptor.FaceletAsset;

@RunAsClient
@RunWith(Arquillian.class)
public class ITTestAjax {

    @Drone
    private WebDriver browser;

    @ArquillianResource
    private URL contextPath;

    @Deployment
    public static WebArchive createDeployment() {
        CoreUIDeployment deployment = new CoreUIDeployment(ITTestAjax.class);
        deployment.archive().addClass(AjaxBean.class);
        addIndexPage(deployment);
        deployment.archive().addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");

        return deployment.getFinalArchive();
    }

    @Test
    public void listener_with_parameter() throws InterruptedException {
        // given
        browser.get(contextPath.toExternalForm());
        WebElement cell = browser.findElement(By.id("myForm:input"));
        cell.sendKeys("123");
        Graphene.guardXhr(cell).sendKeys(Keys.TAB);
        cell = browser.findElement(By.id("myForm:input"));
        Assert.assertEquals("4", cell.getAttribute("value"));
    }

    private static void addIndexPage(CoreUIDeployment deployment) {
        FaceletAsset p = new FaceletAsset();

        p.body("<h:form id='myForm'> ");
        p.body("    <h:inputText id='input' value='#{ajaxBean.value}'> ");
        p.body("        <r:ajax listener='#{ajaxBean.listener(\"4\")}' render='@this' /> ");
        p.body("    </h:inputText> ");
        p.body("</h:form> ");

        deployment.archive().addAsWebResource(p, "index.xhtml");
    }

}

package org.richfaces.ui.iteration;

import static org.jboss.arquillian.graphene.Graphene.guardXhr;
import static org.junit.Assert.assertEquals;

import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.arquillian.warp.WarpTest;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.richfaces.deployment.FrameworkDeployment;

@RunWith(Arquillian.class)
@WarpTest
@RunAsClient
public class ITNestedRepeat {

    @Drone
    private WebDriver browser;

    @ArquillianResource
    private URL contextPath;

    @Deployment
    public static WebArchive deployment() {
        FrameworkDeployment deployment = new FrameworkDeployment(ITNestedRepeat.class);

        deployment.archive()
            .addClasses(NestedDataBean.class)
            .addAsWebResource(ITNestedRepeat.class.getResource("NestedRepeatTest.xhtml"), "NestedRepeatTest.xhtml")
            .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");

        return deployment.getFinalArchive();
    }

    @Test
    public void testRendering() {
        browser.get(contextPath + "NestedRepeatTest.jsf");

        for (int i = 0; i < 3; i++) {
            WebElement input = browser.findElement(By.id("form:outer:" + i + ":inner:0:input"));
            input.sendKeys(Integer.toString(i));
        }

        WebElement ajax = browser.findElement(By.id("form:ajax"));
        guardXhr(ajax).click();

        for (int i = 0; i < 3; i++) {
            WebElement input = browser.findElement(By.id("form:outer:" + i + ":inner:0:input"));
            assertEquals(Integer.toString(i), input.getAttribute("value"));
        }
    }
}

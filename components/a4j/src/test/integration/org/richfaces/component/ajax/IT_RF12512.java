package org.richfaces.component.ajax;

import java.io.File;
import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.richfaces.integration.A4JDeployment;
import org.richfaces.shrinkwrap.descriptor.FaceletAsset;

@RunAsClient
@RunWith(Arquillian.class)
public class IT_RF12512 {

    @Drone
    private WebDriver browser;

    @ArquillianResource
    private URL contextPath;

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        A4JDeployment deployment = new A4JDeployment(IT_RF12512.class);
        deployment.archive().addClass(A4JRepeatBean.class);
        deployment.archive().addClass(AjaxBean.class);
        deployment.archive().addAsWebResource(new File("src/test/resources/org/richfaces/view/facelets/html/a4jrepeatMatrix.xhtml"));
        addIndexPage(deployment);
        return deployment.getFinalArchive();
    }

    @Test
    public void input_matrix() {
        browser.get(contextPath.toExternalForm() + "a4jrepeatMatrix.jsf");
        WebElement increaseLink00 = browser.findElement(By.id("form:a4jRepeatRows:0:a4jRepeatColumns:0:increaseLink"));
        WebElement clearLink00 = browser.findElement(By.id("form:a4jRepeatRows:0:a4jRepeatColumns:0:clearLink"));

        Graphene.guardAjax(increaseLink00).click();
        WebElement output00 = browser.findElement(By.id("form:outputRows:0:outputColumns:0:output"));
        WebElement input00 = browser.findElement(By.id("form:a4jRepeatRows:0:a4jRepeatColumns:0:valueInput"));
        Assert.assertEquals("1", output00.getText());
        Assert.assertEquals("1", input00.getAttribute("value"));

        Graphene.guardAjax(clearLink00).click();
        output00 = browser.findElement(By.id("form:outputRows:0:outputColumns:0:output"));
        input00 = browser.findElement(By.id("form:a4jRepeatRows:0:a4jRepeatColumns:0:valueInput"));
        Assert.assertEquals("0", output00.getText());
        Assert.assertEquals("0", input00.getAttribute("value"));
    }

    @Test
    public void input_single() {
        browser.get(contextPath.toExternalForm());
        WebElement input;
        input = browser.findElement(By.id("myForm:input"));
        Assert.assertEquals("0", input.getAttribute("value"));
        input.sendKeys("123");
        WebElement submit = browser.findElement(By.id("myForm:submit"));
        Graphene.guardAjax(submit).click();
        Assert.assertEquals("0123", input.getAttribute("value"));
        WebElement clear = browser.findElement(By.id("myForm:clear"));
        Graphene.guardAjax(clear).click();
        input = browser.findElement(By.id("myForm:input"));
        Assert.assertEquals("0", input.getAttribute("value"));
    }

    private static void addIndexPage(A4JDeployment deployment) {
        FaceletAsset p = new FaceletAsset();

        p.body("<h:form id='myForm'> ");
        p.body("    <h:inputText id='input' value='#{ajaxBean.value}'/> ");
        p.body("    <h:commandButton id='submit' value = 'Submit Input'> ");
        p.body("        <f:ajax execute='input' render='input' /> ");
        p.body("    </h:commandButton> ");
        p.body("    <h:commandButton id='clear' value = 'Clear Input'> ");
        p.body("        <f:ajax listener='#{ajaxBean.clearValue}' render='input' /> ");
        p.body("    </h:commandButton> ");
        p.body("</h:form> ");

        deployment.archive().addAsWebResource(p, "index.xhtml");
    }

}

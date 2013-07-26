package org.richfaces.ui.validation;

import static org.junit.Assert.assertFalse;

import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.condition.element.ElementIsPresent;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.deployment.FrameworkDeployment;
import org.richfaces.shrinkwrap.descriptor.FaceletAsset;

@RunWith(Arquillian.class)
@RunAsClient
public class IT_RF12831 {

    @Drone
    private WebDriver browser;

    @ArquillianResource
    private URL contextPath;

    @Deployment
    public static WebArchive createDeployment() {
        FrameworkDeployment deployment = new FrameworkDeployment(null);
        addIndexPage(deployment);

        deployment.archive().addClass(IT_RF12831_Model.class).addClass(IT_RF12831_ModelBean.class)
            .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");

        return deployment.getFinalArchive();
    }

    @FindBy(id = "field2")
    WebElement secondInput;

    @FindBy(id = "field1")
    WebElement firstInput;

    @FindBy(className = "rf-msg-err")
    WebElement errorMsg;

    @Test
    public void test() {
        browser.get(contextPath.toExternalForm());

        secondInput.sendKeys("richfaces");
        Graphene.guardXhr(firstInput).click();

        assertFalse(new ElementIsPresent(errorMsg).apply(browser));
    }

    private static void addIndexPage(org.richfaces.deployment.Deployment deployment) {
        FaceletAsset p = new FaceletAsset();

        p.form("<h:outputLabel value='Model - field1: ' for='field1' />");
        p.form("<h:inputText value='#{modelBean.model.field1}' id='field1' validator='#{modelBean.validateField1}'>");
        p.form("    <r:validator />");
        p.form("</h:inputText>");
        p.form("<r:message for='field1' />");

        p.form("<h:outputLabel value='Model - field2: ' for='field2' />");
        p.form("<h:inputText value='#{modelBean.model.field2}' id='field2' validator='#{modelBean.validateField1}'>");
        p.form("    <r:validator />");
        p.form("</h:inputText>");
        p.form("<r:message for='field2' />");

        deployment.archive().addAsWebResource(p, "index.xhtml");
    }
}

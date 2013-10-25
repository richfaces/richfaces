package org.richfaces.component.ajax;

import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.richfaces.integration.CoreUIDeployment;
import org.richfaces.shrinkwrap.descriptor.FaceletAsset;

@RunAsClient
@RunWith(Arquillian.class)
public class TestJsFunction {

    @Drone
    private WebDriver browser;

    @ArquillianResource
    private URL contextPath;

    @Deployment
    public static WebArchive createDeployment() {
//        CoreUIDeployment deployment = new CoreUIDeployment(TestJsFunction.class, "4.2.3.Final");
        CoreUIDeployment deployment = new CoreUIDeployment(TestJsFunction.class);
        deployment.archive().addClass(AjaxBean.class);
        addIndexPage(deployment);
        deployment.archive().addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");

        return deployment.getFinalArchive();
    }

    @Test
    @Ignore("RF-12761")
    public void listener_with_parameter() throws InterruptedException {
        // given
        browser.get(contextPath.toExternalForm());
        WebElement button1 = browser.findElement(By.id("myForm:jsFunction"));
        Graphene.guardAjax(button1).click();
        WebElement button2 = browser.findElement(By.id("myForm2:ajax2"));
        Graphene.guardAjax(button2).click();
    }

    // from RF-12761
    private static void addIndexPage(CoreUIDeployment deployment) {
        FaceletAsset p = new FaceletAsset();
        p.xmlns("rich", "http://richfaces.org/rich");
        p.xmlns("a4j", "http://richfaces.org/a4j");

        p.body("<h:form id='myForm'> ");
        p.body("    <a4j:jsFunction name='jsFunctionTest' actionListener='#{ajaxBean.methodA}' render=':panel'/> ");
        p.body("    <h:commandButton value='Test' id='jsFunction'> ");
        p.body("        <f:ajax onevent='jsFunctionTest()'/> ");
        p.body("    </h:commandButton> ");
        p.body("</h:form> ");
        p.body("<rich:panel id='panel' > ");
        p.body("    <h:form id='myForm2'> ");
        p.body("        <a4j:commandButton value='OK' actionListener='#{ajaxBean.methodB}' id='ajax2' /> ");
        p.body("    </h:form> ");
        p.body("</rich:panel> ");
        p.body("<rich:messages /> ");
        deployment.archive().addAsWebResource(p, "index.xhtml");
    }

}

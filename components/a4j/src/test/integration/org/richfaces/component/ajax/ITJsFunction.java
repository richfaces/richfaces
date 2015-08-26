package org.richfaces.component.ajax;

import static java.text.MessageFormat.format;

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
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.integration.A4JDeployment;
import org.richfaces.shrinkwrap.descriptor.FaceletAsset;

import category.Failing;

@RunAsClient
@RunWith(Arquillian.class)
public class ITJsFunction {

    private static final String JS_FUNCTION_WITH_PARAM_TEMPLATE = "myForm:repeat:{0}:panel";

    @Drone
    private WebDriver browser;

    @ArquillianResource
    private URL contextPath;

    @FindBy(id = "myForm:output")
    private WebElement output;

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
//        CoreUIDeployment deployment = new CoreUIDeployment(ITJsFunction.class, "4.2.3.Final");
        A4JDeployment deployment = new A4JDeployment(ITJsFunction.class);
        deployment.archive().addClass(AjaxBean.class);
        addIndexPage(deployment);
        addParamPage(deployment);

        return deployment.getFinalArchive();
    }

    /**
     * {@link https://issues.jboss.org/browse/RF-12761}
     */
    @Test
    @Category(Failing.class)
    public void listener_with_parameter() throws InterruptedException {
        // given
        browser.get(contextPath.toExternalForm());
        WebElement button1 = browser.findElement(By.id("myForm:jsFunction"));
        Graphene.guardAjax(button1).click();
        WebElement button2 = browser.findElement(By.id("myForm2:ajax2"));
        Graphene.guardAjax(button2).click();
    }

    // from RF-12761
    private static void addIndexPage(A4JDeployment deployment) {
        FaceletAsset p = new FaceletAsset();

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

    @Test
    public void js_function_with_param() throws InterruptedException {
        browser.get(contextPath.toExternalForm() + "param.jsf");
        for (int i : new int[] { 4, 1, 3 }) {
            Graphene.guardAjax(browser.findElement(By.id(format(JS_FUNCTION_WITH_PARAM_TEMPLATE, i)))).click();
            Assert.assertEquals(i, Integer.parseInt(output.getText()));
        }
    }

    private static void addParamPage(A4JDeployment deployment) {
        FaceletAsset p = new FaceletAsset();

        p.body("<h:form id='myForm'> ");
        p.body("    <a4j:jsFunction name='jsFunctionTest' actionListener='#{ajaxBean.listener}' render='@form'> ");
        p.body("        <a4j:param name='param1' assignTo='#{ajaxBean.longValue}'/> ");
        p.body("    </a4j:jsFunction> ");
        p.body("    <a4j:repeat id='repeat' value='#{ajaxBean.nodes}' var='node' rowKeyVar='key' rows='30'> ");
        p.body("        <a4j:outputPanel id='panel' layout='block' onclick='jsFunctionTest(#{key});'> ");
        p.body("            <h:outputText value='#{node.label}  key: #{key}'/> ");
        p.body("        </a4j:outputPanel> ");
        p.body("    </a4j:repeat> ");
        p.body("    <br/><br/>");
        p.body("    value set by jsFunction with param: <h:outputText id='output' value='#{ajaxBean.longValue}'/> ");
        p.body("    <rich:messages /> ");
        p.body("</h:form> ");
        deployment.archive().addAsWebResource(p, "param.xhtml");
    }

}

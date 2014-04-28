package org.richfaces.component.ajax;

import static org.jboss.arquillian.warp.client.filter.http.HttpFilters.request;

import java.net.URL;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.arquillian.warp.Activity;
import org.jboss.arquillian.warp.Inspection;
import org.jboss.arquillian.warp.Warp;
import org.jboss.arquillian.warp.WarpTest;
import org.jboss.arquillian.warp.jsf.AfterPhase;
import org.jboss.arquillian.warp.jsf.BeforePhase;
import org.jboss.arquillian.warp.jsf.Phase;
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
import org.richfaces.integration.CoreUIDeployment;
import org.richfaces.shrinkwrap.descriptor.FaceletAsset;

import category.Failing;

@RunAsClient
@WarpTest
@RunWith(Arquillian.class)
public class ITJsFunction {

    @Drone
    private WebDriver browser;

    @ArquillianResource
    private URL contextPath;

    @FindBy(id = "myForm:repeat:3:panel")
    private WebElement panel3;

    @Deployment
    public static WebArchive createDeployment() {
//        CoreUIDeployment deployment = new CoreUIDeployment(ITJsFunction.class, "4.2.3.Final");
        CoreUIDeployment deployment = new CoreUIDeployment(ITJsFunction.class);
        deployment.archive().addClass(AjaxBean.class);
        addIndexPage(deployment);
        addParamPage(deployment);
        deployment.archive().addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");

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

    @Test
    public void js_function_with_param() throws InterruptedException {
        // given
        browser.get(contextPath.toExternalForm() + "param.jsf");

        Warp.initiate(new Activity() {
            public void perform() {
                Graphene.guardAjax(panel3).click();
            }
        }).group().observe(request().uri().contains("param")).inspect(new Inspection() {
            private static final long serialVersionUID = 1L;

            @Inject
            AjaxBean bean;

            @BeforePhase(Phase.INVOKE_APPLICATION)
            public void verify_param_not_yet_assigned() {
                Assert.assertEquals(0, bean.getLongValue());
            }

            @AfterPhase(Phase.INVOKE_APPLICATION)
            public void verify_param_assigned() {
                Assert.assertEquals(3, bean.getLongValue());
            }
        }).execute();
    }

    private static void addParamPage(CoreUIDeployment deployment) {
        FaceletAsset p = new FaceletAsset();
        p.xmlns("rich", "http://richfaces.org/rich");
        p.xmlns("a4j", "http://richfaces.org/a4j");
        
        p.body("<h:form id='myForm'> ");
        p.body("    <r:jsFunction name='jsFunctionTest' actionListener='#{ajaxBean.listener}' render='@form'> ");
        p.body("        <r:param name='param1' assignTo='#{ajaxBean.longValue}'/> ");
        p.body("    </r:jsFunction> ");
        p.body("    <r:repeat id='repeat' value='#{ajaxBean.nodes}' var='node' rowKeyVar='key' rows='30'> ");
        p.body("        <r:outputPanel id='panel' layout='block' onclick='jsFunctionTest(#{key});'> ");
        p.body("            <h:outputText value='#{node.label}  key: #{key}'/> ");
        p.body("        </r:outputPanel> ");
        p.body("    </r:repeat> ");
        p.body("    <r:messages /> ");
        p.body("</h:form> ");
        deployment.archive().addAsWebResource(p, "param.xhtml");
    }

}

package org.richfaces.component.focus;

import static org.jboss.arquillian.graphene.Graphene.guardHttp;
import static org.jboss.arquillian.graphene.Graphene.guardXhr;
import static org.jboss.arquillian.graphene.Graphene.waitAjax;
import static org.junit.Assert.assertEquals;

import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.arquillian.warp.WarpTest;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.integration.MiscDeployment;
import org.richfaces.shrinkwrap.descriptor.FaceletAsset;

@RunAsClient
@WarpTest
@RunWith(Arquillian.class)
public class TestFocusMultipleForms {

    @Drone
    private WebDriver browser;

    @ArquillianResource
    private URL contextPath;

    @FindBy(id = "form1")
    private Form form1;

    @FindBy(id = "form2")
    private Form form2;

    @FindBy(id = "form3")
    private Form form3;

    @Deployment
    public static WebArchive createDeployment() {
        MiscDeployment deployment = new MiscDeployment(TestFocusMultipleForms.class);

        addIndexPage(deployment);

        return deployment.getFinalArchive();
    }

    @Test
    public void when_page_is_loaded_initially_then_first_input_from_first_form_should_gain_focus() {
        browser.get(contextPath.toExternalForm());

        assertEquals(form1.input1, FocusRetriever.retrieveActiveElement());
    }

    @Test
    public void when_form_is_submitted_then_first_input_from_this_form_should_gain_focus() {
        browser.get(contextPath.toExternalForm());

        guardHttp(form2.submit).click();
        assertEquals(form2.input2, FocusRetriever.retrieveActiveElement());

        guardHttp(form1.submit).click();
        assertEquals(form1.input2, FocusRetriever.retrieveActiveElement());

        guardHttp(form3.submit).click();
        assertEquals(form3.input1, FocusRetriever.retrieveActiveElement());
    }

    @Test
    public void when_ajax_is_sent_then_first_input_from_submitted_form_should_gain_focus() {
        browser.get(contextPath.toExternalForm());

        guardXhr(form2.ajax).click();
        waitAjax().until(new ElementIsFocused(form2.input2));

        guardXhr(form1.ajax).click();
        waitAjax().until(new ElementIsFocused(form1.input2));

        guardXhr(form3.ajax).click();
        waitAjax().until(new ElementIsFocused(form3.input1));
    }

    private static void addIndexPage(MiscDeployment deployment) {
        FaceletAsset p = new FaceletAsset();
        p.xmlns("rich", "http://richfaces.org/rich");
        p.xmlns("a4j", "http://richfaces.org/a4j");

        p.body("<h:form id='form1'>");
        p.body("    <rich:focus id='focus' />");

        p.body("    <h:inputText id='input1' />");
        p.body("    <h:inputText id='input2' required='true' />");

        p.body("    <h:commandButton id='submit' value='Submit' />");

        p.body("    <a4j:commandButton id='ajax' render='@form' value='Ajax' />");
        p.body("</h:form>");

        p.body("<h:form id='form2'>");
        p.body("    <rich:focus id='focus' />");

        p.body("    <h:inputText id='input1' />");
        p.body("    <h:inputText id='input2' required='true' />");

        p.body("    <h:commandButton id='submit' value='Submit' />");

        p.body("    <a4j:commandButton id='ajax' render='@form' value='Ajax' />");
        p.body("</h:form>");

        p.body("<h:form id='form3'>");
        p.body("    <rich:focus id='focus' />");

        p.body("    <h:inputText id='input1' />");
        p.body("    <h:inputText id='input2' />");

        p.body("    <h:commandButton id='submit' value='Submit' />");

        p.body("    <a4j:commandButton id='ajax' render='@form' value='Ajax' />");
        p.body("</h:form>");

        deployment.archive().addAsWebResource(p, "index.xhtml");
    }

    static class Form {
        @FindBy(css = "[id$=input1]")
        WebElement input1;

        @FindBy(css = "[id$=input2]")
        WebElement input2;

        @FindBy(css = "[id$=submit]")
        WebElement submit;

        @FindBy(css = "[id$=ajax]")
        WebElement ajax;
    }
}

package org.richfaces.component.focus;

import static org.jboss.arquillian.graphene.Graphene.guardAjax;
import static org.jboss.arquillian.graphene.Graphene.guardHttp;
import static org.jboss.arquillian.graphene.Graphene.waitGui;
import static org.junit.Assert.assertEquals;

import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.integration.RichDeployment;
import org.richfaces.shrinkwrap.descriptor.FaceletAsset;
import org.richfaces.utils.focus.ElementIsFocused;
import org.richfaces.utils.focus.FocusRetriever;

@RunAsClient
@RunWith(Arquillian.class)
public class ITFocusDelayed {

    @Drone
    private WebDriver browser;

    @ArquillianResource
    private URL contextPath;

    @FindBy(id = "form:submit")
    private WebElement submit;

    @FindBy(id = "form:ajax")
    private WebElement ajax;

    @FindBy(id = "form:input1")
    private WebElement input1;

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        RichDeployment deployment = new RichDeployment(ITFocusDelayed.class);

        addIndexPage(deployment);

        return deployment.getFinalArchive();
    }

    @Test
    public void when_focus_is_delayed_then_it_is_not_applied_on_initial_request() {
        browser.get(contextPath.toExternalForm());
        assertEquals(null, getFocusedElement());
    }

    @Test
    public void test_delayed_focus_can_be_applied_using_client_side_api() {
        browser.get(contextPath.toExternalForm());

        ((JavascriptExecutor) browser).executeScript("RichFaces.component('form:focus').applyFocus(); ");
        waitGui().until(new ElementIsFocused(input1));
    }

    @Test
    public void when_focus_is_delayed_then_it_is_not_applied_on_form_submit_postback() {
        browser.get(contextPath.toExternalForm());

        guardHttp(submit).click();
        assertEquals(null, getFocusedElement());
    }

    @Test
    public void when_focus_is_delayed_then_it_is_not_applied_on_ajax_postback() {
        browser.get(contextPath.toExternalForm());

        guardAjax(ajax).click();
        assertEquals(null, getFocusedElement());
    }

    private WebElement getFocusedElement() {
        return FocusRetriever.retrieveActiveElement();
    }

    private static void addIndexPage(RichDeployment deployment) {
        FaceletAsset p = new FaceletAsset();

        p.body("<h:form id='form'>");
        p.body("    <rich:focus id='focus' delayed='true' />");

        p.body("    <h:inputText id='input1' />");
        p.body("    <h:inputText id='input2' />");

        p.body("    <h:commandButton id='submit' value='Submit' />");

        p.body("    <a4j:commandButton id='ajax' render='@form' value='Ajax' />");

        p.body("</h:form>");

        deployment.archive().addAsWebResource(p, "index.xhtml");
    }
}

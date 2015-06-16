package org.richfaces.component.focus;

import static org.jboss.arquillian.graphene.Graphene.guardAjax;
import static org.jboss.arquillian.graphene.Graphene.guardHttp;
import static org.jboss.arquillian.graphene.Graphene.waitAjax;

import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.integration.RichDeployment;
import org.richfaces.shrinkwrap.descriptor.FaceletAsset;
import org.richfaces.utils.focus.ElementIsFocused;
import org.richfaces.utils.focus.FocusRetriever;

@RunAsClient
@RunWith(Arquillian.class)
public class ITFocusPreserve {

    @Drone
    private WebDriver browser;

    @ArquillianResource
    private URL contextPath;

    @FindBy(id = "form:input2")
    private WebElement input2;

    @FindBy(id = "form:input3")
    private WebElement input3;

    @FindBy(id = "form:submit")
    private WebElement submit;

    @FindBy(id = "form:ajax")
    private WebElement ajax;

    @FindBy(id = "secondForm:renderFirstForm")
    private WebElement renderFirstFormFromSecondForm;

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        RichDeployment deployment = new RichDeployment(ITFocusPreserve.class);

        addIndexPage(deployment);

        return deployment.getFinalArchive();
    }

    @Test
    public void testInputFocusIsPreservedAfterSubmission() {
        // having
        browser.get(contextPath.toExternalForm());

        // when
        input2.click();
        guardHttp(submit).click();

        // then
        Graphene.waitGui().until(new ElementIsFocused(input2));
    }

    @Test
    public void testInputFocusIsPreservedAfterAjax() {
        // having
        browser.get(contextPath.toExternalForm());

        // when
        input3.click();
        guardAjax(ajax).click();

        // then
        waitAjax().until(new ElementIsFocused(input3));
    }

    @Test
    public void when_focus_is_rerendered_from_another_form_then_it_is_rendered_and_working_but_not_applied() throws InterruptedException {
        // having
        browser.get(contextPath.toExternalForm());

        // when
        guardAjax(renderFirstFormFromSecondForm).click();
        Thread.sleep(500);

        input2.click();
        guardAjax(ajax).click();

        // then
        waitAjax().until(new ElementIsFocused(input2));
    }

    private WebElement getFocusedElement() {
        return FocusRetriever.retrieveActiveElement();
    }

    private static void addIndexPage(RichDeployment deployment) {
        FaceletAsset p = new FaceletAsset();

        p.body("<h:form id='form'>");
        p.body("    <rich:focus id='focus' preserve='true' />");

        p.body("    <h:inputText id='input1' />");
        p.body("    <h:inputText id='input2' />");
        p.body("    <h:inputText id='input3' />");

        p.body("    <h:commandButton id='submit' value='Submit' />");

        p.body("    <h:commandButton id='ajax' value='Ajax'>");
        p.body("        <a4j:ajax render='@form' />");
        p.body("    </h:commandButton>");

        p.body("</h:form>");

        p.body("<h:form id='secondForm'>");
        p.body("    <a4j:commandButton id='renderFirstForm' render='form' value='Re-render form with focus'  />");
        p.body("</h:form>");

        deployment.archive().addAsWebResource(p, "index.xhtml");
    }
}

package org.richfaces.integration.partialViewContext;

import static org.jboss.arquillian.graphene.Graphene.guardXhr;
import static org.junit.Assert.assertEquals;

import java.net.URL;

import javax.faces.context.PartialViewContext;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.integration.CoreDeployment;
import org.richfaces.shrinkwrap.descriptor.FaceletAsset;

/**
 * Tests a4j:commandButton processing using {@link PartialViewContext}. (RF-12145)
 */
@RunAsClient
@RunWith(Arquillian.class)
public class MultipleFormUpdateTest {

    @Drone
    WebDriver browser;

    @ArquillianResource
    URL contextPath;

    @FindBy(id = "firstForm")
    Form firstForm;

    @FindBy(id = "secondForm")
    Form secondForm;

    @Deployment
    public static WebArchive createDeployment() {
        CoreDeployment deployment = new CoreDeployment(MultipleFormUpdateTest.class);

        deployment.withWholeCore();

        addIndexPage(deployment);

        return deployment.getFinalArchive();
    }

    @Test
    public void when_form_is_explicitly_listed_in_render_then_its_ViewState_should_be_updated_after_response() {
        browser.get(contextPath.toExternalForm());
        assertEquals("both forms should have same ViewState", getViewState(firstForm), getViewState(secondForm));

        String viewState = getViewState(firstForm);
        firstForm.input.sendKeys("1");
        guardXhr(firstForm.button).click();

        assertEquals("ViewState should not change", viewState, getViewState(firstForm));
        assertEquals("both forms should have same ViewState", getViewState(firstForm), getViewState(secondForm));
        assertEquals("first form input should be 1", "1", getInput(firstForm));
        assertEquals("second form input should be 1", "1", getInput(secondForm));

        secondForm.input.clear();
        secondForm.input.sendKeys("2");
        guardXhr(secondForm.button).click();

        assertEquals("ViewState should not change", viewState, getViewState(secondForm));
        assertEquals("both forms should have same ViewState", getViewState(secondForm), getViewState(firstForm));
        assertEquals("first form input should be 2", "2", getInput(firstForm));
        assertEquals("second form input should be 2", "2", getInput(secondForm));
    }

    private String getViewState(Form form) {
        try {
            return form.viewState.getAttribute("value");
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    private String getInput(Form form) {
        return form.input.getAttribute("value");
    }

    private static void addIndexPage(CoreDeployment deployment) {
        FaceletAsset p = new FaceletAsset();

        p.head("<h:outputScript name='jsf.js' library='javax.faces' />");
        p.head("<h:outputScript name='jquery.js' />");
        p.head("<h:outputScript name='richfaces.js' />");

        p.body("<h:form id='firstForm'>");
        p.body("    <h:inputText value='#{value}' />");
        p.body("    <h:commandButton value='Submit 1' execute='@form' render='@form :secondForm' onclick='RichFaces.ajax(this, event, {\"incId\": \"1\"}); return false;' />");
        p.body("</h:form>");

        p.body("<h:form id='secondForm'>");
        p.body("    <h:inputText value='#{value}' />");
        p.body("    <h:commandButton value='Submit 2' execute='@form' render='@form :firstForm' onclick='RichFaces.ajax(this, event, {\"incId\": \"1\"}); return false;'  />");
        p.body("</h:form>");

        deployment.archive().addAsWebResource(p, "index.xhtml");
    }

    private class Form {

        @FindBy(name = "javax.faces.ViewState")
        WebElement viewState;

        @FindBy(css = "input[type=submit]")
        WebElement button;

        @FindBy(css = "input[type=text]")
        WebElement input;
    }
}

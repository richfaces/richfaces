package org.richfaces.component.placeholder;

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
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.arquillian.page.source.SourceChecker;
import org.richfaces.integration.MiscDeployment;
import org.richfaces.shrinkwrap.descriptor.FaceletAsset;

@RunAsClient
@RunWith(Arquillian.class)
public class TestPlaceholderSubmit {

    private static final String PLACEHOLDER_TEXT = "Placeholder Text";

    @Drone
    WebDriver browser;

    @ArquillianResource
    URL contextPath;

    @ArquillianResource
    SourceChecker sourceChecker;

    @FindBy(id = "form:input")
    WebElement input;

    @FindBy(id = "form:output")
    WebElement output;

    @FindBy(id = "form:submit")
    WebElement submit;

    @FindBy(id = "form:ajax")
    WebElement ajax;

    @Deployment
    public static WebArchive createDeployment() {
        MiscDeployment deployment = new MiscDeployment(TestPlaceholderSubmit.class);

        addIndexPage(deployment);

        return deployment.getFinalArchive();
    }

    @Test
    public void testSubmitEmptyValue() {
        // given
        browser.get(contextPath.toExternalForm());

        // when
        guardHttp(submit).click();

        // then
        assertEquals("", output.getText());
    }

    @Test
    public void testSubmitTextValue() {
        // given
        browser.get(contextPath.toExternalForm());

        // when
        input.click();
        input.sendKeys("xyz");
        guardHttp(submit).click();

        // then
        assertEquals("xyz", output.getText());
    }



    @Test
    public void testAjaxSendsEmptyValue() {
        // given
        browser.get(contextPath.toExternalForm());
        input.click();
        input.sendKeys("xyz");
        guardXhr(ajax).click();
        waitAjax().until().element(output).text().equalTo("xyz");

        // when
        input.clear();
        guardXhr(ajax).click();

        // then
        waitAjax().until().element(output).text().equalTo("");
    }

    @Test
    public void testAjaxSendsTextValue() {
        // given
        browser.get(contextPath.toExternalForm());

        // when
        input.click();
        input.sendKeys("xyz");
        guardXhr(ajax).click();

        // then
        waitAjax().until().element(output).text().equalTo("xyz");
    }

    private static void addIndexPage(MiscDeployment deployment) {
        FaceletAsset p = new FaceletAsset();
        p.xmlns("rich", "http://richfaces.org/misc");
        p.xmlns("a4j", "http://richfaces.org/a4j");

        p.body("<h:form id='form'>");
        p.body("    <h:inputText id='input' value='#{value}'>");
        p.body("        <rich:placeholder id='placeholder' value='" + PLACEHOLDER_TEXT + "' />");
        p.body("    </h:inputText>");

        p.body("    Output: <a4j:outputPanel id='output'>#{value}</a4j:outputPanel>");

        p.body("    <h:commandButton id='submit' value='Submit' />");
        p.body("    <a4j:commandButton id='ajax' value='Ajax' execute='@form' render='output' />");

        p.body("</h:form>");

        deployment.archive().addAsWebResource(p, "index.xhtml");
    }
}

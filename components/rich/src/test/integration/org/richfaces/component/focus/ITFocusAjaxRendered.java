package org.richfaces.component.focus;

import static org.jboss.arquillian.graphene.Graphene.guardAjax;
import static org.jboss.arquillian.graphene.Graphene.waitAjax;
import static org.jboss.arquillian.graphene.Graphene.waitGui;

import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.javascript.JavaScript;
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
public class ITFocusAjaxRendered {

    @Drone
    private WebDriver browser;

    @ArquillianResource
    private URL contextPath;

    @FindBy(id = "form:ajax")
    private WebElement ajax;

    @FindBy(id = "form:input1")
    private WebElement input1;

    @FindBy(id = "form:input2")
    private WebElement input2;

    @JavaScript
    private FocusRetriever focusRetriever;

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        RichDeployment deployment = new RichDeployment(ITFocusAjaxRendered.class);

        addIndexPage(deployment);

        return deployment.getFinalArchive();
    }

    @Test
    public void when_the_focus_is_not_ajaxRendered_then_no_element_should_have_focus_after_ajax() {
        browser.get(contextPath.toExternalForm());
        waitGui().until(new ElementIsFocused(input1));

        // when
        input2.click();

        // then
        guardAjax(ajax).click();
        waitAjax().until(new ElementIsFocused(null));
    }

    private static void addIndexPage(RichDeployment deployment) {
        FaceletAsset p = new FaceletAsset();

        p.body("<h:form id='form'>");
        p.body("    <rich:focus id='focus' ajaxRendered='false' />");

        p.body("    <h:inputText id='input1' />");
        p.body("    <h:inputText id='input2' />");

        p.body("    <a4j:commandButton id='ajax' render='@form' value='Ajax' />");
        p.body("</h:form>");

        deployment.archive().addAsWebResource(p, "index.xhtml");
    }
}

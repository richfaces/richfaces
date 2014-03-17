package org.richfaces.component.focus;

import static org.jboss.arquillian.graphene.Graphene.guardAjax;
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
public class TestFocusAjaxRendered {

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

    @Deployment
    public static WebArchive createDeployment() {
        MiscDeployment deployment = new MiscDeployment(TestFocusAjaxRendered.class);

        addIndexPage(deployment);

        return deployment.getFinalArchive();
    }

    @Test
    public void when_there_are_inputs_with_tabindex_then_the_lowest_tabindex_will_obtain_focus() {
        browser.get(contextPath.toExternalForm());
        assertEquals(input1, FocusRetriever.retrieveActiveElement());

        // when
        input2.click();

        // then
        guardAjax(ajax).click();
        waitAjax().until(new ElementIsFocused(null));
    }

    private static void addIndexPage(MiscDeployment deployment) {
        FaceletAsset p = new FaceletAsset();
        p.xmlns("rich", "http://richfaces.org/rich");
        p.xmlns("a4j", "http://richfaces.org/a4j");

        p.body("<h:form id='form'>");
        p.body("    <rich:focus id='focus' ajaxRendered='false' />");

        p.body("    <h:inputText id='input1' />");
        p.body("    <h:inputText id='input2' />");

        p.body("    <a4j:commandButton id='ajax' render='input1 input2' value='Ajax' />");
        p.body("</h:form>");

        deployment.archive().addAsWebResource(p, "index.xhtml");
    }
}

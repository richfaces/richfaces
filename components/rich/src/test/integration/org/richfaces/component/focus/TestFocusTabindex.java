package org.richfaces.component.focus;

import static org.junit.Assert.assertEquals;

import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.Graphene;
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
public class TestFocusTabindex {

    @Drone
    private WebDriver browser;

    @ArquillianResource
    private URL contextPath;

    @FindBy(id = "form:input1")
    private WebElement input1;

    @Deployment
    public static WebArchive createDeployment() {
        MiscDeployment deployment = new MiscDeployment(TestFocusTabindex.class);

        addIndexPage(deployment);
        addNoTabindexPage(deployment);

        return deployment.getFinalArchive();
    }

    @Test
    public void when_there_are_inputs_with_tabindex_then_the_lowest_tabindex_will_obtain_focus() {
        browser.get(contextPath.toExternalForm());
        Graphene.waitGui().until(new ElementIsFocused(input1));
    }
    
    @Test
    public void when_there_are_no_tabindex_components_then_first_input_will_obtain_focus() {
        browser.get(contextPath.toExternalForm() + "no-tabindex.jsf");
        Graphene.waitGui().until(new ElementIsFocused(input1));
    }

    private WebElement getFocusedElement() {
        return FocusRetriever.retrieveActiveElement();
    }

    private static void addIndexPage(MiscDeployment deployment) {
        FaceletAsset p = new FaceletAsset();
        p.xmlns("rich", "http://richfaces.org/rich");

        p.body("<h:form id='form'>");
        p.body("    <rich:focus id='focus' />");

        p.body("    <h:inputText id='input3' />");
        p.body("    <h:inputText id='input2' tabindex='2' />");
        p.body("    <h:inputText id='input1' tabindex='1' />");
        p.body("</h:form>");

        deployment.archive().addAsWebResource(p, "index.xhtml");
    }
    
    private static void addNoTabindexPage(MiscDeployment deployment) {
        FaceletAsset p = new FaceletAsset();
        p.xmlns("rich", "http://richfaces.org/rich");

        p.body("<h:form id='form'>");
        p.body("    <rich:focus id='focus' />");

        p.body("    <h:inputText id='input1' />");
        p.body("    <h:inputText id='input2' />");
        p.body("    <h:inputText id='input3' />");
        p.body("</h:form>");

        deployment.archive().addAsWebResource(p, "no-tabindex.xhtml");
    }
}

package org.richfaces.integration.javascript;

import static org.jboss.arquillian.graphene.Graphene.guardAjax;
import static org.junit.Assert.assertTrue;

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
import org.richfaces.deployment.CoreDeployment;
import org.richfaces.shrinkwrap.descriptor.FaceletAsset;

@RunWith(Arquillian.class)
@RunAsClient
public class ITAjaxCallbacksScope {

    @Drone
    private WebDriver browser;

    @ArquillianResource
    private URL contextPath;

    @FindBy(id = "button")
    private WebElement button;

    @ArquillianResource
    private JavascriptExecutor executor;

    @Deployment(testable = false)
    public static WebArchive deployment() {
        CoreDeployment deployment = new CoreDeployment(ITAjaxCallbacksScope.class);
//        deployment.withWholeCore();

        addIndexPage(deployment);

        return deployment.getFinalArchive();
    }

    @Test
    public void test_that_context_of_ajax_request_handlers_invocation_is_the_source_of_event() {
        browser.get(contextPath.toExternalForm());

        guardAjax(button).click();

        assertTrue("the context of onbeforedomupdate function should be the source element (button)",
                (Boolean) executor.executeScript("return window.onbeforedomupdateContext === document.getElementById('button')"));

        assertTrue("the context of oncomplete function should be the source element (button)",
            (Boolean) executor.executeScript("return window.oncompleteContext === document.getElementById('button')"));
    }

    private static void addIndexPage(CoreDeployment deployment) {
        FaceletAsset p = new FaceletAsset();

        p.head("<h:outputScript name='jsf.js' library='javax.faces' />");
        p.head("<h:outputScript library='org.richfaces' name='jquery.js' />");
        p.head("<h:outputScript library='org.richfaces' name='richfaces.js' />");

        p.form("<h:commandButton id='button' "
                + "onclick='RichFaces.ajax(this, event, {}); return false;' "
                + "onbeforedomupdate='window.onbeforedomupdateContext = this' "
                + "oncomplete='window.oncompleteContext = this' />");

        deployment.archive().addAsWebResource(p, "index.xhtml");
    }
}
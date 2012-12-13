package org.richfaces.integration.partialViewContext;

import static org.junit.Assert.assertEquals;

import java.net.URL;

import javax.faces.context.PartialViewContext;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.arquillian.warp.Activity;
import org.jboss.arquillian.warp.Inspection;
import org.jboss.arquillian.warp.Warp;
import org.jboss.arquillian.warp.WarpTest;
import org.jboss.arquillian.warp.client.filter.RequestFilter;
import org.jboss.arquillian.warp.client.filter.http.HttpRequest;
import org.jboss.arquillian.warp.jsf.BeforePhase;
import org.jboss.arquillian.warp.jsf.Phase;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.context.ExtendedPartialViewContext;
import org.richfaces.integration.CoreDeployment;
import org.richfaces.shrinkwrap.descriptor.FaceletAsset;

/**
 * Tests a4j:commandButton processing using {@link PartialViewContext}. (RF-12145)
 */
@RunAsClient
@WarpTest
@RunWith(Arquillian.class)
public class RenderAllTest {

    @Drone
    WebDriver browser;

    @ArquillianResource
    URL contextPath;

    @FindBy(css = "input[type=submit]")
    WebElement button;

    @Deployment
    public static WebArchive createDeployment() {
        CoreDeployment deployment = new CoreDeployment(RenderAllTest.class);

        deployment.withWholeCore();

        addIndexPage(deployment);

        return deployment.getFinalArchive();
    }

    @Test
    public void test() {
        browser.get(contextPath.toExternalForm());

        Warp
            .initiate(new Activity() {
                public void perform() {
                    button.click();
                }
            })
            .observe(new RequestFilter<HttpRequest>() {
                public boolean matches(HttpRequest request) {
                    return request.getUri().contains("/RenderAllTest/");
                };
            })
            .inspect(new Inspection() {
                private static final long serialVersionUID = 1L;

                @ArquillianResource
                PartialViewContext pvc;

                @BeforePhase(Phase.RENDER_RESPONSE)
                public void add_oncomplete() {
                    ((ExtendedPartialViewContext) pvc).appendOncomplete("document.title = 'script executed'");
            }
        });

        assertEquals("script should be executed", "script executed", browser.getTitle());
    }

    private static void addIndexPage(CoreDeployment deployment) {
        FaceletAsset p = new FaceletAsset();

        p.head("<h:outputScript name='jsf.js' library='javax.faces' />");
        p.head("<h:outputScript name='jquery.js' />");
        p.head("<h:outputScript name='richfaces.js' />");

        p.form("<h:commandButton value='Render All' render='@all' onclick='RichFaces.ajax(this, event, {\"incId\": \"1\"}); return false;' />");

        deployment.archive().addAsWebResource(p, "index.xhtml");
    }
}

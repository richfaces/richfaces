package org.richfaces.integration.javascript;

import static org.jboss.arquillian.graphene.Graphene.guardXhr;
import static org.jboss.arquillian.warp.jsf.Phase.INVOKE_APPLICATION;
import static org.junit.Assert.assertEquals;

import java.net.URL;

import javax.faces.context.FacesContext;

import org.ajax4jsf.javascript.JSLiteral;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.enricher.findby.FindBy;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.arquillian.warp.Activity;
import org.jboss.arquillian.warp.Inspection;
import org.jboss.arquillian.warp.Warp;
import org.jboss.arquillian.warp.WarpTest;
import org.jboss.arquillian.warp.jsf.AfterPhase;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.richfaces.integration.CoreDeployment;
import org.richfaces.javascript.JavaScriptService;
import org.richfaces.shrinkwrap.descriptor.FaceletAsset;

@RunWith(Arquillian.class)
@WarpTest
@RunAsClient
public class JavaScriptServiceAjaxTest {

    @Drone
    private WebDriver driver;

    @ArquillianResource
    private URL contextPath;

    @FindBy(id = "richfacesAjax")
    private WebElement richfacesAjax;

    @FindBy(id = "jsfAjax")
    private WebElement jsfAjax;

    @Deployment
    public static WebArchive createDeployment() {
        CoreDeployment deployment = new CoreDeployment(JavaScriptServiceAjaxTest.class);

        deployment.withWholeCore();

        addIndexPage(deployment);

        return deployment.getFinalArchive();
    }

    @Test
    public void richfaces_ajax_should_trigger_script_added_by_JavaScriptService() {
        driver.navigate().to(contextPath);

        Warp
            .initiate(new Activity() {
                public void perform() {
                    guardXhr(richfacesAjax).click();
                }
            })
            .inspect(new AddScript());

        assertEquals("executed", driver.getTitle());
    }

    @Test
    public void jsf_ajax_should_trigger_script_added_by_JavaScriptService() {
        driver.navigate().to(contextPath);

        Warp
            .initiate(new Activity() {
                public void perform() {
                    guardXhr(jsfAjax).click();
                }
            })
            .inspect(new AddScript());

        assertEquals("executed", driver.getTitle());
    }



    private static void addIndexPage(CoreDeployment deployment) {
        FaceletAsset p = new FaceletAsset();

        p.head("<title>initial value</title>");

        p.head("<h:outputScript name='jsf.js' library='javax.faces' />");
        p.head("<h:outputScript name='jquery.js' />");
        p.head("<h:outputScript name='richfaces.js' />");

        p.form("<h:commandButton id='richfacesAjax' onclick='RichFaces.ajax(this, event, {}); return false;' />");
        p.form("<h:commandButton id='jsfAjax' onclick='jsf.ajax.request(this, event, {}); return false;' />");

        deployment.archive().addAsWebResource(p, "index.xhtml");
    }

    public static class AddScript extends Inspection {
        private static final long serialVersionUID = 1L;

        @ArquillianResource
        private FacesContext facesContext;

        @ArquillianResource
        private JavaScriptService jsService;

        @AfterPhase(INVOKE_APPLICATION)
        public void add_script_using_JavaScriptService() {
            jsService.addScript(facesContext, new JSLiteral("document.title = 'executed'"));
        }
    }
}

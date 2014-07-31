package org.richfaces.integration.partialResponse;

import static org.jboss.arquillian.graphene.Graphene.guardAjax;
import static org.junit.Assert.assertEquals;

import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.deployment.CoreDeployment;
import org.richfaces.shrinkwrap.descriptor.FaceletAsset;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import category.JSF22Only;
import category.Smoke;

@RunAsClient
@RunWith(Arquillian.class)
@Category({Smoke.class, JSF22Only.class})
public class ITStatelessViews {

    @Drone
    private WebDriver browser;

    @ArquillianResource
    private URL contextPath;

    @FindBy(id = "jsfAjax")
    private WebElement jsfAjax;

    @FindBy(id = "richfacesAjax")
    private WebElement richfacesAjax;

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        CoreDeployment deployment = new CoreDeployment(ITStatelessViews.class);

        deployment.withWholeFramework();

        addIndexPage(deployment);

        return deployment.getFinalArchive();
    }

    @Test
    public void test_jsf_stateless_ajax() throws Exception {
        browser.get(contextPath.toExternalForm());

        guardAjax(jsfAjax).click();

        verifyViewStateForStateless();
    }

    @Test
    public void test_richfaces_stateless_ajax() throws Exception {
        browser.get(contextPath.toExternalForm());

        guardAjax(richfacesAjax).click();

        verifyViewStateForStateless();

    }

    private void verifyViewStateForStateless() throws Exception {
        Document doc = PartialResponseTestingHelper.getDocument(browser);

        Element partialResponse = doc.getDocumentElement();
        assertEquals("partial-response", partialResponse.getNodeName());

        NodeList changesElements = partialResponse.getElementsByTagName("changes");
        assertEquals("there should be exactly one <changes> element", 1, changesElements.getLength());

        Element changes = (Element) changesElements.item(0);

        NodeList updateElements = changes.getElementsByTagName("update");
        assertEquals("there should be exactly one <update> element", 1, changesElements.getLength());

        Element update = (Element) updateElements.item(0);

        assertEquals("j_id1:javax.faces.ViewState:0", update.getAttribute("id"));
        assertEquals("stateless", update.getTextContent());
    }

    private static void addIndexPage(CoreDeployment deployment) {
        FaceletAsset p = new FaceletAsset();

        p.stateless(true);

        p.body("<h:form id='form' prependId='false'>");

        p.body("<h:commandButton id='jsfAjax' value='JSF AJAX' oncomplete='document.title = \"completed\"'><f:ajax render='@none' /></h:commandButton>");
        p.body("<h:commandButton id='richfacesAjax' value='RichFaces AJAX'><a4j:ajax render='@none' /></h:commandButton>");

        p.body("</h:form>");

        PartialResponseTestingHelper.addPartialResponseInterceptorToPage(p);

        deployment.archive().addAsWebResource(p, "index.xhtml");
    }
}
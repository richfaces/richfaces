package org.richfaces.integration.partialViewContext;

import static org.jboss.arquillian.graphene.Graphene.guardAjax;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;

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
import org.richfaces.integration.RichDeployment;
import org.richfaces.shrinkwrap.descriptor.FaceletAsset;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 * Tests that the exception in ajax requests ends partial-response correctly (RF-12893)
 */
@RunAsClient
@RunWith(Arquillian.class)
public class ITAjaxExceptionHandling {

    @Drone
    private WebDriver browser;

    @ArquillianResource
    private URL contextPath;

    @FindBy(id = "button")
    private WebElement exceptionCausingButton;

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        RichDeployment deployment = new RichDeployment(ITAjaxExceptionHandling.class);
        deployment.archive().addClass(ExceptionCausingBean.class);

        addIndexPage(deployment);

        return deployment.getFinalArchive();
    }

    @Test
    public void test() throws ParserConfigurationException, SAXException, IOException {
        browser.get(contextPath.toExternalForm());

        guardAjax(exceptionCausingButton).click();

        Document doc = PartialResponseTestingHelper.getDocument(browser);

        Element partialResponse = doc.getDocumentElement();
        assertEquals("partial-response", partialResponse.getNodeName());

        Element error = (Element) partialResponse.getElementsByTagName("error").item(0);

        String errorName = error.getElementsByTagName("error-name").item(0).getTextContent();
        String errorMessage = error.getElementsByTagName("error-message").item(0).getTextContent();

        assertEquals(IllegalStateException.class.toString(), errorName);
        assertTrue(errorMessage.contains("this should be handled by JSF"));
    }

    private static void addIndexPage(RichDeployment deployment) {
        FaceletAsset p = new FaceletAsset();

        p.head("<h:outputScript name='jsf.js' library='javax.faces' />");
        p.head("<h:outputScript library='org.richfaces' name='jquery.js' />");
        p.head("<h:outputScript library='org.richfaces' name='richfaces.js' />");

        p.form("<h:panelGroup id='panel'>");
        p.form("    <h:commandButton id='button' action='#{exceptionCausingBean.causeException}' >");
        p.form("        <f:ajax />");
        p.form("    </h:commandButton>");
        p.form("</h:panelGroup>");

        PartialResponseTestingHelper.addPartialResponseInterceptorToPage(p);

        deployment.archive().addAsWebResource(p, "index.xhtml");
    }
}

package org.richfaces.component.autocomplete;

import static org.jboss.arquillian.graphene.Graphene.guardAjax;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.autocomplete.RichFacesAutocomplete;
import org.richfaces.integration.RichDeployment;
import org.richfaces.shrinkwrap.descriptor.FaceletAsset;

@RunAsClient
@RunWith(Arquillian.class)
public class ITAutocompleteBehaviors {

    @FindBy(className = "rf-au")
    private RichFacesAutocomplete autocomplete;

    @FindBy(id = "blur")
    private WebElement blurInput;

    @Drone
    private WebDriver browser;

    @ArquillianResource
    private URL contextPath;

    @FindBy(id = "form:output")
    private WebElement outputElement;

    @Deployment(testable = false)
    // RF-12114
    public static WebArchive createDeployment() {
        RichDeployment deployment = new RichDeployment(ITAutocompleteBehaviors.class);

        deployment.archive().addClasses(AutocompleteBean.class);

        addIndexPage(deployment);

        return deployment.getFinalArchive();
    }

    /**
     * onblur should have input value available via 'this.value' expression
     */
    // RF-12114
    @Test
    public void testAjaxOnBlur() {
        // given
        browser.get(contextPath.toExternalForm());

        assertFalse(Boolean.parseBoolean(outputElement.getText()));

        autocomplete.type("t").select(0);

        guardAjax(blurInput).click();

        assertTrue(Boolean.parseBoolean(outputElement.getText()));
    }

    private static void addIndexPage(RichDeployment deployment) {
        FaceletAsset p = new FaceletAsset();

        p.body("<h:form id='form'>");
        p.body("    <rich:autocomplete id='autocomplete' autocompleteList='#{autocompleteBean.suggestions}'>");
        p.body("        <a4j:ajax event='blur' listener='#{autocompleteBean.actionListener}' render='output' />");
        p.body("    </rich:autocomplete>");
        p.body("    <br/>");
        p.body("    listener was invoked: <h:outputText id='output' value='#{autocompleteBean.listenerInvoked}' />");
        p.body("    <br/>");
        p.body("    <input value='blur' type='button' id='blur' />");
        p.body("</h:form>");

        deployment.archive().addAsWebResource(p, "index.xhtml");
    }
}

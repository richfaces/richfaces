package org.richfaces.component.focus;

import static org.junit.Assert.assertEquals;

import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.integration.RichDeployment;
import org.richfaces.shrinkwrap.descriptor.FaceletAsset;

import category.Smoke;

@RunAsClient
@RunWith(Arquillian.class)
@Category(Smoke.class)
public class ITFocusDefaults {

    @Drone
    private WebDriver browser;

    @ArquillianResource
    private URL contextPath;

    @FindBy(id = "ajaxRendered")
    private WebElement ajaxRendered;

    @FindBy(id = "validationAware")
    private WebElement validationAware;

    @FindBy(id = "preserve")
    private WebElement preserve;

    @FindBy(id = "delayed")
    private WebElement delayed;

    @FindBy(id = "focusCandidates")
    private WebElement focusCandidates;

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        RichDeployment deployment = new RichDeployment(ITFocusValidationAware.class);

        deployment.archive().addClasses(ComponentBean.class);

        addIndexPage(deployment);

        return deployment.getFinalArchive();
    }

    private static void addIndexPage(RichDeployment deployment) {
        FaceletAsset p = new FaceletAsset();

        p.form("<rich:focus id='focus' binding='#{componentBean.component}' />");
        p.form("<h:outputText id='ajaxRendered' value='#{componentBean.component.ajaxRendered}'/><br/>");
        p.form("<h:outputText id='validationAware' value='#{componentBean.component.validationAware}'/><br/>");
        p.form("<h:outputText id='preserve' value='#{componentBean.component.preserve}'/><br/>");
        p.form("<h:outputText id='delayed' value='#{componentBean.component.delayed}'/><br/>");
        p.form("<h:outputText id='focusCandidates' value='#{componentBean.focusCandidates}'/><br/>");

        deployment.archive().addAsWebResource(p, "index.xhtml");
    }

    @Test
    public void testDefaultAttributes() {
        Graphene.guardHttp(browser).get(contextPath.toExternalForm());

        assertEquals("Component is ajaxRenderer='true' by default", ajaxRendered.getText(), Boolean.TRUE.toString());
        assertEquals("Component is validationAware='true' by default", validationAware.getText(),
            Boolean.TRUE.toString());
        assertEquals("Component is preserve='false' by default", preserve.getText(), Boolean.FALSE.toString());
        assertEquals("Component is delayed='false' by default", delayed.getText(), Boolean.FALSE.toString());
    }

    @Test
    public void testDefaultFocusCandidates() {
        Graphene.guardHttp(browser).get(contextPath.toExternalForm());
        assertEquals("There are no invalid components, whole form is candidate", "form", focusCandidates.getText());
    }
}

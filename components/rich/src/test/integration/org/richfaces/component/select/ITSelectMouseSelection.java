package org.richfaces.component.select;

import static org.jboss.arquillian.graphene.Graphene.waitAjax;
import static org.junit.Assert.assertEquals;

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
import org.richfaces.component.autocomplete.AutocompleteBean;
import org.richfaces.integration.RichDeployment;
import org.richfaces.shrinkwrap.descriptor.FaceletAsset;

@RunAsClient
@RunWith(Arquillian.class)
public class ITSelectMouseSelection {

    @Drone
    private WebDriver browser;

    @ArquillianResource
    private URL contextPath;

    @FindBy(className = "rf-sel-inp")
    private WebElement selectInput;

    @FindBy(css = ".rf-sel-opt:nth-of-type(2)")
    private WebElement tampaBayOption;

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        RichDeployment deployment = new RichDeployment(ITSelectMouseSelection.class);

        deployment.archive().addClasses(AutocompleteBean.class);

        addIndexPage(deployment);

        return deployment.getFinalArchive();
    }

    @Test
    public void test() {
        browser.get(contextPath.toExternalForm());

        selectInput.sendKeys("t");
        waitAjax().until().element(tampaBayOption).is().visible();

        tampaBayOption.click();

        assertEquals("Tampa Bay", selectInput.getAttribute("value"));
    }

    private static void addIndexPage(RichDeployment deployment) {
        FaceletAsset p = new FaceletAsset();

        p.body("<h:form id='form'>");
        p.body("    <rich:select id='select'  enableManualInput='true'>");
        p.body("        <f:selectItems value='#{autocompleteBean.suggestions}' />");
        p.body("    </rich:select>");
        p.body("</h:form>");

        deployment.archive().addAsWebResource(p, "index.xhtml");
    }
}

package org.richfaces.ui.select;

import static org.jboss.arquillian.graphene.Graphene.waitAjax;
import static org.jboss.arquillian.graphene.Graphene.waitGui;

import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.enricher.findby.FindBy;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.Keyboard;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.richfaces.integration.InputDeployment;
import org.richfaces.shrinkwrap.descriptor.FaceletAsset;
import org.richfaces.ui.autocomplete.AutocompleteBean;

@RunAsClient
@RunWith(Arquillian.class)
public class ITSelectKeyboardSelection {

    @Drone
    private WebDriver browser;

    @ArquillianResource
    private URL contextPath;

    @FindBy(className = "rf-sel-inp")
    private WebElement selectInput;

    @FindBy(css = ".rf-sel-opt:nth-of-type(2)")
    private WebElement tampaBayOption;

    @ArquillianResource
    private Keyboard keyboard;

    @Deployment
    public static WebArchive createDeployment() {
        InputDeployment deployment = new InputDeployment(ITSelectKeyboardSelection.class);

        deployment.archive().addClasses(AutocompleteBean.class).addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");

        addIndexPage(deployment);

        return deployment.getFinalArchive();
    }

    @Test
    public void test() {
        browser.get(contextPath.toExternalForm());

        selectInput.sendKeys("t");
        waitAjax().until().element(tampaBayOption).is().visible();

        keyboard.pressKey(Keys.ARROW_DOWN);
        waitAjax().until().element(tampaBayOption).attribute("class").contains("rf-sel-sel");

        keyboard.pressKey(Keys.ENTER);
        waitGui().until().element(selectInput).attribute("value").equalTo("Tampa Bay");
    }

    private static void addIndexPage(InputDeployment deployment) {
        FaceletAsset p = new FaceletAsset();

        p.body("<h:form id='form'>");
        p.body("    <r:select id='select'  enableManualInput='true'>");
        p.body("        <f:selectItems value='#{autocompleteBean.suggestions}' />");
        p.body("    </r:select>");
        p.body("</h:form>");

        deployment.archive().addAsWebResource(p, "index.xhtml");
    }
}

package org.richfaces.component.autocomplete;

import static org.jboss.arquillian.graphene.Graphene.guardAjax;
import static org.jboss.arquillian.graphene.Graphene.waitGui;
import static org.junit.Assert.assertFalse;

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
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.integration.RichDeployment;
import org.richfaces.shrinkwrap.descriptor.FaceletAsset;

@RunAsClient
@RunWith(Arquillian.class)
public class ITAutocompleteDestroy {

    @Drone
    WebDriver browser;

    @ArquillianResource
    URL contextPath;

    @FindBy(id = "form:render")
    WebElement renderButton;

    @FindBy(css = "input.rf-au-inp")
    WebElement autocompleteInput;

    By suggestionList = By.cssSelector(".rf-au-lst-cord");

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        RichDeployment deployment = new RichDeployment(ITAutocompleteDestroy.class);

        deployment.archive().addClasses(AutocompleteBean.class);

        addIndexPage(deployment);

        return deployment.getFinalArchive();
    }

    @Test
    // RF-12481
    public void when_suggestion_list_opened_and_autocomplete_is_rerendered_then_it_should_not_be_visible() {
        // given
        browser.get(contextPath.toExternalForm());
        autocompleteInput.sendKeys("t");
        waitGui().withMessage("suggestion list is visible").until().element(suggestionList).is().visible();

        // when
        guardAjax(renderButton).click();

        // then
        assertFalse("suggestion list is not displayed", browser.findElement(suggestionList).isDisplayed());
    }

    private static void addIndexPage(RichDeployment deployment) {
        FaceletAsset p = new FaceletAsset();

        p.body("<h:form id='form'>");

        p.body("    <h:commandButton id='render' value='Render'>");
        p.body("        <f:ajax execute='@form' render='@form'/>");
        p.body("    </h:commandButton>");

        p.body("    <rich:autocomplete autocompleteList='#{autocompleteBean.suggestions}' />");

        p.body("</h:form>");

        deployment.archive().addAsWebResource(p, "index.xhtml");
    }
}

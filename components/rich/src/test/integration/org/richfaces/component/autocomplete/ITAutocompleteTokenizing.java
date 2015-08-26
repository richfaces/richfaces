package org.richfaces.component.autocomplete;

import static org.jboss.arquillian.graphene.Graphene.waitGui;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.net.URL;
import java.util.List;

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
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.richfaces.integration.RichDeployment;
import org.richfaces.shrinkwrap.descriptor.FaceletAsset;

@RunAsClient
@RunWith(Arquillian.class)
public class ITAutocompleteTokenizing {

    @Drone
    private WebDriver browser;

    @ArquillianResource
    private URL contextPath;

    @FindBy(css = "input.rf-au-inp")
    private WebElement autocompleteInput;

    @FindBy(css = ".rf-au-itm")
    private List<WebElement> autocompleteItems;

    @ArquillianResource
    private Actions actions;

    @FindBy(css = ".rf-au-lst-cord")
    WebElement suggestionList;

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        RichDeployment deployment = new RichDeployment(ITAutocompleteTokenizing.class);

        deployment.archive().addClasses(AutocompleteBean.class);

        addIndexPage(deployment);

        return deployment.getFinalArchive();
    }

    @Test
    public void testAutofillDisabledSelectionByMouse() {
        browser.get(contextPath.toExternalForm() + "?autofill=false");
        actions.moveToElement(autocompleteInput).perform();

        // selenium workaround
        // cannot send these keys together, selenium will trigger keyup event after 't' letter
        // and there will be only 2 suggestions (beggining with 't') instead of 4 suggestions
        autocompleteInput.sendKeys("t");
        autocompleteInput.sendKeys(",");

        waitGui().until().element(suggestionList).is().visible();
        assertEquals(4, autocompleteItems.size());
        assertEquals("t,", autocompleteInput.getAttribute("value"));

        WebElement secondItem = autocompleteItems.get(1);

        actions.moveToElement(secondItem).perform();
        waitGui().until().element(secondItem).attribute("class").contains("rf-au-itm-sel");
        assertTrue(suggestionList.isDisplayed());
        assertEquals(4, autocompleteItems.size());
        assertEquals("t,", autocompleteInput.getAttribute("value"));

        secondItem.click();
        waitGui().until().element(suggestionList).is().not().visible();
        assertEquals("t,New York", autocompleteInput.getAttribute("value"));

        autocompleteInput.sendKeys(", ");
        waitGui().until().element(suggestionList).is().visible();
        assertEquals(4, autocompleteItems.size());
        assertEquals("t,New York, ", autocompleteInput.getAttribute("value"));

        WebElement thirdItem = autocompleteItems.get(2);

        actions.moveToElement(thirdItem).perform();
        waitGui().until().element(thirdItem).attribute("class").contains("rf-au-itm-sel");
        assertTrue(suggestionList.isDisplayed());
        assertEquals(4, autocompleteItems.size());
        assertEquals("t,New York, ", autocompleteInput.getAttribute("value"));

        thirdItem.click();
        waitGui().until().element(suggestionList).is().not().visible();
        assertEquals("t,New York, San Francisco", autocompleteInput.getAttribute("value"));
    }

    @Test
    public void testAutofillEnabledSelectionByMouse() {
        browser.get(contextPath.toExternalForm() + "?autofill=true");
        actions.moveToElement(autocompleteInput).perform();

        // selenium workaround
        // cannot send these keys together, selenium will trigger keyup event after 't' letter
        // and there will be only 2 suggestions (beggining with 't') instead of 4 suggestions
        autocompleteInput.sendKeys("t");
        autocompleteInput.sendKeys(",");

        waitGui().until().element(suggestionList).is().visible();
        assertEquals(4, autocompleteItems.size());
        assertEquals("t,Toronto", autocompleteInput.getAttribute("value"));

        WebElement secondItem = autocompleteItems.get(1);

        actions.moveToElement(secondItem).perform();
        waitGui().until().element(autocompleteInput).attribute("value").equalTo("t,New York");
        assertTrue(suggestionList.isDisplayed());
        assertEquals(4, autocompleteItems.size());

        secondItem.click();
        waitGui().until().element(suggestionList).is().not().visible();
        assertEquals("t,New York", autocompleteInput.getAttribute("value"));

        actions.moveToElement(autocompleteInput).perform();
        autocompleteInput.sendKeys(", ");
        waitGui().until().element(suggestionList).is().visible();
        assertEquals(4, autocompleteItems.size());
        assertEquals("t,New York, Toronto", autocompleteInput.getAttribute("value"));

        WebElement thirdItem = autocompleteItems.get(2);

        actions.moveToElement(thirdItem).perform();
        waitGui().until().element(autocompleteInput).attribute("value").equalTo("t,New York, San Francisco");
        assertTrue(suggestionList.isDisplayed());
        assertEquals(4, autocompleteItems.size());

        thirdItem.click();
        waitGui().until().element(suggestionList).is().not().visible();
        assertEquals("t,New York, San Francisco", autocompleteInput.getAttribute("value"));
    }

    @Test
    public void when_space_is_not_token_then_it_should_not_be_used_to_separate_input() {
        browser.get(contextPath.toExternalForm() + "?autofill=false");

        autocompleteInput.sendKeys("t");
        waitGui().until().element(suggestionList).is().visible();

        autocompleteInput.sendKeys(" ");
        waitGui().until().element(suggestionList).is().not().visible();
    }

    private static void addIndexPage(RichDeployment deployment) {
        FaceletAsset p = new FaceletAsset();

        p.body("<h:form id='form'>");
        p.body("    <rich:autocomplete id='autocomplete' mode='client' autocompleteList='#{autocompleteBean.suggestions}' tokens=',' autofill='#{param.autofill}' />");
        p.body("</h:form>");

        deployment.archive().addAsWebResource(p, "index.xhtml");
    }
}

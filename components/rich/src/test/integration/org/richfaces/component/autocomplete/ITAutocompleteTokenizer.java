package org.richfaces.component.autocomplete;

import static org.jboss.arquillian.graphene.Graphene.waitGui;
import static org.junit.Assert.assertEquals;

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
public class ITAutocompleteTokenizer {

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
        RichDeployment deployment = new RichDeployment(ITAutocompleteTokenizer.class);

        deployment.archive().addClasses(AutocompleteBean.class);

        addIndexPage(deployment);

        return deployment.getFinalArchive();
    }
    
    
    @Test
    public void testSpaceTokenizer(){
        browser.get(contextPath.toExternalForm()+"?token=%20");
        
        autocompleteInput.sendKeys("N ");
        waitGui().until().element(suggestionList).is().visible();
        //There should be displayed all items from input list because space is token
        assertEquals(4, autocompleteItems.size());
        
        autocompleteInput.sendKeys("New");
        waitGui().until().element(suggestionList).is().visible();   
        //There should be displayed only New York as suggested input
        assertEquals(1, autocompleteItems.size());
        
        autocompleteInput.sendKeys(" ");
        waitGui().until().element(suggestionList).is().visible();
        //Again all 4 items should be displayed because space is token
        assertEquals(4, autocompleteItems.size());
        
        autocompleteInput.sendKeys("Y");
        waitGui().until().element(suggestionList).is().not().visible();
        //Now there should be displayed only one item = New York
        assertEquals(0, autocompleteItems.size());    
        
        autocompleteInput.clear();
        autocompleteInput.sendKeys("New ");
        waitGui().until().element(suggestionList).is().visible();
        assertEquals(4, autocompleteItems.size());    
        
        WebElement secondItem = autocompleteItems.get(1);
        actions.moveToElement(secondItem).perform();
        waitGui().until().element(suggestionList).is().visible();
        assertEquals(4, autocompleteItems.size());
        
    }
    
    @Test
    public void testColonsTokenizer(){
        String token = ":";
        browser.get(contextPath.toExternalForm()+"?token="+token);
        testToken(token);
    }
    
    @Test
    public void testSemiColonsTokenizer(){
        String token = ";";
        browser.get(contextPath.toExternalForm()+"?token="+token);
        testToken(token);
    }
    
    /**
     * This method test token. Use it several ways. It work for all type of tokens expect space.
     * @param token which is used as separator for autocomplete 
     */
    private void testToken(String token){
        autocompleteInput.sendKeys("N"+token);
        waitGui().until().element(suggestionList).is().visible();   
        //In this point there must be displayed all items
        assertEquals(4, autocompleteItems.size());
        
        autocompleteInput.sendKeys("New");
        waitGui().until().element(suggestionList).is().visible();
        //Now only New York should be shown
        assertEquals(1, autocompleteItems.size());
        
        autocompleteInput.sendKeys(" ");
        waitGui().until().element(suggestionList).is().visible();
        //Still only New York should be shown
        assertEquals(1, autocompleteItems.size());
        
        autocompleteInput.sendKeys("Y");
        waitGui().until().element(suggestionList).is().visible();
        //Still only New York should be shown
        assertEquals(1, autocompleteItems.size());       
        //Choose New York from list
        WebElement firstItem = autocompleteItems.get(0);
        actions.moveToElement(firstItem).perform();
        //Add token an write T
        autocompleteInput.sendKeys(token+"T");
        waitGui().until().element(suggestionList).is().visible();   
        //There should be displayed only two options Toronto and Tamba Bay
        assertEquals(2, autocompleteItems.size());
    }
    
    
    private static void addIndexPage(RichDeployment deployment) {
        FaceletAsset p = new FaceletAsset();

        p.body("<h:form id='form'>");
        p.body("    <rich:autocomplete id='autocomplete' mode='client' autocompleteList='#{autocompleteBean.suggestions}' tokens='#{param.token}' autofill='false' />");
        p.body("</h:form>");

        deployment.archive().addAsWebResource(p, "index.xhtml");
    }
}

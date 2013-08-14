package org.richfaces.component.extendedDataTable;

import java.net.URL;
import java.util.List;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.integration.IterationDeployment;
import org.richfaces.shrinkwrap.descriptor.FaceletAsset;

@RunAsClient
@RunWith(Arquillian.class)
public class TestBuiltInFilter {

    @Drone
    private WebDriver browser;

    @ArquillianResource
    private URL contextPath;

    @FindBy(id = "myForm:edt")
    private WebElement edt;

    @FindBy(id = "myForm:edt:0:n")
    private WebElement firstRow;

    @FindBy(id = "myForm:ajax")
    private WebElement button;

    @FindBy(id = "myForm:edt:header")
    private WebElement header;

    @Deployment
    public static WebArchive createDeployment() {
        IterationDeployment deployment = new IterationDeployment(TestBuiltInFilter.class);
        deployment.archive().addClass(IterationBuiltInBean.class);
        addIndexPage(deployment);

        return deployment.getFinalArchive();
    }

    @Test
    public void table_filter() throws InterruptedException {
        // given
        browser.get(contextPath.toExternalForm());

        List<WebElement> cells = browser.findElements(By.cssSelector(".rf-edt-c-column2 .rf-edt-c-cnt"));
        Assert.assertEquals("3", cells.get(0).getText());
        Assert.assertEquals(10, cells.size());

        WebElement filterInput = browser.findElement(By.id("myForm:edt:column2:flt"));
        filterInput.clear();
        filterInput.sendKeys("3");
        filterInput.sendKeys(Keys.TAB);
        Thread.sleep(500);
        cells = browser.findElements(By.cssSelector(".rf-edt-c-column2 .rf-edt-c-cnt"));
        Assert.assertEquals("3", cells.get(0).getText());
        Assert.assertEquals(4, cells.size());
    }

    private static void addIndexPage(IterationDeployment deployment) {
        FaceletAsset p = new FaceletAsset();
        p.xmlns("rich", "http://richfaces.org/iteration");
        p.xmlns("a4j", "http://richfaces.org/a4j");

        p.body("<script type='text/javascript'>");
        p.body("function filterEdt(filterValue) { ");
        p.body("  var edt = RichFaces.$('myForm:edt'); ");
        p.body("  edt.filter('column2', filterValue, true); ");
        p.body("} ");
        p.body("</script>");
        p.body("<h:form id='myForm'> ");
        p.body("    <rich:extendedDataTable id='edt' value='#{iterationBuiltInBean.values}' var='bean' filterVar='fv' > ");
        p.body("        <rich:column id='column1' width='150px' > ");
        p.body("            <f:facet name='header'>Column 1</f:facet> ");
        p.body("            <h:outputText value='Bean:' /> ");
        p.body("        </rich:column> ");
        p.body("        <rich:column id='column2' width='150px' ");
        p.body("                         filterValue='#{iterationBuiltInBean.filterValue}' ");
        p.body("                         filterExpression='#{bean le fv}' > ");
        p.body("            <f:facet name='header'>Column 2</f:facet> ");
        p.body("            <h:outputText value='#{bean}' /> ");
        p.body("        </rich:column> ");
        p.body("        <rich:column id='column3' width='150px'> ");
        p.body("            <f:facet name='header'>Column 3</f:facet> ");
        p.body("            <h:outputText value='Row #{bean}, Column 3' /> ");
        p.body("        </rich:column> ");
        p.body("    </rich:extendedDataTable> ");
        p.body("    <a4j:commandButton id='ajax' execute='edt' render='edt' value='Ajax' /> ");
        p.body("</h:form> ");

        deployment.archive().addAsWebResource(p, "index.xhtml");
    }

}
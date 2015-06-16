package org.richfaces.component.extendedDataTable;

import static org.jboss.arquillian.graphene.Graphene.guardAjax;
import static org.junit.Assert.assertEquals;

import java.net.URL;
import java.util.List;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
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
public class ITBuiltInFilter {

    @Drone
    private WebDriver browser;
    @ArquillianResource
    private URL contextPath;

    @FindBy(id = "blur")
    private WebElement blurButton;
    @FindBy(css = ".rf-edt-c-column2 .rf-edt-c-cnt")
    private List<WebElement> column2CellsElements;
    @FindBy(id = "myForm:edt:column2:flt")
    private WebElement filterInput;
    @FindBy(className = "rf-edt-flt-i")
    private List<WebElement> filtersElements;
    @FindBy(id = "myForm:edt:header")
    private WebElement header;
    @FindBy(id = "myForm:ajax")
    private WebElement setFilterValueTo6Button;

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        RichDeployment deployment = new RichDeployment(ITBuiltInFilter.class);
        deployment.archive().addClass(IterationBuiltInBean.class);
        addIndexPage(deployment);

        return deployment.getFinalArchive();
    }

    @Test
    public void check_builtin_filters_present() {
        // given
        browser.get(contextPath.toExternalForm());

        assertEquals("Only one built-in filter should be present", 1, filtersElements.size());

        List<WebElement> column2BuiltinFilter = header.findElements(By.cssSelector(".rf-edt-c-column2 .rf-edt-flt-i"));
        assertEquals("Built-in filter for column2 should be present", 1, column2BuiltinFilter.size());
        List<WebElement> column3BuiltinFilter = header.findElements(By.cssSelector(".rf-edt-c-column3 .rf-edt-flt-i"));
        assertEquals("Built-in filter for column3 is should not be present", 0, column3BuiltinFilter.size());
    }

    @Test
    public void check_filter_is_applied() throws InterruptedException {
        // given
        browser.get(contextPath.toExternalForm());

        assertEquals("Only one built-in filter should be present", 1, filtersElements.size());
        assertEquals("Value of the first cell of the second column", "3", column2CellsElements.get(0).getText());
        assertEquals("Number of rows present", 10, column2CellsElements.size());

        filterInput.clear();
        filterInput.sendKeys("3");
        guardAjax(blurButton).click();
        assertEquals("Value of the first cell of the second column", "3", column2CellsElements.get(0).getText());
        assertEquals("Number of rows present", 4, column2CellsElements.size());
    }

    @Test
    public void check_filter_is_cleared() {
        // given
        browser.get(contextPath.toExternalForm());

        assertEquals("Only one built-in filter should be present", 1, filtersElements.size());
        assertEquals("Number of rows present", 10, column2CellsElements.size());

        filterInput.clear();
        filterInput.sendKeys("3");
        guardAjax(blurButton).click();

        assertEquals("Number of rows present", 4, column2CellsElements.size());

        guardAjax(setFilterValueTo6Button).click();
        assertEquals("Number of rows present", 7, column2CellsElements.size());

        assertEquals("Filter input value should be backing bean value", "6", filterInput.getAttribute("value"));
    }

    private static void addIndexPage(RichDeployment deployment) {
        FaceletAsset p = new FaceletAsset();
        p.xmlns("fn", "http://java.sun.com/jsp/jstl/functions");

        p.body("<script type='text/javascript'>");
        p.body("function filterEdt(filterValue) {");
        p.body("  var edt = RichFaces.$('myForm:edt');");
        p.body("  edt.filter('column2', filterValue, true);");
        p.body("}");
        p.body("</script>");
        p.body("<h:form id='myForm'>");
        p.body("    <rich:extendedDataTable id='edt' value='#{iterationBuiltInBean.values}' var='bean' filterVar='fv' >");
        p.body("        <rich:column id='column1' width='150px' >");
        p.body("            <f:facet name='header'>Column 1</f:facet>");
        p.body("            <h:outputText value='Bean:' />");
        p.body("        </rich:column>");
        p.body("        <rich:column id='column2' width='150px' ");
        p.body("                         filterValue='#{iterationBuiltInBean.filterValue}' ");
        p.body("                         filterExpression='#{fv eq null or bean le fv}' >");
        p.body("            <f:facet name='header'>Column 2</f:facet>");
        p.body("            <h:outputText value='#{bean}' />");
        p.body("        </rich:column>");
        p.body("        <rich:column id='column3' width='150px' ");
        p.body("                         filterValue='#{iterationBuiltInBean.filterValue2}' ");
        p.body("                         filterExpression='#{fn:startsWith(bean,fv)}' ");
        p.body("                         filterType='custom' >");
        p.body("            <f:facet name='header'>Column 3</f:facet>");
        p.body("            <h:outputText value='Row #{bean}, Column 3' />");
        p.body("        </rich:column>");
        p.body("    </rich:extendedDataTable>");
        p.body("    <br/>");
        p.body("    <a4j:commandButton id='ajax' execute='edt' render='edt' value='Ajax' action='#{iterationBuiltInBean.setFilterValue(6)}' />");
        p.body("    <br/>");
        p.body("    <input id='blur' value='blur' type='button' />");
        p.body("</h:form>");

        deployment.archive().addAsWebResource(p, "index.xhtml");
    }
}

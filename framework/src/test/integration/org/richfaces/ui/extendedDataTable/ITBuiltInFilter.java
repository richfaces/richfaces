package org.richfaces.ui.extendedDataTable;

import static org.jboss.arquillian.graphene.Graphene.guardAjax;

import java.net.URL;
import java.util.List;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.arquillian.warp.Activity;
import org.jboss.arquillian.warp.Inspection;
import org.jboss.arquillian.warp.Warp;
import org.jboss.arquillian.warp.WarpTest;
import org.jboss.arquillian.warp.jsf.AfterPhase;
import org.jboss.arquillian.warp.jsf.Phase;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.deployment.FrameworkDeployment;
import org.richfaces.shrinkwrap.descriptor.FaceletAsset;

@RunAsClient
@WarpTest
@RunWith(Arquillian.class)
public class ITBuiltInFilter {

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
        FrameworkDeployment deployment = new FrameworkDeployment(ITBuiltInFilter.class);
        deployment.archive().addClass(IterationBuiltInBean.class);
        addIndexPage(deployment);

        return deployment.getFinalArchive();
    }

    @Test
    public void check_builtin_filters_present() throws InterruptedException {
        // given
        browser.get(contextPath.toExternalForm());

        browser.findElements(By.className("rf-edt-flt-i"));

        WebElement tableHeader = browser.findElement(By.className("rf-edt-hdr"));
        List<WebElement> column2BuiltinFilter = tableHeader.findElements(By.cssSelector(".rf-edt-c-column2 .rf-edt-flt-i"));
        Assert.assertEquals("Built-in filter for column2 should be present", 1, column2BuiltinFilter.size());
        List<WebElement> column3BuiltinFilter = tableHeader.findElements(By.cssSelector(".rf-edt-c-column3 .rf-edt-flt-i"));
        Assert.assertEquals("Built-in filter for column3 is should not be present", 0, column3BuiltinFilter.size());
    }

    @Test
    public void check_filter_is_cleared() throws InterruptedException {
        // given
        browser.get(contextPath.toExternalForm());

        browser.findElements(By.className("rf-edt-flt-i"));

        List<WebElement> cells = browser.findElements(By.cssSelector(".rf-edt-c-column2 .rf-edt-c-cnt"));
        Assert.assertEquals("Number of rows present", 10, cells.size());

        final WebElement filterInput = browser.findElement(By.id("myForm:edt:column2:flt"));
        filterInput.clear();
        filterInput.sendKeys("3");
        Warp.initiate(new Activity() {
            @Override
            public void perform() {
                filterInput.sendKeys(Keys.TAB);
            }
        }).inspect(new Inspection() {
            private static final long serialVersionUID = 1L;

            @Inject IterationBuiltInBean iterationBuiltInBean;

            @AfterPhase(Phase.INVOKE_APPLICATION)
            public void verify_bean_filter_cleared() {
                Assert.assertEquals("Backing bean filterValue should be set", 3, (long) iterationBuiltInBean.getFilterValue());
            }
        });

        cells = browser.findElements(By.cssSelector(".rf-edt-c-column2 .rf-edt-c-cnt"));
        Assert.assertEquals("Number of rows present", 4, cells.size());

        Warp.initiate(new Activity() {
            @Override
            public void perform() {
                button.click();
            }
        }).inspect(new Inspection() {
            private static final long serialVersionUID = 1L;

            @Inject IterationBuiltInBean iterationBuiltInBean;

            @AfterPhase(Phase.INVOKE_APPLICATION)
            public void verify_bean_filter_cleared() {
                Assert.assertEquals("Backing bean filterValue should be set", 6, (long) iterationBuiltInBean.getFilterValue());
            }
        });

        cells = browser.findElements(By.cssSelector(".rf-edt-c-column2 .rf-edt-c-cnt"));
        Assert.assertEquals("Number of rows present", 7, cells.size());

        final WebElement filterInput2 = browser.findElement(By.id("myForm:edt:column2:flt"));
        Assert.assertEquals("Filter input value should be backing bean value", "6", filterInput2.getAttribute("value"));
    }

    @Test
    public void check_filter_is_applied() throws InterruptedException {
        // given
        browser.get(contextPath.toExternalForm());

        browser.findElements(By.className("rf-edt-flt-i"));

        List<WebElement> cells = browser.findElements(By.cssSelector(".rf-edt-c-column2 .rf-edt-c-cnt"));
        Assert.assertEquals("Value of the first cell of the second column", "3", cells.get(0).getText());
        Assert.assertEquals("Number of rows present", 10, cells.size());

        WebElement filterInput = browser.findElement(By.id("myForm:edt:column2:flt"));
        filterInput.clear();
        filterInput.sendKeys("3");
        guardAjax(filterInput).sendKeys(Keys.TAB);
        cells = browser.findElements(By.cssSelector(".rf-edt-c-column2 .rf-edt-c-cnt"));
        Assert.assertEquals("Value of the first cell of the second column", "3", cells.get(0).getText());
        Assert.assertEquals("Number of rows present", 4, cells.size());
    }

    private static void addIndexPage(FrameworkDeployment deployment) {
        FaceletAsset p = new FaceletAsset();

        p.body("<script type='text/javascript'>");
        p.body("function filterEdt(filterValue) { ");
        p.body("  var edt = RichFaces.$('myForm:edt'); ");
        p.body("  edt.filter('column2', filterValue, true); ");
        p.body("} ");
        p.body("</script>");
        p.body("<h:form id='myForm'> ");
        p.body("    <r:extendedDataTable id='edt' value='#{iterationBuiltInBean.values}' var='bean' filterVar='fv' > ");
        p.body("        <r:column id='column1' width='150px' > ");
        p.body("            <f:facet name='header'>Column 1</f:facet> ");
        p.body("            <h:outputText value='Bean:' /> ");
        p.body("        </r:column> ");
        p.body("        <r:column id='column2' width='150px' ");
        p.body("                         filterValue='#{iterationBuiltInBean.filterValue}' ");
        p.body("                         filterExpression='#{fv eq null or bean le fv}' > ");
        p.body("            <f:facet name='header'>Column 2</f:facet> ");
        p.body("            <h:outputText value='#{bean}' /> ");
        p.body("        </r:column> ");
        p.body("        <r:column id='column3' width='150px' ");
        p.body("                         filterValue='#{iterationBuiltInBean.filterValue2}' ");
        p.body("                         filterExpression='#{fn:startsWith(bean,fv)}' ");
        p.body("                         filterType='custom' > ");
        p.body("            <f:facet name='header'>Column 3</f:facet> ");
        p.body("            <h:outputText value='Row #{bean}, Column 3' /> ");
        p.body("        </r:column> ");
        p.body("    </r:extendedDataTable> ");
        p.body("    <r:commandButton id='ajax' execute='edt' render='edt' value='Ajax' action='#{iterationBuiltInBean.setFilterValue(6)}' /> ");
        p.body("</h:form> ");

        deployment.archive().addAsWebResource(p, "index.xhtml");
    }

}
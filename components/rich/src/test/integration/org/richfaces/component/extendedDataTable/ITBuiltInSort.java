package org.richfaces.component.extendedDataTable;

import static org.jboss.arquillian.graphene.Graphene.guardAjax;

import java.net.URL;

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
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.integration.RichDeployment;
import org.richfaces.shrinkwrap.descriptor.FaceletAsset;

@RunAsClient
@RunWith(Arquillian.class)
public class ITBuiltInSort {

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

    @FindBy(className = "rf-edt-srt")
    private WebElement sortHandle;

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        RichDeployment deployment = new RichDeployment(ITBuiltInSort.class);
        deployment.archive().addClass(IterationBuiltInBean.class);
        addIndexPage(deployment);

        return deployment.getFinalArchive();
    }

    @Test
    public void table_sort() throws InterruptedException {
        // given
        browser.get(contextPath.toExternalForm());
        WebElement cell = browser.findElements(By.cssSelector(".rf-edt-c-column2 .rf-edt-c-cnt")).get(0);
        Assert.assertEquals("3", cell.getText());

        guardAjax(sortHandle).click();
        Thread.sleep(500);
        cell = browser.findElements(By.cssSelector(".rf-edt-c-column2 .rf-edt-c-cnt")).get(0);
        Assert.assertEquals("0", cell.getText());

        guardAjax(sortHandle).click();
        Thread.sleep(500);
        cell = browser.findElements(By.cssSelector(".rf-edt-c-column2 .rf-edt-c-cnt")).get(0);
        Assert.assertEquals("9", cell.getText());

        guardAjax(sortHandle).click();
        Thread.sleep(500);
        cell = browser.findElements(By.cssSelector(".rf-edt-c-column2 .rf-edt-c-cnt")).get(0);
        Assert.assertEquals("0", cell.getText());

    }

    private static void addIndexPage(RichDeployment deployment) {
        FaceletAsset p = new FaceletAsset();

        p.body("<script type='text/javascript'>");
        p.body("function sortEdt(currentSortOrder) { ");
        p.body("  var edt = RichFaces.component('myForm:edt'); ");
        p.body("  var sortOrder = currentSortOrder == 'ascending' ? 'descending' : 'ascending'; ");
        p.body("  edt.sort('column2', sortOrder, true); ");
        p.body("} ");
        p.body("</script>");
        p.body("<h:form id='myForm'> ");
        p.body("    <rich:extendedDataTable id='edt' value='#{iterationBuiltInBean.values}' var='bean' > ");
        p.body("        <rich:column id='column1' width='150px' > ");
        p.body("            <f:facet name='header'>Column 1</f:facet> ");
        p.body("            <h:outputText value='Bean:' /> ");
        p.body("        </rich:column> ");
        p.body("        <rich:column id='column2' width='150px' ");
        p.body("                         sortBy='#{bean}' ");
        p.body("                         sortOrder='#{iterationBuiltInBean.sortOrder}' > ");
        p.body("            <f:facet name='header'>Column 2</f:facet> ");
        p.body("            <h:outputText value='#{bean}' /> ");
        p.body("        </rich:column> ");
        p.body("        <rich:column id='column3' width='150px'" );
        p.body("                     sortBy='#{bean}' ");
        p.body("                     sortOrder='#{iterationBuiltInBean.sortOrder2}' > ");
        p.body("            <f:facet name='header'>Column 3</f:facet> ");
        p.body("            <h:outputText value='Row #{bean}, Column 3' /> ");
        p.body("        </rich:column> ");
        p.body("    </rich:extendedDataTable> ");
        p.body("    <a4j:commandButton id='ajax' execute='edt' render='edt' value='Ajax' /> ");
        p.body("</h:form> ");

        deployment.archive().addAsWebResource(p, "index.xhtml");
    }

}

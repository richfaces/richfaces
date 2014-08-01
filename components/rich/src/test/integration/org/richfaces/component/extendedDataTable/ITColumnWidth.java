package org.richfaces.component.extendedDataTable;

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
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.richfaces.integration.RichDeployment;
import org.richfaces.shrinkwrap.descriptor.FaceletAsset;

@RunAsClient
@RunWith(Arquillian.class)
public class ITColumnWidth {

    @Drone
    private WebDriver browser;

    @ArquillianResource
    private URL contextPath;

    @FindBy(id = "myForm:edt")
    private WebElement edt;

    @FindBy(id = "myForm:edt:header")
    private WebElement header;

    @FindBy(id = "myForm:edt:0:n")
    private WebElement firstRow;

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        RichDeployment deployment = new RichDeployment(ITColumnWidth.class);
        deployment.archive().addClass(IterationBean.class);
        addIndexPage(deployment);

        return deployment.getFinalArchive();
    }

    @Test
    public void setting_column_width() {
        browser.get(contextPath.toExternalForm());
        Assert.assertEquals("200px", firstRow.findElement(By.cssSelector("td .rf-edt-c-column1")).getCssValue("width"));
    }

    @Test
    public void column_resize_smaller() {
        browser.get(contextPath.toExternalForm());
        WebElement column1ResizeHandle = header.findElement(By.cssSelector(".rf-edt-hdr .rf-edt-td-column1 .rf-edt-rsz"));

        Actions builder = new Actions(browser);
        final Action dragAndDrop = builder.dragAndDropBy(column1ResizeHandle, -20, 0).build();
        dragAndDrop.perform();

        Assert.assertEquals("181px", firstRow.findElement(By.cssSelector("td")).getCssValue("width"));
        Assert.assertEquals("180px", firstRow.findElement(By.cssSelector("td .rf-edt-c-column1")).getCssValue("width"));
    }

    @Test
    public void column_resize_bigger() {
        browser.get(contextPath.toExternalForm());
        WebElement column1ResizeHandle = header.findElement(By.cssSelector(".rf-edt-hdr .rf-edt-td-column1 .rf-edt-rsz"));

        Actions builder = new Actions(browser);
        final Action dragAndDrop = builder.dragAndDropBy(column1ResizeHandle, 20, 0).build();
        dragAndDrop.perform();

        Assert.assertEquals("221px", firstRow.findElement(By.cssSelector("td")).getCssValue("width"));
        Assert.assertEquals("220px", firstRow.findElement(By.cssSelector("td .rf-edt-c-column1")).getCssValue("width"));
    }

    private static void addIndexPage(RichDeployment deployment) {
        FaceletAsset p = new FaceletAsset();

        p.body("<h:form id='myForm'>");
        p.body("    <rich:extendedDataTable id='edt' value='#{iterationBean.values}' var='bean'>");
        p.body("        <rich:column id='column1' width='200px'>");
        p.body("            <f:facet name='header'> ");
        p.body("                <h:outputText value='Long header 1'/> ");
        p.body("             </f:facet> ");
        p.body("            <h:outputText value='Bean:' />");
        p.body("        </rich:column>");
        p.body("        <rich:column id='column2'>");
        p.body("            <f:facet name='header'> ");
        p.body("                <h:outputText value='Long header 2'/> ");
        p.body("             </f:facet> ");
        p.body("            <h:outputText value='#{bean}' />");
        p.body("        </rich:column>");
        p.body("        <rich:column id='column3'>");
        p.body("            <f:facet name='header'> ");
        p.body("                <h:outputText value='Long header 3'/> ");
        p.body("             </f:facet> ");
        p.body("            <h:outputText value='#{bean}' />");
        p.body("        </rich:column>");
        p.body("    </rich:extendedDataTable>");
        p.body("</h:form>");

        deployment.archive().addAsWebResource(p, "index.xhtml");
    }
}
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
import org.openqa.selenium.support.FindBy;
import org.richfaces.integration.RichDeployment;
import org.richfaces.shrinkwrap.descriptor.FaceletAsset;

@RunAsClient
@RunWith(Arquillian.class)
public class ITCellWidthWithMaxTableWidth {

    @Drone
    private WebDriver browser;

    @ArquillianResource
    private URL contextPath;

    @FindBy(id = "myForm:edt:0:n")
    private WebElement firstRow;

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        RichDeployment deployment = new RichDeployment(ITCellWidthWithMaxTableWidth.class);
        deployment.archive().addClass(IterationBean.class);
        addIndexPage(deployment);

        return deployment.getFinalArchive();
    }

    @Test
    public void setting_column_width() {
        browser.get(contextPath.toExternalForm());
        Assert.assertEquals("101px", firstRow.findElement(By.cssSelector("td")).getCssValue("width"));
    }

    private static void addIndexPage(RichDeployment deployment) {
        FaceletAsset p = new FaceletAsset();

        p.body("<h:outputStylesheet>  table { width: 100%; }  </h:outputStylesheet>");
        p.body("<h:form id='myForm'>");
        p.body("    <rich:extendedDataTable id='edt' value='#{iterationBean.values}' var='bean'>");
        p.body("        <rich:column id='column1'>");
        p.body("            <h:outputText value='Bean:' />");
        p.body("        </rich:column>");
        p.body("        <rich:column id='column2'>");
        p.body("            <h:outputText value='#{bean}' />");
        p.body("        </rich:column>");
        p.body("    </rich:extendedDataTable>");
        p.body("</h:form>");

        deployment.archive().addAsWebResource(p, "index.xhtml");
    }
}

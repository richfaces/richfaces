package org.richfaces.component.extendedDataTable;

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
import org.richfaces.integration.IterationDeployment;
import org.richfaces.shrinkwrap.descriptor.FaceletAsset;

import java.net.URL;

import static org.jboss.arquillian.graphene.Graphene.guardXhr;
import static org.jboss.arquillian.graphene.Graphene.waitAjax;

@RunAsClient
@RunWith(Arquillian.class)
public class TestTableState {

    @Drone
    private WebDriver browser;

    @ArquillianResource
    private URL contextPath;

    @FindBy(id = "myForm:edt")
    private WebElement edt;

    @FindBy(id = "myForm:edt:0:n")
    private WebElement firstRow;

    @Deployment
    public static WebArchive createDeployment() {
        IterationDeployment deployment = new IterationDeployment(TestTableState.class);
        deployment.archive().addClass(IterationBean.class);
        addIndexPage(deployment);

        return deployment.getFinalArchive();
    }

    @Test
    public void set_width_using_table_state() {
        browser.get(contextPath.toExternalForm());
        // assert the columns widths (selectors are independent of the column order)
        Assert.assertEquals("210px", firstRow.findElement(By.cssSelector("td .rf-edt-c-column1")).getCssValue("width"));
        Assert.assertEquals("75px", firstRow.findElement(By.cssSelector("td .rf-edt-c-column2")).getCssValue("width"));
    }

    @Test
    public void set_order_using_table_state() {
        browser.get(contextPath.toExternalForm());
        Assert.assertEquals("0", firstRow.findElement(By.cssSelector("td")).getText());
    }


    private static void addIndexPage(IterationDeployment deployment) {
        FaceletAsset p = new FaceletAsset();
        p.xmlns("rich", "http://richfaces.org/iteration");
        p.xmlns("a4j", "http://richfaces.org/a4j");

        p.body("<h:form id='myForm'>");
        p.body("    <rich:extendedDataTable id='edt' value='#{iterationBean.values}' var='bean' ");
        p.body("            columnsOrder='#{iterationBean.columnsOrder}'");
        p.body("            tableState='#{iterationBean.state}'>");
        p.body("        <rich:column id='column1' width='50px'>");
        p.body("            <f:facet name='header'>Text</f:facet>");
        p.body("            <h:outputText value='Bean:' />");
        p.body("        </rich:column>");
        p.body("        <rich:column id='column2' width='50px'>");
        p.body("            <f:facet name='header'>Value</f:facet>");
        p.body("            <h:outputText value='#{bean}' />");
        p.body("        </rich:column>");
        p.body("    </rich:extendedDataTable>");
        p.body("</h:form>");

        deployment.archive().addAsWebResource(p, "index.xhtml");
    }
}

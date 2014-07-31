package org.richfaces.component.extendedDataTable;

import java.net.URL;
import java.util.List;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.Graphene;
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
public class ITColumnsOrder {

    @Drone
    private WebDriver browser;

    @ArquillianResource
    private URL contextPath;

    @FindBy(id = "button")
    private WebElement button;

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        RichDeployment deployment = new RichDeployment(ITColumnsOrder.class);
        deployment.archive().addClass(IterationBean.class);
        addIndexPage(deployment);

        return deployment.getFinalArchive();
    }

    @Test
    public void check_columns_order() throws InterruptedException {
        browser.get(contextPath.toExternalForm());

        List<WebElement> cells = browser.findElements(By.cssSelector("#edt\\:0\\:n td"));

        Assert.assertTrue(cells.get(0).getAttribute("class").contains("d2"));
        Assert.assertTrue(cells.get(1).getAttribute("class").contains("d1"));
        Assert.assertTrue(cells.get(2).getAttribute("class").contains("d3"));

        Graphene.guardAjax(button).click();

        cells = browser.findElements(By.cssSelector("#edt\\:0\\:n td"));

        Assert.assertTrue(cells.get(0).getAttribute("class").contains("d3"));
        Assert.assertTrue(cells.get(1).getAttribute("class").contains("d2"));
        Assert.assertTrue(cells.get(2).getAttribute("class").contains("d1"));
    }

    private static void addIndexPage(RichDeployment deployment) {
        FaceletAsset p = new FaceletAsset();

        p.form("<rich:extendedDataTable id='edt' ");
        p.form("                        value='#{iterationBean.values}' ");
        p.form("                        var='bean' ");
        p.form("                        columnsOrder='${iterationBean.columnsOrder}' ");
        p.form("> ");
        p.form("    <rich:column id='column1' styleClass='d1' > ");
        p.form("        <f:facet name='header'>Column 1</f:facet> ");
        p.form("        <h:outputText value='Bean:' /> ");
        p.form("    </rich:column> ");
        p.form("    <rich:column id='column2' styleClass='d2'>");
        p.form("        <f:facet name='header'>Column 2</f:facet> ");
        p.form("        <h:outputText value='#{bean}' /> ");
        p.form("    </rich:column> ");
        p.form("    <rich:column id='column3' styleClass='d3'>" );
        p.form("        <f:facet name='header'>Column 3</f:facet> ");
        p.form("        <h:outputText value='Row #{bean}, Column 3' /> ");
        p.form("    </rich:column> ");
        p.form("</rich:extendedDataTable> ");
        p.form("Columns order: <h:outputText id='output' value='#{iterationBean.columnsOrderString}' /> " );
        p.form("<br /> " );
        p.form("<a4j:commandButton id= 'button' " );
        p.form("     render='edt,output' ");
        p.form("     action='#{iterationBean.setColumnsOrder(\"column3,column2,column1\")}' />");


        deployment.archive().addAsWebResource(p, "index.xhtml");
    }

}

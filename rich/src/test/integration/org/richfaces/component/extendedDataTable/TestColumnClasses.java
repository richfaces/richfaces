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
import org.richfaces.integration.IterationDeployment;
import org.richfaces.shrinkwrap.descriptor.FaceletAsset;

import java.net.URL;
import java.util.List;

@RunAsClient
@RunWith(Arquillian.class)
public class TestColumnClasses {

    @Drone
    private WebDriver browser;

    @ArquillianResource
    private URL contextPath;

    @Deployment
    public static WebArchive createDeployment() {
        IterationDeployment deployment = new IterationDeployment(TestColumnClasses.class);
        deployment.archive().addClass(IterationBuiltInBean.class);
        addIndexPage(deployment);

        return deployment.getFinalArchive();
    }

    @Test
    public void check_column_classes() throws InterruptedException {
        browser.get(contextPath.toExternalForm());

        WebElement body = browser.findElement(By.id("myForm:edt:body"));
        List<WebElement> cells = body.findElements(By.cssSelector("#myForm\\:edt\\:0\\:n td"));

        Assert.assertTrue(cells.get(0).getAttribute("class").contains("c1"));
        Assert.assertTrue(cells.get(0).getAttribute("class").contains("d1"));
        Assert.assertTrue(cells.get(1).getAttribute("class").contains("c2"));
        Assert.assertTrue(cells.get(1).getAttribute("class").contains("d2"));
        Assert.assertTrue(cells.get(2).getAttribute("class").contains("c3"));
        Assert.assertTrue(cells.get(2).getAttribute("class").contains("d3"));
    }

    private static void addIndexPage(IterationDeployment deployment) {
        FaceletAsset p = new FaceletAsset();
        p.xmlns("rich", "http://richfaces.org/rich");
        p.xmlns("a4j", "http://richfaces.org/a4j");

        p.body("<h:form id='myForm'> ");
        p.body("    <rich:extendedDataTable id='edt' value='#{iterationBuiltInBean.values}' var='bean' columnClasses='c1,c2,c3' > ");
        p.body("        <rich:column id='column1' styleClass='d1' > ");
        p.body("            <f:facet name='header'>Column 1</f:facet> ");
        p.body("            <h:outputText value='Bean:' /> ");
        p.body("        </rich:column> ");
        p.body("        <rich:column id='column2' styleClass='d2'>");
        p.body("            <f:facet name='header'>Column 2</f:facet> ");
        p.body("            <h:outputText value='#{bean}' /> ");
        p.body("        </rich:column> ");
        p.body("        <rich:column id='column3' styleClass='d3'>" );
        p.body("            <f:facet name='header'>Column 3</f:facet> ");
        p.body("            <h:outputText value='Row #{bean}, Column 3' /> ");
        p.body("        </rich:column> ");
        p.body("    </rich:extendedDataTable> ");
        p.body("</h:form> ");

        deployment.archive().addAsWebResource(p, "index.xhtml");
    }

}

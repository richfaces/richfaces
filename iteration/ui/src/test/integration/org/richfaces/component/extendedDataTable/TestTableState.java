package org.richfaces.component.extendedDataTable;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.arquillian.warp.ClientAction;
import org.jboss.arquillian.warp.ServerAssertion;
import org.jboss.arquillian.warp.Warp;
import org.jboss.arquillian.warp.WarpTest;
import org.jboss.arquillian.warp.jsf.AfterPhase;
import org.jboss.arquillian.warp.jsf.Phase;
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
import org.richfaces.component.AbstractExtendedDataTable;
import org.richfaces.component.ExtendedDataTableState;
import org.richfaces.integration.IterationDeployment;
import org.richfaces.shrinkwrap.descriptor.FaceletAsset;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import java.net.URL;

import static org.jboss.arquillian.graphene.Graphene.*;
import static org.junit.Assert.assertTrue;

@RunAsClient
@WarpTest
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

    @FindBy(id = "myForm:ajax")
    private WebElement button;

    @FindBy(id = "myForm:edt:header")
    private WebElement header;

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

    @Test
    public void check_that_table_state_is_updated_server_side() {
        // given
        browser.get(contextPath.toExternalForm());

        WebElement textHeader = header.findElement(By.cssSelector(".rf-edt-c-column1 .rf-edt-hdr-c-cnt"));
        WebElement dropZone = header.findElement(By.cssSelector(".rf-edt-hdr-c.rf-edt-c-column2"));

        Actions builder = new Actions(browser);

        Action dragAndDrop = builder.clickAndHold(textHeader)
                .moveToElement(dropZone)
                .release(dropZone)
                .build();

        guardXhr(dragAndDrop).perform();

        // when / then
        Warp.execute(new ClientAction() {

            @Override
            public void action() {
                guardXhr(button).click();
            }
        }).verify(new ServerAssertion() {
            private static final long serialVersionUID = 1L;

            @Inject
            IterationBean bean;

            @AfterPhase(Phase.INVOKE_APPLICATION)
            public void verify_bean_executed() {
                FacesContext facesContext = FacesContext.getCurrentInstance();
                AbstractExtendedDataTable edtComponent = (AbstractExtendedDataTable) facesContext.getViewRoot().findComponent("myForm").findComponent("edt");
                ExtendedDataTableState tableState = new ExtendedDataTableState(edtComponent);
                String[] expectedOrder = {"column1", "column2"};
                Assert.assertArrayEquals(expectedOrder, tableState.getColumnsOrder());
            }
        });
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
        p.body("            <f:facet name='header'>Column 1</f:facet>");
        p.body("            <h:outputText value='Bean:' />");
        p.body("        </rich:column>");
        p.body("        <rich:column id='column2' width='50px'>");
        p.body("            <f:facet name='header'>Column 2</f:facet>");
        p.body("            <h:outputText value='#{bean}' />");
        p.body("        </rich:column>");
        p.body("    </rich:extendedDataTable>");
        p.body("    <a4j:commandButton id='ajax' execute='edt' render='edt' value='Ajax' />");
        p.body("</h:form>");

        deployment.archive().addAsWebResource(p, "index.xhtml");
    }
}

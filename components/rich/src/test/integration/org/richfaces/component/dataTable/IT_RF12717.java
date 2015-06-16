package org.richfaces.component.dataTable;

import static org.jboss.arquillian.graphene.Graphene.guardHttp;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.net.URL;
import java.util.List;

import javax.annotation.Nullable;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.webapp30.WebAppDescriptor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.component.extendedDataTable.IterationBean;
import org.richfaces.integration.RichDeployment;
import org.richfaces.shrinkwrap.descriptor.FaceletAsset;

import com.google.common.base.Function;

@RunAsClient
@RunWith(Arquillian.class)
public class IT_RF12717 {

    @Drone
    private WebDriver browser;

    @ArquillianResource
    private URL contextPath;

    @FindBy(css = "table a")
    private List<WebElement> rowLinks;
    @FindBy(id = "myForm:output")
    private WebElement selectedValueElement;
    @FindBy(css = "input[type='submit']")
    private WebElement showTableButton;

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
//        UIDeployment deployment = new UIDeployment(IT_RF12717.class, "4.2.3.Final");
        RichDeployment deployment = new RichDeployment(IT_RF12717.class);
        deployment.archive().addClass(IterationBean.class);

        deployment.webXml(new Function<WebAppDescriptor, WebAppDescriptor>() {
            @Override
            public WebAppDescriptor apply(@Nullable WebAppDescriptor input) {
                input
                    .createContextParam()
                    .paramName("javax.faces.PARTIAL_STATE_SAVING")
                    .paramValue("false")
                    .up();
                return input;
            }
        });
        addIndexPage(deployment);

        return deployment.getFinalArchive();
    }

    @Test
    public void check_row_click() {
        browser.get(contextPath.toExternalForm());

        assertEquals("", selectedValueElement.getText());
        assertTrue(rowLinks.isEmpty());

        // show the table
        guardHttp(showTableButton).click();
        assertEquals("", selectedValueElement.getText());
        assertEquals(3, rowLinks.size());

        // check row links
        final String[] expectedNumbers = { "3", "6", "4" };
        int size = rowLinks.size();
        for (int i = 0; i < size; i++) {
            guardHttp(rowLinks.get(i)).click();
            assertEquals(expectedNumbers[i], selectedValueElement.getText());
        }
    }

    private static void addIndexPage(RichDeployment deployment) {
        FaceletAsset p = new FaceletAsset();
        p.body("<h:form id='myForm'>");
        p.body("<h:commandButton id='show' value='Show Table' type='submit' action='#{iterationBean.show}' />");
        p.body("<rich:dataTable id='tableId' value='#{iterationBean.data}' var='bean' rows='3' rendered='#{!empty iterationBean.data}'>");
        p.body("    <rich:column>");
        p.body("        <f:facet name='header'>");
        p.body("            <h:outputText value='Header' styleClass='tableHeader' />");
        p.body("        </f:facet>");
        p.body("        <h:commandLink styleClass='selectLink' action='#{iterationBean.setSelectedValue(bean)}' value='Select #{bean}' immediate='true' />");
        p.body("    </rich:column>");
        p.body("</rich:dataTable>");
        p.body("<br/>");
        p.body("selected value: <h:outputText id='output' value='#{iterationBean.selectedValue}' />");
        p.body("</h:form>");

        deployment.archive().addAsWebResource(p, "index.xhtml");
    }
}

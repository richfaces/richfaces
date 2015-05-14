package org.richfaces.component.dataTable;

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
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.component.extendedDataTable.IterationBean;
import org.richfaces.integration.RichDeployment;
import org.richfaces.shrinkwrap.descriptor.FaceletAsset;

@RunAsClient
@RunWith(Arquillian.class)
public class IT_RF12986 {

    @Drone
    private WebDriver browser;

    @ArquillianResource
    private URL contextPath;

    @FindBy(className = "rf-ds-btn-next")
    private WebElement nextButton;

    @FindBy(className = "rf-ds-btn-last")
    private WebElement lastButton;

    @FindBy(className = "rf-dt-r")
    private List<WebElement> rows;

    @FindBy
    private WebElement selection;

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        RichDeployment deployment = new RichDeployment(IT_RF12986.class);
        deployment.archive().addClass(IterationBean.class);
        addIndexPage(deployment);
        return deployment.getFinalArchive();
    }

    @Test
    public void check_row_selection() throws InterruptedException {
        browser.get(contextPath.toExternalForm());
        // click on the 2nd row
        Graphene.guardAjax(rows.get(1)).click();
        Assert.assertEquals("6", selection.getText());
        // move forward one page and click on the row with the same index
        Graphene.guardAjax(nextButton).click();
        Graphene.guardAjax(rows.get(1)).click();
        Assert.assertEquals("2", selection.getText());
        // got to the last page and click on the only row
        Graphene.guardAjax(lastButton).click();
        Graphene.guardAjax(rows.get(0)).click();
        Assert.assertEquals("0", selection.getText());
    }

    private static void addIndexPage(RichDeployment deployment) {
        FaceletAsset p = new FaceletAsset();
        p.form("<rich:dataTable id='tableId' value='#{iterationBean.values}' var='bean' rows='3'> ");
        p.form(
            "    <a4j:ajax execute='@this' event='rowclick' listener='#{iterationBean.setSelectedValue(bean)}' render=':res'/>");
        p.form("    <rich:collapsibleSubTable id='collapsibleTableId' rows='3'/> ");
        p.form("    <rich:column> ");
        p.form("        <f:facet name='header'> ");
        p.form("            <h:outputText value='Header' styleClass='tableHeader'/> ");
        p.form("        </f:facet> ");
        p.form("        <h:outputText value='#{bean}' /> ");
        p.form("    </rich:column> ");
        p.form("    <f:facet name='footer'> ");
        p.form("        <rich:dataScroller id='datascrollerId' for='tableId' fastControls='hide'/> ");
        p.form("    </f:facet> ");
        p.form("</rich:dataTable> ");
        p.body("<a4j:outputPanel id='res'>");
        p.body("    <rich:panel header='Selected:'>");
        p.body("        <h:outputText id='selection' value='#{iterationBean.selectedValue}'/>");
        p.body("    </rich:panel>");
        p.body("</a4j:outputPanel>");

        deployment.archive().addAsWebResource(p, "index.xhtml");
    }

}

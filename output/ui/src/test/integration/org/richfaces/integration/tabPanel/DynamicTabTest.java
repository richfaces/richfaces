package org.richfaces.integration.tabPanel;

import static org.jboss.arquillian.graphene.Graphene.guardXhr;

import java.net.URL;
import java.util.List;

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
import org.richfaces.integration.OutputDeployment;
import org.richfaces.integration.tabPanel.model.TabBean;
import org.richfaces.integration.tabPanel.model.TabPanelBean;
import org.richfaces.shrinkwrap.descriptor.FaceletAsset;

@RunAsClient
@RunWith(Arquillian.class)
public class DynamicTabTest {

    @Drone
    private WebDriver browser;

    @ArquillianResource
    private URL contextPath;

    @FindBy(id = "myForm:tabPanel")
    private WebElement tabPanel;

    @FindBy(id = "myForm:repeat:0:tab:header:inactive")
    private WebElement tab0;

    @FindBy(id = "myForm:repeat:1:tab:header:inactive")
    private WebElement tab1;

    @FindBy(id = "myForm:repeat:2:tab:header:inactive")
    private WebElement tab2;

    @Deployment
    public static WebArchive createDeployment() {
        OutputDeployment deployment = new OutputDeployment(DynamicTabTest.class);
        deployment.archive().addClass(TabBean.class);
        deployment.archive().addClass(TabPanelBean.class);

        addIndexPage(deployment);

        WebArchive archive = deployment.getFinalArchive();
        return archive;
    }

    /**
     * RF-12765
     */
    @Test
    public void check_tab_switch() {
        browser.get(contextPath.toExternalForm() + "index.jsf");

        List<WebElement> tabLabels = tabPanel.findElements(By.className("rf-tab-lbl"));
        Assert.assertEquals(9, tabLabels.size());

        guardXhr(tab2).click();
        Assert.assertTrue(tabPanel.getText().contains("tab6"));

        guardXhr(tab0).click();
        Assert.assertTrue(tabPanel.getText().contains("tab4"));

        guardXhr(tab2).click();
        Assert.assertTrue(tabPanel.getText().contains("tab6"));

        guardXhr(tab0).click();
        Assert.assertTrue(tabPanel.getText().contains("tab4"));

    }

    private static void addIndexPage(OutputDeployment deployment) {
        FaceletAsset p = new FaceletAsset();
        p.xmlns("rich", "http://richfaces.org/output");
        p.xmlns("a4j", "http://richfaces.org/a4j");
        p.xmlns("c", "http://java.sun.com/jsp/jstl/core");
        p.body("<h:form id='myForm'>");
        p.body("<rich:tabPanel id='tabPanel'>");
        p.body("    <a4j:repeat id='repeat' value='#{tabPanelBean.tabBeans}' var='newTab'>");
        p.body("        <rich:tab id='tab' name='#{newTab.tabName}'>");
        p.body("            #{newTab.tabContentText}");
        p.body("            <f:facet name='header'>");
        p.body("                <h:outputText value='#{newTab.tabHeader} ' />");
        p.body("                <h:commandLink value='[x]' rendered='#{newTab.closable}' onclick='removeTab(\"#{newTab.tabId}\");' />");
        p.body("            </f:facet>");
        p.body("            content of tab #{newTab.tabName} ");
        p.body("            <h:form id='myForm2'></h:form>");
        p.body("        </rich:tab>");
        p.body("    </a4j:repeat>");

        p.body("</rich:tabPanel> ");

        p.body("<a4j:jsFunction name='removeTab' action='#{tabPanelBean.removeTab}' render='tabPanel' >");
        p.body("    <a4j:param name='removeTabId'/>");
        p.body("</a4j:jsFunction>");

        p.body("<a4j:commandButton id='a4jCreateTabButton' value='[a4j] Create tab' render='tabPanel' actionListener='#{tabPanelBean.generateNewTab}' />");
        p.body("</h:form>");

        deployment.archive().addAsWebResource(p, "index.xhtml");
    }

}

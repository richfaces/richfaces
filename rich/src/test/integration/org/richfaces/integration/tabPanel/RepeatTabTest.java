package org.richfaces.integration.tabPanel;

import java.net.URL;
import java.util.List;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.integration.OutputDeployment;
import org.richfaces.integration.tabPanel.model.TabBean;
import org.richfaces.integration.tabPanel.model.TabPanelBean;
import org.richfaces.shrinkwrap.descriptor.FaceletAsset;

@RunAsClient
@RunWith(Arquillian.class)
public class RepeatTabTest {

    @Drone
    private WebDriver browser;

    @ArquillianResource
    private URL contextPath;

    @FindBy(id = "myForm:tabPanel")
    private WebElement tabPanel;

    @FindBy(className = "rf-tab-hdr-inact")
    private List<WebElement> tabs;

    @FindBy(id = "myForm:a4jCreateTabButton")
    private WebElement a4jCreateTabButton;

    private DynamicTabTestHelper tabTestHelper = new DynamicTabTestHelper();

    @Deployment
    public static WebArchive createDeployment() {
        OutputDeployment deployment = new OutputDeployment(RepeatTabTest.class);
        deployment.archive().addClass(TabBean.class);
        deployment.archive().addClass(TabPanelBean.class);

        addIndexPage(deployment);

        return deployment.getFinalArchive();
    }

    @Test
    public void check_tab_switch() {
        browser.get(contextPath.toExternalForm() + "index.jsf");
        tabTestHelper.check_tab_switch(tabPanel, tabs, a4jCreateTabButton);
    }

    @Test
    public void check_row_removal() throws InterruptedException {
        browser.get(contextPath.toExternalForm());
        tabTestHelper.check_row_removal(tabPanel, tabs, a4jCreateTabButton);
    }

    private static void addIndexPage(OutputDeployment deployment) {
        FaceletAsset p = new FaceletAsset();
        p.xmlns("rich", "http://richfaces.org/rich");
        p.xmlns("a4j", "http://richfaces.org/a4j");
        p.xmlns("c", "http://java.sun.com/jsp/jstl/core");
        p.body("<h:form id='myForm'>");
        p.body("<rich:tabPanel id='tabPanel'>");
        p.body("    <rich:tab id='tab0' name='tab0' header='tab0 header'>content of tab 0</rich:tab>");
        p.body("    <rich:tab id='tab1' name='tab1' header='tab1 header' disabled='true'>content of tab 1</rich:tab>");
        p.body("    <rich:tab id='tab2' name='tab2' header='tab2 header'>content of tab 2</rich:tab>");
        p.body("    <a4j:repeat id='repeat' value='#{tabPanelBean.tabBeans}' var='newTab'>");
        p.body("        <rich:tab id='tab' name='#{newTab.tabName}'>");
        p.body("            <f:facet name='header'>");
        p.body("                <h:outputText value='#{newTab.tabHeader} ' />");
        p.body("                <h:commandLink value='[x]' rendered='#{newTab.closable}' onclick='var event = arguments[0] || window.event; removeTab(\"#{newTab.tabId}\"); event.stopPropagation(); return false;' />");
        p.body("            </f:facet>");
        p.body("            #{newTab.tabContentText}");
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

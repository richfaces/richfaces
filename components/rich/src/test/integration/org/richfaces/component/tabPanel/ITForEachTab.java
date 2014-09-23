package org.richfaces.component.tabPanel;

import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.component.tabPanel.model.TabBean;
import org.richfaces.component.tabPanel.model.TabPanelBean;
import org.richfaces.fragment.tabPanel.RichFacesTabPanel;
import org.richfaces.integration.RichDeployment;
import org.richfaces.shrinkwrap.descriptor.FaceletAsset;

@RunAsClient
@RunWith(Arquillian.class)
public class ITForEachTab {
    
    @FindByJQuery("[id$='tabPanel']")
    private RichFacesTabPanel tabPanel;

    @Drone
    private WebDriver browser;

    @ArquillianResource
    private URL contextPath;

    @FindBy(id = "myForm:a4jCreateTabButton")
    private WebElement a4jCreateTabButton;

    private DynamicTabTestHelper tabTestHelper = new DynamicTabTestHelper();

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        RichDeployment deployment = new RichDeployment(ITForEachTab.class);
        deployment.archive().addClass(TabBean.class);
        deployment.archive().addClass(TabPanelBean.class);

        addIndexPage(deployment);

        return deployment.getFinalArchive();
    }

    @Test
    public void check_tab_switch() {
        browser.get(contextPath.toExternalForm() + "index.jsf");
        tabTestHelper.check_tab_switch(tabPanel, a4jCreateTabButton);
    }

    @Test
    public void check_row_removal() throws InterruptedException {
        browser.get(contextPath.toExternalForm());
        tabTestHelper.check_row_removal(tabPanel, a4jCreateTabButton);
    }

    private static void addIndexPage(RichDeployment deployment) {
        FaceletAsset p = new FaceletAsset();
        p.body("<h:form id='myForm'>");
        p.body("<rich:tabPanel id='tabPanel'>");
        p.body("    <rich:tab id='tab0' name='tab0' header='tab0 header'>content of tab 0</rich:tab>");
        p.body("    <rich:tab id='tab1' name='tab1' header='tab1 header' disabled='true'>content of tab 1</rich:tab>");
        p.body("    <rich:tab id='tab2' name='tab2' header='tab2 header'>content of tab 2</rich:tab>");
        p.body("    <c:forEach items='#{tabPanelBean.tabBeans}' var='newTab'>");
        p.body("        <rich:tab id='#{newTab.tabId}' name='#{newTab.tabName}' render='tabPanel'>");
        p.body("            <f:facet name='header'>");
        p.body("                <h:outputText value='#{newTab.tabHeader} ' />");
        p.body("                <h:commandLink value='[x]' rendered='#{newTab.closable}' onclick='var event = arguments[0] || window.event; removeTab(\"#{newTab.tabId}\"); event.stopPropagation(); return false;' />");
        p.body("            </f:facet>");
        p.body("            #{newTab.tabContentText}");
        p.body("        </rich:tab>");
        p.body("    </c:forEach>");

        p.body("</rich:tabPanel> ");

        p.body("<a4j:jsFunction id='jsFunction' name='removeTab' action='#{tabPanelBean.removeTab}' render='myForm:tabPanel' >");
        p.body("    <a4j:param name='removeTabId'/>");
        p.body("</a4j:jsFunction>");

        p.body("<a4j:commandButton id='a4jCreateTabButton' value='[a4j] Create tab' render='tabPanel' actionListener='#{tabPanelBean.generateNewTab}' />");
        p.body("</h:form>");

        deployment.archive().addAsWebResource(p, "index.xhtml");
    }

}

package org.richfaces.integration.tabPanel;

import static org.jboss.arquillian.graphene.Graphene.guardXhr;

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
import org.junit.Assert;
import org.junit.Ignore;
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

import com.google.common.base.Function;

@RunAsClient
@RunWith(Arquillian.class)
public class ForEachTabTest {

    @Drone
    private WebDriver browser;

    @ArquillianResource
    private URL contextPath;

    @FindBy(id = "myForm:tabPanel")
    private WebElement tabPanel;

    @FindBy(id = "myForm:tab0:header:inactive")
    private WebElement tab0;

    @FindBy(id = "myForm:tab1:header:inactive")
    private WebElement tab1;

    @FindBy(id = "myForm:tab2:header:inactive")
    private WebElement tab2;

    @FindBy(id = "myForm:tab3:header:inactive")
    private WebElement tab3;

    @FindBy(id = "myForm:tab4:header:inactive")
    private WebElement tab4;

    @FindBy(id = "myForm:tab5:header:inactive")
    private WebElement tab5;

    @FindBy(id = "myForm:a4jCreateTabButton")
    private WebElement a4jCreateTabButton;

    @Deployment
    public static WebArchive createDeployment() {
        OutputDeployment deployment = new OutputDeployment(ForEachTabTest.class);
        deployment.archive().addClass(TabBean.class);
        deployment.archive().addClass(TabPanelBean.class);

        addIndexPage(deployment);

        return deployment.getFinalArchive();
    }

    @Test
    @Ignore
    public void check_tab_switch() {
        browser.get(contextPath.toExternalForm() + "index.jsf");

        List<WebElement> tabLabels = tabPanel.findElements(By.className("rf-tab-lbl"));
        Assert.assertEquals(18, tabLabels.size());

        guardXhr(tab2).click();
        Assert.assertTrue(tabPanel.getText().contains("tab2"));

        guardXhr(tab4).click();
        Assert.assertTrue(tabPanel.getText().contains("tab4"));

        guardXhr(tab5).click();
        Assert.assertTrue(tabPanel.getText().contains("tab5"));

        guardXhr(tab0).click();
        Assert.assertTrue(tabPanel.getText().contains("tab0"));

        guardXhr(a4jCreateTabButton).click();

        WebElement tab6 = browser.findElement(By.id("myForm:tab6:header:inactive"));
        tabLabels = tabPanel.findElements(By.className("rf-tab-lbl"));
        Assert.assertEquals(21, tabLabels.size());

        guardXhr(tab6).click();
        Assert.assertTrue(tabPanel.getText().contains("tab6"));

        guardXhr(tab0).click();
        Assert.assertTrue(tabPanel.getText().contains("tab0"));

//        WebElement removeLink = tab6.findElement(By.tagName("a"));
//        guardXhr(removeLink).click();
//
//        tabLabels = tabPanel.findElements(By.className("rf-tab-lbl"));
//        Assert.assertEquals(18, tabLabels.size());
    }


    /**
     * RF-12768
     */
    @Test
    @Ignore
    public void check_row_removal() throws InterruptedException {
        browser.get(contextPath.toExternalForm());

        List<WebElement> tabLabels = tabPanel.findElements(By.className("rf-tab-lbl"));
        Assert.assertEquals(18, tabLabels.size());

        guardXhr(a4jCreateTabButton).click();
        guardXhr(a4jCreateTabButton).click();
        guardXhr(a4jCreateTabButton).click();

        tabLabels = tabPanel.findElements(By.className("rf-tab-lbl"));
        Assert.assertEquals(27, tabLabels.size()); // 9 tabs, 3 rf-tab-lbl elements per tab

        WebElement tab8 = browser.findElement(By.id("myForm:tab8:header:inactive"));
        WebElement removeLink = tab8.findElement(By.tagName("a"));
        guardXhr(removeLink).click();

        tabLabels = tabPanel.findElements(By.className("rf-tab-lbl"));
        Assert.assertEquals(24, tabLabels.size()); // 8 tabs, 3 rf-tab-lbl elements per tab

        Thread.sleep(300);

        WebElement tab7 = browser.findElement(By.id("myForm:tab7:header:inactive"));
        removeLink = tab7.findElement(By.tagName("a"));
        guardXhr(removeLink).click();

        tabLabels = tabPanel.findElements(By.className("rf-tab-lbl"));
        Assert.assertEquals(21, tabLabels.size()); // 8 tabs, 3 rf-tab-lbl elements per tab

        WebElement tab6 = browser.findElement(By.id("myForm:tab6:header:inactive"));
        removeLink = tab6.findElement(By.tagName("a"));
        guardXhr(removeLink).click();

        tabLabels = tabPanel.findElements(By.className("rf-tab-lbl"));
        Assert.assertEquals(18, tabLabels.size()); // 8 tabs, 3 rf-tab-lbl elements per tab
    }

    private static void addIndexPage(OutputDeployment deployment) {
        FaceletAsset p = new FaceletAsset();
        p.xmlns("rich", "http://richfaces.org/output");
        p.xmlns("a4j", "http://richfaces.org/a4j");
        p.xmlns("c", "http://java.sun.com/jsp/jstl/core");
        p.body("<h:form id='myForm'>");
        p.body("<rich:tabPanel id='tabPanel'>");
        p.body("    <rich:tab id='tab0' name='tab0' header='tab0 header'>content of tab 0</rich:tab>");
        p.body("    <rich:tab id='tab1' name='tab1' header='tab1 header' disabled='true'>content of tab 1</rich:tab>");
        p.body("    <rich:tab id='tab2' name='tab2' header='tab2 header'>content of tab 2</rich:tab>");

        p.body("    <c:forEach items='#{tabPanelBean.tabBeans}' var='newTab'>");
        p.body("        <rich:tab id='#{newTab.tabId}' name='#{newTab.tabName}'>");
        p.body("            #{newTab.tabContentText}");
        p.body("            <f:facet name='header'>");
        p.body("                <h:outputText value='#{newTab.tabHeader} ' />");
        p.body("                <h:commandLink value='[x]' rendered='#{newTab.closable}' onclick='removeTab(\"#{newTab.tabId}\"); return false;' />");
        p.body("            </f:facet>");
        p.body("            content of tab #{newTab.tabName} ");
        p.body("        </rich:tab>");
        p.body("    </c:forEach>");

        p.body("</rich:tabPanel> ");

        p.body("<a4j:jsFunction name='removeTab' action='#{tabPanelBean.removeTab}' render='tabPanel' >");
        p.body("    <a4j:param name='removeTabId'/>");
        p.body("</a4j:jsFunction>");

        p.body("<a4j:commandButton id='a4jCreateTabButton' value='[a4j] Create tab' render='tabPanel' actionListener='#{tabPanelBean.generateNewTab}' />");
        p.body("</h:form>");

        deployment.archive().addAsWebResource(p, "index.xhtml");
    }

}

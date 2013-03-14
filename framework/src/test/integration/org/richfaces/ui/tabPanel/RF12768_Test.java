package org.richfaces.component.tabPanel;

import static org.jboss.arquillian.graphene.Graphene.guardXhr;

import java.net.URL;
import java.util.List;

import javax.annotation.Nullable;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.webapp30.WebAppDescriptor;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.integration.OutputDeployment;
import org.richfaces.shrinkwrap.descriptor.FaceletAsset;

import com.google.common.base.Function;

@RunAsClient
@RunWith(Arquillian.class)
public class RF12768_Test {

    @Drone
    private WebDriver browser;

    @ArquillianResource
    private URL contextPath;

    @FindBy(css = "form")
    private WebElement form;

    @FindBy(css = "table")
    private WebElement table;

    @Deployment
    public static WebArchive createDeployment() {
        OutputDeployment deployment = new OutputDeployment(RF12768_Test.class);
        deployment.archive().addClass(TabBean.class);
        deployment.archive().addClass(TabPanelBean.class);
        deployment.archive().addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");

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
    public void check_row_removal() throws InterruptedException {
        browser.get(contextPath.toExternalForm());
        WebElement createButton = form.findElement(By.id("myForm:a4jCreateTabButton"));
        guardXhr(createButton).click();
        guardXhr(createButton).click();
        guardXhr(createButton).click();

        WebElement tabPanel = form.findElement(By.id("myForm:tabPanel"));
        List<WebElement> tabLabels = tabPanel.findElements(By.className("rf-tab-lbl"));
        Assert.assertEquals(27, tabLabels.size()); // 9 tabs, 3 rf-tab-lbl elements per tab

        WebElement tab9 = form.findElement(By.id("myForm:tab9:header:inactive"));
        WebElement removeLink = tab9.findElement(By.tagName("a"));
        guardXhr(removeLink).click();

        tabPanel = form.findElement(By.id("myForm:tabPanel"));
        tabLabels = tabPanel.findElements(By.className("rf-tab-lbl"));
        Assert.assertEquals(24, tabLabels.size()); // 8 tabs, 3 rf-tab-lbl elements per tab
    }

    private static void addIndexPage(OutputDeployment deployment) {
        FaceletAsset p = new FaceletAsset();

        p.body("<h:form id='myForm'>");
        p.body("<r:tabPanel id='tabPanel'>");
        p.body("    <r:tab id='tab1' name='tab1' header='tab1 header'>content of tab 1</r:tab>");
        p.body("    <r:tab id='tab2' name='tab2' header='tab2 header' disabled='true'>content of tab 2</r:tab>");
        p.body("    <r:tab id='tab3' name='tab3' header='tab3 header'>content of tab 3</r:tab>");

        p.body("    <c:forEach items='#{tabPanelBean.tabBeans}' var='newTab'>");
        p.body("        <r:tab id='#{newTab.tabId}' name='#{newTab.tabName}'>");
        p.body("            #{newTab.tabContentText}");
        p.body("            <f:facet name='header'>");
        p.body("                <h:outputText value='#{newTab.tabHeader} ' />");
        p.body("                <h:commandLink value='[x]' rendered='#{newTab.closable}' onclick='removeTab(\"#{newTab.tabId}\"); return false;' />");
        p.body("            </f:facet>");
        p.body("            content of tab #{newTab.tabName} ");
        p.body("        </r:tab>");
        p.body("    </c:forEach>");

        p.body("</r:tabPanel> ");

        p.body("<r:jsFunction name='removeTab' action='#{tabPanelBean.removeTab}' render='tabPanel' >");
        p.body("    <r:param name='removeTabId'/>");
        p.body("</r:jsFunction>");

        p.body("<r:commandButton id='a4jCreateTabButton' value='[a4j] Create tab' render='tabPanel' actionListener='#{tabPanelBean.generateNewTab}' />");
        p.body("</h:form>");

        deployment.archive().addAsWebResource(p, "index.xhtml");
    }

}

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
public class RF12765_Test {

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
        OutputDeployment deployment = new OutputDeployment(RF12765_Test.class);
        deployment.archive().addClass(TabBean.class);
        deployment.archive().addClass(TabPanelBean.class);

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
    public void check_row_removal() {
        browser.get(contextPath.toExternalForm() + "/index.jsf");

        WebElement tabPanel = form.findElement(By.id("myForm:tabPanel"));
        List<WebElement> tabLabels = tabPanel.findElements(By.className("rf-tab-lbl"));
        Assert.assertEquals(9, tabLabels.size());

        WebElement tab0 = form.findElement(By.id("myForm:repeat:0:tab:header:inactive"));
        WebElement tab1 = form.findElement(By.id("myForm:repeat:1:tab:header:inactive"));
        WebElement tab2 = form.findElement(By.id("myForm:repeat:2:tab:header:inactive"));

        guardXhr(tab2).click();
        tabPanel = form.findElements(By.className("rf-tab-cnt")).get(1);
        Assert.assertTrue(tabPanel.getText().contains("tab6"));
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

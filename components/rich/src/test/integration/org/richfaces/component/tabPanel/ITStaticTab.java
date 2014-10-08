package org.richfaces.component.tabPanel;

import static org.jboss.arquillian.graphene.Graphene.guardAjax;

import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.component.tabPanel.model.SimpleBean;
import org.richfaces.fragment.tabPanel.RichFacesTabPanel;
import org.richfaces.integration.RichDeployment;
import org.richfaces.shrinkwrap.descriptor.FaceletAsset;

import category.Smoke;

@RunAsClient
@RunWith(Arquillian.class)
public class ITStaticTab {
    
    @FindByJQuery("[id$='tabPanel']")
    private RichFacesTabPanel tabPanel;

    @Drone
    private WebDriver browser;

    @ArquillianResource
    private URL contextPath;

    @FindBy(tagName = "body")
    private WebElement body;    

    @FindBy(id = "out")
    private WebElement out;

    @FindBy(id = "myForm:button")
    private WebElement button;

    @FindBy(id = "myForm:inputText")
    private WebElement inputText;

    @FindBy(id = "myForm:outputText")
    private WebElement outputText;

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        RichDeployment deployment = new RichDeployment(ITStaticTab.class);
        deployment.archive().addClass(SimpleBean.class);

        addIndexPage(deployment);
        addHeaderPage(deployment);
        addHeaderButtonPage(deployment);

        WebArchive archive = deployment.getFinalArchive();
        return archive;
    }

    /**
     * {@link https://issues.jboss.org/browse/RF-12839}
     */
    @Test
    @Category(Smoke.class)
    public void check_tab_switch() {
        browser.get(contextPath.toExternalForm() + "index.jsf");

        guardAjax(tabPanel.advanced().getTabHeaders().get(1)).click();
        Assert.assertTrue(out.getText().contains("begin"));
//        Assert.assertTrue(out.getText().contains("tabpanel_complete"));
//        Assert.assertTrue(out.getText().contains("beforedomupdate"));

        // Assert the oncomplete on the tab does work
        Assert.assertTrue(out.getText().contains("tab1_complete"));

    }

    /**
     * {@link https://issues.jboss.org/browse/RF-12969}
     */
    @Test
    public void check_click_active_tab() {
        browser.get(contextPath.toExternalForm() + "index.jsf");
        WebElement activeTab = tabPanel.advanced().getActiveHeaderElement();
        guardAjax(activeTab).click();
        Assert.assertEquals(null, body.getAttribute("JSError"));
    }

    @Test
    public void check_tab_execute() {
        browser.get(contextPath.toExternalForm() + "index.jsf");

        inputText.sendKeys("abcd");
        guardAjax(tabPanel.advanced().getTabHeaders().get(1)).click();
        Assert.assertEquals("abcd", outputText.getText());
    }

    /**
     * {@link https://issues.jboss.org/browse/RF-13278}
     * {@link https://issues.jboss.org/browse/RF-13687}
     */
    @Test
    public void check_header_render() {
        browser.get(contextPath.toExternalForm() + "header.jsf");
        Assert.assertEquals("0 clicks", tabPanel.advanced().getTabHeaders().get(1).findElement(By.className("rf-tab-lbl")).getText());

        guardAjax(tabPanel.advanced().getTabHeaders().get(1)).click();
        guardAjax(tabPanel.advanced().getTabHeaders().get(0)).click();
        Assert.assertEquals("1 clicks", tabPanel.advanced().getTabHeaders().get(1).findElement(By.className("rf-tab-lbl")).getText());

        guardAjax(tabPanel.advanced().getTabHeaders().get(1)).click();
        Assert.assertEquals("1 clicks", tabPanel.advanced().getTabHeaders().get(1).findElement(By.className("rf-tab-lbl")).getText());

        guardAjax(tabPanel.advanced().getTabHeaders().get(0)).click();
        Assert.assertEquals("2 clicks", tabPanel.advanced().getTabHeaders().get(1).findElement(By.className("rf-tab-lbl")).getText());
    }

    /**
     * {@link https://issues.jboss.org/browse/RF-13278}
     */
    @Test
    public void check_header_button_render() {
        browser.get(contextPath.toExternalForm() + "headerButton.jsf");
        Assert.assertEquals("0 clicks", tabPanel.advanced().getTabHeaders().get(1).findElement(By.className("rf-tab-lbl")).getText());
        
        guardAjax(tabPanel.advanced().getTabHeaders().get(0).findElement(By.className("button"))).click();
        Assert.assertEquals("1 clicks", tabPanel.advanced().getTabHeaders().get(1).findElement(By.className("rf-tab-lbl")).getText());

        guardAjax(tabPanel.advanced().getTabHeaders().get(1)).click();
        guardAjax(tabPanel.advanced().getTabHeaders().get(0).findElement(By.className("myText"))).click();
        Assert.assertEquals("1 clicks", tabPanel.advanced().getTabHeaders().get(1).findElement(By.className("rf-tab-lbl")).getText());
        
        guardAjax(tabPanel.advanced().getTabHeaders().get(0).findElement(By.className("button"))).click();
        Assert.assertEquals("2 clicks", tabPanel.advanced().getTabHeaders().get(1).findElement(By.className("rf-tab-lbl")).getText());
    }


    private static void addIndexPage(RichDeployment deployment) {
        FaceletAsset p = new FaceletAsset();
        p.head("<script type='text/javascript'>");
        p.head("    window.onerror=function(msg) { ");
        p.head("        $('body').attr('JSError',msg);");
        p.head("    }");
        p.head("</script>");
        p.body("<h:form id='myForm'>");
        p.body("<rich:tabPanel id='tabPanel' ");
        p.body("               onbegin='$(\"#out\").append(\"begin \\n\")'");
        p.body("               oncomplete='$(\"#out\").append(\"tabpanel_complete \\n\")'");
        p.body("               onbeforedomupdate='$(\"#out\").append(\"beforedomupdate \\n\")'>");
        p.body("    <rich:tab id='tab0' name='tab0' header='tab0 header' ");
        p.body("               oncomplete='$(\"#out\").append(\"tab0_complete \\n\")'>");
        p.body("        content of tab 0");
        p.body("    </rich:tab>");
        p.body("    <rich:tab id='tab1' name='tab1' header='tab1 header' ");
        p.body("               execute='inputText'");
        p.body("               oncomplete='$(\"#out\").append(\"tab1_complete \\n\")'>");
        p.body("        content of tab 1");
        p.body("        <h:outputText id = 'outputText' value='#{simpleBean.string}' />");
        p.body("    </rich:tab>");
        p.body("</rich:tabPanel> ");
        p.body("<h:inputText id = 'inputText' value='#{simpleBean.string}' />");
        p.body("<div id='out'></div>");
        p.body("</h:form>");

        deployment.archive().addAsWebResource(p, "index.xhtml");
    }

    private static void addHeaderPage(RichDeployment deployment) {
        FaceletAsset p = new FaceletAsset();
        p.body("<h:form id='myForm'>");
        p.body("<rich:tabPanel id='tabPanel'>");
        p.body("    <rich:tab id='tab0' name='tab0' "); // header='tab0 header' ");
        p.body("            action='#{simpleBean.incrementCount()}' ");
        p.body("            render='tabPanel@header'> ");
        p.body("        <f:facet name='header'> ");
        p.body("        Click Me ");
        p.body("        </f:facet> ");
        p.body("        content of tab 0");
        p.body("    </rich:tab>");
        p.body("    <rich:tab id='tab1'>");
        p.body("        <f:facet name='header'> ");
        p.body("            <h:outputText id='label' value='#{simpleBean.count} clicks' /> ");
        p.body("        </f:facet> ");
        p.body("        content of tab 1");
        p.body("        <h:outputText id='outputText' value='#{simpleBean.string}' />");
        p.body("    </rich:tab>");
        p.body("</rich:tabPanel> ");
        p.body("</h:form>");

        deployment.archive().addAsWebResource(p, "header.xhtml");
    }

    private static void addHeaderButtonPage(RichDeployment deployment) {
        FaceletAsset p = new FaceletAsset();
        p.body("<h:form id='myForm'>");
        p.body("<rich:tabPanel id='tabPanel' >");
        p.body("    <rich:tab id='tab0' name='tab0'> "); // header='tab0 header' ");
        p.body("        <f:facet name='header'>");
        p.body("            <span class='myText'>Tab 0</span> ");
        p.body("            <a4j:commandLink value='click me' ");
        p.body("                styleClass='button' ");
        p.body("                action='#{simpleBean.incrementCount()}' ");
        p.body("                render='label' ");
        p.body("                oncomplete='return false;' ");
        p.body("                execute='@this' /> ");
        p.body("        </f:facet> ");
        p.body("        content of tab 0");
        p.body("    </rich:tab>");
        p.body("    <rich:tab id='tab1'>");
        p.body("        <f:facet name='header'> ");
        p.body("            <h:outputText id='label' value='#{simpleBean.count} clicks' /> ");
        p.body("        </f:facet> ");
        p.body("        content of tab 1");
        p.body("        <h:outputText id = 'outputText' value='#{simpleBean.string}' />");
        p.body("    </rich:tab>");
        p.body("</rich:tabPanel> ");
        p.body("</h:form>");

        deployment.archive().addAsWebResource(p, "headerButton.xhtml");
    }

}

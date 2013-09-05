package org.richfaces.ui.tabPanel;

import static org.jboss.arquillian.graphene.Graphene.waitModel;

import java.net.URL;
import java.util.List;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.arquillian.warp.Activity;
import org.jboss.arquillian.warp.Warp;
import org.jboss.arquillian.warp.WarpTest;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.richfaces.deployment.FrameworkDeployment;
import org.richfaces.shrinkwrap.descriptor.FaceletAsset;
import org.richfaces.ui.tabPanel.model.TabPanelItemChangeEventBean;

@RunAsClient
@RunWith(Arquillian.class)
@WarpTest
public class ITTabPanelItemChangeEventTest {

    @Drone
    private WebDriver browser;

    @ArquillianResource
    private URL contextPath;

    @FindByJQuery(".rf-tab-hdr:visible")
    private List<WebElement> tabs;

    @Deployment
    public static WebArchive createDeployment() {
        FrameworkDeployment deployment = new FrameworkDeployment(ITTabPanelItemChangeEventTest.class);

        deployment.archive().addClasses(TabPanelItemChangeEventBean.class, ItemChangeEventInspection.class);

        addIndexPage(deployment);

        WebArchive archive = deployment.getFinalArchive();
        return archive;
    }

    @Test
    public void test() {
        browser.get(contextPath.toExternalForm());

        Warp.initiate(new Activity() {
            public void perform() {
                tabs.get(1).click();
                waitModel().until().element(tabs.get(1)).attribute("class").contains("rf-tab-hdr-act");
            }
        }).inspect(new ItemChangeEventInspection());

        Warp.initiate(new Activity() {
            public void perform() {
                tabs.get(0).click();
                waitModel().until().element(tabs.get(0)).attribute("class").contains("rf-tab-hdr-act");
            }
        }).inspect(new ItemChangeEventInspection());
    }

    private static void addIndexPage(FrameworkDeployment deployment) {
        FaceletAsset p = new FaceletAsset();
        p.body("<h:form id='myForm'>");
        p.body("<r:tabPanel id='tabPanel' binding='#{tabPanelItemChangeEventBean.tabPanel}' itemChangeListener='#{tabPanelItemChangeEventBean.itemChangeListener}'>");
        p.body("    <r:tab id='tab0' name='tab0' header='tab0 header'>");
        p.body("        content of tab 1");
        p.body("    </r:tab>");
        p.body("    <r:tab id='tab1' name='tab1' header='tab1 header'>");
        p.body("        content of tab 2");
        p.body("    </r:tab>");
        p.body("</r:tabPanel> ");
        p.body("</h:form>");

        deployment.archive().addAsWebResource(p, "index.xhtml");
    }
}

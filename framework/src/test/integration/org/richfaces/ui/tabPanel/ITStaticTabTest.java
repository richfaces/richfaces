/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.richfaces.ui.tabPanel;

import static org.jboss.arquillian.graphene.Graphene.guardAjax;

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
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.deployment.FrameworkDeployment;
import org.richfaces.shrinkwrap.descriptor.FaceletAsset;
import org.richfaces.ui.tabPanel.model.SimpleBean;

import category.Smoke;

@RunAsClient
@RunWith(Arquillian.class)
public class ITStaticTabTest {

    @Drone
    private WebDriver browser;

    @ArquillianResource
    private URL contextPath;

    @FindBy(tagName = "body")
    private WebElement body;

    @FindBy(id = "myForm:tabPanel")
    private WebElement tabPanel;

    @FindBy(className = "rf-tab-hdr-inact")
    private List<WebElement> tabs;

    @FindBy(id = "out")
    private WebElement out;

    @FindBy(id = "myForm:inputText")
    private WebElement inputText;

    @FindBy(id = "myForm:outputText")
    private WebElement outputText;

    private DynamicTabTestHelper tabTestHelper = new DynamicTabTestHelper();

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        FrameworkDeployment deployment = new FrameworkDeployment(ITStaticTabTest.class);
        deployment.archive().addClass(SimpleBean.class);

        addIndexPage(deployment);

        WebArchive archive = deployment.getFinalArchive();
        return archive;
    }

    /**
     * RF-12839
     */
    @Test
    @Category(Smoke.class)
    public void check_tab_switch() {
        browser.get(contextPath.toExternalForm() + "index.jsf");

        guardAjax(tabs.get(1)).click();
        Assert.assertTrue(out.getText().contains("begin"));
//        Assert.assertTrue(out.getText().contains("tabpanel_complete"));
//        Assert.assertTrue(out.getText().contains("beforedomupdate"));

        // Assert the oncomplete on the tab does work
        Assert.assertTrue(out.getText().contains("tab1_complete"));

    }

    /**
     * RF-12969
     */
    @Test
    public void check_click_active_tab() {
        browser.get(contextPath.toExternalForm() + "index.jsf");
        WebElement activeTab = tabTestHelper.getActiveTab(tabPanel);
        guardAjax(activeTab).click();
        Assert.assertEquals(null, body.getAttribute("JSError"));
    }

    @Test
    public void check_tab_execute() {
        browser.get(contextPath.toExternalForm() + "index.jsf");

        inputText.sendKeys("abcd");
        guardAjax(tabs.get(1)).click();
        Assert.assertEquals("abcd", outputText.getText());
    }


    private static void addIndexPage(FrameworkDeployment deployment) {
        FaceletAsset p = new FaceletAsset();

        p.head("<script type='text/javascript'>");
        p.head("    window.onerror=function(msg) { ");
        p.head("        $('body').attr('JSError',msg);");
        p.head("    }");
        p.head("</script>");

        p.body("<h:form id='myForm'>");
        p.body("<r:tabPanel id='tabPanel' ");
        p.body("               onbegin='$(\"#out\").append(\"begin \\n\")'");
        p.body("               oncomplete='$(\"#out\").append(\"tabpanel_complete \\n\")'");
        p.body("               onbeforedomupdate='$(\"#out\").append(\"beforedomupdate \\n\")'>");
        p.body("    <r:tab id='tab0' name='tab0' header='tab0 header' ");
        p.body("               oncomplete='$(\"#out\").append(\"tab0_complete \\n\")'>");
        p.body("        content of tab 1");
        p.body("    </r:tab>");
        p.body("    <r:tab id='tab1' name='tab1' header='tab1 header' ");
        p.body("               execute='inputText'");
        p.body("               oncomplete='$(\"#out\").append(\"tab1_complete \\n\")'>");
        p.body("        content of tab 2");
        p.body("        <h:outputText id = 'outputText' value='#{simpleBean.string}' />");
        p.body("    </r:tab>");
        p.body("</r:tabPanel> ");
        p.body("<h:inputText id = 'inputText' value='#{simpleBean.string}' />");
        p.body("<div id='out'></div>");
        p.body("</h:form>");

        deployment.archive().addAsWebResource(p, "index.xhtml");
    }

}

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

import java.net.URL;
import java.util.List;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.deployment.FrameworkDeployment;
import org.richfaces.shrinkwrap.descriptor.FaceletAsset;
import org.richfaces.ui.tabPanel.model.TabBean;
import org.richfaces.ui.tabPanel.model.TabPanelBean;

import category.Smoke;

@RunAsClient
@RunWith(Arquillian.class)
public class ITForEachTabTest {

    @Drone
    private WebDriver browser;

    @ArquillianResource
    private URL contextPath;

    @FindBy(id = "myForm:tabPanel")
    private WebElement tabPanel;

    @FindBy(className = "rf-tab-hdr-inact")
    private List<WebElement> tabs;

    @FindBy(id = "myForm:ajaxCreateTabButton")
    private WebElement ajaxCreateTabButton;

    private DynamicTabTestHelper tabTestHelper = new DynamicTabTestHelper();

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        FrameworkDeployment deployment = new FrameworkDeployment(ITForEachTabTest.class);
        deployment.archive().addClass(TabBean.class);
        deployment.archive().addClass(TabPanelBean.class);

        addIndexPage(deployment);

        return deployment.getFinalArchive();
    }

    @Test
    @Category(Smoke.class)
    public void check_tab_switch() {
        browser.get(contextPath.toExternalForm() + "index.jsf");
        tabTestHelper.check_tab_switch(tabPanel, tabs, ajaxCreateTabButton);
    }

    @Test
    public void check_row_removal() throws InterruptedException {
        browser.get(contextPath.toExternalForm());
        tabTestHelper.check_row_removal(tabPanel, tabs, ajaxCreateTabButton);
    }

    private static void addIndexPage(FrameworkDeployment deployment) {
        FaceletAsset p = new FaceletAsset();
        p.body("<h:form id='myForm'>");
        p.body("<r:tabPanel id='tabPanel'>");
        p.body("    <r:tab id='tab0' name='tab0' header='tab0 header'>content of tab 0</r:tab>");
        p.body("    <r:tab id='tab1' name='tab1' header='tab1 header' disabled='true'>content of tab 1</r:tab>");
        p.body("    <r:tab id='tab2' name='tab2' header='tab2 header'>content of tab 2</r:tab>");
        p.body("    <c:forEach items='#{tabPanelBean.tabBeans}' var='newTab'>");
        p.body("        <r:tab id='#{newTab.tabId}' name='#{newTab.tabName}' render='tabPanel'>");
        p.body("            <f:facet name='header'>");
        p.body("                <h:outputText value='#{newTab.tabHeader} ' />");
        p.body("                <h:commandLink value='[x]' rendered='#{newTab.closable}' onclick='var event = arguments[0] || window.event; removeTab(\"#{newTab.tabId}\"); event.stopPropagation(); return false;' />");
        p.body("            </f:facet>");
        p.body("            #{newTab.tabContentText}");
        p.body("        </r:tab>");
        p.body("    </c:forEach>");

        p.body("</r:tabPanel> ");

        p.body("<r:jsFunction id='jsFunction' name='removeTab' action='#{tabPanelBean.removeTab}' render='myForm:tabPanel' >");
        p.body("    <r:param name='removeTabId'/>");
        p.body("</r:jsFunction>");

        p.body("<r:commandButton id='ajaxCreateTabButton' value='[ajax] Create tab' render='tabPanel' actionListener='#{tabPanelBean.generateNewTab}' />");
        p.body("</h:form>");

        deployment.archive().addAsWebResource(p, "index.xhtml");
    }

}

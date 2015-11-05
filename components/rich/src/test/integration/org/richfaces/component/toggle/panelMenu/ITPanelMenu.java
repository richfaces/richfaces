/*
 * JBoss, Home of Professional Open Source
 * Copyright 2014, Red Hat, Inc. and individual contributors
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
package org.richfaces.component.toggle.panelMenu;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.integration.RichDeployment;
import org.richfaces.shrinkwrap.descriptor.FaceletAsset;

import com.google.common.base.Predicate;

@RunWith(Arquillian.class)
@RunAsClient
public class ITPanelMenu {

    @Drone
    private WebDriver browser;
    @ArquillianResource
    private URL contextPath;
    @ArquillianResource
    private JavascriptExecutor executor;

    @FindBy(css = "[id$='disabledGroup:hdr']")
    private WebElement disabledGroupHeader;
    @FindBy(id = "disabledGroup")
    private WebElement disabledGroup;
    @FindBy(className = "disabled")
    private WebElement disabledMenu;
    @FindBy(className = "enabled")
    private WebElement enabledMenu;
    @FindBy(id = "current")
    private WebElement currentItemElement;

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        RichDeployment deployment = new RichDeployment(ITPanelMenu.class);
        deployment.archive()
            .addClasses(PanelMenuBean.class);
        addDisabledMenuItemPage(deployment);
        addDisabledMenuGroupPage(deployment);
        addDisabledMenuGroupPageClient(deployment);
        addDisabledMenuGroupPageExpanded(deployment);
        addDisabledMenuPage(deployment);

        return deployment.getFinalArchive();
    }

    /**
     * RF-13358
     */
    @Test
    public void test_disabled_menu() {
        browser.get(contextPath.toString() + "disabled-menu.jsf");
        waitUntilOnLoadJavaScriptIsExecuted();

        assertEquals(PanelMenuBean.ITEM_DEFAULT, currentItemElement.getText());
        Graphene.guardAjax(disabledMenu).click();
        assertEquals(PanelMenuBean.ITEM_DEFAULT, currentItemElement.getText());
    }

    /**
     * RF-12813
     */
    @Test
    public void test_disabled_menu_item() {
        browser.get(contextPath.toString() + "disabled-menu-item.jsf");
        waitUntilOnLoadJavaScriptIsExecuted();

        assertEquals(PanelMenuBean.ITEM_DEFAULT, currentItemElement.getText());
        Graphene.guardAjax(disabledMenu).click();
        assertEquals(PanelMenuBean.ITEM_DEFAULT, currentItemElement.getText());
    }

    @Test
    public void test_enabled_menu() {
        browser.get(contextPath.toString() + "disabled-menu.jsf");
        waitUntilOnLoadJavaScriptIsExecuted();

        assertEquals(PanelMenuBean.ITEM_DEFAULT, currentItemElement.getText());
        Graphene.guardAjax(enabledMenu).click();
        assertEquals(PanelMenuBean.ITEM_CHANGED, currentItemElement.getText());
    }

    @Test
    public void test_enabled_menu_group() {
        browser.get(contextPath.toString() + "disabled-menu-group-expanded.jsf");
        waitUntilOnLoadJavaScriptIsExecuted();

        assertEquals(PanelMenuBean.ITEM_DEFAULT, currentItemElement.getText());
        Graphene.guardAjax(enabledMenu).click();
        assertEquals(PanelMenuBean.ITEM_CHANGED, currentItemElement.getText());
    }

    @Test
    public void test_enabled_menu_item() {
        browser.get(contextPath.toString() + "disabled-menu-item.jsf");
        waitUntilOnLoadJavaScriptIsExecuted();

        assertEquals(PanelMenuBean.ITEM_DEFAULT, currentItemElement.getText());
        Graphene.guardAjax(enabledMenu).click();
        assertEquals(PanelMenuBean.ITEM_CHANGED, currentItemElement.getText());
    }

    @Test
    public void test_expansion_of_client_mode_disabled_menu_group() {
        browser.get(contextPath.toString() + "disabled-menu-group-client.jsf");
        waitUntilOnLoadJavaScriptIsExecuted();

        Graphene.guardNoRequest(disabledGroupHeader).click();
        assertNotEquals("disabled menu group in client mode should have menu item children", 0, disabledGroup.findElements(By.className("rf-pm-itm")).size());
    }

    /**
     * RF-13358
     */
    @Test
    public void test_expansion_of_disabled_menu_group() {
        browser.get(contextPath.toString() + "disabled-menu-group.jsf");
        waitUntilOnLoadJavaScriptIsExecuted();

        Graphene.guardAjax(disabledGroupHeader).click();
        assertEquals("disabled menu group should have no menu item children", 0, disabledGroup.findElements(By.className("rf-pm-itm")).size());
    }

    /**
     * RF-13358
     */
    @Test
    public void test_item_in_expanded_disabled_menu_group() {
        browser.get(contextPath.toString() + "disabled-menu-group-expanded.jsf");
        waitUntilOnLoadJavaScriptIsExecuted();

        assertEquals(PanelMenuBean.ITEM_DEFAULT, currentItemElement.getText());
        Graphene.guardAjax(disabledMenu).click();
        assertEquals(PanelMenuBean.ITEM_DEFAULT, currentItemElement.getText());
    }

    /**
     * Wait until JS function (hack for enabling the disabled menu) binded to 'onload' event is executed
     */
    private void waitUntilOnLoadJavaScriptIsExecuted() {
        Graphene.waitAjax().until(new Predicate<WebDriver>() {
            @Override
            public boolean apply(WebDriver t) {
                return (Boolean) executor.executeScript("return enableMenuWasCalled;");
            }
        });
    }

    private static void addDisabledMenuGroupPage(RichDeployment deployment) {
        FaceletAsset p = new FaceletAsset();

        //  Re-enable the disabled javascript with this custom javascript call
        p.body("<script type='text/javascript'>");
        p.body("    var enableMenuWasCalled=false;");
        p.body("    function enableMenu() {");
        p.body("        new RichFaces.ui.PanelMenuGroup('disabledGroup',{'collapseEvent':'click','unselectable':false,'selectable':false,'name':'disabledGroup','ajax':{'incId':'1'} , 'expanded':false,'expandEvent':'click','disabled':false,'mode':'ajax'} )");
        p.body("        enableMenuWasCalled=true;");
        p.body("    }");
        p.body("    jQuery(enableMenu);");
        p.body("</script>");

        p.form("<rich:panelMenu itemMode='ajax' groupMode='ajax'>");
        p.form("    <rich:panelMenuGroup id='enabledGroup' label='Group 1' expanded='true'>");
        p.form("        <rich:panelMenuItem id='enabledMenu' label='Item 1' styleClass='enabled' name='Item_1' action='#{panelMenuBean.doAction}' render='current'/>");
        p.form("    </rich:panelMenuGroup>");
        p.form("    <rich:panelMenuGroup id='disabledGroup' disabled='true' label='Group 2'>");
        p.form("        <rich:panelMenuItem id='disabledMenu' label='Item 2' styleClass='disabled' name='Item_2' action='#{panelMenuBean.doAction}' render='current'/>");
        p.form("    </rich:panelMenuGroup>");
        p.form("</rich:panelMenu>");
        p.form("<br/>");
        p.form("<a4j:outputPanel ajaxRendered='true'>");
        p.form("    current item: <h:outputText id='current' value='#{panelMenuBean.current}' />");
        p.form("</a4j:outputPanel>");

        deployment.archive().addAsWebResource(p, "disabled-menu-group.xhtml");
    }

    private static void addDisabledMenuGroupPageClient(RichDeployment deployment) {
        FaceletAsset p = new FaceletAsset();

        //  Re-enable the disabled javascript with this custom javascript call
        p.body("<script type='text/javascript'>");
        p.body("    var enableMenuWasCalled=false;");
        p.body("    function enableMenu() {");
        p.body("        new RichFaces.ui.PanelMenuGroup('disabledGroup',{'collapseEvent':'click','unselectable':false,'selectable':false,'name':'disabledGroup','ajax':{'incId':'1'} , 'expanded':false,'expandEvent':'click','disabled':false,'mode':'client'} )");
        p.body("        enableMenuWasCalled=true;");
        p.body("    }");
        p.body("    jQuery(enableMenu);");
        p.body("</script>");

        p.form("<rich:panelMenu itemMode='ajax' groupMode='ajax'>");
        p.form("    <rich:panelMenuGroup id='enabledGroup' label='Group 1' expanded='true'>");
        p.form("        <rich:panelMenuItem id='enabledMenu' label='Item 1' styleClass='enabled' name='Item_1' action='#{panelMenuBean.doAction}' render='current'/>");
        p.form("    </rich:panelMenuGroup>");
        p.form("    <rich:panelMenuGroup id='disabledGroup' disabled='true' label='Group 2' mode='client'>");
        p.form("        <rich:panelMenuItem id='disabledMenu' label='Item 2' styleClass='disabled' name='Item_2' action='#{panelMenuBean.doAction}' render='current'/>");
        p.form("    </rich:panelMenuGroup>");
        p.form("</rich:panelMenu>");
        p.form("<br/>");
        p.form("<a4j:outputPanel ajaxRendered='true'>");
        p.form("    current item: <h:outputText id='current' value='#{panelMenuBean.current}' />");
        p.form("</a4j:outputPanel>");

        deployment.archive().addAsWebResource(p, "disabled-menu-group-client.xhtml");
    }

    private static void addDisabledMenuGroupPageExpanded(RichDeployment deployment) {
        FaceletAsset p = new FaceletAsset();

        //  Re-enable the disabled javascript with this custom javascript call
        p.body("<script type='text/javascript'>");
        p.body("    function enableMenu() {");
        p.body("        new RichFaces.ui.PanelMenuGroup('disabledGroup',{'collapseEvent':'click','unselectable':false,'selectable':false,'name':'disabledGroup','ajax':{'incId':'1'} , 'expanded':true,'expandEvent':'click','disabled':false,'mode':'ajax'} )");
        p.body("        enableMenuWasCalled=true;");
        p.body("    }");
        p.body("    jQuery(enableMenu);");
        p.body("</script>");

        p.form("<rich:panelMenu itemMode='ajax' groupMode='ajax'>");
        p.form("    <rich:panelMenuGroup id='enabledGroup' label='Group 1' expanded='true'>");
        p.form("        <rich:panelMenuItem id='enabledMenu' label='Item 1' styleClass='enabled' name='Item_1' action='#{panelMenuBean.doAction}' render='current'/>");
        p.form("    </rich:panelMenuGroup>");
        p.form("    <rich:panelMenuGroup id='disabledGroup' disabled='true' label='Group 2' expanded='true'>");
        p.form("        <rich:panelMenuItem id='disabledMenu' label='Item 2' styleClass='disabled' name='Item_2' action='#{panelMenuBean.doAction}' render='current'/>");
        p.form("    </rich:panelMenuGroup>");
        p.form("</rich:panelMenu>");
        p.form("<br/>");
        p.form("<a4j:outputPanel ajaxRendered='true'>");
        p.form("    current item: <h:outputText id='current' value='#{panelMenuBean.current}' />");
        p.form("</a4j:outputPanel>");

        deployment.archive().addAsWebResource(p, "disabled-menu-group-expanded.xhtml");
    }

    private static void addDisabledMenuItemPage(RichDeployment deployment) {
        FaceletAsset p = new FaceletAsset();

        //  Re-enable the disabled javascript with this custom javascript call
        p.body("<script type='text/javascript'>");
        p.body("    var enableMenuWasCalled=false;");
        p.body("    function enableMenu() {");
        p.body("        new RichFaces.ui.PanelMenuItem('disabledMenu',{'unselectable':false,'selectable':true,'name':'Item_2','ajax':{'incId':'1'} , 'disabled':false,'mode':'ajax'} )");
        p.body("        enableMenuWasCalled=true;");
        p.body("    }");
        p.body("    jQuery(enableMenu);");
        p.body("</script>");

        p.form("<rich:panelMenu itemMode='ajax' groupMode='ajax'>");
        p.form("    <rich:panelMenuGroup label='Group' expanded='true'>");
        p.form("        <rich:panelMenuItem id='enabledMenu' label='Item 1' styleClass='enabled' name='Item_1' action='#{panelMenuBean.doAction}' render='current'/>");
        p.form("        <rich:panelMenuItem id='disabledMenu' label='Item 2' styleClass='disabled' disabled='true' name='Item_2' action='#{panelMenuBean.doAction}' render='current'/>");
        p.form("    </rich:panelMenuGroup>");
        p.form("</rich:panelMenu>");
        p.form("<br/>");
        p.form("current item: <h:outputText id='current' value='#{panelMenuBean.current}' />");

        deployment.archive().addAsWebResource(p, "disabled-menu-item.xhtml");
    }

    private static void addDisabledMenuPage(RichDeployment deployment) {
        FaceletAsset p = new FaceletAsset();

        //  Re-enable the disabled javascript with this custom javascript call
        p.body("<script type='text/javascript'>");
        p.body("    var enableMenuWasCalled=false;");
        p.body("    function enableMenu() {");
        p.body("        new RichFaces.ui.PanelMenu('disabledMenu',{'bubbleSelection':true,'ajax':{'incId':'1'} ,'expandSingle':true,'disabled':false} )");
        p.body("        new RichFaces.ui.PanelMenuGroup('disabledMenuGroup',{'collapseEvent':'click','unselectable':false,'selectable':false,'name':'disabledMenu','ajax':{'incId':'1'} , 'expanded':true,'expandEvent':'click','disabled':false,'mode':'ajax'} )");
        p.body("        new RichFaces.ui.PanelMenuItem('disabledMenuItem',{'unselectable':false,'selectable':true,'name':'Item_2','ajax':{'incId':'1'} , 'disabled':false,'mode':'ajax'} )");
        p.body("        enableMenuWasCalled=true;");
        p.body("    }");
        p.body("    jQuery(enableMenu);");
        p.body("</script>");

        p.form("<rich:panelMenu itemMode='ajax' groupMode='ajax'>");
        p.form("    <rich:panelMenuGroup label='Group 1' expanded='true'>");
        p.form("        <rich:panelMenuItem label='Item 1' styleClass='enabled' name='Item_1' mode='ajax' action='#{panelMenuBean.doAction}' render='current'/>");
        p.form("    </rich:panelMenuGroup>");
        p.form("</rich:panelMenu>");
        p.form("<rich:panelMenu id='disabledMenu' itemMode='ajax' disabled='true' groupMode='ajax'>");
        p.form("    <rich:panelMenuGroup id='disabledMenuGroup' label='Group 2' expanded='true'>");
        p.form("        <rich:panelMenuItem id='disabledMenuItem' label='Item 2' styleClass='disabled' name='Item_2' action='#{panelMenuBean.doAction}' render='current'/>");
        p.form("    </rich:panelMenuGroup>");
        p.form("</rich:panelMenu>");
        p.form("<br/>");
        p.form("current item: <h:outputText id='current' value='#{panelMenuBean.current}' />");

        deployment.archive().addAsWebResource(p, "disabled-menu.xhtml");
    }
}

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

package org.richfaces.ui.toggle.panelMenu;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.arquillian.warp.Activity;
import org.jboss.arquillian.warp.Warp;
import org.jboss.arquillian.warp.WarpTest;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.deployment.FrameworkDeployment;
import org.richfaces.shrinkwrap.descriptor.FaceletAsset;

import java.net.URL;


@RunWith(Arquillian.class)
@RunAsClient
@WarpTest
public class ITPanelMenu {

    @Drone
    private WebDriver browser;

    @ArquillianResource
    private URL contextPath;

    @Deployment
    public static WebArchive createDeployment() {
        FrameworkDeployment deployment = new FrameworkDeployment(ITPanelMenu.class);
        deployment.archive()
            .addClasses(PanelMenuBean.class, VerifyMenuAction.DidNotOccur.class, VerifyMenuAction.DidNotOccur.class)
            .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
        addDisabledMenuItemPage(deployment);
        addDisabledMenuGroupPage(deployment);
        addDisabledMenuGroupPageClient(deployment);
        addDisabledMenuGroupPageExpanded(deployment);
        addDisabledMenuPage(deployment);

        return deployment.getFinalArchive();
    }

    @FindBy(className ="enabled")
    private WebElement enabledMenu;

    @FindBy(className ="disabled")
    private WebElement disabledMenu;

    @FindBy(id ="disabledGroup")
    private WebElement disabledGroup;

    @Test
    public void test_enabled_menu_item() {
        browser.get(contextPath.toString() + "disabled-menu-item.jsf");
        Warp
                .initiate(new Activity() {
                    public void perform() {
                        Graphene.guardAjax(enabledMenu).click();
                    }
                })
                .inspect(new VerifyMenuAction.DidOccur());

    }

    /**
     * RF-12813
     */
    @Test
    public void test_disabled_menu_item() {
        browser.get(contextPath.toString() + "disabled-menu-item.jsf");
        Warp
                .initiate(new Activity() {
                    public void perform() {
                        Graphene.guardAjax(disabledMenu).click();
                    }
                })
                .inspect(new VerifyMenuAction.DidNotOccur());

    }

    @Test
    public void test_enabled_menu_group() {
        browser.get(contextPath.toString() + "disabled-menu-group-expanded.jsf");
        Warp
                .initiate(new Activity() {
                    public void perform() {
                        Graphene.guardAjax(enabledMenu).click();
                    }
                })
                .inspect(new VerifyMenuAction.DidOccur());

    }

    /**
     * RF-13358
     */
    @Test
    public void test_item_in_expanded_disabled_menu_group() {
        browser.get(contextPath.toString() + "disabled-menu-group-expanded.jsf");
        Warp
                .initiate(new Activity() {
                    public void perform() {
                        Graphene.guardAjax(disabledMenu).click();
                    }
                })
                .inspect(new VerifyMenuAction.DidNotOccur());

    }

    /**
     * RF-13358
     */
    @Test
    public void test_expansion_of_disabled_menu_group() {
        browser.get(contextPath.toString() + "disabled-menu-group.jsf");
        Graphene.guardNoRequest(disabledGroup).click();
        Assert.assertEquals("disabled menu group should have no menu item children", 0, disabledGroup.findElements(By.className("rf-pm-itm")).size());
    }

    @Test
    public void test_expansion_of_client_mode_disabled_menu_group() {
        browser.get(contextPath.toString() + "disabled-menu-group-client.jsf");
        Graphene.guardNoRequest(disabledGroup).click();
        Assert.assertNotEquals("disabled menu group in client mode should have menu item children", 0, disabledGroup.findElements(By.className("rf-pm-itm")).size());
    }

    @Test
    public void test_enabled_menu() {
        browser.get(contextPath.toString() + "disabled-menu.jsf");
        Warp
                .initiate(new Activity() {
                    public void perform() {
                        Graphene.guardAjax(enabledMenu).click();
                    }
                })
                .inspect(new VerifyMenuAction.DidOccur());

    }

    /**
     * RF-13358
     */
    @Test
    public void test_disabled_menu() {
        browser.get(contextPath.toString() + "disabled-menu.jsf");
        Warp
                .initiate(new Activity() {
                    public void perform() {
                        Graphene.guardAjax(disabledMenu).click();
                    }
                })
                .inspect(new VerifyMenuAction.DidNotOccur());

    }

    private static void addDisabledMenuItemPage(FrameworkDeployment deployment) {
        FaceletAsset p = new FaceletAsset();

        //  Re-enable the disabled javascript with this custom javascript call
        p.body("<script type='text/javascript'> ");
        p.body("    function enableMenu() { ");
        p.body("        new RichFaces.ui.PanelMenuItem('disabledMenu',{'unselectable':false,'selectable':true,'name':'Item_2','ajax':{'incId':'1'} , 'disabled':false,'mode':'ajax'} )");
        p.body("    } ");
        p.body("    jQuery(enableMenu); ");
        p.body("</script>");

        p.form("<r:panelMenu itemMode='ajax' groupMode='ajax'>");
        p.form("    <r:panelMenuGroup label='Group' expanded='true'> ");
        p.form("        <r:panelMenuItem id='enabledMenu' label='Item 1' styleClass='enabled' name='Item_1' action='#{panelMenuBean.doAction}'  />");
        p.form("        <r:panelMenuItem id='disabledMenu' label='Item 2' styleClass='disabled' disabled='true' name='Item_2' action='#{panelMenuBean.doAction}'  />");
        p.form("    </r:panelMenuGroup> ");
        p.form("</r:panelMenu>");

        deployment.archive().addAsWebResource(p, "disabled-menu-item.xhtml");
    }

    private static void addDisabledMenuGroupPage(FrameworkDeployment deployment) {
        FaceletAsset p = new FaceletAsset();

        //  Re-enable the disabled javascript with this custom javascript call
        p.body("<script type='text/javascript'> ");
        p.body("    function enableMenu() { ");
        p.body("        new RichFaces.ui.PanelMenuGroup('disabledGroup',{'collapseEvent':'click','unselectable':false,'selectable':false,'name':'disabledGroup','ajax':{'incId':'1'} , 'expanded':false,'expandEvent':'click','disabled':false,'mode':'ajax'} ) ");
        p.body("    } ");
        p.body("    jQuery(enableMenu); ");
        p.body("</script>");

        p.form("<r:panelMenu itemMode='ajax' groupMode='ajax'>");
        p.form("    <r:panelMenuGroup id='enabledGroup' label='Group 1' expanded='true'> ");
        p.form("        <r:panelMenuItem id='enabledMenu' label='Item 1' styleClass='enabled' name='Item_1' action='#{panelMenuBean.doAction}'  />");
        p.form("    </r:panelMenuGroup> ");
        p.form("    <r:panelMenuGroup id='disabledGroup' disabled='true' label='Group 2'> ");
        p.form("        <r:panelMenuItem id='disabledMenu' label='Item 2' styleClass='disabled' name='Item_2' action='#{panelMenuBean.doAction}'  />");
        p.form("    </r:panelMenuGroup> ");
        p.form("</r:panelMenu>");

        deployment.archive().addAsWebResource(p, "disabled-menu-group.xhtml");
    }

    private static void addDisabledMenuGroupPageClient(FrameworkDeployment deployment) {
        FaceletAsset p = new FaceletAsset();

        //  Re-enable the disabled javascript with this custom javascript call
        p.body("<script type='text/javascript'> ");
        p.body("    function enableMenu() { ");
        p.body("        new RichFaces.ui.PanelMenuGroup('disabledGroup',{'collapseEvent':'click','unselectable':false,'selectable':false,'name':'disabledGroup','ajax':{'incId':'1'} , 'expanded':false,'expandEvent':'click','disabled':false,'mode':'clien'} ) ");
        p.body("    } ");
        p.body("    jQuery(enableMenu); ");
        p.body("</script>");

        p.form("<r:panelMenu itemMode='ajax' groupMode='ajax'>");
        p.form("    <r:panelMenuGroup id='enabledGroup' label='Group 1' expanded='true'> ");
        p.form("        <r:panelMenuItem id='enabledMenu' label='Item 1' styleClass='enabled' name='Item_1' action='#{panelMenuBean.doAction}'  />");
        p.form("    </r:panelMenuGroup> ");
        p.form("    <r:panelMenuGroup id='disabledGroup' disabled='true' label='Group 2' mode='client'> ");
        p.form("        <r:panelMenuItem id='disabledMenu' label='Item 2' styleClass='disabled' name='Item_2' action='#{panelMenuBean.doAction}'  />");
        p.form("    </r:panelMenuGroup> ");
        p.form("</r:panelMenu>");

        deployment.archive().addAsWebResource(p, "disabled-menu-group-client.xhtml");
    }

    private static void addDisabledMenuGroupPageExpanded(FrameworkDeployment deployment) {
        FaceletAsset p = new FaceletAsset();

        //  Re-enable the disabled javascript with this custom javascript call
        p.body("<script type='text/javascript'> ");
        p.body("    function enableMenu() { ");
        p.body("        new RichFaces.ui.PanelMenuGroup('disabledGroup',{'collapseEvent':'click','unselectable':false,'selectable':false,'name':'disabledGroup','ajax':{'incId':'1'} , 'expanded':true,'expandEvent':'click','disabled':false,'mode':'ajax'} ) ");
        p.body("    } ");
        p.body("    jQuery(enableMenu); ");
        p.body("</script>");

        p.form("<r:panelMenu itemMode='ajax' groupMode='ajax'>");
        p.form("    <r:panelMenuGroup id='enabledGroup' label='Group 1' expanded='true'> ");
        p.form("        <r:panelMenuItem id='enabledMenu' label='Item 1' styleClass='enabled' name='Item_1' action='#{panelMenuBean.doAction}'  />");
        p.form("    </r:panelMenuGroup> ");
        p.form("    <r:panelMenuGroup id='disabledGroup' disabled='true' label='Group 2' expanded='true'> ");
        p.form("        <r:panelMenuItem id='disabledMenu' label='Item 2' styleClass='disabled' name='Item_2' action='#{panelMenuBean.doAction}'  />");
        p.form("    </r:panelMenuGroup> ");
        p.form("</r:panelMenu>");

        deployment.archive().addAsWebResource(p, "disabled-menu-group-expanded.xhtml");
    }

    private static void addDisabledMenuPage(FrameworkDeployment deployment) {
        FaceletAsset p = new FaceletAsset();

        //  Re-enable the disabled javascript with this custom javascript call
        p.body("<script type='text/javascript'> ");
        p.body("    function enableMenu() { ");
        p.body("        new RichFaces.ui.PanelMenu('disabledMenu',{'bubbleSelection':true,'ajax':{'incId':'1'} ,'expandSingle':true,'disabled':false} ) ");
        p.body("        new RichFaces.ui.PanelMenuGroup('disabledMenuGroup',{'collapseEvent':'click','unselectable':false,'selectable':false,'name':'disabledMenu','ajax':{'incId':'1'} , 'expanded':true,'expandEvent':'click','disabled':false,'mode':'ajax'} ) ");
        p.body("        new RichFaces.ui.PanelMenuItem('disabledMenuItem',{'unselectable':false,'selectable':true,'name':'Item_2','ajax':{'incId':'1'} , 'disabled':false,'mode':'ajax'} )");
        p.body("    } ");
        p.body("    jQuery(enableMenu); ");
        p.body("</script>");

        p.form("<r:panelMenu id='enabledMenu' itemMode='ajax' groupMode='ajax'>");
        p.form("    <r:panelMenuGroup label='Group 1' expanded='true'> ");
        p.form("        <r:panelMenuItem label='Item 1' styleClass='enabled' name='Item_1' action='#{panelMenuBean.doAction}'  />");
        p.form("    </r:panelMenuGroup> ");
        p.form("</r:panelMenu>");
        p.form("<r:panelMenu id='disabledMenu' itemMode='ajax' disabled='true' groupMode='ajax'>");
        p.form("    <r:panelMenuGroup id='disabledMenuGroup' label='Group 2' expanded='true'> ");
        p.form("        <r:panelMenuItem id='disabledMenuItem' label='Item 2' styleClass='disabled' name='Item_2' action='#{panelMenuBean.doAction}'  />");
        p.form("    </r:panelMenuGroup> ");
        p.form("</r:panelMenu>");

        deployment.archive().addAsWebResource(p, "disabled-menu.xhtml");
    }

}
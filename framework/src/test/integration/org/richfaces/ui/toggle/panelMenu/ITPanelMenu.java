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
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.arquillian.warp.Activity;
import org.jboss.arquillian.warp.Warp;
import org.jboss.arquillian.warp.WarpTest;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
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
            .addClasses(PanelMenuBean.class, VerifyMenuAction.class)
            .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
        addDisabledMenuItemPage(deployment);


        return deployment.getFinalArchive();
    }

    @FindBy(className ="enabled")
    private WebElement enabledMenu;

    @FindBy(className ="disabled")
    private WebElement disabledMenu;

    @Test
    public void test_enabled_menu_item() {
        browser.get(contextPath.toString() + "disabled-menu-item.jsf");
        Warp
                .initiate(new Activity() {
                    public void perform() {
                        enabledMenu.click();
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
                        disabledMenu.click();
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

}
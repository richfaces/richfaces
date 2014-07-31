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

package org.richfaces.component.menu;

import static org.jboss.arquillian.graphene.Graphene.guardNoRequest;

import java.io.File;
import java.net.URL;

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
import org.richfaces.integration.RichDeployment;

@RunWith(Arquillian.class)
@RunAsClient
@WarpTest
public class ITDropDownMenuItem {

    @Drone
    private WebDriver browser;

    @ArquillianResource
    private URL contextPath;

    @Deployment
    public static WebArchive createDeployment() {
        RichDeployment deployment = new RichDeployment(ITDropDownMenuItem.class);
        deployment.archive()
            .addClasses(DropDownMenuBean.class, VerifyMenuAction.class)
            .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
            .addAsWebResource(new File("src/test/resources/org/richfaces/renderkit/html/menuItem_serverMode.xhtml"), "menuItem_serverMode.xhtml")
            .addAsWebResource(new File("src/test/resources/org/richfaces/renderkit/html/menuItem_ajaxMode.xhtml"), "menuItem_ajaxMode.xhtml")
            .addAsWebResource(new File("src/test/resources/org/richfaces/renderkit/html/menuItem_clientMode.xhtml"), "menuItem_clientMode.xhtml");

        return deployment.getFinalArchive();
    }

    @FindBy(className ="rf-ddm-itm-lbl")
    private WebElement menuItem;

    @Test
    public void testServer() {
        browser.get(contextPath.toString() + "menuItem_serverMode.jsf");
        verifyOnServer();
    }

    @Test
    public void testAjax() {
        browser.get(contextPath.toString() + "menuItem_ajaxMode.jsf");
        verifyOnServer();
    }

    @Test
    public void testClick() {
        browser.get(contextPath.toString() + "menuItem_clientMode.jsf");
        guardNoRequest(menuItem).click();
    }

    private void verifyOnServer() {
        Warp
        .initiate(new Activity() {
            public void perform() {
                menuItem.click();
            }
        })
        .inspect(new VerifyMenuAction());
    }
}
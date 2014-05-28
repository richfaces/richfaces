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

package org.richfaces.component.repeat;

import static org.jboss.arquillian.graphene.Graphene.guardAjax;
import static org.junit.Assert.assertEquals;

import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.arquillian.warp.WarpTest;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.richfaces.integration.CoreUIDeployment;

@RunWith(Arquillian.class)
@WarpTest
@RunAsClient
public class ITNestedRepeat {

    @Drone
    private WebDriver browser;

    @ArquillianResource
    private URL contextPath;

    @Deployment
    public static WebArchive deployment() {
        CoreUIDeployment deployment = new CoreUIDeployment(ITNestedRepeat.class);

        deployment.archive()
            .addClasses(NestedDataBean.class)
            .addAsWebResource(ITNestedRepeat.class.getResource("NestedRepeatTest.xhtml"), "NestedRepeatTest.xhtml")
            .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");

        return deployment.getFinalArchive();
    }

    @Test
    public void testRendering() {
        browser.get(contextPath + "NestedRepeatTest.jsf");

        for (int i = 0; i < 3; i++) {
            WebElement input = browser.findElement(By.id("form:outer:" + i + ":inner:0:input"));
            input.sendKeys(Integer.toString(i));
        }

        WebElement ajax = browser.findElement(By.id("form:ajax"));
        guardAjax(ajax).click();

        for (int i = 0; i < 3; i++) {
            WebElement input = browser.findElement(By.id("form:outer:" + i + ":inner:0:input"));
            assertEquals(Integer.toString(i), input.getAttribute("value"));
        }
    }
}

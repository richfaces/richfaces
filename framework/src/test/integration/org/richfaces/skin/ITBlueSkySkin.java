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

package org.richfaces.skin;

import category.Failing;
import com.google.common.base.Function;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.webapp30.WebAppDescriptor;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.deployment.FrameworkDeployment;
import org.richfaces.shrinkwrap.descriptor.FaceletAsset;

import java.net.URL;

@RunAsClient
@RunWith(Arquillian.class)
public class ITBlueSkySkin {

    @Drone
    private WebDriver browser;

    @ArquillianResource
    private URL contextPath;

    @FindBy(className = "rf-p-hdr")
    WebElement panel;

    @FindBy(id = "input")
    WebElement input;

    @FindBy(id = "buttonPlain")
    WebElement buttonPlain;

    @FindBy(id = "buttonDefault")
    WebElement buttonDefault;

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        FrameworkDeployment deployment = new FrameworkDeployment(ITBlueSkySkin.class);
        deployment.webXml(new Function<WebAppDescriptor, WebAppDescriptor>() {
            public WebAppDescriptor apply(WebAppDescriptor input) {

                input.getOrCreateContextParam()
                        .paramName("org.richfaces.skin")
                        .paramValue("blueSky");

                return input;
            };
        });


        addIndexPage(deployment);
        deployment.archive().addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");

        return deployment.getFinalArchive();
    }

    @Test
    public void test_skin() throws InterruptedException {
        browser.get(contextPath.toExternalForm());
        String expectedUrl = "url(" + contextPath.toExternalForm() + "faces/rfRes/buttonBackgroundImage.png?db=eAFjZGBkZOBm!P-f8f-n70Bi37UfDEwAUQgJhA__&ln=org.richfaces.images)";
        Assert.assertEquals(expectedUrl, buttonDefault.getCssValue("background-image"));
    }

    private static void addIndexPage(FrameworkDeployment deployment) {
        FaceletAsset p = new FaceletAsset();
        p.form("<r:panel id='panel' header='Header Text'>Some content ");
        p.form("    <h:inputText id='input' /> ");
        p.form("</r:panel> ");
        p.form("<h:commandButton id='buttonDefault' value = 'Some Button' /> ");
        deployment.archive().addAsWebResource(p, "index.xhtml");
    }
}
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

package org.richfaces.component.ajax;

import static org.jboss.arquillian.graphene.Graphene.guardAjax;
import static org.junit.Assert.assertEquals;

import java.net.URL;
import java.util.List;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.integration.A4JDeployment;
import org.richfaces.shrinkwrap.descriptor.FaceletAsset;

@RunAsClient
@RunWith(Arquillian.class)
public class ITJsfAjaxScriptRendering {

    @Drone
    private WebDriver browser;

    @ArquillianResource
    private URL contextPath;

    @FindBy(id = "input")
    private WebElement input;

    @FindBy(id = "button")
    private WebElement button;

    @FindByJQuery("script[src*='jsf.js']")
    private List<WebElement> jsfJsScripts;

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        A4JDeployment deployment = new A4JDeployment(ITJsfAjaxScriptRendering.class);

        addIndexPage(deployment);

        return deployment.getFinalArchive();
    }

    @Test
    public void test_that_jsf_js_is_rendered_just_once() {
        browser.get(contextPath.toExternalForm());

        assertEquals("there should be one jsf.js script on the page", 1, jsfJsScripts.size());

        guardAjax(button).click();
        assertEquals("there should be still just one jsf.js script on the page", 1, jsfJsScripts.size());

        guardAjax(button).click();
        assertEquals("there should be still just one jsf.js script on the page", 1, jsfJsScripts.size());
    }

    private static void addIndexPage(A4JDeployment deployment) {
        FaceletAsset p = new FaceletAsset();

        p.form("<h:commandLink id='button' value='Click me '>");
        p.form("    <a4j:ajax event='click' render='@form' />");
        p.form("</h:commandLink>");

        deployment.archive().addAsWebResource(p, "index.xhtml");
    }

}

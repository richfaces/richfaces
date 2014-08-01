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

package org.richfaces.integration.partialViewContext;

import static org.jboss.arquillian.graphene.Graphene.waitAjax;

import java.net.URL;

import javax.faces.context.PartialResponseWriter;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.integration.RichDeployment;
import org.richfaces.shrinkwrap.descriptor.FaceletAsset;

/**
 * Tests that the {@link PartialResponseWriter#redirect(String)} writes partial-response correctly
 * for redirected requests (RF-12824)
 */
@RunAsClient
@RunWith(Arquillian.class)
public class ITAjaxRedirection {

    @Drone
    private WebDriver browser;

    @ArquillianResource
    private URL contextPath;

    @FindBy(id = "button")
    private WebElement button;

    @FindBy(tagName = "body")
    private WebElement body;

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        RichDeployment deployment = new RichDeployment(ITAjaxRedirection.class);

        addIndexPage(deployment);
        addRedirectedPage(deployment);

        return deployment.getFinalArchive();
    }

    @Test
    public void test() {
        browser.get(contextPath.toExternalForm());

        button.click();

        waitAjax().until().element(body).text().contains("Redirected");
    }

    private static void addIndexPage(RichDeployment deployment) {
        FaceletAsset p = new FaceletAsset();

        p.head("<h:outputScript name='jsf.js' library='javax.faces' />");
        p.head("<h:outputScript library='org.richfaces' name='jquery.js' />");
        p.head("<h:outputScript library='org.richfaces' name='richfaces.js' />");

        p.form("<h:panelGroup id='panel'>");
        p.form("    <h:commandButton id='button' action='redirected?faces-redirect=true'>");
        p.form("        <f:ajax />");
        p.form("    </h:commandButton>");
        p.form("</h:panelGroup>");

        deployment.archive().addAsWebResource(p, "index.xhtml");
    }

    private static void addRedirectedPage(RichDeployment deployment) {
        FaceletAsset p = new FaceletAsset();

        p.body("Redirected");

        deployment.archive().addAsWebResource(p, "redirected.xhtml");
    }
}

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

import static org.junit.Assert.assertEquals;

import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.richfaces.context.ExtendedPartialViewContext;
import org.richfaces.integration.RichDeployment;
import org.richfaces.shrinkwrap.descriptor.FaceletAsset;

/**
 * Tests partial view updates using {@link ExtendedPartialViewContext}. (RF-13317)
 */
@RunAsClient
@RunWith(Arquillian.class)
public class ITViewScoped {

    @Drone
    private WebDriver browser;

    @ArquillianResource
    private URL contextPath;

    @FindBy(id = "form")
    private Form form;

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        RichDeployment deployment = new RichDeployment(ITViewScoped.class);

        addIndexPage(deployment);

        return deployment.getFinalArchive();
    }

    @Test
    public void test() {
        // given
        browser.get(contextPath.toExternalForm());
        String viewState = form.getViewState();

        // when
        form.setInput("text");

        // then
        form.submit();
        assertEquals("text", form.getInput());
        assertEquals(viewState, form.getViewState());

        form.submit();
        assertEquals("text", form.getInput());
        assertEquals(viewState, form.getViewState());
    }



    private static void addIndexPage(RichDeployment deployment) {
        FaceletAsset p = new FaceletAsset();

        p.head("<h:outputScript name='jsf.js' library='javax.faces' />");
        p.head("<h:outputScript library='org.richfaces' name='jquery.js' />");
        p.head("<h:outputScript library='org.richfaces' name='richfaces.js' />");

        p.body("<h:form id='form'>");
        p.body("    <h:inputText value='#{viewScope.value}' />");
        p.body("    <h:commandButton value='Submit' execute='@form' render='@form' onclick='RichFaces.ajax(this, event, {\"incId\": \"1\"}); return false;' />");
        p.body("</h:form>");

        deployment.archive().addAsWebResource(p, "index.xhtml");
    }
}

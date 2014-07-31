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

import javax.faces.context.PartialViewContext;

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
import org.richfaces.integration.RichDeployment;
import org.richfaces.shrinkwrap.descriptor.FaceletAsset;

/**
 * Tests r:commandButton processing using {@link PartialViewContext}. (RF-12145)
 */
@RunAsClient
@RunWith(Arquillian.class)
public class ITMultipleFormUpdate {

    @Drone
    WebDriver browser;

    @ArquillianResource
    URL contextPath;

    @FindBy(id = "firstForm")
    Form firstForm;

    @FindBy(id = "secondForm")
    Form secondForm;

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        RichDeployment deployment = new RichDeployment(ITMultipleFormUpdate.class);

        addIndexPage(deployment);

        return deployment.getFinalArchive();
    }

    @Test
    public void when_form_is_explicitly_listed_in_render_then_its_ViewState_should_be_updated_after_response() {
        browser.get(contextPath.toExternalForm());
        assertEquals("both forms should have same ViewState", firstForm.getViewState(), secondForm.getViewState());

        String viewState = firstForm.getViewState();
        firstForm.setInput("1");
        firstForm.submit();

        assertEquals("ViewState should not change", viewState, firstForm.getViewState());
        assertEquals("both forms should have same ViewState", firstForm.getViewState(), secondForm.getViewState());
        assertEquals("first form input should be 1", "1", firstForm.getInput());
        assertEquals("second form input should be 1", "1", secondForm.getInput());

        secondForm.setInput("2");
        secondForm.submit();

        assertEquals("ViewState should not change", viewState, secondForm.getViewState());
        assertEquals("both forms should have same ViewState", secondForm.getViewState(), firstForm.getViewState());
        assertEquals("first form input should be 2", "2", firstForm.getInput());
        assertEquals("second form input should be 2", "2", secondForm.getInput());
    }

    private static void addIndexPage(RichDeployment deployment) {
        FaceletAsset p = new FaceletAsset();

        p.head("<h:outputScript name='jsf.js' library='javax.faces' />");
        p.head("<h:outputScript library='org.richfaces' name='jquery.js' />");
        p.head("<h:outputScript library='org.richfaces' name='richfaces.js' />");

        p.body("<h:form id='firstForm'>");
        p.body("    <h:inputText value='#{value}' />");
        p.body("    <h:commandButton value='Submit 1' execute='@form' render='@form :secondForm' onclick='RichFaces.ajax(this, event, {\"incId\": \"1\"}); return false;' />");
        p.body("</h:form>");

        p.body("<h:form id='secondForm'>");
        p.body("    <h:inputText value='#{value}' />");
        p.body("    <h:commandButton value='Submit 2' execute='@form' render='@form :firstForm' onclick='RichFaces.ajax(this, event, {\"incId\": \"1\"}); return false;'  />");
        p.body("</h:form>");

        deployment.archive().addAsWebResource(p, "index.xhtml");
    }
}

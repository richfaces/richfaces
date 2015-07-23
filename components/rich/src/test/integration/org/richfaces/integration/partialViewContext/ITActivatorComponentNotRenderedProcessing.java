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

import static org.jboss.arquillian.graphene.Graphene.guardAjax;
import static org.jboss.arquillian.graphene.Graphene.waitAjax;
import static org.junit.Assert.fail;

import java.net.URL;
import java.util.concurrent.TimeUnit;

import javax.faces.context.PartialViewContext;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.integration.RichDeployment;
import org.richfaces.shrinkwrap.descriptor.FaceletAsset;

import com.google.common.base.Predicate;

/**
 * Tests r:commandButton processing using {@link PartialViewContext}. (RF-12145)
 */
@RunAsClient
@RunWith(Arquillian.class)
@Ignore("https://issues.jboss.org/browse/RF-14095")
public class ITActivatorComponentNotRenderedProcessing {

    @Drone
    WebDriver browser;

    @ArquillianResource
    URL contextPath;

    @FindBy(id = "button")
    WebElement button;

    @FindBy(id = "output")
    WebElement output;

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        RichDeployment deployment = new RichDeployment(ITActivatorComponentNotRenderedProcessing.class);

        addIndexPage(deployment);

        return deployment.getFinalArchive();
    }

    @Test
    public void when_executed_component_is_not_rendered_after_ajax_request_then_its_oncomplete_handler_should_be_executed() {
        browser.get(contextPath.toExternalForm());

        guardAjax(button).click();

        waitAjax().withTimeout(1, TimeUnit.SECONDS).until(new Predicate<WebDriver>() {

            @Override
            public boolean apply(WebDriver input) {
                return (Boolean) ((JavascriptExecutor) browser).executeScript("return !!window.oncompleteEvaluated");
            }
        });
    }

    @Test
    public void when_executed_component_is_not_rendered_after_ajax_request_then_its_render_target_should_be_taken_into_consideration() {
        browser.get(contextPath.toExternalForm());

        guardAjax(button).click();

        waitAjax().until().element(output).text().equalTo("postback");

        try {
            // button should not be present
            button.click();
            fail();
        } catch (NoSuchElementException e) {
        }
    }

    private static void addIndexPage(RichDeployment deployment) {
        FaceletAsset p = new FaceletAsset();

        p.head("<h:outputScript name='jsf.js' library='javax.faces' />");
        p.head("<h:outputScript library='org.richfaces' name='jquery.js' />");
        p.head("<h:outputScript library='org.richfaces' name='richfaces.js' />");

        p.form("<h:panelGroup id='panel'>");
        p.form("    <h:commandButton id='button' onclick='RichFaces.ajax(this, event, {}); return false;' render='panel output' oncomplete='window.oncompleteEvaluated = true' rendered='#{!facesContext.postback}' />");
        p.form("</h:panelGroup>");

        p.form("<h:panelGroup id='output'>#{facesContext.postback ? 'postback' : 'initial'}</h:panelGroup>");

        deployment.archive().addAsWebResource(p, "index.xhtml");
    }
}

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
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.integration.RichDeployment;
import org.richfaces.shrinkwrap.descriptor.FaceletAsset;

/**
 * Tests r:commandButton processing using {@link PartialViewContext}. (RF-12145)
 */
@RunAsClient
@RunWith(Arquillian.class)
public class ITRenderAll {

    @Drone
    private WebDriver browser;
    @ArquillianResource
    private URL contextPath;

    @FindBy(css = "input[id$='appendOncompleteAndRenderAll'][type=submit]")
    private WebElement appendOncompleteAndRenderAllButton;
    @FindBy(id = "counter")
    private WebElement counter;
    @FindBy(css = "input[id$='incrementAndRenderAll'][type=submit]")
    private WebElement incrementAndRenderAllButton;

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        RichDeployment deployment = new RichDeployment(ITRenderAll.class);

        deployment.archive().addClasses(CounterBean.class);

        addIndexPage(deployment);

        return deployment.getFinalArchive();
    }

    @Test
    public void button_replaced_with_render_all_should_keep_working() {
        browser.get(contextPath.toExternalForm());
        assertEquals("0", counter.getText());

        guardAjax(incrementAndRenderAllButton).click();
        assertEquals("counter should be incremented and rendered during render=@all", "1", counter.getText());
        guardAjax(incrementAndRenderAllButton).click();
        assertEquals("counter should be incremented even for second request", "2", counter.getText());
    }

    @Test
    public void render_all_should_replace_all_page_content() {
        browser.get(contextPath.toExternalForm());
        assertEquals("0", counter.getText());
        guardAjax(incrementAndRenderAllButton).click();
        assertEquals("counter should be incremented and rendered during render=@all", "1", counter.getText());
    }

    @Test
    public void test_oncomplete_script_should_be_called_during_render_all() {
        browser.get(contextPath.toExternalForm());
        guardAjax(appendOncompleteAndRenderAllButton).click();
        assertEquals("title updated", "script executed", browser.getTitle());
    }

    private static void addIndexPage(RichDeployment deployment) {
        FaceletAsset p = new FaceletAsset();

        p.head("<h:outputScript name='jsf.js' library='javax.faces' />");
        p.head("<h:outputScript library='org.richfaces' name='jquery.js' />");
        p.head("<h:outputScript library='org.richfaces' name='richfaces.js' />");

        p.form("<h:commandButton id='incrementAndRenderAll' value='increment and render all' render='@all' action='#{counterBean.increment}' onclick='RichFaces.ajax(this, event, {\"incId\": \"1\"}); return false;' />");
        p.form("<br/>");
        p.form("<h:commandButton id='appendOncompleteAndRenderAll' value='append oncomplete and render all' render='@all' action='#{counterBean.appendOncomplete}' onclick='RichFaces.ajax(this, event, {\"incId\": \"1\"}); return false;' />");
        p.form("<br/>");
        p.form("<div id='counter'>#{counterBean.state}</div>");

        deployment.archive().addAsWebResource(p, "index.xhtml");
    }
}

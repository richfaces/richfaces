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
package org.richfaces.integration.partialViewContext;

import static org.hamcrest.Matchers.equalTo;
import static org.jboss.arquillian.graphene.Graphene.guardAjax;
import static org.junit.Assert.assertThat;

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
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.integration.RichDeployment;
import org.richfaces.shrinkwrap.descriptor.FaceletAsset;

/**
 * Tests that the exception in ajax requests ends partial-response correctly (RF-12893)
 */
@RunAsClient
@RunWith(Arquillian.class)
public class ITImplicitAjaxOutputRendering {

    @Drone
    private WebDriver browser;

    @ArquillianResource
    private URL contextPath;

    @FindBy(id = "button")
    private WebElement button;

    @FindBy(id = "buttonLimitRender")
    private WebElement buttonLimitRender;

    @FindBy(id = "panel")
    private WebElement panel;

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        RichDeployment deployment = new RichDeployment(ITImplicitAjaxOutputRendering.class);

        deployment.archive().addClasses(CounterBean.class);

        addIndexPage(deployment);

        return deployment.getFinalArchive();
    }

    @Test
    public void implicitly_rendered_areas_are_rendered_for_each_ajax_request_not_matter_what_render_attribute_specifies() {
        browser.get(contextPath.toExternalForm());
        assertThat(panel.getText(), equalTo("1"));

        guardAjax(button).click();
        assertThat(panel.getText(), equalTo("2"));
    }

    @Test
    public void implicitly_rendered_areas_arent_rendered_when_limitRender_is_true() {
        browser.get(contextPath.toExternalForm());
        assertThat(panel.getText(), equalTo("1"));

        guardAjax(buttonLimitRender).click();
        assertThat(panel.getText(), equalTo("1"));
    }

    private static void addIndexPage(RichDeployment deployment) {
        FaceletAsset p = new FaceletAsset();

        p.form("<a4j:commandButton id='button' render='@this' />");
        p.form("<a4j:commandButton id='buttonLimitRender' render='@this' limitRender='true' />");

        p.form("<a4j:outputPanel id='panel' ajaxRendered='true'>");
        p.form("    #{counterBean.incrementAndGet()}");
        p.form("</a4j:outputPanel>");

        deployment.archive().addAsWebResource(p, "index.xhtml");
    }
}

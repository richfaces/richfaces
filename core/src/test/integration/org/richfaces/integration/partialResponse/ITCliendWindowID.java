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
package org.richfaces.integration.partialResponse;

import static org.jboss.arquillian.warp.client.filter.http.HttpFilters.request;
import static org.junit.Assert.assertFalse;
import static org.richfaces.integration.push.AbstractPushTest.createBasicDeployment;

import java.net.URL;

import javax.servlet.http.HttpServletRequest;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.arquillian.warp.Activity;
import org.jboss.arquillian.warp.Inspection;
import org.jboss.arquillian.warp.Warp;
import org.jboss.arquillian.warp.WarpTest;
import org.jboss.arquillian.warp.servlet.AfterServlet;
import org.jboss.arquillian.warp.servlet.BeforeServlet;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.webapp30.WebAppDescriptor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.deployment.CoreDeployment;
import org.richfaces.shrinkwrap.descriptor.FaceletAsset;

import com.google.common.base.Function;
import com.google.common.base.Predicate;

/**
 *
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 */
@RunWith(Arquillian.class)
@WarpTest
public class ITCliendWindowID {

    @ArquillianResource
    private URL contextPath;

    @Drone
    private WebDriver browser;

    @FindBy
    private WebElement inputText;

    @FindBy(css = "input[name='javax.faces.ClientWindow']")
    private WebElement hiddenFieldWithClientWindowID;

    @Deployment
    public static WebArchive createDeployment() {
        CoreDeployment deployment = createBasicDeployment(ITCliendWindowID.class);

        deployment.webXml(new Function<WebAppDescriptor, WebAppDescriptor>() {
            public WebAppDescriptor apply(WebAppDescriptor webXml) {
                return webXml
                    .createContextParam()
                    .paramName("javax.faces.CLIENT_WINDOW_MODE")
                    .paramValue("url")
                    .up();
            }
        });

        addIndexPage(deployment);
        return deployment.getFinalArchive().addClass(SimpleBean.class);
    }

    @Test
    @RunAsClient
    public void should_include_hidden_field_with_client_window_id() {
        browser.navigate().to(contextPath);
        Graphene.waitModel().until(new Predicate<WebDriver>() {
            private String value;

            @Override
            public boolean apply(WebDriver t) {
                value = hiddenFieldWithClientWindowID.getAttribute("value").trim();
                return !value.isEmpty();
            }

            @Override
            public String toString() {
                return "value to be not empty.";
            }
        });
    }

    @Test
    @RunAsClient
    public void should_include_client_window_id_into_request_header() {
        browser.navigate().to(contextPath);
        Warp
            .initiate(new Activity() {

                @Override
                public void perform() {
                    Graphene.guardAjax(inputText).sendKeys("RichFaces");
                }
            })
            .observe(request().parameter().containsParameter("jfwid"))
            .inspect(new ClientWindowIDAssertion());
    }

    public static class ClientWindowIDAssertion extends Inspection {

        private static final long serialVersionUID = 1L;

        @ArquillianResource
        HttpServletRequest request;

        @BeforeServlet
        public void beforeServlet() throws Exception {
            assertFalse(request.getParameter("jfwid").trim().isEmpty());
        }

        @AfterServlet
        public void afterServlet() throws InterruptedException {
            assertFalse(request.getParameter("jfwid").trim().isEmpty());
        }
    }

    private static void addIndexPage(CoreDeployment deployment) {
        FaceletAsset p = new FaceletAsset();

        p.form("<h:inputText id='inputText' value='#{simpleBean.test}'>");
        p.form("    <a4j:ajax event='keyup' render='out' />");
        p.form("</h:inputText>");

        p.body("<a4j:outputPanel id='out'>");
        p.body("  <a id='link' href='/foo.html'>#{simpleBean.test}</a>");
        p.body("</a4j:outputPanel>");

        deployment.archive().addAsWebResource(p, "index.xhtml");
    }
}

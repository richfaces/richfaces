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
import static org.jboss.arquillian.warp.client.filter.http.HttpFilters.request;
import static org.junit.Assert.assertEquals;

import java.net.URL;

import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import javax.faces.context.PartialViewContext;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.arquillian.warp.Activity;
import org.jboss.arquillian.warp.Inspection;
import org.jboss.arquillian.warp.Warp;
import org.jboss.arquillian.warp.WarpTest;
import org.jboss.arquillian.warp.jsf.BeforePhase;
import org.jboss.arquillian.warp.jsf.Phase;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.context.ExtendedPartialViewContext;
import org.richfaces.integration.UIDeployment;
import org.richfaces.shrinkwrap.descriptor.FaceletAsset;

/**
 * Tests r:commandButton processing using {@link PartialViewContext}. (RF-12145)
 */
@RunAsClient
@WarpTest
@RunWith(Arquillian.class)
public class ITRenderAll {

    @Drone
    private WebDriver browser;

    @ArquillianResource
    private URL contextPath;

    @FindBy(css = "input[type=submit]")
    private WebElement button;

    @FindBy(id = "counter")
    private WebElement counter;

    @Deployment
    public static WebArchive createDeployment() {
        UIDeployment deployment = new UIDeployment(ITRenderAll.class);
        
        deployment.archive().addClasses(CounterBean.class);

        addIndexPage(deployment);

        return deployment.getFinalArchive();
    }

    @Test
    public void render_all_should_replace_all_page_content() {
        browser.get(contextPath.toExternalForm());
        assertEquals("0", counter.getText());

        Warp
            .initiate(new Activity() {
                public void perform() {
                    guardAjax(button).click();
                }
            })
            .group()
                .observe(request().uri().contains("index"))
                .inspect(new IncrementCounter())
            .execute();

        assertEquals("counter should be incremented and rendered during render=@all", "1", counter.getText());
    }

    @Test
    public void button_replaced_with_render_all_should_keep_working() {
        browser.get(contextPath.toExternalForm());
        assertEquals("0", counter.getText());

        Warp
            .initiate(new Activity() {
                public void perform() {
                    guardAjax(button).click();
                }
            })
            .observe(request().uri().contains("index"))
            .inspect(new IncrementCounter());

        assertEquals("counter should be incremented and rendered during render=@all", "1", counter.getText());

        Warp
            .initiate(new Activity() {
                public void perform() {
                    guardAjax(button).click();
                }
            })
            .observe(request().uri().contains("index"))
            .inspect(new IncrementCounter());

        assertEquals("counter should be incremented even for second request", "2", counter.getText());
    }

    public static class IncrementCounter extends Inspection {
        private static final long serialVersionUID = 1L;

        @ManagedProperty("#{counterBean}")
        private CounterBean bean;

        @BeforePhase(Phase.RENDER_RESPONSE)
        public void add_oncomplete() {
            bean.incrementAndGet();
        }
    }

    @Test
    public void test_oncomplete_script_should_be_called_during_render_all() {
        browser.get(contextPath.toExternalForm());

        Warp
            .initiate(new Activity() {
                public void perform() {
                    guardAjax(button).click();
                }
            })
            .group()
                .observe(request().uri().contains("index"))
                .inspect(new Inspection() {
                    private static final long serialVersionUID = 1L;

                    @ArquillianResource
                    private PartialViewContext pvc;

                    @ArquillianResource
                    private FacesContext facesContext;

                    @BeforePhase(Phase.RENDER_RESPONSE)
                    public void add_oncomplete() {
                        ((ExtendedPartialViewContext) pvc).appendOncomplete("document.title = 'script executed'");
                }
            })
            .execute();

        assertEquals("title updated", "script executed", browser.getTitle());
    }

    private static void addIndexPage(UIDeployment deployment) {
        FaceletAsset p = new FaceletAsset();

        p.head("<h:outputScript name='jsf.js' library='javax.faces' />");
        p.head("<h:outputScript library='org.richfaces' name='jquery.js' />");
        p.head("<h:outputScript library='org.richfaces' name='richfaces.js' />");

        p.form("<h:commandButton value='Render All' render='@all' onclick='RichFaces.ajax(this, event, {\"incId\": \"1\"}); return false;' />");

        p.form("<div id='counter'>#{counterBean.state}</div>");

        deployment.archive().addAsWebResource(p, "index.xhtml");
    }
}
